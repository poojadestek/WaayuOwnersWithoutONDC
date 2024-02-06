package com.waayu.owner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("order_placed")
    @Expose
    private String order_placed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerData(String name, String mobile, String email, String order_placed) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.order_placed=order_placed;
    }

    public String getOrder_placed() {
        return order_placed;
    }

    public void setOrder_placed(String order_placed) {
        this.order_placed = order_placed;
    }
}
