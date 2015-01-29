package com.hand.hrmexp.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.hand.R;
import com.littlemvc.utl.AsNetWorkUtl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HtmlBaseActivity extends SherlockActivity{
	private  WebView contentWebView; 
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_html_base);
		
		//加载ActionBar设置标题
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_titlebar));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.abs_layout);
		
		TextView titleView = (TextView) findViewById(R.id.contextTitle);
		titleView.setText("生成单据");
		//绑定ActionBar按钮事件
		
		((ImageView)findViewById(R.id.addImage)).setVisibility(View.INVISIBLE);;
		
		TextView returnView = (TextView) findViewById(R.id.returnImage);
		returnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		
		bindAllViews();
		
		 url = getIntent().getStringExtra("url");
	}
	

	
	private void bindAllViews() {
		contentWebView = (WebView)findViewById(R.id.activity_html_base_webview);
		contentWebView.setWebChromeClient(new AlertWebChromeClient());
		contentWebView.setWebViewClient(new ContentWebClient());
//		contentWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		contentWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//		if (Build.VERSION.SDK_INT >= 19) {
//			contentWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//		}       
//		else {
//			contentWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//		}
		WebSettings webSettings = contentWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
//		webSettings.setJavaScriptEnabled(true); 
//		// 设置可以支持缩放 
//		webSettings.setSupportZoom(true); 
//		// 设置出现缩放工具 
//		webSettings.setBuiltInZoomControls(true);
//		//扩大比例的缩放
//		webSettings.setUseWideViewPort(true);
//		//自适应屏幕
//		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		webSettings.setLoadWithOverviewMode(true);
	} 
	
	

	@Override
	protected void onResume() {
		load(url);
		super.onResume();
	}
	
	/**
	 * @param url
	 */
    protected void load(String url) {
    	//System.out.println(AsNetWorkUtl.getAsNetWorkUtl(null).getAbsoluteUrl(url.replace("${base_url}", "")));
    	String  _url   = AsNetWorkUtl.getAsNetWorkUtl(null).getAbsoluteUrl(url.replace("${base_url}", ""));
    	
	    contentWebView.loadUrl(AsNetWorkUtl.getAsNetWorkUtl(null).getAbsoluteUrl(url.replace("${base_url}", "")));
//    	contentWebView.loadUrl("http://www.baidu.com/");
    }
    
	private class AlertWebChromeClient extends WebChromeClient{
		@Override
	    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
	    {
	        new AlertDialog.Builder(HtmlBaseActivity.this)
	            .setTitle("")
	            .setMessage(message)
	            .setPositiveButton(android.R.string.ok,
	                    new AlertDialog.OnClickListener()
	                    {
	                        public void onClick(DialogInterface dialog, int which)
	                        {
	                            result.confirm();
	                        }
	                    })
	            .setCancelable(false)
	            .create()
	            .show();

	        return true;
	    };
	    
	    @Override
	    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
	        new AlertDialog.Builder(HtmlBaseActivity.this)
	        .setTitle("")
	        .setMessage(message)
	        .setPositiveButton(android.R.string.ok,
	                new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int which)
	            {
	                result.confirm();
	            }
	        })
	        .setNegativeButton(android.R.string.cancel,
	                new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int which)
	            {
	                result.cancel();
	            }
	        })
	        .create()
	        .show();

	        return true;
	    }
	    
	}
	
	
	private class ContentWebClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			

			
			Uri query_string = Uri.parse(url);
			String query_scheme = query_string.getScheme();
			String query_host = query_string.getHost();

			if ((query_scheme.equalsIgnoreCase("https") || query_scheme.equalsIgnoreCase("http")) && query_host != null
			        && query_string.getQueryParameter("new_window") == null) {
				return false;// handle the load by webview
			}

			if (query_scheme.equalsIgnoreCase("tel")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, query_string);
				startActivity(intent);
				return true;
			}

			if (query_scheme.equalsIgnoreCase("mailto")) {

				android.net.MailTo mailTo = android.net.MailTo.parse(url);

				// Create a new Intent to send messages
				// 系统邮件系统的动作为Android.content.Intent.ACTION_SEND

				Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
				sendIntent.setType("plain/text");

				// 设置邮件默认地址
				sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { mailTo.getTo() });
				// 调用系统的邮件系统
				try {
					startActivity(sendIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(HtmlBaseActivity.this, "没有找到邮件客户端", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
			return false;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			  
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			//每次网络请求初始化 timer;

		}
	}
	
}
