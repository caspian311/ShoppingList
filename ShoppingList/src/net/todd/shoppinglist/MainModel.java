package net.todd.shoppinglist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.todd.shoppinglist.service.AllDataAvailableListener;
import net.todd.shoppinglist.service.DataChangedListener;
import net.todd.shoppinglist.service.DataService;
import net.todd.shoppinglist.service.ShoppingListChange;

public class MainModel implements IMainModel {
	private IShoppingItemChangeListener itemCreatedListener;
	private IShoppingItemChangeListener itemRemovedListener;
	private IShoppingItemChangeListener itemCheckedListener;
	private IShoppingItemChangeListener itemUncheckedListener;

	private final DataService service;
	private final Map<String, ShoppingItem> allItems = new HashMap<String, ShoppingItem>();

	public MainModel(DataService service) {
		this.service = service;
		service.addDataChangedListener(new DataChangedListener() {
			@Override
			public void dataChanged(List<ShoppingListChange> changes) {
				for (ShoppingListChange change : changes) {
					switch(change.getChangeType()) {
					case ShoppingListChange.CREATED:
						itemCreated(change);
						break;
					case ShoppingListChange.DELETED:
						itemDeleted(change);
						break;
					case ShoppingListChange.CHECKED:
						itemChecked(change);
						break;
					case ShoppingListChange.UNCHECKED:
						itemUnchecked(change);
						break;
					}
				}
			}
		});
		service.addGetAllDataListener(new AllDataAvailableListener() {
			@Override
			public void allItemsAvailable(List<ShoppingItem> allItems) {
				for (ShoppingItem shoppingItem : allItems) {
					MainModel.this.allItems.put(shoppingItem.getId(), shoppingItem);
					itemCreatedListener.shoppingItemChanged(shoppingItem);
				}
			}
		});
		service.start();
	}

	@Override
	public void addItemCreatedListener(IShoppingItemChangeListener listener) {
		this.itemCreatedListener = listener;
	}
	
	@Override
	public void addItemRemovedListener(IShoppingItemChangeListener listener) {
		itemRemovedListener = listener;
	}
	
	@Override
	public void addItemCheckedListener(IShoppingItemChangeListener listener) {
		this.itemCheckedListener = listener;
	}
	
	@Override
	public void addItemUncheckedListener(IShoppingItemChangeListener listener) {
		this.itemUncheckedListener = listener;
	}

	@Override
	public void createItem(String newItemText) {
		service.createNewItem(newItemText);
	}
	
	@Override
	public void removeShoppingItem(String itemToRemoveId) {
		service.removeItem(itemToRemoveId);
	}

	@Override
	public void checkItem(String itemToCheckId) {
		if (allItems.get(itemToCheckId).isChecked()) {
			service.uncheckItem(itemToCheckId);
		} else {
			service.checkItem(itemToCheckId);
		}
	}

	private void itemCreated(ShoppingListChange change) {
		ShoppingItem newItem = new ShoppingItem(change.getId(), change.getName(), false);
		itemCreatedListener.shoppingItemChanged(newItem);
	}

	private void itemDeleted(ShoppingListChange change) {
		ShoppingItem itemToRemove = allItems.remove(change.getId());
		itemRemovedListener.shoppingItemChanged(itemToRemove);
	}

	private void itemChecked(ShoppingListChange change) {
		ShoppingItem checkedItem = allItems.get(change.getId());
		checkedItem.check();
		itemCheckedListener.shoppingItemChanged(checkedItem);
	}
	
	private void itemUnchecked(ShoppingListChange change) {
		ShoppingItem checkedItem = allItems.get(change.getId());
		checkedItem.uncheck();
		itemUncheckedListener.shoppingItemChanged(checkedItem);
	}
}
