package edu.ucf.cop4331c.dishdriver.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.ucf.cop4331c.dishdriver.NavigationActivity;
import edu.ucf.cop4331c.dishdriver.R;

/**
 * Created by viviennedo on 3/14/17.
 */


public class PartySizeDialog extends DialogFragment {

    public static PartySizeDialog newInstance(int tablePosition) {
        PartySizeDialog partySizeDialog = new PartySizeDialog();

        Bundle bundle = new Bundle();
        bundle.putInt("TABLE_NUMBER", tablePosition);

        partySizeDialog.setArguments(bundle);

        return partySizeDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflaterCompat = getActivity().getLayoutInflater();
        View dialogView = layoutInflaterCompat.inflate(R.layout.dialog_party_size, null);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), getTheme())
                .setView(dialogView)
                .create();

        int tablePosition = getArguments().getInt("TABLE_NUMBER");

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

//        This was causing problems, so I commented it out. But, this is how you would create an onClickListener
//        Button reserveButton = (Button) dialogView.findViewById(R.id.reserveButton);
//        reserveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Reserve Button Pressed", Toast.LENGTH_SHORT).show();
//                ReservationDialog editNameDialogFragment = ReservationDialog.newInstance("Some Title", tablePosition);
//                editNameDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_reservation");
//            }
//        });


        cardParty1RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Table 1 Button Pressed", Toast.LENGTH_SHORT).show();
                // Here we are making an intent to the Navigation Activity.
                Intent tableIntent = new Intent(getActivity(), NavigationActivity.class);
                // We pass in an extra keyed "PARTY_NUMBER" that contains the integer value that represents the party size.
                tableIntent.putExtra("PARTY_NUMBER", 1);
                getActivity().startActivity(tableIntent);
            }
        });

        // We do the same thing for all the other party sizes

        cardParty2RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Table 2 Button Pressed", Toast.LENGTH_SHORT).show();
                Intent tableIntent = new Intent(getActivity(), NavigationActivity.class);
                tableIntent.putExtra("PARTY_NUMBER", 2);
                getActivity().startActivity(tableIntent);
            }
        });

        cardParty3RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Table 3 Button Pressed", Toast.LENGTH_SHORT).show();
                Intent tableIntent = new Intent(getActivity(), NavigationActivity.class);
                tableIntent.putExtra("PARTY_NUMBER", 3);
                getActivity().startActivity(tableIntent);
            }
        });

        cardParty4RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Table 4 Button Pressed", Toast.LENGTH_SHORT).show();
                Intent tableIntent = new Intent(getActivity(), NavigationActivity.class);
                tableIntent.putExtra("PARTY_NUMBER", 4);
                getActivity().startActivity(tableIntent);
            }
        });

        return alertDialog;
    }


}
