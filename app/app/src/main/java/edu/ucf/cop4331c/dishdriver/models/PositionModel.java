package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.ucf.cop4331c.dishdriver.enums.Roles;

import java.util.Date;

/**
 * Created by rebeca on 3/14/2017.
 */

public class PositionModel {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Employee_ID")
    @Expose
    private Integer employeeId;
    @SerializedName("Role_ID")
    @Expose
    private Integer roleId;
    @SerializedName("Restaurant_ID")
    @Expose
    private Integer restaurantId;
    @SerializedName("DT_Hired")
    @Expose
    private Date dTHired;
    @SerializedName("DT_Unhired")
    @Expose
    private Date dTUnhired;

    public PositionModel(Integer id, Integer employeeId, Integer roleId, Integer restaurantId, Date dTHired) {
        this.id = id;
        this.employeeId = employeeId;
        this.roleId = roleId;
        this.restaurantId = restaurantId;
        this.dTHired = dTHired;
    }

    public Call<ArrayList<PositionModel>> forUser(UserModel user) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call<ArrayList<PositionModel>> forRestaurant(RestaurantModel restaurant) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call<ArrayList<PositionModel>> forRestaurant(RestaurantModel restaurant, Roles roles) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call<int> create(RegistrantModel registrant, RestaurantModel restaurant, Roles roles) throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call update() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }
    public Call unhire() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }

    public PositionModel current() throws UnsupportedOperationException{ throw new UnsupportedOperationException; }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Date getdTHired() {
        return dTHired;
    }

    public void setdTHired(Date dTHired) {
        this.dTHired = dTHired;
    }

    public Date getdTUnhired() {
        return dTUnhired;
    }

    public void setdTUnhired(Date dTUnhired) {
        this.dTUnhired = dTUnhired;
    }
}
