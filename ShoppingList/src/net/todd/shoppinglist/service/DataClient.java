package net.todd.shoppinglist.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.todd.shoppinglist.ShoppingItem;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.JsonWriter;
import android.util.Log;

public class DataClient {
	private static final String TAG = DataClient.class.toString();

	private static final String GET_CHANGES_URL = "http://localhost";
	private static final String GET_ALL_DATA_URL = "http://localhost";
	private static final String POST_NEW_ITEM_URL = "http://localhost";

	public List<ShoppingListChange> getChangesSince(Date date) {
		HttpGet get = new HttpGet(GET_CHANGES_URL);
		BasicHttpParams params = new BasicHttpParams();
		params.setParameter("since", date);
		get.setParams(params);
		return handleResponse(get,
				new HandleResponse<List<ShoppingListChange>>() {
					@Override
					public List<ShoppingListChange> onResponse(
							String responseData) throws Exception {
						return parseChangeData(responseData);
					}
				});
	}

	private <T> T handleResponse(HttpRequestBase request,
			HandleResponse<T> responseHandler) {
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				return responseHandler.onResponse(out.toString());
			} else {
				throw new Exception("Status code " + statusCode);
			}
		} catch (Exception e) {
			Log.e(TAG, "Error requesting data: " + e.getMessage());
			return null;
		}
	}

	private List<ShoppingListChange> parseChangeData(String jsonDataAsString)
			throws Exception {
		List<ShoppingListChange> list = new ArrayList<ShoppingListChange>();

		JSONArray changes = new JSONObject(jsonDataAsString)
				.getJSONArray("changes");
		for (int i = 0; i < changes.length(); i++) {
			list.add(parseChangeItem(changes.getJSONObject(i)));
		}

		return list;
	}

	private ShoppingListChange parseChangeItem(JSONObject change)
			throws Exception {
		String id = change.getString("id");
		int changeType = change.getInt("changeType");
		String name = change.getString("name");

		return new ShoppingListChange(id, changeType, name);
	}

	public List<ShoppingItem> getAllData() {
		HttpGet get = new HttpGet(GET_ALL_DATA_URL);
		return handleResponse(get, new HandleResponse<List<ShoppingItem>>(){
			@Override
			public List<ShoppingItem> onResponse(String response)
					throws Exception {
				return parseAllData(response);
			}
		});
	}

	private List<ShoppingItem> parseAllData(String jsonDataAsString)
			throws Exception {
		List<ShoppingItem> list = new ArrayList<ShoppingItem>();

		JSONArray changes = new JSONObject(jsonDataAsString)
				.getJSONArray("data");
		for (int i = 0; i < changes.length(); i++) {
			list.add(parseShoppingItem(changes.getJSONObject(i)));
		}

		return list;
	}

	private ShoppingItem parseShoppingItem(JSONObject item) throws Exception {
		String id = item.getString("id");
		String value = item.getString("value");

		return new ShoppingItem(id, value);
	}

	public boolean saveNewItem(String newItemText) {
		HttpPost post = new HttpPost(POST_NEW_ITEM_URL);
		try {
			post.setEntity(new StringEntity(createJsonForNewItem(newItemText)));
		} catch (Exception e) {
			Log.e(TAG, "Could not create new item: " + e.getMessage());
		}
		
		return handleResponse(post, new HandleResponse<Boolean>() {
			@Override
			public Boolean onResponse(String response) throws Exception {
				return true;
			}
		});
	}

	private String createJsonForNewItem(String newItemText) throws IOException {
		StringWriter out = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(out);
		jsonWriter.beginObject();
		jsonWriter.name("value").value(newItemText);
		jsonWriter.endObject();
		jsonWriter.close();
		String json = out.toString();
		return json;
	}

	private static interface HandleResponse<T> {
		T onResponse(String response) throws Exception;
	}
}
