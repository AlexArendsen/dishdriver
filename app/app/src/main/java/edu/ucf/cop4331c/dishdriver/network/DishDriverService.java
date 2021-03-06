package edu.ucf.cop4331c.dishdriver.network;

import edu.ucf.cop4331c.dishdriver.models.CredentialLoginModel;
import edu.ucf.cop4331c.dishdriver.models.DishQueryModel;
import edu.ucf.cop4331c.dishdriver.models.LoginResponseModel;
import edu.ucf.cop4331c.dishdriver.models.LogoutResponseModel;
import edu.ucf.cop4331c.dishdriver.models.NonQueryResponseModel;
import edu.ucf.cop4331c.dishdriver.models.OrderQueryModel;
import edu.ucf.cop4331c.dishdriver.models.OrderedDishQueryModel;
import edu.ucf.cop4331c.dishdriver.models.PositionQueryModel;
import edu.ucf.cop4331c.dishdriver.models.RestaurantQueryModel;
import edu.ucf.cop4331c.dishdriver.models.ReviewQueryModel;
import edu.ucf.cop4331c.dishdriver.models.SqlModel;
import edu.ucf.cop4331c.dishdriver.models.TableQueryModel;
import edu.ucf.cop4331c.dishdriver.models.TableReservationQueryModel;
import edu.ucf.cop4331c.dishdriver.models.TokenLoginModel;
import edu.ucf.cop4331c.dishdriver.models.UserQueryModel;
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
    Observable<UserQueryModel> queryUsers(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Observable<RestaurantQueryModel> queryRestaurants(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Observable<PositionQueryModel> queryPositions(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Observable<DishQueryModel> queryDishes(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Observable<OrderedDishQueryModel> queryOrderedDishes(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Observable<ReviewQueryModel> queryReviews(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Observable<TableReservationQueryModel> queryTableReservations(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Observable<TableQueryModel> queryTables(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    @POST("/query")
    Observable<OrderQueryModel> queryOrders(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

    // Non-Query Stuffages (for INSERTs, UPDATEs, and DELETEs)
    @POST("/query")
    Observable<NonQueryResponseModel> nonQuery(@Header("dd-token-client") String token, @Body SqlModel sqlModel);

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
