package com.zm.hsy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.util.CustomProgressDialog;

import java.math.BigDecimal;

import cn.jpush.android.api.JPushInterface;
/** 清除缓存 */
@SuppressLint("SimpleDateFormat")
public class SettingCleanActivity extends Activity implements OnClickListener {

	private String userid, communityid;
	private String cacheSize, downloadSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_cleanspace);

		Intent i = this.getIntent();
		communityid = i.getStringExtra("id");
		userid = Futil.getValue(SettingCleanActivity.this, "userid");

		findview();

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
	private void setsize() {
		try {
			cacheSize = Futil.getTotalCacheSize(SettingCleanActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
			cacheSize = "0.00KB";
		}
		String cacheR = substringR(cacheSize);
		double cacheL = kiloByte(cacheR, substringL(cacheSize));
		System.out.println("cacheL--" + cacheL);
		System.out.println("cacheR--" + cacheR);

		String downloadR = substringR(cacheSize);
		double downloadL = kiloByte(downloadR, substringL(cacheSize));
		System.out.println("downloadL--" + downloadL);
		System.out.println("downloadR--" + downloadR);

		double totalityL = cacheL + downloadL;

		String totalitysize = getSize(totalityL);
		System.out.println("totalityL---"+totalityL);
		System.out.println("totalitysize---"+totalitysize);
		cache.setText(cacheSize);
	}

	protected void onRestart() {
		super.onRestart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.clear_item3_img:
			Futil.clearAllCache(SettingCleanActivity.this);
			setsize();
			break;

		case R.id.back_top:
			finish();
			break;
		}
	}

	private ImageView back_top, clearcache;
	private TextView cache, download, totality;

	private void findview() {

		totality = (TextView) findViewById(R.id.clear_item1_tv2);
		download = (TextView) findViewById(R.id.clear_item2_tv2);
		cache = (TextView) findViewById(R.id.clear_item3_tv2);
		setsize();
		clearcache = (ImageView) findViewById(R.id.clear_item3_img);
		clearcache.setOnClickListener(this);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	/**
	 * 单位转换成KB
	 * 
	 * @return
	 */
	public double kiloByte(String string, double db) {
		double kiloByte = 0.00;
		if (string.equals("KB")) {
			kiloByte = db;
		} else if (string.equals("MB")) {
			kiloByte = db * 1024;
		} else if (string.equals("GB")) {
			kiloByte = db * 1024 * 1024;
		} else if (string.equals("TB")) {
			kiloByte = db * 1024 * 1024 * 1024;
		}
		return kiloByte;
	}

	/**
	 * 格式化单位
	 * 
	 * @return
	 */
	public String getSize(double size) {

		double megaByte = size / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(size));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	/**
	 * 截取字符串
	 * 
	 * @return
	 */
	public double substringL(String string) {
		String cacheL = "";
		int cs = string.length();
		cacheL = string.substring(0, cs - 2);
		double db = Double.valueOf(cacheL);
		return db;
	}

	/**
	 * 截取字符串
	 * 
	 * @return
	 */
	public String substringR(String string) {
		String cacheR = "";
		int cs = string.length();
		cacheR = string.substring(cs - 2, cs);
		return cacheR;
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
