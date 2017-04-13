package edu.ucf.cop4331c.dishdriver;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.adapters.TableAdapter;
import edu.ucf.cop4331c.dishdriver.dialogs.PartySizeDialog;
import edu.ucf.cop4331c.dishdriver.events.ShowPartyDialogEvent;
import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import rx.Subscriber;
import xdroid.toaster.Toaster;

/**
 * Created by viviennedo on 3/14/17.
 */

public class TableActivity extends AppCompatActivity {

    @BindView(R.id.tableRecyclerView)
    RecyclerView mTableRecyclerView;

    TableAdapter mTableAdapter;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toaster.toast("Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);


        mTableRecyclerView.setLayoutManager(gridLayoutManager);

        mTableAdapter = new TableAdapter(this);

        mTableRecyclerView.setAdapter(mTableAdapter);


        if (SessionModel.currentRestaurant() != null)
            DishModel.forRestaurant(SessionModel.currentRestaurant()).subscribe(new Subscriber<List<DishModel>>() {

                List<DishModel> dishModelList;

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<DishModel> dishModels) {
                    //Toaster.toast(SessionModel.currentRestaurant().getName());
                    //for (DishModel d  : dishModels)
                    //    Toaster.toast(d.getName());

                }
            });
        else
            ;

//        final Button button = (Button) findViewById(R.id.reserveButton);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//               showEditDialog();
//        }
//        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPartyDialogOpen(ShowPartyDialogEvent event) {
        PartySizeDialog.newInstance(event.getTableId()).show(getSupportFragmentManager(), "PARTY_DIALOG");
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkbox_deposit:
                if (checked)
                    Toaster.toast("hello, I want a table");
                //else
                break;


        }
    }


}
