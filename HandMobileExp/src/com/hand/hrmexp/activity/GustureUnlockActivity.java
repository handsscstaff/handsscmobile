package com.hand.hrmexp.activity;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.hand.hrmexp.activity.LockPatternView.Cell;
import com.hand.hrmexp.activity.LockPatternView.OnPatternListener;
import com.hand.hrmexp.model.LoginModel;

import com.hand.R;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.littlemvc.utl.AsNetWorkUtl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class GustureUnlockActivity extends Activity implements
		OnPatternListener, LMModelDelegate {

	private LockPatternView unlockPatternView;
	private List<LockPatternView.Cell> lockPattern;
	private TextView reLoginView;

	private LoginModel model;
	private HashMap<String, String> loginParm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 取得所存的手势
		SharedPreferences preferences = getSharedPreferences("gustureLock",
				Context.MODE_APPEND);
		String patternString = preferences.getString("choosePattern", null);
		// 木有就返回
		if (patternString == null) {
			finish();
			return;
		}
		lockPattern = LockPatternView.stringToPattern(patternString);

		setContentView(R.layout.activity_unlock);
		unlockPatternView = (LockPatternView) findViewById(R.id.unlockPattern);
		unlockPatternView.setOnPatternListener(this);
		reLoginView = (TextView) findViewById(R.id.reLogin);
		reLoginView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				startLoginActivity();
			}
		});

		model = new LoginModel(this);
		loginParm = new HashMap<String, String>();

	}

	/**
	 * 生成登录参数
	 */
	private void generateParm() {
		SharedPreferences preferences = getSharedPreferences("gustureLock",
				Context.MODE_APPEND);
		String userName = preferences.getString("userName", "False");
		String userPassword = preferences.getString("userPassword", "False");		

		loginParm.put("user_name", userName);
		loginParm.put("user_password", userPassword);
		loginParm.put("device_type", "Android");
		loginParm.put("push_token", "-1");
		loginParm.put("device_id",
				((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
						.getDeviceId());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPatternStart() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onPatternCleared() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		// TODO 自动生成的方法存根
		if (pattern.equals(lockPattern)) {
			Toast.makeText(getApplicationContext(), "解锁成功", Toast.LENGTH_SHORT).show();
			generateParm();
			model.load(loginParm);
			// startMenuActivity();
//			finish();
		} else {
			unlockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
		}
	}

	/**
	 * 
	 * 打开Menu页面
	 * 
	 */
	private void startMenuActivity() {
		Intent i = new Intent(this, MenuActivity.class);
		startActivity(i);
		finish();
	}
	
	/**
	 * 
	 * 回到Login页面登录其他用户
	 * 
	 */
	private void startLoginActivity() {
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
		finish();
	}	
	
	@Override
	public void modelDidFinshLoad(LMModel model) {
		// TODO Auto-generated method stub

		AsHttpRequestModel reponseModel = (AsHttpRequestModel)model;
		String json = new String(reponseModel.mresponseBody);
		try {
			JSONObject jsonobj = new JSONObject(json);
			String code = ((JSONObject) jsonobj.get("head")).get("code").toString();
			if(code.equals("ok")){
				String token = ((JSONObject)jsonobj.get("body")).get("token").toString();
				
				/////////将返回的token加到工具头中
				storeSomething(token);
				AsNetWorkUtl.addHeader("token", token);
				//存登录信息
				startMenuActivity();
			}else if (code.equals("failure")) {
				Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "网络繁忙请稍后再试", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			
		} finally{
			
		};		
	}

	@Override
	public void modelDidStartLoad(LMModel model) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void modelDidFaildLoadWithError(LMModel model) {
		// TODO 自动生成的方法存根

	}
	
	public void storeSomething(String token) throws JSONException {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();

		try {

				editor.putString("token",token);

		} finally {
			editor.commit();
		}

	}
	
}
