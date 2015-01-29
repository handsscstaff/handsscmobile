package com.hand.hrmexp.model;

import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

public class MenuModel extends AsHttpRequestModel{
		private  XmlConfigReader configReader;
	
	
	public MenuModel(LMModelDelegate delegate){
		super(delegate);
		configReader = XmlConfigReader.getInstance();
	}
	
	public void load()
	{
		
		try {
			
			String queryUrl = configReader
			        .getAttr(new Expression(
			                "/backend-config/url[@name='function_query_url']",
			                "value"));
			
			
			this.post(queryUrl, null);
			
		} catch (Exception e) {
	
			this.modelDelegate.modelDidFaildLoadWithError(this);
			return;
		}
		
		
	}

}
