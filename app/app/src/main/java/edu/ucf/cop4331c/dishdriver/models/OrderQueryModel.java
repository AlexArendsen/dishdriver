package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class OrderQueryModel {
    private String code;
    private OrderModel[] results;

    public OrderQueryModel(String code, OrderModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OrderModel[] getResults() {
        return results;
    }

    public void setResults(OrderModel[] results) {
        this.results = results;
    }
}
