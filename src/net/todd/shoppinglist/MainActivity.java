package net.todd.shoppinglist;

import java.util.ArrayList;

import net.todd.shoppinglist.service.DataService;
import net.todd.shoppinglist.service.DataService.DataServiceBinder;
import net.todd.shoppinglist.service.ServicePresenter;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.apps.dashclock.ui.SwipeDismissListViewTouchListener;

public class MainActivity extends Activity {
	protected static final String TAG = MainActivity.class.toString();
	private DataService service;
	private boolean isBound;

	private Menu actionBarMenu;
	private ShoppingItemAdapter adapter;

	enum Direction {
		RIGHT, LEFT
	}

	static final int DELTA = 50;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main_menu, menu);

		actionBarMenu = menu;

		Log.i(TAG, "Action bar created.");

		setUpApp();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_item:
			Toast.makeText(this, "Add new item", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "App created.");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		getActionBar().setTitle(R.string.app_name);
		getActionBar().setSubtitle(R.string.sub_title);

		adapter = new ShoppingItemAdapter(this, new ArrayList<ShoppingItem>());

		ListView listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);

		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				listView,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							adapter.remove(adapter.getItem(position));
						}
						adapter.notifyDataSetChanged();
					}

					@Override
					public boolean canDismiss(int position) {
						return true;
					}
				});
		listView.setOnTouchListener(touchListener);
		listView.setOnScrollListener(touchListener.makeScrollListener());
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "App started.");
		super.onStart();

		Intent intent = new Intent(this, DataService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (isBound) {
			unbindService(serviceConnection);
			isBound = false;
		}
	}

	private void setUpApp() {
		SpinnerController spinnerController = new SpinnerController(
				MainActivity.this, actionBarMenu);
		MainModel mainModel = new MainModel(adapter, spinnerController);

		ServicePresenter.create(service, mainModel);
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.i(TAG, "Service connected.");
			service = ((DataServiceBinder) binder).getService();
			isBound = true;

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			isBound = false;
		}
	};
}
