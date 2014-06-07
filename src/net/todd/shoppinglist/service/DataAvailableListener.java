package net.todd.shoppinglist.service;

import java.util.List;

import net.todd.shoppinglist.ShoppingItem;

public interface DataAvailableListener {
	void allItemsAvailable(List<ShoppingItem> items);
}
