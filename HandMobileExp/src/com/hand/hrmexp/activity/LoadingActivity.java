/**
 * 
 */
package com.hand.hrmexp.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.hand.R;
import com.hand.hrmexp.model.ExpenseTypeModel;
import com.hand.hrmexp.model.LoadingModel;
import com.actionbarsherlock.app.SherlockActivity;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.littlemvc.utl.AsNetWorkUtl;

import com.hand.hrmexp.activity.SettingsActivity;
import com.hand.hrmexp.application.HrmexpApplication;
import com.hand.hrms4android.exception.ParseException;
import com.hand.hrms4android.parser.ConfigReader;
import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.handexp.utl.ConstantsUtl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jiang titeng
 * 
 *         All right reserve 
 */
public class LoadingActivity extends SherlockActivity implements
		LMModelDelegate {  
  
	private SharedPreferences mPreferences;
	private String baseUrl;

	private LoadingModel model;

	private ExpenseTypeModel expenseModel;

	private Button reloadButton;
	private TextView informationTextView;
	private ImageView alertImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_loading);

		buildAllviews();
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		baseUrl = mPreferences.getString("sys_basic_url", "");

		model = new LoadingModel(this);



		if (!checkBaseUrl(baseUrl)) {

			startSettingsActivity();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		baseUrl = mPreferences.getString("sys_basic_url", "");

		if (checkBaseUrl(baseUrl)) {

			// 初始化网络工具，设置基础url
			AsHttpRequestModel.utl = AsNetWorkUtl.getAsNetWorkUtl(baseUrl);

			doReload();
		}

	}

	@Override
	protected void onDestroy() {
		System.out.println("destroy");
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		if (item.getItemId() == R.id.menu_settings) {
			startSettingsActivity();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSherlock().getMenuInflater().inflate(R.menu.activity_loading, menu);
		return true;
	}

	// ////////////////////////private///////////////////////

	private void buildAllviews() {
		informationTextView = (TextView) findViewById(R.id.activity_loading_infomation);
		reloadButton = (Button) findViewById(R.id.activity_loading_reload_button);
		alertImage = (ImageView) findViewById(R.id.activity_loading_alert);

	}

	/**
	 * 重新读取配置文件
	 */
	public void doReload() {

		setViewAsNew();
		// 同步费用类型
		this.model.load();

	}

	/**
	 * 
	 * 检查url是否正确
	 */
	private boolean checkBaseUrl(String url) {
		if (url == null) {
			return false;
		}

		if (url.length() == 0) {
			return false;
		}

		if (url.equals("http://")) {
			return false;
		}

		return true;
	}

	/**
	 * 设置刷新按钮，提示框不可见
	 */

	private void setViewAsNew() {
		informationTextView.setText(R.string.activity_loading_text);
		reloadButton.setVisibility(View.INVISIBLE);
		alertImage.setVisibility(View.INVISIBLE);
	}

	private void startSettingsActivity() {
		Intent i = new Intent(this, SettingsActivity.class);
		startActivity(i);

	}

	private void startLoginActivity() {
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
		finish();
	}

	private void startGustureActivity() {
		Intent i = new Intent(this, GustureUnlockActivity.class);
		startActivity(i);
		finish();
	}

	// ////////////////////////// model delegate //////////////////////

	@Override
	public void modelDidFinshLoad(LMModel model) {
		// TODO Auto-generated method stub

		//
		// startLoginActivity();
		if (model.equals(this.model)) {

			File dir = HrmexpApplication.getApplication().getDir(
					ConstantsUtl.SYS_PREFRENCES_CONFIG_FILE_DIR_NAME,
					Context.MODE_PRIVATE);

			File configFile = new File(dir, ConstantsUtl.configFile);

			FileOutputStream fileOutputStream = null;

			try {
				fileOutputStream = new FileOutputStream(configFile);

				fileOutputStream.write(this.model.mresponseBody);

				HrmexpApplication.getApplication().reader = XmlConfigReader.getInstance();
				HrmexpApplication.getApplication().reader.getAttr(new Expression("/backend-config", ""));
				

				
				fileOutputStream.close();

			} catch (Exception ex) {

				Toast.makeText(this, "读写配置文件出现错误", Toast.LENGTH_SHORT).show();

				return;

			}
			
			expenseModel = new ExpenseTypeModel(this);
			expenseModel.load(null);
		} else if (model.equals(this.expenseModel)) {

			Editor editor = mPreferences.edit();
			try {
				editor.putString(ConstantsUtl.expenseType, new String(
						expenseModel.mresponseBody, "UTF-8"));

			} catch (UnsupportedEncodingException e) {
				Toast.makeText(this, "费用类型同步失败", Toast.LENGTH_SHORT).show();

				e.printStackTrace();
				return;
			}

			editor.commit();

			if (isEnabledLock()) {
//				Toast.makeText(getApplicationContext(), "True",
//						Toast.LENGTH_SHORT).show();
				startGustureActivity();
				finish();
			} else {
//				Toast.makeText(getApplicationContext(), "False",
//						Toast.LENGTH_SHORT).show();
				startLoginActivity();
			}

		}

	}

	@Override
	public void modelDidStartLoad(LMModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelDidFaildLoadWithError(LMModel model) {
		// TODO Auto-generated method stub
		if (model.equals(this.model)) {
			Toast.makeText(this, "请求配置文件出现错误", Toast.LENGTH_SHORT).show();

		} else if (model.equals(this.expenseModel)) {
			Toast.makeText(this, "同步费用类型失败", Toast.LENGTH_SHORT).show();

		}
	}

	/**
	 * 检查是否已经设置手势密码并启用手势密码
	 * 
	 * @return
	 */

	private Boolean isEnabledLock() {
		SharedPreferences preferences = getSharedPreferences("gustureLock",
				Context.MODE_APPEND);
		String enabledFlag = preferences.getString("enabledFlag", "False");
		String choosePattern = preferences.getString("choosePattern", "False");
		String userName = preferences.getString("userName", "False");
		String userPassword = preferences.getString("userPassword", "False");
		// 检查是否都存在
		if (!enabledFlag.endsWith("False")  && !choosePattern.endsWith("False")
				&& !userName.endsWith("False") && !userPassword.endsWith("False")) {
			return true;
		} else {
			return false;
		}
	}

}
