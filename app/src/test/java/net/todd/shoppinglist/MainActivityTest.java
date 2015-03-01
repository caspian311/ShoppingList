package net.todd.shoppinglist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(CustomRobolectricRunner.class)
public class MainActivityTest {
    @Mock
    private static LoaderManager loaderManager;
    @Mock
    private static CursorAdapter adapter;

    private ActivityController<TestMainActivity> controller;
    private MainActivity testObject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(adapter.getViewTypeCount()).thenReturn(1);

        controller = Robolectric.buildActivity(TestMainActivity.class).create();
        testObject = controller.get();
    }

    @Test
    public void onResume_shouldInitializeTheLoader() {
        reset(loaderManager);
        controller.resume();

        verify(loaderManager).restartLoader(eq(MainActivity.SHOPPING_LIST_LOADER), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }

    @Test
    public void loaderShouldPullFromCorrectURI() {
        reset(loaderManager);
        controller.resume();

        ArgumentCaptor<LoaderManager.LoaderCallbacks> captor = ArgumentCaptor.forClass(LoaderManager.LoaderCallbacks.class);
        verify(loaderManager).restartLoader(eq(MainActivity.SHOPPING_LIST_LOADER), any(Bundle.class), captor.capture());
        CursorLoader loader = (CursorLoader)captor.getValue().onCreateLoader(0, null);

        assertEquals(MainActivity.URI, loader.getUri());
    }

    @Test
    public void whenLoaderFinishes_swapTheCursorOnTheAdapter() {
        controller.resume();
        ListView list = (ListView) testObject.findViewById(R.id.list);

        ArgumentCaptor<LoaderManager.LoaderCallbacks> captor = ArgumentCaptor.forClass(LoaderManager.LoaderCallbacks.class);
        verify(loaderManager).restartLoader(eq(MainActivity.SHOPPING_LIST_LOADER), any(Bundle.class), captor.capture());

        Cursor data = mock(Cursor.class);
        captor.getValue().onLoadFinished(null, data);

        verify(adapter).swapCursor(data);
    }

    @Test
    public void onResume_setsAnOnClickListener_onTheList() {
        controller.resume();

        ListView listView = (ListView)testObject.findViewById(R.id.list);
        assertNotNull(listView.getOnItemLongClickListener());
    }

    @Test
    public void onPause_removesTheOnClickListener_onTheList() {
        controller.resume();
        controller.pause();

        ListView listView = (ListView)testObject.findViewById(R.id.list);
        assertNull(listView.getOnItemLongClickListener());
    }

    @Test
    public void onResume_setsAnOnClickListener_onTheAddItemButton() {
        controller.resume();

        View addButton = testObject.findViewById(R.id.add_item_button);
        assertTrue(addButton.hasOnClickListeners());
    }

    @Test
    public void clickingTheAddButtonStartsTheNewItemActivity() {
        controller.resume();

        testObject.findViewById(R.id.add_item_button).performClick();

        ShadowActivity shadow = shadowOf(testObject);
        ShadowActivity.IntentForResult intent = shadow.peekNextStartedActivityForResult();
        assertEquals(NewItemActivity.class.getName(), intent.intent.getComponent().getClassName());
    }

    @Test
    public void clickingTheAddButtonStartsActivityWithExpectedResult() {
        controller.resume();

        testObject.findViewById(R.id.add_item_button).performClick();

        ShadowActivity shadow = shadowOf(testObject);
        ShadowActivity.IntentForResult intent = shadow.peekNextStartedActivityForResult();
        assertEquals(MainActivity.NEW_ITEM_REQUEST, intent.requestCode);
    }

    @Test
    public void onPause_removesTheOnClickListener_onTheAddItemButton() {
        controller.resume();
        controller.pause();

        View addButton = testObject.findViewById(R.id.add_item_button);
        assertFalse(addButton.hasOnClickListeners());
    }

    private static class TestMainActivity extends MainActivity {
        @Override
        public LoaderManager getLoaderManager() {
            return loaderManager;
        }

        @Override
        protected CursorAdapter createAdapter(Context context) {
            return adapter;
        }
    }
}
