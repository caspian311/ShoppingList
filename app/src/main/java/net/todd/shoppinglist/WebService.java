package net.todd.shoppinglist;

import android.content.ContentValues;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebService<T> {
    private final ItemParser<T> itemParser;
    private String url;

    public WebService(String url, ItemParser<T> itemParser) {
        this.url = url;
        this.itemParser = itemParser;
    }

    public List<T> getAll() throws Exception {
        HttpRequestBase request = new HttpGet(url);
        HttpResponse response = new DefaultHttpClient().execute(request);

        List<T> list = new ArrayList<T>();

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            JSONArray array = getResponseAsAJsonArray(response);

            for (int i = 0; i < array.length(); i++) {
                list.add(itemParser.parseItem(array.getJSONObject(i)));
            }
        } else {
            throwHttpError(response);
        }
        return list;
    }

    private JSONArray getResponseAsAJsonArray(HttpResponse response) throws IOException, JSONException {
        return new JSONArray(getResponseString(response));
    }

    private String getResponseString(HttpResponse response) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.getEntity().writeTo(out);
        out.close();
        return out.toString();
    }

    private void throwHttpError(HttpResponse response) throws Exception {
        throw new Exception("HTTP Server Error: " + response.getStatusLine().getStatusCode());
    }

    public void post(ContentValues contentValues) throws Exception {
        HttpPost request = new HttpPost(url);

        List<NameValuePair> namedValuePairs = new ArrayList<NameValuePair>();
        for (String key : contentValues.keySet()) {
            String value = contentValues.getAsString(key);
            namedValuePairs.add(new BasicNameValuePair(key, value));
        }

        request.setEntity(new UrlEncodedFormEntity(namedValuePairs));
        HttpResponse response = new DefaultHttpClient().execute(request);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
            throwHttpError(response);
        }
    }

    public void delete(String id) throws Exception {
        HttpDelete request = new HttpDelete(url + "/" + id);
        HttpResponse response = new DefaultHttpClient().execute(request);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_ACCEPTED) {
            throwHttpError(response);
        }
    }
}
