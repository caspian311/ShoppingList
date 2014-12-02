package net.todd.shoppinglist;

import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingListItemParser implements ItemParser {
    @Override
    public ShoppingListItem parseItem(JSONObject jsonObject) throws JSONException {
        ShoppingListItem item = new ShoppingListItem();
        item.setName(jsonObject.getString("value"));
        return item;
    }
}
