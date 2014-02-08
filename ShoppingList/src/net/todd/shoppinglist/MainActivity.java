package net.todd.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	private boolean isServiceBound;

	private final DataServiceConnection serviceConnection;

	public MainActivity() {
		serviceConnection = new DataServiceConnection();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MainPresenter.create(new MainView(this), new MainModel());
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		
		Intent intent = new Intent(this, DataService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if (isServiceBound) {
			unbindService(serviceConnection);
			isServiceBound = false;
		}
	}
}
