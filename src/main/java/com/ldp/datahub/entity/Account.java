package com.ldp.datahub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Account {
	
	private String userId;
	private Float actualBalance;
	private Float availableBalance;
	private Float creditLimit;
	
	@JsonIgnore  
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Float getActualBalance() {
		return actualBalance;
	}
	public void setActualBalance(Float actualBalance) {
		this.actualBalance = actualBalance;
	}
	public Float getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(Float availableBalance) {
		this.availableBalance = availableBalance;
	}
	public Float getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(Float creditLimit) {
		this.creditLimit = creditLimit;
	}

}
