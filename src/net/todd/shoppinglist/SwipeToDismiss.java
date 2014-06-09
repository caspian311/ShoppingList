package net.todd.shoppinglist;

import android.widget.ListView;

import com.google.android.apps.dashclock.ui.DismissCallbacks;
import com.google.android.apps.dashclock.ui.SwipeDismissListViewTouchListener;

public class SwipeToDismiss {
	public static void addBehaviorTo(ListView listView,
			DismissCallbacks dismissCallback) {
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				listView, dismissCallback);
		listView.setOnTouchListener(touchListener);
		listView.setOnScrollListener(touchListener.makeScrollListener());
	}
}
