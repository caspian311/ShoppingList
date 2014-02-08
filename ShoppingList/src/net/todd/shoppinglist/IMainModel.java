package net.todd.shoppinglist;

public interface IMainModel {
	void addItemCreatedListener(IListener listener);
	void addItemCheckedListener(IListener iListener);
	void addItemRemovedListener(IListener listener);
	
	void createItem(String newItemText);
	ShoppingItem getNewItem();
	
	void removeShoppingItem(ShoppingItem itemToRemove);
	ShoppingItem getItemToRemove();
	
	void checkItem(ShoppingItem itemToCheck);
	ShoppingItem getCheckedItem();
}
