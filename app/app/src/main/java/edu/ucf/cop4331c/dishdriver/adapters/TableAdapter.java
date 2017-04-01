package edu.ucf.cop4331c.dishdriver.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.dialogs.PartySizeDialog;
import edu.ucf.cop4331c.dishdriver.events.ShowPartyDialogEvent;

/**
 * Created by viviennedo on 3/14/17.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {
    private WeakReference<Context> mContextWeakReference;

    public TableAdapter(Context context) {
        mContextWeakReference = new WeakReference<Context>(context);
}

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContextWeakReference.get() != null) {
            View view = LayoutInflater.from(mContextWeakReference.get())
                    .inflate(R.layout.card_table, parent, false);

            TableViewHolder tableViewHolder = new TableViewHolder(view, new TableViewHolder.IMyViewHolderClicks() {
                @Override
                public void myOnClick(View caller, int position) {
                    Context context = mContextWeakReference.get();
                    Toast.makeText(context, "Table: " + String.valueOf(position) + " clicked.", Toast.LENGTH_SHORT).show();
                    caller.setBackground(ContextCompat.getDrawable(context, R.color.colorPrimaryDark));
                    EventBus.getDefault().post(new ShowPartyDialogEvent(position + 1));
                }
            });

            return tableViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        holder.mTableNameTextView.setText("Table " + String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return 6;
    }

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
