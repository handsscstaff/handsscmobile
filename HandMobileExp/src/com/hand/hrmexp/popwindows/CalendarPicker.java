//package com.hand.hrmexp.popwindows;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import com.hand.R;
//import com.wt.calendarcard.CalendarCardPager;
//import com.wt.calendarcard.CardGridItem;
//import com.wt.calendarcard.CheckableLayout;
//import com.wt.calendarcard.OnCellItemClick;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//public class CalendarPicker extends PopupWindow{
//
//	public Context context;
//	public TextView date;
//	public View popwindow_calendar;
//	public com.wt.calendarcard.CalendarCardPager pager;
//	public CardGridItem cardGridItemfrom;
//	public CardGridItem cardGridItemto;
//	public CheckableLayout cardGridItemfromV;
//	public CheckableLayout cardGridItemtoV;
//	
//	public String dateto;
//	public String datefrom;
//	
//	public CalendarPicker(Context context ,TextView date){
//		 
//		this.context = context;
//		this.date = date;
//		 
//		LayoutInflater inflater = (LayoutInflater) context  
//	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		popwindow_calendar = inflater.inflate(R.layout.popwindow_calendar, null);
//		  
//		 setContentView(popwindow_calendar);
//		 setWidth(LayoutParams.WRAP_CONTENT);
//		 setHeight(LayoutParams.WRAP_CONTENT);
//		 pager.setOnCellItemClick(new OnCellItemClick() {
//				@Override
//				public void onCellClick(View v, CardGridItem item) {
//					if(cardGridItemto !=null && cardGridItemfrom !=null){
//						
//						cardGridItemfromV.setChecked(false);
//						cardGridItemtoV.setChecked(false);
//						cardGridItemfromV = null;
//						cardGridItemtoV = null;
//						cardGridItemto = null;
//						cardGridItemfrom = null;
//					}
//					if(cardGridItemfrom == null){
//						cardGridItemfrom = item;
//						cardGridItemfromV = (CheckableLayout) v;
//						if(cardGridItemto !=null && cardGridItemfrom !=null){
//							goBack();
//						}
//						return ;
//					}
//					
//					if(cardGridItemto == null){
//						cardGridItemto = item;
//						cardGridItemtoV  =  (CheckableLayout) v;
//						if(cardGridItemto !=null && cardGridItemfrom !=null){
//							goBack();
//						}
//						return ;
//					}
//					
//
////					mTextView.setText(getResources().getString(R.string.sel_date, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(item.getDate().getTime())));
//				}
//			});
//	}
//	
//	
//	private void goBack()
//	{	
//		Date datefrom1 = cardGridItemfrom.getDate().getTime();
//		Date dateto1 = cardGridItemto.getDate().getTime();
//		
//		if( dateto1.compareTo(datefrom1) <0){
//			CardGridItem tmp ;
//			tmp = cardGridItemfrom;
//			cardGridItemfrom = cardGridItemto;
//			
//			cardGridItemto = tmp;
//		}
//		
////		date.compareTo(date)
//		this.datefrom = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cardGridItemfrom.getDate().getTime());
//		this.dateto = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cardGridItemto.getDate().getTime());
//		
//		date.setText(datefrom + " - " + dateto );
//		CalendarPicker.this.dismiss();
//	}
//	
//}
