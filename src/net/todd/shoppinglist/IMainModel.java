package net.todd.shoppinglist;

import java.util.Map;

public interface IMainModel {
	void addItemCreatedListener(IShoppingItemCreatedListener listener);
	void addItemRemovedListener(IShoppingItemRemovedListener listener);
	void addItemCheckListener(IShoppingItemChangeListener iListener);
	
	void createItem(String newItemText);
	void removeShoppingItem(String itemToRemoveId);
	void checkItem(String itemToCheckId);
	void mergeItems(Map<String, ShoppingItem> allItems);
}
