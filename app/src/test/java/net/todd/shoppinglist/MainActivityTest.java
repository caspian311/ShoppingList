package net.todd.shoppinglist;

import android.app.LoaderManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(CustomRobolectricRunner.class)
public class MainActivityTest {
    @Mock
    private static LoaderManager loaderManager;

    private ActivityController<TestMainActivity> controller;
    private MainActivity testObject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        controller = Robolectric.buildActivity(TestMainActivity.class).create().start();
        testObject = controller.get();
    }

    @Test
    public void onResume_shouldInitializeTheLoader() {
        reset(loaderManager);
        controller.resume();

        verify(loaderManager).initLoader(MainActivity.SHOPPING_LIST_LOADER, null, testObject);
    }

    private static class TestMainActivity extends MainActivity {
        @Override
        public LoaderManager getLoaderManager() {
            return loaderManager;
        }
    }
}
