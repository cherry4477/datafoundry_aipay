package com.ldp.datahub.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BillDetail implements Cloneable {
	private int id;
	private String userId;
	private String planId;
	private String orderId;
	private String tradeItem;
	private Timestamp opTime;
	private int opType;
	private String channel;
	private String tradeUser;
	private int method;
	private String remark;
	private float tradeAmount;
	private float actualAmount;
	private float availableAmount;
	
	public BillDetail clone(){
		BillDetail detail = null;
		try {
			detail= (BillDetail)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return detail;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@JsonIgnore
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTradeItem() {
		return tradeItem;
	}
	public void setTradeItem(String tradeItem) {
		this.tradeItem = tradeItem;
	}
	public int getOpType() {
		return opType;
	}
	public void setOpType(int opType) {
		this.opType = opType;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getTradeUser() {
		return tradeUser;
	}
	public void setTradeUser(String tradeUser) {
		this.tradeUser = tradeUser;
	}
	public float getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(float tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public float getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(float actualAmount) {
		this.actualAmount = actualAmount;
	}
	public float getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(float availableAmount) {
		this.availableAmount = availableAmount;
	}
	public Timestamp getOpTime() {
		return opTime;
	}
	public void setOpTime(Timestamp opTime) {
		this.opTime = opTime;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
