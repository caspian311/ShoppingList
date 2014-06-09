package net.todd.shoppinglist.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.util.Log;

public class DataClient<T> implements IDataClient<T> {
	private static final String TAG = DataClient.class.toString();

	private Parser<T> parser;
	
	public DataClient(Parser<T> parser) {
		this.parser = parser;
	}
	
	private HttpGet createGetWithParameters(String url, Map<String, String> params) {
		List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();
		for (String parameter : params.keySet()) {
			requestParameters.add(new BasicNameValuePair(parameter, params.get(parameter)));
		}
		url += "?" + URLEncodedUtils.format(requestParameters, "utf-8");
		return new HttpGet(url);
	}

	private void execute(HttpRequestBase request) {
		execute(request, null);
	}

	private List<T> execute(HttpRequestBase request, ResponseParser<T> responseHandler) {
		Log.i(TAG, "http exec: " + request.getRequestLine());
		try {
			HttpResponse response = new DefaultHttpClient().execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				return responseHandler.parseResponse(out.toString());
			} else if (statusCode == HttpStatus.SC_CREATED) {
				return null;
			} else {
				throw new Exception("Status code " + statusCode);
			}
		} catch (Exception e) {
			Log.e(TAG, "Error requesting data from " + request.getRequestLine() + ": " + e.getMessage());
			return null;
		}
	}
	
	public List<T> get(String url) {
		return execute(createGet(url), new ResponseParser<T>() {
			@Override
			public List<T> parseResponse(String response) throws Exception {
				return parseAllData(response);
			}
		});
	}
	
	public List<T> get(String url, Map<String, String> params) {
		return execute(createGetWithParameters(url, params), new ResponseParser<T>() {
			@Override
			public List<T> parseResponse(String response) throws Exception {
				return parseAllData(response);
			}
		});
	}

	private HttpGet createGet(String url) {
		return new HttpGet(url);
	}

	private List<T> parseAllData(String jsonDataAsString)
			throws Exception {
		List<T> list = new ArrayList<T>();

		JSONArray changes = new JSONArray(jsonDataAsString);
		for (int i = 0; i < changes.length(); i++) {
			list.add(parser.parseItem(changes.getJSONObject(i)));
		}

		return list;
	}

	public void post(String url, Map<String, String> data) {
		execute(createPost(url, data));
	}

	private HttpEntityEnclosingRequestBase createPost(String url, Map<String, String> data) {
		return attachDataToRequest(new HttpPost(url), data);
	}

	private HttpEntityEnclosingRequestBase attachDataToRequest(HttpEntityEnclosingRequestBase post, Map<String, String> data) {
		try {
			List<NameValuePair> nameValuePairs = createNameValuePairs(data);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (Exception e) {
			Log.e(TAG, "Could not encode parameters: " + e.getMessage());
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

	public void delete(String url) {
		execute(createDelete(url));
	}

	private HttpDelete createDelete(String url) {
		return new HttpDelete(url);
	}
}
