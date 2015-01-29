package com.handexp.utl;

import android.app.AlertDialog;
import android.content.Context;



public class DialogUtl {
	
	
	

	public static void showAlert(Context context,String message)
	{
		
		new AlertDialog.Builder(context).setMessage(message).setPositiveButton("确定", null).create().show();
		
	}
	
	
}
