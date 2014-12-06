package net.todd.shoppinglist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListContentProvider extends ContentProvider {
    private Map<Long, String> itemIdMap = new HashMap<Long, String>();
    private long count;
    private WebService webService;

    public ListContentProvider() {
        webService = new WebService("http://10.0.3.2:3000/shoppingItems");
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public ShoppingListCursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        count = 0;

        List<ShoppingListItem> data = webService.getAll(new ShoppingListItemParser());
        for (ShoppingListItem item : data) {
            itemIdMap.put(++count, item.getRealId());
            item.setId(count);
        }
        return new ShoppingListCursor(data);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i("shopping list", "inserting content values: " + values);
        webService.post(values);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = itemIdMap.get(Long.parseLong(selection));
        Log.i("shopping list", "deleting value: " + selection);
        webService.delete(id);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
