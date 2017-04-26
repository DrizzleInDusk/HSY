package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** Tag1忘记密码Tag2注册 */
public class ForgetPWActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String string = obj.getString("message");
					String c = obj.getString("code");
					if (c.equals("2")) {
						Toast.makeText(ForgetPWActivity.this, "" + string,
								Toast.LENGTH_LONG).show();
						time = new TimeCount(60000, 1000);
						time.start();
					} else {
						Toast.makeText(ForgetPWActivity.this, "" + string,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == URLManager.two) {// 注册
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String c = obj.getString("code");
					if (c.equals("2")) {
						try {
							JSONObject user = obj.getJSONObject("user");

							String userbelong = user.getString("belong");
							String userblurb = user.getString("blurb");
							String userhead = user.getString("head");
							String userid = user.getString("id");
							String usernickname = user.getString("nickname");
							String usermobile = user.getString("mobile");
							Futil.saveValue(ForgetPWActivity.this,
									"userbelong", userbelong);
							Futil.saveValue(ForgetPWActivity.this, "userblurb",
									userblurb);
							Futil.saveValue(ForgetPWActivity.this, "userhead",
									userhead);
							Futil.saveValue(ForgetPWActivity.this, "userid",
									userid);
							Futil.saveValue(ForgetPWActivity.this,
									"usernickname", usernickname);
							Futil.saveValue(ForgetPWActivity.this,
									"usermobile", usermobile);
							Toast.makeText(ForgetPWActivity.this, "注册成功",
									Toast.LENGTH_LONG).show();
							finish();
						} catch (JSONException e) {
							e.printStackTrace();
							stopProgressDialog();
						}
					} else {
						String string = obj.getString("message");
						Toast.makeText(ForgetPWActivity.this, "" + string,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == URLManager.three) {// 登陆-忘记密码
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String c = obj.getString("code");
					if (c.equals("2")) {
						try {
							JSONObject user = obj.getJSONObject("user");

							String userbelong = user.getString("belong");
							String userblurb = user.getString("blurb");
							String userhead = user.getString("head");
							String userid = user.getString("id");
							String usernickname = user.getString("nickname");
							String usermobile = user.getString("mobile");
							Futil.saveValue(ForgetPWActivity.this,
									"userbelong", userbelong);
							Futil.saveValue(ForgetPWActivity.this, "userblurb",
									userblurb);
							Futil.saveValue(ForgetPWActivity.this, "userhead",
									userhead);
							Futil.saveValue(ForgetPWActivity.this, "userid",
									userid);
							Futil.saveValue(ForgetPWActivity.this,
									"usernickname", usernickname);
							Futil.saveValue(ForgetPWActivity.this,
									"usermobile", usermobile);
							Toast.makeText(ForgetPWActivity.this, "修改密码成功",
									Toast.LENGTH_LONG).show();
							finish();
						} catch (JSONException e) {
							e.printStackTrace();
							stopProgressDialog();
						}
					} else {
						String string = obj.getString("message");
						Toast.makeText(ForgetPWActivity.this, "" + string,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}

	};
	private String yzCode, Tag, TopName;
	private String strUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);

		Intent i = this.getIntent();
		findview();
		Tag = i.getStringExtra("Tag");
		TopName = i.getStringExtra("TopName");
		forget_top_tv1.setText(TopName);
		if (Tag.equals("1")) {// 忘记密码
			strUrl = URLManager.SendFCode;
			forget_log_in.setVisibility(View.VISIBLE);
			register.setVisibility(View.GONE);
			forget_newpw.setVisibility(View.VISIBLE);
			forget_password.setVisibility(View.GONE);
			usertype.setVisibility(View.GONE);
		} else {// 注册
			strUrl = URLManager.SendRCode;
			forget_log_in.setVisibility(View.GONE);
			register.setVisibility(View.VISIBLE);
			forget_newpw.setVisibility(View.GONE);
			forget_password.setVisibility(View.VISIBLE);
			usertype.setVisibility(View.VISIBLE);
		}
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
	private TimeCount time;
	private String phonenum, password;
	private String individual = null;// 1个人2公司

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.type_qiye:// 注册页 企业
			type_qiye.setSelected(true);
			type_geren.setSelected(false);
			individual = "2";
			break;
		case R.id.type_geren:// 注册页 个人
			type_qiye.setSelected(false);
			type_geren.setSelected(true);
			individual = "1";
			break;
		case R.id.register:// 注册
			if (forget_password.getText().length() == 0) {
				Futil.showMessage(ForgetPWActivity.this, "请填写密码");
			} else {
				phonenum = forget_phonenum.getText().toString().trim();
				yzCode = forget_yanzhengma.getText().toString();
				password = forget_password.getText().toString();
				registe();
			}
			break;
		case R.id.forget_log_in:// 登陆-忘记密码
			if (forget_newpw.getText().length() == 0) {
				Futil.showMessage(ForgetPWActivity.this, "请填写新密码");
			} else {
				phonenum = forget_phonenum.getText().toString().trim();
				yzCode = forget_yanzhengma.getText().toString();
				password = forget_newpw.getText().toString();
				login();
			}
			break;

		case R.id.forget_time:
			if (forget_phonenum.getText().length() == 0) {
				Futil.showMessage(ForgetPWActivity.this, "手机号不能为空");
			} else if (forget_phonenum.getText().length() != 11) {
				Futil.showMessage(ForgetPWActivity.this, "请输入正确的手机号");
			} else {
				phonenum = forget_phonenum.getText().toString().trim();
				getCode();
			}
			break;
		case R.id.back_top:
			finish();
			break;
		}
	}

	private ImageView back_top;
	private TextView forget_time, forget_top_tv1;
	private EditText forget_phonenum, forget_yanzhengma, forget_newpw,
			forget_password;
	private TextView forget_log_in;
	private RelativeLayout register, usertype;
	private RadioButton type_qiye;
	private RadioButton type_geren;

	private void findview() {
		forget_top_tv1 = (TextView) findViewById(R.id.forget_top_tv1);
		forget_phonenum = (EditText) findViewById(R.id.forget_phonenum);
		forget_yanzhengma = (EditText) findViewById(R.id.forget_yanzhengma);
		forget_newpw = (EditText) findViewById(R.id.forget_newpw);// 忘记密码页
		forget_password = (EditText) findViewById(R.id.forget_password);// 注册页

		forget_time = (TextView) findViewById(R.id.forget_time);
		forget_time.setOnClickListener(this);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

		register = (RelativeLayout) findViewById(R.id.register);
		register.setOnClickListener(this);// 注册页

		usertype = (RelativeLayout) findViewById(R.id.usertype);// 注册页
		type_qiye = (RadioButton) findViewById(R.id.type_qiye);// 注册页 企业
		type_qiye.setOnClickListener(this);
		type_geren = (RadioButton) findViewById(R.id.type_geren);// 注册页 个人
		type_geren.setOnClickListener(this);
		type_geren.setSelected(true);
		individual = "1";

		forget_log_in = (TextView) findViewById(R.id.forget_log_in);
		forget_log_in.setOnClickListener(this);// 忘记密码页
	}

	private void login() {// 忘记密码
		startProgressDialog();
		String strUrl = URLManager.PasswordRecovery;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.mobile", phonenum);
		map.put("password", password);
		map.put("yzCode", yzCode);
		Futil.xutils(strUrl, map, handler, URLManager.three);
	}

	private void registe() {// 注册
		startProgressDialog();
		String strUrl = URLManager.Register;
		HashMap<String, String> map = new HashMap<String, String>();
		// user.mobile+user.password+yzCode
		map.put("user.mobile", phonenum);
		map.put("user.password", password);
		map.put("yzCode", yzCode);
		map.put("user.individual", individual);
		Futil.xutils(strUrl, map, handler, URLManager.two);
	}

	private void getCode() {
		startProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.mobile", phonenum);
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			forget_time.setText("重新获取验证码");
			forget_time.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			forget_time.setClickable(false);
			forget_time.setText(millisUntilFinished / 1000 + "后重新获取");
		}
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
