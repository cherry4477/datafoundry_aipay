package com.ldp.datahub.action;

import java.util.Map;
// test

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.ldp.datahub.common.Constant;

public class BaseAction {
	private static Log log = LogFactory.getLog(BaseAction.class);
	//设置响应状态
	public void setResponseStatus(Map<String, Object> jsonMap,HttpServletResponse response){
		Integer code = (Integer)jsonMap.get(Constant.result_code);
		 if(code>0){
    		 if(code==Constant.fail_code){
    			 response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		 }else{
    			 response.setStatus(HttpStatus.BAD_REQUEST.value());
    		 }
    	 }else{
    		 
    		 response.setStatus(HttpStatus.OK.value());
    	 }
	}
	
}
