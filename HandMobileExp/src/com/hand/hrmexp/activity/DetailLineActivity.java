package com.hand.hrmexp.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import ui.custom.component.NumberText;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.hand.R;
import com.hand.hrmexp.application.HrmexpApplication;
import com.hand.hrmexp.dao.MOBILE_EXP_REPORT_LINE;
import com.hand.hrmexp.dialogs.DatePickerWrapDialog;
import com.hand.hrmexp.popwindows.ExpenseTypePopwindow;
import com.handexp.utl.BitmapUtl;
import com.handexp.utl.ViewUtl;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.db.DbRequestModel;
import com.mas.album.AlbumView;
import com.mas.album.Util;
import com.mas.album.items.ImageItem;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetailLineActivity extends Activity implements
		View.OnClickListener, LMModelDelegate {

	// 费用类型
	private LinearLayout expenseTypell;

	private TextView expenseTypeTextView;

	private ExpenseTypePopwindow expenseTypePicker;

	// 币种和汇率
	private TextView currenyTextView;
	private EditText exchangeRateTextView;

	public static final int CURRENCY_CONTENT = 3;

	// 地点
	private LinearLayout placell;

	private EditText placeEditText;
	// 日期
	private LinearLayout datell;

	private TextView dateToTextView;

	private TextView dateFromTextView;

	private DatePickerWrapDialog dateToDateDialog;

	private DatePickerWrapDialog dateFromDateDialog;
	// 返回
	private TextView returnImgBtn;
	// 拍照
	private ImageView photoImgView;

	// 照片列表数据
	private ArrayList<ImageItem> imageList = new ArrayList<ImageItem>();

	// 备注
	private EditText commentEditText;

	// 数量
	private EditText quantityEditText;
	// 单价
	private ui.custom.component.NumberText priceNumerText;
	// 总金额
	private TextView amountTextView;

	// 保存按钮
	private Button saveBtn;
	private Button add_button;
	private LinearLayout buttonll;

	// 根view
	private FrameLayout rootView;

	// 数据库

	private Boolean flag;

	// 拍照
	public static final int IMAGE_CAPTURE = 0;

	// 相册
	public static final int ACTION_GET_CONTENT = 1;

	public static final int ALBUM = 2;

	public DbRequestModel dbmodel;

	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	// ////////////id 如有有id者查询数据
	public int detailId = 0;

	public String status;

	// 0 为从记一笔出来的,或从列表的+进来 1为老数据
	public int fromFlag;

	// 百度定位
	public LocationClient mLocationClient;

	public BDLocation location;

	public int MAX_SIZE = 300000;

	// 解决光标位置问题
	OnKeyListener keylistener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			EditText editText = (EditText) v;
			editText.setSelection(editText.getText().length());
			return false;
		}
	};

	// 图片压缩配置
	BitmapFactory.Options opts;

	// uri

	Uri photoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail_line);

		buildAllviews();

		dbmodel = new DbRequestModel(this);
		mLocationClient = HrmexpApplication.getApplication().mLocationClient;

		detailId = getIntent().getIntExtra("detailId", 0);

		if (detailId == 0) {
			// 新数据
			fromFlag = 0;
		} else {
			// 老数据
			fromFlag = 1;
		}

		// 默认压缩4倍
		opts = new BitmapFactory.Options();
		opts.inSampleSize = 4;
		opts.inJustDecodeBounds = false;
		opts.inInputShareable = true;
		opts.inPurgeable = true;

		// 默认情况
		if (detailId == 0) {
			ImageItem.mMemoryCache.put("imageList", imageList);
			mLocationClient.requestLocation();
			location = mLocationClient.getLastKnownLocation();
			if (location != null) {
				String province = location.getProvince();
				String city = location.getCity();
				if (province != null && city != null) {

					placeEditText.setText(province + ">" + city);
				}

			}

		} else {

			dbmodel.query(MOBILE_EXP_REPORT_LINE.class, " id = " + detailId,
					null);

		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		// if(data == null){
		// return;
		// }

		Bitmap bitmap;
		Uri originalUri;
		byte[] content = null;

		switch (requestCode) {
		case IMAGE_CAPTURE:

			if (resultCode == 0) {
				return;
			}
			originalUri = photoUri;

			try {

				Uri uri = Uri.parse(originalUri.toString());

				Cursor cursor = null;
				String fileName;
				try {
					String[] proj = { MediaStore.Images.Media.DATA };
					cursor = this.getContentResolver().query(uri, proj, null,
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

				content = Util.readStream(getContentResolver().openInputStream(
						uri));

				bitmap = Util.CompressBytes(content, fileName);
				content = Util.CompressBytes(content, this.MAX_SIZE, fileName);

				photoImgView.setImageBitmap(bitmap);
				imageList.add(new ImageItem(content, bitmap));

				System.gc();

			} catch (Exception e) {
				Toast.makeText(DetailLineActivity.this, "获取相册图片失败",
						Toast.LENGTH_LONG).show();

				e.printStackTrace();

				return;
			}
			break;
		case ACTION_GET_CONTENT:
			if (data == null) {
				return;
			}

			originalUri = data.getData();

			try {

				content = Util.readStream(getContentResolver().openInputStream(
						Uri.parse(originalUri.toString())));

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

				bitmap = Util.CompressBytes(content, fileName);
				content = Util.CompressBytes(content, this.MAX_SIZE, fileName);

				photoImgView.setImageBitmap(bitmap);
				imageList.add(new ImageItem(content, bitmap));

				System.gc();

			} catch (Exception e) {
				Toast.makeText(DetailLineActivity.this, "获取相册图片失败",
						Toast.LENGTH_LONG).show();

				e.printStackTrace();

				return;
			}

			break;
		case CURRENCY_CONTENT:
			if (data == null) {

				return;
			}

			Bundle bundle = data.getExtras();
			String currency = bundle.getString("currency");
			String exchangeRate = bundle.getString("exchangeRate");
			currenyTextView.setText(currency);
			exchangeRateTextView.setText(exchangeRate);
			break;

		case ALBUM:
			if (ImageItem.mMemoryCache.get("imageList").size() == 0) {

				photoImgView.setImageResource(R.drawable.camera);

			} else {
				Bitmap bm = ImageItem.mMemoryCache.get("imageList").get(0).bm;
				photoImgView.setImageBitmap(bm);

			}

			break;

		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (status != null && status.equalsIgnoreCase("upload")) {
			if (!ViewUtl.inRangeOfView(returnImgBtn, event))
				// 拦截所有事件
				return true;
		}

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			if (imm != null) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

			}
			expenseTypePicker.dismiss();
			return super.dispatchTouchEvent(event);

		}

		return super.dispatchTouchEvent(event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		// 退出的时候清理缓存
		ImageItem.mMemoryCache = new HashMap<String, ArrayList<ImageItem>>();
		System.gc();

		if (fromFlag == 1) {
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
		}
	}

	// ///////////////private //////////////////////////////////

	// 对保存按钮进行动画操作
	private void btnAnimation() {
		ObjectAnimator anima = ObjectAnimator.ofInt(new ViewWrapper(saveBtn),
				"width", saveBtn.getWidth(), saveBtn.getWidth() / 2);
		anima.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				add_button.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub

			}
		});

		anima.setDuration(300).start();

	}

	private void buildAllviews() {
		// 绑定返回按钮
		returnImgBtn = (TextView) findViewById(R.id.return_btn);
		returnImgBtn.setOnClickListener(this);

		rootView = (FrameLayout) findViewById(R.id.framelayout);

		// 费用类型
		expenseTypell = (LinearLayout) findViewById(R.id.expense_type);
		expenseTypell.setOnClickListener(this);
		expenseTypeTextView = (TextView) findViewById(R.id.detailTypeLabel);
		expenseTypeTextView.setOnClickListener(this);
		expenseTypePicker = new ExpenseTypePopwindow(this, expenseTypeTextView);

		// 币种和汇率
		currenyTextView = (TextView) findViewById(R.id.currencyLabel);
		currenyTextView.setOnClickListener(this);
		currenyTextView.setText("CNY-人民币");
		exchangeRateTextView = (EditText) findViewById(R.id.exchangeRateLabel);
		exchangeRateTextView.setOnClickListener(this);
		exchangeRateTextView.setOnKeyListener(keylistener);
		exchangeRateTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if (s.toString().equals(".")) {
					exchangeRateTextView.setText("0.");
				} else {
					return;
				}

			}
		});
		exchangeRateTextView.setText("1.00");

		// 日期
		datell = (LinearLayout) findViewById(R.id.llcalendar_id);

		dateToTextView = (TextView) findViewById(R.id.dateToTextView);
		dateToTextView.setOnClickListener(this);
		dateFromTextView = (TextView) findViewById(R.id.dateFromTextView);
		dateFromTextView.setOnClickListener(this);

		dateToDateDialog = new DatePickerWrapDialog(this, dateToTextView);

		dateFromDateDialog = new DatePickerWrapDialog(this, dateFromTextView);
		// 照相

		photoImgView = (ImageView) findViewById(R.id.cameraImageView);
		photoImgView.setOnClickListener(this);

		new ExpenseTypePopwindow(this, expenseTypeTextView);

		// 数量单价总金额
		quantityEditText = (EditText) findViewById(R.id.quantityEditText);
		quantityEditText.addTextChangedListener(amountTextWatcher);
		quantityEditText.setOnKeyListener(keylistener);

		priceNumerText = (NumberText) findViewById(R.id.priceNumberText);
		priceNumerText.addTextChangedListener(amountTextWatcher);
		priceNumerText.setOnKeyListener(keylistener);

		amountTextView = (TextView) findViewById(R.id.amountTextView);

		// 地点
		placeEditText = (EditText) findViewById(R.id.placeEditText);
		// 增加doneEditorInfo.IME_ACTION_DONE只有对android:singleLine="true"的EditText有效。至少对HTC_A9191是这样的。
		// 有些输入法不支持
		placeEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		placeEditText.setOnKeyListener(keylistener);

		// 备注
		commentEditText = (EditText) findViewById(R.id.expense_desc_id);

		// 保存
		add_button = (Button) findViewById(R.id.add_button);
		add_button.setOnClickListener(this);
		buttonll = (LinearLayout) findViewById(R.id.buttonll);
		saveBtn = (Button) findViewById(R.id.save_button);
		saveBtn.setOnClickListener(this);

	}

	// //////////初始化界面
	private void initView(MOBILE_EXP_REPORT_LINE _record) {
		// 初始化时间
		dateFromDateDialog.setDate(_record.expense_date);
		dateToDateDialog.setDate(_record.expense_date_to);

		// 单价

		priceNumerText.setText(String.format("%f", _record.expense_amount));
		quantityEditText.setText(String.format("%d", _record.expense_number));

		placeEditText.setText(_record.expense_place);
		commentEditText.setText(_record.description);

		// 币种和汇率
		currenyTextView.setText(_record.currency);
		exchangeRateTextView.setText(String
				.format("%.2f", _record.exchangeRate));

		// 初始化picker
		try {
			expenseTypePicker.initTypePicker(_record.expense_class_id,
					_record.expense_type_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		status = _record.local_status;
		ImageItem.mMemoryCache.put("imageList", imageList);

		if (_record.item1 != null) {
			Class<? extends MOBILE_EXP_REPORT_LINE> ownerClass = _record
					.getClass();
			for (int i = 1; i < 10; i++) {
				String fieldName = "item" + i;
				try {
					Field field = ownerClass.getField(fieldName);
					byte[] content = (byte[]) field.get(_record);
					imageList.add(new ImageItem(content, Util
							.CompressBytes(content)));

				} catch (NoSuchFieldException e) {

					e.printStackTrace();

				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (NullPointerException e) {
					break;
				}

			}

			photoImgView.setImageBitmap(imageList.get(0).bm);
			// mContent = _record.item1;

		}
		if (status.equalsIgnoreCase("upload")) {

			// 添加以提交的图片
			ImageView view = new ImageView(this);
			view.setBackgroundResource(R.drawable.submitted);
			FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(
					230, 120);
			layoutparams.gravity = Gravity.BOTTOM | Gravity.CENTER;
			layoutparams.bottomMargin = 50;
			rootView.addView(view, layoutparams);

			saveBtn.setVisibility(View.INVISIBLE);
		} else if (status.equalsIgnoreCase("new")) {

			// 执行按钮动画
			// btnAnimation();
			// 不能使用动画，因为在没出现之前获取view的宽度都为0
			WindowManager manager = (WindowManager) this
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = manager.getDefaultDisplay();
			int width = display.getWidth();
			saveBtn.getLayoutParams().width = (width - 30) / 2;
			saveBtn.invalidate();
			add_button.setVisibility(View.VISIBLE);

		}

	}

	// 保存逻辑
	private void save() {
		String priceNumber = priceNumerText.getText().toString();
		String place = placeEditText.getText().toString();

		if (priceNumber.equalsIgnoreCase("")
				|| priceNumber.equalsIgnoreCase("0")
				|| place.equalsIgnoreCase("")) {
			Toast.makeText(DetailLineActivity.this, "请输入完整信息",
					Toast.LENGTH_LONG).show();
			return;
		}

		if (dateToTextView.getText().toString()
				.compareTo(dateFromTextView.getText().toString()) < 0) {

			Toast.makeText(DetailLineActivity.this, "开始日期不能小于结束日期",
					Toast.LENGTH_LONG).show();

			return;
		}

		MOBILE_EXP_REPORT_LINE line = new MOBILE_EXP_REPORT_LINE();

		line.expense_amount = Float.parseFloat(priceNumerText.getText()
				.toString());

		if (!quantityEditText.getText().toString().equalsIgnoreCase("")) {
			line.expense_number = Integer.parseInt(quantityEditText.getText()
					.toString());
		} else {
			line.expense_number = 1;

		}
		line.total_amount = line.expense_number * line.expense_amount;

		// 日期
		line.expense_date = dateFromTextView.getText().toString();
		line.expense_date_to = dateToTextView.getText().toString();

		// 费用类型
		line.expense_class_desc = expenseTypePicker.expense_class_desc;
		line.expense_type_desc = expenseTypePicker.expense_type_desc;
		line.expense_class_id = expenseTypePicker.expense_class_id;
		line.expense_type_id = expenseTypePicker.expense_type_id;

		// 币种和汇率
		line.currency = currenyTextView.getText().toString();
		float data = Float
				.parseFloat(exchangeRateTextView.getText().toString());
		DecimalFormat fnum = new DecimalFormat("##0.00");
		String exchangeRate = fnum.format(data);
		line.exchangeRate = Float.parseFloat(exchangeRate);

		// 地点
		line.expense_place = placeEditText.getText().toString();

		line.local_status = "new";

		this.imageList = ImageItem.mMemoryCache.get("imageList");

		// 图片
		for (int i = 0; i < 9; i++) {
			Class<? extends MOBILE_EXP_REPORT_LINE> ownerClass = line
					.getClass();
			String fieldName = "item".concat(String.valueOf(i + 1));
			try {
				Field field = ownerClass.getField(fieldName);
				try {
					if (i >= imageList.size()) {
						field.set(line, null);
					} else {
						field.set(line, imageList.get(i).content);
					}
				} catch (IllegalAccessException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

			} catch (NoSuchFieldException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

		}
		// line.item1 = dataList.get(0).getmContent();
		// 描述

		line.description = commentEditText.getText().toString();

		if (detailId == 0) {
			dbmodel.insert(line);
			btnAnimation();
		} else {
			dbmodel.update(line, " id = " + detailId);

		}
	}

	// //////////////弹出相册逻/////////////////
	public void showAlbum() {
		Intent intent = new Intent(this, AlbumView.class);
		ImageItem.mMemoryCache.put("imageList", imageList);

		startActivityForResult(intent, this.ALBUM);
	}

	// ////////////////弹出照相对话框////////////
	public void showCapture() {

		// 拍照逻辑
		final CharSequence[] items = { "相册", "拍照" };
		AlertDialog dlg = new AlertDialog.Builder(DetailLineActivity.this)
				.setTitle("选择图片")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 这里item是根据选择的方式，
						// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
						if (item == 1) {
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");

							SimpleDateFormat timeStampFormat = new SimpleDateFormat(
									"yyyy_MM_dd_HH_mm_ss");
							String filename = timeStampFormat
									.format(new Date());
							ContentValues values = new ContentValues();
							values.put(Media.TITLE, filename);
							photoUri = getContentResolver()
									.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											values);

							getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
									photoUri);
							// File dir = new
							// File(Environment.getExternalStorageDirectory().getAbsolutePath()
							// +"tmp");
							// if(!dir.exists())
							// dir.mkdir();
							// File file = new File(dir,"img");
							//
							//
							// getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
							// file.toURI());
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

	// ////////////////////click////////////////////////////
	@Override
	public void onClick(View v) {

		// 点击费用类型，弹出picker选择
		if (v.equals(expenseTypeTextView)) {
			expenseTypePicker.showAtLocation(rootView, Gravity.BOTTOM
					| Gravity.CENTER, 0, 0);

		} else if (v.equals(currenyTextView)) {
			Intent intent = new Intent(getApplicationContext(),
					CurrencyListActivity.class);
			startActivityForResult(intent, CURRENCY_CONTENT);
		} else if (v.equals(exchangeRateTextView)) {

		} else if (v.equals(photoImgView)) {
			if (imageList.size() == 0) {
				showCapture();
			} else {
				showAlbum();
			}
		} else if (v.equals(saveBtn)) {
			// 保存按钮
			save();

		} else if (v.equals(dateToTextView)) {
			dateToDateDialog.showDateDialog();

		} else if (v.equals(dateFromTextView)) {

			dateFromDateDialog.showDateDialog();

		} else if (v.equals(returnImgBtn)) {
			this.finish();
			// 退出的时候清理缓存
			ImageItem.mMemoryCache = new HashMap<String, ArrayList<ImageItem>>();
			System.gc();

			if (fromFlag == 1) {
				overridePendingTransition(R.anim.move_left_in_activity,
						R.anim.move_right_out_activity);
			}

		} else if (v.equals(add_button)) {
			Intent intent = new Intent(this, DetailLineActivity.class);
			startActivity(intent);
			finish();
		}

	}

	// ///////////////////////////text watch/////////////////////////
	// 当数量金额变化后动态改变总金额
	TextWatcher amountTextWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {

			// TODO 当两个输入框体没有任何值的时候，这些值为"",而不是Null
			float priceNumber, amount;
			int quantity;

			// 当还没输入值的时候 ，使用默认值
			if (priceNumerText.getText().toString().equalsIgnoreCase("")) {

				priceNumber = 0;
			} else {
				if (s.toString().charAt(0) == '.') {

					priceNumerText.setText("0".concat(s.toString()));
				}
				priceNumber = Float.parseFloat(priceNumerText.getText()
						.toString());

			}

			if (quantityEditText.getText().toString().equalsIgnoreCase("")) {

				quantity = 1;

			} else {
				quantity = Integer.parseInt(quantityEditText.getText()
						.toString());
			}

			amount = priceNumber * quantity;

			amountTextView.setText(String.format("%.2f", amount));

		}

	};

	// ////////////////////////lmdelegate/////////////////////////////////////////////////

	@Override
	public void modelDidFinshLoad(LMModel model) {
		// TODO Auto-generated method stub

		if (dbmodel.currentMethod.equalsIgnoreCase("query")) {

			MOBILE_EXP_REPORT_LINE record = (MOBILE_EXP_REPORT_LINE) dbmodel.result
					.get(0);
			initView(record);

		} else if (dbmodel.currentMethod.equalsIgnoreCase("insert")) {

			detailId = dbmodel.id;

		}

	}

	@Override
	public void modelDidStartLoad(LMModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelDidFaildLoadWithError(LMModel model) {
		// TODO Auto-generated method stub

	}

	private static class ViewWrapper {
		private View mTarget;

		public ViewWrapper(View target) {
			mTarget = target;
		}

		public int getWidth() {
			return mTarget.getLayoutParams().width;
		}

		public void setWidth(int width) {
			mTarget.getLayoutParams().width = width;
			mTarget.requestLayout();
		}

	}
}
