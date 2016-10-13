package com.ldp.datahub.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static int daysBetween(Timestamp start, Timestamp end) {
		return daysBetween(new Date(start.getTime()), new Date(end.getTime()));
	}
	
	public static Date addDays(Date day,int days){
		long dayTime=1000* 3600 * 24 ;
		long time=days*dayTime;
		return new Date(day.getTime()+time);
	}
	
	public static Date addYears(Date day,int years){
		return addDays(day, 365*years);
	}
	public static int daysBetween(Date start, Date end) {

		try {
			Date startd = sdf.parse(sdf.format(start));
			Date endd = sdf.parse(sdf.format(end));
			Calendar cal = Calendar.getInstance();
			cal.setTime(startd);
			long time1 = cal.getTimeInMillis();
			cal.setTime(endd);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);

			return Integer.parseInt(String.valueOf(between_days));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static Date toDate(String dateStr){
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String format(Date day){
		return sdf.format(day);
	}

}
