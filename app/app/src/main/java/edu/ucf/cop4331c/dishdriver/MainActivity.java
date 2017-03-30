package edu.ucf.cop4331c.dishdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.ucf.cop4331c.dishdriver.models.SqlModel;
import edu.ucf.cop4331c.dishdriver.models.UsersQueryModel;
import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final String TAG = "whatevs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DishDriverProvider.getInstance().queryUsers(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel("SELECT * FROM Users", new String[] {""})
        ).enqueue(new Callback<UsersQueryModel>() {
            @Override
            public void onResponse(Call<UsersQueryModel> call, Response<UsersQueryModel> response) {
                Log.d(TAG, "onResponse: Some bullshit");
            }

            @Override
            public void onFailure(Call<UsersQueryModel> call, Throwable t) {
                Log.d(TAG, "onFailure: Some bullshit");
            }
        });
    }

    @OnClick(R.id.takeMeToAppButton)
    public void navigateToApp() {
        startActivity(new Intent(this, SignInActivity.class));
    }

    @OnClick(R.id.takeMeToLibraryButton)
    public void navigateToLibrary() {
        startActivity(new Intent(this, LibraryDebugActivity.class));
    }

}
