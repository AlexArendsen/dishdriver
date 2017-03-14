package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rebeca on 3/14/2017.
 */

public class ReviewsModel {
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Customer_ID")
    @Expose
    private Integer customerID;
    @SerializedName("Restaurant_ID")
    @Expose
    private Integer restaurantID;
    @SerializedName("Waiter_ID")
    @Expose
    private Integer waiterID;
    @SerializedName("Service_Rating")
    @Expose
    private Integer serviceRating;
    @SerializedName("Food_Rating")
    @Expose
    private Integer foodRating;
    @SerializedName("Comments")
    @Expose
    private String comments;

    public ReviewsModel(Integer iD, Integer customerID, Integer restaurantID, Integer waiterID, Integer serviceRating, Integer foodRating, String comments) {
        this.iD = iD;
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.waiterID = waiterID;
        this.serviceRating = serviceRating;
        this.foodRating = foodRating;
        this.comments = comments;
    }

    public static Call<ArrayList<ReviewsModel>> forRestaurant(RestaurantModel r) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call<int> submit() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }

    public Integer getiD() {
        return iD;
    }

    public void setiD(Integer iD) {
        this.iD = iD;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Integer restaurantID) {
        this.restaurantID = restaurantID;
    }

    public Integer getWaiterID() {
        return waiterID;
    }

    public void setWaiterID(Integer waiterID) {
        this.waiterID = waiterID;
    }

    public Integer getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(Integer serviceRating) {
        this.serviceRating = serviceRating;
    }

    public Integer getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(Integer foodRating) {
        this.foodRating = foodRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
