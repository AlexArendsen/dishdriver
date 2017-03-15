package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;

/**
 * Created by rebeca on 3/14/2017.
 */

public class DishesModel {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Restaurant_ID")
    @Expose
    private Integer restaurantId;
    @SerializedName("Price")
    @Expose
    private Integer price;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("DT_Deleted")
    @Expose
    private Date dTDeleted;

    public DishesModel(Integer id, Integer restaurantId, Integer price, String name, String description, Date dTDeleted) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.price = price;
        this.name = name;
        this.description = description;
        this.dTDeleted = dTDeleted;
    }

    public static Call<ArrayList<DishesModel>> forRestaurant(RestaurantModel r) throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public static Call<ArrayList<DishesModel>> search(RestaurantModel r, String query) throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<Integer> create() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public void update() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public void delete() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getdTDeleted() {
        return dTDeleted;
    }

    public void setdTDeleted(Date dTDeleted) {
        this.dTDeleted = dTDeleted;
    }
}
