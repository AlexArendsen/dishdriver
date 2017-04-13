package edu.ucf.cop4331c.dishdriver;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.adapters.OrderAdapter;
import edu.ucf.cop4331c.dishdriver.custom.ProgressDialogActivity;
import edu.ucf.cop4331c.dishdriver.enums.Status;
import edu.ucf.cop4331c.dishdriver.models.OrderModel;
import edu.ucf.cop4331c.dishdriver.models.OrderedDishModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xdroid.toaster.Toaster;

import static xdroid.core.Global.getContext;

/**
 * Created by tjcle on 3/14/2017.
 */

public class CookActivity extends ProgressDialogActivity {

    @BindView(R.id.orderRecyclerView)
    RecyclerView mOrderRecyclerView;

    //holds the id of the Order being rejected
    String rejectID;



    private ArrayList<Order> mOrders = new ArrayList<Order>();

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toaster.toast("Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);
        ButterKnife.bind(this);
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getOrders();
    }

    public void getOrders() {
        enableProgressDialog("Retrieving Orders...");
        OrderModel.forRestaurant(SessionModel.currentRestaurant()).asObservable()
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::from)
                .filter(orderModel -> orderModel.getStatus().equals(Status.PLACED))
                .concatMap(this::getCombinedObservable)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Order>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onNext(List<Order> orders) {
                        mOrders.clear();
                        mOrders.addAll(orders);
                        mOrderRecyclerView.setAdapter(new OrderAdapter(getContext(), mOrders));
                        dismissProgressDialog();
                    }
                });
    }

    public Observable<Order> getCombinedObservable(OrderModel orderModel) {
        return orderModel.dishes().asObservable()
                .map(orderedDishModels -> {
                    ArrayList<OrderedDishModel> dishes = new ArrayList<OrderedDishModel>();
                    dishes.addAll(orderedDishModels);
                    return new Order(orderModel, dishes);
                });
    }
}

