package net.todd.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
				Log.v("shoppinglist", "click event fired!!!!!!!!!!!!");
				EditText newItemText = (EditText)findViewById(R.id.newItemText);
				createNewItem(newItemText.getText().toString());
			}
        });
        
        Log.v("shoppinglist", "app started!!!!!!!!!!!!");
    }
    
    private void createNewItem(String newItemValue) {
    	LinearLayout listContents = (LinearLayout)findViewById(R.id.listContents);
    	
    	LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	LinearLayout newListItemLayout = (LinearLayout)inflater.inflate(R.layout.item_layout, null, false);
    	
    	TextView newItemTextView = (TextView)newListItemLayout.findViewById(R.id.new_item);
    	newItemTextView.setText(newItemValue);
    	
    	listContents.addView(newListItemLayout, 0);
    }
}
