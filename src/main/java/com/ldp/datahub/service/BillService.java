package com.ldp.datahub.service;

public interface BillService
{

	/**
	 * 充值 体现 扣年费
	 * @param user
	 * @param admin
	 * @param orderId
	 * @param amount
	 * @param type
	 * @param channel
	 * @return
	 * @throws Exception
	 */
	public String recharge(String user,String orderId,float amount,
			String returnUrl,String clientAddr)throws Exception;
	public int query(String orderId);
	
	boolean readNoticeMsg(String Msg);
}
