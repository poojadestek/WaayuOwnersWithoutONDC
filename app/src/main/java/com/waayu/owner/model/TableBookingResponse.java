package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

public class TableBookingResponse {
    @SerializedName("ResponseCode")
    String ResponseCode;
    @SerializedName("Result")
    String Result;
    @SerializedName("ResponseMsg")
    String ResponseMsg;

    @SerializedName("status")
    String status;

    public String getResponseCode() {
        return ResponseCode;
    }

    public String getResult() {
        return Result;
    }

    public String getResponseMsg() {
        return ResponseMsg;
    }

    public String getStatus() {
        return status;
    }
}
