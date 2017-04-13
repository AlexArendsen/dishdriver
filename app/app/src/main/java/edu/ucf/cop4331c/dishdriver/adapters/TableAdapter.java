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

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.NavigationActivity;
import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.dialogs.ReservationDialog;
import xdroid.toaster.Toaster;

/**
 * Created by viviennedo on 3/14/17.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private WeakReference<AppCompatActivity> mAppCompatWeakReference;

    public TableAdapter(AppCompatActivity activity) {
        mAppCompatWeakReference = new WeakReference<AppCompatActivity>(activity);
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mAppCompatWeakReference.get() != null) {
            View view = LayoutInflater.from(mAppCompatWeakReference.get())
                    .inflate(R.layout.card_table, parent, false);

            TableViewHolder tableViewHolder = new TableViewHolder(view, new TableViewHolder.IMyViewHolderClicks() {
                @Override
                public void myOnClick(View caller, int position) {
                    Context context = mAppCompatWeakReference.get();
                    Toaster.toast("Table: " + String.valueOf(position) + " clicked.");
                    caller.setBackground(ContextCompat.getDrawable(context, R.color.colorPrimaryDark));
                    context.startActivity(new Intent(context, NavigationActivity.class));
                    //EventBus.getDefault().post(new ShowPartyDialogEvent(position + 1));
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

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    static class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public IMyViewHolderClicks mListener;
        TextView mTableNameTextView;

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
