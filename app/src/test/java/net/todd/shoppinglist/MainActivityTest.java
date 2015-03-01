package net.todd.shoppinglist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.MatrixCursor;
import android.os.Bundle;
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
import org.robolectric.util.ActivityController;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        verify(loaderManager).initLoader(eq(MainActivity.SHOPPING_LIST_LOADER), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class));
    }

    @Test
    public void loaderShouldPullFromCorrectURI() {
        reset(loaderManager);
        controller.resume();

        ArgumentCaptor<LoaderManager.LoaderCallbacks> captor = ArgumentCaptor.forClass(LoaderManager.LoaderCallbacks.class);
        verify(loaderManager).initLoader(eq(MainActivity.SHOPPING_LIST_LOADER), any(Bundle.class), captor.capture());
        CursorLoader loader = (CursorLoader)captor.getValue().onCreateLoader(0, null);

        assertEquals(MainActivity.URI, loader.getUri());
    }

    @Test
    public void whenLoaderFinishes_swapTheCursorOnTheAdapter() {
        controller.resume();
        ListView list = (ListView) testObject.findViewById(R.id.list);

        ArgumentCaptor<LoaderManager.LoaderCallbacks> captor = ArgumentCaptor.forClass(LoaderManager.LoaderCallbacks.class);
        verify(loaderManager).initLoader(eq(MainActivity.SHOPPING_LIST_LOADER), any(Bundle.class), captor.capture());

        Cursor data = mock(Cursor.class);
        captor.getValue().onLoadFinished(null, data);

        verify(adapter).swapCursor(data);
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
