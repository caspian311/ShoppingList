package net.todd.shoppinglist;

public class MainPresenter {
	public static void create(final IMainView mainView,
			final IMainModel mainModel) {
		mainView.addItemButtonListener(new IShoppingItemAddedListener() {
			@Override
			public void itemCreated(String value) {
				mainModel.createItem(value);
				mainView.clearNewItemText();
			}
		});

		mainView.addDeleteListener(new IShoppingItemModifiedListener() {
			@Override
			public void itemModified(String itemToDeleteId) {
				mainModel.removeShoppingItem(itemToDeleteId);
			}
		});
		
		mainView.addCheckItemListener(new IShoppingItemModifiedListener() {
			@Override
			public void itemModified(String itemToCheckId) {
				mainModel.checkItem(itemToCheckId);
			}
		});
		
		mainModel.addItemCreatedListener(new IShoppingItemCreatedListener() {
			@Override
			public void shoppingItemChanged(ShoppingItem shoppingItem) {
				mainView.createNewItem(shoppingItem.getId(), shoppingItem.getValue(), shoppingItem.isChecked());
			}
		});

		mainModel.addItemRemovedListener(new IShoppingItemRemovedListener() {
			@Override
			public void shoppingItemRemoved(String shoppingItemId) {
				mainView.removeShoppingItem(shoppingItemId);
			}
		});

		mainModel.addItemCheckListener(new IShoppingItemChangeListener() {
			@Override
			public void shoppingItemChanged(ShoppingItem shoppingItem) {
				if (shoppingItem.isChecked()) {
					mainView.checkItem(shoppingItem.getId());
				} else {
					mainView.uncheckItem(shoppingItem.getId());
				}
			}
		});
		
		mainView.setup();
	}
}
