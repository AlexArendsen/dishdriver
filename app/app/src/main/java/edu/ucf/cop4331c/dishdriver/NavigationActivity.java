package edu.ucf.cop4331c.dishdriver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by viviennedo on 3/16/17.
 */

public class NavigationActivity extends AppCompatActivity {
    private int mTableNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Retrieves the table number from the intent that we passed in the PartySizeDialog.
        mTableNumber = getIntent().getIntExtra("PARTY_NUMBER", 0);

//        switch (mTableNumber) {
//            case 1:
//                break;
//            case 2:
//                break;
//            //...
//        }

        // Sets the textView to be the party size that you have selected from the previous PartySizeDialog.
        TextView textView = (TextView) findViewById(R.id.tableNameTextView);
        textView.setText(String.valueOf(mTableNumber));
    }
}
