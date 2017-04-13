package edu.ucf.cop4331c.dishdriver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import xdroid.toaster.Toaster;

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
    public void onClick(View v) {

        // We will search for Chad Thai
        RestaurantModel.search("Chad").observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<RestaurantModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<RestaurantModel> restaurantModels) {
                        String msg = "Restaurants: ";
                        for (RestaurantModel r : restaurantModels) msg += r.getName() + "; ";
                        Toaster.toast(msg);
                    }
                });
    }
}
