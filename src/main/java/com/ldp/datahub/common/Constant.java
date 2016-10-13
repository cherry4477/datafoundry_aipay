package com.ldp.datahub.common;

public class Constant {
	
	public static final String result_code="code";
	public static final String result_msg = "msg";
	public static final String result_data="data";
	public static final String data_total="total";
	public static final String data_result = "result";
	    
	public static final int sucess_code=0;
    public static final String sucess = "ok";
    
    public static final int fail_code=1001;
    public static final String exception = "unknown error";
    
    public static final int fail_req_code =1011;
    public static final String fail_req="request error";
    
    public static final int param_err_code=1007;
    public static final String param_err="invalid parameters";
    
    public static final int no_user_code=6002;
    public static final String no_user = "user not registered yet";
    
    public static final int no_info_code = 1009;
    public static final String no_info = "not found";
    
    public static final int no_login_code=8005;
    public static final String no_login="no login";
    
    public static final int no_auth_code=1005;
    public static final String no_auth="auth failed";
    
    public static final int server_link_code=1011;
    public static final String server_link="server link error";
    
    public static final int no_money_code=8007;
    public static final String no_money="Sorry, your credit is running low";
    
    public static final int duplicate_code=8008;
    public static final String duplicate="duplicate order requset";
    
    public static final int no_order_code=8009;
    public static final String no_order="order is not exist";
    
    public static final int illegal_code=8010; 
    public static final String illegal="Illegal request";
    
    public static class QutaName{
    	public static String REPO_PUBLIC = "repo_public";//共有repo
    	public static String REPO_PRIVATE = "repo_private";//私有repo
    	
    	public static String PULL_NUM = "pull_num"; //下载
    	public static String DEPOSIT = "deposit"; //托管
    }
    
    public static class PayWay{
    	public static int FREE = 0;//免费
    	public static int BEFORE=1;//预付费
    	public static int AFTER =2;//后付费
    }
    
    public static class RechargeType{
    	public static int add=1;//充值、收入
    	public static int decrease = 2;//扣费、消费
    	public static int vipcost = 3; //年费
    }
    
    public static class UserType{
    	public static int REGIST=1;
    	public static int ADMIN=2;
    	public static int VIP1=3;
    	public static int VIP2 = 4;
    	public static int VIP3 = 5;
    }
    
    /**
     * 账单流水操作类型
     * @author lxy10
     *
     */
    public static class TradeType{
    	public static int recharge_add=1;//充值
    	public static int recharge_dec=2;//提现
    	public static int vip_cost=3;//会员扣费
    	public static int buy_init=4;//购买待生效
    	public static int buy_over=5;//购买生效
    	public static int buy_fail=6;//购买失效
    	public static int buy_cancel=7;//购买后退款
    	public static int sell_over=8;//售出交易成功
    	public static int sell_effect=9;//售出交易生效
    	public static int sell_cancel=10;//售出退款
    	public static int recharge_dec_handle = 12;//充值退款处理中
    	public static int recharge_dec_fail=13;//充值退款失败
    	public static int recharge_handle=14;//充值处理中
    	public static int recharge_fail=15;//充值失败
    }
   public static class TradeStatus{
	   public static int success=1; //成功
	   public static int fail=2;//失败
	   public static int effect=3;//生效
   }
   public static class QueryRechargeStatus{
	   public static int success = 1;//成功
	   public static int fail = 2;//失败
	   public static int acceptance =3;//转账已受理
	   public static int handle =4;//充值处理中
	   public static int queryFail =5;//充值失败
	   
   }
}
