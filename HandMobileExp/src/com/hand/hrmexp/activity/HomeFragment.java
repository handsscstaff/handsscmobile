package com.hand.hrmexp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hand.R;
import com.hand.hrmexp.application.HrmexpApplication;
import com.hand.hrmexp.model.HomeModel;
import com.hand.hrms4android.exception.ParseExpressionException;
import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.mas.customview.ImageViewPager;

public class HomeFragment extends Fragment implements LMModelDelegate {
	private View rootview;
	private Button btn;

	private LinearLayout expDetailLinell;
	private LinearLayout uploadListll;

	private LinearLayout expNewll;

	private LinearLayout pieChartll;

	// ///三个汇总金额
	private TextView todayTextView;
	private TextView weekTextView;
	private TextView monthTextView;

	private android.support.v4.app.FragmentTransaction transaction;

	private ImageViewPager imageViewPager;

	private HomeModel model;

	private String buildExpUrl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.activity_home, container, false);
		buildAllViews();
		model = new HomeModel(this);
		model.load();
		return rootview;

	}

	@Override
	public void onResume() {
		super.onResume();
		model = new HomeModel(this);
		model.load();
		// imageViewPager.imageDisplayStart();

	}

	@Override
	public void onStop() {
		super.onStop();
		imageViewPager.imageDisplayShutdown();
	}

	// /////////////////private//////////////////////
	private void buildAllViews() {

		TextView _title = (TextView) getActivity().findViewById(
				R.id.main_head_title);
		_title.setText("首页");

		todayTextView = (TextView) rootview.findViewById(R.id.todayTextView);
		weekTextView = (TextView) rootview.findViewById(R.id.weekTextView);
		monthTextView = (TextView) rootview.findViewById(R.id.monthTextView);

		transaction = HrmexpApplication.getApplication().transaction;

		imageViewPager = (ImageViewPager) rootview
				.findViewById(R.id.imageViewPager);

		imageViewPager.setDrawables(new int[] { R.drawable.display1,
				R.drawable.display2, R.drawable.display3 });

		btn = (Button) rootview.findViewById(R.id.writeBtn);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),
						DetailLineActivity.class);
				startActivity(intent);

				// transaction.setCustomAnimations(android.R.anim.fade_in,
				// android.R.anim.fade_out);
				// transaction.replace(R.id.main_fragment, new
				// DetailLineFragment()).commit();

			}
		});

		expDetailLinell = (LinearLayout) rootview
				.findViewById(R.id.expDetailLinell);
		expDetailLinell.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(),
						DetailListActivity.class);
				startActivity(intent);

			}
		});
		uploadListll = (LinearLayout) rootview.findViewById(R.id.uploadListll);
		uploadListll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(),
						UploadListActivity.class);
				startActivity(intent);
			}
		});

		expNewll = (LinearLayout) rootview.findViewById(R.id.newExpll);
		expNewll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						HtmlBaseActivity.class);
				intent.putExtra("url", buildExpUrl);
				startActivity(intent);
			}
		});

		pieChartll = (LinearLayout) rootview.findViewById(R.id.pieChartll);
		pieChartll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(), PieCharActivity.class);
				startActivity(intent);

			}
		});

		try {
			buildExpUrl = XmlConfigReader.getInstance().getAttr(
					new Expression(
							"/backend-config/url[@name='build_exp_url']",
							"value"));
		} catch (ParseExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getActivity(), "读取生成报销单接口失败", Toast.LENGTH_LONG)
					.show();
			return;
		}

	}

	@Override
	public void modelDidFinshLoad(LMModel _model) {

		if (model.todayAmount != null) {
			
			todayTextView.setText(String.format("%.2f",
					Float.parseFloat(model.todayAmount)));
		} else {

			todayTextView.setText("0.00");
		}
		if (model.weekAmount != null) {
			
			weekTextView.setText(String.format("%.2f",
					Float.parseFloat(model.weekAmount)));
		} else {

			weekTextView.setText("0.00");
		}

		if (model.monthAmount != null) {

			monthTextView.setText(String.format("%.2f",
					Float.parseFloat(model.monthAmount)));
		} else {

			monthTextView.setText("0.00");
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

}
