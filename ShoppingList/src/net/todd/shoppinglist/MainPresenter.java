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
		
		mainModel.addItemCreatedListener(new IShoppingItemChangeListener() {
			@Override
			public void shoppingItemChanged(ShoppingItem shoppingItem) {
				mainView.createNewItem(shoppingItem.getId(), shoppingItem.getValue());
			}
		});

		mainModel.addItemRemovedListener(new IShoppingItemChangeListener() {
			@Override
			public void shoppingItemChanged(ShoppingItem shoppingItem) {
				mainView.removeShoppingItem(shoppingItem.getId());
			}
		});

		mainModel.addItemCheckedListener(new IShoppingItemChangeListener() {
			@Override
			public void shoppingItemChanged(ShoppingItem shoppingItem) {
				mainView.checkItem(shoppingItem.getId());
			}
		});
		
		mainModel.addItemUncheckedListener(new IShoppingItemChangeListener() {
			@Override
			public void shoppingItemChanged(ShoppingItem shoppingItem) {
				mainView.uncheckItem(shoppingItem.getId());
			}
		});
		
		mainView.setup();
	}
}
