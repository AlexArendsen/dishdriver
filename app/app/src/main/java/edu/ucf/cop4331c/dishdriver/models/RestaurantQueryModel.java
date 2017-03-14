package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class RestaurantQueryModel {
    private String code;
    private RestaurantModel[] results;

    public RestaurantQueryModel(String code, RestaurantModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RestaurantModel[] getResults() {
        return results;
    }

    public void setResults(RestaurantModel[] results) {
        this.results = results;
    }
}
