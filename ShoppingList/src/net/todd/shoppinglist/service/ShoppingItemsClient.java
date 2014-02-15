package net.todd.shoppinglist.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import net.todd.shoppinglist.ShoppingItem;

public class ShoppingItemsClient {

private static final String BASE_URL = "http://10.0.2.2/app";
	
	private static final String SHOPPING_ITEMS_URL = BASE_URL + "/shoppingItems";
	private static final String SHOPPING_ITEMS_URL_WITH_ID = BASE_URL + "/shoppingItems/%s";

	private IDataClient<ShoppingItem> dataClient;
	
	public ShoppingItemsClient() {
		dataClient = new DataClient<ShoppingItem>(new Parser<ShoppingItem>() {
			@Override
			public ShoppingItem parseItem(JSONObject json) throws Exception {
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
