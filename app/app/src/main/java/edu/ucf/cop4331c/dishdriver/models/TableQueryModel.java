package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class TableQueryModel {
    private String code;
    private TableModel[] results;

    public TableQueryModel(String code, TableModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public  TableModel[] getResults() {
        return results;
    }

    public void setResults( TableModel[] results) {
        this.results = results;
    }
}
