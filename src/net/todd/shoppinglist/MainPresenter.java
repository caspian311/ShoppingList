package net.todd.shoppinglist;

public class MainPresenter {
	public static void create(final IMainView mainView,
			final IMainModel mainModel) {
		mainView.setup();
		
		mainView.addItemButtonListener(new IListener() {
			@Override
			public void handle() {
				String newItemText = mainView.getNewItemText();
				mainView.clearNewItemText();

				mainModel.createItem(newItemText);
			}
		});

		mainModel.addItemCreatedListener(new IListener() {
			@Override
			public void handle() {
				ShoppingItem newItemValue = mainModel.getNewItem();
				mainView.createNewItem(newItemValue);
			}
		});

		mainView.addDeleteListener(new IListener() {
			@Override
			public void handle() {
				ShoppingItem itemToRemove = mainView.getItemToDelete();
				mainModel.removeShoppingItem(itemToRemove);
			}
		});

		mainModel.addItemRemovedListener(new IListener() {
			@Override
			public void handle() {
				ShoppingItem itemToRemove = mainModel.getItemToRemove();
				mainView.removeShoppingItem(itemToRemove);
			}
		});

		mainView.addCheckItemListener(new IListener() {
			@Override
			public void handle() {
				ShoppingItem itemToCheck = mainView.getItemToCheck();
				mainModel.checkItem(itemToCheck);
			}
		});

		mainModel.addItemCheckedListener(new IListener() {
			@Override
			public void handle() {
				ShoppingItem shoppingItem = mainModel.getCheckedItem();
				mainView.checkItem(shoppingItem);
			}
		});
	}
}
