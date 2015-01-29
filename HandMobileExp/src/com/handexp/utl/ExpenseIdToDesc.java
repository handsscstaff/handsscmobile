package com.handexp.utl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hand.hrmexp.application.HrmexpApplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ExpenseIdToDesc {
	
   static 	SharedPreferences  mPreferences = PreferenceManager.getDefaultSharedPreferences ( HrmexpApplication.getApplication());
   static JSONObject	expense_type_data;
   
   static public  JSONArray expense_class;
	
   static{
		
		try {
			expense_type_data = new JSONObject(mPreferences.getString(ConstantsUtl.expenseType, ""));
			expense_class =  expense_type_data.getJSONObject("body").getJSONArray("list");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


   public 	static String  ExpenseClassIdToDesc(int expense_class_id)
   {
	   
		try {

			for(int i =0;i<expense_class.length();i++){
				int value = expense_class.getJSONObject(i).getInt("expense_class_id");
				if(expense_class_id == value){
					return expense_class.getJSONObject(i).getString("expense_class_desc");
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	   
	   return null;
	   
   }
   
   public  static JSONArray getExpenseTypeArray(int expense_class_id){
		
	   try {

			for(int i =0;i<expense_class.length();i++){
				int value = expense_class.getJSONObject(i).getInt("expense_class_id");
				if(expense_class_id == value){
					return  expense_class.getJSONObject(i).getJSONArray("expense_types");
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	   
	   
	   
	   return null;
	   
	   
	   
   }
   
/**
 *     通过费用类别的id，和费用类型id返回，费用类型描述
 */
   
   public  static  String  ExpenseTypeIdToDesc( int expense_class_id,int expense_type_id)
   {
	   
		try {
			
			JSONArray expense_type =getExpenseTypeArray(expense_class_id);
					
			for(int i =0;i<expense_type.length();i++){
				int value = expense_type.getJSONObject(i).getInt("expense_type_id");
				if(expense_type_id == value){
					return expense_type.getJSONObject(i).getString("expense_type_desc");
				}
				
			}		
					
					
				
				
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	   
	   return null;
   }

}
