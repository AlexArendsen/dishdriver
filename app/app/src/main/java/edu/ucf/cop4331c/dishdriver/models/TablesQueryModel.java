package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class TablesQueryModel {
    private String code;
    private Tables[] results;

    public TablesQueryModel(String code,  Tables[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public  Tables[] getResults() {
        return results;
    }

    public void setResults( Tables[] results) {
        this.results = results;
    }
}
