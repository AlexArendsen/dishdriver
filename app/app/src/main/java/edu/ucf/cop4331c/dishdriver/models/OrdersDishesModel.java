package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;

/**
 * Created by ashton on pi + .0002.
 */

public class OrdersDishesModel {
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

    public OrdersDishesModel(Integer id, int orderId, int dishId, String notesFromKitchen) {
        this.id = id;
        this.orderId = orderId;
        this.dishId = dishId;
        this.notesFromKitchen = notesFromKitchen;
    }

    public Call reject(String reason) throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call reject() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call voidThisShit() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public static Call<ArrayList<OrdersDishesModel>> between(Date start, Date end) throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public static Call<ArrayList<OrdersDishesModel>> onDay(Date day) throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }

    public Integer getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDishId() {
        return dishId;
    }
    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public boolean isRejected() {
        return isRejected;
    }
    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public boolean isVoided() {
        return isVoided;
    }
    public void setVoided(boolean voided) {
        isVoided = voided;
    }

    public String getNotesFromKitchen() {
        return notesFromKitchen;
    }
    public void setNotesFromKitchen(String notesFromKitchen) {
        this.notesFromKitchen = notesFromKitchen;
    }
}