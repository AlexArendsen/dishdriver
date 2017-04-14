package edu.ucf.cop4331c.dishdriver.adapters;

import android.content.Context;
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

    private final ArrayList<Order> mOrders;
    private WeakReference<Context> contextWeakReference;

    public OrderAdapter(Context context, ArrayList<Order> orders) {
        mOrders = orders;
        contextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (contextWeakReference.get() != null) {
            View view = LayoutInflater.from(contextWeakReference.get())
                    .inflate(R.layout.card_order, parent, false);

            return new OrderViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        setupDishUI(holder.orderRelativeLayout, position);
        holder.orderNumberTextView.setText("Order Number: " + mOrders.get(position).getmOrderModel().getId());
    }

    public void setupDishUI(RelativeLayout relativeLayout, int positionInAdapter) {
        ArrayList<OrderedDishModel> dishes = mOrders.get(positionInAdapter).getmOrderedDishModels();
        int textViewCount = 0;

        for (int i = 2; i < relativeLayout.getChildCount(); i++) {
            View view = relativeLayout.getChildAt(i);

            if (view instanceof TextView) {
                if (textViewCount < dishes.size()) {
                    view.setVisibility(View.VISIBLE);
                    String notes = dishes.get(textViewCount).getNotesToKitchen();
                    String fromK  = dishes.get(textViewCount).getNotesFromKitchen();
                    ((TextView) view).setText(dishes.get(textViewCount).getName() + " \n" + ((notes==null)? ((fromK==null)?"": fromK) : notes));
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
        TextView orderNumberTextView;

        public OrderViewHolder(View itemView) {
            super(itemView);

            orderNumberTextView = (TextView) itemView.findViewById(R.id.orderNumberTextView);
            orderRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.orderRelativeLayout);
        }


    }
}
