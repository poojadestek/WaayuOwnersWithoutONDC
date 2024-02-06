package com.waayu.owner.model.Track;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderTrackResponse{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public List<DataItem> getData(){
		return data;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}