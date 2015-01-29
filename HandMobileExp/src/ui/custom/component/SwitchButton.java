package ui.custom.component;

import com.hand.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class SwitchButton extends View implements OnClickListener {
	// 图片
	private Bitmap mSwitchBottom, mSwitchThumb, mSwitchFrame, mSwitchMask;
	// 默认位置及状态
	private float mCurrentX = 0;
	private boolean mSwitchOn = true;// 开关默认是开着的
	// 滑动距离
	private int mMoveLength;
	// 目标区域大小
	private Rect mDest = null;
	// 源图片大小
	private Rect mSrc = null;
	// 偏移量
	private int mDeltX = 0;
	// 滑动后位置,即下一次滑动的有效位置
	private float mLastX = 0;
	
	private Paint mPaint = null;

	private Boolean mFlag = false;
	
	private OnChangeListener mListener = null;  

	public SwitchButton(Context context) {
		this(context, null);
		// TODO 自动生成的构造函数存根
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);  
	}
	
	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle); 
		// TODO 自动生成的构造函数存根
		init();
	}

	public void init() {
		mSwitchBottom = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_bottom);

		mSwitchFrame = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_frame);

		mSwitchMask = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_mask);

		mSwitchThumb = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_btn_pressed);

		setOnClickListener(this);

		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO 自动生成的方法存根
				return false;
			}
		});
		mMoveLength = mSwitchBottom.getWidth() - mSwitchFrame.getWidth();
//		mMoveLength = mSwitchBottom.getWidth();
		mDest = new Rect(0, 0, mSwitchFrame.getWidth(),
				mSwitchFrame.getHeight());
		mSrc = new Rect();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setAlpha(255);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mDeltX > 0 || mDeltX == 0 && mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(mMoveLength - mDeltX, 0, mSwitchBottom.getWidth(),
						mSwitchFrame.getHeight());
			}
		} else if (mDeltX < 0 || mDeltX == 0 && !mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(-mDeltX, 0, mSwitchFrame.getWidth() - mDeltX,
						mSwitchFrame.getHeight());
			}
		}
		int count = canvas.saveLayer(new RectF(mDest), null,
				Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG
						| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
						| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
						| Canvas.CLIP_SAVE_FLAG);
		canvas.drawBitmap(mSwitchBottom, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchThumb, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchFrame, 0, 0, null);
		canvas.drawBitmap(mSwitchMask, 0, 0, mPaint);
		canvas.restoreToCount(count);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			mCurrentX = event.getX();
			mDeltX = (int) (mCurrentX - mLastX);
			// 如果开关开着向左滑动，或者开关关着就向右滑动
			if((mSwitchOn && mDeltX < 0 )||(!mSwitchOn && mDeltX > 0)){
				mFlag = true;
				mDeltX = 0;
			}
			//设置滑动界限
			if(Math.abs(mDeltX) > mMoveLength){
				mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength; 
			}
			//重新加载页面
			invalidate();
			return true;
		case MotionEvent.ACTION_UP:
			//滑动位置不足复位的情况
			if (Math.abs(mDeltX) > 0 && Math.abs(mDeltX) < mMoveLength / 2){
				mDeltX = 0;
				invalidate();
				return true;
			} else if(Math.abs(mDeltX) > mMoveLength / 2 && Math.abs(mDeltX) <= mMoveLength){
				mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
				mSwitchOn = !mSwitchOn;
				if(mListener != null){
					mListener.onChange(this, mSwitchOn);
				}
				invalidate();
				mDeltX = 0;
				return true;
			} else if(mDeltX == 0 && mFlag){
				//已经处理完毕的状态
				mDeltX = 0;
				mFlag = false;
				return true;
			}
			return super.onTouchEvent(event);
		default:
			break;
		}
		invalidate();
		return super.onTouchEvent(event);
		
	}

	public void setOnChangeListener(OnChangeListener listener) {
		mListener = listener; 
	}
	
	public interface OnChangeListener {
		public void onChange(SwitchButton sb, boolean state);
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		mDeltX = mSwitchOn ? mMoveLength : -mMoveLength;
		mSwitchOn = !mSwitchOn;
		if(mListener != null){
			mListener.onChange(this, mSwitchOn);
		}
		invalidate();
		mDeltX = 0;
	}

	/**
	 * 
	 * 返回开关的状态
	 * @return 开关状态
	 */
	
	public Boolean getSwitchStatus(){
		return mSwitchOn;
	}
	
	/**
	 * 
	 * 改变开关状态
	 * @param flag
	 */
	
	public void initStatus(Boolean flag) {
		mSwitchOn = flag;
		invalidate();
	}
	
}
