package com.waayu.owner.model.Dinein;

import com.google.gson.annotations.SerializedName;

public class DineInPaymentHistoryItem{

	@SerializedName("date")
	private String date;

	@SerializedName("transaction_id")
	private String transactionId;

	@SerializedName("amount")
	private String amount;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("table_id")
	private String tableId;

	@SerializedName("order_status")
	private String orderStatus;

	@SerializedName("payment_type")
	private String paymentType;

	@SerializedName("date_time")
	private String dateTime;

	@SerializedName("table_booking_mobile")
	private String tableBookingMobile;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("number_of_guests")
	private String numberOfGuests;

	@SerializedName("time_slot")
	private String timeSlot;

	public String getDate(){
		return date;
	}

	public String getTransactionId(){
		return transactionId;
	}

	public String getAmount(){
		return amount;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getTableId(){
		return tableId;
	}

	public String getOrderStatus(){
		return orderStatus;
	}

	public String getPaymentType(){
		return paymentType;
	}

	public String getDateTime(){
		return dateTime;
	}

	public String getTableBookingMobile(){
		return tableBookingMobile;
	}

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}

	public String getNumberOfGuests(){
		return numberOfGuests;
	}

	public String getTimeSlot(){
		return timeSlot;
	}
}