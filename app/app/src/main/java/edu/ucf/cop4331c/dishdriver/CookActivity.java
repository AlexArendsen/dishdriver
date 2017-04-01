package edu.ucf.cop4331c.dishdriver;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;

import java.util.ArrayList;

/**
 * Created by tjcle on 3/14/2017.
 */

public class CookActivity extends AppCompatActivity {

    //holds the id of the Order being rejected
    String rejectID;

    //presumably I need an arraylist of orders?
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
    ArrayList<String> OrderList;
    String notes;
    public Order(ArrayList<String> Orders, String notes){
        OrderList = Orders;
        this.notes = notes;
    }
    public Order(){
        OrderList = new ArrayList<String>();
    }
    public void addOrder(String Order, String notes){
        OrderList.add(Order);
        this.notes = notes;
    }
}
