package com.waayu.owner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductDataItem implements Parcelable {

	@SerializedName("Menuitem_Data")
	private List<MenuitemDataItem> menuitemData;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;


	protected ProductDataItem(Parcel in) {
		id = in.readString();
		title = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ProductDataItem> CREATOR = new Creator<ProductDataItem>() {
		@Override
		public ProductDataItem createFromParcel(Parcel in) {
			return new ProductDataItem(in);
		}

		@Override
		public ProductDataItem[] newArray(int size) {
			return new ProductDataItem[size];
		}
	};

	public List<MenuitemDataItem> getMenuitemData(){
		return menuitemData;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}




}