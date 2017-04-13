
package edu.ucf.cop4331c.dishdriver.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.TableActivity;
import edu.ucf.cop4331c.dishdriver.custom.ItemAdapter;
import edu.ucf.cop4331c.dishdriver.helpers.MoneyFormatter;
import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import xdroid.toaster.Toaster;

/**
 * Created by viviennedo on 4/2/17.
 */

public class CheckDialog extends DialogFragment {

    // Lol, I know this is really ugly, but it works...don't judge!! I will reformat the method into ItemAdapter later.

    private static String total;
    private static String gratuity;
    private static String totalWithTip;

    //TODO: Eventually you will want to change this to an arraylist of the item models and not just strings.

    /**
     * Creates a new instance of the check dialog class. Sets the bundle (arguments) of the new instance to contain the list of inputted items.
     *
     * @param items     The list of items that have been added to the order by the waiter.
     * @return          Returns a new CheckDialog which contains all the items for this order.
     */



    public static CheckDialog newInstance(ArrayList<DishModel> items) {


        CheckDialog checkDialog = new CheckDialog();

        ArrayList<DishModel> convertToStr = new ArrayList<>();

        total = getCheckTotalAmount(items);
        gratuity = getTipAmount(items);
        totalWithTip = getSubtotalWithTip(items);


        convertToStr.addAll(items);


        Bundle bundle = new Bundle();

        // Check if our list of items is not empty.
        if (!items.isEmpty()) {
            // If it is not empty then we just add it as an argument.
            // Put STRING as Gson is going to convert your arraylist into json (Which is basically a String).
            bundle.putString("STRINGS", new Gson().toJson(convertToStr));

        } else {
            // If it is empty we add an empty list as a placeholder.
            ArrayList<String> empty = new ArrayList<>();
            bundle.putStringArrayList("STRINGS", empty);
        }

        checkDialog.setArguments(bundle);

        // Return the check dialog.
        return checkDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.receipt_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String json = getArguments().getString("STRINGS", "INVALID");

        ArrayList<DishModel> items;

        if (!json.equals("INVALID")) {
            items = new Gson().fromJson(json, new TypeToken<ArrayList<DishModel>>() {
            }.getType());
        } else {
            items = new ArrayList<>();
        }



        // Bind our Recycler view and set its layout manager to be LinearLayout (Default orientation is vertical)
        RecyclerView receiptRecyclerView = (RecyclerView) view.findViewById(R.id.receiptOfItemsRecyclerView);
        receiptRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Bind our gratuityEditText
//        EditText gratuityEditText = (EditText) view.findViewById(R.id.gratuityEditTextView);

        ItemAdapter itemAdapter = new ItemAdapter((AppCompatActivity) getActivity(), items, false, null,null);
        receiptRecyclerView.setAdapter(itemAdapter);

        Button mTipButton = (Button) view.findViewById(R.id.tipButton);
        Button mSubmitButton = (Button) view.findViewById(R.id.submitButton);
        TextView mItemTotalTextView = (TextView) view.findViewById(R.id.itemTotalTextView);
        TextView mGratuityTextView = (TextView) view.findViewById(R.id.gratuityTextView);
        TextView mSubtotalTextView = (TextView) view.findViewById(R.id.itemSubtotalTextView);

        mSubtotalTextView.setText(total);


        mGratuityTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mSubtotalTextView.setText(totalWithTip);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mItemTotalTextView.setText(total);


        // TODO: bind the TextView to
        mTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toaster.toast("hello this button works");
                mGratuityTextView.setText((String) gratuity);


            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent orderIntent = new Intent(getActivity(), TableActivity.class);
                orderIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(orderIntent);
                dismiss();
            }
        });

    }

    private boolean isEmpty(EditText etText) {

        return etText.getText().toString().trim().length() == 0;
    }

    public static String getCheckTotalAmount(ArrayList<DishModel> items) {

        // Iterate through item models and sum the price.
        int sum = 0;

        for (int i = 0; i < items.size(); i++ ) {

            sum += items.get(i).getPrice();
        }


        return MoneyFormatter.format(sum);
    }

    public static String getTipAmount(ArrayList<DishModel> items) {

        // Iterate through item models and sum the price.
        int sum = 0;

        for (int i = 0; i < items.size(); i++ ) {

            sum += items.get(i).getPrice();
        }


        return (String.valueOf(new DecimalFormat("0.00").format((sum * 0.0018))));

    }

    public static String getSubtotalWithTip(ArrayList<DishModel> items) {

        // Iterate through item models and sum the price.
        double sum = 0;

        for (int i = 0; i < items.size(); i++ ) {

            sum += items.get(i).getPrice();
        }


        return String.valueOf(new DecimalFormat("0.00").format((sum * 0.0018) + (sum/100)));
    }







}
