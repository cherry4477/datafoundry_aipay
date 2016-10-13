package com.ldp.datahub.action;

import java.util.Map;
// test

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.ldp.datahub.common.Constant;
import com.ldp.datahub.exception.LinkServerException;
import com.ldp.datahub.util.ConfigUtil;
import com.ldp.datahub.util.RequestUtil;

import net.sf.json.JSONObject;

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
	 /**
	  * 判断是否是admin
	  * @param me
	  * @param loginName
	  * @param jsonMap
	  * @return
	  * @throws LinkServerException
	  */
	//判断是否为admin或I
	 public boolean isAdminOrI(String me,String loginName,Map<String, Object> jsonMap) throws LinkServerException{
		try {
			if (StringUtils.isEmpty(me)) {
				log.error("please login:"+loginName);//登录请求
				jsonMap.put(Constant.result_code, Constant.no_login_code);
				jsonMap.put(Constant.result_msg, Constant.no_login);
				return false;
			} else {
				if (!me.equals(loginName)) {
					JSONObject json = RequestUtil.getUserInfo(me);
					JSONObject user = json.getJSONObject(Constant.result_data);
					if (user.getInt("userType") != Constant.UserType.ADMIN) {
						log.error(me + " no auth");//认证失败
						jsonMap.put(Constant.result_code, Constant.no_auth_code);
						jsonMap.put(Constant.result_msg, Constant.no_auth);
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			throw new LinkServerException("link user server error");
		}
	}
	
	 /**
	  * 判断用户是否存在 
	  * @param loginname
	  * @param jsonMap
	  * @return
	  */
	 public boolean isExist(String loginname,Map<String, Object> jsonMap){
		 try {
			 JSONObject json = RequestUtil.getUserInfo(loginname);
				if(json==null){
					log.error(loginname+" user not registered yet");//认证失败，用户不存在
					jsonMap.put(Constant.result_code, Constant.no_user_code);
					jsonMap.put(Constant.result_msg, Constant.no_user);
					return false;
				}
				return true;
			
		 }catch (Exception e) {
				throw new LinkServerException("link user server error");
		}
	 }
	 /**
	  * 判断是否是admin
	  * @param me
	  * @param jsonMap
	  * @return
	  * @throws LinkServerException
	  */
	 public boolean isAdmin(String me,Map<String, Object> jsonMap) throws LinkServerException{
		 try {
			 JSONObject json = RequestUtil.getUserInfo(me);
			 JSONObject user = json.getJSONObject(Constant.result_data);
				if(user.getInt("userType")!=Constant.UserType.ADMIN){
					log.error(me+" no auth");//认证失败
					jsonMap.put(Constant.result_code, Constant.no_auth_code);
					jsonMap.put(Constant.result_msg, Constant.no_auth);
					return false;
				}
				return true;
			
		 }catch (Exception e) {
				throw new LinkServerException("link user server error");
		}
	 }	 
//	 public int getUserType(String me){
//		 JSONObject json = RequestUtil.getUserInfo(me);
//		 JSONObject user = json.getJSONObject(Constant.result_data);
//		 return user.getInt("userType");
//	 }
}
