package net.todd.shoppinglist;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.view.View;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
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

        findViewById(R.id.add_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("value", "monkey");
                Uri uri = Uri.parse("content://net.todd.shoppinglist");

                new AsyncQueryHandler(getContentResolver()) {
                }.startInsert(-1, null, uri, contentValues);
            }
        });

        getLoaderManager().restartLoader(0, null, loaderCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(null);
        findViewById(R.id.add_item_button).setOnClickListener(null);

        getLoaderManager().destroyLoader(0);
    }
}
