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
    private Integer employeeId;

    @SerializedName("Role_ID")
    @Expose
    private Integer roleId;

    @SerializedName("Restaurant_ID")
    @Expose
    private Integer restaurantId;

    @SerializedName("DT_Hired")
    @Expose
    private Date dTHired;

    @SerializedName("DT_Unhired")
    @Expose
    private Date dTUnhired;

    @SerializedName("Restaurant_Name")
    @Expose
    private String restaurantName;
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
    /**
     * Fetch all positions currently held by the provided user from the database.
     *
     * @param user The user whose current positions are to be fetched
     * @return The list of positions held by the provided user.
     */
    public Observable<List<PositionModel>> forUser(UserModel user) {
        return query(
                "SELECT " +
                    "P.*, " +
                    "R.Name AS Restaurant_Name" +
                "FROM " +
                    "Positions P " +
                    "INNER JOIN Restaurants R ON P.Restaurant_ID = R.Id " +
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
     * @param restaurant The restaurant whose positions are to be fetched
     * @return The list of the positions currently working at the provided restaurant.
     */
    public Observable<List<PositionModel>> forRestaurant(RestaurantModel restaurant) {
        return query(
                "SELECT * FROM Positions WHERE Restaurant_ID = ? AND DT_Unhired IS NULL",
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
    public Observable<List<PositionModel>> forRestaurant(RestaurantModel restaurant, Role role) {

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
                        Integer.toString(restaurant.getId()),
                        Integer.toString(role.ordinal() + 1)
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
    public String getName() {
        return getRole().toString() + " at " + restaurantName;
    }

    public Role getRole() {
        switch(roleId) {
            case 1:  return Role.Admin;
            case 2:  return Role.Waiter;
            case 3:  return Role.Cook;
            default: return Role.Unknown;
        }
    }
    // endregion

    // region Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Date getdTHired() {
        return dTHired;
    }

    public void setdTHired(Date dTHired) {
        this.dTHired = dTHired;
    }

    public Date getdTUnhired() {
        return dTUnhired;
    }

    public void setdTUnhired(Date dTUnhired) {
        this.dTUnhired = dTUnhired;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
    // endregion
}
