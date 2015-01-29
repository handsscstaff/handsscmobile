package com.hand.hrmexp.activity;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hand.R;
import com.hand.hrmexp.model.ExpenseTypeModel;
import com.hand.hrmexp.model.LoadingModel;
import com.hand.hrmexp.model.LoginModel;
import com.handexp.utl.ConstantsUtl;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

public class SystemSetFragment extends Fragment implements LMModelDelegate {

	private View rootview;
	private Button quitButton;
	private LinearLayout syncView;
	private LinearLayout gustureView;
	private ExpenseTypeModel model;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		rootview = inflater
				.inflate(R.layout.fragment_setting, container, false);
		buildAllViews();
		return rootview;
	}

	/**
	 * 
	 * 绑定View事件
	 * 
	 */
	private void buildAllViews() {

		
		TextView _title = (TextView) getActivity().findViewById(R.id.main_head_title);
		_title.setText("设置");
		
		model = new ExpenseTypeModel(this);
		// 退出按钮
		quitButton = (Button) rootview.findViewById(R.id.quitButton);
		quitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				getActivity().finish();
				getActivity().overridePendingTransition(0, R.anim.quit_view);
			}
		});
		// 同步按钮
		syncView = (LinearLayout) rootview.findViewById(R.id.synchData);
		syncView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				model.load(null);
			}
		});
		// 手势
		gustureView = (LinearLayout) rootview.findViewById(R.id.gusture);
		gustureView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(),GustureOptionsActivity.class);
				startActivity(intent);			
				getActivity().overridePendingTransition(R.anim.move_right_in_activity,R.anim.move_left_out_activity);				
			}
		});
	}

	@Override
	public void modelDidFinshLoad(LMModel model) {
		// TODO 自动生成的方法存根

		AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
		String json = new String(reponseModel.mresponseBody);
		try {
			JSONObject jsonobj = new JSONObject(json);
			String code = ((JSONObject) jsonobj.get("head")).get("code")
					.toString();
			if (code.equals("success")) {
				Toast.makeText(getActivity(), "同步完成", Toast.LENGTH_SHORT)
						.show();
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(getActivity());
				Editor editor = preferences.edit();
				try {
					AsHttpRequestModel model1 = (AsHttpRequestModel) model;
					editor.putString(ConstantsUtl.expenseType, new String(
							model1.mresponseBody, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				editor.commit();
			} else if (code.equals("failure")) {
				Toast.makeText(getActivity(), "同步失败", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getActivity(), "网络繁忙请稍后再试", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();

		} finally {

		}
		;
	}

	@Override
	public void modelDidStartLoad(LMModel model) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void modelDidFaildLoadWithError(LMModel model) {
		// TODO 自动生成的方法存根
		Toast.makeText(getActivity(), "网络繁忙请稍后再试", Toast.LENGTH_LONG).show();
	}

}
