package net.todd.shoppinglist.service;

import java.util.List;

import net.todd.shoppinglist.MainModel;
import net.todd.shoppinglist.ShoppingItem;

public class ServicePresenter {
	public static void create(final DataService service, final MainModel mainModel) {
		service.addFetchingDataListener(new FetchDataNotifyier() {
			public void notifyDataBeingFetched() {
				mainModel.showSpinner();
			}
		});
		service.addDataAvailableListener(new DataAvailableListener() {
			@Override
			public void allItemsAvailable(List<ShoppingItem> allItems) {
				mainModel.mergeItems(allItems);
				mainModel.hideSpinner();
			}
		});
		
		service.start();
	}
}
