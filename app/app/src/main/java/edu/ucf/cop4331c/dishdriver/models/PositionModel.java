package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.ucf.cop4331c.dishdriver.enums.Role;
import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by rebeca on 3/14/2017.
 */

public class PositionModel {

    // region Field Definitions

    @SerializedName("ID")
    @Expose
    private Integer id;

    @SerializedName("Employee_ID")
    @Expose
    private Integer employeeID;

    @SerializedName("Role_ID")
    @Expose
    private Integer roleID;

    @SerializedName("Restaurant_ID")
    @Expose
    private Integer restaurantID;

    @SerializedName("DT_Hired")
    @Expose
    private Date dTHired;

    @SerializedName("DT_Unhired")
    @Expose
    private Date dTUnhired;

    @SerializedName("Restaurant_Name")
    @Expose
    private String restaurantName;

    @SerializedName("Employee_Name")
    @Expose
    private String employeeName;

    // endregion

    // region query() implementation
    public static Observable<List<PositionModel>> query(String sql, String[] args) {

        return DishDriverProvider.getInstance().queryPositions(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel(sql, args)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {

                    // If no response was sent, just give back an empty list so things don't
                    // explode in UI
                    if (qm == null || qm.getResults() == null) return Observable.just(new ArrayList<PositionModel>());
                    return Observable.just(Arrays.asList(qm.getResults()));

                });
    }
    // endregion

    // region DB Retrieval

    public static Observable<PositionModel> get(int id) {
        return query(
                "SELECT " +
                    "P.*, " +
                    "R.Name AS Restaurant_Name, " +
                    "IF( " +
                        "NULLIF(U.FirstName, '') IS NULL, " +
                        "U.Email, " +
                        "CONCAT(U.FirstName, ' ', U.LastName) " +
                    ") AS Employee_Name " +
                "FROM " +
                    "Positions P " +
                    "INNER JOIN Restaurants R ON P.Restaurant_ID = R.Id " +
                    "INNER JOIN Users U ON P.Employee_ID = U.Id " +
                "WHERE " +
                    "P.Id = ? ",
                new String[] { Integer.toString(id) }
        ).flatMap(list -> Observable.just((list.isEmpty()) ? null : list.get(0) ) );
    }

    /**
     * Fetch all positions currently held by the provided user from the database.
     *
     * @param user The user whose current positions are to be fetched
     * @return The list of positions held by the provided user.
     */
    public static Observable<List<PositionModel>> forUser(UserModel user) {

        // UGGGGGGGGGGGGGGH Java WHY??!?
        // WHY in 20-freaking-17 do you not have MULTILINE. STRING. LITERALS.
        // *freaks out into the sunset*
        // #ThanksOracle

        return query(
            "SELECT " +
                "P.*, " +
                "R.Name AS Restaurant_Name, " +
                "IF( " +
                    "NULLIF(U.FirstName, '') IS NULL, " +
                    "U.Email, " +
                    "CONCAT(U.FirstName, ' ', U.LastName) " +
                ") AS Employee_Name " +
            "FROM " +
                "Positions P " +
                "INNER JOIN Restaurants R ON P.Restaurant_ID = R.Id " +
                "INNER JOIN Users U ON P.Employee_ID = U.Id " +
            "WHERE " +
                "P.Employee_ID = ? " +
                "AND P.DT_Unhired IS NULL",
            new String[] { Integer.toString(user.getID()) }
        );
    }

    /**
     * Fetch all positions currently working at the provided restaurant. Unhired positions are
     * excluded.
     *
     * @return The list of the positions currently working at the provided restaurant.
     */
    public static Observable<List<PositionModel>> forRestaurant(RestaurantModel restaurant) {

        return query(
            "SELECT " +
                "P.*, " +
                "R.Name AS Restaurant_Name, " +
                "IF( " +
                    "NULLIF(U.FirstName, '') IS NULL, " +
                    "U.Email, " +
                    "CONCAT(U.FirstName, ' ', U.LastName) " +
                ") AS Employee_Name " +
            "FROM " +
                "Positions P " +
                "INNER JOIN Restaurants R ON P.Restaurant_ID = R.Id " +
                "INNER JOIN Users U ON P.Employee_ID = U.Id " +
            "WHERE " +
                "P.Restaurant_ID = ? " +
                "AND P.DT_Unhired IS NULL",
            new String[] { Integer.toString(restaurant.getId()) }
        );
    }


