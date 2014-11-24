package net.todd.shoppinglist;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.list);

        listView.setOnItemClickListener(this);

        String[] fromColumns = { "name" };
        int[] toViews = {R.id.shopping_item};

        mAdapter = new SimpleCursorAdapter(this,
                R.layout.shopping_item, null,
                fromColumns, toViews, 0);
        listView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Uri.parse("content://net.todd.shoppinglist"),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selection = ((TextView)view.findViewById(R.id.shopping_item)).getText().toString();
        Log.i("monkey", "item selected: " + selection);
    }
}
