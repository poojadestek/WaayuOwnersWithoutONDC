package com.waayu.owner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderInfo{

	@SerializedName("rest_charge")
	private String restCharge;

	@SerializedName("wall_amt")
	private String wallAmt;

	@SerializedName("coupon_title")
	private String couponTitle;

	@SerializedName("rest_distance")
	private String restDistance;

	@SerializedName("p_method_name")
	private String pMethodName;

	@SerializedName("dunzo_task_id")
	private String dunzo_task_id;

	@SerializedName("dp_type")
	private String dp_type;

	@SerializedName("cou_amt")
	private String couAmt;

	@SerializedName("o_status")
	private String oStatus;

	@SerializedName("order_complete_date")
	private String orderCompleteDate;

	@SerializedName("address_type")
	private String addressType;

	@SerializedName("rider_tip")
	private String riderTip;

	@SerializedName("cust_address")
	private String custAddress;

	@SerializedName("tax")
	private String tax;

	@SerializedName("rest_name")
	private String restName;

	@SerializedName("delivery_charge")
	private String deliveryCharge;

	@SerializedName("rest_address")
	private String restAddress;

	@SerializedName("subtotal")
	private String subtotal;

	@SerializedName("rider_name")
	private String riderName;

	@SerializedName("order_total")
	private String orderTotal;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("a_note")
	private String aNote;

	@SerializedName("order_items")
	private List<OrderItemsItem> orderItems;

	@SerializedName("Order_flow_id")
	private String orderFlowId;

	@SerializedName("rider_img")
	private String riderImg;

	@SerializedName("rider_mobile")
	private String riderMobile;

	@SerializedName("otp")
	private String Otp;

	@SerializedName("rid")
	private String rid;


	@SerializedName("customer_name")
	private String customer_name;
	@SerializedName("customer_mobile")
	private String customer_mobile;


	public String getDunzo_task_id() {
		return dunzo_task_id;
	}

	public String getDp_type() {
		return dp_type;
	}

	public String getRid() {
		return rid;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public void setCustomer_mobile(String customer_mobile) {
		this.customer_mobile = customer_mobile;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public String getCustomer_mobile() {
		return customer_mobile;
	}

	public String getRestCharge(){
		return restCharge;
	}

	public String getWallAmt(){
		return wallAmt;
	}

	public String getCouponTitle(){
		return couponTitle;
	}

	public String getPMethodName(){
		return pMethodName;
	}

	public String getCouAmt(){
		return couAmt;
	}

	public String getOStatus(){
		return oStatus;
	}

	public String getOrderCompleteDate(){
		return orderCompleteDate;
	}

	public String getAddressType(){
		return addressType;
	}

	public String getRiderTip(){
		return riderTip;
	}

	public String getCustAddress(){
		return custAddress;
	}

	public String getTax(){
		return tax;
	}

	public String getRestName(){
		return restName;
	}

	public String getDeliveryCharge(){
		return deliveryCharge;
	}

	public String getRestAddress(){
		return restAddress;
	}

	public String getSubtotal(){
		return subtotal;
	}

	public String getRiderName(){
		return riderName;
	}

	public String getOrderTotal(){
		return orderTotal;
	}

	public String getOrderId(){
		return orderId;
	}

	public List<OrderItemsItem> getOrderItems(){
		return orderItems;
	}

	public String getOrderFlowId() {
		return orderFlowId;
	}

	public void setOrderFlowId(String orderFlowId) {
		this.orderFlowId = orderFlowId;
	}

	public String getRiderImg() {
		return riderImg;
	}

	public void setRiderImg(String riderImg) {
		this.riderImg = riderImg;
	}

	public String getRiderMobile() {
		return riderMobile;
	}

	public void setRiderMobile(String riderMobile) {
		this.riderMobile = riderMobile;
	}

	public String getOtp() {
		return Otp;
	}

	public void setOtp(String otp) {
		Otp = otp;
	}

	public String getRestDistance() {
		return restDistance;
	}

	public void setRestDistance(String restDistance) {
		this.restDistance = restDistance;
	}

	public String getaNote() {
		return aNote;
	}

	public void setaNote(String aNote) {
		this.aNote = aNote;
	}
}