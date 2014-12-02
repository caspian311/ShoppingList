package net.todd.shoppinglist;

import org.json.JSONException;
import org.json.JSONObject;

public interface ItemParser<T> {
    T parseItem(JSONObject jsonObject) throws JSONException;
}
