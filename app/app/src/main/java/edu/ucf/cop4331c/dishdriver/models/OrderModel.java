package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.ucf.cop4331c.dishdriver.enums.Status;
import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;
import edu.ucf.cop4331c.dishdriver.network.NotificationService;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static edu.ucf.cop4331c.dishdriver.enums.Status.*;


/**
 * Created by rebeca on 3/14/2017.
 */

public class OrderModel {

    // region Field Definitions

    @SerializedName("ID")
    @Expose
    private Integer id;

    @SerializedName("Waiter_ID")
    @Expose
    private Integer waiterId;

    @SerializedName("Cook_ID")
    @Expose
    private Integer cookId;

    @SerializedName("Table_ID")
    @Expose
    private Integer tableId;

    @SerializedName("DT_Created")
    @Expose
    private Date dTCreated;

    @SerializedName("DT_Placed")
    @Expose
    private Date dTPlaced;

    @SerializedName("DT_Rejected")
    @Expose
    private Date dTRejected;

    @SerializedName("DT_Accepted")
    @Expose
    private Date dTAccepted;

    @SerializedName("DT_Cancelled")
    @Expose
    private Date dTCancelled;

    @SerializedName("DT_Cooked")
    @Expose
    private Date dTCooked;

    @SerializedName("DT_Payed")
    @Expose
    private Date dTPayed;

    @SerializedName("Discount")
    @Expose
    private Integer discount;

    @SerializedName("Payment")
    @Expose
    private Integer payment;

    @SerializedName("Instructions")
    @Expose
    private String instructions;

    // endregion

