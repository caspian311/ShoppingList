package net.todd.shoppinglist;

public class MainPresenter {
	public static void create(final IMainView mainView,
			final IMainModel mainModel) {
		mainView.setup();
		
		mainView.addItemButtonListener(new IListener() {
			@Override
			public void handle() {
				mainModel.createItem(mainView.getNewItemText());
				mainView.clearNewItemText();
			}
		});

		mainModel.addItemCreatedListener(new IListener() {
			@Override
			public void handle() {
				mainView.createNewItem(mainModel.getNewItem());
			}
		});

		mainView.addDeleteListener(new IListener() {
			@Override
			public void handle() {
				mainModel.removeShoppingItem(mainView.getItemToDelete());
			}
		});

		mainModel.addItemRemovedListener(new IListener() {
			@Override
			public void handle() {
				mainView.removeShoppingItem(mainModel.getItemToRemove());
			}
		});

		mainView.addCheckItemListener(new IListener() {
			@Override
			public void handle() {
				mainModel.checkItem(mainView.getItemToCheck());
			}
		});

		mainModel.addItemCheckedListener(new IListener() {
			@Override
			public void handle() {
				mainView.checkItem(mainModel.getCheckedItem());
			}
		});
	}
}
