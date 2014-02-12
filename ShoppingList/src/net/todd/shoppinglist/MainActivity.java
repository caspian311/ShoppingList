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

public class MainActivity extends Activity {
	private DataService service;
	private boolean isBound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MainPresenter.create(new MainView(this), new MainModel(service));
	}

	@Override
	protected void onStart() {
		super.onStart();

		Intent intent = new Intent(this, DataService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (isBound) {
			unbindService(connection);
			isBound = false;
		}
	}

	private ServiceConnection connection = new ServiceConnection() {
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
