package edu.ucf.cop4331c.dishdriver.models;

import java.util.List;

import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import edu.ucf.cop4331c.dishdriver.network.DishDriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xdroid.toaster.Toaster;

/**
 * Created by ashton on pi + .0002.
 */

public class SessionModel {

    private static UserModel sUser;
    private static PositionModel sPosition;
    private static RestaurantModel sRestaurant;
    private static String sToken;
    private static String restaurantName = "%Thai%";

    public static Observable<LoginResponseModel> login(final String email, final String password) {
        final DishDriverService dd = DishDriverProvider.getInstance();

        sRestaurant = new RestaurantModel();

        // search for a restaurant
        RestaurantModel.search(restaurantName).subscribe(new Subscriber<List<RestaurantModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<RestaurantModel> restaurantModels) {
                if (!restaurantModels.isEmpty()) {
                    sRestaurant = restaurantModels.get(0);
                    sRestaurant.employees().subscribe(new Subscriber<List<PositionModel>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<PositionModel> positionModels) {
                            for (PositionModel positionModel : positionModels) {
                                if (positionModel.getEmployeeID().equals(sUser.getID()))
                                    sPosition = positionModel;
                            }
                        }
                    });
                }
            }
        });

        return dd.loginObservable(DishDriverProvider.DD_HEADER_CLIENT, new CredentialLoginModel(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(loginResponseModel -> {
                    sUser = loginResponseModel.getResults().get(0);
                    sToken = sUser.sessionToken;
                    return Observable.just(loginResponseModel);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<LoginResponseModel> login(String token) {
        final DishDriverService dd = DishDriverProvider.getInstance();

        return dd.loginObservable(DishDriverProvider.DD_HEADER_CLIENT, new TokenLoginModel(token))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(loginResponseModel -> {
                    sUser = loginResponseModel.getResults().get(0);
                    sToken = sUser.sessionToken;
                    return Observable.just(loginResponseModel);
                });
    }

    public static Call logout() throws UnsupportedOperationException {
        return DishDriverProvider.getInstance().logout();
    }

    public static UserModel currentUser(){
        return sUser;
    }
    public static PositionModel currentPosition(){
        return sPosition;
    }
    public static RestaurantModel currentRestaurant(){
        return sRestaurant;
    }
}
