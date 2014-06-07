package net.todd.shoppinglist;

import java.util.List;

import android.widget.ArrayAdapter;

public class MainModel {
	private ArrayAdapter<ShoppingItem> adapter;
	private SpinnerController spinnerController;

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
}
