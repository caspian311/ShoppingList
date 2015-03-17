package net.todd.shoppinglist;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CursorAdapter;

public class TaskManager extends AsyncQueryHandler {
    private LoaderManager loaderManager;
    private LoaderManager.LoaderCallbacks<Cursor> cursorLoader;

    public TaskManager(final Context context, ContentResolver contentResolver, LoaderManager loaderManager, final CursorAdapter adapter) {
        super(contentResolver);
        this.loaderManager = loaderManager;
        cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(context, MainActivity.URI, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                adapter.swapCursor(null);
            }
        };
    }

    public void createTask(String newItemName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", newItemName);

        startInsert(-1, null, MainActivity.URI, contentValues);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        kickOffLoader();
    }

    private void kickOffLoader() {
        loaderManager.restartLoader(MainActivity.SHOPPING_LIST_LOADER, null, cursorLoader);
    }

    public void loadTasks() {
        kickOffLoader();
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        kickOffLoader();
    }

    public void deleteTask(long id) {
        startDelete(-1, null, MainActivity.URI, "" + id, null);
    }
}
