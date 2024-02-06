package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

public class ResponseCP {

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	@SerializedName("ResultData")
	private DataCP resultData;

	public String getResponseCode(){
		return responseCode;
	}

	public DataCP getResultData(){
		return resultData;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}