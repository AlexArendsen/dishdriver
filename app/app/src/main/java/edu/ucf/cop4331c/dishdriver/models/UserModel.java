package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by copper on 3/14/17.
 */

public class UserModel {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("DT_Created")
    @Expose
    private String dTCreated;
    @SerializedName("SessionToken")
    @Expose
    private String sessionToken;
    @SerializedName("DT_LastLogin")
    @Expose
    private String dTLastLogin;
    @SerializedName("DT_Cancelled")
    @Expose
    private Object dTCancelled;

    public Integer getID() {
        return id;
    }

    public void setID(Integer iD) {
        this.id = iD;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDTCreated() {
        return dTCreated;
    }

    public void setDTCreated(String dTCreated) {
        this.dTCreated = dTCreated;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getDTLastLogin() {
        return dTLastLogin;
    }

    public void setDTLastLogin(String dTLastLogin) {
        this.dTLastLogin = dTLastLogin;
    }

    public Object getDTCancelled() {
        return dTCancelled;
    }

    public void setDTCancelled(Object dTCancelled) {
        this.dTCancelled = dTCancelled;
    }
}
