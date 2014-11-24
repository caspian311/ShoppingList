package net.todd.shoppinglist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ListContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public ShoppingListCursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<ShoppingListItem> data = new ArrayList<ShoppingListItem>();
        data.add(new ShoppingListItem());
        data.get(0).setName("monkey");
        Log.i("monkey", "loading " + uri.toString());
        return new ShoppingListCursor(data);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
