package net.todd.shoppinglist.service;

import org.json.JSONObject;

public interface Parser<T> {
	T parseItem(JSONObject json) throws Exception;
}
