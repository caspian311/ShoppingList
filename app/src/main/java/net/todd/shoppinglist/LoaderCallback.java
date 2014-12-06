package net.todd.shoppinglist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ResourceCursorAdapter;

public class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
    private ResourceCursorAdapter adapter;
    private Context context;

    public LoaderCallback(ResourceCursorAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, Uri.parse("content://net.todd.shoppinglist"),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}