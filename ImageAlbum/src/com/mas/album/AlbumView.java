package com.mas.album;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

import com.mas.album.adapter.ImageGridAdapter;
import com.mas.album.adapter.ViewPagerAdapter;
import com.mas.album.items.ImageItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumView extends Activity {

	private GridView mGridView;

	private ViewPager mViewPager;

	private ViewPagerAdapter viewpageradapter;

	private ImageGridAdapter imageadapter;

	private ArrayList<ImageItem> imageList;
	

	private ImageView backImageView;

	private ImageView delImageView;

	private FrameLayout mFrameLayout;

	private ImageView addPic;

	private TextView returnTextView;
	
	private  TextView numTextView;

	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	private int MAX_PIC = 7;

	private int MAX_SIZE = 300000;


	private int currentPosition;

		public static final int IMAGE_CAPTURE = 0;

	public static final int ACTION_GET_CONTENT = 1;
	
	//uri
	
	Uri photoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_grid);

		prepare();

		returnTextView = (TextView) findViewById(R.id.returnTextView);
		returnTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				returnWithBytes();

			}
		});

		backImageView = (ImageView) findViewById(R.id.backImageView);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				goBackGrid();
			}
		});

		delImageView = (ImageView) findViewById(R.id.delImageView);
		delImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = currentPosition;
				imageList.remove(currentPosition);

				// viewpageradapter.notifyDataSetChanged();
				viewpageradapter = new ViewPagerAdapter(imageList,
						AlbumView.this);
				mViewPager.setAdapter(viewpageradapter);

				imageadapter.notifyDataSetChanged();
				// mViewPager.requestLayout();
				// mGridView.requestLayout();
				
				setImageNum();

				goBackGrid();
			}
		});

		addPic = (ImageView) findViewById(R.id.addPic);

		addPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(imageList.size() <=9){
					showDialog();
				}else{
					Toast.makeText(AlbumView.this, "最大只能9张照片", Toast.LENGTH_LONG).show();

				}

			}
		});

		mFrameLayout = (FrameLayout) findViewById(R.id.mFrameLayout);


		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		viewpageradapter = new ViewPagerAdapter(imageList, this);
		mViewPager.setAdapter(viewpageradapter);
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());

		mGridView = (GridView) findViewById(R.id.mGridView);
		imageadapter = new ImageGridAdapter(imageList, this);
		mGridView.setAdapter(imageadapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentPosition = position;
				mViewPager.setCurrentItem(position);
				goBackPager();

			}
		});
		
		
		numTextView  = (TextView) findViewById(R.id.numTextView);
		setImageNum();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (data == null) {
			return;
		}

		byte[] mContent;
		Uri originalUri;
		Bitmap bm;

		switch (requestCode) {
		case IMAGE_CAPTURE:
//			originalUri = data.getData();
			if(resultCode == 0){
				return;
			}
			originalUri = photoUri;
			
			try {
				Uri uri  =  Uri.parse(originalUri.toString());

				mContent = Util.readStream(getContentResolver()
						.openInputStream(Uri.parse(originalUri.toString())));
				
				String fileName = uri.toString();
				 fileName = uri.toString().replace("file://", "");
				 

				bm = Util.CompressBytes(mContent,fileName);
				mContent = Util.CompressBytes(mContent, this.MAX_SIZE,fileName);

				imageList.add(new ImageItem(mContent, bm));
				System.gc();
				setImageNum();

				
				showGrid();

				viewpageradapter = new ViewPagerAdapter(imageList,
						AlbumView.this);
				mViewPager.setAdapter(viewpageradapter);

				imageadapter.notifyDataSetChanged();

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;
		case ACTION_GET_CONTENT:
			if(data == null){
				return;
			}

			originalUri = data.getData();

			try {
				mContent = Util.readStream(getContentResolver()
						.openInputStream(Uri.parse(originalUri.toString())));
				
				Cursor cursor = null;
				String fileName;
				try {
					String[] proj = { MediaStore.Images.Media.DATA };
					cursor = this.getContentResolver().query(originalUri, proj, null,
							null, null);
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					fileName = cursor.getString(column_index);

				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}

				bm = Util.CompressBytes(mContent,fileName);
				mContent = Util.CompressBytes(mContent, this.MAX_SIZE,fileName);

				imageList.add(new ImageItem(mContent, bm));
				System.gc();
				setImageNum();

				
				showGrid();

				viewpageradapter = new ViewPagerAdapter(imageList,
						AlbumView.this);
				mViewPager.setAdapter(viewpageradapter);

				imageadapter.notifyDataSetChanged();
			} catch (Exception e) {

				e.printStackTrace();
				return;

			}

			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			returnWithBytes();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

/****************** private *************************/

	private void prepare() {

		imageList = (ArrayList<ImageItem>) ImageItem.mMemoryCache
				.get("imageList");
		
		

	}

	public void setImageNum(){
		
		numTextView.setText(imageList.size() + " photo");
		
	}

	public void setMaxPic(int max) {
		this.MAX_PIC = max;
	}

	private void goBackPager() {

		Animation animation = AnimationUtils.loadAnimation(AlbumView.this,
				R.anim.gird_push_top);
		animation.setDuration(500);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				mGridView.setVisibility(View.GONE);

			}
		});
		mGridView.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(AlbumView.this,
				R.anim.pager_push_grid);
		animation.setDuration(500);
		mFrameLayout.setVisibility(View.VISIBLE);
		mViewPager.startAnimation(animation);
	}

	private void returnWithBytes() {
		Intent intent = getIntent();
		ImageItem.mMemoryCache.put("imageList", imageList);
		// intent.putParcelableArrayListExtra("imageList", _list);
		setResult(0, intent);
		finish();

	}

	private void showGrid() {
		mFrameLayout.setVisibility(View.GONE);
		mGridView.setVisibility(View.VISIBLE);

	}

	private void goBackGrid() {

		Animation animation = AnimationUtils.loadAnimation(AlbumView.this,
				R.anim.pager_push_top);
		animation.setDuration(500);
		mViewPager.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(AlbumView.this,
				R.anim.gird_push_pager);
		animation.setDuration(500);
		mGridView.setVisibility(View.VISIBLE);
		mGridView.startAnimation(animation);

	}

	private void showDialog() {

		final CharSequence[] items = { "相册", "拍照" };
		AlertDialog dlg = new AlertDialog.Builder(AlbumView.this)
				.setTitle("选择图片")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
		
						
						if (item == 1) {
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							
							SimpleDateFormat timeStampFormat = new SimpleDateFormat(
									"yyyy_MM_dd_HH_mm_ss");
									String filename = timeStampFormat.format(new Date());
									ContentValues values = new ContentValues();
									values.put(Media.TITLE, filename);
							 photoUri = getContentResolver().insert(
									MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
							 
								getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

							startActivityForResult(getImageByCamera,
									IMAGE_CAPTURE);
						} else {
							Intent getImage = new Intent(
									Intent.ACTION_GET_CONTENT);
							getImage.addCategory(Intent.CATEGORY_OPENABLE);
							getImage.setType("image/jpeg");
							startActivityForResult(getImage, ACTION_GET_CONTENT);
						}
					}
				}).create();
		dlg.show();
	}

	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {

			currentPosition = position;
			System.out.println(currentPosition);
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

}
