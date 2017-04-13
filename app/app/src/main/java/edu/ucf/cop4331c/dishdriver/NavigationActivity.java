package edu.ucf.cop4331c.dishdriver;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.ucf.cop4331c.dishdriver.adapters.OrderAdapter;
import edu.ucf.cop4331c.dishdriver.custom.ItemAdapter;
import edu.ucf.cop4331c.dishdriver.custom.ProgressDialogActivity;
import edu.ucf.cop4331c.dishdriver.dialogs.CheckDialog;
import edu.ucf.cop4331c.dishdriver.dialogs.ItemModifyDialog;
import edu.ucf.cop4331c.dishdriver.enums.Status;
import edu.ucf.cop4331c.dishdriver.enums.TableStatus;
import edu.ucf.cop4331c.dishdriver.helpers.MoneyFormatter;
import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.NonQueryResponseModel;
import edu.ucf.cop4331c.dishdriver.models.OrderModel;
import edu.ucf.cop4331c.dishdriver.models.OrderedDishModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import pl.droidsonroids.gif.GifTextView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xdroid.toaster.Toaster;

import static xdroid.core.Global.getContext;

public class NavigationActivity extends ProgressDialogActivity {

//    public String onReturnValue(String foo) {
//
//        Log.i("onReturnValue", "Got value " + foo + " back from Dialog!");
//
//        return foo;
//
//
//    }


    private int mTableNumber;
    private OrderModel mOrderModel;
    private TableModel mTableModel;
    private OrderedDishModel mOrderDishModel;
    // private ArrayList<Order> mOrders = new ArrayList<Order>();

    @BindView(R.id.menuSpinner)
    Spinner mMenuSpinner;
    @BindView(R.id.menuItemRecyclerView)
    RecyclerView mMenuItemRecyclerView;
    @BindView(R.id.convertOrderDishModelButton)
    ImageButton mOrderDishButton;
    @BindView(R.id.checkButton)
    ImageButton mCheckButton;
    @BindView(R.id.sendOrderToKitchenButton)
    ImageButton mOrderButton;
    private ItemAdapter itemAdapter;
//    @BindView(R.id.hypeTextView)
//    GifTextView mHypeTextView;


    // Oh my gosh, major debugging... do not bind views if you aren;t going to use them!!!

//    @BindView(R.id.itemPriceTextView)
//    TextView mItemPriceTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

        // Retrieves the table number from the intent that we passed in the PartySizeDialog.
        mTableNumber = getIntent().getIntExtra("PARTY_NUMBER", 0);
        mOrderModel = new Gson().fromJson(getIntent().getStringExtra("ORDER_MODEL"), OrderModel.class);
        mTableModel = new Gson().fromJson(getIntent().getStringExtra("TABLE_MODEL"), TableModel.class);
        mOrderDishModel = new Gson().fromJson(getIntent().getStringExtra("ORDER_DISH_MODEL"), OrderedDishModel.class);

        final ArrayList<DishModel> menuItemsList = new ArrayList<>();

        ArrayAdapter<DishModel> menuAdapter = new ArrayAdapter<DishModel>(getBaseContext(), android.R.layout.simple_list_item_1, menuItemsList);
        mMenuSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.complementPrimaryColor));
        mMenuSpinner.setAdapter(menuAdapter);

        itemAdapter = new ItemAdapter(this, new ArrayList<DishModel>(), true, mOrderModel, mOrderDishModel);

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

                        final ArrayList<String> temps = new ArrayList<String>();
                        final ArrayList<String> prices = new ArrayList<String>();

                        // I know, it's gross!! Ah!!
                        for(int x = 0; x < menuItemsList.size(); x++ ) {

                            temps.add(menuItemsList.get(x).getName());
                            prices.add(MoneyFormatter.format(menuItemsList.get(x).getPrice()));
                        }

                        // Bind the spinner to the names to display
                        ArrayAdapter<String> menuItemAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, temps);
                        mMenuSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.complementPrimaryColor));
                        mMenuSpinner.setAdapter(menuItemAdapter);
                        dismissProgressDialog();


                    }
                });

    }


    @OnClick(R.id.convertOrderDishModelButton)
    public void convertToOrderedDishModel() {

        final ArrayList<OrderedDishModel> OrderedDishModelList = new ArrayList<>();

        Toaster.toast("YOU ARE IN EDIT MODE");

        mOrderModel.place(itemAdapter.getOrder()).asObservable()
                .subscribeOn(Schedulers.io())
                .flatMap(nonQueryResponseModel -> mOrderModel.dishes())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OrderedDishModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<OrderedDishModel> orderedDishModels) {

                        for(OrderedDishModel orderedDishModel : orderedDishModels) {

                            OrderedDishModelList.add(orderedDishModel);
                        }


                    }

                });

    }

    // TODO: Add check method back in later

    @OnClick(R.id.checkButton)
    public void onCheckButtonClicked() {

        // Setting the status back to UNRESERVED once the check button is submitted.
        Toaster.toast("I am clicking check butt");
        // mTableModel.setTableStatus(1);
        CheckDialog.newInstance(((ItemAdapter) mMenuItemRecyclerView.getAdapter()).getItems()).show(getSupportFragmentManager(), "RECEIPT_DIALOG");


    }

    // go back to SignInActivity

    @OnClick(R.id.sendOrderToKitchenButton)
    public void onOrderButtonClicked() {

        if((itemAdapter.getItems().size() == 0)) {

            new AlertDialog.Builder(NavigationActivity.this)
                    .setTitle("Error: Empty Order")
                    .setMessage("Seems like you have no items to place....")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return;





        }

        else {


            mOrderModel.place(itemAdapter.getOrder()).asObservable()
                    .subscribeOn(Schedulers.io())
                    .flatMap(nonQueryResponseModel -> mOrderModel.dishes())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<OrderedDishModel>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<OrderedDishModel> orderedDishModels) {


                        }

                    });


        Intent intent = new Intent(NavigationActivity.this, TableActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("TABLE_MODEL", new Gson().toJson(mTableModel));
        startActivity(intent);
        finish();

        }
//                .subscribe(new Subscriber<NonQueryResponseModel>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(NonQueryResponseModel nonQueryResponseModel) {
//                        Intent intent = new Intent(NavigationActivity.this, TableActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        // IF ALL ELSE FAILS, USE THIS FLAG TO SET COLORS
//                        //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//                    }
//                });


    }



//    public void getOrders() {
//        enableProgressDialog("Retrieving Orders...");
//        OrderModel.forRestaurant(SessionModel.currentRestaurant()).asObservable()
//                .subscribeOn(Schedulers.io())
//                .flatMap(Observable::from)
//                .filter(orderModel -> orderModel.getStatus().equals(Status.PLACED))
//                .concatMap(this::getCombinedObservable)
//                .toList()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Order>>() {
//                    @Override
//                    public void onCompleted() {
//                        dismissProgressDialog();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        dismissProgressDialog();
//                    }
//
//                    @Override
//                    public void onNext(List<Order> orders) {
//                        mOrders.clear();
//                        mOrders.addAll(orders);
//                        mMenuItemRecyclerView.setAdapter(new OrderAdapter(getContext(), mOrders));
//                        dismissProgressDialog();
//                    }
//                });
//    }
//
//    public Observable<Order> getCombinedObservable(OrderModel orderModel) {
//        return orderModel.dishes().asObservable()
//                .map(orderedDishModels -> {
//                    ArrayList<OrderedDishModel> dishes = new ArrayList<OrderedDishModel>();
//                    dishes.addAll(orderedDishModels);
//                    return new Order(orderModel, dishes);
//                });
//    }




}
