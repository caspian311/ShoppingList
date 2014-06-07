package net.todd.shoppinglist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShoppingItemAdapter extends ArrayAdapter<ShoppingItem> {
	private Context context;
	private ArrayList<ShoppingItem> values;

	public ShoppingItemAdapter(Context context, ArrayList<ShoppingItem> values) {
		super(context, R.layout.item_layout, values);

		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.item_layout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.new_item);

		textView.setText(values.get(position).getValue());

		return rowView;
	}
}