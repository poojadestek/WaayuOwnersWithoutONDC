
package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class DeliveryOptionModel {

    @SerializedName("DPartnerList")
    private List<DeliveryOption> mOrderData;
    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMsg")
    private String mResponseMsg;
    @SerializedName("Result")
    private String mResult;
    @SerializedName("WaitingTime")
    private  String mWaitingTime;

    public List<DeliveryOption> getOrderData() {
        return mOrderData;
    }

    public void setOrderData(List<DeliveryOption> orderData) {
        mOrderData = orderData;
    }

    public String getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(String responseCode) {
        mResponseCode = responseCode;
    }

    public String getResponseMsg() {
        return mResponseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        mResponseMsg = responseMsg;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

    public String getmWaitingTime() {
        return mWaitingTime;
    }

    public void setmWaitingTime(String mWaitingTime) {
        this.mWaitingTime = mWaitingTime;
    }
}
