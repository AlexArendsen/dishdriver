package edu.ucf.cop4331c.dishdriver.network;

import edu.ucf.cop4331c.dishdriver.models.SqlModel;
import edu.ucf.cop4331c.dishdriver.models.UsersQueryModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by copper on 3/14/17.
 */

public interface DishDriverService {
    @POST("/query")
    Call<UsersQueryModel> queryUsers(@Body SqlModel sqlModel);
}
