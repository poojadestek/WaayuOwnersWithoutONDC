package com.waayu.owner.model.Dinein;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DineInResponse{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("DineInPaymentHistory")
	private List<DineInPaymentHistoryItem> dineInPaymentHistory;

	@SerializedName("Result")
	private String result;

	@SerializedName("total_amount")
	private String total_amount;

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}

	public void setResponseMsg(String responseMsg){
		this.responseMsg = responseMsg;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public void setDineInPaymentHistory(List<DineInPaymentHistoryItem> dineInPaymentHistory){
		this.dineInPaymentHistory = dineInPaymentHistory;
	}

	public List<DineInPaymentHistoryItem> getDineInPaymentHistory(){
		return dineInPaymentHistory;
	}

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}
}