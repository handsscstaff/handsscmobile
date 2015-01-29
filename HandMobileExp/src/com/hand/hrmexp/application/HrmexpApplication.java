package com.hand.hrmexp.application;


import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.hand.R;
import com.hand.hrms4android.parser.ConfigReader;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.db.sqlite.FinalDb;
import com.littlemvc.model.request.db.DbRequestModel;
import com.littlemvc.utl.AsNetWorkUtl;
import com.loopj.android.http.AsyncHttpClient;

import android.app.Application;

public class HrmexpApplication extends Application implements BDLocationListener{
	private static 	HrmexpApplication instance;
	public android.support.v4.app.FragmentTransaction  transaction;
	
	
	public  FinalDb finalDb ;
	
	
	public LocationClient mLocationClient;
	
	
	//reader
	public ConfigReader reader;
	
	public static HrmexpApplication getApplication(){
		return instance;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		finalDb = FinalDb.create(this);
		
		DbRequestModel.finalDb = finalDb;
		
		//由于添加了缓存的问题，所以这里必须获得上下文
		AsNetWorkUtl.application = this;
		locationInit();
		JPushInterface.setDebugMode(true);
		

		JPushInterface.init(this);

		
	}
	

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
		//System.out.println(location.getProvince());
		//System.out.println(location.getCity());
	} 
	
	}
	
///////////////////////private/////////////
	
////初始化定位
	private void locationInit()
	{
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
		option.setIsNeedAddress(true);
		option.setScanSpan(1000*60*1);//设置发起定位请求的间隔时间为5000ms		
		 mLocationClient = new LocationClient(this);
		 mLocationClient.registerLocationListener(this); 
		 mLocationClient.setLocOption(option);	 
		 mLocationClient.start();
		 
	}

	
	
	
}
