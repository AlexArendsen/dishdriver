package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;

/**
 * Created by copper on 3/14/17.
 */

public class UserModel {
    @SerializedName("ID")
    @Expose
    protected Integer id;
    @SerializedName("Email")
    @Expose
    protected String email;
    @SerializedName("Password")
    @Expose
    protected String password;
    @SerializedName("FirstName")
    @Expose
    protected String firstName;
    @SerializedName("LastName")
    @Expose
    protected String lastName;
    @SerializedName("DT_Created")
    @Expose
    protected String dTCreated;
    @SerializedName("SessionToken")
    @Expose
    protected String sessionToken;
    @SerializedName("DT_LastLogin")
    @Expose
    protected String dTLastLogin;
    @SerializedName("DT_Cancelled")
    @Expose
    protected Object dTCancelled;

    public UserModel(Integer id, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Call<Integer> create() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
    public Call<Boolean> update() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }

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
