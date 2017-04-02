package edu.ucf.cop4331c.dishdriver.custom;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.dialogs.ItemModifyDialog;
import xdroid.toaster.Toaster;

/**
 * Created by viviennedo on 3/25/17.
 */


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private WeakReference<AppCompatActivity> appCompatActivityWeakReference;
    private ArrayList<String> mItems;

    public ItemAdapter(AppCompatActivity activity, ArrayList<String> items) {
        mItems = items;
        appCompatActivityWeakReference = new WeakReference<AppCompatActivity>(activity);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout, parent, false);

        // Instantiating a new ItemViewHolder with an IMyViewHolderClick interface implementation.
        return new ItemViewHolder(contactView, new ItemViewHolder.IMyViewHolderClicks() {
            @Override
            public void removeItemFromAdapter(int position) {
                // Call this method with the passed position.
                removeItem(position);
            }

            @Override
            public void addNoteForItemAtPosition(int position) {
                AppCompatActivity appCompatActivity = appCompatActivityWeakReference.get();
                if (appCompatActivity != null)
                    new ItemModifyDialog().show(appCompatActivity.getSupportFragmentManager(), "ITEM_MODIFY_DIALOG");
            }
        });
    }


    // Get all your data here
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        // This will set the text on the holder from our RecyclerView, given the position
        // Basically, it shows the information
        // holder.mItemDescriptionTextView.setText(mItems.get(position));
        holder.mMenuItemTextView.setText(mItems.get(position));

        // [ x ] <--- info here.... access it.
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(String item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
    }

    public void getCheckTotalAmount() {
        // Iterate through item models and sum the price.
        double sum = 0.0;

        // This will not be a string, but an item model object that has a getter for the item price.
        for(String item: mItems) {
//            sum+= item.getPrice
        }
    }

    // create remove method somewhere

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mMenuItemTextView;
        ImageView mDeleteItemImageView;
        ImageView mModifyItemImageView;
        TextView mItemDescriptionTextView;

        // Create a listener object to be passed
        IMyViewHolderClicks mListener;

        // Constructor with passed view and our listener object that will be called later.
        public ItemViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);

            // Setting our listener field.
            mListener = listener;



            mMenuItemTextView = (TextView) itemView.findViewById(R.id.itemTextView);
            mDeleteItemImageView = (ImageView) itemView.findViewById(R.id.itemClearImageView);
            mModifyItemImageView = (ImageView) itemView.findViewById(R.id.itemModifyImageView);
            mItemDescriptionTextView = (TextView) itemView.findViewById(R.id.itemDescriptionTextView);

            mDeleteItemImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Testing this stuff", Toast.LENGTH_SHORT).show();
                    // We are calling our listener's remove item from adapter method in order to remove this item from the list.
                    mListener.removeItemFromAdapter(getAdapterPosition());
                }
            });

            mModifyItemImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Toaster.toast("This modify button is working");

                }
            });
        }

        // This interface will be used to handle item removals from the adapter.
        public interface IMyViewHolderClicks {
            void removeItemFromAdapter(int position);
            void addNoteForItemAtPosition(int position);
        }


        //        public ItemViewHolder(View itemView, IMyViewHolderClicks listener) {
//            super(itemView);
//            ButterKnife.bind(itemView);
//            mDeleteItemImageView = (ImageView) itemView.findViewById(R.id.itemClearImageView);
//            itemView.setOnClickListener(this);
//            mListener = listener;
//
//
//        }

//        public ItemViewHolder(View contactView) {
//            super(contactView);
//        }


//        @Override
//
//        public void onClick(View v) {
//
//                mListener.myOnClick(v, getLayoutPosition());
//
//        }


    }

}
