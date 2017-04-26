package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import cn.jpush.android.api.JPushInterface;
/** 隐私设置*/
public class YinsiActivity extends Activity implements OnClickListener {

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yinsi);
		context=this;
		findview();
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
		case R.id.tongyong:
			ActivityJumpControl.getInstance(YinsiActivity.this)
					.gotoYinsiTyActivity();
			break;
		case R.id.heimingdan:
			ActivityJumpControl.getInstance(YinsiActivity.this)
					.gotoYinsiHmdActivity();
			break;
		case R.id.xinxianshi:
			ActivityJumpControl.getInstance(YinsiActivity.this)
					.gotoYinsiXxsActivity();
			break;

		}
	}
	private void findview() {
		findViewById(R.id.back_top).setOnClickListener(this);
		findViewById(R.id.tongyong).setOnClickListener(this);
		findViewById(R.id.heimingdan).setOnClickListener(this);
		findViewById(R.id.xinxianshi).setOnClickListener(this);

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
