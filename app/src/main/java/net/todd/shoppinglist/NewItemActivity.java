package net.todd.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewItemActivity extends Activity {
    public static final String NEW_ITEM_NAME = "newItemName";

    private TextWatcher textChangedListener;
    private EditText newItemText;
    private Button addItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        newItemText = (EditText)findViewById(R.id.new_item_text);
        addItemButton = (Button)findViewById(R.id.create_new_item);

        textChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                addItemButton.setEnabled(newItemText.getText().toString().length() != 0);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        newItemText.addTextChangedListener(textChangedListener);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItemName = newItemText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(NEW_ITEM_NAME, newItemName);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        addItemButton.setOnClickListener(null);
        newItemText.removeTextChangedListener(textChangedListener);
    }
}
