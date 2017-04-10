package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.enums.TableStatus;
import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import retrofit2.Call;
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
    private Integer tableStatusId = 2;

    // endregion

    // region Constructors
    public TableModel(Integer id, Integer restaurantId, String name, Integer positionX, Integer positionY, Integer capacity) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.capacity = capacity;
    }
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

    /**
     *
     * @param r The restaurant we want information for
     * @return A list containing all the tables for the given restaurant
     */
    public static Observable<List<TableModel>> forRestaurant(RestaurantModel r){

        return query(
          "SELECT T.* FROM Tables T JOIN Restaurants R ON T.Restaurant_ID = R.ID WHERE R.ID =?",
                new String[]{Integer.toString(r.getId())}
        );
    }

    /**
     *
     * @return A table reservatin model, if the table is already reserved throw an exception
     * NOTE: THIS HAS PLACEHOLDER VALUES FOR THE RESERVATION, PULLING THE DATA FROM THE UI STILL NEEDS TO BE IMPLEMENTED
     * @throws UnsupportedOperationException
     */
    public TableReservationModel reserve() throws UnsupportedOperationException{
        Integer reservationId = this.positionX + (this.positionY * 3) + 1;

        // check if a table is already reserved, if so throw an exception
        if(this.tableStatusId == 1) {
            throw new UnsupportedOperationException();
        }

        this.tableStatusId = 1; // mark table as reserved
        return new TableReservationModel(reservationId, this.id, "PartyName", 4, 25, null, null);
    }

    public Observable<NonQueryResponseModel> create(){
        return NonQueryResponseModel.run(
                "INSERT INTO Tables" +
                        "(Restaurant_ID, Name, Capacity)" +
                        "VALUES" +
                        "(?, ?, 4)",
               new String[] { getName(), Integer.toString(SessionModel.currentRestaurant().getId()) }
        );
    }

    public Observable<NonQueryResponseModel> update(){
        return NonQueryResponseModel.run(
                "UPDATE Tables SET" +
                        "Name = ?" +
                        "WHERE Id = ?",
                new String[] { getName(), Integer.toString(getId()) }
        );
    }

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
            case 1:  return TableStatus.RESERVED;
            case 2:  return TableStatus.UNRESERVED;
            default: return TableStatus.OCCUPIED;
        }
    }

    public void setTableStatus(Integer tableStatusId) {
        this.tableStatusId = tableStatusId;
    }
    // endregion
}
