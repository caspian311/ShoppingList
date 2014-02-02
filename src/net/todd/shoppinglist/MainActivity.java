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
    	LinearLayout listContents = (LinearLayout)findViewById(R.id.listContents);
    	
    	LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	ViewGroup newListItemLayout = (ViewGroup)inflater.inflate(R.layout.item_layout, null, false);
    	
    	TextView newItemTextView = (TextView)newListItemLayout.findViewById(R.id.new_item);
    	newItemTextView.setText(newItemValue);
    	
    	listContents.addView(newListItemLayout, 0);
    }
}