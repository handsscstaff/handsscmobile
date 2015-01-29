package ui.custom.component;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class NumberText extends EditText implements TextWatcher {
	private int precision;

	public NumberText(Context context) {
		this(context, null);
	}

	public NumberText(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.editTextStyle);
		// TODO Auto-generated constructor stub
	}

	public NumberText(Context context, AttributeSet attrs,	 int defStyle) {
		super(context, attrs,defStyle);
		
    	TypedArray a = context.obtainStyledAttributes(attrs,
                com.hand.R.styleable.NumberText, 0, 0);
     	
    	precision = a.getInteger(com.hand.R.styleable.NumberText_precision,1);
     	
		this.addTextChangedListener(this);
		this.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
				    @Override  
				    public void onFocusChange(View v, boolean hasFocus) {  
				        if(hasFocus) {
				        	//TODO 防止bug每次获得焦点清空

				        	
			
				        	
				        	
				        } else {
				        	
				        	
				
				        }
				    
				    }

			});
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		
		String temp = s.toString();

		
		int posDot = temp.indexOf(".");
		if (posDot <= 0)
			return;
		if (temp.length() - posDot - 1 > precision) {
			s.delete(posDot + 3, posDot + 4);
		}
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}

}
