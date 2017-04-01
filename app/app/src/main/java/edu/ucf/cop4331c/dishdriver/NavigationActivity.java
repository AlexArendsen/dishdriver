package edu.ucf.cop4331c.dishdriver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.custom.ItemAdapter;
import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import xdroid.toaster.Toaster;

//class Dishes {
//    private String name;
//    private double price;
//
//    public Dishes(String name, double price) {
//        this.name = name;
//        this.price = price;
//    }
//}
public class NavigationActivity extends AppCompatActivity {


    private int mTableNumber;

    @BindView(R.id.menuSpinner)
    Spinner mMenuSpinner;
    @BindView(R.id.menuItemRecyclerView)
    RecyclerView mMenuItemRecyclerView;
    @BindView(R.id.beverageSpinner)
    Spinner mBeverageSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

//        final ArrayList<Dishes> menuDishesList = new ArrayList<Dishes>();
//        menuDishesList.add(new Dishes("Menu", 0));
//        menuDishesList.add(new Dishes("coconut curry wrap", 10.00));
//        menuDishesList.add(new Dishes("Acorn Squash", 5.00));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

        // Retrieves the table number from the intent that we passed in the PartySizeDialog.
        mTableNumber = getIntent().getIntExtra("PARTY_NUMBER", 0);

//        switch (mTableNumber) {
//            case 1:
//                break;
//            case 2:
//                break;
//            //...
//        }


        final ArrayList<String> menuItemsList = new ArrayList<>();
        menuItemsList.add("Menu");
        menuItemsList.add("COCONUT CURRY WRAP!");
        menuItemsList.add("Acorn squash");
        menuItemsList.add("MAYO FOR TJ");
        menuItemsList.add("Kimchi");
        menuItemsList.add("MUSHROOMS for Ashton");
        menuItemsList.add("Scones for Gareth!!");

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, menuItemsList);
        mMenuSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.pinkRed));
        mMenuSpinner.setAdapter(menuAdapter);

        final ItemAdapter itemAdapter = new ItemAdapter(new ArrayList<String>());

        mMenuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {

                    ((ItemAdapter) mMenuItemRecyclerView.getAdapter()).addItem(menuItemsList.get(position));
                    mMenuSpinner.setSelection(0);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMenuItemRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mMenuItemRecyclerView.setAdapter(itemAdapter);

//        // trying to create beverage spinner
//
        final ArrayList<String> beverageList = new ArrayList<String>();
        beverageList.add("Beverages");
        beverageList.add("Kombocha");
        beverageList.add("Chai Tea");


        ArrayAdapter<String> beverageAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, beverageList);
        mBeverageSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.pinkRed));
        mBeverageSpinner.setAdapter(beverageAdapter);

        mBeverageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != 0) {
                    ((ItemAdapter) mMenuItemRecyclerView.getAdapter()).addItem(beverageList.get(position));
                    mBeverageSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
