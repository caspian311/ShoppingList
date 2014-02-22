package net.todd.shoppinglist.service;

import java.util.Map;

import net.todd.shoppinglist.IMainModel;
import net.todd.shoppinglist.ShoppingItem;

public class ServicePresenter {
	public static void create(final DataService service, final IMainModel mainModel) {
		service.addGetAllDataListener(new AllDataAvailableListener() {
			@Override
			public void allItemsAvailable(Map<String, ShoppingItem> allItems) {
				mainModel.mergeItems(allItems);
			}
		});
		
		service.start();
	}
}
