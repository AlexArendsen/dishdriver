package edu.ucf.cop4331c.dishdriver.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.ucf.cop4331c.dishdriver.Order;
import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.models.OrderedDishModel;

/**
 * Created by tjcle on 4/8/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private WeakReference<AppCompatActivity> appCompatActivityWeakReference;
    private final ArrayList<Order> mOrders;

    public OrderAdapter(AppCompatActivity appCompatActivity, ArrayList<Order> orders) {
        mOrders = orders;
        appCompatActivityWeakReference = new WeakReference<AppCompatActivity>(appCompatActivity);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (appCompatActivityWeakReference.get() != null) {
            View view = LayoutInflater.from(appCompatActivityWeakReference.get())
                    .inflate(R.layout.card_order, parent, false);

            return new OrderViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        setupDishUI(holder.orderRelativeLayout, position);
    }

    public void setupDishUI(RelativeLayout relativeLayout, int positionInAdapter) {
        ArrayList<OrderedDishModel> dishes = mOrders.get(positionInAdapter).getmOrderedDishModels();
        int textViewCount = 0;

        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            View view = relativeLayout.getChildAt(i);

            if (view instanceof TextView) {
                if (textViewCount < dishes.size()) {
                    ((TextView) view).setVisibility(View.VISIBLE);
                    ((TextView) view).setText(dishes.get(textViewCount).getName());
                    textViewCount++;
                } else {
                    ((TextView) view).setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout orderRelativeLayout;

        public OrderViewHolder(View itemView) {
            super(itemView);

            orderRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.orderRelativeLayout);
        }


    }
}
