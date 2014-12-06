package net.todd.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
    private LoaderCallback loaderCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.shopping_item, null, new String[]{"name"}, new int[]{R.id.shopping_item_name}, 0);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        loaderCallback = new LoaderCallback(adapter, this);
        getLoaderManager().initLoader(0, null, loaderCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                Log.i("monkey", "item to delete: " + id);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.delete_dialog_title)
                        .setMessage(R.string.delete_dialog_message)
                        .setPositiveButton(R.string.delete_button_text,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = Uri.parse("content://net.todd.shoppinglist")
                                                .buildUpon()
                                                .appendPath("" + id)
                                                .build();

                                        new AsyncQueryHandler(getContentResolver()) {
                                            @Override
                                            protected void onDeleteComplete(int token, Object cookie, int result) {
                                                super.onDeleteComplete(token, cookie, result);

                                                getLoaderManager().restartLoader(0, null, loaderCallback);
                                            }
                                        }.startDelete(-1, null, uri, null, null);
                                    }
                                })
                        .setNegativeButton(R.string.cancel_delete_button, null)
                        .create().show();

                return true;
            }
        });

        findViewById(R.id.add_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("value", "monkey");
                Uri uri = Uri.parse("content://net.todd.shoppinglist");

                new AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                        super.onInsertComplete(token, cookie, uri);
                        getLoaderManager().restartLoader(0, null, loaderCallback);
                    }
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
