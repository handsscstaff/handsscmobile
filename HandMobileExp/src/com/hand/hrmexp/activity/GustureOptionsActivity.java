package com.hand.hrmexp.activity;

import ui.custom.component.SwitchButton;
import ui.custom.component.SwitchButton.OnChangeListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.hand.R;

public class GustureOptionsActivity extends SherlockActivity{
	
	private LinearLayout setLockLL; 
	private SwitchButton switchButtonView;
	private TextView returnImgView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gustureoption);
		//加载ActionBar
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_titlebar));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.abs_layout);
		TextView titleView = (TextView) findViewById(R.id.contextTitle);
		titleView.setVisibility(View.INVISIBLE);
		ImageView addView = (ImageView) findViewById(R.id.addImage);
		addView.setVisibility(View.INVISIBLE);
		bindAllViews();
	}

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.move_left_in_activity,R.anim.move_right_out_activity);
	}
	
	private void bindAllViews(){
		setLockLL = (LinearLayout) findViewById(R.id.setLock);
		setLockLL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(GustureOptionsActivity.this,GustureLockActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
				
			}
		});
		switchButtonView = (SwitchButton) findViewById(R.id.swithButton);
		SharedPreferences preferences=getSharedPreferences("gustureLock",Context.MODE_APPEND);
		String enabledFlag=preferences.getString("enabledFlag", "null");
//		Toast.makeText(getApplicationContext(), enabledFlag, Toast.LENGTH_SHORT).show();
		if(enabledFlag == "True"){
			switchButtonView.initStatus(true);
		}else{
			switchButtonView.initStatus(false);
		}
		
		switchButtonView.setOnChangeListener(new OnChangeListener() {
			
			@Override
			public void onChange(SwitchButton sb, boolean state) {
				// TODO 自动生成的方法存根
				String enabledFlag = null;
				enabledFlag = sb.getSwitchStatus() ? "True" : "False";
				SharedPreferences preferences = getSharedPreferences("gustureLock",MODE_APPEND);
				preferences.edit().putString("enabledFlag",enabledFlag).commit();
//				Toast.makeText(getApplicationContext(), enabledFlag, Toast.LENGTH_SHORT).show();
				
			}
		});
		returnImgView = (TextView) findViewById(R.id.returnImage);
		returnImgView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
				overridePendingTransition(R.anim.move_left_in_activity,R.anim.move_right_out_activity);
			}
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
