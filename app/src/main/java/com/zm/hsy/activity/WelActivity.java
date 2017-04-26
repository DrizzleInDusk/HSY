package com.zm.hsy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.AdvertisementAdapter;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

public class WelActivity extends Activity implements OnPageChangeListener {
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				System.out.println("WelActivity>>>"+obj);
				try {
					JSONArray jsonArray = obj.getJSONArray("adsStartList");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String picture = jsonObject.getString("picture");
						picture = URLManager.Ads_URL + picture;
						urls.add(picture);
					}
					findview();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wel);
		vp = (ViewPager) findViewById(R.id.viewpager);
		views = new ArrayList<View>();
		gointo();

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

	private AdvertisementAdapter vpAdapter;
	private ViewPager vp;
	private int size;
	private View start_btn;
	private ArrayList<View> views;
	List<String> urls = new ArrayList<>();

	private void findview() {
		size = urls.size();
		for (int i = 0; i < size + 1; i++) {
			if (i != urls.size()) {
				ImageView constructViews = constructViews(urls.get(i));
				views.add(constructViews);
			} else {
				ImageView imageView = new ImageView(this);
				imageView.setBackgroundResource(R.color.ashy_light);
				views.add(imageView);
			}
		}
		vpAdapter = new AdvertisementAdapter(views);
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(this);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == size) {
			Intent intent = new Intent(WelActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.enter_right_to_left,
					R.anim.exit_right_to_left);
		}
	}

	/**
	 * 进入时调用
	 */
	private void gointo() {
		startProgressDialog();
		String strUrl = URLManager.adsList;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("places", "1");
		Futil.xutils(strUrl, map, handler, URLManager.one);

	}

	/* 加载图片 */
	private ImageView constructViews(String ImageUrl) {
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setLayoutParams(new MarginLayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		Picasso.with(this).load(ImageUrl).into(imageView);
		return imageView;
	}

	/**
	 * 等待页
	 */
	private CustomProgressDialog progressDialog;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
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
