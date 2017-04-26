package com.zm.hsy.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.FameHalladminAdapter;
import com.zm.hsy.adapter.FameHallpaihangAdapter;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/** 名人堂 */
@SuppressLint("SimpleDateFormat")
public class BBSFameHallActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONObject webmaster = obj.getJSONObject("webmaster");// 版主
					changeview(webmaster);

				} catch (JSONException e) {
					e.printStackTrace();
				}
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
					adminadapter = new FameHalladminAdapter(
							BBSFameHallActivity.this, adminList);
					guanli_viewp.setAdapter(adminadapter);
					adminadapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					JSONArray signarray = obj.getJSONArray("signList");// 签到
					for (int i = 0; i < signarray.length(); i++) {
						JSONObject signobj = signarray.getJSONObject(i);
						User su = new User();
						su.setFans(signobj.getString("fans"));
						su.setHeadStatus(signobj.getString("headStatus"));
						JSONObject suobj = signobj.getJSONObject("user");
						su.setId(suobj.getString("id"));
						su.setHead(suobj.getString("head"));
						su.setNickname(suobj.getString("nickname"));
						su.setBlurb(suobj.getString("blurb"));
						signList.add(su);
					}
					signadapter = new FameHallpaihangAdapter(
							BBSFameHallActivity.this, signList);
					qiandao_viewp.setAdapter(signadapter);
					signadapter.notifyDataSetChanged();
					if (signList.size() >= 5) {
						qiandao_rl.setVisibility(View.VISIBLE);
					} else {
						qiandao_rl.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					famehall_qiandao_rl.setVisibility(View.GONE);
					qiandao_rl.setVisibility(View.GONE);
				}
				try {
					JSONArray activearray = obj.getJSONArray("activeList");// 活跃
					for (int i = 0; i < activearray.length(); i++) {
						JSONObject activeobj = activearray.getJSONObject(i);
						User acu = new User();
						acu.setFans(activeobj.getString("fans"));
						acu.setHeadStatus(activeobj.getString("headStatus"));
						JSONObject acuobj = activeobj.getJSONObject("user");
						acu.setId(acuobj.getString("id"));
						acu.setHead(acuobj.getString("head"));
						acu.setNickname(acuobj.getString("nickname"));
						acu.setBlurb(acuobj.getString("blurb"));
						activeList.add(acu);
					}
					activeadapter = new FameHallpaihangAdapter(
							BBSFameHallActivity.this, activeList);
					huoyue_viewp.setAdapter(activeadapter);
					activeadapter.notifyDataSetChanged();
					if (activeList.size() >= 5) {
						huoyue_rl.setVisibility(View.VISIBLE);
					} else {
						huoyue_rl.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					famehall_huoyue_rl.setVisibility(View.GONE);
					huoyue_rl.setVisibility(View.GONE);
				}
				try {
					JSONArray scorearray = obj.getJSONArray("scoreList");// 积分
					for (int i = 0; i < scorearray.length(); i++) {
						JSONObject scoreobj = scorearray.getJSONObject(i);
						User scu = new User();
						scu.setFans(scoreobj.getString("fans"));
						scu.setHeadStatus(scoreobj.getString("headStatus"));
						JSONObject scuobj = scoreobj.getJSONObject("user");
						scu.setId(scuobj.getString("id"));
						scu.setHead(scuobj.getString("head"));
						scu.setNickname(scuobj.getString("nickname"));
						scu.setBlurb(scuobj.getString("blurb"));
						scoreList.add(scu);
					}
					scoreadapter = new FameHallpaihangAdapter(
							BBSFameHallActivity.this, scoreList);
					jifen_viewp.setAdapter(scoreadapter);
					scoreadapter.notifyDataSetChanged();
					if (scoreList.size() >= 5) {
						jifen_rl.setVisibility(View.VISIBLE);
					} else {
						jifen_rl.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					famehall_jifen_rl.setVisibility(View.GONE);
					jifen_rl.setVisibility(View.GONE);
				}
			}
			stopProgressDialog();
		}

	};

	private String userid, communityid;
	private ArrayList<User> adminList;
	private FameHalladminAdapter adminadapter;
	private ArrayList<User> signList;
	private FameHallpaihangAdapter signadapter;
	private ArrayList<User> activeList;
	private FameHallpaihangAdapter activeadapter;
	private ArrayList<User> scoreList;
	private FameHallpaihangAdapter scoreadapter;
	private ListView guanli_viewp, qiandao_viewp, huoyue_viewp, jifen_viewp;
	private RelativeLayout famehall_jifen_rl,famehall_huoyue_rl,famehall_qiandao_rl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bbs_famehall);

		famehall_jifen_rl = (RelativeLayout) findViewById(R.id.jifen_rl);
		famehall_huoyue_rl = (RelativeLayout) findViewById(R.id.huoyue_rl);
		famehall_qiandao_rl = (RelativeLayout) findViewById(R.id.qiandao_rl);
		guanli_viewp = (ListView) findViewById(R.id.famehall_guanli_viewp);
		guanli_viewp.setFocusable(false);
		qiandao_viewp = (ListView) findViewById(R.id.famehall_qiandao_viewp);
		qiandao_viewp.setFocusable(false);
		huoyue_viewp = (ListView) findViewById(R.id.famehall_huoyue_viewp);
		huoyue_viewp.setFocusable(false);
		jifen_viewp = (ListView) findViewById(R.id.famehall_jifen_viewp);
		jifen_viewp.setFocusable(false);

		adminList = new ArrayList<User>();
		signList = new ArrayList<User>();
		activeList = new ArrayList<User>();
		scoreList = new ArrayList<User>();

		Intent i = this.getIntent();
		communityid = i.getStringExtra("communityid");
		userid = Futil.getValue(BBSFameHallActivity.this, "userid");

		System.out
				.println("BBSFameHallActivity-community.id----" + communityid);
		findview();
		goin();
	}

	@Override
	protected void onRestart() {
		adminList.clear();
		signList.clear();
		activeList.clear();
		scoreList.clear();
		goin();
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

	private ImageView back_top;
	private RoundedImageView banzhu_head;
	private TextView banzhu_name, banzhu_blurb;
	private RelativeLayout qiandao_rl, huoyue_rl, jifen_rl, banzhu_guanli;

	private void findview() {
		qiandao_rl = (RelativeLayout) findViewById(R.id.famehall_qiandao_rl);
		huoyue_rl = (RelativeLayout) findViewById(R.id.famehall_huoyue_rl);
		jifen_rl = (RelativeLayout) findViewById(R.id.famehall_jifen_rl);

		banzhu_head = (RoundedImageView) findViewById(R.id.famehall_banzhu_head);
		banzhu_name = (TextView) findViewById(R.id.famehall_banzhu_name);
		banzhu_blurb = (TextView) findViewById(R.id.famehall_banzhu_blurb);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);
		banzhu_guanli = (RelativeLayout) findViewById(R.id.famehall_banzhu_guanli);
		banzhu_guanli.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.back_top:
			finish();
			break;
		case R.id.famehall_banzhu_guanli:
			ActivityJumpControl.getInstance(BBSFameHallActivity.this)
					.gotoBBSGuanliActivity(communityid);

			break;
		}
	}

	private void changeview(JSONObject webmaster) {
		try {
			JSONObject user = webmaster.getJSONObject("user");
			String nickname = user.getString("nickname");
			banzhu_name.setText(nickname);
			String blurb = user.getString("blurb");
			if (blurb != null && !blurb.equals("") && !blurb.equals("null")) {
				banzhu_blurb.setText(blurb);
			} else {
				banzhu_blurb.setText("暂无简介");
			}
			final String id = user.getString("id");
			if (id.equals(userid)) {
				banzhu_guanli.setVisibility(View.VISIBLE);
			} else {
				banzhu_guanli.setVisibility(View.GONE);
			}
			String head = user.getString("head");
			String type = webmaster.getString("headStatus");
			if (!type.equals("http")) {
				head = URLManager.Head_URL + head;
			}
			Picasso.with(this).load(head).resize(400, 400)
					.placeholder(R.mipmap.xionghaiz)
					.error(R.mipmap.xionghaiz).into(banzhu_head);
			banzhu_head.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ActivityJumpControl.getInstance(BBSFameHallActivity.this)
							.gotoZhuboActivity(id);

				}
			});

		} catch (JSONException e) {
			banzhu_guanli.setVisibility(View.GONE);
			e.printStackTrace();
		}

	}

	private void goin() {
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
