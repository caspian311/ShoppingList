package net.todd.shoppinglist.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.todd.shoppinglist.ShoppingItem;
import android.os.Handler;
import android.util.Log;

public class GetAllItemsTask implements Runnable {
	private static final String TAG = GetAllItemsTask.class.toString();

	private static final long POLLING_FREQUENCY = 10 * 1000;
	
	private final ShoppingItemsClient dataClient;
	private final BackgroundThread backgroundThread;
	private final AllDataAvailableListener allDataAvailableListener;

	private Handler uiHandler;
	
	public GetAllItemsTask(ShoppingItemsClient dataClient, AllDataAvailableListener allDataAvailableListener, BackgroundThread backgroundThread, Handler uiHandler) {
		this.dataClient = dataClient;
		this.allDataAvailableListener = allDataAvailableListener;
		this.backgroundThread = backgroundThread;
		this.uiHandler = uiHandler;
	}
	
	@Override
	public void run() {
		Log.i(TAG, "Fetching all items");
		final List<ShoppingItem> items = dataClient.get();
		if (items != null) {
			uiHandler.post(new Runnable() {

				@Override
				public void run() {
					Map<String, ShoppingItem> remoteItems = hashifyItems(items);
					allDataAvailableListener.allItemsAvailable(remoteItems);
				}
			});
		}
		backgroundThread.scheduleTask(this, POLLING_FREQUENCY);
	}
	
	private Map<String, ShoppingItem> hashifyItems(List<ShoppingItem> remoteItems) {
		Map<String, ShoppingItem> map = new HashMap<String, ShoppingItem>();
		for (ShoppingItem shoppingItem : remoteItems) {
			map.put(shoppingItem.getId(), shoppingItem);
		}
		return map;
	}
}
