package edu.ucf.cop4331c.dishdriver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.ucf.cop4331c.dishdriver.enums.Role;
import edu.ucf.cop4331c.dishdriver.models.NonQueryResponseModel;
import edu.ucf.cop4331c.dishdriver.models.PositionModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import edu.ucf.cop4331c.dishdriver.models.UserModel;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AdminEditUserActivity extends AppCompatActivity {

    @BindView(R.id.positionSpinner)
    Spinner mPositionSpinner;
    @BindView((R.id.showAllUserSpinner))
    Spinner mShowAllUserSpinner;
    @BindView(R.id.deleteUserButton)
    Button mDeleteUserButton;
    @BindView(R.id.generatePasswordButton)
    Button mGeneratePasswordButton;
    @BindView(R.id.firstNameEditText)
    EditText mFirstNameEditText;
    @BindView(R.id.lastNameEditText)
    EditText mLastNameEditText;
    @BindView(R.id.emailEditText)
    EditText mEmailEditText;
    @BindView(R.id.passwordTextView)
    EditText mPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_activity);
        ButterKnife.bind(this);

        SessionModel.currentUser();
        SessionModel.currentPosition();
        SessionModel.currentRestaurant();

        System.out.print(SessionModel.currentRestaurant().getId());

        final ArrayList<String> positionList = new ArrayList<>();
        //this will never change so we are hard coding this in :)
        positionList.add("Positions");
        positionList.add("Waiter");
        positionList.add("Cook");

        ArrayAdapter<String> positionAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, positionList);
        mPositionSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        mPositionSpinner.setAdapter(positionAdapter);

        final ArrayList<String> showAllUserList = new ArrayList<>();
        showAllUserList.add("Select a User");
        PositionModel.forRestaurant(SessionModel.currentRestaurant()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<PositionModel>>() {
            @Override
            public void onCompleted() {
                //do nothing, we want to retrieve info onNext

            }

            @Override
            public void onError(Throwable e) {
                //do nothing, we want to retrieve info onNext
            }

            @Override
            public void onNext(List<PositionModel> positionModels) {
                for (PositionModel p : positionModels) {
                    showAllUserList.add(p.getEmployeeName());
                }
            }
        });
        /*           ^^
        showAllUserList.add("Ashton");
        showAllUserList.add("Alex");
        showAllUserList.add("Chad Dude");
        showAllUserList.add("Chris");*/

        ArrayAdapter<String> showAllUserAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, showAllUserList);
        mShowAllUserSpinner.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        mShowAllUserSpinner.setAdapter(showAllUserAdapter);
    }

    @OnClick(R.id.deleteUserButton)
    public void onDeleteUserButtonClicked() {
        //delete user
        PositionModel.forUser((UserModel) mShowAllUserSpinner.getSelectedItem()).subscribe(new Subscriber<List<PositionModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //Deleted User not Successful message
                new AlertDialog.Builder(AdminEditUserActivity.this)
                        .setTitle("Failed to Delete")
                        .setMessage("Sorry, this user does not exist. Please close the app and try again." + e)
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public void onNext(List<PositionModel> positionModels) {
                positionModels.get(0).unhire();

            }
        });


    }

    ;

    @OnClick(R.id.generatePasswordButton)
    public void onGenerateUserButtonClicked() {
        //generate password
        String password = "Pandora123!";
        mPasswordTextView.setText(password);

        //validate
        if ((mEmailEditText.getText().toString().length() > 1) && (mFirstNameEditText.getText().toString().length() > 1) && (mLastNameEditText.getText().toString().length() > 1))
            //check if this user is already created
            if (UserModel.findFirst(mEmailEditText.getText().toString()) != null) {
                //create the new user
                UserModel newUser = new UserModel(mEmailEditText.getText().toString(), mPasswordTextView.getText().toString(), mFirstNameEditText.getText().toString(), mLastNameEditText.getText().toString());
                newUser.create()
                        .subscribeOn(Schedulers.io())
                        .flatMap(nonQueryResponseModel -> {
                            newUser.setID(nonQueryResponseModel.getResults().getInsertId());
                            return PositionModel.create(newUser, SessionModel.currentRestaurant(), Role.Waiter);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NonQueryResponseModel>() {
                            @Override
                            public void onCompleted() {
                                //successful message
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                //successful message
                                new AlertDialog.Builder(AdminEditUserActivity.this)
                                        .setTitle("Failed")
                                        .setMessage("Sorry, please try again at a later time." + e)
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }

                            @Override
                            public void onNext(NonQueryResponseModel nonQueryResponseModel) {
                                //successful message
                                new AlertDialog.Builder(AdminEditUserActivity.this)
                                        .setTitle("Successful!")
                                        .setMessage("This user has been created! Please write down the generated password for this user at this time.")
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                ArrayAdapter<String> showAllUserAdapter = (ArrayAdapter<String>) mShowAllUserSpinner.getAdapter();
                            }
                        });
            } else {
                new AlertDialog.Builder(AdminEditUserActivity.this)
                        .setTitle("User Error")
                        .setMessage("This user already exists. Please use a different email address.")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        else {

            new AlertDialog.Builder(AdminEditUserActivity.this)
                    .setTitle("Validation Error")
                    .setMessage("All fields where not filled out.Please take this time to fill in everything.")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }
}