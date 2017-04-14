package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class TableReservationQueryModel {
    private String code;
    private TableReservationModel[] results;

    public TableReservationQueryModel(String code, TableReservationModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TableReservationModel[] getResults() {
        return results;
    }

    public void setResults(TableReservationModel[] results) {
        this.results = results;
    }
}