    // region query() implementation
    public static Observable<List<OrderModel>> query(String sql, String[] args) {
        return DishDriverProvider.getInstance().queryOrders(
                DishDriverProvider.DD_HEADER_CLIENT,
                new SqlModel(sql, args)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(qm -> {

                    // If no response was sent, just give back an empty list so things don't
                    // explode in UI
                    if (qm == null || qm.getResults() == null) return Observable.just(new ArrayList<OrderModel>());
                    return Observable.just(Arrays.asList(qm.getResults()));

                });
    }
    // endregion

    // Note: no constructors needed for now, the empty constructor should suffice.

    // region DB Retrieval

    public static Observable<OrderModel> get(int id) {
        return query(
            "SELECT * FROM Orders WHERE Id = ?",
            new String[] { Integer.toString(id) }
        ).flatMap(list -> Observable.just((list.isEmpty()) ? null : list.get(0)) );
    }

    public static Observable<List<OrderModel>> forWaiter(PositionModel waiter) {
        return forWaiter(waiter.getID());
    }

    public static Observable<List<OrderModel>> forWaiter(int id) {
        return query(
                "SELECT * FROM Orders WHERE Waiter_ID = ? ORDER BY DT_Created DESC, Id DESC",
                new String[] { Integer.toString(id) }
        );
    }

    public static Observable<List<OrderModel>> forCook(PositionModel cook) {
        return forCook(cook.getID());
    }

    public static Observable<List<OrderModel>> forCook(int id) {
        return query(
                "SELECT * FROM Orders WHERE Cook_ID = ? ORDER BY DT_Created DESC, Id DESC",
                new String[] { Integer.toString(id) }
        );
    }

    public static Observable<List<OrderModel>> forRestaurant(RestaurantModel restaurant) {
        return forRestaurant(restaurant.getId());
    }

    public static Observable<List<OrderModel>> forRestaurant(int id) {
        return query(
                "SELECT O.* FROM Orders O INNER JOIN Tables T ON O.Table_ID = T.Id ORDER BY DT_Created DESC, O.Id DESC",
                new String[] { Integer.toString(id) }
        );
    }
    // endregion

    /**
     * Gets all non-voided dishes related to this order.
     *
     * @return Return all the non-voided dishes for this order
     */
    public Observable<List<OrderedDishModel>> dishes(){
        return OrderedDishModel.odQuery(
                "SELECT * FROM Ordered_Dishes WHERE Order_Id = ? AND IsVoided = 0",
                new String[] {Integer.toString(getId())}
        );
    }

    @Deprecated
    public Observable<Integer> grandTotal() throws Exception {
        throw new Exception("OrderModel.grandTotal is deprecated. Please use dishTotal() instead.");
    }

    /**
     * Calculates the total cost of the dishes ordered for this order. This does **NOT** include
     * the discount, or any form of tax; it only produces the summation of the
     *
     * @return Total cost of the dishes in this order. Discount, tax, etc. NOT included.
     */
    public Observable<Integer> dishTotal(){
        return dishes().flatMap(list -> {

            // Android... why must you tempt me so? This would be SO much nicer...
            //return Observable.just(list.stream().mapToInt(d -> d.getOrderedPrice()).sum());

            // Gotta do this bs instead because APIv19 T_T
            int sum = 0;
            for(OrderedDishModel d : list) sum += d.getOrderedPrice();

            return Observable.just(sum);
        });
    }

    /**
     * Returns the current status of this order
     *
     * @return The current status of this order
     */
    public Status getStatus(){

        if (dTPayed != null)          return PAID;
        else if (dTCancelled != null) return CANCELLED;
        else if (dTCooked != null)    return COOKED;
        else if (dTAccepted != null)  return ACCEPTED;
        else if (dTRejected != null && (dTPlaced == null || dTRejected.after(dTPlaced)))  return REJECTED;
        else if (dTPlaced != null)    return PLACED;
        else return NEW;

        /* Wanted to keep this here bc it's lovely in its own way
        if( dTPayed == null )
            if (dTCooked == null)
                if (dTCancelled == null)
                    if (dTAccepted == null)
                        if (dTRejected == null)
                            if (dTPlaced == null)
                                return NEW;
                            else return PLACED;
                        else return CANCELLED;
                    else return REJECTED;
                else return ACCEPTED;
            else return COOKED;
        else return PAID;
        */
    }

    // region State Transition Methods
    /**
     * Creates an order in the database associated with the given waiter and table. After
     * completion, the ID of this model is updated.
     *
     * @param waiter The waiter to be associated with this order
     * @param table The table to be associated with this order
     * @return A NonQueryResponseModel that represents the progress of the action.
     * @throws IllegalStateException If this order has already been created
     */
    public Observable<NonQueryResponseModel> create(PositionModel waiter, TableModel table) throws IllegalStateException {

        if (dTCreated != null)
            throw new IllegalStateException("This order has already been created.");

        this.waiterId = waiter.getID();
        this.tableId  = table.getId();
        this.dTCreated = new Date();

        return NonQueryResponseModel.run(
            "INSERT INTO Orders (Waiter_ID, Table_ID, DT_Created) VALUES (?, ?, NOW())",
            new String[] {Integer.toString(waiter.getID()), Integer.toString(table.getId())}
        ).doOnNext(qr -> {
            this.id = qr.getResults().getInsertId();
        }).doOnCompleted(() -> {
            NotificationService.broadcast(
                "Order Created",
                "[#" + id + "] " + waiter.getEmployeeName() + " has created an order at table " + table.getName()
            );
        });

    }

    /**
     * Places the order for the kitchen to review, adding the provided dishes.
     *
     * Note: if you want to remove dishes from an order, call the makeVoid() method on the
     * OrderedDishModels you wish to remove.
     *
     * @param newDishes Dishes to be added to this
     * @return The NonQueryResponseModel representing the associated actions
     * @throws IllegalStateException If the order is neither new nor rejected
     */
    public Observable<NonQueryResponseModel> place(List<OrderedDishModel> newDishes) throws IllegalStateException {

        // First, check if the order is in the right state to be placed
        Status s = getStatus();

        // TODO -- Use this to conditionally form the notification message
        boolean isUpdate = s == Status.PLACED;
        if (!Arrays.asList(new Status[]{ NEW, PLACED, REJECTED }).contains(s))
            throw new IllegalStateException("Order must be new or rejected to be placed");

        if (s == REJECTED) dTRejected = null;

        // Then, create all dishes in the Ordered_Dishes table. If there are no dishes to add, then
        // forget about it (this may happen a lot if an order is re-placed after rejection from the
        // kitchen, and no additional dishes need to be added)
        Observable<NonQueryResponseModel> oDishCreate = null;

        if (newDishes != null && !newDishes.isEmpty()) {
            String sql = "INSERT INTO Ordered_Dishes (Order_ID, Dish_ID, IsRejected, IsVoided, OrderedPrice, NotesFromKitchen) VALUES ";
            int i = 0;
            ArrayList<String> args = new ArrayList<>();
            for(OrderedDishModel od : newDishes) {

                // CRAZY GROSS
                if (i++ > 0) sql += ", ";
                sql += "(?, ?, ?, ?, ?, ?)";
                args.addAll(Arrays.asList(
                        Integer.toString(id),
                        Integer.toString(od.getDishId()),
                        "0", "0",
                        Integer.toString(od.getPrice()), // <-- This is where the price gets frozen
                        od.getNotesFromKitchen()
                ));
            }

            // (String[])args.toArray() doesn't work for some reason, so I have to do this bs
            int idx = 0;
            String[] aArgs = new String[args.size()];
            for(String a : args) aArgs[idx++] = a;

            oDishCreate = NonQueryResponseModel.run(sql, aArgs);
        }

        // Also, transition the order record
        dTPlaced = new Date();

        Observable<NonQueryResponseModel> oOrderPlace = NonQueryResponseModel.run(
            "UPDATE Orders SET DT_Placed = NOW() WHERE Id = ?",
            new String[] { Integer.toString(id) }
        ).doOnCompleted(() -> {
            NotificationService.broadcast(
                    "Order Placed",
                    "[#" + id + "] " + SessionModel.currentPosition().getEmployeeName() + " has placed an order"
            );
        });

        // Wait for all Observables to complete
        return (oDishCreate == null)
                ? oOrderPlace
                : Observable.merge(oDishCreate, oOrderPlace) ;
    }

    /**
     * Accepts the placed order
     *
     * @param cook The cook who accepted the order
     * @return A NonQueryResponseModel representing the associated action
     * @throws IllegalStateException If the order is not in the "Placed" state
     */
    public Observable<NonQueryResponseModel> accept(PositionModel cook) throws IllegalStateException {

        if (getStatus() != Status.PLACED)
            throw new IllegalStateException("Order must be placed to be accepted");

        cookId = cook.getID();
        dTAccepted = new Date();

        return NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Accepted = NOW(), Cook_ID = ? WHERE Id = ?",
                new String[] { Integer.toString(cook.getID()),  Integer.toString(id) }
        ).doOnCompleted(() -> {
            NotificationService.broadcast(
                "Order Accepted",
                "[#" + id + "] " + cook.getEmployeeName() + " has accepted order " + getId()
            );
        });
    }

