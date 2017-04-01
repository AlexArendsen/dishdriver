package edu.ucf.cop4331c.dishdriver.dialogs;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


import edu.ucf.cop4331c.dishdriver.R;

/**
 * Created by viviennedo on 3/15/17.
 */

public class ReservationDialog extends DialogFragment {

    private EditText mEditText;
    private CheckBox mDepositCheckBox;
    private Button mSubmitButton;

    public ReservationDialog() {

        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReservationDialog newInstance(String title) {
        ReservationDialog frag = new ReservationDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_reservation, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        mDepositCheckBox = (CheckBox) view.findViewById(R.id.checkbox_deposit);
        mSubmitButton = (Button) view.findViewById(R.id.confirmation_button);

        mSubmitButton.setEnabled(false);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // This is how you start the nag activity
                // getActivity().startActivity();
            }
        });

        mDepositCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSubmitButton.setEnabled(true);
                } else {
                    mSubmitButton.setEnabled(false);
                }
            }
        });

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}