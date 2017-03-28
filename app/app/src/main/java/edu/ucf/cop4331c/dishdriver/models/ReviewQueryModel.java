package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class ReviewQueryModel {
    private String code;
    private ReviewModel[] results;

    public ReviewQueryModel(String code, ReviewModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ReviewModel[] getResults() {
        return results;
    }

    public void setResults(ReviewModel[] results) {
        this.results = results;
    }
}
