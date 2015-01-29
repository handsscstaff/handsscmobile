package com.hand.hrmexp.model;

import java.util.HashMap;

import android.util.Log;

import com.hand.hrms4android.exception.ParseExpressionException;
import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.*;
import com.littlemvc.utl.AsNetWorkUtl;

public class LoginModel extends AsHttpRequestModel{
	private  XmlConfigReader configReader;	
	
	public LoginModel(LMModelDelegate delegate){
		super(delegate);
		configReader = XmlConfigReader.getInstance();

	}
	public void load(HashMap parm)
	{
		try {
			String queryUrl = configReader
			        .getAttr(new Expression(
			                "/backend-config/url[@name='login_submit_url']",
			                "value"));
			
			AsNetWorkUtl.removeAllCookies();
			
			this.post(queryUrl, parm);
			
		} catch (ParseExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	
}
