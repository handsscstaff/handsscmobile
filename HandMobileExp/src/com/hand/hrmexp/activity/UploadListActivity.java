package com.hand.hrmexp.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.hand.R;
import com.hand.hrmexp.activity.DetailListActivity;
import com.hand.hrmexp.adapter.ContactsInfoAdapter;
import com.hand.hrmexp.adapter.DetailListAdapter;
import com.hand.hrmexp.application.HrmexpApplication;
import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_DATA;
import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_LINE;
import com.hand.hrmexp.model.UploadModel;
import com.handexp.utl.DialogUtl;
import com.littlemvc.db.sqlite.FinalDb;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.db.DbRequestModel;
import com.mas.customview.ProgressDialog;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;

public class UploadListActivity extends SherlockActivity implements LMModelDelegate{
	
	List<List<String>> group;
	List<List<MOBILE_EXP_REPORT_DATA>> child;
	DetailListAdapter adapter;
	ExpandableListView uploadListView;
	private FinalDb finalDb;
	
	public DbRequestModel dbmodel;
	
	ProgressDialog  progressDialog;
	//存放选择的上传数据的位置
	List<Integer[]> flagList = new ArrayList<Integer[]>();
	//存放选择的上传数据的数量
	int uploadCount;
	//上传过程中是否有失败
	Boolean isFaildFlag = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.activity_uploadlist);
		
		//指示器 
		progressDialog  = new ProgressDialog(this,"正在上传.....");  
		progressDialog.setCancelable(false);

		dbmodel = new DbRequestModel(this);
		
		finalDb   = HrmexpApplication.getApplication().finalDb;
		//加载ActionBar设置标题
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_titlebar));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.abs_layout);
		TextView titleView = (TextView) findViewById(R.id.contextTitle);
		titleView.setText("批量上传");
		//绑定ActionBar按钮事件
		TextView returnView = (TextView) findViewById(R.id.returnImage);
		returnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		ImageView addView = (ImageView) findViewById(R.id.addImage);
		addView.setImageDrawable(getResources().getDrawable(R.drawable.submit));
		addView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(flagList.size() == 0){
					Toast.makeText(getApplicationContext(), "请选择需要上传的数据", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//开启指示器
				progressDialog.show(); 
				//初始化
				uploadCount = flagList.size();
				isFaildFlag =false;
				
				for (Integer[] curreyArray : flagList){
					MOBILE_EXP_REPORT_DATA data = child.get(curreyArray[0]).get(curreyArray[1]);	
					UploadModel upModel = new UploadModel(UploadListActivity.this);
					upModel.tag = data;
					upModel.upload(data);
				
				}
				
			}
		});
		
		uploadListView = (ExpandableListView) findViewById(R.id.uploadList);
		uploadListView.setOnChildClickListener(new OnChildClickListener() {		
			@Override
			public boolean onChildClick(ExpandableListView parent, View view, int groupPosition,
					int childPosition, long id) {
				// TODO Auto-generated method stub
				Integer[] array = new Integer[] {groupPosition,childPosition};
				ImageView selectImageView = (ImageView) view.findViewById(R.id.isSelectImage);
				//检查ChildlistView是否已经被选中
				for (Integer[] curreyArray : flagList) {
					if(curreyArray[0] == array[0] && curreyArray[1] == array[1]){
						flagList.remove(curreyArray);
						view.setBackgroundColor(Color.rgb(255, 255, 255));
						selectImageView.setImageResource(R.drawable.unselected);
						return true;
					};
				}
				flagList.add(array);
				view.setBackgroundColor(Color.rgb(255,255,204));
				selectImageView.setImageResource(R.drawable.selected);
				return true;
			}
		});
		

		//无法收缩
		uploadListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				return true;
			}
		});

