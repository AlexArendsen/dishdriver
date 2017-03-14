package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class TablesQueryModel {
    private String code;
    private TablesModel[] results;

    public TablesQueryModel(String code,  TablesModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public  TablesModel[] getResults() {
        return results;
    }

    public void setResults( TablesModel[] results) {
        this.results = results;
    }
}
