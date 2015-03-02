package net.todd.shoppinglist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(CustomRobolectricRunner.class)
public class ShoppingListCursorTest {

    private List<ShoppingListItem> data;
    private ShoppingListCursor testObject;

    @Before
    public void setUp() {
        ShoppingListItem item1 = new ShoppingListItem();
        item1.setId(123l);
        item1.setRealId(UUID.randomUUID().toString());
        item1.setName("item 1");
        ShoppingListItem item2 = new ShoppingListItem();
        item2.setId(456l);
        item2.setRealId(UUID.randomUUID().toString());
        item2.setName("item 2");
        ShoppingListItem item3 = new ShoppingListItem();
        item3.setId(789l);
        item3.setRealId(UUID.randomUUID().toString());
        item3.setName("item 3");
        data = Arrays.asList(item1, item2, item3);

        testObject = new ShoppingListCursor(data);

        testObject.getCount();
    }

    @Test
    public void countShouldBeAccurate() {
        assertEquals(3, testObject.getCount());
    }

    @Test
    public void getStringForNameShouldWork() {
        testObject.moveToFirst();
        assertEquals("item 1", testObject.getString(testObject.getColumnIndex("name")));
        testObject.moveToNext();
        assertEquals("item 2", testObject.getString(testObject.getColumnIndex("name")));
        testObject.moveToNext();
        assertEquals("item 3", testObject.getString(testObject.getColumnIndex("name")));
    }

    @Test
    public void getLongForIdShouldWork() {
        testObject.moveToFirst();
        assertEquals(123l, testObject.getLong(testObject.getColumnIndex("_id")));
        testObject.moveToNext();
        assertEquals(456l, testObject.getLong(testObject.getColumnIndex("_id")));
        testObject.moveToNext();
        assertEquals(789l, testObject.getLong(testObject.getColumnIndex("_id")));
    }
}