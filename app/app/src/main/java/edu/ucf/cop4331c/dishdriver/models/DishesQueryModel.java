package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class DishesQueryModel {
    private String code;
    private DishesModel[] results;

    public DishesQueryModel(String code, DishesModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DishesModel[] getResults() {
        return results;
    }

    public void setResults(DishesModel[] results) {
        this.results = results;
    }
}
