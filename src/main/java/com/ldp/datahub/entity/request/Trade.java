package com.ldp.datahub.entity.request;

public class Trade {

	private String order_id;
	private String plan_id;
	private String trade_item;
	private Float trade_amount;
	private String trade_user; //交易对方
	private Integer status; //交易状态
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getPlan_id() {
		return plan_id;
	}
	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}
	public String getTrade_item() {
		return trade_item;
	}
	public void setTrade_item(String trade_item) {
		this.trade_item = trade_item;
	}
	public Float getTrade_amount() {
		return trade_amount;
	}
	public void setTrade_amount(Float trade_amount) {
		this.trade_amount = trade_amount;
	}
	public String getTrade_user() {
		return trade_user;
	}
	public void setTrade_user(String trade_user) {
		this.trade_user = trade_user;
	}
	
}
