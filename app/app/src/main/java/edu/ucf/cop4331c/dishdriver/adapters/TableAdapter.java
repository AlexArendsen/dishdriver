package edu.ucf.cop4331c.dishdriver.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.NavigationActivity;
import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.custom.ProgressDialogActivity;
import edu.ucf.cop4331c.dishdriver.dialogs.ReservationDialog;
import edu.ucf.cop4331c.dishdriver.enums.TableStatus;
import edu.ucf.cop4331c.dishdriver.models.NonQueryResponseModel;
import edu.ucf.cop4331c.dishdriver.models.OrderModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import edu.ucf.cop4331c.dishdriver.models.TableReservationModel;
import rx.Observable;
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

        // final int[] tableStatus = {2};


        if (mAppCompatWeakReference.get() != null) {
            View view = LayoutInflater.from(mAppCompatWeakReference.get())
                    .inflate(R.layout.card_table, parent, false);

            TableViewHolder tableViewHolder = new TableViewHolder(view, new TableViewHolder.IMyViewHolderClicks() {
                // OKay, so here, we will check if the Status is correct and also if the Waiter if correct.


                // Oh okay, I need to somehow get the onClick on the view just for the table.
                @Override
                public void myOnClick(View caller, int position) {

                    Context context = mAppCompatWeakReference.get();
//                  Toaster.toast("Table: " + String.valueOf(position) + " clicked.");
//                   caller.setBackground(ContextCompat.getDrawable(context, R.color.colorPrimaryDark));
//                    context.startActivity(new Intent(context, NavigationActivity.class));
                    //EventBus.getDefault().post(new ShowPartyDialogEvent(position + 1));

                    // Toast.makeText(context, "Table: " + String.valueOf(position) + " clicked.", Toast.LENGTH_SHORT).show();
                    Toaster.toast("I am clicking this table?" + mTableModels.get(position).getStatus());

                    // mTableModels.get(position).getTableStatus();
                    // Toaster.toast("" + position + " of " + mTableModels.size() + " duh " + mTableModels.get(position).getTableStatus());

                    OrderModel currentOrderModel = new OrderModel();

                    // Okay, so once you have the status and the ID, you have to check if they are the same
                    // Start the ShowPartyDialog only if the status is marked 0, I suppose for now we get mark it and test?
                    // We are changing the state to OCCUPIED instead

                    ProgressDialogActivity activity = (ProgressDialogActivity) mAppCompatWeakReference.get();
                    activity.enableProgressDialog("Creating Order Model...");
                    currentOrderModel.create(SessionModel.currentPosition(), mTableModels.get(position)).asObservable()
                            .subscribeOn(Schedulers.io())
                            .flatMap(nonQueryResponseModel -> OrderModel.get(nonQueryResponseModel.getResults().getInsertId()))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<OrderModel>() {
                                @Override
                                public void onCompleted() {
                                    activity.dismissProgressDialog();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    activity.dismissProgressDialog();
                                }

                                @Override
                                public void onNext(OrderModel orderModel) {
                                    orderModel.setWaiterId(SessionModel.currentPosition().getID());
                                    orderModel.setTableId(mTableModels.get(position).getId());

                                    mTableModels.get(position).getStatus().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<TableStatus>() {


                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(TableStatus tableStatus) {
                                            switch (tableStatus) {
                                                case OCCUPIED:
                                                    if (! SessionModel.currentPosition().getID().equals(currentOrderModel.getWaiterId())) break;
                                                case UNRESERVED:
                                                    Intent intent = new Intent(context, NavigationActivity.class);
                                                    intent.putExtra("ORDER_MODEL", new Gson().toJson(orderModel));
                                                    activity.dismissProgressDialog();
                                                    context.startActivity(intent);
                                                    break;

                                            }

                                        }

                                    });

//                                        if (mTableModels.get(position).getStatus() == Observable<TableStatus.UNRESERVED>) {
//
//                                        mTableModels.get(position).setTableStatus(2);
//                                        Intent intent = new Intent(context, NavigationActivity.class);
//                                        intent.putExtra("ORDER_MODEL", new Gson().toJson(orderModel));
//                                        activity.dismissProgressDialog();
//                                        context.startActivity(intent);
//                                    }
//
//                                    // Check to see if the Table is the same as the Waiter associated with it,
//                                    // If it is Occupied, only the Waiter associated with the table can reopen it.
//
//                                    else if ((mTableModels.get(position).getTableStatus() == TableStatus.OCCUPIED) &&
//                                            SessionModel.currentPosition().getID().equals(currentOrderModel.getWaiterId())) {
//                                        Intent intent = new Intent(context, NavigationActivity.class);
//                                        intent.putExtra("ORDER_MODEL", new Gson().toJson(orderModel));
//                                        context.startActivity(intent);
//                                    } else {
//                                        Toaster.toast(mTableModels.get(position).getTableStatus() + " compared to " + TableStatus.OCCUPIED);
//                                        Toaster.toast(SessionModel.currentPosition().getID() + "compared to " + currentOrderModel.getWaiterId());
//                                    }
                                }
                            });
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

                //Toaster.toast("hello");
                // TODO: MAKE IT GO TO ReservationDialog
                // Toaster.toast("hello");
                AppCompatActivity appCompatActivity = mAppCompatWeakReference.get();

                if (appCompatActivity != null) {
                    mTableModels.get(position).getStatus().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<TableStatus>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(TableStatus tableStatus) {
                            switch (tableStatus) {
                                case UNRESERVED:
                                    ReservationDialog.newInstance("Reservation", position +1, mTableModels.get(position))
                                            .show(appCompatActivity.getSupportFragmentManager(), "RESERVATION_DIALOG");
                                    break;
                                case RESERVED:
                                    Toaster.toast("This table is reserved.");
                                default:
                            }
                        }
                    });
                }


