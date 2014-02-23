package net.todd.shoppinglist.service;

import java.util.List;
import java.util.Map;

import net.todd.shoppinglist.ShoppingItem;

import org.json.JSONObject;

import android.util.Log;

public class ShoppingItemsClient {
	protected static final String TAG = ShoppingItemsClient.class.toString();
	
	private static final String BASE_URL = "http://10.0.2.2:3000";
	private static final String SHOPPING_ITEMS_URL = BASE_URL + "/shoppingItems";
	private static final String SHOPPING_ITEMS_URL_WITH_ID = BASE_URL + "/shoppingItems/%s";

	private IDataClient<ShoppingItem> dataClient;
	
	public ShoppingItemsClient() {
		dataClient = new DataClient<ShoppingItem>(new Parser<ShoppingItem>() {
			@Override
			public ShoppingItem parseItem(JSONObject json) throws Exception {
				Log.i(TAG, "json received: " + json);
				String id = json.getString("id");
				String value = json.getString("value");
				boolean checked = json.getBoolean("checked");
				return new ShoppingItem(id, value, checked);
			}
		});
	}
	
	public List<ShoppingItem> get() {
		return dataClient.get(SHOPPING_ITEMS_URL);
	}

	public void post(Map<String, String> data) {
		dataClient.post(SHOPPING_ITEMS_URL, data);
	}

	public void delete(String id) {
		dataClient.delete(createUrlWithId(id));
	}

	public void put(String id, Map<String, String> data) {
		dataClient.put(createUrlWithId(id), data);
	}

	private String createUrlWithId(String id) {
		return String.format(SHOPPING_ITEMS_URL_WITH_ID, id);
	}
}
