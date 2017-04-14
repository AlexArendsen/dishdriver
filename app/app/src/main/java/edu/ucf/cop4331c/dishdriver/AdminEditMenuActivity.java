package edu.ucf.cop4331c.dishdriver;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.NonQueryResponseModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import rx.Observable;
import xdroid.toaster.Toaster;

public class AdminEditMenuActivity extends AppCompatActivity {

    @BindView(R.id.nameInput)
    EditText mNameInput;
    @BindView(R.id.priceInput)
    EditText mPriceInput;
    @BindView(R.id.descriptionInput)
    EditText mDescriptionInput;
    @BindView(R.id.addDrink)
    Button mAddDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_restaurant_activity);

        ButterKnife.bind(this);
    }

    public void dialogBoxForEachItem() {
        new AlertDialog.Builder(getBaseContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @OnClick(R.id.addDrink)
    public void onAddMenuItemButtonClicked() {

        //validate input has value
        if ((mPriceInput.getText().length() >= 1) && (mNameInput.getText().length() >= 1) && (mDescriptionInput.getText().length() >= 1)) {

            DishModel d = new DishModel();
            d.setName(mNameInput.getText().toString());
            d.setDescription(mDescriptionInput.getText().toString());
            d.setPrice(Integer.parseInt(mPriceInput.getText().toString()));
            d.setRestaurantID(SessionModel.currentRestaurant().getId());

            Observable<NonQueryResponseModel> oCreate = d.create();
            oCreate.subscribe();

            Toaster.toast("Added");
            mNameInput.setText("");
            mPriceInput.setText("");
            mDescriptionInput.setText("");

        } else {
            //failed message
            new android.app.AlertDialog.Builder(AdminEditMenuActivity.this)
                    .setTitle("Failed")
                    .setMessage("Please fill in every box.")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        //finish();
    }
}
