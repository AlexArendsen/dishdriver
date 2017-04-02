package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.ucf.cop4331c.dishdriver.enums.Status;
import retrofit2.Call;
import rx.Observable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rebeca on 3/14/2017.
 */

public class OrderModel {
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

    public OrderModel(Integer id, Integer waiterId, Integer cookId, Integer tableId, Date dTCreated, Date dTPlaced, Date dTRejected, Date dTAccepted, Date dTCancelled, Date dTCooked, Date dTPayed, Integer discount, Integer payment, String instructions) {
        this.id = id;
        this.waiterId = waiterId;
        this.cookId = cookId;
        this.tableId = tableId;
        this.dTCreated = dTCreated;
        this.dTPlaced = dTPlaced;
        this.dTRejected = dTRejected;
        this.dTAccepted = dTAccepted;
        this.dTCancelled = dTCancelled;
        this.dTCooked = dTCooked;
        this.dTPayed = dTPayed;
        this.discount = discount;
        this.payment = payment;
        this.instructions = instructions;
    }

    /**
     *
     * @return Return all the dishes for the order
     */
    public Observable<List<OrderedDishModel>> dishes(){
        return OrderedDishModel.query(
                "SELECT * FROM Ordered_Dishes WHERE Order_Id = ?",
                new String[] {Integer.toString(getId())}
        );
    }

    /**
     *
     * @return Returns the total amount of $$$ due for the order
     */
    public Observable<Integer> grandTotal(){
        return OrderedDishModel.query(
            "SELECT SUM(OrderedPrice) FROM Ordered_Dishes " +
            "WHERE Order_Id = ?",
            new String[] {Integer.toString(getId())}
        );
    }

    /**
     *
     * @return
     */
    public Status getStatus(){
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
    }

    /**
     *
     * @param waiter
     * @return Creates an order in the database associated with the given waiter
     */
    public Observable<NonQueryResponseModel> create(PositionModel waiter){

        return NonQueryResponseModel.run(
            "INSERT INTO Orders (Waiter_ID, Table_ID) VALUES (?, ?)",
            new String[] {Integer.toString(waiter.getId()), Integer.toString(getTableId())}
        );
    }

    public Call<OrderModel> place(ArrayList<DishModel> newDishes) throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }

    public void accept(PositionModel cook) {
        NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Accepted = NOW() WHERE Id = ?",
                new String[] { Integer.toString(id) }
        );
    }
    public void reject() {
        NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Rejected = NOW() WHERE Id = ?",
                new String[] { Integer.toString(id) }
        );
    }
    public void cancel() {
        NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Cancelled = NOW() WHERE Id = ?",
                new String[] { Integer.toString(id) }
        );
    }
    public void markCooked() {
        NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Cooked = NOW() WHERE Id = ?",
                new String[] { Integer.toString(id) }
        );
    }

    public void markPaid() {
        NonQueryResponseModel.run(
                "UPDATE Orders SET DT_Payed = NOW() WHERE Id = ?",
                new String[] { Integer.toString(id) }
        );
    }


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
}
