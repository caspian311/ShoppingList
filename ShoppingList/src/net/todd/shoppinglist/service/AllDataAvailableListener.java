package net.todd.shoppinglist.service;

import java.util.List;

import net.todd.shoppinglist.ShoppingItem;

public interface AllDataAvailableListener {
	void allItemsAvailable(List<ShoppingItem> allItems);
}
