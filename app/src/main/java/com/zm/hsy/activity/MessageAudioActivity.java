package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.MyABMessageAdapter;
import com.zm.hsy.entity.MyMessage;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 消息中心--1我的音频 2我的社区 */
public class MessageAudioActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray messageCentreList = obj
							.getJSONArray("messageCentreList");
					messlist =new ArrayList<MyMessage>();
					for (int i = 0; i < messageCentreList.length(); i++) {
						JSONObject messageCentre = messageCentreList
								.getJSONObject(i);
						MyMessage me = new MyMessage();
						me.setNickname(messageCentre.getString("nickname"));
						me.setHead(messageCentre.getString("head"));
						me.setHeadStatus(messageCentre.getString("headStatus"));
						me.setId(messageCentre.getString("uid"));
						me.setYycontent(messageCentre.getString("yycontent"));

						JSONObject pushMessage = messageCentre
								.getJSONObject("pushMessage");
						me.setAddTime(pushMessage.getString("addTime"));
						me.setContent(pushMessage.getString("content"));
						me.setPicture(pushMessage.getString("picture"));
						messlist.add(me);
					}
					madapter=new MyABMessageAdapter(context, messlist,Tag);
					abmessage_viewp.setAdapter(madapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}else if (msg.what == URLManager.two) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray messageCentreList = obj
							.getJSONArray("messageCentreList");
					messlist =new ArrayList<MyMessage>();
					for (int i = 0; i < messageCentreList.length(); i++) {
						JSONObject messageCentre = messageCentreList
								.getJSONObject(i);
						MyMessage me = new MyMessage();
						me.setNickname(messageCentre.getString("nickname"));
						me.setHead(messageCentre.getString("head"));
						me.setHeadStatus(messageCentre.getString("headStatus"));
						me.setId(messageCentre.getString("uid"));
						me.setAudioname(messageCentre.getString("audioname"));

						JSONObject pushMessage = messageCentre
								.getJSONObject("pushMessage");
						me.setAddTime(pushMessage.getString("addTime"));
						me.setContent(pushMessage.getString("content"));
						me.setPicture(pushMessage.getString("picture"));
						messlist.add(me);
					}
					madapter=new MyABMessageAdapter(context, messlist,Tag);
					abmessage_viewp.setAdapter(madapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}

	};
	private ArrayList<MyMessage> messlist;
	private MyABMessageAdapter madapter;
	private Context context;
	private String userid, Tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audiomessage);
		context=this;
		Intent i = this.getIntent();
		Tag = i.getStringExtra("Tag");
		userid = Futil.getValue(context, "userid");
		findview();
		changeview();
	}

	private void changeview() {
		if(Tag.equals("1")){
			audiomess_top_tv.setText("音频消息中心");
			getAudioPushMes();
		}else if(Tag.equals("2")){
			audiomess_top_tv.setText("社区消息中心");
			getBBSPushMes();
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back_top:
			finish();
			break;

		}
	}
	private ImageView back_top;
	private TextView audiomess_top_tv;
	private ListView abmessage_viewp;

	private void findview() {
		abmessage_viewp = (ListView) findViewById(R.id.abmessage_viewp);
		abmessage_viewp.setFocusable(false);
		
		audiomess_top_tv = (TextView) findViewById(R.id.audiomess_top_tv);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

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
	private void getBBSPushMes() {
		startProgressDialog();
		String strUrl = URLManager.getPushMessageByUser;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		map.put("pft", "1");
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}
	private void getAudioPushMes() {
		startProgressDialog();
		String strUrl = URLManager.getPushMessageByUser;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		map.put("pft", "2");
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
