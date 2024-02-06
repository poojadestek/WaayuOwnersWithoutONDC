package com.waayu.owner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestuarantDatum {
    @SerializedName("rest_rating")
    @Expose
    private String restRating;
    @SerializedName("rest_comment")
    @Expose
    private String restComment;
    @SerializedName("rider_rating")
    @Expose
    private String riderRating;
    @SerializedName("rider_comment")
    @Expose
    private String riderComment;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("rider_name")
    @Expose
    private String riderName;
    @SerializedName("self_pickup")
    @Expose
    private String selfPickup;

    public String getRestRating() {
        return restRating;
    }

    public void setRestRating(String restRating) {
        this.restRating = restRating;
    }

    public String getRestComment() {
        return restComment;
    }

    public void setRestComment(String restComment) {
        this.restComment = restComment;
    }

    public String getRiderRating() {
        return riderRating;
    }

    public void setRiderRating(String riderRating) {
        this.riderRating = riderRating;
    }

    public String getRiderComment() {
        return riderComment;
    }

    public void setRiderComment(String riderComment) {
        this.riderComment = riderComment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getSelfPickup() {
        return selfPickup;
    }

    public void setSelfPickup(String selfPickup) {
        this.selfPickup = selfPickup;
    }
}
