package com.waayu.owner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Restorent implements Parcelable {

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("RestData")
	public RestorentData resultData;



	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	protected Restorent(Parcel in) {
		responseCode = in.readString();
		responseMsg = in.readString();
		result = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(responseCode);
		dest.writeString(responseMsg);
		dest.writeString(result);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Restorent> CREATOR = new Creator<Restorent>() {
		@Override
		public Restorent createFromParcel(Parcel in) {
			return new Restorent(in);
		}

		@Override
		public Restorent[] newArray(int size) {
			return new Restorent[size];
		}
	};

	public String getResponseCode(){
		return responseCode;
	}

	public RestorentData getResultData(){
		return resultData;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}


}