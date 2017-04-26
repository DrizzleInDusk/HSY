package com.zm.hsy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

public class MusicDownActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;

			}
			stopProgressDialog();
		}

	};

	private String userid, communityid;
	private String scoreid = "0";
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
		context=this;
		Intent i = this.getIntent();
		communityid = i.getStringExtra("communityid");
		userid = Futil.getValue(context, "userid");
		findview();
	}

	protected void onRestart() {

		super.onRestart();
	}
	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPause(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.back_top:
			finish();
			break;

		}
	}
	private ImageView back_top;

	private void findview() {

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private void getscore(String e) {
		startProgressDialog();
		String strUrl = "http://192.168.1.79:8080/hsyfm/getIpAddr";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", e);
		Futil.xutils(strUrl, map, handler, URLManager.two);
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
