package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by rebeca on 3/14/2017.
 */

public class RestaurantModel {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("DT_Opened")
    @Expose
    private Date dTOpened;
    @SerializedName("DT_Closed")
    @Expose
    private Date dTClosed;

    public RestaurantModel(Integer id, String name, Date dTOpened, Date dTClosed) {
        this.id = id;
        this.name = name;
        this.dTOpened = dTOpened;
        this.dTClosed = dTClosed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getdTOpened() {
        return dTOpened;
    }

    public void setdTOpened(Date dTOpened) {
        this.dTOpened = dTOpened;
    }

    public Date getdTClosed() {
        return dTClosed;
    }

    public void setdTClosed(Date dTClosed) {
        this.dTClosed = dTClosed;
    }
}
