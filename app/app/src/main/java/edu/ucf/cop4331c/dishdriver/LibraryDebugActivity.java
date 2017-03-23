package edu.ucf.cop4331c.dishdriver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import edu.ucf.cop4331c.dishdriver.models.LoginResponseModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantQueryModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

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

        // We will search for a restaurant whose name begins with a capital P
        RestaurantModel.search("").subscribe(new Subscriber<ArrayList<RestaurantModel>>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onNext(ArrayList<RestaurantModel> restaurantModels) {
                if (restaurantModels.size() > 0)
                    Toast.makeText(
                        LibraryDebugActivity.this,
                        "Found restaurants! The first one is called " + restaurantModels.get(0).getName(),
                        Toast.LENGTH_LONG
                    ).show();
                else Toast.makeText(LibraryDebugActivity.this, "None found! :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
