package com.hand.hrmexp.model;

import java.util.Date;

import android.database.Cursor;

import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_LINE;
import com.handexp.utl.DateUtl;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.littlemvc.model.request.db.DbRequestModel;

public class HomeModel extends DbRequestModel{
	
	
	public String  firstDateOfWeek;
	
	public String  lastDateOfWeek;
	
	public String  firstDateOfMonth;
	
	public String  LastDateOfMonth;
	
	public String  nowDate;
	
	
	/////特殊处理，直接访问三个汇总金额
	public String weekAmount;
	public String monthAmount;
	public String todayAmount;

	public HomeModel(LMModelDelegate delegate) {
		super(delegate);
		Date  currentDate = 	new Date();
		firstDateOfWeek =  DateUtl.getFirstDayOfWeekS(currentDate);
		lastDateOfWeek = DateUtl.getLastDayOfWeekS(currentDate);
		
		firstDateOfMonth = DateUtl.getFirstDayOfMonthS(currentDate);
		LastDateOfMonth = DateUtl.getLastDayOfMonthS(currentDate);
		
		nowDate = DateUtl.getCurrentDate();
		
			
	}
	
	public void load()
	{
		//TODO 注意sum出来的结果是string类型 不能getint
		this.finalDb.checkTableExist(MOBILE_EXP_REPORT_LINE.class);
		
		Cursor currentDay = 	this.finalDb.db.rawQuery("select sum(total_amount) from  MOBILE_EXP_REPORT_LINE where expense_date =  ?"  
				
				 ,new String[]{nowDate});
		while(currentDay.moveToNext()){
			
			todayAmount = currentDay.getString(0);

			
		}
		
		Cursor weekDay  =  this.finalDb.db.rawQuery("select sum(total_amount) from  MOBILE_EXP_REPORT_LINE where expense_date >= ? and expense_date <= ?", new String[]{firstDateOfWeek,lastDateOfWeek} );
		
		while(weekDay.moveToNext()){
			
			weekAmount = weekDay.getString(0);

			
		}
		
		
		Cursor monthDay = this.finalDb.db.rawQuery("select sum(total_amount) from  MOBILE_EXP_REPORT_LINE where expense_date >= ? and expense_date <= ?",  new String[]{firstDateOfMonth,LastDateOfMonth});
		
		while(monthDay.moveToNext()){
			
			monthAmount = monthDay.getString(0);

			
		}
		
		modelDelegate.modelDidFinshLoad(this);
	}

}
