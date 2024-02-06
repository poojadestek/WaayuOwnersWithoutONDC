package com.waayu.owner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestorentData implements Parcelable {
	@SerializedName("Product_Data")
	private List<ProductDataItem> productData;

	protected RestorentData(Parcel in) {
		productData = in.createTypedArrayList(ProductDataItem.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(productData);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<RestorentData> CREATOR = new Creator<RestorentData>() {
		@Override
		public RestorentData createFromParcel(Parcel in) {
			return new RestorentData(in);
		}

		@Override
		public RestorentData[] newArray(int size) {
			return new RestorentData[size];
		}
	};

	public List<ProductDataItem> getProductData(){
		return productData;
	}
}