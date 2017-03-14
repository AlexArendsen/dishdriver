package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class OrdersQueryModel {
    private String code;
    private OrdersModel[] results;

    public OrdersQueryModel(String code, OrdersModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OrdersModel[] getResults() {
        return results;
    }

    public void setResults(OrdersModel[] results) {
        this.results = results;
    }
}
