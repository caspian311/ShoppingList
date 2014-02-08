package net.todd.shoppinglist;

import java.util.UUID;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class DataService extends Service implements IDataService {
	public class DataServiceBinder extends Binder {
		public IDataService getService() {
			return DataService.this;
		}
	}

	private final IBinder binder;
	
	public DataService() {
		binder = new DataServiceBinder();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public String getUUID() {
		return UUID.randomUUID().toString();
	}
}
