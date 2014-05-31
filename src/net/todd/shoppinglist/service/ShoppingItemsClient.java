package net.todd.shoppinglist.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import net.todd.shoppinglist.ShoppingItem;

import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

public class ShoppingItemsClient {
	protected static final String TAG = ShoppingItemsClient.class.toString();
	
	private static final String DEFAULT_BASE_URL = "http://10.0.2.2:3000";
	private static final String PRODUCTION_BASE_URL = "http://caspian311-shoppinglist.herokuapp.com";

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
		return dataClient.get(shoppingItemsUrl());
	}
	
	private String shoppingItemsUrl() {
		return MessageFormat.format("{0}/shoppingItems", baseUrl());
	}
	
	private String shoppingItemsUrlWithId(String id) {
		return MessageFormat.format("{0}/shoppingItems/{1}", baseUrl(), id);
	}

	private String baseUrl() {
		if (Build.PRODUCT.contains("sdk")) {
			return DEFAULT_BASE_URL;
		}
		return PRODUCTION_BASE_URL;
	}

	public void post(Map<String, String> data) {
		dataClient.post(shoppingItemsUrl(), data);
	}

	public void delete(String id) {
		dataClient.delete(shoppingItemsUrlWithId(id));
	}

	public void put(String id, Map<String, String> data) {
		dataClient.put(shoppingItemsUrlWithId(id), data);
	}
}
