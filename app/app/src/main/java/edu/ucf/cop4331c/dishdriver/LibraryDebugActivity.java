package edu.ucf.cop4331c.dishdriver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import edu.ucf.cop4331c.dishdriver.models.LoginResponseModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantQueryModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.id.message;

public class LibraryDebugActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LIBRARY_DEBUG_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_debug);

        Button button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        // We will "search" for a restaurant. This will just return all of the restaurants
        RestaurantModel.search("").observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<RestaurantModel>>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onNext(List<RestaurantModel> restaurantModels) {
                String msg = "Restaurants: ";
                for(RestaurantModel r : restaurantModels) msg += r.getName() + "; ";
                Toast.makeText(LibraryDebugActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
