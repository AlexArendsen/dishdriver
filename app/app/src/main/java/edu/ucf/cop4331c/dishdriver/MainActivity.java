package edu.ucf.cop4331c.dishdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    final String TAG = "whatevs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.takeMeToAppButton)
    public void navigateToApp() {
        startActivity(new Intent(this, SignInActivity.class));
    }

    @OnClick(R.id.takeMeToLibraryButton)
    public void navigateToLibrary() {
        startActivity(new Intent(this, LibraryDebugActivity.class));
    }

}
