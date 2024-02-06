package com.waayu.owner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerList {
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ResponseMsg")
    @Expose
    private String responseMsg;
    @SerializedName("total_user")
    @Expose
    private String total_user;
    @SerializedName("user_data")
    @Expose
    private List<CustomerData> userData;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public List<CustomerData> getUserData() {
        return userData;
    }

    public void setUserData(List<CustomerData> userData) {
        this.userData = userData;
    }

    public String getTotal_user() {
        return total_user;
    }

    public void setTotal_user(String total_user) {
        this.total_user = total_user;
    }
}
