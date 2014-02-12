package net.todd.shoppinglist;

public interface IMainView {
	void setup();
	
	void addItemButtonListener(IShoppingItemAddedListener listener);
	void addDeleteListener(IShoppingItemModifiedListener listener);
	void addCheckItemListener(IShoppingItemModifiedListener listener);
	
	void clearNewItemText();
	void createNewItem(final String newShoppingItemId, final String newShoppingItemValue);
	void removeShoppingItem(String shoppingItemId);
	void checkItem(String shoppingItemId);
	void uncheckItem(String shoppingItemId);
}
