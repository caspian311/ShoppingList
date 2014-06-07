package net.todd.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SpinnerController {
	private Menu menu;
	private Activity context;
	private ImageView view;

	public SpinnerController(Activity context, Menu menu) {
		this.context = context;
		this.menu = menu;

		LayoutInflater inflater = (LayoutInflater) context.getApplication()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (ImageView) inflater.inflate(R.layout.syncing_image, null);
	}

	public void show() {
		Animation rotation = AnimationUtils.loadAnimation(
				context.getApplication(), R.anim.sync_rotate);
		rotation.setRepeatCount(Animation.INFINITE);
		view.startAnimation(rotation);

		getSyncingMenuItem().setActionView(view);
	}

	public void hide() {
		view.clearAnimation();
		getSyncingMenuItem().setActionView(null);
	}

	private MenuItem getSyncingMenuItem() {
		return menu.findItem(R.id.syncing);
	}
}
