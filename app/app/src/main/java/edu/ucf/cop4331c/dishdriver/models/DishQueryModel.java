package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class DishQueryModel {
    private String code;
    private DishModel[] results;

    public DishQueryModel(String code, DishModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DishModel[] getResults() {
        return results;
    }

    public void setResults(DishModel[] results) {
        this.results = results;
    }
}
