package edu.ucf.cop4331c.dishdriver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.ucf.cop4331c.dishdriver.models.LoginResponseModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import rx.Subscriber;

public class LibraryDebugActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LIBRARY_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_debug);

        Button button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Log.d("MR.bool", "We signed in");
        SessionModel.login("ashton@dishdriver.com", "password").subscribe(new Subscriber<LoginResponseModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ERROR");
            }

            @Override
            public void onNext(LoginResponseModel loginResponseModel) {
                Toast.makeText(LibraryDebugActivity.this, loginResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
