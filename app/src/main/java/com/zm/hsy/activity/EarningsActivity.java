package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
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
/** 收益管理 */
public class EarningsActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String now=obj.getString("now_money");
					if(now.equals("0")){
						now_money.setText("暂无收益");
					}else{
						now_money.setText(""+now);
					}
				} catch (JSONException e) {
					now_money.setText("暂无收益");
				}

				try {
					String before=obj.getString("before_money");
					if(before.equals("0")){
						before_money.setText("0.00");
					}else{
						before_money.setText(""+before);
					}
				} catch (JSONException e) {
					before_money.setText("0.00");
				}

				try {
					String tol=obj.getString("tol_money");
					if(tol.equals("0")){
						tol_money.setText("0.00");
					}else{
						tol_money.setText(""+tol);
					}
				} catch (JSONException e) {
					tol_money.setText("0.00");
				}

			}else if (msg.what == URLManager.two){
				JSONObject obj = (JSONObject) msg.obj;
				try {
					code=obj.getString("code");
					if(code.equals("2")){
						payment=obj.getString("payment");
						realname=obj.getString("realname");
						das_shezhi_bangding.setText("已绑定");
					}else{
						payment="";
						das_shezhi_bangding.setText("未绑定");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}

	};

	private String userid;
	private String code,payment,realname;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_earnings);
		context = this;
		userid = Futil.getValue(context, "userid");
		findview();
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);

		getprofit();
		getaccountInfo();
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
		case R.id.das_jilu:
			ActivityJumpControl.getInstance(this).gotoDsjiluActivity();
			break;
		case R.id.das_shezhi:
			ActivityJumpControl.getInstance(this).gotoShezhizfbActivity(code,payment,realname);
			break;
		case R.id.back_top:
			finish();
			break;

		}
	}

	private TextView now_money,before_money,tol_money,das_shezhi_bangding;

	private void findview() {

		now_money = (TextView) findViewById(R.id.now_money);
		before_money = (TextView) findViewById(R.id.before_money);
		tol_money = (TextView) findViewById(R.id.tol_money);
		das_shezhi_bangding = (TextView) findViewById(R.id.das_shezhi_bangding);

		findViewById(R.id.das_jilu).setOnClickListener(this);
		findViewById(R.id.das_shezhi).setOnClickListener(this);
		findViewById(R.id.back_top).setOnClickListener(this);

	}

	private void getprofit() {
		startProgressDialog();
		String strUrl = URLManager.profit;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}
	private void getaccountInfo() {
		startProgressDialog();
		String strUrl = URLManager.UserAccountInfo;
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
