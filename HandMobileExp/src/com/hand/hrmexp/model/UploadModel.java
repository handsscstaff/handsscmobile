package com.hand.hrmexp.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import com.hand.hrmexp.application.HrmexpApplication;
import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_DATA;
import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_LINE;
import com.hand.hrms4android.exception.ParseExpressionException;
import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.db.sqlite.FinalDb;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mas.album.Util;
import com.mas.album.items.ImageItem;

public class UploadModel extends AsHttpRequestModel{

	private  XmlConfigReader configReader;	
	
	private  FinalDb finalDb;
	
	public UploadModel(LMModelDelegate delegate) {
		super(delegate);
		configReader = XmlConfigReader.getInstance();
		finalDb = HrmexpApplication.getApplication().finalDb;  
	}
	
	public HashMap<String,String> packParam(final MOBILE_EXP_REPORT_LINE data)
	{
		final String[] currency = data.currency.split("-");
		HashMap <String,String> param = new HashMap<String,String>(){
			{
				put("expense_class_id",String.format("%d",data.expense_class_id));
				put("expense_type_id",String.format("%d",data.expense_type_id));
				put("expense_amount",String.format("%f", data.expense_amount));
				put("expense_number",String.format("%d", data.expense_number));
				put("expense_date",data.expense_date);
				put("expense_date_to",data.expense_date_to);
				put("expense_place",data.expense_place);
				put("description",data.description);
				put("local_id",String.format("%d",data.id));
				put("exchange_rate",String.valueOf(data.exchangeRate));
				put("currency_code", currency[0]);
				
				
			}
			
		};
		return param;
	}
	public void upload(MOBILE_EXP_REPORT_DATA data){
				
		String queryUrl = null;
		try {
			queryUrl = configReader
			        .getAttr(new Expression(
			                "/backend-config/url[@name='hmb_expense_detail_insert']",
			                "value"));
		} catch (ParseExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MOBILE_EXP_REPORT_LINE line	 =  finalDb.findById(data.id,MOBILE_EXP_REPORT_LINE.class);
		
		//无照片
		if(line.item1 ==null){
			uploadBytes(queryUrl, packParam(line), line.item1, "");	
		//一张照片
		}else if(line.item1 !=null && line.item2 == null ){
			
			uploadBytes(queryUrl, packParam(line), line.item1, "upload1.jpg");
		}else {
			//多张
			
			List<HashMap<String, Object>> files = new ArrayList<HashMap<String, Object>>();
			
			Class<? extends MOBILE_EXP_REPORT_LINE> ownerClass = line.getClass();
			for(int i =1;i<10;i++){
				String fieldName = "item" + i;
				try {
					Field field = ownerClass.getField(fieldName);
					byte[] content = (byte[]) field.get(line);
					if(content == null){
						break;
					}
					
					String fileName  =  "upload" + i +".jpg";
					HashMap<String, Object> file = new HashMap<String, Object>();
					file.put("fileName", fileName);
					file.put("content", content);
					
					files.add(file);
					
				} catch (NoSuchFieldException e) {
					
					e.printStackTrace();
					
				} catch (IllegalAccessException e) {
					
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					
					e.printStackTrace();
				}catch (NullPointerException e){
					break;
				}
				
			}
			
			
			
			uploadFiles(queryUrl, packParam(line), files);
		}
		
		
//		if(data.item1  != null){
//			uploadBytes(queryUrl, packParam(data), data.item1, "upload1");
//		}else {
//			uploadBytes(queryUrl, packParam(data), data.item1, "");	
//		}
//		
		
		
		
	}

}
