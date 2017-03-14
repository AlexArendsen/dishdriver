package edu.ucf.cop4331c.dishdriver.network;

import edu.ucf.cop4331c.dishdriver.models.CredentialLoginModel;
import edu.ucf.cop4331c.dishdriver.models.LoginResponseModel;
import edu.ucf.cop4331c.dishdriver.models.LogoutResponseModel;
import edu.ucf.cop4331c.dishdriver.models.SqlModel;
import edu.ucf.cop4331c.dishdriver.models.TokenLoginModel;
import edu.ucf.cop4331c.dishdriver.models.UsersQueryModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by copper on 3/14/17.
 */

public interface DishDriverService {
    @POST("/query")
    Call<UsersQueryModel> queryUsers(@Body SqlModel sqlModel);

    @POST("/login")
    Call<LoginResponseModel> login(@Body CredentialLoginModel credentials);

    @POST("/login")
    Call<LoginResponseModel> login(@Body TokenLoginModel token);

    @POST("/login")
    Observable<LoginResponseModel> loginObservable(@Body CredentialLoginModel token);

    @POST("/login")
    Observable<LoginResponseModel> loginObservable(@Body TokenLoginModel token);

    @POST("/logout")
    Call<LogoutResponseModel> logout();

}
