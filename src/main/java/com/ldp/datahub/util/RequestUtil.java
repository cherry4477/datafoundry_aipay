package com.ldp.datahub.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.google.gson.JsonObject;

import net.sf.json.JSONObject;

public class RequestUtil {
	private static Log log = LogFactory.getLog(RequestUtil.class);
	private static String host = "";
	private static Properties properties = new Properties();
	
	static{
		InputStream inStream = RequestUtil.class.getClassLoader()
				.getResourceAsStream("application.properties");
		try {
			properties.load(inStream);
			String server = System.getenv("API_SERVER");
			if(StringUtils.isEmpty(server)){
				host=properties.getProperty("SERVER_URL");
			}else{
				host = "http://"+server+":"+System.getenv("API_PORT");
			}
		} catch (IOException e) {
			log.error("Error loading configuration file", e);
		}
	}
	
	public static JSONObject getUserInfo(String userName) {
		 try {
			 URL url = new URL(host+"/users/"+userName);
				//打开restful链接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				// 提交模式
				conn.setRequestMethod("GET");//POST GET PUT DELETE
				//设置访问提交模式，表单提交
				conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
				conn.setConnectTimeout(10000);//连接超时 单位毫秒
				conn.setReadTimeout(2000);//读取超时 单位毫秒
				conn.setDoOutput(false);// 是否输入参数
				
				InputStream inStream=conn.getInputStream();
				byte[] bypes = new byte[2048];
				inStream.read(bypes, 0, inStream.available());
				String content = new String(bypes, "utf-8");
				log.info("request response:"+content);
				return JSONObject.fromObject(content);
		} catch (Exception e) {
			log.error("request error:"+e.getMessage());
			return null;
		}
	 }
	public static JSONObject send(String userName) {
		 try {
			 URL url = new URL(host+"/users/"+userName);
				//打开restful链接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				          
				// 提交模式
				conn.setRequestMethod("POST");//POST GET PUT DELETE
				//设置访问提交模式，表单提交
				conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
				conn.setConnectTimeout(10000);//连接超时 单位毫秒
				conn.setReadTimeout(2000);//读取超时 单位毫秒
				conn.setDoOutput(true);// 是否输入参数
				OutputStream outStream = conn.getOutputStream();
				JsonObject json  = new JsonObject();
				json.addProperty("OrderId", "aaaa");
				
				outStream.write(json.toString().getBytes());
		        outStream.flush();
		        outStream.close();
				InputStream inStream=conn.getInputStream();
				byte[] bypes = new byte[2048];
				inStream.read(bypes, 0, inStream.available());
				String content = new String(bypes, "utf-8");
				log.info("request response:"+content);
				return JSONObject.fromObject(content);
		} catch (Exception e) {
			log.error("request error:"+e.getMessage());
			return null;
		}
	 }

}
