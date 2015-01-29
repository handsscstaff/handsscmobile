//package com.hand.hrmexp.activity;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.util.List;
//
//import com.hand.R;
//import com.hand.hrmexp.application.HrmexpApplication;
//import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_LINE;
//import com.hand.hrmexp.popwindows.CalendarPopwindow;
//import com.hand.hrmexp.popwindows.ExpenseTypePopwindow;
//import com.littlemvc.db.sqlite.FinalDb;
// 
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Base64;
//import android.view.View.OnClickListener;
//import android.support.v4.app.Fragment;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.ScaleAnimation;
//import android.view.inputmethod.InputMethodManager;
//import android.webkit.WebView.FindListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class CopyOfDetailLineActivity extends Activity implements View.OnClickListener {
//	private LinearLayout llexpense_type;
//	private LinearLayout llplace;
//	private LinearLayout llcalendar;
//	private ImageButton returnbtn;
//	private ImageView mimageView;
//	private EditText edExpenseDesc;
//	//数据源 
//	private byte[] mContent;
//	
//	private ui.custom.component.NumberText amount;
//	private EditText quantity;
//	
//	//保存按钮
//	private Button   save;
//	
//	private  FinalDb finalDb;
//	private ExpenseTypePopwindow expensetypepicker;
//	private CalendarPopwindow calendarpicker;
//	
//	private Boolean readonlyFlag;
//	
//	private View  rootview;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_detail_line);
//		
//		finalDb   = HrmexpApplication.getApplication().finalDb;
//		
//		buildViews();
//	}
//	
//
//	
//	@Override
//	public void onResume() {
//		super.onResume();
//		
//		if(true)
//		{
//			initViews();
//			
//			
//		}
//		
//		
//
//		
//		
//	}
//	
//	private void initViews()
//	{
//		
//		List<MOBILE_EXP_REPORT_LINE> resultList = finalDb.findAllByWhere(MOBILE_EXP_REPORT_LINE.class, " id=\"" + 1 + "\"");
//		for(int i =0;i<resultList.size();i++){
//			MOBILE_EXP_REPORT_LINE data= 	resultList.get(i);
////			mimageView.set
//			
//			mContent =  data.item1;
//			Bitmap myBitmap = getPicFromBytes(data.item1, null);
//			// //把得到的图片绑定在控件上显示
//			mimageView.setImageBitmap(myBitmap);
//		}
//	
//	}
//	
///////////////////private //////////////////////////////////	
//	private void buildViews()
//	{
//		
//		
//		
//		rootview = findViewById(R.id.detail_line_id);
//
//		
//		returnbtn =   (ImageButton)findViewById(R.id.return_btn);	 
//		returnbtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = 	new Intent(CopyOfDetailLineActivity.this,MenuActivity.class);
//				startActivity(intent);
//				finish();
//				
//			}
//		});
//	 
//
////		//数据源
////		//金额
////		amount = (ui.custom.component.NumberText)findViewById(R.id.amount);
////		
////		//数量
////		quantity = (EditText) findViewById(R.id.quantity);
////		
////		//照相 
////		mimageView  =(ImageView)findViewById(R.id.camera);
////		bind(mimageView);
//		
//		//费用类型
//		llexpense_type = (LinearLayout)findViewById(R.id.expense_type);
//		llexpense_type.setOnClickListener(this);
//		TextView typelable  = (TextView)findViewById(R.id.detailTypeLabel);
//		expensetypepicker = new ExpenseTypePopwindow(this,typelable);
//		//日期
//		llcalendar = (LinearLayout)findViewById(R.id.llcalendar_id);
//		llcalendar.setOnClickListener(this);
////		calendarpicker  = new CalendarPicker(this, (TextView)findViewById(R.id.tvcalendar_id));
//
//		//地点
//		llplace = (LinearLayout)findViewById(R.id.place);
//	  
//		//描述
//		edExpenseDesc = (EditText) findViewById(R.id.expense_desc_id);
//	  
//	  //保存
//		save = (Button)findViewById(R.id.save_button);
//		  
//		save.setOnClickListener(this);
//
//		
//		
//	}
//	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		return super.onTouchEvent(event);
//	}
//	
//	//处理弹出框的干扰
//	@Override  
//	public boolean dispatchTouchEvent(MotionEvent ev) {  
//	    if (ev.getAction() == MotionEvent.ACTION_DOWN) {  
//	        View v = getCurrentFocus();  
//	        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//            if (imm != null) {  
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
//            } 
//	        expensetypepicker.dismiss();
//	        return super.dispatchTouchEvent(ev);  
//	    }  
//	
//	    // 必不可少，否则所有的组件都不会有TouchEvent了  
//	    if (getWindow().superDispatchTouchEvent(ev)) 
//	    {  
//	        return true;  
//	    }  
//	    return onTouchEvent(ev);  
//	}  
//	
//	private void bind(ImageView img)
//	{
//		
//		OnClickListener  imgViewListener = new OnClickListener()
//		{
//			public void onClick ( View v )
//			{
//				final CharSequence[] items =
//				{ "相册", "拍照" };
//				AlertDialog dlg = new AlertDialog.Builder(CopyOfDetailLineActivity.this).setTitle("选择图片").setItems(items,
//						new DialogInterface.OnClickListener()
//						{
//							public void onClick ( DialogInterface dialog , int item )
//							{
//								// 这里item是根据选择的方式，
//								// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
//								if (item == 1)
//								{
//									Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
//									startActivityForResult(getImageByCamera, 1);
//								} else
//								{
//									Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
//									getImage.addCategory(Intent.CATEGORY_OPENABLE);
//									getImage.setType("image/jpeg");
//									startActivityForResult(getImage, 0);
//								}
//							}
//						}).create();
//				dlg.show();
//			}
//		};
//		// 给imageView控件绑定点点击监听器
//		img.setOnClickListener(imgViewListener);
//		
//	}
//	public void addButton()
//	{
//		Animation mScaleAnimation = new ScaleAnimation(1f, 0.5f, 1f,
//        1f,// 整个屏幕就0.0到1.0的大小//缩放
//        Animation.INFINITE,1,
//        Animation.INFINITE, 1);
//
//		Animation mAnimation = new ScaleAnimation(1f, 0.5f, 1f,1f);
//
//		mScaleAnimation.setDuration(1000);
//		mAnimation.setFillAfter(true);
//		save.startAnimation(mAnimation);
//		
//	}
//	
//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if( calendarpicker.getHeight()  > 0 || calendarpicker.getHeight() >0 ){
//			expensetypepicker.dismiss();
//			calendarpicker.dismiss();
//			return true;	
//		}
//        return super.onKeyDown(keyCode, event);
//    }
//	
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		if(v.equals(llexpense_type)){
//			
//			expensetypepicker.setAnimationStyle(R.style.AnimBottom);
//			expensetypepicker.showAtLocation(findViewById(R.id.detail_line_id),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//		
//		}else if(v.equals(save)){
//			//保存按钮逻辑 
//			
//			
//			
//			MOBILE_EXP_REPORT_LINE  line = 	 new MOBILE_EXP_REPORT_LINE();
//			
//			line.expense_amount =   Integer.parseInt(amount.getText().toString());
//			
//			//默认添加数量为一 
//
//			if(!quantity.getText().toString().equalsIgnoreCase("")){
//				line.expense_number  =  Integer.parseInt(quantity.getText().toString());
//			}else{
//				line.expense_number = 1;
//				
//			}	
//			//日期
//			line.expense_date = calendarpicker.datefrom;
//			line.expense_date_to = calendarpicker.dateto;
//			//费用类型
//			line.expense_class_desc = expensetypepicker.expense_class_desc;
//			line.expense_type_desc = expensetypepicker.expense_type_desc;
//			line.expense_class_id = expensetypepicker.expense_class_id;
//			line.expense_type_id = expensetypepicker.expense_type_id;
//			
//			line.expense_place = "上海市>上海市";
//			
//			line.local_status = "NEW";
//			//图片
//			line.item1  = mContent;
//			//描述
//			line.description = edExpenseDesc.getText().toString();
//			
//			finalDb.save(line);
//			
//			addButton();
//			
//
//			
//			
//			
//		}else if(v.equals(llcalendar)){
//
//			calendarpicker.showAtLocation(findViewById(R.id.detail_line_id),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//		}
//	}
//	
//	
//	
//
//	protected void onActivityResult ( int requestCode , int resultCode , Intent data )
//	{
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//
//		ContentResolver resolver = getContentResolver();
//		/**
//		 * 因为两种方式都用到了startActivityForResult方法，
//		 * 这个方法执行完后都会执行onActivityResult方法， 所以为了区别到底选择了那个方式获取图片要进行判断，
//		 * 这里的requestCode跟startActivityForResult里面第二个参数对应
//		 */
//		//处理返回按钮
//		if(data == null){
//			
//			return;
//		}
//		
//		if (requestCode == 0)
//		{
//			try
//			{
//				// 获得图片的uri
//				Uri originalUri = data.getData();
//				// 将图片内容解析成字节数组
//			  mContent = readStream(resolver.openInputStream(Uri.parse(originalUri.toString())));
//				// 将字节数组转换为ImageView可调用的Bitmap对象
//				Bitmap myBitmap = getPicFromBytes(mContent, null);
//				// //把得到的图片绑定在控件上显示
//				mimageView.setImageBitmap(myBitmap);
//			} catch ( Exception e )
//			{
//				System.out.println(e.getMessage());
//			}
//
//		} else if (requestCode == 1)
//		{
//			Bitmap myBitmap = null;
//			try
//			{
//				super.onActivityResult(requestCode, resultCode, data);
//				Bundle extras = data.getExtras();
//				myBitmap = (Bitmap) extras.get("data");
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//				mContent = baos.toByteArray();
//				mimageView.setImageBitmap(myBitmap);
//			} catch ( Exception e )
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			// 把得到的图片绑定在控件上显示
//
//		}
//	}
//	
//	
//	
//	public static byte[] readStream ( InputStream inStream ) throws Exception
//	{
//		byte[] buffer = new byte[1024];
//		int len = -1;
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		while ((len = inStream.read(buffer)) != -1)
//		{
//			outStream.write(buffer, 0, len);
//		}
//		byte[] data = outStream.toByteArray();
//		outStream.close();
//		inStream.close();
//		return data;
//
//	}
//	
//	
//	public static Bitmap getPicFromBytes ( byte[] bytes , BitmapFactory.Options opts )
//	{
//		if (bytes != null)
//			if (opts != null)
//				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
//			else
//				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//		return null;
//	}
//
//	
//	
//
//}
