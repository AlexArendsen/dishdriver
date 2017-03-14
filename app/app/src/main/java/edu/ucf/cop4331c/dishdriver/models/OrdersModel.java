package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.ucf.cop4331c.dishdriver.enums.Status;

import java.util.Date;

/**
 * Created by rebeca on 3/14/2017.
 */

public class OrdersModel {
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

    public OrdersModel(Integer id, Integer waiterId, Integer cookId, Integer tableId, Date dTCreated, Date dTPlaced, Date dTRejected, Date dTCancelled, Date dTCooked, Date dTPayed, Integer discount, Integer payment, String instructions) {
        this.id = id;
        this.waiterId = waiterId;
        this.cookId = cookId;
        this.tableId = tableId;
        this.dTCreated = dTCreated;
        this.dTPlaced = dTPlaced;
        this.dTRejected = dTRejected;
        this.dTCancelled = dTCancelled;
        this.dTCooked = dTCooked;
        this.dTPayed = dTPayed;
        this.discount = discount;
        this.payment = payment;
        this.instructions = instructions;
    }

    public Call<ArrayList<OrderedDish>> dishes() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call<int> grandTotal() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Status getStatus() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call<Order> create(Position waiter) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call<Order> place(ArrayList<Dish> newDishes) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call accept(Position cook) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call reject() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call cancel() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call markCooked() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call markPaid() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }


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
