package net.todd.shoppinglist;

import java.util.List;

import android.widget.ArrayAdapter;

public class MainModel {
	protected static final String TAG = MainModel.class.toString();
	private ArrayAdapter<ShoppingItem> adapter;
	private SpinnerController spinnerController;
	private IShoppingItemRemovedListener shoppingItemRemovedListener;

	public MainModel(ArrayAdapter<ShoppingItem> adapter, SpinnerController spinnerController) {
		this.adapter = adapter;
		this.spinnerController = spinnerController;
	}

	public void mergeItems(List<ShoppingItem> allItems) {
		adapter.clear();
		adapter.addAll(allItems);
	}

	public void showSpinner() {
		spinnerController.show();
	}

	public void hideSpinner() {
		spinnerController.hide();
	}

	public void deleteShoppingItem(ShoppingItem shoppingItem) {
		shoppingItemRemovedListener.shoppingItemRemoved(shoppingItem.getId());
	}

	public void addDeleteListener(
			IShoppingItemRemovedListener shoppingItemRemovedListener) {
				this.shoppingItemRemovedListener = shoppingItemRemovedListener;
	}
}
