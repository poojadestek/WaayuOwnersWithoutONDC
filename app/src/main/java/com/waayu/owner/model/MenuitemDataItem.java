package com.waayu.owner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MenuitemDataItem implements Parcelable {

    @SerializedName("is_customize")
    private int isCustomize;
    @SerializedName("is_variations")
    private int isVariations;
    @SerializedName("cdesc")
    private String cdesc;

    @SerializedName("status")
    private String status;
    @SerializedName("is_quantity")
    private String isQuantity;
    @SerializedName("disc_perc")
    private String disc_perc;
    @SerializedName("s_price")
    private double s_price;
    @SerializedName("is_veg")
    private int isVeg;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("gst")
    private String gst;
    @SerializedName("item_img")
    private String itemImg;
    @SerializedName("price")
    private double price;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIsCustomize(int isCustomize) {
        this.isCustomize = isCustomize;
    }

    public void setIsVariations(int isVariations) {
        this.isVariations = isVariations;
    }

    public void setCdesc(String cdesc) {
        this.cdesc = cdesc;
    }

    public void setIsQuantity(String isQuantity) {
        this.isQuantity = isQuantity;
    }

    public void setDisc_perc(String disc_perc) {
        this.disc_perc = disc_perc;
    }

    public void setS_price(double s_price) {
        this.s_price = s_price;
    }

    public void setIsVeg(int isVeg) {
        this.isVeg = isVeg;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MenuitemDataItem() {
    }

    protected MenuitemDataItem(Parcel in) {
        isCustomize = in.readInt();
        isVariations = in.readInt();
        cdesc = in.readString();
        isQuantity = in.readString();
        disc_perc = in.readString();
        s_price = in.readDouble();
        isVeg = in.readInt();
        id = in.readString();
        title = in.readString();
        gst = in.readString();
        itemImg = in.readString();
        status = in.readString();
        price = in.readDouble();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isCustomize);
        dest.writeInt(isVariations);
        dest.writeString(cdesc);
        dest.writeString(isQuantity);
        dest.writeString(disc_perc);
        dest.writeDouble(s_price);
        dest.writeInt(isVeg);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(gst);
        dest.writeString(itemImg);
        dest.writeString(status);
        dest.writeDouble(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuitemDataItem> CREATOR = new Creator<MenuitemDataItem>() {
        @Override
        public MenuitemDataItem createFromParcel(Parcel in) {
            return new MenuitemDataItem(in);
        }

        @Override
        public MenuitemDataItem[] newArray(int size) {
            return new MenuitemDataItem[size];
        }
    };

    public int getIsCustomize() {
        return isCustomize;
    }

    public int getIsVariations() {
        return isVariations;
    }

    public String getCdesc() {
        return cdesc;
    }

    public String getIsQuantity() {
        return isQuantity;
    }

    public String getDisc_perc() {
        return disc_perc;
    }

    public double getS_price() {
        return s_price;
    }

    public int getIsVeg() {
        return isVeg;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGst() {
        return gst;
    }

    public String getItemImg() {
        return itemImg;
    }

    public double getPrice() {
        return price;
    }
}