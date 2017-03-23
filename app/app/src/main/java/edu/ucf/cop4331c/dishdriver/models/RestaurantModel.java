package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

import edu.ucf.cop4331c.dishdriver.enums.Roles;
import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rebeca on 3/14/2017.
 */

public class RestaurantModel {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("DT_Opened")
    @Expose
    private Date dTOpened;
    @SerializedName("DT_Closed")
    @Expose
    private Date dTClosed;

    public RestaurantModel(Integer id, String name, Date dTOpened, Date dTClosed) {
        this.id = id;
        this.name = name;
        this.dTOpened = dTOpened;
        this.dTClosed = dTClosed;
    }

    public static Call<RestaurantQueryModel> get(int id) {
        return DishDriverProvider.getInstance().queryRestaurants(DishDriverProvider.DD_HEADER_CLIENT, new SqlModel(
                "SELECT * FROM Restaurants WHERE Id = ?",
                new String[] {Integer.toString(id)}
        ));
    }

    public static Call<RestaurantQueryModel> search(String query) {

        // Reformat query argument. We're only offering a prefix search, so
        // we will only be adding the 0+ wildcard character (%) to the end
        // of the query.
        query += "%";

        return DishDriverProvider.getInstance().queryRestaurants(DishDriverProvider.DD_HEADER_CLIENT, new SqlModel(
                "SELECT * FROM Restaurants WHERE Name LIKE ?",
                new String[] { query }
        ));
    }

    public static Call<RestaurantQueryModel> forUser(PositionModel position) {

        return DishDriverProvider.getInstance().queryRestaurants(DishDriverProvider.DD_HEADER_CLIENT, new SqlModel(
                "SELECT R.* FROM Restaurants R JOIN Positions P ON R.Id = P.Restaurant_ID WHERE P.Id = ?",
                new String[] { Integer.toString(position.getId()) }
        ));

    }

    public Call<ArrayList<PositionModel>> waiters() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<ArrayList<PositionModel>> cooks() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<ArrayList<PositionModel>> admins() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<Integer> create() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call update()throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<ArrayList<DishesModel>> menu()throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call saveMenu(ArrayList<DishesModel> dishes)throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call addEmployee(RegistrantModel registrant, Roles roles)throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call addEmployee(UserModel user, Roles roles)throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getdTOpened() {
        return dTOpened;
    }

    public void setdTOpened(Date dTOpened) {
        this.dTOpened = dTOpened;
    }

    public Date getdTClosed() {
        return dTClosed;
    }

    public void setdTClosed(Date dTClosed) {
        this.dTClosed = dTClosed;
    }
}
