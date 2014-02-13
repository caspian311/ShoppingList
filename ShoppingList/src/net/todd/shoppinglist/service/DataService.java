package net.todd.shoppinglist.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.todd.shoppinglist.ShoppingItem;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DataService extends Service {
	protected static final String TAG = DataService.class.toString();

	private final IBinder binder = new DataServiceBinder();

	private DataChangedListener dataChangedListener;
	private AllDataAvailableListener allDataAvailableListener;
	private BackgroundThread backgroundThread;
	
	private final long POLLING_FREQUENCY = 5 * 1000;
	private final Runnable getChanges;
	private final Runnable getAllItems;
	private Date lastChangesReceived;

	private final ShoppingItemsClient shoppingItemsClient;
	private final ShoppingItemsChangesClient shoppingItemsChangesClient;
	
	public DataService() {
		shoppingItemsClient = new ShoppingItemsClient();
		shoppingItemsChangesClient = new ShoppingItemsChangesClient();
		
		getChanges = new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching changes");
				List<ShoppingListChange> changes = shoppingItemsChangesClient.getSince(lastChangesReceived);
				lastChangesReceived = new Date();
				if (changes != null) {
					dataChangedListener.dataChanged(changes);
				}
				backgroundThread.getHandler().postDelayed(getChanges, POLLING_FREQUENCY);
			}
		};
		getAllItems = new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching all items");
				List<ShoppingItem> items = shoppingItemsClient.get();
				lastChangesReceived = new Date();
				if (items != null) {
					allDataAvailableListener.allItemsAvailable(items);
				}
				backgroundThread.getHandler().postDelayed(getChanges, POLLING_FREQUENCY);
			}
		};
	}

	public class DataServiceBinder extends Binder {
		public DataService getService() {
			return DataService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "Binding DataService");
		start();
		return binder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "Unbinding DataService");
		stop();
		return true;
	}

	private void stop() {
		backgroundThread.quit();		
	}

	private void start() {
		backgroundThread = new BackgroundThread();
		backgroundThread.start();
		backgroundThread.getHandler().post(getAllItems);
	}

	public void addDataChangedListener(DataChangedListener dataChangedListener) {
		this.dataChangedListener = dataChangedListener;
	}

	public void addGetAllDataListener(AllDataAvailableListener allDataAvailableListener) {
		this.allDataAvailableListener = allDataAvailableListener;
	}

	public void createNewItem(String newItemText) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("value", newItemText);
		shoppingItemsClient.post(data);
	}

	public void removeItem(String id) {
		shoppingItemsClient.delete(id);
	}

	public void checkItem(String id) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("id", id);
		data.put("isChecked", Boolean.TRUE.toString());
		shoppingItemsClient.put(id, data);
	}

	public void uncheckItem(String id) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("id", id);
		data.put("isChecked", Boolean.FALSE.toString());
		shoppingItemsClient.put(id, data);
	}
}