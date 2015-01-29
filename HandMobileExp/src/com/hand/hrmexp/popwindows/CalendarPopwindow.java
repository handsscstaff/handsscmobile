//package com.hand.hrmexp.popwindows;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import com.andexert.calendarlistview.library.DayPickerView;
//import com.hand.R;
//
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//public class CalendarPopwindow extends PopupWindow implements com.andexert.calendarlistview.library.DatePickerController{
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
//	public int clickTime ;//点击次数，每次余2来判断是点击
//	
//	 private DayPickerView dayPickerView;
//	public CalendarPopwindow(Context context ,TextView date){
//		 
//		this.context = context;
//		this.date = date;
//		
//		clickTime = 0;
//		 
//		LayoutInflater inflater = (LayoutInflater) context  
//	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		popwindow_calendar = inflater.inflate(R.layout.popwindow_calendar, null);
//		  
//		
//        dayPickerView = (DayPickerView) popwindow_calendar.findViewById(R.id.pickerView);
//        dayPickerView.setmController(this);
//        
////        returnBtn =  (ImageButton) popwindow_calendar.findViewById(R.id.return_btn);
////        returnBtn.setOnClickListener(new View.OnClickListener() {
////			
////			@Override
////			public void onClick(View v) {
////				
////				CalendarPicker.this.dismiss();
////			}
////		});
//        
//		 setContentView(popwindow_calendar);
//		 setWidth(LayoutParams.WRAP_CONTENT);
//		 setHeight(LayoutParams.WRAP_CONTENT);
//
//					
//
//	}
//
//	
//    @Override
//    public int getMaxYear()
//    {
//        return 2015;
//    }
//
//    @Override
//    public void onDayOfMonthSelected(int year, int month, int day)
//    {
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
//    		CalendarPopwindow.this.dismiss();
//    		dateto = datetoTmp;
//    		datefrom =  datefromTmp;
//    		
//    		datetoTmp = null;
//    		datefromTmp = null;
//    				
//    		date.setText(datefrom + " - " + dateto );
//    		
//    	}
//    	
//    	clickTime ++ ;
//        Log.e("Day Slected", day + " / " + month + " / " + year);
//    }
//
//	
//}
