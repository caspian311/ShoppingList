package net.todd.shoppinglist;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainView implements IMainView {
	private Activity context;
	
	private IListener addItemListener;
	private IListener deleteListener;
	private IListener checkItemListener;

	private ShoppingItem itemToDelete;
	private ShoppingItem itemToCheck;

	public MainView(final Activity context) {
		this.context = context;
	}
	
	public void setup() {
		context.setContentView(R.layout.activity_main);
		
		ImageButton addItemButton = (ImageButton)context.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addItemListener.handle();	
			}
        });
	}
	
    public void addItemButtonListener(final IListener listener) {
        this.addItemListener = listener;
    }
    
    public void addDeleteListener(final IListener listener) {
    	this.deleteListener = listener;
    }

    public void addCheckItemListener(final IListener listener) {
    	this.checkItemListener = listener;
    }
    
    public ShoppingItem getItemToDelete() {
    	return this.itemToDelete;
    }
    
    public void clearNewItemText() {
    	EditText newItemText = (EditText)context.findViewById(R.id.newItemText);
		newItemText.setText("");
    }
    
    public String getNewItemText() {
    	EditText newItemText = (EditText)context.findViewById(R.id.newItemText);
		return newItemText.getText().toString();
    }

    public void createNewItem(final ShoppingItem newItemValue) {
    	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	final ViewGroup newListItemViewGroup = (ViewGroup)inflater.inflate(R.layout.item_layout, null, false);
    	
    	TextView newItemTextView = (TextView)newListItemViewGroup.findViewById(R.id.new_item);
    	newItemTextView.setText(newItemValue.getValue());
    	
    	LinearLayout listContents = (LinearLayout)context.findViewById(R.id.listContents);
    	listContents.addView(newListItemViewGroup, 0);
    	
    	shoppingItemViewGroupMap.put(newItemValue, newListItemViewGroup);
    	
    	newListItemViewGroup.findViewById(R.id.remove_item).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				itemToDelete = newItemValue;
				deleteListener.handle();
			}
		});
    	newListItemViewGroup.findViewById(R.id.check_item).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				itemToCheck = newItemValue;
				checkItemListener.handle();
			}
		});
    }
    
    public ShoppingItem getItemToCheck() {
    	return itemToCheck;
    }
    
	public void checkItem(ShoppingItem shoppingItem) {
    	ViewGroup itemViewGroup = shoppingItemViewGroupMap.get(shoppingItem);
    	View checkItem = itemViewGroup.findViewById(R.id.check_item);
    	if (shoppingItem.isChecked()) {
    		checkItem.setBackgroundResource(R.drawable.check);
    	} else {
    		checkItem.setBackgroundResource(R.drawable.uncheck);
    	}
    }
    
    private final Map<ShoppingItem, ViewGroup> shoppingItemViewGroupMap = new HashMap<ShoppingItem, ViewGroup>();
    
    public void removeShoppingItem(ShoppingItem shoppingItem) {
    	ViewGroup viewItemToRemove = shoppingItemViewGroupMap.get(shoppingItem);
    	ViewGroup parent = (ViewGroup)viewItemToRemove.getParent();
		parent.removeView(viewItemToRemove);
    }
}
