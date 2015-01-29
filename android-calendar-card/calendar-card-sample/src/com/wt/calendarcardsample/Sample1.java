package com.wt.calendarcardsample;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.wt.calendarcard.CalendarCard;
import com.wt.calendarcard.CardGridItem;
import com.wt.calendarcard.OnCellItemClick;

public class Sample1 extends Activity {
	
	private CalendarCard mCalendarCard;
	private TextView mTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample1);
		mCalendarCard = (CalendarCard)findViewById(R.id.calendarCard1);
		mCalendarCard.setOnCellItemClick(new OnCellItemClick() {
			@Override
			public void onCellClick(View v, CardGridItem item) {
				mTextView.setText(getResources().getString(R.string.sel_date, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(item.getDate().getTime())));
			}
		});
		
		
		mTextView = (TextView)findViewById(R.id.textView1);
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
        	this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
