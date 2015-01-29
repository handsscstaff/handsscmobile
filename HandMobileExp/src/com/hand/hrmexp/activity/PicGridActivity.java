//package com.hand.hrmexp.activity;
//
//import java.io.ByteArrayOutputStream;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//
//import com.hand.R;
//import com.hand.hrmexp.adapter.ImageItemAdapter;
//import com.hand.hrmexp.adapter.ImageItemAdapter.TextCallback;
//
//import com.hand.hrmexp.model.ImageItem;
//import com.handexp.utl.BitmapUtl;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class PicGridActivity extends Activity implements OnClickListener {
//
//	private GridView gridView;
//	private List<ImageItem> dataList;
//	private ImageItemAdapter adapter;
//	private Button deleteBtn;
//
//	private ImageView addPicView;
//	private LinearLayout returnLl;
//
//	// 照片实际的数据
//	private byte[] mContent;	
//	// 拍照
//	public static final int IMAGE_CAPTURE = 0;
//
//	// 相册
//	public static final int ACTION_GET_CONTENT = 1;
//	public static final String EXTRA_IMAGE_LIST = "imagelist";
//	public static Bitmap bitmap;
//	BitmapFactory.Options opts;
//	ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	Uri originUrl;
//	String realImgPath;
//	ImageItem item;
//	
//	Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
//				Toast.makeText(getApplicationContext(), "最多选择9张图片",
//						Toast.LENGTH_SHORT).show();
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_image_grid);
//		//默认压缩4倍
//		opts = new BitmapFactory.Options(); 
//		opts.inSampleSize = 4;
//		opts.inJustDecodeBounds = false;
//		opts.inInputShareable = true;
//		opts.inPurgeable = true;
//		
//		dataList = (ArrayList<ImageItem>) getIntent().getSerializableExtra("dataList");	
//		
////		dataList = new ArrayList<ImageItem>();
//
//		bindAllViews();
//	}
//
//	@Override
//	protected void onStart(){
//		super.onStart();
////		this.dataList = new ArrayList<ImageItem>();
//		
//	}
//	
//	private void bindAllViews() {
//		// 头部左
//		returnLl = (LinearLayout) findViewById(R.id.returnView);
//		returnLl.setOnClickListener(this);
//		// 头部右
//		addPicView = (ImageView) findViewById(R.id.addView);
//		addPicView.setOnClickListener(this);
//		// 底部按钮
//		deleteBtn = (Button) findViewById(R.id.deleteButton);
//		deleteBtn.setOnClickListener(this);
//		// 绑定适配器
//		gridView = (GridView) findViewById(R.id.gridview);
//		adapter = new ImageItemAdapter(getApplicationContext(), dataList,
//				mHandler);
//		adapter.setTextCallback(new TextCallback() {
//			public void onListen(int count) {
//				deleteBtn.setText("取消" + "(" + count + ")");
//			}
//		});
//		gridView.setAdapter(adapter);
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO 自动生成的方法存根
//				adapter.notifyDataSetChanged();
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO 自动生成的方法存根
//		switch (v.getId()) {
//		case R.id.addView:
////			Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
//			if(dataList.size() >= 9){
//				Toast.makeText(getApplicationContext(), "当前最多支持9张照片", Toast.LENGTH_SHORT)
//				.show();	
//				return;
//			}
//			final CharSequence[] items = {"相册","拍照"};
//			AlertDialog dlg = new AlertDialog.Builder(PicGridActivity.this)
//				.setTitle("添加图片")
//				.setItems(items, new DialogInterface.OnClickListener() {
//					//相册在前拍照在后
//					@Override
//					public void onClick(DialogInterface dialog, int item) {
//						// TODO 自动生成的方法存根
//						if(item == 1){
//							Intent getImageByCamera = new Intent(
//									"android.media.action.IMAGE_CAPTURE");
//							startActivityForResult(getImageByCamera, IMAGE_CAPTURE);
//						} else {
//							Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
//							getImage.addCategory(Intent.CATEGORY_OPENABLE);
//							getImage.setType("image/jpeg");
//							startActivityForResult(getImage, ACTION_GET_CONTENT);
//						}
//					}
//				}).create();
//				dlg.show();
//			break;
//		case R.id.returnView:
////			Toast.makeText(getApplicationContext(), "return",Toast.LENGTH_SHORT).show();
//			//Intent传递回去
//			Intent intent = new Intent();
//			intent.putExtra("dataList", (Serializable) dataList);
//			setResult(ACTION_GET_CONTENT, intent);
//			
//			finish();
//			break;
//		case R.id.deleteButton:
//			Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT)
//					.show();
//			deletePic();
//			adapter.notifyDataSetChanged();
//			deleteBtn.setText("取消");
////			ArrayList<String> list = new ArrayList<String>();
////			Collection<String> c = adapter.getMap().values();
////			Iterator<String> it = c.iterator();
////			for (; it.hasNext();) {
////				list.add(it.next());
////			}
////			if (Bimp.act_bool) {
////				// Intent intent = new Intent(getApplicationContext(),)
////				// startActivity(intent);
////				Bimp.act_bool = false;
////			}
////			for (int i = 0; i < list.size(); i++) {
////				if (Bimp.drr.size() < 9) {
////					Bimp.drr.add(list.get(i));
////				}
////			}
////			finish();
//		default:
//			break;
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		
//		if(data == null){
//			return;
//		}
//		
//		switch (requestCode) {
//		
//		case IMAGE_CAPTURE:
//			Bundle extras = data.getExtras();
//			Bitmap bitmap = (Bitmap) extras.get("data");
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
//			
//			if (data.getData() != null)
//			{
//				originUrl = data.getData();
//			}
//			else
//			{
//				originUrl  = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));	
//			}
//			realImgPath = getRealPath(originUrl);
//			mContent = baos.toByteArray();
//			item = new ImageItem(originUrl.toString(),null,realImgPath,mContent);
//			dataList.add(item);
//			
//			adapter.notifyDataSetChanged();
//			break;
//		case ACTION_GET_CONTENT:
//			
//			originUrl = data.getData();
//			
//			try {
//				realImgPath = getRealPath(originUrl);
//				Log.d("realImgPath", realImgPath);
//				mContent = BitmapUtl.readStream(getContentResolver().openInputStream(Uri.parse(originUrl.toString())));
//				Bitmap myBitmap = BitmapUtl.bytesToBitmap(mContent, opts);			
//				//已经压缩过的所以速率是100
//				mContent = BitmapUtl.BitmapTobytes(myBitmap, 100);				
//				item = new ImageItem(originUrl.toString(),null,realImgPath,mContent);
//				dataList.add(item);
//			} catch (Exception e) {
//				// TODO: handle exception
//				Toast.makeText(getApplicationContext(), "获取图片失败!!", Toast.LENGTH_SHORT).show();
//				e.printStackTrace();
//				return;
//			}
////			adapter = new ImageItemAdapter(getApplicationContext(), dataList,
////					mHandler);
////			gridView.setAdapter(adapter);
//			adapter.notifyDataSetChanged();
//			break;
//
//		default:
//			break;
//		}
//	}
//	
//	
//	/**
//	 * 获取图片的真实地址
//	 * @param originUrl
//	 * @return String真实地址 	 	
//	 */
//	private String getRealPath(Uri originUrl){
//		String[] proj = {MediaStore.Images.Media.DATA};
//		Cursor actualimagecursor = getApplicationContext().getContentResolver().query(originUrl, proj, null, null, null);
//		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
//		actualimagecursor.moveToFirst();  
//		String img_path = actualimagecursor.getString(actual_image_column_index);  		
//		return img_path;
//	}
//	
//	/**
//	 * 取消添加的图片
//	 * 
//	 */
//	private void deletePic(){
//		Iterator<ImageItem> iter = dataList.iterator();  
//		while(iter.hasNext()){
//			ImageItem item = iter.next();
//			if(item.getIsSelected() == true){
//				iter.remove();
//			}
//		}
//		adapter.initSelectedTotal();
//	}
//}
