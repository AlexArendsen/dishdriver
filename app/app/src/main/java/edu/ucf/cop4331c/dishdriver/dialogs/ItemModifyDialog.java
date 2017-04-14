package edu.ucf.cop4331c.dishdriver.dialogs;

import android.content.DialogInterface;
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

import com.google.gson.Gson;

import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.models.OrderModel;
import edu.ucf.cop4331c.dishdriver.models.OrderedDishModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import xdroid.toaster.Toaster;

/**
 * Created by viviennedo on 4/1/17.
 */

public class ItemModifyDialog extends DialogFragment {

//    public interface MyDialogFragmentListener {
//        public String onReturnValue(String foo);
//    }

    public static ItemModifyDialog newInstance(OrderedDishModel orderedDishModel) {

        ItemModifyDialog frag = new ItemModifyDialog();
        Bundle args = new Bundle();
        args.putString("ORDER_DISH_MODEL", new Gson().toJson(orderedDishModel));
        frag.setArguments(args);
        return frag;
    }


    private EditText mModifyEditText;
    private Button mSubmitButton;
    private OrderedDishModel mOrderDishModel;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_modify_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

//        ItemModifyDialog itemModifyDialog = new ItemModifyDialog();
        Bundle bundle = new Bundle();
        mOrderDishModel = new Gson().fromJson(getArguments().getString("ORDER_DISH_MODEL"), OrderedDishModel.class);

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
                if (!isEmpty(mModifyEditText)) {

                    String sModifyEditText = mModifyEditText.getText().toString();
                    bundle.putString("ITEM_MODIFY_DIALOG", sModifyEditText);
                    String test = bundle.getString("ITEM_MODIFY_DIALOG");

                    mOrderDishModel.updateNotesToKitchen(test);

                    mSubmitButton.setEnabled(true);

                    mSubmitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
//                            activity.onReturnValue(test);
                            // Toaster.toast(test);
                            getDialog().dismiss();

                        }
                    });

                } else
                    mSubmitButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }





}
