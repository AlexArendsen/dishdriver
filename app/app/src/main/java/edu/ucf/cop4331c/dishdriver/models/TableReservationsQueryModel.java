package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class TableReservationsQueryModel {
    private String code;
    private TableReservationsModel[] results;

    public TableReservationsQueryModel(String code,  TableReservationsModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public  TableReservationsModel[] getResults() {
        return results;
    }

    public void setResults( TableReservationsModel[] results) {
        this.results = results;
    }
}
