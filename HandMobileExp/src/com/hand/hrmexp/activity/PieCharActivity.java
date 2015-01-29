package com.hand.hrmexp.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hand.R;
import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_DATA;
import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_LINE;
import com.hand.hrmexp.model.PieCharModel;
import com.handexp.utl.ExpenseIdToDesc;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PieCharActivity extends SherlockActivity implements LMModelDelegate,OnClickListener{
	
	////view
	PieChart mChart;
	
	ImageView leftDateImageView;
	
	ImageView rightDateImageView;
	
	TextView dateTextView;
	
	//model	
	PieCharModel piecharmodel;

	//存放当前选择日期	
	String _date;
	Date   date;
	int  year;
	int month;
	
	 
	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
	 
	 
	 HashSet set;
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_pie_char);
		buildAllViews();
		
		//加载ActionBar设置标题
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_titlebar));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.abs_layout1);
		TextView titleView = (TextView) findViewById(R.id.contextTitle);
		titleView.setText("报销明细");
		
		TextView returnView = (TextView) findViewById(R.id.returnImage);
		returnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		
		piecharmodel = new PieCharModel(this);
		
		date= new Date();
		year = date.getYear();
		month = date.getMonth() ;
		_date =  df.format(date);
		dateTextView.setText((year + 1900) + " 年  " + (month+ 1) + " 月  ");
		piecharmodel.load(_date);
			
	}
	
	@Override
	protected void onResume() {
		super.onResume();

	}
	
	@Override
	public void onClick(View v) {
		if(v.equals(leftDateImageView)){
			month = month -1;
			if(month == -1){
				month = 11;
				year  = year -1;
			}
			date.setMonth(month);
			date.setYear(year);
			_date = df.format(date);
			piecharmodel.load(_date);
			dateTextView.setText((year+ 1900) + " 年  " + (month+ 1) + " 月  ");

		}else if(v.equals(rightDateImageView)){
			month = month +1;
			if(month == 12){
				month = 0;
				year = year +1;
				
			}
			date.setMonth(month);
			date.setYear(year);
			_date = df.format(date);
			piecharmodel.load(_date);
			
			dateTextView.setText((year+ 1900) + " 年  " + (month+ 1) + " 月  ");

		}
		
	}
	
	/////////////////////private/////////////////
	private void buildAllViews()
	{
		leftDateImageView = (ImageView) findViewById(R.id.leftDateImageView);
		leftDateImageView.setOnClickListener(this);
		
		
		rightDateImageView = (ImageView) findViewById(R.id.rightDateImageView);
		rightDateImageView.setOnClickListener(this);
		
		dateTextView = (TextView) findViewById(R.id.dateTextView);
		
		mChart = 	(PieChart) findViewById(R.id.piechat);
        mChart.setHoleRadius(60f);
        mChart.setDescription("");
        mChart.setDrawYValues(true);
        mChart.setDrawCenterText(true);
        mChart.setDrawHoleEnabled(true);      
        mChart.setRotationAngle(0);
        // draws the corresponding description value into the slice
        mChart.setDrawXValues(true);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);       
        // display percentage values
        mChart.setUsePercentValues(true);
        
        
        
		
	}
	

	@Override
	public void modelDidFinshLoad(LMModel model) {
		// TODO Auto-generated method stub
		if(piecharmodel.result.size() == 0){
	        mChart.setData(null);
	        mChart.invalidate();
			return;
		}
		
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
		
		//每次查询都重新初始化hashset
		set = new HashSet();
		//取出所有组，取出重复
	   for (  int i =0;  i< piecharmodel.result.size();i++){
		   
		   MOBILE_EXP_REPORT_DATA line =    (MOBILE_EXP_REPORT_DATA)piecharmodel.result.get(i);
		set.add(line.expense_class_id);
	   }
	   
	 Iterator iterator=   set.iterator() ;
	 int count=0;
	   while(iterator.hasNext()){
		   int expense_class_id =  (Integer) iterator.next();
		   String expense_class_desc = ExpenseIdToDesc.ExpenseClassIdToDesc(expense_class_id);
		   float amount = 0;
		   for(int i =0;  i< piecharmodel.result.size();i++){
			   MOBILE_EXP_REPORT_DATA line =    (MOBILE_EXP_REPORT_DATA)piecharmodel.result.get(i);
			   
				if(line.expense_class_id == expense_class_id){
					

					amount += line.total_amount;

					
				}
				
		   }
		   
		   
		   yVals1.add(new Entry(amount,count));
		   xVals.add(expense_class_desc + '(' +amount +')' );
		   
		   count++;
	   }
		
	        PieDataSet set1 = new PieDataSet(yVals1, "");
	        
	        set1.setSliceSpace(3f);
	        set1.setColors(ColorTemplate.createColors(this,
	                ColorTemplate.VORDIPLOM_COLORS));
	        
	        PieData data = new PieData(xVals, set1);
	        mChart.setData(data);

	        // undo all highlights
	        mChart.highlightValues(null);

	        // set a text for the chart center
	        mChart.setCenterText("Total Value\n" +  mChart.getYValueSum() + "\n(all slices)");
	        mChart.invalidate();
		
	}

	@Override
	public void modelDidStartLoad(LMModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modelDidFaildLoadWithError(LMModel model) {
		// TODO Auto-generated method stub
		
	}


	
	

	
	
	
}
