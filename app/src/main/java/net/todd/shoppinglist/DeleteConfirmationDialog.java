package net.todd.shoppinglist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DeleteConfirmationDialog {
    private final AlertDialog.Builder builder;

    public DeleteConfirmationDialog(Context context) {
        this.builder = new AlertDialog.Builder(context)
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(R.string.delete_button_text, null)
                .setNegativeButton(R.string.cancel_delete_button, null);
    }

    public void setPositiveButtonClickListener(DialogInterface.OnClickListener clickListener) {
        builder.setPositiveButton(R.string.delete_button_text, clickListener);
    }

    public void show() {
        builder.create().show();
    }
}
