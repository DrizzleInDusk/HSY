package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 管理中心-加V认证 */
public class AddVActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					hStatus = obj.getString("hStatus");
					if (hStatus.equals("2")) {
						addv_head_add.setVisibility(View.GONE);
						addv_head_tv2.setVisibility(View.VISIBLE);
						addv_head_iv.setVisibility(View.VISIBLE);
					} else {
						addv_head_add.setVisibility(View.VISIBLE);
						addv_head_tv2.setVisibility(View.GONE);
						addv_head_iv.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					addv_head_add.setVisibility(View.VISIBLE);
					addv_head_tv2.setVisibility(View.GONE);
					addv_head_iv.setVisibility(View.GONE);
				}
				try {
					mStatus = obj.getString("mStatus");
					if (mStatus.equals("2")) {
						addv_shouji_add.setVisibility(View.GONE);
						addv_shouji_tv2.setVisibility(View.VISIBLE);
						addv_shouji_iv.setVisibility(View.VISIBLE);
					} else {
						addv_shouji_add.setVisibility(View.VISIBLE);
						addv_shouji_tv2.setVisibility(View.GONE);
						addv_shouji_tv2.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					addv_shouji_add.setVisibility(View.VISIBLE);
					addv_shouji_tv2.setVisibility(View.GONE);
					addv_shouji_tv2.setVisibility(View.GONE);
				}
				try {
					aStatus = obj.getString("aStatus");
					if (aStatus.equals("2")) {
						addv_luyin_add.setVisibility(View.GONE);
						addv_luyin_iv.setVisibility(View.VISIBLE);
						addv_luyin_tv2.setVisibility(View.VISIBLE);
					} else {
						addv_luyin_add.setVisibility(View.VISIBLE);
						addv_luyin_iv.setVisibility(View.GONE);
						addv_luyin_tv2.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					addv_luyin_add.setVisibility(View.VISIBLE);
					addv_luyin_iv.setVisibility(View.GONE);
					addv_luyin_tv2.setVisibility(View.GONE);
				}
				canclick();
			}if(msg.what == URLManager.two){
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String message = obj.getString("message");
					if(!message.equals("null")&&!message.equals("")&&message!=null){
						Futil.showMessage(context, message);
					}
				} catch (JSONException e) {
				}
			}
			stopProgressDialog();
		}

	

	};
	
	private String userid;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addv);
		context = this;
		userid = Futil.getValue(context, "userid");
		findview();
		
	}

	protected void onRestart() {

		super.onRestart();
	}

	
	
	private String aStatus="",mStatus="",hStatus="";
	private void canclick() {
		if(aStatus.equals("2")&&mStatus.equals("2")&&hStatus.equals("2")){
			jiaVCertification.setEnabled(true);
		}else{
			jiaVCertification.setEnabled(false);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.jiaVCertification:
			addv();
			break;

		case R.id.addv_head_add:
			ActivityJumpControl.getInstance(AddVActivity.this)
			.gotoInformationActivity();
			break;

		case R.id.addv_shouji_add:
			ActivityJumpControl.getInstance((Activity) context)
					.gotoBangdingSjActivity();
			break;
		case R.id.addv_luyin_add:
			ActivityJumpControl.getInstance((Activity)context).gotoMyAudioActivity(
					"0", "1", "我的声音");
			break;

		case R.id.back_top:
			finish();
			break;

		}
	}
	@Override
	protected void onResume() {
		jiaVAuthentication();

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

	private TextView addv_head_add, addv_head_tv2, addv_shouji_add,
			addv_shouji_tv2, addv_luyin_add, addv_luyin_tv2;
	private ImageView addv_head_iv, addv_shouji_iv, addv_luyin_iv;
	private TextView jiaVCertification;
		
	private void findview() {

		jiaVCertification = (TextView) findViewById(R.id.jiaVCertification);
		jiaVCertification.setOnClickListener(this);
		
		addv_head_add = (TextView) findViewById(R.id.addv_head_add);
		addv_head_add.setOnClickListener(this);
		addv_head_iv = (ImageView) findViewById(R.id.addv_head_iv);
		addv_head_tv2 = (TextView) findViewById(R.id.addv_head_tv2);

		addv_shouji_add = (TextView) findViewById(R.id.addv_shouji_add);
		addv_shouji_add.setOnClickListener(this);
		addv_shouji_iv = (ImageView) findViewById(R.id.addv_shouji_iv);
		addv_shouji_tv2 = (TextView) findViewById(R.id.addv_shouji_tv2);

		addv_luyin_add = (TextView) findViewById(R.id.addv_luyin_add);
		addv_luyin_add.setOnClickListener(this);
		addv_luyin_iv = (ImageView) findViewById(R.id.addv_luyin_iv);
		addv_luyin_tv2 = (TextView) findViewById(R.id.addv_luyin_tv2);

		findViewById(R.id.back_top).setOnClickListener(this);

	}

	private void jiaVAuthentication() {
		startProgressDialog();
		String strUrl = URLManager.jiaVAuthentication;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}

	private void addv() {
		startProgressDialog();
		String strUrl = URLManager.jiaVCertification;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
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
