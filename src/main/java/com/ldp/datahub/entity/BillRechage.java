package com.ldp.datahub.entity;

import java.sql.Timestamp;

public class BillRechage {
	private String orderId;
	private int userId;
	private Timestamp rechargeTime;
	private int rechargeType;
	private float rechargeAmount;
	private String channel;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Timestamp getRechargeTime() {
		return rechargeTime;
	}
	public void setRechargeTime(Timestamp rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
	public int getRechargeType() {
		return rechargeType;
	}
	public void setRechargeType(int rechargeType) {
		this.rechargeType = rechargeType;
	}
	public float getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(float rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
