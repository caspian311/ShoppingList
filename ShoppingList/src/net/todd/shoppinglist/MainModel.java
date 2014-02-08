package net.todd.shoppinglist;

public class MainModel implements IMainModel {
	private IListener itemCreatedListener;
	private IListener itemRemovedListener;
	private IListener itemCheckedListener;

	private ShoppingItem newItem;
	private ShoppingItem itemToRemove;
	private ShoppingItem checkedItem;

	@Override
	public void createItem(String newItemText) {
		this.newItem = new ShoppingItem(newItemText);
		itemCreatedListener.handle();
	}

	@Override
	public void addItemCreatedListener(IListener listener) {
		this.itemCreatedListener = listener;
	}

	@Override
	public ShoppingItem getNewItem() {
		return newItem;
	}

	@Override
	public void removeShoppingItem(ShoppingItem itemToRemove) {
		this.itemToRemove = itemToRemove;
		itemRemovedListener.handle();
	}

	@Override
	public ShoppingItem getItemToRemove() {
		return itemToRemove;
	}

	@Override
	public void addItemRemovedListener(IListener listener) {
		itemRemovedListener = listener;
	}

	@Override
	public void addItemCheckedListener(IListener listener) {
		this.itemCheckedListener = listener;
	}

	@Override
	public void checkItem(ShoppingItem itemToCheck) {
		if (itemToCheck.isChecked()) {
			itemToCheck.unCheck();
		} else {
			itemToCheck.check();
		}

		this.checkedItem = itemToCheck;
		itemCheckedListener.handle();
	}

	@Override
	public ShoppingItem getCheckedItem() {
		return checkedItem;
	}
}
