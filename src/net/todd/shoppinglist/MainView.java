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
	private IShoppingItemAddedListener addItemListener;
	private IShoppingItemModifiedListener deleteItemListener;
	private IShoppingItemModifiedListener checkItemListener;

	private final Map<String, ViewGroup> shoppingItemViewGroupMap = new HashMap<String, ViewGroup>();
	private final Activity context;
	
	public MainView(final Activity context) {
		this.context = context;
	}
	
	public void setup() {
		context.setContentView(R.layout.activity_main);
		
		attachCreateItemEventing();
	}
	
	private void attachCreateItemEventing() {
		ImageButton addItemButton = (ImageButton)context.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addItemListener.itemCreated(getNewItemText());
			}
        });
    }

	private String getNewItemText() {
    	EditText newItemText = (EditText)context.findViewById(R.id.newItemText);
		return newItemText.getText().toString();
    }
	
    public void addItemButtonListener(final IShoppingItemAddedListener listener) {
        this.addItemListener = listener;
    }
    
    public void addDeleteListener(final IShoppingItemModifiedListener listener) {
    	this.deleteItemListener = listener;
    }

    public void addCheckItemListener(final IShoppingItemModifiedListener listener) {
    	this.checkItemListener = listener;
    }
    
    public void clearNewItemText() {
    	EditText newItemText = (EditText)context.findViewById(R.id.newItemText);
		newItemText.setText("");
    }
    
    public void createNewItem(String id, String value, boolean isChecked) {
    	createWidget(id, value, isChecked);
    	attachEventing(id);
    }
    
    private void createWidget(String id, String value, boolean isChecked) {
    	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	final ViewGroup newListItemViewGroup = (ViewGroup)inflater.inflate(R.layout.item_layout, null, false);
    	
    	TextView newItemTextView = (TextView)newListItemViewGroup.findViewById(R.id.new_item);
    	newItemTextView.setText(value);
    	
    	LinearLayout listContents = (LinearLayout)context.findViewById(R.id.listContents);
    	listContents.addView(newListItemViewGroup, 0);
    	
    	shoppingItemViewGroupMap.put(id, newListItemViewGroup);
    	
    	if (isChecked) {
    		checkItem(id);
    	}
    }

	private void attachEventing(final String newShoppingItemId) {
		addDeleteEventing(newShoppingItemId);
    	addCheckEventing(newShoppingItemId);
	}

	private void addCheckEventing(final String id) {
		ViewGroup newListItemViewGroup = shoppingItemViewGroupMap.get(id);
		newListItemViewGroup.findViewById(R.id.check_item).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkItemListener.itemModified(id);
			}
		});
	}

	private void addDeleteEventing(final String id) {
		ViewGroup newListItemViewGroup = shoppingItemViewGroupMap.get(id);
		newListItemViewGroup.findViewById(R.id.remove_item).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteItemListener.itemModified(id);
			}
		});
	}
    
	public void checkItem(String shoppingItemId) {
		modifyItem(shoppingItemId, R.drawable.check);
    }
	
	public void uncheckItem(String shoppingItemId) {
    	modifyItem(shoppingItemId, R.drawable.uncheck);
    }

	private void modifyItem(String shoppingItemId, int backgroundResource) {
		ViewGroup itemViewGroup = shoppingItemViewGroupMap.get(shoppingItemId);
    	View checkItem = itemViewGroup.findViewById(R.id.check_item);
		checkItem.setBackgroundResource(backgroundResource);
	}
    
    public void removeShoppingItem(String shoppingItemId) {
    	ViewGroup viewItemToRemove = shoppingItemViewGroupMap.get(shoppingItemId);
    	ViewGroup parent = (ViewGroup)viewItemToRemove.getParent();
		parent.removeView(viewItemToRemove);
    }
}
