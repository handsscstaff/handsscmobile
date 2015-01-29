package com.hand.hrmexp.activity;

import java.io.ByteArrayOutputStream;

import java.io.InputStream;
import java.util.List;

import ui.custom.component.NumberText;

import com.hand.R;
import com.hand.hrmexp.application.HrmexpApplication;
import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_LINE;
import com.hand.hrmexp.dialogs.DatePickerWrapDialog;
import com.hand.hrmexp.popwindows.ExpenseTypePopwindow;
import com.handexp.utl.DialogUtl;
import com.littlemvc.db.sqlite.FinalDb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View.OnClickListener;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类
//如果使用地理围栏功能，需要import如下类
import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
import com.baidu.location.GeofenceClient.OnRemoveBDGeofencesResultListener;
import com.baidu.location.LocationClientOption.LocationMode;

public class location_of_DetailLineActivity extends Activity  implements View.OnClickListener{


	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
	            return ;
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation){
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			System.out.println(location.getProvince());
			System.out.println(location.getCity());
		} 

		System.out.println(sb.toString());

		

			
		}
		
		
		
	
	};
	
	
	//费用类型
	private LinearLayout expenseTypell;	
	
	private TextView expenseTypeTextView;
	
	private ExpenseTypePopwindow expenseTypePicker;
	
	//地点
	private LinearLayout placell;	
	//日期
	private LinearLayout datell;
	
	private TextView dateToTextView;
	
	private TextView dateFromTextView;
	
	private DatePickerWrapDialog dateToDateDialog;
	
	private DatePickerWrapDialog  dateFromDateDialog;
	//返回
	private ImageButton returnImgBtn;	
	//拍照
	private ImageView photoImgView;	
	//备注 
	private EditText commentEditText;	
	
	//数量
	private EditText quantityEditText;	
	//单价
	private ui.custom.component.NumberText priceNumerText;	
	//总金额
	private TextView amountTextView;
	
	//保存按钮
	private Button saveBtn;
	
	//根view
	private View rootView;
	
	//数据库
	private FinalDb finalDb;

	private Boolean flag;

	
	//拍照
	public static final  int IMAGE_CAPTURE = 0;
	
	//相册
	public static final int ACTION_GET_CONTENT = 1;

	

	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="gcj02";
		 
		 
		 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		option.setIsNeedAddress(true);
		int span=1000;

		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms

		
		 mLocationClient = new LocationClient(HrmexpApplication.getApplication());
		 mLocationClient.registerLocationListener( myListener ); 
		 mLocationClient.setLocOption(option);
		 
		 mLocationClient.start();
		setContentView(R.layout.activity_detail_line);

		buildAllviews();
	}

	@Override 
	public void onResume() {
		super.onResume();

	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
			case IMAGE_CAPTURE:
				Bundle extras = data.getExtras();
				Bitmap bitmap = (Bitmap) extras.get("data");
				
				
				break;
			case ACTION_GET_CONTENT:
				break;
		
		
		
		}
		
		
		
	}
	


// ///////////////private //////////////////////////////////
	private void buildAllviews() {
		
		rootView = findViewById(R.id.detail_line_id);
		
		//费用类型		
		expenseTypell = (LinearLayout) findViewById(R.id.expense_type);
		expenseTypell.setOnClickListener(this);
		expenseTypeTextView = (TextView) findViewById(R.id.detailTypeLabel);
//		expenseTypePicker  = new ExpenseTypePopwindow(this, expenseTypeTextView);
		
		
		//日期
		datell  = (LinearLayout) findViewById(R.id.llcalendar_id);
		
		dateToTextView  = (TextView) findViewById(R.id.dateToTextView);
		dateToTextView.setOnClickListener(this);
		dateFromTextView = (TextView)findViewById(R.id.dateFromTextView);
		dateFromTextView.setOnClickListener(this);
		
		
		dateToDateDialog = new DatePickerWrapDialog(this, dateToTextView);
		
		dateFromDateDialog  = new DatePickerWrapDialog(this,dateFromTextView);
		//照相
		
		photoImgView = (ImageView) findViewById(R.id.cameraImageView);
		photoImgView.setOnClickListener(this);
		
		
		new ExpenseTypePopwindow(this, expenseTypeTextView);
		
		//数量单价总金额
		quantityEditText = (EditText) findViewById(R.id.quantityEditText);	
		quantityEditText.addTextChangedListener(amountTextWatcher);
		
		priceNumerText = (NumberText) findViewById(R.id.priceNumberText);
		priceNumerText.addTextChangedListener(amountTextWatcher);
		
		amountTextView = (TextView) findViewById(R.id.amountTextView);
		
		
		//保存
		saveBtn = (Button) findViewById(R.id.save_button);
		saveBtn.setOnClickListener(this);
		

	}

	//保存逻辑
	private  void save()
	{
	     String priceNumber = 	priceNumerText.getText().toString();
		
		if( priceNumber.equalsIgnoreCase("") || priceNumber.equalsIgnoreCase("0") ){
			
			
		}
		
		
		
		
		
	}
	
//////////////////////click////////////////////////////
	@Override
	public void onClick(View v) {
		
		//点击费用类型，弹出picker选择
		if(v.equals(expenseTypell)){
			expenseTypePicker.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
			
		}else if(v.equals(photoImgView)){
			//拍照逻辑
			final CharSequence[] items =
			{ "相册", "拍照" };
			AlertDialog dlg = new AlertDialog.Builder(location_of_DetailLineActivity.this).setTitle("选择图片").setItems(items,
					new DialogInterface.OnClickListener()
					{
						public void onClick ( DialogInterface dialog , int item )
						{
							// 这里item是根据选择的方式，
							// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
							if (item == 1)
							{
								Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
								startActivityForResult(getImageByCamera, IMAGE_CAPTURE);
							} else
							{
								Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
								getImage.addCategory(Intent.CATEGORY_OPENABLE);
								getImage.setType("image/jpeg");
								startActivityForResult(getImage, ACTION_GET_CONTENT);
							}
						}
					}).create();
			dlg.show();
		}else if(v.equals(saveBtn)){
			//保存按钮
			save();
			
		}else if(v.equals(dateToTextView)){
			dateToDateDialog.showDateDialog();

		}else if(v.equals(dateFromTextView)){
			
			dateFromDateDialog.showDateDialog();
			
		}
		
	}

	
/////////////////////////////text watch/////////////////////////
	//当数量金额变化后动态改变总金额
	TextWatcher amountTextWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			
			
			//TODO 当两个输入框体没有任何值的时候，这些值为"",而不是Null
			float priceNumber , amount ;
			int  quantity;

			//当还没输入值的时候  ，使用默认值
			if(priceNumerText.getText().toString().equalsIgnoreCase("")){
				
				priceNumber = 0;
			}else{
				 priceNumber =  	Float.parseFloat(priceNumerText.getText().toString());
				
			}
			
			if(quantityEditText.getText().toString().equalsIgnoreCase("")){
				
				quantity =1;
			       
			}else{
				quantity = Integer.parseInt(quantityEditText.getText().toString());
			}
			


		      amount = priceNumber * 	quantity;
		     
		     amountTextView.setText(String.format("%.2f",amount));
				
			
			
		}
		
		
	 };
}
