package net.todd.shoppinglist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.net.Uri;

import java.util.List;

public class ListContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public ShoppingListCursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        WebService webService = new WebService();
        List<ShoppingListItem> data = webService.getAll("http://10.0.2.2:3000/shoppingItems", new ShoppingListItemParser());
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
