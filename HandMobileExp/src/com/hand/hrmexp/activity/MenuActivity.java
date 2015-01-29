package com.hand.hrmexp.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hand.R;
import com.hand.hrmexp.application.HrmexpApplication;
import com.hand.hrmexp.model.MenuModel;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class MenuActivity extends FragmentActivity implements LMModelDelegate, View.OnClickListener{
	
	private ResideMenu resideMenu;
	
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub	
		super.onCreate(arg0);
		HrmexpApplication.getApplication().transaction =         getSupportFragmentManager().beginTransaction();

		
		setContentView(R.layout.activity_menu);
		setUpMenu();
		changeFragment(new HomeFragment()); 
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub


		super.onResume();
	}
	
//////////////////private///////////////////
	
    private void setUpMenu() {
    	
    	 new MenuModel(this).load();

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.homebg);
        resideMenu.attachToActivity(this);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.7f);


        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.main_head_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        
        
        

    }
    
    private void addHomeItem()
    {
    	
		//添加主页
		ResideMenuItem homeItem     = new ResideMenuItem(this, R.drawable.home,     "主页");
		homeItem.tag = "homeItem";
		homeItem.setOnClickListener(this);
		resideMenu.addMenuItem(homeItem, ResideMenu.DIRECTION_LEFT);
    }
    
    private void addSettingItem()
    {
    	//添加设置
    	ResideMenuItem settingItem = new ResideMenuItem(this, R.drawable.settings,     "设置");
    	settingItem.tag = "settingItem";
    	settingItem.setOnClickListener(this);
    	resideMenu.addMenuItem(settingItem, ResideMenu.DIRECTION_LEFT);
    }
    
    @Override
    public void onClick(View view) {
         
    	ResideMenuItem itemview = (ResideMenuItem)view;
    	String tag = (String) itemview.tag;
    	if(tag.startsWith("${base_url}")){
    		Bundle bundle = new Bundle();
			bundle.putString("url", (String) itemview.tag);
			bundle.putString("title", itemview.tv_title.getText().toString());
			changeFragment(Fragment.instantiate(this, "com.hand.hrmexp.activity.HtmlFragment", bundle));
    		
			resideMenu.closeMenu();
			return;
    	}else if(tag.equalsIgnoreCase("homeItem")){
    		
    		changeFragment(new HomeFragment()); 
    		resideMenu.closeMenu();
    	}else if(tag.equalsIgnoreCase("settingItem")){
    		
    		changeFragment(new SystemSetFragment());
    		resideMenu.closeMenu();
    	}
    	
    	
    	
//        if (view == itemHome){
//            changeFragment(new HomeFragment());
//        }
    }
    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


	@Override
	public void modelDidFinshLoad(LMModel model) {
		// TODO Auto-generated method stub
		AsHttpRequestModel model1 = (AsHttpRequestModel)model;
		String json = new String(model1.mresponseBody);
		
		try {
			JSONObject jsonobj = new  JSONObject(json);
			JSONArray arr = 	jsonobj.getJSONObject("body").getJSONArray("list");

			
			addHomeItem();
			
			for(int i=0;i< arr.length();i++){
				
				JSONArray arrobj = arr.getJSONObject(i).getJSONArray("items");
				for(int j=0;j<arrobj.length();j++){
					
			        // create menu items;
					ResideMenuItem item     = new ResideMenuItem(this, R.drawable.timesheetfill,     arrobj.getJSONObject(j).getString("title"));

					item.tag = arrobj.getJSONObject(j).getString("url");

			        resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT);
			        item.setOnClickListener(this);

					
				}
				
				
			}
		
			addSettingItem();
			
//			System.out.println(((JSONObject)jsonobj.get("body")).get("list").toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void modelDidStartLoad(LMModel model) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void modelDidFaildLoadWithError(LMModel model) {
		// TODO Auto-generated method stub
		
	}
}
