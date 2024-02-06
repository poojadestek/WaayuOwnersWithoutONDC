package com.waayu.owner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryData {

    @SerializedName("ResponseCode")
    public String responseCode;
    @SerializedName("Result")
    public String result;
    @SerializedName("ResponseMsg")
    public String responseMsg;
    @SerializedName("RestData")
    public RestData restData;


    public String getResponseCode() {
        return responseCode;
    }

    public RestData getResultData() {
        return restData;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public String getResult() {
        return result;
    }

    public class RestData {
        @SerializedName("Product_Data")
        public ArrayList<ProductDatum> product_Data;

        public ArrayList<ProductDatum> getProduct_Data() {
            return product_Data;
        }

        public class ProductDatum {
            @SerializedName("id")
            public String id;
            @SerializedName("title")
            public String title;
            @SerializedName("gst")
            public String gst;
            @SerializedName("item_img")
            public String item_img;
            @SerializedName("price")
            public String price;
            @SerializedName("s_price")
            public String s_price;
            @SerializedName("disc_perc")
            public String disc_perc;
            @SerializedName("is_veg")
            public Object is_veg;
            @SerializedName("is_customize")
            public String is_customize;
            @SerializedName("is_variations")
            public String is_variations;
            @SerializedName("cdesc")
            public String cdesc;
            @SerializedName("status")
            public String status;

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }

            public String getGst() {
                return gst;
            }

            public String getItem_img() {
                return item_img;
            }

            public String getPrice() {
                return price;
            }

            public String getS_price() {
                return s_price;
            }

            public String getDisc_perc() {
                return disc_perc;
            }

            public Object getIs_veg() {
                return is_veg;
            }

            public String getIs_customize() {
                return is_customize;
            }

            public String getIs_variations() {
                return is_variations;
            }

            public String getCdesc() {
                return cdesc;
            }

            public String getStatus() {
                return status;
            }
        }

    }

}
