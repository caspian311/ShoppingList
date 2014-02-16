package net.todd.shoppinglist.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class ShoppingItemsChangesClient {

private static final String BASE_URL = "http://10.0.2.2:3000";
	
	private static final String CHANGES_URL = BASE_URL + "/changes";

	private final IDataClient<ShoppingListChange> dataClient;
	
	public ShoppingItemsChangesClient() {
		dataClient = new DataClient<ShoppingListChange>(new Parser<ShoppingListChange>() {
			@Override
			public ShoppingListChange parseItem(JSONObject json)
					throws Exception {
				String id = json.getString("itemId");
				int changeType = json.getInt("changeType");
				String name = null;
				if (changeType == ShoppingListChange.CREATED) {
					name = json.getString("name");
				}
				return new ShoppingListChange(id, changeType, name);
			}
		});
	}
	
	public List<ShoppingListChange> getSince(Date lastChangesReceived) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", "" + lastChangesReceived.getTime());
		return dataClient.get(CHANGES_URL, params);
	}
}
