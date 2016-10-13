package com.ldp.datahub.service.impl;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Service;
import com.ldp.datahub.exception.VerifySignatureFail;
import com.ldp.datahub.service.BillService;
import com.ldp.datahub.util.ConfigUtil;
import com.ldp.datahub.util.MySecureProtocolSocketFactory;
import com.ldp.datahub.util.RequestUtil;

import cn.com.hongpay.client.anal.ResMsgAnalyze;
import cn.com.hongpay.client.gen.ReqMsgGenerate;
import cn.com.hongpay.client.vo.pay.SinglePayResVo;
import cn.com.hongpay.client.vo.pay.SinglePayVo;
import cn.com.hongpay.client.vo.query.QueryResVo;
import cn.com.hongpay.client.vo.query.QueryVo;

@Service
public class BillServiceImpl implements BillService
{
	private static Log log = LogFactory.getLog(BillServiceImpl.class);
	
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
	@Override
	public String recharge(String userId, String orderId,float amount,
			String returnUrl,String clientAddr) throws Exception{
		String reqSignMsg = null;
	
		reqSignMsg = madePayMsg(userId,orderId,returnUrl,clientAddr,amount);
			
		return reqSignMsg;
	}
	
	/**
	 * 查询充值结果
	 */
	public int query(String orderId){
		String queryMsg =madeQueryMsg(orderId);
		String url = ConfigUtil.getQueryUrl();
		String Msg = send(queryMsg,url);
		String result = readMsg(Msg,"query");
		
		return Integer.parseInt(result);
	}
	
	/**
	 * 读取支付通知报文
	 */
	@Override
	public boolean readNoticeMsg(String Msg){
		//1得到连接
		ResMsgAnalyze analyze = ConfigUtil.getResMsgAnalyze();
		try {
			//2获取报文
			//验证并解析通知报文
			SinglePayResVo singlePayResVo = analyze.verifyPayNotifyMsg(Msg);
			//3解读报文
			if(singlePayResVo != null){
				String orderId = singlePayResVo.getPartnerTradeNo();
				
				//4区分报文类别
				String result = singlePayResVo.getResultCode();
				if(result.equals("00")){
					return true;
				}else{
					return false;
				}
			}
			//验证签名失败
			log.info("verifyPayNotifyMsg fail");
			return false;
			
		} catch (Exception e) {
			//解读报文失败
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 生成支付报文
	 * @param orderId
	 * @param returnUrl
	 * @param clientAddr
	 * @param amount
	 * @return
	 */
	private String madePayMsg(String userId,String orderId,String returnUrl,String clientAddr,float amount){
		String reqSignMsg=null;
		ReqMsgGenerate reqMsgGenerate = ConfigUtil.getPrivateReqMsgGenerate();
		//设置支付请求信息
		SinglePayVo payVo = new SinglePayVo();
		payVo.setServiceType("1");//服务类型，请根据接口文档按需使用
		payVo.setClientIp(clientAddr);//客户端IP,request为HttpServletRequest
		//＊＊＊环境中取
		payVo.setPartnerId(ConfigUtil.getPartnerId());//合作方ID ，由鸿支付统一分配，必填
		//＊＊＊环境中取
		payVo.setPartnerVerifyCode(ConfigUtil.getVerifyCode());//合作方身份校验码，由鸿支付统一分配，必填
		payVo.setRequestTime(new Date());//当前日期，必填
		
		//TRANS_SUM
		payVo.setPayerPartnerAcctId(userId);//付款方账户标识，必填
		payVo.setPayerPartnerAcctName(userId);//付款方账户名称，必填
		//＊＊＊环境中取
		payVo.setNotifyUrl(ConfigUtil.getNotifyUrl());
		payVo.setReturnUrl(returnUrl);
		payVo.setTotalRecord(1);//交易记录数，请填1，必填
		payVo.setTotalAmount((long)(amount*100));//交易总金额，单位分，必填
		
		//TRANS_DETAIL
		payVo.setPartnerTradeNo(orderId);//订单号，必填
		payVo.setPayAmount((long)(amount*100));//订单金额，单位分，必填
		//＊＊＊环境中取
		payVo.setPayeePartnerAcctId(ConfigUtil.getPartnerAcctId());//收款方账户标识，必填
		//＊＊＊环境中取
		payVo.setPayeePartnerAcctName(ConfigUtil.getPartnerAcctName());//收款方账户名称，必填
		
		payVo.setTradeTime(new Date());
		
		//生成支付请求报文
		try {
			reqSignMsg = reqMsgGenerate.generatePayMsg(payVo);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("made pay msg fail");
		}
		return reqSignMsg;
	}
	

	/**
	 * 生成查询报文
	 * @param orderId
	 * @return
	 */
	private String madeQueryMsg(String orderId){
		ReqMsgGenerate reqMsgGenerate = ConfigUtil.getPrivateReqMsgGenerate();
		//根据要查询的订单号构造查询请求报文
		QueryVo queryVo = new QueryVo();
		String queryMsg = null;
    	//INFO
    	queryVo.setServiceType("70");//服务类型 必填
    	queryVo.setPartnerId(ConfigUtil.getPartnerId());//合作方标识 ，由鸿支付统一分配，必填。请填写鸿支付提供的合作方ID
    	queryVo.setPartnerVerifyCode(ConfigUtil.getVerifyCode());//合作方身份校验码，由鸿支付统一分配，必填。请填写鸿支付提供的身份校验码
    	queryVo.setRequestTime(new Date());//请求日期，必填
    	//BODY
    	queryVo.setPartnerTradeNo(orderId);
    	queryVo.setQueryTime(new Date());//当前日期，必填
    	queryVo.setQueryRemark("查询交易结果");//查询备注，选填
    	//生成签名的查询请求报文
    	try {
    		queryMsg = reqMsgGenerate.generateQueryMsg(queryVo);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("made query message fail");
		}
		return queryMsg;
	}
	
	/**
	 * 后台发送报文
	 * @param Msg
	 * @return
	 */
	private String send(String Msg,String url){
		String msg =null;
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);
		httpClient.getParams().setParameter("Content-Encoding",HTTP.UTF_8);
		httpClient.getParams().setParameter("; charset=", HTTP.UTF_8);
		httpClient.getParams().setParameter("US-ASCII", HTTP.UTF_8);
		String URL = url;  
		Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory (), 443);  
		//MySecureProtocolSocketFactory请参考附录A 
		Protocol.registerProtocol("https", myhttps);   
		    	
		//设置请求url及请求报文
		PostMethod postMethod = new PostMethod(URL);  
		postMethod.setParameter("requestPacket", Msg);

		//发起请求
		int statusCode;
		try {
			statusCode = httpClient.executeMethod(postMethod);

			if(statusCode == HttpStatus.SC_OK) {
			//请求成功，获取查询结果
			//从responseBody获取响应报文
			byte[] responseBody = postMethod.getResponseBody();
			msg = new String(responseBody, HTTP.UTF_8);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		return msg;
		
	}
	/**
	 * 读取查询响应报文
	 * @param msg
	 * @return
	 */
	String readMsg(String msg,String type){
		ResMsgAnalyze analyze = ConfigUtil.getResMsgAnalyze();
		String result = null;
		try {
			if(type.equals("query")){
				QueryResVo queryResVo = analyze.verifyQueryResMsg(msg);
				if(queryResVo == null){
					throw new  VerifySignatureFail("Verify signature failed");
				}else{
					//查询成功 ，其中queryResVo.getRetCode() 为查询结果
					result= queryResVo.getRetCode();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}

	

