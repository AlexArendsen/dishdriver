package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.enums.TableStatus;
import edu.ucf.cop4331c.dishdriver.helpers.DateFormatter;
import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by rebeca on 3/14/2017.
 */

public class TableModel {

    // region Field Definitions
    @SerializedName("ID")
    @Expose
    private Integer id;

    @SerializedName("Restaurant_ID")
    @Expose
    private Integer restaurantId;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Position_X")
    @Expose
    private Integer positionX;

    @SerializedName("Position_Y")
    @Expose
    private Integer positionY;

    @SerializedName("Capacity")
    @Expose
    private Integer capacity = 4;

    @SerializedName("TableStatus_ID")
    @Expose
    private Integer tableStatusId;

    // endregion

    // region query() implementation
    public static Observable<List<TableModel>> query(String sql, String[] args) {

        return DishDriverProvider.getInstance().queryTables(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel(sql, args)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {

                    // If no response was sent, just give back an empty list so things don't
                    // explode in UI
                    if (qm == null || qm.getResults() == null) return Observable.just(new ArrayList<TableModel>());
                    return Observable.just(Arrays.asList(qm.getResults()));

                });
    }
    // endregion

    // region DB Retrieval

    public static Observable<TableModel> get(int id) {
        return query(
          "SELECT * FROM Tables WHERE Id = ?",
           new String[]{ Integer.toString(id) }
        ).flatMap(list -> Observable.just((list.isEmpty()) ? null : list.get(0) ));
    }

    /**
     * A list containing all the tables for the given restaurant
     *
     * @param restaurant The restaurant we want information for
     * @return The list of tables
     */
    public static Observable<List<TableModel>> forRestaurant(RestaurantModel restaurant){

        return query(
          "SELECT T.* FROM Tables T JOIN Restaurants R ON T.Restaurant_ID = R.ID WHERE R.ID =?",
                new String[]{Integer.toString(restaurant.getId())}
        );
    }
    // endregion

    // region DB Modification
    /**
     * Creates a reservation for this table with the provided options.
     *
     * @param partyName The name of the party associated with this reservation
     * @param partySize The size of the party associated with this reservation
     * @param deposit The money amount, in cents, of the deposit for the associated reservation
     * @param reserved The date and time that is requested for this table reservation
     * @return A NonQueryResponseModel that conveys the progress of the associated transaction
     */
    public Observable<NonQueryResponseModel> reserve(String partyName, int partySize, int deposit, Date reserved) {

        return NonQueryResponseModel.run(
                "INSERT INTO Table_Reservations " +
                    "(Table_ID, Party_Name, Party_Size, Deposit, DT_Requested) " +
                "VALUES (?, ?, ?, ?, ?)",
                new String[] {
                        Integer.toString(id),
                        partyName,
                        Integer.toString(partySize),
                        Integer.toString(deposit),
                        DateFormatter.forSQL(reserved)
                }
        );

    }

    public Observable<NonQueryResponseModel> create(){
        return NonQueryResponseModel.run(
                "INSERT INTO Tables " +
                "(Restaurant_ID, Name, Capacity) " +
                "VALUES " +
                "(?, ?, 4)",
               new String[] { getName(), Integer.toString(SessionModel.currentRestaurant().getId()) }
        );
    }

    public Observable<NonQueryResponseModel> update(){
        return NonQueryResponseModel.run(
                "UPDATE Tables SET " +
                "Name = ? " +
                "WHERE Id = ?",
                new String[] { getName(), Integer.toString(getId()) }
        );
    }
    // endregion

    // region Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TableStatus getTableStatus() {

        if(tableStatusId == null) {
            return TableStatus.UNRESERVED;
        }
        switch(tableStatusId) {
            case 0:  return TableStatus.RESERVED;
            case 1:  return TableStatus.UNRESERVED;
            case 2: return TableStatus.OCCUPIED;
            default: return TableStatus.UNRESERVED;
        }
    }

    public void setTableStatus(Integer tableStatusId) {
        this.tableStatusId = tableStatusId;
    }
    // endregion
}
