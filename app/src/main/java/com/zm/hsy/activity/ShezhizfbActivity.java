package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 绑定支付宝*/
public class ShezhizfbActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String code=obj.getString("code");
					if(code.equals("2")){
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}

	};

	private String userid, tag,paynum,name;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shezhizfb);
		context=this;
		Intent i = this.getIntent();
		tag=i.getStringExtra("code");
		paynum=i.getStringExtra("payment");
		name=i.getStringExtra("name");
		userid = Futil.getValue(context, "userid");
		findview();
		if(tag.equals("2")){
			submit_tv.setVisibility(View.GONE);
			payment_et.setVisibility(View.GONE);
			payname_et.setVisibility(View.GONE);
			payment_tv.setVisibility(View.VISIBLE);
			payname_tv.setVisibility(View.VISIBLE);
			payment_tv.setText(paynum);
			payname_tv.setText(name);
		}else{
			submit_tv.setVisibility(View.VISIBLE);
			payment_et.setVisibility(View.VISIBLE);
			payment_tv.setVisibility(View.GONE);
			payname_et.setVisibility(View.VISIBLE);
			payname_tv.setVisibility(View.GONE);
		}
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
		switch (v.getId()) {

		case R.id.submit_tv:
			payment=payment_et.getText().toString().trim();
			realname=payname_et.getText().toString().trim();
			if(payment.length()>0){
				setpayment();
			}else{
				Futil.showMessage(context,"请输入账号");
			}
			break;
		case R.id.back_top:
			finish();
			break;

		}
	}
	private ImageView back_top;
	private TextView payment_tv,submit_tv,payname_tv;
	private EditText payment_et,payname_et;

	private void findview() {
		submit_tv = (TextView) findViewById(R.id.submit_tv);
		submit_tv.setOnClickListener(this);
		payment_tv = (TextView) findViewById(R.id.payment_tv);
		payment_et = (EditText) findViewById(R.id.payment_et);
		payname_tv = (TextView) findViewById(R.id.payname_tv);
		payname_et = (EditText) findViewById(R.id.payname_et);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}
	private String payment,realname;
	private void setpayment() {
		startProgressDialog();
		String strUrl = URLManager.UpUser;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		map.put("belong", "0");
		map.put("payment", payment);
		map.put("realname", realname);
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