//		uploadListView.setGroupIndicator(this.getResources().getDrawable((Integer) null));
	}
	
	public void onResume(){
		super.onResume();
		
		try {
			initializeData();
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//		adapter = new DetailListAdapter(group, child, UploadListActivity.this,R.layout.activity_upload_child,null,flagList);
//		uploadListView.setAdapter(adapter);		
		//打开每一个Group
//		int groupCount = uploadListView.getCount();
//		for(int i =0; i<groupCount;i++){
//			uploadListView.expandGroup(i);
//		}
	
	}
	

	
	/**
	 * 
	 * 初始化数据
	 * @throws ParseException 
	 * 
	 * 
	 */
	
	private void initializeData() throws ParseException {
		group = new ArrayList<List<String>>();
		child = new ArrayList<List<MOBILE_EXP_REPORT_DATA>>();
		String[] groupInfo = new String[2];
		List<MOBILE_EXP_REPORT_DATA> childInfo = new ArrayList<MOBILE_EXP_REPORT_DATA>();
		
		List<MOBILE_EXP_REPORT_DATA> resultList = finalDb.findByColumNameWithWhere(MOBILE_EXP_REPORT_DATA.class, "local_status  = 'new'" , " expense_date desc");
		String topDate = null; 
		Boolean flag = false;
		
	
		
		for(int i =0;i<resultList.size();i++){
			MOBILE_EXP_REPORT_DATA data = 	resultList.get(i);
			if(topDate == null){
				topDate = data.expense_date;
				groupInfo = new String[]{topDate,"累计：¥"};
			}
			flag = DetailListActivity.dateCompare(data.expense_date,topDate);
			if(flag){
					
					addInfo(groupInfo, childInfo);
					childInfo.clear();
					topDate = data.expense_date;
					groupInfo = new String[]{topDate,"累计：¥"};
			}
			
			childInfo.add(data);
		}
		if(childInfo.size() != 0){
			addInfo(groupInfo, childInfo);
		}
		
		adapter = new DetailListAdapter(group, child, UploadListActivity.this,R.layout.activity_upload_child,null, flagList);
		uploadListView.setAdapter(adapter);		
		//打开每一个Group
		int groupCount = uploadListView.getCount();
		for(int i =0; i<groupCount;i++){
			uploadListView.expandGroup(i);
		}

	}

	/**
	 * 构建绑定适配器的List
	 * @param g groupList
	 * @param c childList
	 */
	
	private void addInfo(String[] g, List<MOBILE_EXP_REPORT_DATA> c) {
		// TODO Auto-generated method stub
		List<String> groupitem = new ArrayList<String>();
		List<MOBILE_EXP_REPORT_DATA> childitem = new ArrayList<MOBILE_EXP_REPORT_DATA>();
		for (int i = 0; i < g.length; i += 1) {
			groupitem.add(g[i]);
		}
		group.add(groupitem);
		for (int j = 0; j < c.size(); j += 1) {
			childitem.add(c.get(j));
		}
		child.add(childitem);
	}

	@Override
	public void modelDidFinshLoad(LMModel model) {
		// TODO Auto-generated method stub
		
		//上传的model
		if(model.getClass().getName().equalsIgnoreCase(UploadModel.class.getName())){
			
			 UploadModel _model     = (UploadModel) model;
			 
			 String json = new String(_model.mresponseBody);
			 JSONObject jsonobj;
			try {
				uploadCount--;
				jsonobj = new JSONObject(json);
				 String code = ((JSONObject) jsonobj.get("head")).get("code").toString();
				 if(code.equalsIgnoreCase("success")){
					 String local_id = ((JSONObject)jsonobj.get("body")).get("local_id").toString();
					 //修改为上传状态 
					 MOBILE_EXP_REPORT_DATA data   = (MOBILE_EXP_REPORT_DATA)_model.tag;
					  data.local_status = "upload";
					  dbmodel.update(data," id = " + data.id);
				 }
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			 
			
			if(uploadCount == 0){
				
				flagList = new ArrayList<Integer[]>();
				progressDialog.dismiss();
				try {
					initializeData();
				} catch (ParseException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
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
		
		//上传失败
		if(model.getClass().getName().equalsIgnoreCase(UploadModel.class.getName()))
		{
			
			uploadCount--;
			isFaildFlag = true;
			if(uploadCount == 0){
				
				flagList = new ArrayList<Integer[]>();
				progressDialog.dismiss();
				
				try {
					initializeData();
				} catch (ParseException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
				if(isFaildFlag){
					DialogUtl.showAlert(this,"网络出现问题，请检查网络后重新提交");
					//弹出窗口上传过程有数据发生错误
				}
				
			}
			
			
		}else {
			
			
		}
		
	}
	
}
