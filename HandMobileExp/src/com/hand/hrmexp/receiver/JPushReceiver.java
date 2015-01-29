package com.hand.hrmexp.receiver;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.hand.R;
import com.hand.hrmexp.activity.LoadingActivity;
import com.hand.hrmexp.application.HrmexpApplication;


import cn.jpush.android.api.JPushInterface;



import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class JPushReceiver extends BroadcastReceiver{
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.e(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }		
	}	
	private void processCustomMessage(Context context, Bundle bundle)
	{
		
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        NotificationCompat.BigTextStyle  notiStyle = new
                NotificationCompat.BigTextStyle();

        // go to loading activity
        
        Intent notifyIntent =
                new Intent(context,LoadingActivity.class);
        
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                context,
                0,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
//		RemoteViews expandedView = new RemoteViews(context.getPackageName(), 
//		        R.layout.customer_notitfication_layout);
//		
//		expandedView.setTextViewText(R.id.displayMessage, message);
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti  =  new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.icon)
        .setAutoCancel(true)
        .setContentTitle("汉得移动报销")
        .setContentText(message)
        .setStyle(notiStyle.bigText(message).setBigContentTitle("汉得移动报销"))
        .setContentIntent(pendingIntent)
        
        .build();
		//为了自动展开
		if(Build.VERSION.SDK_INT >= 16){
			noti.priority = Notification.PRIORITY_MAX;
		}else {
			
		}
		
//		noti.bigContentView = expandedView;
	
		notificationManager.notify(new Date().getSeconds(), noti);
		
		
	}
	
	
	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}


	

}
