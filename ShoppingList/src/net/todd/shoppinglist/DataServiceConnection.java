package net.todd.shoppinglist;

import net.todd.shoppinglist.DataService.DataServiceBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class DataServiceConnection implements ServiceConnection, IServiceProvider {
	private IDataService service;
	private boolean isServiceBound;
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		DataServiceBinder dataServiceBinder = (DataServiceBinder) binder;
    	service = dataServiceBinder.getService();
        isServiceBound = false;
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		isServiceBound = false;
	}
	
	public boolean isServiceBound() {
		return isServiceBound;
	}

	public IDataService getService() {
		return service;
	}
}
