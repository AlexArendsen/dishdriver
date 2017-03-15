package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by copper on 3/14/17.
 */

public class LoginResponseModel {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("results")
    @Expose
    private List<UserModel> results = null;

    @SerializedName("message")
    @Expose
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserModel> getResults() {
        return results;
    }

    public void setResults(List<UserModel> results) {
        this.results = results;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
