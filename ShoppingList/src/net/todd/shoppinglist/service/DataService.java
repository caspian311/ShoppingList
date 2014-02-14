package net.todd.shoppinglist.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class DataService extends Service {
	protected static final String TAG = DataService.class.toString();

	private final IBinder binder = new DataServiceBinder();

	private DataChangedListener dataChangedListener;
	private AllDataAvailableListener allDataAvailableListener;
	private BackgroundThread backgroundThread;
	
	private final ShoppingItemsClient shoppingItemsClient;
	private final ShoppingItemsChangesClient shoppingItemsChangesClient;
	
	public DataService() {
		Log.i(TAG, "DataService instantiated");
		
		shoppingItemsClient = new ShoppingItemsClient();
		shoppingItemsChangesClient = new ShoppingItemsChangesClient();
	}

	public class DataServiceBinder extends Binder {
		public DataService getService() {
			Log.i(TAG, "instance of service requested");
			return DataService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "Binding DataService");
		
		backgroundThread = new BackgroundThread();
		backgroundThread.start();
		
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

	public void start() {
		Handler uiHandler = new Handler();
		GetChangesTask getChangesTask = new GetChangesTask(shoppingItemsChangesClient, dataChangedListener, backgroundThread, uiHandler);
		GetAllItemsTask getAllItems = new GetAllItemsTask(shoppingItemsClient, allDataAvailableListener, backgroundThread, getChangesTask, uiHandler);
		
		backgroundThread.scheduleTask(getAllItems);
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