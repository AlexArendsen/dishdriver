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

public class ReviewModel {

    // region Field Definitions
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
    // endregion

    // region Constructors
    public ReviewModel(Integer iD, Integer customerID, Integer restaurantID, Integer waiterID, Integer serviceRating, Integer foodRating, String comments) {
        this.iD = iD;
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.waiterID = waiterID;
        this.serviceRating = serviceRating;
        this.foodRating = foodRating;
        this.comments = comments;
    }
    // endregion

    // region query() implementation
    public static Observable<List<ReviewModel>> query(String sql, String[] args) {

        return DishDriverProvider.getInstance().queryReviews(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel(sql, args)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {

                    // If no response was sent, just give back an empty list so things don't
                    // explode in UI
                    if (qm == null || qm.getResults() == null)
                        return Observable.just(new ArrayList<ReviewModel>());
                    return Observable.just(Arrays.asList(qm.getResults()));

                });
    }
    // endregion

    public static Call<ArrayList<ReviewModel>> forRestaurant(RestaurantModel r) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Call<Integer> submit() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    // region Getters and Setters
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
    // endregion
}
