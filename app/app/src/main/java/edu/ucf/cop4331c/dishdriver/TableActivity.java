package edu.ucf.cop4331c.dishdriver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.adapters.TableAdapter;
import edu.ucf.cop4331c.dishdriver.custom.ProgressDialogActivity;
import edu.ucf.cop4331c.dishdriver.dialogs.PartySizeDialog;
import edu.ucf.cop4331c.dishdriver.events.ShowPartyDialogEvent;
import edu.ucf.cop4331c.dishdriver.models.OrderModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by viviennedo on 3/14/17.
 */

public class TableActivity extends ProgressDialogActivity {

    private static final String TAG = "TableActivity-";
    @BindView(R.id.tableRecyclerView)
    RecyclerView mTableRecyclerView;
    TableAdapter mTableAdapter;
    boolean doubleBackToExitPressedOnce = false;
    private ArrayList<TableModel> mTableModels;
    private ArrayList<OrderModel> mOrderModels;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        ButterKnife.bind(this);
        // Retrieve table models.
        getTables();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mTableAdapter = new TableAdapter(this);
        mTableRecyclerView.setLayoutManager(gridLayoutManager);
//        if (SessionModel.currentRestaurant() != null)
//            DishModel.forRestaurant(SessionModel.currentRestaurant()).subscribe(new Subscriber<List<DishModel>>() {
//
//                List<DishModel> dishModelList;
//
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onNext(List<DishModel> dishModels) {
//                    Toaster.toast(SessionModel.currentRestaurant().getName());
//                    for (DishModel d  : dishModels)
//                        Toaster.toast(d.getName());
//
//                }
//            });
//        else
//            Toaster.toast("Not right");

//        final Button button = (Button) findViewById(R.id.reserveButton);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//               showEditDialog();
//        }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // Here is where the Dialog for the PartySize is displayed


    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onPartyDialogOpen(ShowPartyDialogEvent event) {
        PartySizeDialog.newInstance(event.getTableId()).show(getSupportFragmentManager(), "PARTY_DIALOG");
    }

    // Wait, why is the checkbox being checked here? ):

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.reservationDepositCheckBox:
               if (checked)
                    Toast.makeText(this, "hello, I want a table", Toast.LENGTH_SHORT).show();
                //else
                break;


        }
    }

    /**
     * Gets the tables for this restaurant from our backend.
     */
    public void getTables() {

        // You have to pass mTableModels down to ReservationDialog
        // and then create a new reservation with the create method

        mTableModels = new ArrayList<>();
        enableProgressDialog("Retrieving Tables...");

        // Here, you will access the db and set the TableAdapter here

        TableModel.forRestaurant(SessionModel.currentRestaurant()).asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TableModel>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage(), e);
                        dismissProgressDialog();
                    }

                    @Override
                    // Associate the Tables with a TableModel and and set the TableModels here by passing them in
                    // the TableAdapter.

                    public void onNext(List<TableModel> tableModels) {
                        mTableModels.clear();
                        mTableModels.addAll(tableModels);
                        mTableAdapter.setTableModels(mTableModels);
                        mTableRecyclerView.setAdapter(mTableAdapter);
                        dismissProgressDialog();
                        getOrders();
                    }
                });
    }

    // We are going to grab all of the orders associated with the forRestaurant method
    // Recall that OrderModel.forRestaurant() will return all of the orders associated with the restaurants.
    // Mmm we need ALL of the Orders to compare the orders from the Waiter. That is why we are grabing all of the elements.


    public void getOrders() {

        mOrderModels = new ArrayList<>();
        enableProgressDialog("Retrieving All Orders...");

        OrderModel.forRestaurant(SessionModel.currentRestaurant()).asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OrderModel>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "onError: " + e.getMessage(), e);
                        dismissProgressDialog();

                    }

                    // Here, we are getting all of the OrderModel elements associated with the restaurant
                    // Make sure to clear and then set elements
                    @Override
                    public void onNext(List<OrderModel> orderModels) {

                        mOrderModels.clear();
                        mOrderModels.addAll(orderModels);
                        mTableAdapter.setOrderModels(mOrderModels);
                        dismissProgressDialog();
                    }
                });






    }




}
