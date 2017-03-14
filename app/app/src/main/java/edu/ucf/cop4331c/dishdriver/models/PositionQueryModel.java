package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class PositionQueryModel {
    private String code;
    private PositionModel[] results;

    public PositionQueryModel(String code, PositionModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PositionModel[] getResults() {
        return results;
    }

    public void setResults(PositionModel[] results) {
        this.results = results;
    }
}