//                if (appCompatActivity != null && mTableModels.get(position).getTableStatus() == TableStatus.UNRESERVED) {
//                    TableModel tableModel = mTableModels.get(position);
//                    ReservationDialog.newInstance("Reservation", position + 1, tableModel).show(appCompatActivity.getSupportFragmentManager(), "RESERVATION_DIALOG");
//                }

//                else if(mTableModels.get(position).getTableStatus() == TableStatus.RESERVED) {
//                    // TODO: figure out a way to UNRESERVE IT
//
//                }

                return true;

            }
        });

        mTableModels.get(position).getStatus().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<TableStatus>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TableStatus tableStatus) {
                switch (tableStatus) {
                    case OCCUPIED:
                        holder.mTableRelativeLayout.setBackground(ContextCompat.getDrawable(holder.mTableRelativeLayout.getContext(), R.color.colorPrimaryDark));
                        break;
                    case RESERVED:
                        holder.mTableRelativeLayout.setBackground(ContextCompat.getDrawable(holder.mTableRelativeLayout.getContext(), R.color.purple));
                        break;
                    default:
                        holder.mTableRelativeLayout.setBackground(ContextCompat.getDrawable(holder.mTableRelativeLayout.getContext(), R.color.complementPrimaryColor));
                        break;
                }


            }
        });

//        if (mTableModels.get(position).getTableStatus() == TableStatus.OCCUPIED) {
//
//            holder.mTableRelativeLayout.setBackground(ContextCompat.getDrawable(holder.mTableRelativeLayout.getContext(), R.color.colorPrimaryDark));
//        }
//
//
//        else if (mTableModels.get(position).getTableStatus() == TableStatus.RESERVED) {
//
//            holder.mTableRelativeLayout.setBackground(ContextCompat.getDrawable(holder.mTableRelativeLayout.getContext(), R.color.purple));
//        }
//
//        else {
//
//            holder.mTableRelativeLayout.setBackground(ContextCompat.getDrawable(holder.mTableRelativeLayout.getContext(), R.color.complementPrimaryColor));
//        }

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

        RelativeLayout mTableRelativeLayout;
        TextView mTableNameTextView;

        public IMyViewHolderClicks mListener;

        public TableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

        public TableViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);

            mTableRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.tableRelativeLayout);
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