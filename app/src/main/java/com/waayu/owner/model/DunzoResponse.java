package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

public class DunzoResponse{

	@SerializedName("response")
	private String response;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("taskid")
	private String taskid;

	public DunzoResponse(String response, String createdAt, String orderId, String taskid) {
		this.response = response;
		this.createdAt = createdAt;
		this.orderId = orderId;
		this.taskid = taskid;
	}

	public String getResponse(){
		return response;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getOrderId(){
		return orderId;
	}

	public String getTaskid(){
		return taskid;
	}
}