package net.todd.shoppinglist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {
    private LoaderCallback loaderCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.shopping_item, null, new String[]{"name"}, new int[]{R.id.shopping_item}, 0);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        loaderCallback = new LoaderCallback(adapter, this);
        getLoaderManager().initLoader(0, null, loaderCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new ShoppingItemClickListener());

        getLoaderManager().restartLoader(0, null, loaderCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(null);

        getLoaderManager().destroyLoader(0);
    }
}
