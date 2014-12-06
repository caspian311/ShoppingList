package net.todd.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
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

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int SHOPPING_LIST_LOADER = 1;
    private static final int NEW_ITEM_REQUEST = Activity.RESULT_FIRST_USER + 1;

    private Uri uri;
    private SimpleCursorAdapter adapter;

    public MainActivity() {
        this.uri = Uri.parse("content://net.todd.shoppinglist");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new SimpleCursorAdapter(this, R.layout.shopping_item, null, new String[]{"name"}, new int[]{R.id.shopping_item_name}, 0);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        getLoaderManager().initLoader(SHOPPING_LIST_LOADER, null, this);
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
                                        new AsyncQueryHandler(getContentResolver()) {
                                            @Override
                                            protected void onDeleteComplete(int token, Object cookie, int result) {
                                                super.onDeleteComplete(token, cookie, result);

                                                getLoaderManager().restartLoader(SHOPPING_LIST_LOADER, null, MainActivity.this);
                                            }
                                        }.startDelete(-1, null, uri, "" + id, null);
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
                startActivityForResult(new Intent(MainActivity.this, NewItemActivity.class), NEW_ITEM_REQUEST);
            }
        });

        getLoaderManager().restartLoader(SHOPPING_LIST_LOADER, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            doInsert(data.getStringExtra(NewItemActivity.NEW_ITEM_NAME));
        }
    }

    private void doInsert(String newItemName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", newItemName);

        new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                super.onInsertComplete(token, cookie, uri);
                getLoaderManager().restartLoader(SHOPPING_LIST_LOADER, null, MainActivity.this);
            }
        }.startInsert(-1, null, uri, contentValues);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(null);
        findViewById(R.id.add_item_button).setOnClickListener(null);

        getLoaderManager().destroyLoader(SHOPPING_LIST_LOADER);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Uri.parse("content://net.todd.shoppinglist"),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        findViewById(R.id.no_items_available).setVisibility(adapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
