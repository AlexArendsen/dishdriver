package edu.ucf.cop4331c.dishdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.network.DishDriverProvider;

/**
 * Created by copper on 4/1/17.
 */

public class PostNotificationModel {

    // region Field Definitions
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("included_segments")
    @Expose
    private List<String> includedSegments = null;
    @SerializedName("contents")
    @Expose
    private Contents contents;
    @SerializedName("headings")
    @Expose
    private Headings headings;
    // endregion

    // region Constructors
    public PostNotificationModel(String title, String message) {
        appId = DishDriverProvider.APP_ID;
        includedSegments = new ArrayList<>();
        includedSegments.add("All");

        contents = new Contents();
        headings = new Headings();

        contents.setEn(message);
        headings.setEn(title);
    }
    // endregion

    // region Getters and Setters
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<String> getIncludedSegments() {
        return includedSegments;
    }

    public void setIncludedSegments(List<String> includedSegments) {
        this.includedSegments = includedSegments;
    }

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    public Headings getHeadings() {
        return headings;
    }

    public void setHeadings(Headings headings) {
        this.headings = headings;
    }
    // endregion

    // region Private Classes
    public class Contents {

        @SerializedName("en")
        @Expose
        private String en;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

    }

    public class Headings {

        @SerializedName("en")
        @Expose
        private String en;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

    }

    // endregion

}

