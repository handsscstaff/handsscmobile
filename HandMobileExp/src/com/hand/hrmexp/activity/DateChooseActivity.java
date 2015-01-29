//package com.hand.hrmexp.activity;
//
//import com.andexert.calendarlistview.library.DayPickerView;
//import com.hand.hrmexp.popwindows.CalendarPopwindow;
//
//import android.R;
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//public class DateChooseActivity extends Activity implements com.andexert.calendarlistview.library.DatePickerController{
//
//	
//	public Context context;
//	public TextView date;
//	public View popwindow_calendar;
//	public ImageButton returnBtn;
//	
//	//存放选择的日期
//	public String dateto;
//	public String datefrom;
//	
//	//存放临时选择的日期
//	public String  datetoTmp;
//	public String  datefromTmp;
//	
//	
//	public int clickTime ;//点击次数，每次余2来判断是点击
//	
//	 private DayPickerView dayPickerView;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(com.hand.R.layout.popwindow_calendar);
//		
//		
//		
//	}
//	
//	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//	}
//
//
//	@Override
//	public int getMaxYear() {
//		// TODO Auto-generated method stub
//		return 2015;
//	}
//
//
//	@Override
//	public void onDayOfMonthSelected(int year, int month, int day) {
//		// TODO Auto-generated method stub
//    	//bug  月份少一天
//    	month += 1;
//    	
//    	if(clickTime %2 == 0){
//    		
//    		datetoTmp =  year  + "-" + month + "-" + day;
//    	}else{
//    		
//    		datefromTmp =  year  + "-" + month + "-" + day;
//    		
//    	}
//    	
//    	if(datetoTmp !=null && datefromTmp !=null){
//    		      
//    		if(datetoTmp.compareTo(datefromTmp) < 0){
//    			String tmp = datetoTmp;
//    			datetoTmp = datefromTmp;
//    			datefromTmp = tmp;
//    		}
//    		
//    		
//    		dateto = datetoTmp;
//    		datefrom =  datefromTmp;
//    		
//    		datetoTmp = null;
//    		datefromTmp = null;
//    				
//    		date.setText(datefrom + " - " + dateto );
//    		
//    		finish();
//    		
//    	}
//    	
//    	clickTime ++ ;
//        Log.e("Day Slected", day + " / " + month + " / " + year);
//    }
//	
//	
//}
