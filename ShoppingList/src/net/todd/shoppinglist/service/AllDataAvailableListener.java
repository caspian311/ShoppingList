package net.todd.shoppinglist.service;

import java.util.Map;

import net.todd.shoppinglist.ShoppingItem;

public interface AllDataAvailableListener {
	void allItemsAvailable(Map<String, ShoppingItem> remoteItems);
}
