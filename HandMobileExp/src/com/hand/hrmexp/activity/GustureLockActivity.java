package com.hand.hrmexp.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Text;

import com.hand.hrmexp.activity.LockPatternView.Cell;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hand.R;

public class GustureLockActivity extends Activity implements
		LockPatternView.OnPatternListener, OnClickListener {

	private static final String TAG = "LockSetupActivity";
	private LockPatternView lockPatternView;
	private TextView resetView;
	private TextView hintView;

	private static final int STEP_1 = 1; // start
	private static final int STEP_2 = 2; // set
	private static final int STEP_3 = 3; // alert
	private static final int STEP_4 = 4; // confirm

	private int step;

	private List<LockPatternView.Cell> choosePattern;

	private boolean confirm = false;
	
	private Animation shake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_lock);
		lockPatternView = (LockPatternView) findViewById(R.id.lockPattern);
		lockPatternView.setOnPatternListener(this);
		
		hintView = (TextView) findViewById(R.id.hint);
		resetView = (TextView) findViewById(R.id.reSet);
		resetView.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				step = STEP_1;
				updateView();
			}
		});
		step = STEP_1;
		updateView();
		shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	}

	private void updateView() {
		switch (step) {
		case STEP_1:
			resetView.setVisibility(View.INVISIBLE);
			hintView.setText(R.string.ready);
			hintView.setTextColor(Color.rgb(00, 255, 255));
			choosePattern = null;
			confirm = false;
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
			break;
		case STEP_2:
			resetView.setVisibility(View.VISIBLE);
			resetView.setText(R.string.try_again);
			lockPatternView.disableInput();
			step = STEP_3;
			updateView();
			break;
		case STEP_3:
//			resetView.setVisibility(View.INVISIBLE);
			hintView.setText(R.string.goon);
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
			break;
		case STEP_4:
			if (confirm) {
				lockPatternView.disableInput();
				//save
				SharedPreferences preferences=getSharedPreferences("gustureLock",Context.MODE_APPEND);
				preferences.edit().putString("choosePattern",LockPatternView.patternToString(choosePattern)).commit();
				//显示Toast
				resetView.setVisibility(View.INVISIBLE);
				Toast.makeText(this, "手势密码修改成功", Toast.LENGTH_LONG).show();
				finish();
			} else {
				hintView.setText(R.string.wrong);
				hintView.startAnimation(shake);
				hintView.setTextColor(Color.rgb(204, 51, 51));
				lockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				lockPatternView.enableInput();
			}
			break;
		default:
			break;

		}
	}


	@Override
	public void onBackPressed () {
		finish();
		overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
		super.onBackPressed();
	}
	
	@Override
	public void onPatternStart() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onPatternCleared() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onPatternDetected(List<LockPatternView.Cell> pattern) {
		Log.d(TAG, "onPatternDetected");

		if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
			Toast.makeText(this,
					R.string.lockpattern_recording_incorrect_too_short,
					Toast.LENGTH_LONG).show();
			lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
			return;
		}

		if (choosePattern == null) {
			choosePattern = new ArrayList<LockPatternView.Cell>(pattern);
			Log.d(TAG,
					"choosePattern = "
							+ Arrays.toString(choosePattern.toArray()));

			step = STEP_2;
			updateView();
			return;
		}

		Log.d(TAG,
				"choosePattern = " + Arrays.toString(choosePattern.toArray()));
		Log.d(TAG, "pattern = " + Arrays.toString(pattern.toArray()));

		if (choosePattern.equals(pattern)) {
			// Log.d(TAG, "pattern = "+pattern.toString());
			// Log.d(TAG, "pattern.size() = "+pattern.size());
			Log.d(TAG, "pattern = " + Arrays.toString(pattern.toArray()));

			confirm = true;
		} else {
			confirm = false;
		}

		step = STEP_4;
		updateView();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		
	}

}
