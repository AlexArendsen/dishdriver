package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ashton on pi + .0002.
 */

public class OrderedDishModel {

    // region Field Definitions
    @SerializedName("ID")
    @Expose
    private Integer id;

    @SerializedName("Order_ID")
    @Expose
    private int orderId;

    @SerializedName("Dish_ID")
    @Expose
    private int dishId;

    @SerializedName("IsRejected")
    @Expose
    private boolean isRejected = false;

    @SerializedName("IsVoided")
    @Expose
    private boolean isVoided = false;

    @SerializedName("NotesFromKitchen")
    @Expose
    private String notesFromKitchen;
    // endregion

    // region Constructors
    public OrderedDishModel(Integer id, int orderId, int dishId, String notesFromKitchen) {
        this.id = id;
        this.orderId = orderId;
        this.dishId = dishId;
        this.notesFromKitchen = notesFromKitchen;
    }
    // endregion

    // region query() implementation
    public static Observable<List<OrderedDishModel>> query(String sql, String[] args) {

        return DishDriverProvider.getInstance().queryOrderedDishes(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel(sql, args)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {

                    // If no response was sent, just give back an empty list so things don't
                    // explode in UI
                    if (qm == null || qm.getResults() == null) return Observable.just(new ArrayList<OrderedDishModel>());
                    return Observable.just(Arrays.asList(qm.getResults()));

                }).observeOn(AndroidSchedulers.mainThread());
    }
    // endregion

    // region DB Modification

    /**
     * Reject this dish from its associated order, citing the provided reason
     * @param reason The reason that this dish was rejected from its order
     * @return A NonQueryResponseModel conveying the status of the associated transaction
     */
    public Observable<NonQueryResponseModel> reject(String reason) {

        notesFromKitchen = reason;
        isRejected = true;

        return NonQueryResponseModel.run(
                "UPDATE Ordered_Dishes SET IsRejected = 1, NotesFromKitchen = ? WHERE Id = ?",
                new String[] { Integer.toString(id), notesFromKitchen }
        );
    }

    /**
     * Reject this dish from its associated order, without a reason
     * @return A NonQueryResponseModel conveying the status of the associated transaction
     */
    public Observable<NonQueryResponseModel> reject() {
        return reject("(No notes)");
    }

    /**
     * Remove this dish from its associated order
     * @return A NonQueryResponseModel conveying the status of the associated transaction
     */
    public Observable<NonQueryResponseModel> makeVoid() {

        isVoided = true;

        return NonQueryResponseModel.run(
                "UPDATE Ordered_Dishes SET IsVoided = 1 WHERE Id = ?",
                new String[] { Integer.toString(id) }
        );
    }
    // endregion

    // region DB Retrieval

    /**
     * Gets all non-voided dishes from all orders placed at the given restaurant between the two
     * given times, inclusively.
     *
     * @param r The subject restaurant
     * @param start The beginning of the selection interval
     * @param end The end of the selection interval
     * @return The list of dishes
     */
    public static Observable<List<OrderedDishModel>> between(RestaurantModel r, Date start, Date end) {

        return query(
                "SELECT" +
                    "OD.*" +
                "FROM" +
                    "Ordered_Dishes OD" +
                    "JOIN Orders O ON OD.Order_ID = O.Id" +
                "WHERE" +
                    "O.DT_Placed BETWEEN ? AND ?" +
                    "AND OD.IsVoided != 1",
                new String[] { start.toString(), end.toString() }
        );
    }

    /**
     * Gets all non-voided dishes from all orders placed on the given day.
     * @param r The subject restaurant
     * @param day Any moment within the desired day
     * @return The list of dishes
     */
    public static Observable<List<OrderedDishModel>> onDay(RestaurantModel r, Date day) {

        // Number of milliseconds in one day
        long dayLength = (long)8.64e7;

        // Round down to the nearest 12:00 AM
        long dayBegin  = day.getTime() - (day.getTime() % dayLength);

        return between(r, new Date(dayBegin), new Date(dayBegin + dayLength));
    }
    // endregion

    // region Getters and Setters
    public Integer getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getDishId() {
        return dishId;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public boolean isVoided() {
        return isVoided;
    }

    public String getNotesFromKitchen() {
        return notesFromKitchen;
    }
    // endregion
}