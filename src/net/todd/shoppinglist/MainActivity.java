package net.todd.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static class CheckboxStateMaintainer implements OnClickListener {
    	private boolean isChecked;

		@Override
		public void onClick(View v) {
			if (isChecked) {
				isChecked = false;
				v.setBackgroundResource(R.drawable.uncheck);
			} else {
				isChecked = true;
				v.setBackgroundResource(R.drawable.check);
			}
		}
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);

        ImageButton addItemButton = (ImageButton)findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View button) {
				EditText newItemText = (EditText)findViewById(R.id.newItemText);
				createNewItem(newItemText.getText().toString());
				newItemText.setText("");
			}
        });
    }
    
    private void createNewItem(String newItemValue) {
    	ViewGroup newListItemViewGroup = createNewItem();
    	
    	populateName(newItemValue, newListItemViewGroup);
    	setupDeleteEvent(newListItemViewGroup);
    	setupCheckEvent(newListItemViewGroup);

    	attachNewItem(newListItemViewGroup);
    }

	private void setupCheckEvent(final ViewGroup newListItemViewGroup) {
		newListItemViewGroup.findViewById(R.id.check_item).setOnClickListener(new CheckboxStateMaintainer());
	}

	private void setupDeleteEvent(final ViewGroup newListItemViewGroup) {
		newListItemViewGroup.findViewById(R.id.remove_item).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewGroup parent = (ViewGroup)newListItemViewGroup.getParent();
				parent.removeView(newListItemViewGroup);
			}
		});
	}

	private void attachNewItem(ViewGroup newListItemLayout) {
		LinearLayout listContents = (LinearLayout)findViewById(R.id.listContents);
    	listContents.addView(newListItemLayout, 0);
	}

	private void populateName(String newItemValue, ViewGroup newListItemLayout) {
		TextView newItemTextView = (TextView)newListItemLayout.findViewById(R.id.new_item);
    	newItemTextView.setText(newItemValue);
	}

	private ViewGroup createNewItem() {
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	ViewGroup newListItemLayout = (ViewGroup)inflater.inflate(R.layout.item_layout, null, false);
		return newListItemLayout;
	}
}
