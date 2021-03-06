package com.hand.hrmexp.model;

import java.util.HashMap;

import com.hand.hrms4android.exception.ParseExpressionException;
import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.handexp.utl.ConstantsUtl;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

public class ExpenseTypeModel extends AsHttpRequestModel {

	private  XmlConfigReader configReader;	
	
	public ExpenseTypeModel(LMModelDelegate delegate) {
		super(delegate);
		configReader = XmlConfigReader.getInstance();
		this.modelDelegate = delegate;
	}

	public void load(HashMap parm) {
		// this.post(ConstantsUtl.tmp, null);
		
		try {
			String queryUrl = configReader.getAttr(new Expression(
					"/backend-config/url[@name='synchronization_url']", "value"));

			this.post(queryUrl, parm);

		} catch (ParseExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
