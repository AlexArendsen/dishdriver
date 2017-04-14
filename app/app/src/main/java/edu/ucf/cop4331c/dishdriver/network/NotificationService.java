package edu.ucf.cop4331c.dishdriver.network;

import edu.ucf.cop4331c.dishdriver.models.PostNotificationModel;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by copper on 4/1/17.
 */

public class NotificationService {

    public static void broadcast(String title, String body) {
        OneSignalProvider.getInstance().broadcast(
                "Basic " + OneSignalProvider.REST_KEY,
                new PostNotificationModel(title, body)
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

}
