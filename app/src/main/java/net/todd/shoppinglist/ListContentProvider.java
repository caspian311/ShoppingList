package net.todd.shoppinglist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListContentProvider extends ContentProvider {
//    private static final String SERVICE_URL = "http://10.0.3.2:3000/shoppingItems";
    private static final String SERVICE_URL = "http://caspian311-shoppinglist.herokuapp.com/shoppingItems";

    private Map<Long, String> itemIdMap = new HashMap<Long, String>();
    private long count;
    private WebService webService;

    public ListContentProvider() {
        webService = new WebService(SERVICE_URL);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public ShoppingListCursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        count = 0;

        List<ShoppingListItem> data = new ArrayList<ShoppingListItem>();

        try {
            data.addAll(webService.getAll(new ShoppingListItemParser()));
        } catch (Exception e) {
            Log.e("shopping list", "Error occurred when getting all shopping items: " + e);
        }

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
        try {
            webService.post(values);
        } catch (Exception e) {
            Log.e("shopping list", "Error occurred when inserting item: " + e);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = itemIdMap.get(Long.parseLong(selection));
        Log.i("shopping list", "deleting value: " + selection);
        try {
            webService.delete(id);
        } catch (Exception e) {
            Log.e("shopping item", "Error occurred when deleting item: " + e);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
