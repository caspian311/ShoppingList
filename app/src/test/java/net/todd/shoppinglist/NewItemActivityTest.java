package net.todd.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(CustomRobolectricRunner.class)
public class NewItemActivityTest {

    private ActivityController<NewItemActivity> controller;
    private NewItemActivity testObject;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(NewItemActivity.class).create();
        testObject = controller.get();
    }

    @Test
    public void onResume_newItemButtonHasAClickListener() {
        controller.resume();

        View view = testObject.findViewById(R.id.create_new_item);

        assertTrue(view.hasOnClickListeners());
    }

    @Test
    public void onPause_newItemButtonHasNoClickListener() {
        controller.resume();
        controller.pause();

        View view = testObject.findViewById(R.id.create_new_item);

        assertFalse(view.hasOnClickListeners());
    }

    @Test
    public void initiallyCreateNewItemButtonIsDisabled() {
        controller.resume();

        Button button = (Button)testObject.findViewById(R.id.create_new_item);

        assertFalse(button.isEnabled());
    }

    @Test
    public void createNewItemButtonIsEnabled_whenTextIsPutInTheTextBox() {
        controller.resume();

        EditText newItemText = (EditText)testObject.findViewById(R.id.new_item_text);
        Button button = (Button)testObject.findViewById(R.id.create_new_item);

        newItemText.setText("a");

        assertTrue(button.isEnabled());
    }

    @Test
    public void createNewItemButtonIsDisabled_whenTextIsRemovedFromTheTextBox() {
        controller.resume();

        EditText newItemText = (EditText)testObject.findViewById(R.id.new_item_text);
        Button button = (Button)testObject.findViewById(R.id.create_new_item);

        newItemText.setText("a");
        newItemText.setText("");

        assertFalse(button.isEnabled());
    }

    @Test
    public void onPause_textChangeListenerIsRemove() {
        controller.resume();
        controller.pause();

        EditText newItemText = (EditText)testObject.findViewById(R.id.new_item_text);
        Button button = (Button)testObject.findViewById(R.id.create_new_item);

        newItemText.setText("a");

        assertFalse(button.isEnabled());
    }

    @Test
    public void clickingTheAddItemButton_returnsBackToTheMainActivity_WithCorrectReturnCode() {
        controller.resume();

        EditText newItemText = (EditText)testObject.findViewById(R.id.new_item_text);
        Button button = (Button)testObject.findViewById(R.id.create_new_item);

        newItemText.setText("a");
        button.performClick();

        ShadowActivity shadowActivity = shadowOf(testObject);

        assertEquals(Activity.RESULT_OK, shadowActivity.getResultCode());
    }

    @Test
    public void clickingTheAddItemButton_returnsBackToTheMainActivity_WithNewItemName() {
        controller.resume();

        String expectedNewItemName = UUID.randomUUID().toString();

        EditText newItemText = (EditText)testObject.findViewById(R.id.new_item_text);
        Button button = (Button)testObject.findViewById(R.id.create_new_item);

        newItemText.setText(expectedNewItemName);
        button.performClick();

        ShadowActivity shadowActivity = shadowOf(testObject);
        Intent resultIntent = shadowActivity.getResultIntent();
        String actualNewItemName = resultIntent.getStringExtra(NewItemActivity.NEW_ITEM_NAME);

        assertEquals(expectedNewItemName, actualNewItemName);
    }

    @Test
    public void clickingTheAddItemButton_finishesTheActivity() {
        controller.resume();

        EditText newItemText = (EditText)testObject.findViewById(R.id.new_item_text);
        Button button = (Button)testObject.findViewById(R.id.create_new_item);

        newItemText.setText("a");
        button.performClick();

        assertTrue(testObject.isFinishing());
    }
}