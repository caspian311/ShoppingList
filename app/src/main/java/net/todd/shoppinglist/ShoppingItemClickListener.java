package net.todd.shoppinglist;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class ShoppingItemClickListener implements android.widget.AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selection = ((TextView) view.findViewById(R.id.shopping_item)).getText().toString();
        Log.i("monkey", "item selected: " + selection);
    }
}
