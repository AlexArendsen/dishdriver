package edu.ucf.cop4331c.dishdriver.models;

import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import edu.ucf.cop4331c.dishdriver.network.DishDriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ashton on pi + .0002.
 */

public class SessionModel {

    private static UserModel user;
    private static PositionModel position;
    private static RestaurantModel restaurant;
    private static String token;

    public static Observable<LoginResponseModel> login(final String email, final String password) {
        final DishDriverService dd = DishDriverProvider.getInstance();

        return dd.loginObservable(new CredentialLoginModel(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(loginResponseModel -> {
                    user = loginResponseModel.getResults().get(0);
                    token = loginResponseModel.getResults().get(0).sessionToken;
                    return Observable.just(loginResponseModel);
                })
                .observeOn(AndroidSchedulers.mainThread());
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
