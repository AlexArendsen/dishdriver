package edu.ucf.cop4331c.dishdriver;

import android.app.Application;

import com.onesignal.OneSignal;

/**
 * Created by copper on 3/29/17.
 */

public class DishDriverApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();
    }
}
