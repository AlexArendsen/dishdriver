package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class OrderedDishQueryModel {
    private String code;
    private OrderedDishModel[] results;

    public OrderedDishQueryModel(String code, OrderedDishModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OrderedDishModel[] getResults() {
        return results;
    }

    public void setResults(OrderedDishModel[] results) {
        this.results = results;
    }
}
