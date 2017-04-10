package edu.ucf.cop4331c.dishdriver.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.CookActivity;
import edu.ucf.cop4331c.dishdriver.NavigationActivity;
import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.SignInActivity;
import edu.ucf.cop4331c.dishdriver.dialogs.ReservationDialog;
import edu.ucf.cop4331c.dishdriver.enums.TableStatus;
import edu.ucf.cop4331c.dishdriver.events.ShowPartyDialogEvent;
import edu.ucf.cop4331c.dishdriver.models.NonQueryResponseModel;
import edu.ucf.cop4331c.dishdriver.models.OrderModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xdroid.toaster.Toaster;

/**
 * Created by viviennedo on 3/14/17.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    private WeakReference<AppCompatActivity> mAppCompatWeakReference;
    private ArrayList<TableModel> mTableModels = new ArrayList<>();
    private ArrayList<OrderModel> mOrders = new ArrayList<>();
    public static boolean testingBool = false;
    // Set the table status to UNRESERVED


    public TableAdapter(AppCompatActivity activity) {
        mAppCompatWeakReference = new WeakReference<AppCompatActivity>(activity);
}

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final int[] tableStatus = {2};



        if (mAppCompatWeakReference.get() != null) {
            View view = LayoutInflater.from(mAppCompatWeakReference.get())
                    .inflate(R.layout.card_table, parent, false);

            TableViewHolder tableViewHolder = new TableViewHolder(view, new TableViewHolder.IMyViewHolderClicks() {
                @Override
                // OKay, so here, we will check if the Status is correct and also if the Waiter if correct.


                // Oh okay, I need to somehow get the onClick on the view just for the table.

                public void myOnClick(View caller, int position) {


                    // We have to set the number here for the status...


                    Context context = mAppCompatWeakReference.get();
                    // Toast.makeText(context, "Table: " + String.valueOf(position) + " clicked.", Toast.LENGTH_SHORT).show();
                    caller.setBackground(ContextCompat.getDrawable(context, R.color.colorPrimaryDark));

                    mTableModels.get(position).setTableStatus(tableStatus[0]);
                    mTableModels.get(position).getTableStatus();
                    SessionModel.currentPosition().getEmployeeID();
                    Toaster.toast("" + position + " of " + mTableModels.size() + " duh " + mTableModels.get(position).getTableStatus());

                    // Okay, so once you have the status and the ID, you have to check if they are the same
                    // Start the ShowPartyDialog only if the status is marked 0, I suppose for now we get mark it and test?

                    // We are changing the state to OCCUPIED instead
                    if(mTableModels.get(position).getTableStatus() == TableStatus.UNRESERVED) {

                        tableStatus[0] = 3;
                        OrderModel orderModel = new OrderModel();
                        orderModel.create(SessionModel.currentPosition(), mTableModels.get(position)).asObservable()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<NonQueryResponseModel>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(NonQueryResponseModel nonQueryResponseModel) {

                                    }
                                });

                        EventBus.getDefault().post(new ShowPartyDialogEvent(position + 1));


                    }

                    //else if(SessionModel.currentPosition().getEmployeeID() == )

                    else {

//                        if(SessionModel.currentPosition().getEmployeeID()) {
//
//                        }

                        Intent ieventreport = new Intent(context,NavigationActivity.class);
                        context.startActivity(ieventreport);

                    }









                }
            });


            return tableViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {

        holder.mTableNameTextView.setText("Table " + String.valueOf(position + 1));
        holder.mTableNameTextView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Toaster.toast("hello");
                // TODO: MAKE IT GO TO ReservationDialog
                AppCompatActivity appCompatActivity = mAppCompatWeakReference.get();

                if (appCompatActivity != null) {
                    ReservationDialog.newInstance("Reservation", position + 1).show(appCompatActivity.getSupportFragmentManager(), "RESERVATION_DIALOG");
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public void setTableModels(ArrayList<TableModel> tableModels) {
        mTableModels = tableModels;
    }

    public void setOrderModels(ArrayList<OrderModel> orderModels) {
        mOrders = orderModels;
    }

    // AH HA, I found where you can click on the card and go to the NavigationAct


    static class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTableNameTextView;

        public IMyViewHolderClicks mListener;

        public TableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

        public TableViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            mTableNameTextView = (TextView) itemView.findViewById(R.id.tableNameTextView);
            itemView.setOnClickListener(this);
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.myOnClick(v, getLayoutPosition());
        }


        public interface IMyViewHolderClicks {
            void myOnClick(View caller, int position);
        }




    }
}
