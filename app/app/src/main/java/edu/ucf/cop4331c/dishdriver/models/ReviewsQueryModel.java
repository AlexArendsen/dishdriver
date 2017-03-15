package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by rebeca on 3/14/2017.
 */

public class ReviewsQueryModel {
    private String code;
    private ReviewsModel[] results;

    public ReviewsQueryModel(String code, ReviewsModel[] results) {
        this.code = code;
        this.results = results;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ReviewsModel[] getResults() {
        return results;
    }

    public void setResults(ReviewsModel[] results) {
        this.results = results;
    }
}
