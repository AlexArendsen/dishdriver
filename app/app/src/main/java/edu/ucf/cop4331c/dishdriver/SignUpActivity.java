package edu.ucf.cop4331c.dishdriver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.models.LoginResponseModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import rx.Subscriber;

/**
 * Created by Melissa on 3/14/2017.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        Button button = (Button) findViewById(R.id.LoginButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        EditText UserName = (EditText) findViewById(R.id.UserName);
        String userName = UserName.getText().toString();

        EditText Password = (EditText) findViewById(R.id.Password);
        String password = Password.getText().toString();

        // We will "search" for a restaurant. This will just return all of the restaurants
/*        RestaurantModel.search("").subscribe(new Subscriber<List<RestaurantModel>>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onNext(List<RestaurantModel> restaurantModels) {
                String msg = "Restaurants: ";
                for(RestaurantModel r : restaurantModels) msg += "Hi; \n";
                Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
*/
        SessionModel.login(userName, password).subscribe(new Subscriber<LoginResponseModel>() {
            @Override
            public void onCompleted() { 
                Toast.makeText(SignUpActivity.this, "Completed?", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ERROR");
            }

            @Override
            public void onNext(LoginResponseModel loginResponseModel) {
                Toast.makeText(SignUpActivity.this, loginResponseModel.getCode(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(SignUpActivity.this, loginResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });;
    }
}
