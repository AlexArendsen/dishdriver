package edu.ucf.cop4331c.dishdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.ucf.cop4331c.dishdriver.models.LoginResponseModel;
import edu.ucf.cop4331c.dishdriver.models.PositionModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import edu.ucf.cop4331c.dishdriver.models.UserModel;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Melissa on 3/14/2017.
 * @author Ashton Ansag
 */

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginButton)
    public void login(View v){

        EditText UserName = (EditText) findViewById(R.id.userNameEditText);
        String userName = UserName.getText().toString();

        EditText Password = (EditText) findViewById(R.id.passwordEditText);
        String password = Password.getText().toString();

        SessionModel.login(userName, password).subscribe(new Subscriber<LoginResponseModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ERROR");
                return;
            }

            @Override
            public void onNext(LoginResponseModel loginResponseModel) {

                startActivity(new Intent(SignInActivity.this, CookActivity.class));

                //Toast.makeText(SignInActivity.this, "size: "+ loginResponseModel.getResults().get(0).getEmail(), Toast.LENGTH_SHORT).show();
                //UserModel userModel = loginResponseModel.getResults().get(0);

                //Toast.makeText(SignInActivity.this, PositionModel.forUser(userModel).isEmpty();


                 //   return; // this is a customer

                //toPage(loginResponseModel.getResults().get(0));

                UserName.setText("");//overkill
                Password.setText("");//overkill but maybe useful with signoutbutton probably not
                //finish();
            }
        });
    }

    public void toPage(UserModel userModel){


    }
}
