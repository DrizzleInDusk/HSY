package com.zm.hsy.activity;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.MyFragmentPagerAdapter;
import com.zm.hsy.fragment.RingF1;
import com.zm.hsy.fragment.RingF2;
import com.zm.hsy.fragment.RingF3;
import com.zm.hsy.fragment.RingF4;
import com.zm.hsy.https.Futil;
import com.zm.hsy.myview.CustomViewPager;
import com.zm.hsy.util.CustomProgressDialog;

/** 铃声 */
public class RingActivity extends FragmentActivity implements OnClickListener {

	private RingF1 ringf1;
	private RingF2 ringf2;
	private RingF3 ringf3;
	private RingF4 ringf4;
	private ArrayList<Fragment> fragmentsList;
	private CustomViewPager mPager;
	int currIndex1 = 0; // 设置常量

	private String userid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ring);

		userid = Futil.getValue(RingActivity.this, "userid");
		findview();
		initViewPager();
	}

	protected void onRestart() {

		super.onRestart();
	}
	@Override
	protected void onResume() {

		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPause(this);
	}
	private ArrayList<TextView> list = new ArrayList<TextView>();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ring_tv1:
			for (int i = 0; i < list.size(); i++) {
				list.remove(i).setTextColor(Color.parseColor("#000000"));
			}
			ring_tv1.setTextColor(Color.parseColor("#1abc9c"));
			list.add(ring_tv1);
			mPager.setCurrentItem(0);

			break;
		case R.id.ring_tv2:
			for (int i = 0; i < list.size(); i++) {
				list.remove(i).setTextColor(Color.parseColor("#000000"));
			}
			ring_tv2.setTextColor(Color.parseColor("#1abc9c"));
			list.add(ring_tv2);
			mPager.setCurrentItem(1);

			break;
		case R.id.ring_tv3:
			for (int i = 0; i < list.size(); i++) {
				list.remove(i).setTextColor(Color.parseColor("#000000"));
			}
			ring_tv3.setTextColor(Color.parseColor("#1abc9c"));
			list.add(ring_tv3);
			mPager.setCurrentItem(2);

			break;
		case R.id.ring_tv4:
			for (int i = 0; i < list.size(); i++) {
				list.remove(i).setTextColor(Color.parseColor("#000000"));
			}
			ring_tv4.setTextColor(Color.parseColor("#1abc9c"));
			list.add(ring_tv4);
			mPager.setCurrentItem(3);

			break;
		case R.id.back_top:
			finish();
			break;

		}
	}
	private ImageView back_top;
	private ImageView ring_img1, ring_img2, ring_img3, ring_img4;
	private TextView ring_tv1, ring_tv2, ring_tv3, ring_tv4;

	private void findview() {

		ring_img1 = (ImageView) findViewById(R.id.ring_img1);
		ring_img2 = (ImageView) findViewById(R.id.ring_img2);
		ring_img3 = (ImageView) findViewById(R.id.ring_img3);
		ring_img4 = (ImageView) findViewById(R.id.ring_img4);

		ring_tv1 = (TextView) findViewById(R.id.ring_tv1);
		ring_tv1.setOnClickListener(this);
		ring_tv2 = (TextView) findViewById(R.id.ring_tv2);
		ring_tv2.setOnClickListener(this);
		ring_tv3 = (TextView) findViewById(R.id.ring_tv3);
		ring_tv3.setOnClickListener(this);
		ring_tv4 = (TextView) findViewById(R.id.ring_tv4);
		ring_tv4.setOnClickListener(this);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}
	private void initViewPager() {
		mPager = (CustomViewPager) findViewById(R.id.ring_vPager);
		fragmentsList = new ArrayList<Fragment>();
		// 不同的Fragment传入的是不同的值，这个值用来在TestFragment类中的onCreatView()方法中根据这个
		// 传进来的int值返回不同的View
		ringf1 = new RingF1();
		ringf2 = new RingF2();
		ringf3 = new RingF3();
		ringf4 = new RingF4();
		fragmentsList.add(ringf1);
		fragmentsList.add(ringf2);
		fragmentsList.add(ringf3);
		fragmentsList.add(ringf4);
		// 设置ViewPager的适配器（自定义的继承自FragmentPagerAdapter的adapter）
		// 参数分别是FragmentManager和装载着Fragment的容器
		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentsList));
		Log.i("getFragmentManager()", "" + getFragmentManager());
		// 设置默认是第一页currIndex = 0

		// 设置ViewPager的页面切换监听事件
		startProgressDialog();
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		stopProgressDialog();
	}

	// 这个是ViewPager的页面切换事件监听器
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg1) {

			switch (arg1) {

			// 下面的意思是：拿第一个case 0举例，如果切换的是到case 0
			// 下面的if的意思是，如果现在是在序号为1的界面切换过来到序号为0的页面
			// 那么执行动画，并把一开始所在界面上的文字颜色设为轻亮，最后设置当前页面的文字为高亮
			// TranslateAnimation的四个参数代表的意思是：动画起始x,终结x，起始y,终结y位置
			// setVisibility 0=可见 4=不可见
			case 0:
				ring_img1.setVisibility(View.VISIBLE);
				ring_img2.setVisibility(View.GONE);
				ring_img3.setVisibility(View.GONE);
				ring_img4.setVisibility(View.GONE);

				break;
			case 1:
				ring_img1.setVisibility(View.GONE);
				ring_img2.setVisibility(View.VISIBLE);
				ring_img3.setVisibility(View.GONE);
				ring_img4.setVisibility(View.GONE);
				break;
			case 2:
				ring_img1.setVisibility(View.GONE);
				ring_img2.setVisibility(View.GONE);
				ring_img3.setVisibility(View.VISIBLE);
				ring_img4.setVisibility(View.GONE);
				break;
			case 3:
				ring_img1.setVisibility(View.GONE);
				ring_img2.setVisibility(View.GONE);
				ring_img3.setVisibility(View.GONE);
				ring_img4.setVisibility(View.VISIBLE);
				break;

			}
			currIndex1 = arg1;
			// Log.i("title选择的是",""+ arg1);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private CustomProgressDialog progressDialog;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(RingActivity.this);
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
