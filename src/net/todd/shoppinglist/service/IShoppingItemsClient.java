package net.todd.shoppinglist.service;

import java.util.List;
import java.util.Map;

import net.todd.shoppinglist.ShoppingItem;

public interface IShoppingItemsClient {
	List<ShoppingItem> get();
	void post(Map<String, String> data);
	void delete(String id);
}
