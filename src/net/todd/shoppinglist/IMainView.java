package net.todd.shoppinglist;

public interface IMainView {
	void setup();
	
	void addItemButtonListener(final IListener listener);
	void addDeleteListener(final IListener listener);
	void addCheckItemListener(final IListener listener);
	
	String getNewItemText();
	void clearNewItemText();
	void createNewItem(ShoppingItem newItemValue);
	void removeShoppingItem(ShoppingItem shoppingItem);
	ShoppingItem getItemToDelete();
	ShoppingItem getItemToCheck();
	void checkItem(ShoppingItem shoppingItem);
}
