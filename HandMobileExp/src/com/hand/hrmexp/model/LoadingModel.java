package com.hand.hrmexp.model;

import com.handexp.utl.ConstantsUtl;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

public class LoadingModel extends AsHttpRequestModel {

	
	public LoadingModel(LMModelDelegate delegate){
		super(delegate);
	}
	
	public void load()
	{
		this.post(ConstantsUtl.configFile, null);
		
	}

}
