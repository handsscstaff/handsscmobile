package com.hand.hrmexp.adapter;

import java.util.List;

import com.hand.R;
import com.hand.hrmexp.model.CurrencyModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CurrencyListViewAdapter extends BaseAdapter{
	
	private Context context;
	private List<CurrencyModel> dataList;
	
	public CurrencyListViewAdapter(Context context, List<CurrencyModel> dataList){
		this.context = context;
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_currency_detail, null);
		}
		CurrencyModel itemInfo = dataList.get(position);
		String currencyString = itemInfo.getCurrency();
		String exchangeRateString = itemInfo.getExchangeRate();
		TextView currencyTextView = (TextView) convertView.findViewById(R.id.currency);
		TextView exchangeRateTextView = (TextView) convertView.findViewById(R.id.exchangeRate);
		currencyTextView.setText(currencyString);
		exchangeRateTextView.setText(exchangeRateString);
		return convertView;
	}

}
