package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 意见反馈 */
public class FeedbackActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String message=obj.getString("message");
					Futil.showMessage(context,message);
					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}

	};

	private String userid,shield;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		context =this ;
		shield= Futil.getValue(context,"shield");
		userid = Futil.getValue(context, "userid");
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.feedback_img:
			String feeds=feedback_et.getText().toString().trim();
			if(feeds.length()>0){
				addOpinion(feeds);
			}else{
				Futil.showMessage(context,"请输入想返回的内容");
			}
			break;
		case R.id.back_top:
			finish();
			break;

		}
	}
	private ImageView back_top,feedback_img;
	private EditText contact_et,feedback_et;

	private void findview() {

//		contact_et = (EditText) findViewById(R.id.contact_et);
		feedback_et = (EditText) findViewById(R.id.feedback_et);
		
		feedback_img = (ImageView) findViewById(R.id.feedback_img);
		feedback_img.setOnClickListener(this);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private void addOpinion(String content) {
		startProgressDialog();
		String strUrl = URLManager.addOpinion;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", userid);
		map.put("opinion.type", shield);
		map.put("opinion.content", content);
		Futil.xutils(strUrl, map, handler, URLManager.one);
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
