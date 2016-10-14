package com.ldp.datahub.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ldp.datahub.common.Constant;
import com.ldp.datahub.entity.request.Recharge;
import com.ldp.datahub.service.BillService;
import com.ldp.datahub.util.ConfigUtil;

import cn.com.hongpay.client.anal.ResMsgAnalyze;
import cn.com.hongpay.client.gen.ReqMsgGenerate;
import cn.com.hongpay.client.utils.CommonTools;
import cn.com.hongpay.client.vo.pay.SinglePayNotifyResVo;
import cn.com.hongpay.client.vo.pay.SinglePayResVo;


/**
 * 会员控制层类
 * 
 * @author: 刘雪莹
 * @Date: 2014/10/23
 */

@Controller
public class BillAction extends BaseAction
{
	private static Log log = LogFactory.getLog(BillAction.class);

	@Autowired
	private BillService billService;
	
	/**
	 * 充值，提现，扣年费
	 * @param loginName
	 * @param body
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/bill/{loginName:.*}/recharge", method = RequestMethod.PUT)
	public @ResponseBody Map<String, Object> recharge(@PathVariable String loginName,@RequestBody Recharge recharge,HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			String orderId = recharge.getOrder_id();
			Float amount = recharge.getAmount();
			String returnUrl = recharge.getReturnUrl();
			String clientAddr = CommonTools.getClientAddr(request);
			if(StringUtils.isEmpty(orderId)||amount<=0){
				log.error("recharge fail:"+loginName+",param error");
				jsonMap.put(Constant.result_code, Constant.param_err_code);
				jsonMap.put(Constant.result_msg, Constant.param_err);
				return jsonMap;
			}
			//充值 提现 扣年费
			String reqSignMsg = billService.recharge(loginName,orderId,amount,returnUrl,clientAddr);
			jsonMap.put(Constant.result_code, Constant.sucess_code);
			jsonMap.put(Constant.result_msg, Constant.sucess);
			jsonMap.put(Constant.result_data, reqSignMsg);
			log.info("recharge ok:"+loginName);
			
		}
		catch (Exception e) {
			log.error("recharge fail:"+loginName+";"+e.getMessage());
			jsonMap.put(Constant.result_code, Constant.fail_code);
			jsonMap.put(Constant.result_msg, Constant.exception);
		}finally {
			setResponseStatus(jsonMap, response);
		}
		return jsonMap;
	}
	
	@RequestMapping(value = "/bill/recharge", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> query(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			
			String orderId = request.getParameter("order_id");
            //发起查询请求
			int result = billService.query(orderId);
			jsonMap.put(Constant.result_code, Constant.sucess_code);
			jsonMap.put(Constant.result_msg, Constant.sucess);
			jsonMap.put("result", result);
			log.info("query ok:");
			
		}catch (Exception e) {
			log.error("query fail: "+e.getMessage());
			jsonMap.put(Constant.result_code, Constant.fail_code);
			jsonMap.put(Constant.result_msg, Constant.exception);
		}finally {
			setResponseStatus(jsonMap, response);
		}
		return jsonMap;
	}

	@RequestMapping(value = "/payconfirm/message", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> receivePay(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		ResMsgAnalyze analyze = ConfigUtil.getResMsgAnalyze();
		ReqMsgGenerate reqMsgGenerate = ConfigUtil.getPrivateReqMsgGenerate();
		try{
			InputStream ins = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(ins));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null){
				buffer.append(line);
			}
			String payNotifyXml = buffer.toString();
			
			boolean result = billService.readNoticeMsg(payNotifyXml); 
			SinglePayResVo singlePayResVo = analyze.verifyPayNotifyMsg(payNotifyXml);
			if(result){
				//** 返回信息给鸿支付，告知已收到支付结果通知
				SinglePayNotifyResVo payNotifyResVo = new SinglePayNotifyResVo();
		    	payNotifyResVo.setPartnerId(ConfigUtil.getPartnerId());//合作方标识 ，由鸿支付统一分配，必填。请填写鸿支付提供的合作方ID
		    	payNotifyResVo.setPartnerVerifyCode(ConfigUtil.getVerifyCode());//合作方身份校验码，由鸿支付统一分配，必填。请填写鸿支付提供的身份校验码
		    	payNotifyResVo.setRequestTime(new Date());//当前日期，必填
		    	payNotifyResVo.setTradeNo(singlePayResVo.getTradeNo());
		    	payNotifyResVo.setPartnerTradeNo(singlePayResVo.getPartnerTradeNo());
		    	payNotifyResVo.setResultCode("00");//00：成功收到通知
		    	
		    	//生成签名的支付通知反馈报文
		    	String signPayNotifyMsg = reqMsgGenerate.generatePayNotifyResMsg(payNotifyResVo);
		    	
		    	data.put("signPayNotifyMsg", signPayNotifyMsg);
		    	data.put("order_id", singlePayResVo.getPartnerTradeNo());
		    	data.put("result", 0);
		    	jsonMap.put(Constant.result_code, Constant.sucess_code);
		    	jsonMap.put(Constant.result_msg, Constant.sucess);
		    	jsonMap.put(Constant.result_data, data);
		    	
			}else{
				
				data.put("signPayNotifyMsg", "验证签名失败".getBytes());
		    	data.put("order_id", singlePayResVo.getPartnerTradeNo());
		    	data.put("result", 1);
		    	jsonMap.put(Constant.result_code, Constant.sucess_code);
		    	jsonMap.put(Constant.result_msg, Constant.sucess);
		    	jsonMap.put(Constant.result_data, data);
			}
			
		}catch (Exception e) {
			log.error(e.getMessage());
			data.put("signPayNotifyMsg", "报文解析失败".getBytes());
	    	jsonMap.put(Constant.result_code, Constant.fail_code);
	    	jsonMap.put(Constant.result_msg, e.getMessage());
	    	jsonMap.put(Constant.result_data, data);
		}finally {
			setResponseStatus(jsonMap, response);
		}
		return jsonMap;
	}
}