    /**
     * Fetch all positions currently working at the provided restaurant that assume the provided
     * role. Unhired positions are excluded.
     *
     * @param restaurant The restaurant whose positions are to be fetched
     * @param role The role
     * @return The list of the positions currently working at the provided restaurant.
     */
    public static Observable<List<PositionModel>> forRestaurant(RestaurantModel restaurant, Role role) {

        // So clunky T_T
        return forRestaurant(restaurant)
                .flatMap(models -> {
                    return Observable.just(
                            Arrays.asList((PositionModel[])models.stream().filter(p -> p.getRole() == role).toArray())
                    );
                });
    }
    // endregion

    // region DB Modification

    /**
     * Alias for PositionMode.create()
     * @param user The user to hire
     * @param restaurant The restaurant to hire this user to
     * @param role The role of this user (waiter, cook, or administrator)
     * @return The NonQueryResponseModel conveying the progress of the associated DB transaction.
     */
    public static Observable<NonQueryResponseModel> hire(UserModel user, RestaurantModel restaurant, Role role) {
        return create(user, restaurant, role);
    }

    /**
     * Hires an existing user to the provided restaurant
     * @param user The user to hire
     * @param restaurant The restaurant to hire this user to
     * @param role The role of this user (waiter, cook, or administrator)
     * @return The NonQueryResponseModel conveying the progress of the associated DB transaction.
     */
    public static Observable<NonQueryResponseModel> create(UserModel user, RestaurantModel restaurant, Role role) {
        return NonQueryResponseModel.run(
                "INSERT INTO Positions (Employee_ID, Role_ID, Restaurant_ID, DT_Hired) VALUES (?, ?, ?, NOW())",
                new String[] {
                        Integer.toString(user.getID()),
                        Integer.toString(role.ordinal() + 1),
                        Integer.toString(restaurant.getId())
        });
    }

    // Skipping this one for now, de-deprecate if UI makes a case for it
    @Deprecated
    public Observable<NonQueryResponseModel> update() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Fires this employee.
     * @return The NonQueryResponseModel conveying the progress of the associated DB transaction.
     */
    public Observable<NonQueryResponseModel> unhire() {
        return NonQueryResponseModel.run(
                "UPDATE Positions SET DT_Unhired = ? WHERE Id = ?",
                new String[] { Integer.toString(id) }
        );
    }
    // endregion

    /**
     * Returns the user's currently selected position
     * @return The user's currently selected position
     */
    public PositionModel current() {
        return SessionModel.currentPosition();
    }

    // region Extended Accessors

    /**
     * A friendly, human-readable name for this position in the format "[Role] at [Restaurant]"
     * @return A string in the format "[Role] at [Restaurant]"
     */
    public String getPositionName() {
        return getRole().toString() + " at " + restaurantName;
    }

    public Role getRole() {
        switch(roleID) {
            case 1:  return Role.Admin;
            case 2:  return Role.Waiter;
            case 3:  return Role.Cook;
            default: return Role.Unknown;
        }
    }
    // endregion

    // region Getters and Setters

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public Integer getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Integer restaurantID) {
        this.restaurantID = restaurantID;
    }

    public Date getDTHired() {
        return dTHired;
    }

    public void setDTHired(Date dTHired) {
        this.dTHired = dTHired;
    }

    public Date getDTUnhired() {
        return dTUnhired;
    }

    public void setDTUnhired(Date dTUnhired) {
        this.dTUnhired = dTUnhired;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    // endregion
}
