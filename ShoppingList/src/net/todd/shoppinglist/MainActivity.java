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

		MainPresenter.create(new MainView(this), new MainModel(service));
		Log.i(TAG, "App created");
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "Starting app");
		super.onStart();

		bindService();
	}

	private void bindService() {
		Intent intent = new Intent(this, DataService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);		
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "Pausing app");
		super.onPause();
		
		unbindService();
	}
	
	@Override
	protected void onResume() {
		Log.i(TAG, "Resuming app");
		super.onResume();
		
		bindService();
	}
	
	private void unbindService() {
		if (isBound) {
			unbindService(serviceConnection);
			isBound = false;
		}
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "Stopping app");
		super.onStop();

		unbindService();
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = ((DataServiceBinder) binder).getService();
			isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			isBound = false;
		}
	};
}
