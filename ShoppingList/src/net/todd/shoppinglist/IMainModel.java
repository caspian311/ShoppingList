package net.todd.shoppinglist;

public interface IMainModel {
	void addItemCreatedListener(IShoppingItemChangeListener listener);
	void addItemRemovedListener(IShoppingItemChangeListener listener);
	void addItemCheckedListener(IShoppingItemChangeListener iListener);
	void addItemUncheckedListener(IShoppingItemChangeListener listener);
	
	void createItem(String newItemText);
	void removeShoppingItem(String itemToRemoveId);
	void checkItem(String itemToCheckId);
}
