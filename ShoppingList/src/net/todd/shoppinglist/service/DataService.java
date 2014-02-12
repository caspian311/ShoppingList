package net.todd.shoppinglist.service;

import java.util.Date;
import java.util.List;

import net.todd.shoppinglist.ShoppingItem;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class DataService extends Service {
	private final IBinder binder = new DataServiceBinder();

	private DataChangedListener dataChangedListener;

	private BackgroundThread backgroundThread;

	private DataClient dataClient;

	private AllDataAvailableListener allDataAvailableListener;

	public DataService() {
		dataClient = new DataClient();
	}

	public class DataServiceBinder extends Binder {
		public DataService getService() {
			return DataService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public void start() {
		backgroundThread = new BackgroundThread();
		backgroundThread.start();
		backgroundThread.getHandler().post(new Runnable() {
			@Override
			public void run() {
				List<ShoppingItem> items = dataClient.getAllData();
				if (items != null) {
					allDataAvailableListener.allItemsAvailable(items);
				}
			}
		});
		backgroundThread.getHandler().post(new Runnable() {
			@Override
			public void run() {
				List<ShoppingListChange> changes = dataClient
						.getChangesSince(new Date());
				if (changes != null) {
					dataChangedListener.dataChanged(changes);
				}
			}
		});
	}

	public void addDataChangedListener(DataChangedListener dataChangedListener) {
		this.dataChangedListener = dataChangedListener;
	}

	public void addGetAllDataListener(
			AllDataAvailableListener allDataAvailableListener) {
		this.allDataAvailableListener = allDataAvailableListener;
	}

	public void createNewItem(String newItemText) {
		dataClient.saveNewItem(newItemText);
	}

	public void removeItem(String id) {
		// TODO implement me!
	}

	public void checkItem(String id) {
		// TODO implement me!
	}

	public void uncheckItem(String id) {
		// TODO implement me!
	}
}