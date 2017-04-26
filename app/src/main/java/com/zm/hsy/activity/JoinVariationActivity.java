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
import android.widget.RadioButton;
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
/*** 参加活动页 */
public class JoinVariationActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				System.out.println("obj---"+obj);
				try {
					String code = obj.getString("code");
					System.out.println("code---"+code);
					String message = obj.getString("message");
					System.out.println("message---"+message);
					if (code.equals("1")) {
						Futil.showMessage(context, "" + message);
					} else if (code.equals("2")) {
						Futil.showMessage(context, "" + message);
						try {
							Thread.sleep(500);
							finish();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Futil.showMessage(context, "请重试---");
				}
			} else if (msg.what == URLManager.two) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String pageviewnums = obj.getString("pageviews");
					pageviews_tv.setText("浏览数" + pageviewnums);
				} catch (JSONException e) {
					pageviews_tv.setText("浏览数0");
				}
				try {
					String signCountnums = obj.getString("signCount");
					signCount_tv.setText("报名人数" + signCountnums);
				} catch (JSONException e) {
					signCount_tv.setText("报名人数0");
				}

			}
			stopProgressDialog();
		}

	};

	private String userid, toptv, activityId;
	private Context context = JoinVariationActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joinvariation);
		userid = Futil.getValue(JoinVariationActivity.this, "userid");

		findview();

		Intent i = this.getIntent();
		toptv = i.getStringExtra("toptv");
		join_top_tv.setText(toptv);
		activityId = i.getStringExtra("variationid");
		gotoSign();
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.submit_tv:
			if (userid != null && !userid.equals("")) {
				getet();
			} else {
				Futil.showMessage(context, "请先登录");
			}
			break;
		case R.id.man:
			man.setSelected(true);
			woman.setSelected(false);
			gander = "1";
			break;
		case R.id.woman:
			man.setSelected(false);
			woman.setSelected(true);
			gander = "2";
			break;
		case R.id.back_top:
			finish();
			break;

		}
	}

	private void getet() {
		name = rl2_name.getText().toString().trim();
		age = rl3_age.getText().toString().trim();
		phonenum = join_phonenum.getText().toString().trim();
		if (name.length() == 0) {
			Futil.showMessage(context, "请输入姓名");
		} else if (age.length() == 0) {
			Futil.showMessage(context, "请输入年龄");
		} else if (phonenum.length() == 0) {
			Futil.showMessage(context, "请输入手机号");
		} else if (phonenum.length() != 11) {
			Futil.showMessage(context, "请输入11位手机号");
		} else {
			addSign();
		}

	}

	private String gander, name, age, phonenum;
	private ImageView back_top;
	private TextView join_top_tv, submit_tv, pageviews_tv, signCount_tv;
	private RadioButton man, woman;
	private EditText rl2_name, rl3_age, join_phonenum;

	private void findview() {
		join_top_tv = (TextView) findViewById(R.id.join_top_tv);
		rl2_name = (EditText) findViewById(R.id.rl2_name);
		rl3_age = (EditText) findViewById(R.id.rl3_age);
		join_phonenum = (EditText) findViewById(R.id.join_phonenum);

		submit_tv = (TextView) findViewById(R.id.submit_tv);
		submit_tv.setOnClickListener(this);

		pageviews_tv = (TextView) findViewById(R.id.pageviews_tv);
		signCount_tv = (TextView) findViewById(R.id.signCount_tv);

		man = (RadioButton) findViewById(R.id.man);
		man.setSelected(true);
		gander = "1";
		man.setOnClickListener(this);
		woman = (RadioButton) findViewById(R.id.woman);
		woman.setSelected(false);
		woman.setOnClickListener(this);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private void gotoSign() {
		startProgressDialog();
		String strUrl = URLManager.gotoActivitySign;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("activity.id", activityId);
		Futil.xutils(strUrl, map, handler, URLManager.two);
	}

	private void addSign() {
		startProgressDialog();
		String strUrl = URLManager.ActivitySign;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("activitySign.activityId", activityId);
		map.put("activitySign.name", name);
		map.put("activitySign.sex", gander);
		map.put("activitySign.phone", phonenum);
		map.put("activitySign.age", age);
		map.put("activitySign.uid", userid);
		System.out.println("activityId--" + activityId);
		System.out.println("name--" + name);
		System.out.println("gander--" + gander);
		System.out.println("phonenum--" + phonenum);
		System.out.println("userid--" + userid);
		System.out.println("age--" + age);
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
