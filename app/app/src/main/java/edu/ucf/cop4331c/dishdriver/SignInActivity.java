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

        EditText UserName = (EditText) findViewById(R.id.userNameEditText);
        EditText Password = (EditText) findViewById(R.id.passwordEditText);

        String userName = UserName.getText().toString();
        String password = Password.getText().toString();

        SessionModel.login(userName, password).subscribe(new Subscriber<LoginResponseModel>() {
            @Override
            public void onCompleted() {
                //Toaster.toast("onComplete");
            

                if (SessionModel.currentUser() != null) {
                    PositionModel.forUser(SessionModel.currentUser()).subscribe(new Subscriber<List<PositionModel>>() {
                        @Override
                        public void onCompleted() {
                           // Toaster.toast("test");


                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, e.getMessage());// "onError: ERROR ttt");
                            return;
                        }

                        @Override
                        public void onNext(List<PositionModel> positionModels) {
                            Toaster.toast(positionModels.get(0).getRestaurantID());
                            //Toaster.toast(""+ positionModels.size());

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

    public void toPage(UserModel userModel){


        if (userModel != null) {
            PositionModel.forUser(userModel).subscribe(new Subscriber<List<PositionModel>>() {
                @Override
                public void onCompleted() {
                    Toast.makeText(SignInActivity.this, "hey", Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<PositionModel> positionModels) {
                    Toast.makeText(SignInActivity.this, positionModels.size(), Toast.LENGTH_SHORT).show();
                }
            });
        }else
            Toast.makeText(SignInActivity.this, "This guy is not an employee", Toast.LENGTH_SHORT).show();


    }
}