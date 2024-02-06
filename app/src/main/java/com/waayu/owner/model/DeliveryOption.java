package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryOption{

	@SerializedName("dp_name")
	private String dpName;

	@SerializedName("dp_id")
	private String dpId;

	@SerializedName("estimated_price")
	private  String estimated_price;

	@SerializedName("dunzo_err_msg")
	private  String dunzo_err_msg;

	@SerializedName("dunzo_additional_err_msg")
	private  String dunzo_additional_err_msg;

	@SerializedName("dunzo_taskid")
	private  String dunzo_taskid;

	public String getDunzo_taskid() {
		return dunzo_taskid;
	}

	public String getDunzo_err_msg() {
		return dunzo_err_msg;
	}

	public String getDunzo_additional_err_msg() {
		return dunzo_additional_err_msg;
	}

	public String getDpName(){
		return dpName;
	}

	public String getDpId(){
		return dpId;
	}

	public String getEstimated_price() {
		return estimated_price;
	}

	public void setEstimated_price(String estimated_price) {
		this.estimated_price = estimated_price;
	}
}