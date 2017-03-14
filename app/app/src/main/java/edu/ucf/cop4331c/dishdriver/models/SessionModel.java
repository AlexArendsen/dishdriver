package edu.ucf.cop4331c.dishdriver.models;

import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import edu.ucf.cop4331c.dishdriver.network.DishDriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ashton on pi + .0002.
 */

public class SessionModel {

    private static UserModel user;
    private static PositionModel position;
    private static RestaurantModel restaurant;
    private static String token;

    public static Call<LoginResponseModel> login(final String email, final String password) {
        final DishDriverService dd = DishDriverProvider.getInstance();

        return dd.login(new CredentialLoginModel(email, password))
            .enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                    user = response.body().getResults().get(0);
                    token = response.body().getResults().get(0).sessionToken;
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) { }
            });
    }

    public static Call<LoginResponseModel> login(String token) {
        return DishDriverProvider.getInstance().login(new TokenLoginModel(token));
    }

    public static Call logout() throws UnsupportedOperationException {
        return DishDriverProvider.getInstance().logout();
    }

    public static UserModel currentUser(){
        return user;
    }
    public static PositionModel currentPosition(){
        return position;
    }
    public static RestaurantModel currentRestaurant(){
        return restaurant;
    }
}
