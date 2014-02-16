package net.todd.shoppinglist.service;

import java.util.Date;
import java.util.List;

import android.os.Handler;
import android.util.Log;

public class GetChangesTask implements Runnable {
	private static final String TAG = GetChangesTask.class.toString();
	public static final int POLLING_FREQUENCY = 1 * 1000;
	
	private Date lastChangesReceived;
	private ShoppingItemsChangesClient dataClient;
	private DataChangedListener dataChangedListener;
	private BackgroundThread backgroundThread;
	private Handler uiHandler;
	
	public GetChangesTask(ShoppingItemsChangesClient dataClient, DataChangedListener dataChangedListener, BackgroundThread backgroundThread, Handler uiHandler) {
		this.dataClient = dataClient;
		this.dataChangedListener = dataChangedListener;
		this.backgroundThread = backgroundThread;
		this.uiHandler = uiHandler;
	}
	
	@Override
	public void run() {
		Log.i(TAG, "Fetching changes");
		final List<ShoppingListChange> changes = dataClient.getSince(lastChangesReceived);
		lastChangesReceived = new Date();
		if (changes != null && changes.size() > 0) {
			uiHandler.post(new Runnable() {
				@Override
				public void run() {
					dataChangedListener.dataChanged(changes);
				}
			});
		}
		backgroundThread.scheduleTask(this, POLLING_FREQUENCY);
	}

	public void setLastChangesReceived(Date lastChangesRecieved) {
		lastChangesReceived = lastChangesRecieved;
	}
}
