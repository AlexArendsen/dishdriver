package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ashton on pi + .00002.
 */

public class OrdersDishesModel {
    @SerializedName("ID")
    @Expose
    private Integer id;

    private boolean isRejected = false;
    private boolean isVoided = true;
    private String notesFromKitchen;


    public OrdersDishesModel(Integer id, String notesFromKitchen) {
        this.id = id;
        this.isVoided = false;
        this.notesFromKitchen = notesFromKitchen;
    }

    public Call reject(String reason);
    public  Call reject();
    Call void();
    static Call<ArrayList<OrderedDish>> between(Date start, Date end);
    static Call<ArrayList<OrderedDish>> onDay(Date day);



    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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