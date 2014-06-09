package net.todd.shoppinglist.service;

import java.util.List;
import java.util.Map;

public interface IDataClient<T> {
	List<T> get(String url, Map<String, String> params);
	List<T> get(String url);
	void post(String url, Map<String, String> data);
	void delete(String url);
}
