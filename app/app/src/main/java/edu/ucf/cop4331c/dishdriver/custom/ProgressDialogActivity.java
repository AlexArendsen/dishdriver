package edu.ucf.cop4331c.dishdriver.custom;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by viviennedo on 4/4/17.
 */

// You can extend classes that need to use the progressbar 
// This creates a Progress bar that will appear whenever you try to access the database

public class ProgressDialogActivity extends AppCompatActivity {
    ProgressDialog mProgressDialog;

    public void enableProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
