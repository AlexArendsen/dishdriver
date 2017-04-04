package edu.ucf.cop4331c.dishdriver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.adapters.TableAdapter;
import edu.ucf.cop4331c.dishdriver.dialogs.ReservationDialog;
import edu.ucf.cop4331c.dishdriver.dialogs.PartySizeDialog;
import edu.ucf.cop4331c.dishdriver.events.ShowPartyDialogEvent;

/**
 * Created by viviennedo on 3/14/17.
 */

public class TableActivity extends AppCompatActivity {

    @BindView(R.id.tableRecyclerView)
    RecyclerView mTableRecyclerView;

    TableAdapter mTableAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);


        mTableRecyclerView.setLayoutManager(gridLayoutManager);

        mTableAdapter = new TableAdapter(this);

        mTableRecyclerView.setAdapter(mTableAdapter);

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

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onPartyDialogOpen(ShowPartyDialogEvent event) {
        PartySizeDialog.newInstance(event.getTableId()).show(getSupportFragmentManager(), "PARTY_DIALOG");
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkbox_deposit:
               if (checked)
                    Toast.makeText(this, "hello, I want a table", Toast.LENGTH_SHORT).show();
                //else
                break;


        }
    }




}
