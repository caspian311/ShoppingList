package net.todd.shoppinglist;

import net.todd.shoppinglist.service.DataService;
import net.todd.shoppinglist.service.DataService.DataServiceBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.toString();
	
	private DataService service;
	private boolean isBound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Creating app");
		super.onCreate(savedInstanceState);

		Log.i(TAG, "App created");
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "Starting app");
		super.onStart();

		Intent intent = new Intent(this, DataService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	private void setupApp() {
		MainPresenter.create(new MainView(this), new MainModel(service));
	}
	
	@Override
	protected void onStop() {
		Log.i(TAG, "Stopping app");
		super.onStop();

		if (isBound) {
			unbindService(serviceConnection);
			isBound = false;
		}
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.i(TAG, "Service connected");
			service = ((DataServiceBinder) binder).getService();
			isBound = true;
			
			setupApp();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			isBound = false;
		}
	};
}
