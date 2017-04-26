package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.BBSBanzhuguanliAdapter;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 名人堂版主管理 */
public class BBSGuanliActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {

					JSONArray adminarray = obj.getJSONArray("adminList");// 管理
					for (int i = 0; i < adminarray.length(); i++) {
						JSONObject adminobj = adminarray.getJSONObject(i);
						User au = new User();
						au.setHeadStatus(adminobj.getString("headStatus"));
						JSONObject adobj = adminobj.getJSONObject("user");
						au.setHead(adobj.getString("head"));
						au.setNickname(adobj.getString("nickname"));
						au.setBlurb(adobj.getString("blurb"));
						au.setId(adobj.getString("id"));
						adminList.add(au);
					}
					bzadapter = new BBSBanzhuguanliAdapter(
							BBSGuanliActivity.this, adminList, userid,
							communityid, handler);
					guanli_viewp.setAdapter(bzadapter);
					bzadapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else if (msg.what == URLManager.two) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String message = obj.getString("message");
					String code = obj.getString("code");
					if (code.equals("2")) {
						Toast.makeText(BBSGuanliActivity.this, "" + message,
								Toast.LENGTH_LONG).show();
						onRestart();
					} else {
						Toast.makeText(BBSGuanliActivity.this, "" + message,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == URLManager.three) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String message = obj.getString("message");
					String code = obj.getString("code");
					if (code.equals("2")) {
						Toast.makeText(BBSGuanliActivity.this, "" + message,
								Toast.LENGTH_LONG).show();
						onRestart();
					} else {
						Toast.makeText(BBSGuanliActivity.this, "" + message,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}
	};
	private String communityid, userid;
	private BBSBanzhuguanliAdapter bzadapter;
	private ArrayList<User> adminList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_banzhu_guanli);
		Intent i = this.getIntent();
		communityid = i.getStringExtra("communityid");
		userid = Futil.getValue(BBSGuanliActivity.this, "userid");
		
		adminList = new ArrayList<User>();
		gointo();
		findViewById();
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
	protected void onRestart() {
		adminList.clear();
		gointo();
		super.onRestart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_top:
			finish();
			break;
		case R.id.banzhu_tisheng:
			getet();
			break;

		default:
			break;
		}
	}

	private ImageView back_top;
	private ListView guanli_viewp;
	private RelativeLayout banzhu_tisheng;
	private EditText banzhu_et;

	private void findViewById() {
		guanli_viewp = (ListView) findViewById(R.id.banzhu_guanli_viewp);
		guanli_viewp.setFocusable(false);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);
		banzhu_et = (EditText) findViewById(R.id.banzhu_et);
		banzhu_tisheng = (RelativeLayout) findViewById(R.id.banzhu_tisheng);
		banzhu_tisheng.setOnClickListener(this);

	}

	private void getet() {
		startProgressDialog();
		int n = banzhu_et.getText().length();
		if (!userid.equals("")) {
			if (n >= 0) {
				String str1 = banzhu_et.getText().toString().trim();
				upadmin(str1);
			} else {
				Toast.makeText(BBSGuanliActivity.this, "请限制社区名称长度",
						Toast.LENGTH_LONG).show();
				stopProgressDialog();
			}
		} else {
			Toast.makeText(BBSGuanliActivity.this, "请先登录", Toast.LENGTH_LONG)
					.show();
			stopProgressDialog();
		}

	}

	private void upadmin(String str1) {
		String strUrl = URLManager.UpAdmin;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		map.put("community.id", communityid);
		map.put("nickname", str1);
		Futil.xutils(strUrl, map, handler, URLManager.two);
	}

	private void gointo() {
		startProgressDialog();
		String strUrl = URLManager.HallOfFame;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("community.id", communityid);
		Futil.xutils(strUrl, map, handler, URLManager.one);

	}

	/**
	 * 等待页
	 */
	private CustomProgressDialog progressDialog;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog
					.createDialog(BBSGuanliActivity.this);
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
