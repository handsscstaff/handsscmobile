package com.handexp.utl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtl {
	
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String getCurrentDate()
	{
		return df.format(new Date());
		
		
	}
	
	
	public static String getFirstDayOfMonthS(Date date)
	{
		
		return df.format(getFirstDayOfMonth(date));
	}
	
	public static String    getLastDayOfMonthS(Date   date)
	{
		
		return df.format(getLastDayOfMonth(date));
	}
	
	public static String  getFirstDayOfWeekS(Date date)
	{
		return df.format(getFirstDayOfWeek(date));
	}
	
	public static String  getLastDayOfWeekS(Date date)
	{
		
		return df.format(getLastDayOfWeek(date));
	}
	
	
	public static Date  getFirstDayOfMonth(Date date)
	{
		 Calendar   calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		Date FirstDayOfMonth = calendar.getTime();
		FirstDayOfMonth.setDate(1);
		
		return FirstDayOfMonth;
		
		
	}
	
	
	
	public static Date    getLastDayOfMonth(Date   date)
	{
		 Calendar   calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		Date  lastMonthDay = calendar.getTime();
		lastMonthDay.setDate(lastDayOfMonth);
		
		return lastMonthDay;
				
	}
	
	public static Date  getLastDayOfWeek(Date date)
	{
		 Calendar   calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		
		//当时间为周日的情况下，不需要加一周
		if(date.getDay() !=0){
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
		}

	
		Date  lastWeekDay = calendar.getTime();
		
		return lastWeekDay;
		
		
	}
	
	public static Date  getFirstDayOfWeek(Date date)
	{
		Calendar   calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		//当时间为周日的情况下，需要减少一周
		if(date.getDay() ==0){
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
			
		}
		return calendar.getTime();
		
		
	}
	
}
