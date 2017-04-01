package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by copper on 3/14/17.
 */

public class UserQueryModel {
    private String code;
    private UserModel[] results;

    public UserQueryModel(String code, UserModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserModel[] getResults() {
        return results;
    }

    public void setResults(UserModel[] results) {
        this.results = results;
    }
}
