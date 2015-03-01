package net.todd.shoppinglist;

import android.content.ContentValues;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
public class ListContentProviderTest {
    private ListContentProvider testObject;
    @MockitoAnnotations.Mock
    private WebService webService;
    private List<ShoppingListItem> items;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testObject = new ListContentProvider(webService);

        ShoppingListItem item1 = new ShoppingListItem();
        item1.setName("item 1");
        item1.setRealId(UUID.randomUUID().toString());
        ShoppingListItem item2 = new ShoppingListItem();
        item2.setName("item 2");
        item2.setRealId(UUID.randomUUID().toString());
        items = Arrays.asList(item1, item2);
        when(webService.getAll()).thenReturn(items);
    }

    @Test
    public void query_returnsACursorWithTheSameNumberOfItemsFromService() {
        ShoppingListCursor cursor = testObject.query(null, null, null, null, null);

        assertEquals(2, cursor.getCount());
    }

    @Test
    public void query_returnsACursorWithCorrectData() {
        ShoppingListCursor cursor = testObject.query(null, null, null, null, null);

        cursor.moveToFirst();
        assertEquals("item 1" , cursor.getString(cursor.getColumnIndex("name")));
        assertEquals(1 , cursor.getLong(cursor.getColumnIndex("id")));

        cursor.moveToNext();
        assertEquals(2 , cursor.getLong(cursor.getColumnIndex("id")));
    }

    @Test
    public void inserting_causesAPostOnTheService() throws Exception {
        ContentValues values = new ContentValues();
        values.put("value", UUID.randomUUID().toString());

        testObject.insert(null, values);

        verify(webService).post(values);
    }

    @Test
    public void delete_causesADeleteWithTheGivenItemsId() throws Exception {
        ShoppingListCursor cursor = testObject.query(null, null, null, null, null);
        cursor.moveToFirst();
        long id = cursor.getLong(cursor.getColumnIndex("id"));
        testObject.delete(null, "" + id, null);

        verify(webService).delete(items.get(0).getRealId());
    }
}