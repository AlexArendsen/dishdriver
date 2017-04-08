package edu.ucf.cop4331c.dishdriver;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

import java.util.ArrayList;

import static android.R.attr.colorPrimary;
import static android.R.attr.order;
import static xdroid.core.Global.getContext;
import static xdroid.core.Global.getResources;

/**
 * Created by tjcle on 3/14/2017.
 */

public class CookActivity extends AppCompatActivity {

    //holds the id of the Order being rejected
    String rejectID;

    //presumably I need an arraylist of orders?
    ArrayList<Order> orders = new ArrayList<Order>();

    boolean doubleBackToExitPressedOnce = false;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);
        ButterKnife.bind(this);
    }
}

class Order {
    ArrayList<TextView> dishViews = new ArrayList<TextView>();
    ArrayList<String> OrderList;
    int orderNum;
    String notes;

    //constructors
    public Order(int orderNum, ArrayList<String> Orders, String notes){
        this.orderNum = orderNum;
        OrderList = Orders;
        this.notes = notes;
    }
    public Order(int orderNum){
        this.orderNum = orderNum;
        OrderList = new ArrayList<String>();
    }

    //adds a dish to the OrderList
    public void addOrder(int orderNum, String dish, String notes){
        this.orderNum = orderNum;
        OrderList.add(dish);
        this.notes = notes;
    }

    public void createLinearLayout(){
        LinearLayout orderHolder = new LinearLayout(getContext());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)orderHolder.getLayoutParams();
        params.width = ActionBar.LayoutParams.WRAP_CONTENT;
        params.height = ActionBar.LayoutParams.WRAP_CONTENT;

        /*
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
         */
    }

    public void createTextViews(){
        for(int i = 0; i < OrderList.size(); i++){
            TextView dish = new TextView(getContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)dish.getLayoutParams();
            params.setMargins(0, 60, 0, 0);
            params.height = 50;
            params.width = 530;
            dish.setLayoutParams(params);
            dish.setBackgroundResource(R.color.colorPrimary);
            dish.setGravity(Gravity.CENTER);
            dish.setText(OrderList.get(i));
            dish.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            dish.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            dish.setTextColor(Color.WHITE);
            dish.setId(orderNum * 100 + i);
            /*
            These are the attributes that I'm replacing with java stuff
            android:layout_width="530dp"
            android:layout_height="50dp"
            android:layout_marginTop="60dp"
            android:background="@color/colorPrimary"
            android:gravity="center"
            android:text="@string/dishname"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="20sp"
             */
            dishViews.add(dish);
        }
    }
}
