package com.hand.hrmexp.activity;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.hand.R;
import com.hand.hrmexp.model.LoginModel;
import com.handexp.utl.ConstantsUtl;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.littlemvc.utl.AsNetWorkUtl;
import com.mas.customview.ProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class LoginActivity extends Activity implements LMModelDelegate {
	
	EditText usernameEditText;
	EditText passwordEditText;
	private ProgressDialog dialog;
	
	Button loginButton;
	
	LoginModel model;
	
	HashMap<String, String> loginParm;
	SharedPreferences preferences;
	Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		buildAllViews();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		model = new LoginModel(this);
		loginParm = new HashMap<String, String>();
		dialog = new ProgressDialog(this, "登录中,请稍后。");
		
		
	}

	
///////////////////////////////buildAllViews/////////////////////////
	private void buildAllViews(){
		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.loginButton);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(usernameEditText.getText().toString().equals("")){
					Toast.makeText(LoginActivity.this, usernameEditText.getHint(), Toast.LENGTH_SHORT).show();
					return;
				};
				if(passwordEditText.getText().toString().equals("")){
					Toast.makeText(LoginActivity.this, passwordEditText.getHint(), Toast.LENGTH_LONG).show();
					return;
				};
				generateParm();
				model.load(loginParm);
				
			}
		});
	}
	
/**
 * 生成登录参数 	
 */
	private void generateParm(){
		
		loginParm.put("user_name", usernameEditText.getText().toString());
		loginParm.put("user_password", passwordEditText.getText().toString());
		loginParm.put("device_type", "Android");
		try {
			editor.putString(ConstantsUtl.SYS_PREFRENCES_PUSH_TOKEN, JPushInterface.getRegistrationID(this.getApplicationContext()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			editor.commit();
		}
		String token = preferences.getString(ConstantsUtl.SYS_PREFRENCES_PUSH_TOKEN, "");
		if (token.length() != 0) {
			loginParm.put("push_token", token);
		}else{
			loginParm.put("push_token", "-1");
		}
		loginParm.put("device_id",  ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId());	
	}
	
	public void storeSomething(String token) throws JSONException {

		try {
				
				editor.putString("token",token);

		} finally {
			editor.commit();
		}

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
				saveUserData();
				Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
				startActivity(intent);
				finish();
			}else if (code.equals("failure")) {
				Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(LoginActivity.this, "网络繁忙请稍后再试", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			
		} finally{
			dialog.dismiss();
		};		
	}

	@Override
	public void modelDidStartLoad(LMModel model) {
		// TODO Auto-generated method stub
		dialog.show();
				
	}

	@Override
	public void modelDidFaildLoadWithError(LMModel model) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		Toast.makeText(LoginActivity.this, "网络繁忙请稍后再试", Toast.LENGTH_LONG).show();
	}
	
	private void saveUserData() {
		SharedPreferences preferences=getSharedPreferences("gustureLock",Context.MODE_APPEND);
//		Toast.makeText(getApplicationContext(), "userName:"+loginParm.get("user_name"),
//				Toast.LENGTH_SHORT).show();
//		Toast.makeText(getApplicationContext(), "userPassword:"+loginParm.get("user_password"),
//				Toast.LENGTH_SHORT).show();
		String userName = loginParm.get("user_name");
		String userPassword = loginParm.get("user_password");
		preferences.edit().putString("userName",userName).commit();
		preferences.edit().putString("userPassword",userPassword).commit();
	}
}
