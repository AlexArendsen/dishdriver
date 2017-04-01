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
import edu.ucf.cop4331c.dishdriver.network.NotificationService;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xdroid.toaster.Toaster;

/**
 * Created by Melissa on 3/14/2017.
 * @author Ashton Ansag
 */

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private UserModel userModel;

    void setUserModel(UserModel userModel){
        this.userModel = userModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginButton)
    public void login(View v){

        NotificationService.broadcast("A Test Notification", "'''''There should be no single-quotes at the beginning of this message");

        EditText UserName = (EditText) findViewById(R.id.userNameEditText);
        EditText Password = (EditText) findViewById(R.id.passwordEditText);

        // TODO -- Remove this
        UserName.setText("tj@dishdriver.com");
        Password.setText("password");

        String userName = UserName.getText().toString();
        String password = Password.getText().toString();

        SessionModel.login(userName, password).subscribe(new Subscriber<LoginResponseModel>() {
            @Override
            public void onCompleted() {


                if (SessionModel.currentUser() != null) {
                    PositionModel.forUser(SessionModel.currentUser()).subscribe(new Subscriber<List<PositionModel>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, e.getMessage());
                            return;
                        }

                        @Override
                        public void onNext(List<PositionModel> positionModels) {
                            Toaster.toast(positionModels.get(0).getRole().toString());

                            switch(positionModels.get(0).getRoleID()){
                                case 1:
                                    startActivity(new Intent(SignInActivity.this, AdminNavigationActivity.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(SignInActivity.this, CookActivity.class));
                                    break;
                                case 3:
                                    startActivity(new Intent(SignInActivity.this, TableActivity.class));
                                    break;
                                default:
                                    Toaster.toast("fail...");
                            }
                            finish();
                        }
                    });
                }else
                    Toaster.toast("Not an employee");


            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ERROR");
                return;
            }

            @Override
            public void onNext(LoginResponseModel loginResponseModel) {

                //Toast.makeText(SignInActivity.this, "onComplete", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(SignInActivity.this, CookActivity.class));
                UserName.setText("");//overkill
                Password.setText("");//overkill but maybe useful with signoutbutton probably not
                //finish();
            }
        });

    }
}
