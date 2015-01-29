package com.hand.hrmexp.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.hand.R;
import com.hand.hrmexp.adapter.CurrencyListViewAdapter;
import com.hand.hrmexp.model.CurrencyModel;
import com.hand.hrmexp.model.ExchangeRateModel;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.mas.customview.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CurrencyListActivity extends SherlockActivity implements LMModelDelegate{
	
	private ListView currencyListView;
	private List<CurrencyModel> dataList;
	private CurrencyListViewAdapter adapter;
	public int CURRENCY_CONTENT = 3;
	private ExchangeRateModel model;
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_currency_list);
		//加载ActionBar设置标题
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_titlebar));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.abs_layout);
		TextView titleView = (TextView) findViewById(R.id.contextTitle);
		titleView.setText("币种选择");
		TextView returnView = (TextView) findViewById(R.id.returnImage);
		returnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		ImageView addView = (ImageView) findViewById(R.id.addImage);	
		addView.setVisibility(View.INVISIBLE);
		dataList = new ArrayList<CurrencyModel>();
		model = new ExchangeRateModel(this);
		dialog = new ProgressDialog(this, "数据正在加载中，请稍后");
		dialog.show();		
		bindAllViews();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		model.load();
	};	
	
	private void bindAllViews(){
		currencyListView = (ListView) findViewById(R.id.currencyListView);
//		dataList.add(new CurrencyModel("CNY-人民币", "1.00"));
//		dataList.add(new CurrencyModel("USD-美元", "6.67"));
		
		currencyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				String currency = dataList.get(position).getCurrency();
				String exchangeRate = dataList.get(position).getExchangeRate();
				intent.putExtra("currency", currency);
				intent.putExtra("exchangeRate", exchangeRate);
				setResult(CURRENCY_CONTENT, intent);
				
				finish();				
			}
		});
	}
	/**
	 * 
	 * 初始化币种和汇率数据
	 * @throws JSONException 
	 * 
	 */
	private void initializeData(JSONArray jsonArr) throws JSONException{
		dataList.clear();
		int length = jsonArr.length();
		for(int i = 0;i < length; i+= 1){
			JSONObject data = new JSONObject();
			data = (JSONObject) jsonArr.get(i);	
			try {
				dataList.add(new CurrencyModel(data.getString("currency"), data.getString("exchange_rate")));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				continue;
			}
		}
	}
	
	
	@Override
	public void modelDidFinshLoad(LMModel model) {
		// TODO 自动生成的方法存根
		
		AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
		String json = new String(reponseModel.mresponseBody);
		try {
			JSONObject jsonObj = new JSONObject(json);
			String code = ((JSONObject) jsonObj.get("head")).get("code").toString();
			if(code.equals("success")){
				JSONArray listArr = (JSONArray) ((JSONObject) jsonObj.get("body")).get("list");
				try {
					initializeData(listArr);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				adapter = new CurrencyListViewAdapter(getApplicationContext(), dataList);
				currencyListView.setAdapter(adapter);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			dialog.dismiss();
		}
	}

	@Override
	public void modelDidStartLoad(LMModel model) {
		// TODO 自动生成的方法存根
	
	}

	@Override
	public void modelDidFaildLoadWithError(LMModel model) {
		// TODO 自动生成的方法存根
		
	}
}
