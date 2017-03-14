package edu.ucf.cop4331c.dishdriver.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import edu.ucf.cop4331c.dishdriver.R;

/**
 * Created by viviennedo on 3/14/17.
 */

public class PartySizeDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setView(R.layout.dialog_party_size)
                .setPositiveButton("Reserve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when they click reserve.
                    }
                })
                .create();
    }
}
