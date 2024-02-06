package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookaTableModel {
    @SerializedName("ResponseCode")
    private String ResponseCode;
    @SerializedName("ResponseMsg")
    private String ResponseMsg;
    @SerializedName("OrderHistory")
    private List<OrderHistory> OrderHistory;
    @SerializedName("Result")
    private String Result;

    public String getResponseCode ()
    {
        return ResponseCode;
    }

    public void setResponseCode (String ResponseCode)
    {
        this.ResponseCode = ResponseCode;
    }

    public String getResponseMsg ()
    {
        return ResponseMsg;
    }

    public void setResponseMsg (String ResponseMsg)
    {
        this.ResponseMsg = ResponseMsg;
    }

    public List<OrderHistory> getOrderHistory ()
    {
        return OrderHistory;
    }

    public void setOrderHistory (List<OrderHistory> OrderHistory)
    {
        this.OrderHistory = OrderHistory;
    }

    public String getResult ()
    {
        return Result;
    }

    public void setResult (String Result)
    {
        this.Result = Result;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ResponseCode = "+ResponseCode+", ResponseMsg = "+ResponseMsg+", OrderHistory = "+OrderHistory+", Result = "+Result+"]";
    }

    public class OrderHistory
    {
        @SerializedName("customer_mobile")
        private String customer_mobile;
        @SerializedName("Order_flow_id")
        private String Order_flow_id;
        @SerializedName("table_booking_date")
        private String table_booking_date;
        @SerializedName("name")
        private String name;

        @SerializedName("cancel_by")
        private String cancel_by;

        @SerializedName("cancel_dueto")
        private String cancel_dueto;

        @SerializedName("cancel_reason")
        private String cancel_reason;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("id")
        private String id;
        @SerializedName("table_booking_time")
        private String table_booking_time;
        @SerializedName("status")
        private String status;
        @SerializedName("number_of_guests")
        private String number_of_guests;

        public String getCustomer_mobile ()
        {
            return customer_mobile;
        }

        public void setCustomer_mobile (String customer_mobile)
        {
            this.customer_mobile = customer_mobile;
        }

        public String getCancel_reason() {
            return cancel_reason;
        }

        public void setCancel_reason(String cancel_reason) {
            this.cancel_reason = cancel_reason;
        }

        public String getCancel_by() {
            return cancel_by;
        }

        public void setCancel_by(String cancel_by) {
            this.cancel_by = cancel_by;
        }

        public String getCancel_dueto() {
            return cancel_dueto;
        }

        public void setCancel_dueto(String cancel_dueto) {
            this.cancel_dueto = cancel_dueto;
        }

        public String getOrder_flow_id ()
        {
            return Order_flow_id;
        }

        public void setOrder_flow_id (String Order_flow_id)
        {
            this.Order_flow_id = Order_flow_id;
        }

        public String getTable_booking_date ()
        {
            return table_booking_date;
        }

        public void setTable_booking_date (String table_booking_date)
        {
            this.table_booking_date = table_booking_date;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getCreated_at ()
        {
            return created_at;
        }

        public void setCreated_at (String created_at)
        {
            this.created_at = created_at;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getTable_booking_time ()
        {
            return table_booking_time;
        }

        public void setTable_booking_time (String table_booking_time)
        {
            this.table_booking_time = table_booking_time;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public String getNumber_of_guests ()
        {
            return number_of_guests;
        }

        public void setNumber_of_guests (String number_of_guests)
        {
            this.number_of_guests = number_of_guests;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [customer_mobile = "+customer_mobile+", Order_flow_id = "+Order_flow_id+", table_booking_date = "+table_booking_date+", name = "+name+", created_at = "+created_at+", id = "+id+", table_booking_time = "+table_booking_time+", status = "+status+", number_of_guests = "+number_of_guests+"]";
        }
    }


}
