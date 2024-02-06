package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderHistoryItem implements Serializable {

    @SerializedName("order_date")
    private String orderDate;

    @SerializedName("total")
    private String total;

    @SerializedName("id")
    private String id;

    @SerializedName("status")
    private String status;

    @SerializedName("cust_add")
    private String custAdd;

    @SerializedName("customer_name")
    private String customer_name;

    @SerializedName("customer_mobile")
    private String customer_mobile;

    @SerializedName("p_method_name")
    private String p_method_name;

    @SerializedName("self_pickup")
    private String self_pickup;

    @SerializedName("Order_flow_id")
    private String orderFlowId;

    @SerializedName("current_status")
    private String current_status;

    @SerializedName("order_count")
    private String order_count;

    @SerializedName("is_table")
    private String isTable;

    @SerializedName("table_booking_date")
    private String tableBookingDate;

    @SerializedName("table_booking_time")
    private String tableBookingTime;

    @SerializedName("number_of_guests")
    private String noOfGuest;

    @SerializedName("table_customer_mobile")
    private String tableCustomerMobile;

    public String getIsTable() {
        return isTable;
    }

    public String getTableCustomerMobile() {
        return tableCustomerMobile;
    }

    public String getTableBookingDate() {
        return tableBookingDate;
    }

    public String getTableBookingTime() {
        return tableBookingTime;
    }

    public String getNoOfGuest() {
        return noOfGuest;
    }

    public String getCurrent_status() {
        return current_status;
    }

    public String getOrder_count() {
        return order_count;
    }



    public String getOrderFlowId() {
        return orderFlowId;
    }

    public void setOrderFlowId(String orderFlowId) {
        this.orderFlowId = orderFlowId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDate() {


        return orderDate;
    }


    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getP_method_name() {
        return p_method_name;
    }

    public void setP_method_name(String p_method_name) {
        this.p_method_name = p_method_name;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setCustAdd(String custAdd) {
        this.custAdd = custAdd;
    }

    public String getCustAdd() {
        return custAdd;
    }

    public String getSelf_pickup() {
        return self_pickup;
    }

    public void setSelf_pickup(String self_pickup) {
        this.self_pickup = self_pickup;
    }
}