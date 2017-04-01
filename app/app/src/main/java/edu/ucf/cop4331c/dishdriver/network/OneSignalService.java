package edu.ucf.cop4331c.dishdriver.network;

import edu.ucf.cop4331c.dishdriver.models.PostNotificationModel;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Completable;
import rx.Observable;

/**
 * Created by copper on 4/1/17.
 */

public interface OneSignalService {

    @POST("/api/v1/notifications")
    Observable<Void> broadcast(
            @Header("Authorization") String rest_key,
            @Body PostNotificationModel model
            );

}