    /**
     * Rejects the placed order
     *
     * @return A NonQueryResponseModel representing the associated action
     * @throws IllegalStateException If the order is not in the "Placed" state
     */
    public Observable<NonQueryResponseModel> reject() throws IllegalStateException {

        if (getStatus() != Status.PLACED)
            throw new IllegalStateException("Order must be placed to be rejected");

        dTRejected = new Date();

        return NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Rejected = NOW() WHERE Id = ?",
                new String[] { Integer.toString(id) }
        ).doOnCompleted(() -> {
            NotificationService.broadcast(
                "Order Rejected",
                "[#" + id + "] Kitchen has rejected order " + id
            );
        });
    }

    /**
     * Cancels this order
     *
     * An order can be cancelled if it is New, Placed, Accepted, Rejected, or Cooked; it cannot be
     * cancelled if it is not yet created, or Paid.
     *
     * @return A NonQueryResponseModel representing the associated action
     * @throws IllegalStateException If the order is new or paid.
     */
    public Observable<NonQueryResponseModel> cancel() throws IllegalStateException {

        Status s = getStatus();
        if (dTCreated == null || s == Status.PAID)
            throw new IllegalStateException("Uncreated or paid orders cannot be cancelled");

        dTCancelled = new Date();

        return NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Cancelled = NOW() WHERE Id = ?",
                new String[] { Integer.toString(id) }
        ).doOnCompleted(() -> {
            NotificationService.broadcast(
                "Order Cancelled",
                "[#" + id + "] " + SessionModel.currentPosition().getEmployeeName() + " has rejected order " + id
            );
        });
    }

    /**
     * Marks this order as cooked
     *
     * @return A NonQueryResponseModel representing the associated action
     * @throws IllegalStateException If the order is not in the accepted state
     */
    public Observable<NonQueryResponseModel> markCooked() throws IllegalStateException {

        if (getStatus() != Status.ACCEPTED)
            throw new IllegalStateException("Only accepted orders can be marked cooked");

        dTCooked = new Date();

        return NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Cooked = NOW() WHERE Id = ?",
                new String[] { Integer.toString(id) }
        ).doOnCompleted(() -> {
            NotificationService.broadcast(
                "Order Cooked",
                "[#" + id + "] Order " + id + " has been cooked and is ready for pick-up."
            );
        });
    }

    /**
     * Marks the order as paid
     *
     * @return A NonQueryResponseModel representing the associated action
     * @throws IllegalStateException If the order is not in the cooked state
     */
    public Observable<NonQueryResponseModel> markPaid() throws IllegalStateException {

        if (getStatus() != Status.COOKED)
            throw new IllegalStateException("Only cooked orders can be marked paid");

        dTPayed = new Date();

        return NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Payed = NOW() WHERE Id = ?",
                new String[] { Integer.toString(id) }
        ).doOnCompleted(() -> {
            NotificationService.broadcast(
                "Order Paid",
                "[#" + id + "] Order " + id + " has been paid!"
            );
        });
    }
    // endregion

    // region Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Integer waiterId) {
        this.waiterId = waiterId;
    }

    public Integer getCookId() {
        return cookId;
    }

    public void setCookId(Integer cookId) {
        this.cookId = cookId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Date getdTCreated() {
        return dTCreated;
    }

    public void setdTCreated(Date dTCreated) {
        this.dTCreated = dTCreated;
    }

    public Date getdTPlaced() {
        return dTPlaced;
    }

    public void setdTPlaced(Date dTPlaced) {
        this.dTPlaced = dTPlaced;
    }

    public Date getdTRejected() {
        return dTRejected;
    }

    public void setdTRejected(Date dTRejected) {
        this.dTRejected = dTRejected;
    }

    public Date getdTAccepted() {
        return dTRejected;
    }

    public void setdTAccepted(Date dTAccepted) {
        this.dTAccepted = dTAccepted;
    }

    public Date getdTCancelled() {
        return dTCancelled;
    }

    public void setdTCancelled(Date dTCancelled) {
        this.dTCancelled = dTCancelled;
    }

    public Date getdTCooked() {
        return dTCooked;
    }

    public void setdTCooked(Date dTCooked) {
        this.dTCooked = dTCooked;
    }

    public Date getdTPayed() {
        return dTPayed;
    }

    public void setdTPayed(Date dTPayed) {
        this.dTPayed = dTPayed;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    // endregion
}
