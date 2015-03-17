package net.todd.shoppinglist;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowContentResolver;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(CustomRobolectricRunner.class)
public class TaskManagerTest {
    @Mock
    private CursorAdapter adapter;
    @Mock
    private static LoaderManager loaderManager;

    private TaskManager testObject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Activity testActivity = Robolectric.buildActivity(TestActivity.class).create().get();

        testObject = new TaskManager(testActivity, testActivity.getContentResolver(), testActivity.getLoaderManager(), adapter);
    }

    @Test
    public void loadTask_shouldStartACursorLoader() {
        testObject.loadTasks();

        ArgumentCaptor<LoaderManager.LoaderCallbacks> captor = ArgumentCaptor.forClass(LoaderManager.LoaderCallbacks.class);
        verify(loaderManager).restartLoader(anyInt(), any(Bundle.class), captor.capture());
        Loader loader = captor.getValue().onCreateLoader(0, null);

        assertTrue(loader instanceof CursorLoader);
    }

    @Test
    public void loadTask_whenTaskDoneLoading_swapCursor() {
        testObject.loadTasks();

        ArgumentCaptor<LoaderManager.LoaderCallbacks> captor = ArgumentCaptor.forClass(LoaderManager.LoaderCallbacks.class);
        verify(loaderManager).restartLoader(anyInt(), any(Bundle.class), captor.capture());

        Cursor data = mock(Cursor.class);
        captor.getValue().onLoadFinished(null, data);

        verify(adapter).swapCursor(data);
    }

    @Test
    public void onInsertComplete_restartLoader_whenLoadingDone_swapCursor() {
        testObject.onInsertComplete(0, null, null);

        ArgumentCaptor<LoaderManager.LoaderCallbacks> captor = ArgumentCaptor.forClass(LoaderManager.LoaderCallbacks.class);
        verify(loaderManager).restartLoader(anyInt(), any(Bundle.class), captor.capture());

        Cursor data = mock(Cursor.class);
        captor.getValue().onLoadFinished(null, data);

        verify(adapter).swapCursor(data);
    }

    @Test
    public void onDeleteComplete_restartLoader_whenLoadingDone_swapCursor() {
        testObject.onDeleteComplete(0, null, 0);

        ArgumentCaptor<LoaderManager.LoaderCallbacks> captor = ArgumentCaptor.forClass(LoaderManager.LoaderCallbacks.class);
        verify(loaderManager).restartLoader(anyInt(), any(Bundle.class), captor.capture());

        Cursor data = mock(Cursor.class);
        captor.getValue().onLoadFinished(null, data);

        verify(adapter).swapCursor(data);
    }

    private static class TestActivity extends Activity
    {
        @Override
        public LoaderManager getLoaderManager() {
            return loaderManager;
        }
    }
}