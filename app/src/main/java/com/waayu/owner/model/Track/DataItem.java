package com.waayu.owner.model.Track;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("ostatus")
	private String ostatus;

	@SerializedName("orderstatus")
	private String orderstatus;

	@SerializedName("date_time")
	private String dateTime;

	public String getOstatus(){
		return ostatus;
	}

	public String getOrderstatus(){
		return orderstatus;
	}

	public String getDateTime(){
		return dateTime;
	}
}