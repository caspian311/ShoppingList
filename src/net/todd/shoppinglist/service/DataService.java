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

	private DataAvailableListener dataAvailableListener;
	private FetchDataNotifyier fetchingDataListener;
	private BackgroundThread backgroundThread;
	
	private final IShoppingItemsClient shoppingItemsClient;

	
	public DataService() {
		Log.i(TAG, "DataService instantiated");
		
		shoppingItemsClient = new ShoppingItemsClient();
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
		GetAllItemsTask getAllItems = new GetAllItemsTask(shoppingItemsClient, fetchingDataListener, dataAvailableListener, backgroundThread, uiHandler);
		
		backgroundThread.scheduleTask(getAllItems);
	}

	public void addDataAvailableListener(DataAvailableListener dataAvailableListener) {
		this.dataAvailableListener = dataAvailableListener;
	}

	public void createNewItem(final String id, final String value) {
		backgroundThread.scheduleImmediateTask(new Runnable() {
			@Override
			public void run() {
				Map<String, String> data = new HashMap<String, String>();
				data.put("id", id);
				data.put("value", value);
				
				shoppingItemsClient.post(data);
			}
		});
	}

	public void removeItem(final String id) {
		backgroundThread.scheduleImmediateTask(new Runnable() {
			@Override
			public void run() {
				shoppingItemsClient.delete(id);
			}
		});
	}

	public void addFetchingDataListener(FetchDataNotifyier listener) {
		this.fetchingDataListener = listener;
	}
}