package edu.ucf.cop4331c.dishdriver;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminNavigationActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_navigation);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.generateReport)
    public void generateReport(){
        startActivity(new Intent(AdminNavigationActivity.this, Admin_email_activity.class));
    }

    @OnClick(R.id.cookButton)
    public void cookButton(){
        startActivity(new Intent(AdminNavigationActivity.this, CookActivity.class));
    }

    @OnClick(R.id.editUser)
    public void editUser(){
        startActivity(new Intent(AdminNavigationActivity.this, AdminEditUserActivity.class));
    }

    @OnClick(R.id.waiterButton)
    public void waiterButton(){
        startActivity(new Intent(AdminNavigationActivity.this, TableActivity.class));
    }

    @OnClick(R.id.editMenu)
    public void editMenu(){
        startActivity(new Intent(AdminNavigationActivity.this, AdminEditMenuActivity.class));
    }
}