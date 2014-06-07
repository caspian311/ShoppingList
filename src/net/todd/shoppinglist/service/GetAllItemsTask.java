package net.todd.shoppinglist.service;

import java.util.List;

import net.todd.shoppinglist.ShoppingItem;
import android.os.Handler;
import android.util.Log;

public class GetAllItemsTask implements Runnable {
	private static final String TAG = GetAllItemsTask.class.toString();

	private static final long POLLING_FREQUENCY = 10 * 1000;
	
	private final ShoppingItemsClient dataClient;
	private final BackgroundThread backgroundThread;
	private final DataAvailableListener dataAvailableListener;

	private Handler uiHandler;

	private FetchDataNotifyier fetchDataHandler;
	
	public GetAllItemsTask(ShoppingItemsClient dataClient, FetchDataNotifyier fetchDataHandler, DataAvailableListener dataAvailableListener, BackgroundThread backgroundThread, Handler uiHandler) {
		this.dataClient = dataClient;
		this.fetchDataHandler = fetchDataHandler;
		this.dataAvailableListener = dataAvailableListener;
		this.backgroundThread = backgroundThread;
		this.uiHandler = uiHandler;
	}
	
	@Override
	public void run() {
		Log.i(TAG, "Fetching all items");
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				fetchDataHandler.notifyDataBeingFetched();
			}
		});
		final List<ShoppingItem> items = dataClient.get();
		if (items != null) {
			uiHandler.post(new Runnable() {
				@Override
				public void run() {
					dataAvailableListener.allItemsAvailable(items);
				}
			});
		}
		backgroundThread.scheduleTask(this, POLLING_FREQUENCY);
	}
}
