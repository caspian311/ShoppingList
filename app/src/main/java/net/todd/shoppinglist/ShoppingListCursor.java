package net.todd.shoppinglist;

import android.database.AbstractCursor;

import java.util.List;

public class ShoppingListCursor extends AbstractCursor {
    private final List<ShoppingListItem> data;

    public ShoppingListCursor(List<ShoppingListItem> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{ "_id", "name" };
    }

    @Override
    public String getString(int column) {
        return data.get(mPos).getName();
    }

    @Override
    public short getShort(int column) {
        return 0;
    }

    @Override
    public int getInt(int column) {
        return 0;
    }

    @Override
    public long getLong(int column) {
        return 0;
    }

    @Override
    public float getFloat(int column) {
        return 0;
    }

    @Override
    public double getDouble(int column) {
        return 0;
    }

    @Override
    public boolean isNull(int column) {
        return false;
    }
}
