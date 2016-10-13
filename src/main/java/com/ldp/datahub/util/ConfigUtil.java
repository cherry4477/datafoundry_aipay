package com.ldp.datahub.util;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.health.model.HealthService;

import cn.com.hongpay.client.anal.ResMsgAnalyze;
import cn.com.hongpay.client.gen.ReqMsgGenerate;

public class ConfigUtil {
	private static Log log = LogFactory.getLog(ConfigUtil.class);
	private static Properties properties = new Properties();
	private static Properties kafkaPro = new Properties();
	private static String consul="";
	private static String pfxPwd;
	private static String privateKeyPwd ;
	private static String partnerId;
	private static String verifyCode;
	private static String notifyUrl;
	private static String partnerAcctId;
	private static String partnerAcctName;
	private static String queryUrl;
	
	static{
		InputStream inStream = ConfigUtil.class.getClassLoader()
				.getResourceAsStream("application.properties");
		try {
			properties.load(inStream);
			
			consul =  System.getenv("CONSUL_SERVER");
			if(StringUtils.isEmpty(consul)){
				consul = properties.getProperty("CONSUL_SERVER");
			}

			pfxPwd = System.getenv("PFX_PWD");
			if(StringUtils.isEmpty(pfxPwd)){
				pfxPwd=properties.getProperty("PFX_PWD");
			}
			
			privateKeyPwd = System.getenv("PRIVATE_KEY_PWD");
			if(StringUtils.isEmpty(privateKeyPwd)){
				privateKeyPwd=properties.getProperty("PRIVATE_KEY_PWD");
			}
			partnerId = System.getenv("PARTNER_ID");
			if(StringUtils.isEmpty(partnerId)){
				partnerId=properties.getProperty("PARTNER_ID");
			}
			verifyCode = System.getenv("VERIFY_CODE");
			if(StringUtils.isEmpty(verifyCode)){
				verifyCode=properties.getProperty("VERIFY_CODE");
			}
			notifyUrl = System.getenv("NOTIFY_URL");
			if(StringUtils.isEmpty(notifyUrl)){
				notifyUrl=properties.getProperty("NOTIFY_URL");
			}
			partnerAcctId = System.getenv("PARTNER_ACCT_ID");
			if(StringUtils.isEmpty(partnerAcctId)){
				partnerAcctId=properties.getProperty("PARTNER_ACCT_ID");
			}
			partnerAcctName = System.getenv("PARTNER_ACCT_NAME");
			if(StringUtils.isEmpty(partnerAcctName)){
				partnerAcctName=properties.getProperty("PARTNER_ACCT_NAME");
			}
			queryUrl = System.getenv("QUERY_URL");
			if(StringUtils.isEmpty(queryUrl)){
				queryUrl=properties.getProperty("QUERY_URL");
			}
		}catch (IOException e) {
			log.error("Error loading configuration file", e);
		}
	}

	 public static String getServerConfig(String name){
		    ConsulClient client = new ConsulClient(ConfigUtil.getConsul());
	    	Response<List<HealthService>> response =  client.getHealthServices(name, false, null);
	    	List<HealthService> list = response.getValue();
	    	for(HealthService service:list){
	    		//MYSQL 配置加载
	    		String ip = service.getService().getAddress();
	    		int port = service.getService().getPort();
	    		
	    		log.info("load service:"+ip+":"+port);
	    		
		    	return ip+":"+port;
		    	//重新加载数据源
	    	}
	    	return "";
	 }
	 public static Properties getKafkaConfig(){
		if(kafkaPro.isEmpty()){
			String kafka ="";
			String kafkaServer = System.getenv("kafka_service_name");
			if(StringUtils.isEmpty(kafkaServer)){
				kafka = properties.getProperty("kafka");
			}else{
				kafka = getServerConfig(kafkaServer);
			}
			kafkaPro.put("metadata.broker.list",kafka );
			kafkaPro.put("serializer.class", "kafka.serializer.StringEncoder");
			kafkaPro.put("key.serializer.class", "kafka.serializer.StringEncoder");
		}
		return kafkaPro;
	}
	public static String getConsul(){
		return consul;
	}
	public static ReqMsgGenerate  getPublicReqMsgGenerate(){
		String pfxFile =  "hongpay/test_public_key.cer ";
		ReqMsgGenerate reqMsgGenerate = new ReqMsgGenerate(pfxFile,"aipay123456","aipay654321");
		return reqMsgGenerate;
	}
	public static ReqMsgGenerate  getPrivateReqMsgGenerate(){
		String pfxFile = "hongpay/test_private_key.pfx";
		ReqMsgGenerate reqMsgGenerate = new ReqMsgGenerate(pfxFile,"aipay123456","aipay654321");
		return reqMsgGenerate;
	}
	public static ResMsgAnalyze  getResMsgAnalyze(){
		String cerFile  ="hongpay/aipay_public.cer";
		return  new ResMsgAnalyze(cerFile);
	}
	
	public static String getPartnerId(){
		return partnerId;
	}
	public static String getVerifyCode(){
		return verifyCode;
	}
	public static String getNotifyUrl(){
		return notifyUrl;
	}
	public static String getPartnerAcctId(){
		return partnerAcctId;
	}
	public static String getPartnerAcctName(){
		return partnerAcctName;
	}
	public static String getQueryUrl(){
		return queryUrl;
	}
	
}
