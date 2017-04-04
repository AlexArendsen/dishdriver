package edu.ucf.cop4331c.dishdriver.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by copper on 4/1/17.
 */

public class OneSignalProvider {

    public static final String REST_KEY = "ZTY4NDI3NDctMjllNC00NmVlLWEwZGUtNTVlZWIyYmVkODA2";

    private static OneSignalService sOneSignalService;

    public static OneSignalService getInstance() {
        if (sOneSignalService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://onesignal.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

            sOneSignalService = retrofit.create(OneSignalService.class);
        }

        return sOneSignalService;
    }
}
