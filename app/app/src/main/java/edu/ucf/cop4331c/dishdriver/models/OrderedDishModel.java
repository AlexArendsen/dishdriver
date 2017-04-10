package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.helpers.DateFormatter;
import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by ashton on pi + .0002.
 */

public class OrderedDishModel extends DishModel {

    // region Field Definitions
    @SerializedName("OrderedDish_ID")
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
    private int isRejected = 0;

    @SerializedName("IsVoided")
    @Expose
    private int isVoided = 0;

    @SerializedName("OrderedPrice")
    @Expose
    private int orderedPrice;

    @SerializedName("NotesFromKitchen")
    @Expose
    private String notesFromKitchen;

    @SerializedName("NotesToKitchen")
    @Expose
    private String notesToKitchen;

    // endregion

    // region Constructors

    @Deprecated
    public OrderedDishModel(Integer id, int orderId, int dishId, String notesFromKitchen) {
        this.id = id;
        this.orderId = orderId;
        this.dishId = dishId;
        this.notesFromKitchen = notesFromKitchen;
    }

    public OrderedDishModel(DishModel dish, OrderModel order) {
        this.id = -1;
        this.dishId = dish.getID();
        this.orderId = order.getId();
        this.notesFromKitchen = "";
        this.notesToKitchen = "";
        this.setName(dish.getName());
        this.setPrice(dish.getPrice());
        this.setRestaurantID(dish.getRestaurantID());
        this.setOrderedPrice(this.getPrice());
    }

    public OrderedDishModel(DishModel dish, OrderModel order, String notesToKitchen) {
        this.id = -1;
        this.dishId = dish.getID();
        this.orderId = order.getId();
        this.notesFromKitchen = "";
        this.notesToKitchen = notesToKitchen;
        this.setName(dish.getName());
        this.setPrice(dish.getPrice());
        this.setRestaurantID(dish.getRestaurantID());
        this.setOrderedPrice(this.getPrice());
    }
    // endregion

    // region query() implementation
    public static Observable<List<OrderedDishModel>> odQuery(String sql, String[] args) {

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

                });
    }
    // endregion

    // region DB Retrieval
    public static Observable<OrderedDishModel> getOrderedDish(int id) {
        return odQuery(
            "SELECT D.*, OD.*, OD.Id AS OrderedDish_ID " +
            "FROM Ordered_Dishes OD INNER JOIN Orders O ON OD.Order_ID = O.Id INNER JOIN Dishes D ON OD.Dish_ID = D.Id " +
            "WHERE OD.Id = ?",
            new String[] { Integer.toString(id) }
        ).flatMap(list -> Observable.just((list.isEmpty()) ? null : list.get(0) ));
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
        isRejected = 1;

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

        isVoided = 1;

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

        return odQuery(
                "SELECT D.*, OD.*, OD.Id AS OrderedDish_ID " +
                "FROM Ordered_Dishes OD INNER JOIN Orders O ON OD.Order_ID = O.Id INNER JOIN Dishes D ON OD.Dish_ID = D.Id " +
                "WHERE O.DT_Placed BETWEEN ? AND ? AND OD.IsVoided != 1 AND D.Restaurant_ID = ?",
                new String[] {
                        DateFormatter.forSQL(start),
                        DateFormatter.forSQL(end),
                        Integer.toString(r.getId())
                }
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
        return isRejected == 1;
    }

    public boolean isVoided() {
        return isVoided == 1;
    }

    public String getNotesFromKitchen() {
        return notesFromKitchen;
    }

    public String getNotesToKitchen() {
        return notesToKitchen;
    }

    public void setToFromKitchen(String notesToKitchen) {
        this.notesToKitchen = notesFromKitchen;
    }

    public int getOrderedPrice() { return orderedPrice; }

    public void setOrderedPrice(int orderedPrice) { this.orderedPrice = orderedPrice; }

    // endregion
}