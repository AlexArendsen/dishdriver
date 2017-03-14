package edu.ucf.cop4331c.dishdriver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.adapters.TableAdapter;

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

        mTableAdapter = new TableAdapter(getApplicationContext());

        mTableRecyclerView.setAdapter(mTableAdapter);
    }
}
