package edu.ucf.cop4331c.dishdriver.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by copper on 3/14/17.
 */

public class DishDriverProvider {

    // NOTICE!!! If we start using SSL, make sure you change this connection
    // string so that it begins with "https".
    public static final String BASE_URL_DIGITAL_OCEAN = "http://45.55.187.43:6789/";

    public static final String BASE_URL_LOCAL = "http://10.0.0.3:6789/";
    public static final String DD_HEADER_CLIENT = "asdf123";

    private static DishDriverService sDishDriverService;

    public static DishDriverService getInstance() {
        if (sDishDriverService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_DIGITAL_OCEAN)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

            sDishDriverService = retrofit.create(DishDriverService.class);
        }

        return sDishDriverService;
    }
}

