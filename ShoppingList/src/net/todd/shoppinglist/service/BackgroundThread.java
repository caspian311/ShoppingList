package net.todd.shoppinglist.service;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class BackgroundThread extends Thread {
	private static final String TAG = BackgroundThread.class.toString();
	private Handler handler;

	public Handler getHandler() {
		return handler;
	}
	
	@Override
	public void run() {
		try {
			Looper.prepare();
			
			handler = new Handler();
			
			Looper.loop();
		} catch (Exception e) {
			Log.e(TAG, "Looper died: " + e.getMessage());
		}
	}
}
