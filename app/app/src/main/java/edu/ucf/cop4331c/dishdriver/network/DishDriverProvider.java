package edu.ucf.cop4331c.dishdriver.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by copper on 3/14/17.
 */

public class DishDriverProvider {
    public static final String BASE_URL = "http://192.168.0.5:6789/";
    public static final String DD_HEADER_CLIENT = "asdf123";

    private static DishDriverService sDishDriverService;

    public static DishDriverService getInstance() {
        if (sDishDriverService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

            sDishDriverService = retrofit.create(DishDriverService.class);
        }

        return sDishDriverService;
    }
}

