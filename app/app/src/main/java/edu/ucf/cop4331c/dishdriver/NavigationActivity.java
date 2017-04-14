package edu.ucf.cop4331c.dishdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.ucf.cop4331c.dishdriver.custom.ItemAdapter;
import edu.ucf.cop4331c.dishdriver.custom.ProgressDialogActivity;
import edu.ucf.cop4331c.dishdriver.dialogs.CheckDialog;
import edu.ucf.cop4331c.dishdriver.helpers.MoneyFormatter;
import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import pl.droidsonroids.gif.GifTextView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//class Dishes {
//    private String name;
//    private double price;
//
//    public Dishes(String name, double price) {
//        this.name = name;
//        this.price = price;
//    }
//}
public class NavigationActivity extends ProgressDialogActivity {


    private int mTableNumber;

    @BindView(R.id.menuSpinner)
    Spinner mMenuSpinner;
    @BindView(R.id.menuItemRecyclerView)
    RecyclerView mMenuItemRecyclerView;
    @BindView(R.id.beverageSpinner)
    Spinner mBeverageSpinner;
    @BindView(R.id.checkButton)
    ImageButton mCheckButton;
    @BindView(R.id.sendOrderToKitchenButton)
    ImageButton mOrderButton;
//    @BindView(R.id.hypeTextView)
//    GifTextView mHypeTextView;


    // Oh my gosh, major debugging... do not bind views if you aren;t going to use them!!!

//    @BindView(R.id.itemPriceTextView)
//    TextView mItemPriceTextView;
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



        final ArrayList<DishModel> menuItemsList = new ArrayList<>();

//        menuItemsList.add("Menu");
//        menuItemsList.add("COCONUT CURRY WRAP!");
//        menuItemsList.add("Acorn squash");
//        menuItemsList.add("MAYO FOR TJ");
//        menuItemsList.add("Kimchi");
//        menuItemsList.add("MUSHROOMS for Ashton");
//        menuItemsList.add("Scones for Gareth!!");

        ArrayAdapter<DishModel> menuAdapter = new ArrayAdapter<DishModel>(getBaseContext(), android.R.layout.simple_list_item_1, menuItemsList);
        mMenuSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.complementPrimaryColor));
        mMenuSpinner.setAdapter(menuAdapter);

        final ItemAdapter itemAdapter = new ItemAdapter(this, new ArrayList<DishModel>(), true);

        mMenuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {


                    ((ItemAdapter) mMenuItemRecyclerView.getAdapter()).addItem(menuItemsList.get(position));
                    mMenuItemRecyclerView.smoothScrollToPosition(mMenuItemRecyclerView.getAdapter().getItemCount());
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
//        final ArrayList<DishModel> beverageList = new ArrayList<String>();
//        beverageList.add("Beverages");
//        beverageList.add("Kombocha");
//        beverageList.add("Chai Tea");
//
//
//        ArrayAdapter<String> beverageAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, beverageList);
//        mBeverageSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.pinkRed));
//        mBeverageSpinner.setAdapter(beverageAdapter);
//
//        mBeverageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (position != 0) {
//                    ((ItemAdapter) mMenuItemRecyclerView.getAdapter()).addItem(beverageList.get(position));
//                    mMenuItemRecyclerView.smoothScrollToPosition(mMenuItemRecyclerView.getAdapter().getItemCount());
//                    mBeverageSpinner.setSelection(0);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

         getDishes(menuItemsList);


    }

    private void getDishes(final ArrayList<DishModel> menuItemsList) {

        // Enable the progress bar whenever you need to grab data from the database
        enableProgressDialog("Test");
        //TODO: Use this as soon as session model is fixed
//        DishModel.forRestaurant(SessionModel.currentRestaurant()).asObservable()
        DishModel.forRestaurant(SessionModel.currentRestaurant())
        // RestaurantModel.get(3).asObservable()
                // flatMap is going to grab the first element is your list of Observable objects.
                // in our case, we have a list of restaurantModels
               // .flatMap(restaurantModels -> DishModel.forRestaurant(restaurantModels.get(0)))
                // subscribeOn means you will look at the backend (where the elements are located at)
                .subscribeOn(Schedulers.io())
                // observeOn means to display elements on the UI mainthread (which is where we are interested in viewing it
                .observeOn(AndroidSchedulers.mainThread())
                // subscribe acts like pacman and eats up whatever you found in subscribeOn and blows it up on UI
                .subscribe(new Subscriber<List<DishModel>>() {
                    // Then, we perform these methods: onNext, onCompleted, onError
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(NavigationActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    // Take a list of DishModels and loop through to get the names
                    public void onNext(List<DishModel> dishModels) {
                        menuItemsList.clear();
//                        beverageList.clear();

                        for (DishModel dishModel : dishModels) {

                            menuItemsList.add(dishModel);



                        }



//                        for (DishModel dishModel : dishModels) {
//                            beverageList.add(MoneyFormatter.format(dishModel.getPrice()));
//
//                        }

                        final ArrayList<String> temps = new ArrayList<String>();
                        final ArrayList<String> prices = new ArrayList<String>();

                        for(int x = 0; x < menuItemsList.size(); x++ ) {

                            temps.add(menuItemsList.get(x).getName());
                            prices.add(MoneyFormatter.format(menuItemsList.get(x).getPrice()));
                        }




                        // Bind the spinner to the names to display
                        ArrayAdapter<String> menuItemAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, temps);
                        mMenuSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.complementPrimaryColor));
                        mMenuSpinner.setAdapter(menuItemAdapter);


//                        ArrayAdapter<String> beverageAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, beverageList);
//                        mBeverageSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.pinkRed));
//                        mBeverageSpinner.setAdapter(beverageAdapter);




                        dismissProgressDialog();
                    }
                });
    }

    // TODO: Add check method back in later

    @OnClick(R.id.checkButton)
    public void onCheckButtonClicked() {
        CheckDialog.newInstance(((ItemAdapter) mMenuItemRecyclerView.getAdapter()).getItems()).show(getSupportFragmentManager(), "RECEIPT_DIALOG");
    }

    // go back to SignInActivity

    @OnClick(R.id.sendOrderToKitchenButton)
    public void onOrderButtonClicked() {
        //startActivity(new Intent(NavigationActivity.this, SignInActivity.class));
    }


}
