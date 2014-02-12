package net.todd.shoppinglist.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.todd.shoppinglist.ShoppingItem;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class DataClient {
	private static final String TAG = DataClient.class.toString();

	private static final String GET_CHANGES_URL = "http://localhost";

	private static final String URL = "http://localhost/app/shoppingItems";
	private static final String URL_WITH_ID = "http://localhost/app/shoppingItems/%s";

	public List<ShoppingListChange> getSince(Date date) {
		return execute(createGetWithTimestamp(date),
				new ResponseParser<List<ShoppingListChange>>() {
					@Override
					public List<ShoppingListChange> parseResponse(String responseData) throws Exception {
						return parseChangeData(responseData);
					}
				});
	}

	private HttpGet createGetWithTimestamp(Date date) {
		HttpGet get = new HttpGet(GET_CHANGES_URL);
		BasicHttpParams params = new BasicHttpParams();
		params.setParameter("since", date);
		get.setParams(params);
		return get;
	}

	private void execute(HttpRequestBase request) {
		execute(request, null);
	}

	private <T> T execute(HttpRequestBase request, ResponseParser<T> responseHandler) {
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				return responseHandler.parseResponse(out.toString());
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

	public List<ShoppingItem> get() {
		return execute(createGet(), new ResponseParser<List<ShoppingItem>>() {
			@Override
			public List<ShoppingItem> parseResponse(String response) throws Exception {
				return parseAllData(response);
			}
		});
	}

	private HttpGet createGet() {
		return new HttpGet(URL);
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

	public void post(Map<String, String> data) {
		execute(createPost(data));
	}

	private HttpPost createPost(Map<String, String> data) {
		HttpPost post = new HttpPost(URL);
		try {
			List<NameValuePair> nameValuePairs = createNameValuePairs(data);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (Exception e) {
			Log.e(TAG, "Could not create new item: " + e.getMessage());
		}
		return post;
	}

	private List<NameValuePair> createNameValuePairs(Map<String, String> data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : data.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, data.get(key)));
		}
		return nameValuePairs;
	}

	public void delete(String id) {
		execute(createDelete(id));
	}

	private HttpDelete createDelete(String id) {
		return new HttpDelete(createUrlWithId(id));
	}

	private String createUrlWithId(String id) {
		return String.format(URL_WITH_ID, id);
	}

	public void put(String id, Map<String, String> data) {
		execute(createPut(id, data));
	}

	private HttpRequestBase createPut(String id, Map<String, String> data) {
		HttpPut put = new HttpPut(createUrlWithId(id));
		try {
			List<NameValuePair> nameValuePairs = createNameValuePairs(data);
			put.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (Exception e) {
			Log.e(TAG, "Could not create new item: " + e.getMessage());
		}
		return put;
	}
}
