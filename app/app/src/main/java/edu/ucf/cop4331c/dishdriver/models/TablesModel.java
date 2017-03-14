package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rebeca on 3/14/2017.
 */

public class TablesModel {
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

    public TablesModel(Integer id, Integer restaurantId, String name, Integer positionX, Integer positionY, Integer capacity) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.capacity = capacity;
    }

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
}
