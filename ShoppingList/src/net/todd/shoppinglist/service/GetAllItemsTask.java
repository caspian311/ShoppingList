package net.todd.shoppinglist.service;

import java.util.Date;
import java.util.List;

import net.todd.shoppinglist.ShoppingItem;
import android.os.Handler;
import android.util.Log;

public class GetAllItemsTask implements Runnable {
	private static final String TAG = GetAllItemsTask.class.toString();
	
	private final ShoppingItemsClient dataClient;
	private final BackgroundThread backgroundThread;
	private final AllDataAvailableListener allDataAvailableListener;
	private final GetChangesTask getChangesTask;

	private Handler uiHandler;
	
	public GetAllItemsTask(ShoppingItemsClient dataClient, AllDataAvailableListener allDataAvailableListener, BackgroundThread backgroundThread, GetChangesTask getChangesTask, Handler uiHandler) {
		this.dataClient = dataClient;
		this.allDataAvailableListener = allDataAvailableListener;
		this.backgroundThread = backgroundThread;
		this.getChangesTask = getChangesTask;
		this.uiHandler = uiHandler;
	}
	
	@Override
	public void run() {
		Log.i(TAG, "Fetching all items");
		getChangesTask.setLastChangesReceived(new Date());
		final List<ShoppingItem> items = dataClient.get();
		if (items != null) {
			uiHandler.post(new Runnable() {

				@Override
				public void run() {
					allDataAvailableListener.allItemsAvailable(items);
				}
			});
		}
		backgroundThread.scheduleTask(getChangesTask, GetChangesTask.POLLING_FREQUENCY);
	}
}
