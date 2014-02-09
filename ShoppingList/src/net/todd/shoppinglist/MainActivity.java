package net.todd.shoppinglist;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MainPresenter.create(new MainView(this), new MainModel());
	}
}
