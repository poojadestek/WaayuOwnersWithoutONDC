package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

public class QrModel{

	@SerializedName("mydate")
	private String mydate;

	@SerializedName("downloads")
	private String downloads;

	@SerializedName("orders")
	private String orders;

	public QrModel(String mydate, String downloads, String orders) {
		this.mydate = mydate;
		this.downloads = downloads;
		this.orders = orders;
	}

	public void setMydate(String mydate){
		this.mydate = mydate;
	}

	public String getMydate(){
		return mydate;
	}

	public void setDownloads(String downloads){
		this.downloads = downloads;
	}

	public String getDownloads(){
		return downloads;
	}

	public void setOrders(String orders){
		this.orders = orders;
	}

	public String getOrders(){
		return orders;
	}
}