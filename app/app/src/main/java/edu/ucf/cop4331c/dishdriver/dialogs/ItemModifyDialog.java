package edu.ucf.cop4331c.dishdriver.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.ucf.cop4331c.dishdriver.R;
import xdroid.toaster.Toaster;

/**
 * Created by viviennedo on 4/1/17.
 */

public class ItemModifyDialog extends DialogFragment {

    private EditText mModifyEditText;
    private Button mSubmitButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_modify_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mModifyEditText = (EditText) view.findViewById(R.id.itemModifyEditText);
        mSubmitButton = (Button) view.findViewById(R.id.submitButton);
        mSubmitButton.setEnabled(false);


        mModifyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isEmpty(mModifyEditText)) mSubmitButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();

            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


}
