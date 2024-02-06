package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

public class ProductData {
    @SerializedName("productdetails")
    Productdetails productdetails;

    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMsg")
    private String mResponseMsg;
    @SerializedName("Result")
    private String mResult;

    public Productdetails getProductdetails() {
        return productdetails;
    }

    public String getmResponseCode() {
        return mResponseCode;
    }

    public String getmResponseMsg() {
        return mResponseMsg;
    }

    public String getmResult() {
        return mResult;
    }

    public class Productdetails {

        @SerializedName("id")
        private String id;

        @SerializedName("category_id")
        private String category_id;

        @SerializedName("gst")
        private String gst;

        @SerializedName("is_variations")
        private String is_variations;
        @SerializedName("title")
        private String title;
        @SerializedName("item_img")
        private String item_img;
        @SerializedName("status")
        private String status;
        @SerializedName("discount_price")
        private String discount_price;
        @SerializedName("base_price")
        private String base_price;

        @SerializedName("is_veg")
        private String is_veg;
        @SerializedName("is_egg")
        private String is_egg;
        @SerializedName("is_recommended")
        private String is_recommended;
        @SerializedName("is_customize")
        private String is_customize;
        @SerializedName("is_customize_time")
        private String is_customize_time;
        @SerializedName("open_time")
        private String open_time;
        @SerializedName("close_time")
        private String close_time;
        @SerializedName("evening_open_time")
        private String evening_open_time;
        @SerializedName("evening_close_time")
        private String evening_close_time;
        @SerializedName("cdesc")
        private String cdesc;

        public String getIs_variations() {
            return is_variations;
        }

        public String getCategory_id() {
            return category_id;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getItem_img() {
            return item_img;
        }

        public String getStatus() {
            return status;
        }

        public String getDiscount_price() {
            return discount_price;
        }

        public String getBase_price() {
            return base_price;
        }

        public String getGst() {
            return gst;
        }

        public String getIs_veg() {
            return is_veg;
        }

        public String getIs_egg() {
            return is_egg;
        }

        public String getIs_recommended() {
            return is_recommended;
        }

        public String getIs_customize() {
            return is_customize;
        }

        public String getIs_customize_time() {
            return is_customize_time;
        }

        public String getOpen_time() {
            return open_time;
        }

        public String getClose_time() {
            return close_time;
        }

        public String getEvening_open_time() {
            return evening_open_time;
        }

        public String getEvening_close_time() {
            return evening_close_time;
        }

        public String getCdesc() {
            return cdesc;
        }
    }
}
