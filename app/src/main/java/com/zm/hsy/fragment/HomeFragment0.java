package com.zm.hsy.fragment;

import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment0 extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.tab_frag1, container, false);

		initView(view);
		gotoFragment(1, tab_tv1, tab_title1);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_title:
			ActivityJumpControl.getInstance(getActivity()).gotoSearchActivity();
			break;
		case R.id.tab_tv1:
			gotoFragment(1, tab_tv1, tab_title1);
			break;
		case R.id.tab_tv2:
			gotoFragment(2, tab_tv2, tab_title2);
			break;
		case R.id.tab_tv3:
			gotoFragment(3, tab_tv3, tab_title3);
			break;
		case R.id.tab_tv4:
			gotoFragment(4, tab_tv4, tab_title4);
			break;
		case R.id.tab_tv5:
			gotoFragment(5, tab_tv5, tab_title5);
			break;
		case R.id.tab_tv6:
			gotoFragment(6, tab_tv6, tab_title6);
			break;
		default:
			break;
		}
	}

	private TextView tab_tv1, tab_tv2, tab_tv3, tab_tv4, tab_tv5, tab_tv6,
			top_title_tv1;
	private ImageView tab_title1, tab_title2, tab_title3, tab_title4,
			tab_title5, tab_title6;

	private LinearLayout top_title;

	ListView listView;

	private void initView(View view) {

		// 快速注册
		tab_tv1 = (TextView) view.findViewById(R.id.tab_tv1);
		tab_tv2 = (TextView) view.findViewById(R.id.tab_tv2);
		tab_tv3 = (TextView) view.findViewById(R.id.tab_tv3);
		tab_tv4 = (TextView) view.findViewById(R.id.tab_tv4);
		tab_tv5 = (TextView) view.findViewById(R.id.tab_tv5);
		tab_tv6 = (TextView) view.findViewById(R.id.tab_tv6);
		tab_title1 = (ImageView) view.findViewById(R.id.tab_title1);
		tab_title2 = (ImageView) view.findViewById(R.id.tab_title2);
		tab_title3 = (ImageView) view.findViewById(R.id.tab_title3);
		tab_title4 = (ImageView) view.findViewById(R.id.tab_title4);
		tab_title5 = (ImageView) view.findViewById(R.id.tab_title5);
		tab_title6 = (ImageView) view.findViewById(R.id.tab_title6);
		top_title = (LinearLayout) view.findViewById(R.id.top_title);
		top_title_tv1 = (TextView) view.findViewById(R.id.top_title_tv1);

		tab_tv1.setSelected(true);

		top_title.setOnClickListener(this);
		tab_tv1.setOnClickListener(this);
		tab_tv2.setOnClickListener(this);
		tab_tv3.setOnClickListener(this);
		tab_tv4.setOnClickListener(this);
		tab_tv5.setOnClickListener(this);
		tab_tv6.setOnClickListener(this);
	}

	private Fragment contentFragment;
	// fragment管理者
	private FragmentManager fragmentManager;
	// 开启一个Fragment事务
	private FragmentTransaction transaction;
	private ArrayList<TextView> tvlist = new ArrayList<TextView>();
	private ArrayList<ImageView> ivlist = new ArrayList<ImageView>();

	private void gotoFragment(int tag, TextView tv, ImageView iv) {
		fragmentManager = getChildFragmentManager();
		transaction = fragmentManager.beginTransaction();
		top_title_tv1.setVisibility(View.GONE);
		top_title.setVisibility(View.GONE);
		if (tag == 1) {
			top_title_tv1.setVisibility(View.VISIBLE);
			contentFragment = new TabVF1();
			transaction.replace(R.id.tab_frag1_framelayout, contentFragment);
			transaction.commit();
		} else if (tag == 2) {
			top_title.setVisibility(View.VISIBLE);
			contentFragment = new TabVF2();
			transaction.replace(R.id.tab_frag1_framelayout, contentFragment);
			transaction.commit();
		} else if (tag == 3) {
			top_title.setVisibility(View.VISIBLE);
			contentFragment = new TabVF3();
			transaction.replace(R.id.tab_frag1_framelayout, contentFragment);
			transaction.commit();
		} else if (tag == 4) {
			top_title.setVisibility(View.VISIBLE);
			contentFragment = new TabVF4();
			transaction.replace(R.id.tab_frag1_framelayout, contentFragment);
			transaction.commit();
		} else if (tag == 5) {
			top_title.setVisibility(View.VISIBLE);
			contentFragment = new TabVF5();
			transaction.replace(R.id.tab_frag1_framelayout, contentFragment);
			transaction.commit();
		} else if (tag == 6) {
			top_title.setVisibility(View.VISIBLE);
			contentFragment = new TabVF6();
			transaction.replace(R.id.tab_frag1_framelayout, contentFragment);
			transaction.commit();
		}
		for (int i = 0; i < tvlist.size(); i++) {
			tvlist.remove(i).setTextColor(Color.parseColor("#000000"));
		}
		tv.setTextColor(Color.parseColor("#1abc9c"));
		tvlist.add(tv);
		for (int i = 0; i < ivlist.size(); i++) {
			ivlist.remove(i).setVisibility(View.GONE);
		}
		iv.setVisibility(View.VISIBLE);
		ivlist.add(iv);
	}

	private CustomProgressDialog progressDialog;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(getActivity());
			progressDialog.setMessage("加载中...");
		}

		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

}
