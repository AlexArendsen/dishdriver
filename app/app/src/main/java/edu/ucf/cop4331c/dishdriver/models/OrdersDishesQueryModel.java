package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class OrdersDishesQueryModel {
    private String code;
    private OrdersDishesModel[] results;

    public OrdersDishesQueryModel(String code, OrdersDishesModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OrdersDishesModel[] getResults() {
        return results;
    }

    public void setResults(OrdersDishesModel[] results) {
        this.results = results;
    }
}
