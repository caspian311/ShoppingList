package net.todd.shoppinglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.todd.shoppinglist.service.DataService;

public class MainModel implements IMainModel {
	private final List<IShoppingItemCreatedListener> itemCreatedListeners = new ArrayList<IShoppingItemCreatedListener>();
	private final List<IShoppingItemRemovedListener> itemRemovedListeners = new ArrayList<IShoppingItemRemovedListener>();
	private final List<IShoppingItemChangeListener> itemCheckedListeners = new ArrayList<IShoppingItemChangeListener>();

	private final Map<String, ShoppingItem> allItems = new HashMap<String, ShoppingItem>();
	
	private final DataService dataService;

	public MainModel(DataService dataService) {
		this.dataService = dataService;
	}
	
	@Override
	public void addItemCreatedListener(IShoppingItemCreatedListener listener) {
		this.itemCreatedListeners.add(listener);
	}
	
	@Override
	public void addItemRemovedListener(IShoppingItemRemovedListener listener) {
		itemRemovedListeners.add(listener);
	}
	
	@Override
	public void addItemCheckListener(IShoppingItemChangeListener listener) {
		this.itemCheckedListeners.add(listener);
	}
	
	@Override
	public void createItem(String newItemText) {
		ShoppingItem newItem = new ShoppingItem(UUID.randomUUID().toString(), newItemText, false);
		itemCreated(newItem);
		
		dataService.createNewItem(newItem.getId(), newItem.getValue());
	}

	private void itemCreated(ShoppingItem newItem) {
		allItems.put(newItem.getId(), newItem);
		notifyCreatedListeners(newItem);
	}
	
	private void notifyCreatedListeners(ShoppingItem shoppingItem) {
		for (IShoppingItemCreatedListener itemCreatedListener : itemCreatedListeners) {
			itemCreatedListener.shoppingItemChanged(shoppingItem);
		}
	}
	
	@Override
	public void removeShoppingItem(String itemToRemoveId) {
		itemRemoved(itemToRemoveId);
		
		dataService.removeItem(itemToRemoveId);
	}

	private void itemRemoved(String itemToRemoveId) {
		allItems.remove(itemToRemoveId);
		notifyRemovedListeners(itemToRemoveId);
	}

	private void notifyRemovedListeners(String shoppingItemId) {
		for (IShoppingItemRemovedListener itemRemovedListener : itemRemovedListeners) {
			itemRemovedListener.shoppingItemRemoved(shoppingItemId);
		}
	}
	
	@Override
	public void checkItem(String itemToCheckId) {
		ShoppingItem item = allItems.get(itemToCheckId);
		if (item.isChecked()) {
			item.uncheck();	
		} else {
			item.check();
		}
		
		notifyCheckListeners(item);
		
		dataService.updateItem(item.getId(), item.getValue(), item.isChecked());
	}
	
	private void notifyCheckListeners(ShoppingItem shoppingItem) {
		for (IShoppingItemChangeListener itemCheckedListener : itemCheckedListeners) {
			itemCheckedListener.shoppingItemChanged(shoppingItem);
		}
	}
	
	@Override
	public void mergeItems(Map<String, ShoppingItem> remoteItems) {
		Set<String> itemsToBeRemoved = getItemsToBeRemoved(this.allItems.keySet(), remoteItems.keySet());
		removeItemsToBeRemoved(itemsToBeRemoved);

		Set<String> itemsToBeAdded = getItemsToBeAdded(this.allItems.keySet(), remoteItems.keySet());
		addItemsToBeAdded(itemsToBeAdded, remoteItems);
		
		Set<String> itemsToBeUpdated = getItemsToBeUpdated(this.allItems, remoteItems);
		updateItemsToBeUpdated(itemsToBeUpdated, remoteItems);
	}

	private void updateItemsToBeUpdated(Set<String> itemsToBeUpdated, Map<String, ShoppingItem> remoteItems) {
		for (String id : itemsToBeUpdated) {
			this.allItems.put(id, remoteItems.get(id));
			notifyCheckListeners(this.allItems.get(id));
		}
	}

	private Set<String> getItemsToBeUpdated(Map<String, ShoppingItem> allItems, Map<String, ShoppingItem> remoteItems) {
		Set<String> itemsToBeUpdated = new HashSet<String>();
		for (String id : remoteItems.keySet()) {
			ShoppingItem remoteItem = remoteItems.get(id);
			ShoppingItem localItem = allItems.get(id);
			
			if (!remoteItem.equals(localItem)) {
				itemsToBeUpdated.add(id);
			}
		}
		return itemsToBeUpdated;
	}

	private void removeItemsToBeRemoved(Set<String> itemsToBeRemoved) {
		for (String id : itemsToBeRemoved) {
			itemRemoved(id);
		}
	}

	private void addItemsToBeAdded(Set<String> itemsToBeAdded, Map<String, ShoppingItem> remoteItems) {
		for (String id : itemsToBeAdded) {
			ShoppingItem itemToAdd = remoteItems.get(id);
			itemCreated(itemToAdd);
		}
	}

	private Set<String> getItemsToBeAdded(Set<String> localIds, Set<String> remoteIds) {
		Set<String> idsToBeAdded = new HashSet<String>();
		for (String id : remoteIds) {
			if (!localIds.contains(id)) {
				idsToBeAdded.add(id);
			}
		}
		return idsToBeAdded;
	}

	private Set<String> getItemsToBeRemoved(Set<String> localIds, Set<String> remoteIds) {
		Set<String> idsNotInRemote = new HashSet<String>();
		for (String id : localIds) {
			if (!remoteIds.contains(id)) {
				idsNotInRemote.add(id);
			}
		}
		return idsNotInRemote;
	}
}
