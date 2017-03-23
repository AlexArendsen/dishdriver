package edu.ucf.cop4331c.dishdriver.network;

import edu.ucf.cop4331c.dishdriver.models.CredentialLoginModel;
import edu.ucf.cop4331c.dishdriver.models.LoginResponseModel;
import edu.ucf.cop4331c.dishdriver.models.LogoutResponseModel;
import edu.ucf.cop4331c.dishdriver.models.PositionQueryModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantQueryModel;
import edu.ucf.cop4331c.dishdriver.models.SqlModel;
import edu.ucf.cop4331c.dishdriver.models.TokenLoginModel;
import edu.ucf.cop4331c.dishdriver.models.UsersQueryModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by copper on 3/14/17.
 */

public interface DishDriverService {
    // Query d00dz
    @POST("/query")
    Call<UsersQueryModel> queryUsers(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Call<RestaurantQueryModel> queryRestaurants(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Observable<RestaurantQueryModel> queryRestaurantsObservable(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Call<PositionQueryModel> queryPositions(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    // The Login Bros
    @POST("/login")
    Call<LoginResponseModel> login(@Body CredentialLoginModel credentials);

    @POST("/login")
    Call<LoginResponseModel> login(@Body TokenLoginModel token);

    @POST("/login")
    Observable<LoginResponseModel> loginObservable(@Header("dd-token-client") String value, @Body CredentialLoginModel token);

    @POST("/login")
    Observable<LoginResponseModel> loginObservable(@Header("dd-token-client") String value, @Body TokenLoginModel token);

    // The lone logout man
    @POST("/logout")
    Call<LogoutResponseModel> logout();

}
