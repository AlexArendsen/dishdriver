package edu.ucf.cop4331c.dishdriver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        RestaurantModel.search("P").enqueue(new Callback<RestaurantQueryModel>() {
            @Override
            public void onResponse(Call<RestaurantQueryModel> call, Response<RestaurantQueryModel> response) {
                RestaurantQueryModel body = response.body();
                String message = "Oh no! Could not find any restaurants!";
                RestaurantModel[] results = null;

                if (body == null) message = "No response body found :(";
                else results = body.getResults();

                if (results != null && results.length > 0) message = "Found one! It's called " + results[0].getName();
                Toast.makeText(LibraryDebugActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<RestaurantQueryModel> call, Throwable t) {
                Toast.makeText(LibraryDebugActivity.this, "There was a problem!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
