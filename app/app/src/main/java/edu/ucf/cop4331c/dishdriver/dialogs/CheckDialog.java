package edu.ucf.cop4331c.dishdriver.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.custom.ItemAdapter;

/**
 * Created by viviennedo on 4/2/17.
 */

public class CheckDialog extends DialogFragment {

    //TODO: Eventually you will want to change this to an arraylist of the item models and not just strings.

    /**
     * Creates a new instance of the check dialog class. Sets the bundle (arguments) of the new instance to contain the list of inputted items.
     *
     * @param items     The list of items that have been added to the order by the waiter.
     * @return          Returns a new CheckDialog which contains all the items for this order.
     */
    public static CheckDialog newInstance(ArrayList<String> items) {
        CheckDialog checkDialog = new CheckDialog();

        // Create a new bundle object and add the list of strings under the ITEMS key.
        Bundle bundle = new Bundle();

        // Check if our list of items is not empty.
        if (!items.isEmpty()) {
            // If it is not empty then we just add it as an argument.
            bundle.putStringArrayList("ITEMS", items);
        } else {
            // If it is empty we add an empty list as a placeholder.
            ArrayList<String> empty = new ArrayList<>();
            bundle.putStringArrayList("ITEMS", empty);
        }

        // Set the check dialog's arguments to this bundle (So that we can later retrieve it..
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

        // Get our list of items if applicable.
        ArrayList<String> items = getArguments().getStringArrayList("ITEMS");

        // Bind our Recycler view and set its layout manager to be LinearLayout (Default orientation is vertical)
        RecyclerView receiptRecyclerView = (RecyclerView) view.findViewById(R.id.receiptOfItemsRecyclerView);
        receiptRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Bind our gratuityEditText
        EditText gratuityEditText = (EditText) view.findViewById(R.id.gratuityEditTextView);

        ItemAdapter itemAdapter = new ItemAdapter((AppCompatActivity) getActivity(), items, false);
        receiptRecyclerView.setAdapter(itemAdapter);
    }
}
