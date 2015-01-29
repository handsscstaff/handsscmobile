package com.hand.hrmexp.adapter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import com.hand.R;
import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_DATA;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailListAdapter extends BaseExpandableListAdapter {

	List<List<String>> group;
	List<List<MOBILE_EXP_REPORT_DATA>> child;
	Context context;
	TextView amount;
	int childResourceId;
	List<Integer[]> flagList;
	HashMap<String, Integer> iconMap;
	
	public DetailListAdapter(List<List<String>> group,
			List<List<MOBILE_EXP_REPORT_DATA>> child, Context context,
			int childResourceId, TextView amount, List<Integer[]> flagList) {
		this.group = group;
		this.child = child;
		this.context = context;
		this.childResourceId = childResourceId;
		this.amount = amount;
		this.flagList = flagList;
		this.iconMap = new HashMap<String, Integer>();
		iconMap.put("交通", R.drawable.traffic);
		iconMap.put("餐饮", R.drawable.food);
		iconMap.put("通讯", R.drawable.communication);
		iconMap.put("票务", R.drawable.ticket);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return child.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parents) {
		// TODO Auto-generated method stub


		
		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(childResourceId,
					null);
		}
		MOBILE_EXP_REPORT_DATA childInfo = child.get(groupPosition).get(
				childPosition);
		String classString = childInfo.expense_class_desc;
		String typeString = childInfo.expense_class_desc + ">"
				+ childInfo.expense_type_desc;
		String descString = childInfo.description;
		if (descString.equalsIgnoreCase("") || descString == null) {

		} else {
			TextView desc = (TextView) convertView.findViewById(R.id.descText);
			desc.setText(descString);
		}
		DecimalFormat decimalFormat=new DecimalFormat("0.00");
		String amountString = String.valueOf(decimalFormat.format(childInfo.total_amount));
		String status = childInfo.local_status;

		TextView type = (TextView) convertView.findViewById(R.id.typeText);
		type.setText(typeString);

		TextView amount = (TextView) convertView
				.findViewById(R.id.detailAmount);
		amount.setText(amountString);
		if (childResourceId == R.layout.activity_detail_child) {
			ImageView upload = (ImageView) convertView
					.findViewById(R.id.upload);
			if (status.equals("new") || status.equals("NEW")) {
				upload.setVisibility(View.INVISIBLE);
			} else {
				upload.setVisibility(View.VISIBLE);
			}
			int resId;
			try {
				
				resId = iconMap.get(classString);
				
			} catch (Exception e) {
				// TODO: handle exception
				resId = R.drawable.food;
			}
			//设置类型图片
			ImageView typeImage = (ImageView) convertView.findViewById(R.id.typeImage);
			typeImage.setImageResource(resId);
		} else if (childResourceId == R.layout.activity_upload_child) {
			//上传页面检查是否已经选中
			if (checkSelected(groupPosition, childPosition)) {

				convertView.setBackgroundColor(Color.rgb(255, 255, 204));
				ImageView selectImageView = (ImageView) convertView
						.findViewById(R.id.isSelectImage);
				selectImageView.setImageResource(R.drawable.selected);
			}else{
				convertView.setBackgroundColor(Color.rgb(255, 255, 255));
				ImageView selectImageView = (ImageView) convertView
						.findViewById(R.id.isSelectImage);
				selectImageView.setImageResource(R.drawable.unselected);				
			}
		}
		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		checkSum(groupPosition);
		List<String> groupInfo = group.get(groupPosition);
		String dateString = groupInfo.get(0);
		String sumString = groupInfo.get(1);
		convertView = (View) LayoutInflater.from(context).inflate(
				R.layout.activity_detail_group, null);
		TextView date = (TextView) convertView.findViewById(R.id.group_date);
		TextView sum = (TextView) convertView.findViewById(R.id.group_sum);
		date.setText(dateString);
		sum.setText(sumString);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return child.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	// 自己定义一个获得文字信息的方法
	TextView getTextView(String s) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 40);
		TextView text = new TextView(context);
		text.setLayoutParams(lp);

		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		text.setPadding(36, 0, 0, 0);
		text.setText(s);
		return text;
	}

	/**
	 * 检查行累计金额
	 * 
	 * @para groupPosition 头索引
	 * 
	 */
	public Float checkSum(Integer groupPosition) {
		Integer childLen = child.get(groupPosition).size();
		if (childLen == 0) {
			// group.remove((int) groupPosition);
			return (float) 0;
		}
		Float sum = (float) 0;
		for (int i = 0; i < childLen; i += 1) {
			sum += child.get(groupPosition).get(i).total_amount;
		}
		group.get(groupPosition).set(1, "累计：¥" + sum.toString());
		// if(groupPosition + 1 == group.size()){
		// checkSum();
		// }
		return sum;

	}

	/**
	 * 检查总累计金额
	 * 
	 */
	public Float checkSum() {
		Integer groupLen = group.size();
		Float sum = (float) 0;
		for (int i = 0; i < groupLen; i += 1) {
			for (int j = 0; j < child.get(i).size(); j += 1) {
				float temp = child.get(i).get(j).total_amount;
				sum += temp;
			}
		}
		if (amount != null)
			amount.setText("¥" + sum.toString());
		return sum;
	}

	/**
	 * 
	 * 检查记录是否被选中
	 * 
	 */

	private Boolean checkSelected(int groupPosition, int childPosition) {
		for (Integer[] curreyArray : flagList) {
			if (groupPosition == curreyArray[0]
					&& childPosition == curreyArray[1]) {
				return true;
			}
		}
		return false;
	}
}