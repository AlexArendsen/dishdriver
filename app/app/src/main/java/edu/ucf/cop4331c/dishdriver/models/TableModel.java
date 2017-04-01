package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private Integer capacity;
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

    public static Call<ArrayList<TableModel>> forRestaurant(RestaurantModel r) throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<TableReservationModel> reserve() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<Boolean> unreserved() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<Boolean> create() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<Boolean> update() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }

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
    // endregion
}
