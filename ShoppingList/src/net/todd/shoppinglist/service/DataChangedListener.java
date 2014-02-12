package net.todd.shoppinglist.service;

import java.util.List;

public interface DataChangedListener {
	void dataChanged(List<ShoppingListChange> changes);
}
