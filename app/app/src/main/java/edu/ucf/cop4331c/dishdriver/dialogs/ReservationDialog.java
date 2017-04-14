package edu.ucf.cop4331c.dishdriver.dialogs;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.NavigationActivity;
import edu.ucf.cop4331c.dishdriver.R;
import edu.ucf.cop4331c.dishdriver.models.DishModel;
import edu.ucf.cop4331c.dishdriver.models.NonQueryResponseModel;
import edu.ucf.cop4331c.dishdriver.models.OrderModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import edu.ucf.cop4331c.dishdriver.models.TableModel;
import edu.ucf.cop4331c.dishdriver.models.TableReservationModel;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xdroid.toaster.Toaster;

/**
 * Created by viviennedo on 3/15/17.
 */

public class ReservationDialog extends DialogFragment {

    private static final String TAG = "RESRVATION_DIALOG";
    private EditText mEditText;
    private  EditText mEditTextSize;
    private TimePicker mTimePicker;
    private CheckBox mDepositCheckBox;
    private Button mSubmitButton;
    private TableModel mTableModel;

    public ReservationDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReservationDialog newInstance(String title, int tablePosition, TableModel tableModel) {
        ReservationDialog frag = new ReservationDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("TABLE_NUMBER", tablePosition);
        args.putString("TABLE_MODEL", new Gson().toJson(tableModel));
        frag.setArguments(args);
        return frag;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_reservation, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int tablePosition = getArguments().getInt("TABLE_NUMBER");
        mTableModel = new Gson().fromJson(getArguments().getString("TABLE_MODEL"), TableModel.class);

        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.reservationNameEditText);
        mEditTextSize = (EditText) view.findViewById(R.id.reservationPartySizeEditText);
        mDepositCheckBox = (CheckBox) view.findViewById(R.id.reservationDepositCheckBox);
        mSubmitButton = (Button) view.findViewById(R.id.reservationConfirmationButton);

        mSubmitButton.setEnabled(false);
        Date myDate = new Date();
        myDate.setTime(System.currentTimeMillis());

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), String.valueOf(tablePosition), Toast.LENGTH_SHORT).show();
                // Toaster.toast("Does this work hmmmm?");

                if(isEmpty(mEditText)) {
                    mEditText.setError("Please input a reservation name");
                    return;
                }

                if(isEmpty(mEditTextSize)) {
                    mEditTextSize.setError("Please input a party size");
                    return;
                }


//                public TableReservationModel(Integer id, Integer tableId, String partyName,
//                Integer partySize, Integer deposit, Date dTRequested, Date dTAccepted)


                    TableReservationModel newReservation = new TableReservationModel(1, 0, mEditText.getText().toString(),
                            Integer.parseInt(mEditTextSize.getText().toString()), 1, myDate, myDate);


//                newReservation.setPartyName(mEditText.getText().toString());
//                newReservation.setTableId(0);

                TableReservationModel.forRestaurant(SessionModel.currentRestaurant())
                        .subscribeOn(Schedulers.io())
                        .flatMap(tableReservationModels -> {
                            for (TableReservationModel tableReservationModel : tableReservationModels) {

                                if (tableReservationModel.getTableId().equals(mTableModel.getId())) {
                                    return Observable.error(new Exception("TABLE RESERVED"));
                                }
                            }

                            return mTableModel.reserve(mEditText.getText().toString(), Integer.valueOf(mEditTextSize.getText().toString()), 0, myDate).asObservable();
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NonQueryResponseModel>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "onCompleted: COMPLETE");
                                dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: FAILURE", e);
                                dismiss();
                            }

                            @Override
                            public void onNext(NonQueryResponseModel nonQueryResponseModel) {
                                Toast.makeText(getContext(), "Reservation Made", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        });
//
//                mTableModel.reserve(mEditText.getText().toString(), Integer.valueOf(mEditTextSize.getText().toString()), 0, myDate).asObservable()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<NonQueryResponseModel>() {
//                            @Override
//                            public void onCompleted() {
//                                Log.d(TAG, "onCompleted: COMPLETE");
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.e(TAG, "onError: FAILURE", e);
//                            }
//
//                            @Override
//                            public void onNext(NonQueryResponseModel nonQueryResponseModel) {
//                                Toast.makeText(getContext(), "Reservation Made", Toast.LENGTH_SHORT).show();
//                            }
//                        });

                // go back to SignInActivity
//                Intent reservationIntent = new Intent(getActivity(), SignInActivity.class);
//                getActivity().startActivity(reservationIntent);


            }
        });

        mDepositCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSubmitButton.setEnabled(true);
                    //exit this out
                } else {
                    mSubmitButton.setEnabled(false);
                }
            }
        });

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
    }
}