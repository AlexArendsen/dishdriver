package edu.ucf.cop4331c.dishdriver.custom;

import android.content.Context;
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

import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.dialogs.ItemModifyDialog;
import edu.ucf.cop4331c.dishdriver.helpers.MoneyFormatter;
import edu.ucf.cop4331c.dishdriver.models.DishModel;
import xdroid.toaster.Toaster;

/**
 * Created by viviennedo on 3/25/17.
 */


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {


    // WeakReference will ensure that the activity context is not leaked if the activity is killed by the app.
    private WeakReference<AppCompatActivity> appCompatActivityWeakReference;
    private ArrayList<DishModel> mItems;
    private boolean mShowRemoveButton;

    public ItemAdapter(AppCompatActivity activity, ArrayList<DishModel> items, boolean showRemoveButton) {
        mItems = items;
        appCompatActivityWeakReference = new WeakReference<AppCompatActivity>(activity);
        mShowRemoveButton = showRemoveButton;
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

            // this is checking weak reference
            public void addNoteForItemAtPosition(int position) {
                AppCompatActivity appCompatActivity = appCompatActivityWeakReference.get();
                if (appCompatActivity != null)
                    // If there is something there, then we want to create a dialog
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
        holder.mMenuItemTextView.setText((CharSequence) mItems.get(position).getName());
        holder.mItemPriceTextView.setText((CharSequence) MoneyFormatter.format(mItems.get(position).getPrice()));
        holder.mItemDescriptionTextView.setText((CharSequence) mItems.get(position).getDescription());


        // Here we check whether or not we should show the remove button.
        if (mShowRemoveButton) {
            holder.mDeleteItemImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mDeleteItemImageView.setVisibility(View.GONE);
        }

        // [ x ] <--- info here.... access it.

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(DishModel item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
    }
    public ArrayList<DishModel> getItems() {
        return mItems;
    }

//    // TODO: Sum up the items here instead of in CheckDialog. You can then call the method and return the sum
//    public double getCheckTotalAmount() {
//        // Iterate through item models and sum the price.
//        double sum = 0.0;
//
//        // This will not be a string, but an item model object that has a getter for the item price.
//        for(String item: mItems) {
////            sum+= item.getPrice
//        }
//
//        return 0.0;
//    }
//

//
//    public double getTipAmount() {
//
//        //generate tip amount.
//        return ( getCheckTotalAmount()*0.18);
//
//    }
//
//    public double getSubtotal() {
//
//        if(true) // if gratuityEditTextView is populated
//            return ( getCheckTotalAmount() + getTipAmount() );
//
//        else return getCheckTotalAmount();
//    }

    // create remove method somewhere

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mMenuItemTextView;
        ImageView mDeleteItemImageView;
        ImageView mModifyItemImageView;
        TextView mItemDescriptionTextView;
        TextView mItemPriceTextView;

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
            mItemPriceTextView = (TextView) itemView.findViewById(R.id.itemPriceTextView);


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
                    mListener.addNoteForItemAtPosition(getAdapterPosition());
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
