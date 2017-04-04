package edu.ucf.cop4331c.dishdriver;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminEditUserActivity extends AppCompatActivity {

    @BindView(R.id.positionSpinner)
    Spinner mPositionSpinner;
    @BindView((R.id.showAllUserSpinner))
    Spinner mShowAllUserSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_activity);
        ButterKnife.bind(this);

        final ArrayList<String> positionList = new ArrayList<>();
        positionList.add("Positions");
        positionList.add("Waiter");
        positionList.add("Cook");

        ArrayAdapter<String> positionAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, positionList);
        mPositionSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        mPositionSpinner.setAdapter(positionAdapter);

        final ArrayList<String> showAllUserList = new ArrayList<>();
        showAllUserList.add("Select a User");
        showAllUserList.add("Ashton");
        showAllUserList.add("Alex");
        showAllUserList.add("Chad Dude");
        showAllUserList.add("Chris");

        ArrayAdapter<String> showAllUserAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, showAllUserList);
        mShowAllUserSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        mShowAllUserSpinner.setAdapter(showAllUserAdapter);







    }
}