package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UnknownFormatConversionException;
import java.util.function.Consumer;

import edu.ucf.cop4331c.dishdriver.enums.Roles;
import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    public static Observable<ArrayList<RestaurantModel>> search(String query) {

        // Reformat query argument. We're only offering a prefix search, so
        // we will only be adding the 0+ wildcard character (%) to the end
        // of the query.
        query += "%";

        // TODO -- This will be painful to write every time. It should be abstracted into a static
        //         helper method somewhere

        return DishDriverProvider.getInstance().queryRestaurantsObservable(DishDriverProvider.DD_HEADER_CLIENT, new SqlModel(
                "SELECT * FROM Restaurants WHERE Name LIKE ?",
                new String[] { query }
        ))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {
                    if (qm == null || qm.getResults() == null) return Observable.just(new ArrayList<RestaurantModel>());
                    return Observable.just(new ArrayList<RestaurantModel>(Arrays.asList(qm.getResults())));
                }).observeOn(AndroidSchedulers.mainThread());

    }

    public static Call<RestaurantQueryModel> forUser(PositionModel position) {

        return DishDriverProvider.getInstance().queryRestaurants(DishDriverProvider.DD_HEADER_CLIENT, new SqlModel(
                "SELECT R.* FROM Restaurants R JOIN Positions P ON R.Id = P.Restaurant_ID WHERE P.Id = ?",
                new String[] { Integer.toString(position.getId()) }
        ));

    }

    public Call<PositionQueryModel> waiters() {

        return DishDriverProvider.getInstance().queryPositions(DishDriverProvider.DD_HEADER_CLIENT, new SqlModel(
                "SELECT * FROM Positions P WHERE Restaurant_ID = ? AND Role_ID = 2",
                new String[] { Integer.toString(id) }
        ));

    }

    public Call<PositionQueryModel> cooks() {

        return DishDriverProvider.getInstance().queryPositions(DishDriverProvider.DD_HEADER_CLIENT, new SqlModel(
                "SELECT * FROM Positions P WHERE Restaurant_ID = ? AND Role_ID = 3",
                new String[] { Integer.toString(id) }
        ));

    }

    public Call<PositionQueryModel> admins() {

        return DishDriverProvider.getInstance().queryPositions(DishDriverProvider.DD_HEADER_CLIENT, new SqlModel(
                "SELECT * FROM Positions P WHERE Restaurant_ID = ? AND Role_ID = 1",
                new String[] { Integer.toString(id) }
        ));

    }

    public Call<Integer> create() throws UnsupportedOperationException {
        // NOTE: We will need to create a new class called RestaurantScriptModel that will expose
        // the new record's ID, and that will take some elbow grease.

        new SqlModel(
                "INSERT INTO Restaurants (Name, DT_Opened) VALUES (?, NOW())",
                new String[] { this.getName() }
        );

        throw new UnsupportedOperationException();
    }

    public Call update() throws UnsupportedOperationException {
        // NOTe: RestaurantSccriptModel or whatever we end up calling it will be used here too.

        new SqlModel(
                "UPDATE Restaurants SET Name = ? WHERE Id = ?",
                new String[] { name, Integer.toString(id) }
        );

        throw new UnsupportedOperationException();
    }

    public Call<DishesQueryModel> menu() {
        return null;
        /*return DishDriverProvider.getInstance().queryDishes(new SqlModel(
                "SELECT * FROM Dishes WHERE Restaurant_ID = ? AND DT_Deleted IS NULL",
                new String[] { Integer.toString(id) }
        ));*/
    }

    public Call saveMenu(ArrayList<DishesModel> dishes) throws UnsupportedOperationException {
        // TODO -- Write this...
        // dishes.stream().map(... into a SQL query? An ORM would be so nice for this.

        throw new UnsupportedOperationException();
    }

    public Call addEmployee(UserModel user, Roles role) throws UnsupportedOperationException {

        new SqlModel(
                "INSERT INTO Positions (Employee_ID, Role_ID, Restaurant_ID, DT_Hired) VALUES (?, ?, ?, NOW())",
                new String[] { Integer.toString(user.getID()), Integer.toString(role.ordinal()), Integer.toString(id) }
        );

        throw new UnsupportedOperationException();
    }

    // Gonna throw this one out for now
    /*public Call addEmployee(RegistrantModel user, Roles roles) throws UnsupportedOperationException {

        return

        throw new UnsupportedOperationException();
    }*/

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
