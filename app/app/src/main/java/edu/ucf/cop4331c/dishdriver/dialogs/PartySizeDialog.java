package edu.ucf.cop4331c.dishdriver.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucf.cop4331c.dishdriver.R;

/**
 * Created by viviennedo on 3/14/17.
 */

public class PartySizeDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflaterCompat = getActivity().getLayoutInflater();
        View dialogView = layoutInflaterCompat.inflate(R.layout.dialog_party_size, null);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), getTheme())
                .setView(dialogView)
                .create();

        RelativeLayout cardParty1RelativeLayout = (RelativeLayout) dialogView.findViewById(R.id.cardParty1);
        RelativeLayout cardParty2RelativeLayout = (RelativeLayout) dialogView.findViewById(R.id.cardParty2);
        RelativeLayout cardParty3RelativeLayout = (RelativeLayout) dialogView.findViewById(R.id.cardParty3);
        RelativeLayout cardParty4RelativeLayout = (RelativeLayout) dialogView.findViewById(R.id.cardParty4);

        TextView cardParty1TextView = (TextView) cardParty1RelativeLayout.findViewById(R.id.tableNameTextView);
        TextView cardParty2TextView = (TextView) cardParty2RelativeLayout.findViewById(R.id.tableNameTextView);
        TextView cardParty3TextView = (TextView) cardParty3RelativeLayout.findViewById(R.id.tableNameTextView);
        TextView cardParty4TextView = (TextView) cardParty4RelativeLayout.findViewById(R.id.tableNameTextView);

        cardParty1TextView.setText("1");
        cardParty2TextView.setText("2");
        cardParty3TextView.setText("3");
        cardParty4TextView.setText("4");

        Button reserveButton = (Button) dialogView.findViewById(R.id.reserveButton);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Reserve Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        cardParty1RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Table 1 Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        cardParty2RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Table 2 Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        cardParty3RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Table 3 Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        cardParty4RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Table 4 Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        return alertDialog;
    }
}
