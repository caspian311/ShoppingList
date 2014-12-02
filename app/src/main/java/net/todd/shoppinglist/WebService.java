package net.todd.shoppinglist;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class WebService<T> {
    public List<T> getAll(String url, ItemParser<T> itemParser) {
        try {
            HttpRequestBase request = new HttpGet(url);
            HttpResponse response = new DefaultHttpClient().execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                List<T> list = new ArrayList<T>();
                JSONArray array = new JSONArray(out.toString());
                for (int i=0; i<array.length(); i++) {
                    list.add(itemParser.parseItem(array.getJSONObject(i)));
                }
                return list;
            } else {
                throw new RuntimeException("HTTP Server Error: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
