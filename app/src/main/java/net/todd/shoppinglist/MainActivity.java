package net.todd.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {
    public static final int SHOPPING_LIST_LOADER = 1;
    public static final int NEW_ITEM_REQUEST = Activity.RESULT_FIRST_USER + 1;

    public static final Uri URI = Uri.parse("content://net.todd.shoppinglist");
    private CursorAdapter adapter;
    private TaskManager taskManager;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = createAdapter(this);
        listView.setAdapter(adapter);

        taskManager = new TaskManager(this, getContentResolver(), getLoaderManager(), adapter);
    }

    protected CursorAdapter createAdapter(Context context) {
        return new SimpleCursorAdapter(context, R.layout.shopping_item, null, new String[]{"name"}, new int[]{R.id.shopping_item_name}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                                        taskManager.deleteTask(id);
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

        taskManager.loadTasks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String taskName = data.getStringExtra(NewItemActivity.NEW_ITEM_NAME);
            taskManager.createTask(taskName);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        listView.setOnItemLongClickListener(null);
        findViewById(R.id.add_item_button).setOnClickListener(null);

        getLoaderManager().destroyLoader(SHOPPING_LIST_LOADER);
    }
}
