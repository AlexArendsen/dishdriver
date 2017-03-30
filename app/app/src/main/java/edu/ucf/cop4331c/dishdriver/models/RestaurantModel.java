package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.enums.Role;
import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by rebeca on 3/14/2017.
 */

public class RestaurantModel {

    /* [Alex -- 27 March 2017] NOTE
     * -----------------
     * Please use this RestaurantModel class as a template for implementing the rest of the
     * DishDriver library classes. Here's a checklist:
     *
     * [] First: create a branch from meeting-two-library called library-<MODEL>-implement-<YOUR_NAME>
     *
     * [] You can skip writing SQL if you aren't confident in writing SQL.
     *
     * [] Implement the methods for that class:
     *      If the method returns instances of its own class, use the query() method from the
     *          first checklist item (eg, the `get()` method).
     *      If the method returns instances of a different class, use the query() method of that class
     *          to obtain the Observable (eg, the `menu()` method).
     *      If the method does some kind of mutation of the database (and INSERT or UPDATE), the
     *          associated method should return Observable<NonQueryResponseModel>, and use the
     *          NonQueryResponseModel.run() method (eg, the `addEmployee()` method).
     *
     * [] No implemented method should throw or declare an UnsupportedOperationException
     *
     * [] (Optional) Add JavaDoc. If you enter "/**" and press enter on the line above a method,
     *      Android Studio will spit out a block ready for you to fill out. This documentation step
     *      can take a long time-- I recommend you return to it only once you have completed
     *      implementing all of the classes you wish to implement. See the "DB Retrieval" section
     *      of this class for examples.
     *
     * [] (Optional) Add code regions to the class
     *
     * [] When all the methods have been implemented, commit and push your branch to VSTS, and then
     *      let me know so I can review it and merge it in.
     */

    // region Field Definitions
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

    // endregion

    // region Constructors
    public RestaurantModel(Integer id, String name, Date dTOpened, Date dTClosed) {
        this.id = id;
        this.name = name;
        this.dTOpened = dTOpened;
        this.dTClosed = dTClosed;
    }
    // endregion

    // region query() Definition
    public static Observable<List<RestaurantModel>> query(String sql, String[] args) {

        return DishDriverProvider.getInstance().queryRestaurants(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel(sql, args)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {

                    // If no response was sent, just give back an empty list so things don't
                    // explode in UI
                    if (qm == null || qm.getResults() == null) return Observable.just(new ArrayList<RestaurantModel>());
                    return Observable.just(Arrays.asList(qm.getResults()));

                });
    }
    // endregion

    // region DB Retrieval

    /**
     * Get the restaurant with the given ID number
     * @param id The ID number to search for
     * @return A list containing either the one restaurant with the ID, or no restaurants.
     */
    public static Observable<List<RestaurantModel>> get(int id) {
        return query("SELECT * FROM Restaurant WHERE Id = ?", new String[] {Integer.toString(id)});
    }

    /**
     * Searches for restaurants with names beginning with the given string.
     * @param query The string to search by
     * @return The first 25 restaurants whose names begin with the provide string.
     */
    public static Observable<List<RestaurantModel>> search(String query) {

        // Reformat query argument. We're only offering a prefix search, so
        // we will only be adding the 0+ wildcard character (%) to the end
        // of the query.
        query += "%";

        return query("SELECT * FROM Restaurants WHERE Name LIKE ? ORDER BY Name ASC LIMIT 25", new String[] {query});
    }

    /**
     * Gets all restaurants that the given user works for
     * @param position The user to use when finding restaurants
     * @return A list of the restaurants for which the given user works
     */
    public static Observable<List<RestaurantModel>> forUser(PositionModel position) {

        return query(
                "SELECT R.* FROM Restaurants R JOIN Positions P ON R.Id = P.Restaurant_ID WHERE P.Id = ?",
                new String[] { Integer.toString(position.getId()) }
        );

    }
    // endregion

    // region DB Modification
    public Observable<NonQueryResponseModel> create() {

        return NonQueryResponseModel.run(
                "INSERT INTO Restaurants" +
                        "(Name, DT_Opened)" +
                        "VALUES" +
                        "(?, NOW())",
                new String[] { getName() }
        );
    }

    public Observable<NonQueryResponseModel> update() {

        return NonQueryResponseModel.run(
                "UPDATE Restaurants SET" +
                        "Name = ? " +
                "WHERE Id = ?",
                new String[] { getName(), Integer.toString(getId()) }
        );

    }

    // endregion

    // region Employee Retrieval
    public Observable<List<PositionModel>> waiters() {

        return PositionModel.query(
                "SELECT * FROM Positions P WHERE Restaurant_ID = ? AND Role_ID = 2",
                new String[] { Integer.toString(id) }
        );

    }

    public Observable<List<PositionModel>> cooks() {

        return PositionModel.query(
                "SELECT * FROM Positions P WHERE Restaurant_ID = ? AND Role_ID = 3",
                new String[] { Integer.toString(id) }
        );

    }

    public Observable<List<PositionModel>> admins() {

        return PositionModel.query(
                "SELECT * FROM Positions P WHERE Restaurant_ID = ? AND Role_ID = 1",
                new String[] { Integer.toString(id) }
        );

    }

    public Observable<List<PositionModel>> employees() {

        return PositionModel.query(
                "SELECT * FROM Positions P WHERE Restaurant_ID = ?",
                new String[] { Integer.toString(id) }
        );

    }
    // endregion

    // region Employees Modification
    public Observable<NonQueryResponseModel> addEmployee(UserModel user, Role role) {

        // Note: we don't have to check if the employee included doesn't work for the restaurant
        // already because the DB will simply throw an error if they do.
        return NonQueryResponseModel.run(
                "INSERT INTO Positions (Employee_ID, Role_ID, Restaurant_ID, DT_Hired) VALUES (?, ?, ?, NOW())",
                new String[] { Integer.toString(user.getID()), Integer.toString(role.ordinal()), Integer.toString(id) }
        );

    }
    // endregion

    // region Menu Retrieval
    public Observable<List<DishModel>> menu() {

        return DishModel.query(
                "SELECT * FROM Dishes WHERE Restaurant_ID = ? AND DT_Deleted IS NULL",
                new String[] { Integer.toString(id) }
        );
    }
    // endregion

    // region Menu Modification
    public Observable<NonQueryResponseModel> saveMenu(ArrayList<DishModel> dishes) {

        // If there are no dishes to save, then we're done
        if (dishes.isEmpty()) return Observable.just(new NonQueryResponseModel());

        String sql = "INSERT INTO Dishes (Restaurant_ID, Price, Name, Description) VALUES ";
        ArrayList<String> arguments = new ArrayList();
        int n = 0;

        // Note: this is really gross and not efficient.
        for(DishModel d : dishes) {
            if (n++ > 0) sql += ", "; // <--- Disgusting, I know #dontjudgeme #justjava7things T_T
            sql += "(?, ?, ?, ?)";
            arguments.addAll(Arrays.asList(new String[] {
                    Integer.toString(getId()),
                    Integer.toString(d.getPrice()),
                    d.getName(),
                    d.getDescription()
            }));
        }

        return NonQueryResponseModel.run(sql, (String[])arguments.toArray());
    }
    // endregion

    // region Getters and Setters
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

    public Date getDTOpened() {
        return dTOpened;
    }

    public void setDTOpened(Date dTOpened) {
        this.dTOpened = dTOpened;
    }

    public Date getDTClosed() {
        return dTClosed;
    }

    public void setDTClosed(Date dTClosed) {
        this.dTClosed = dTClosed;
    }
    // endregion
}
