package com.hand.hrmexp.dialogs;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerWrapDialog {
	
	public  Calendar c = Calendar.getInstance();
	
	//TODO 这里的yyyy-MM-dd 不能写成yyyy-mm-dd
	protected  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public int  year;
	
	public int month;
	
	public int dayOfMonth;
	
	private Context context;
	
	private TextView setView;
	
	public DatePickerWrapDialog(Context context, TextView setView)
	{
		this.context = context;
		this.setView = setView;
		
		getNumber();
		setView();
	}
	
//根据当前年月日更新前台显示	
	public void setView()
	{
		//android月份从0开始很蛋疼
		int _month = month +1 ;
		String month_ = String.format("%d",_month);
		String  dayOfMonth_ = String.format("%d", dayOfMonth);
		//为了满足yyyy-mm-dd的要求,对于9以下的月份要拼接成 09 08 类型
		if(_month <=9){
			
			month_ = "0" + month_;
		}
		
		if(dayOfMonth <= 9){
			
			dayOfMonth_ = "0" + dayOfMonth_;
		}
		
		
		setView.setText(year + "-" + month_  + "-" + dayOfMonth_);
		
	}
	
	
	public void setDate(String date)
	{
		try {
			Date _date = (Date) sdf.parse(date);
		
			int month = _date.getMonth();
			c.setTime(_date);
			getNumber();
			setView();
			
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
		
	}
	
	
	public void getNumber()
	{
		year = c.get(Calendar.YEAR);
		
		month = c.get(Calendar.MONTH);
		dayOfMonth =  c.get(Calendar.DAY_OF_MONTH);
		
	}

	//日期对话框
	public  void showDateDialog()
	{
	
		new DatePickerDialog(context,
			// 绑定监听器
			new DatePickerDialog.OnDateSetListener()
			{
				@Override
				public void onDateSet(DatePicker dp, int year,
					int month, int dayOfMonth)
				{
				     
					DatePickerWrapDialog.this.year= year;
					DatePickerWrapDialog.this.month = month ;
					DatePickerWrapDialog.this.dayOfMonth = dayOfMonth;
					
					setView();
				}
			}
		//设置初始日期
		, year
		, month
		, dayOfMonth).show();
		
		
	}
	
}
