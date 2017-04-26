package com.zm.hsy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.MyMessageAdapter;
import com.zm.hsy.entity.MyMessage;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
/** 消息中心 */
public class MessageActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				messlist = new ArrayList<MyMessage>();
				mMap = new HashMap<String, MyMessage>();
				try {
					JSONArray messageCentreList = obj
							.getJSONArray("messageCentreList");
					if (messageCentreList.length() != 0) {
						for (int i = 0; i < messageCentreList.length(); i++) {
							JSONObject messageCentre = messageCentreList
									.getJSONObject(i);
							JSONObject pushMessage = messageCentre
									.getJSONObject("pushMessage");
							String uid = messageCentre.getString("uid");
							String nickname = messageCentre
									.getString("nickname");
							String head = messageCentre.getString("head");
							String headStatus = messageCentre
									.getString("headStatus");
							String num = messageCentre.getString("num");

							String status = pushMessage.getString("status");
							String addTime = pushMessage.getString("addTime");
							String content = pushMessage.getString("content");
							String picture = pushMessage.getString("picture");

							MyMessage mess = mMap.get(uid);
							if (mess == null) {
								MyMessage me = new MyMessage();
								me.setNickname(nickname);
								me.setHead(head);
								me.setHeadStatus(headStatus);
								me.setNum(num);
								me.setId(uid);

								me.setStatus(status);
								me.setAddTime(addTime);
								me.setContent(content);
								me.setPicture(picture);
								mMap.put(uid, me);
							} else {
								String s = mess.getStatus();
								if (!s.equals("1")) {
									mess.setNickname(nickname);
									mess.setHead(head);
									mess.setHeadStatus(headStatus);
									mess.setNum(num);
									mess.setId(uid);

									mess.setStatus(status);
									mess.setAddTime(addTime);
									mess.setContent(content);
									mess.setPicture(picture);
								}
							}
						}
						if (mMap.get("0") == null) {
							MyMessage me = new MyMessage();
							me.setNickname("官方");
							me.setHead("");
							me.setHeadStatus("");
							me.setNum("0");
							me.setId("0");
							me.setAddTime("");
							mMap.put("0", me);
						}
						for (String key : mMap.keySet()) {
							MyMessage myMessage = mMap.get(key);
							String id = myMessage.getId();
							if (id.equals("0")) {
								messlist.add(0, myMessage);
							} else {
								messlist.add(myMessage);
							}
						}
					} else {
						MyMessage me = new MyMessage();
						me.setNickname("官方");
						me.setHead("");
						me.setHeadStatus("");
						me.setNum("0");
						me.setId("0");
						me.setAddTime("");
						messlist.add(me);
					}

					madapter = new MyMessageAdapter(context, messlist, handler);
					message_viewp.setAdapter(madapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == URLManager.two) {
				String fuid = (String) msg.obj;
				String pft;
				if (fuid.equals("0")) {
					pft = "4";
				} else {
					pft = "3";
				}
				DelPushMes(fuid, pft);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						getPushMes();
					}
				}, 500);

			}
			stopProgressDialog();
		}

	};
	private Map<String, MyMessage> mMap;
	private MyMessageAdapter madapter;
	private ArrayList<MyMessage> messlist;

	private String userid;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		context = this;
		userid = Futil.getValue(context, "userid");
		findview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yinpin:
			ActivityJumpControl.getInstance(this).gotoMessageAudioActivity("1");
			break;
		case R.id.shequ:
			ActivityJumpControl.getInstance(this).gotoMessageAudioActivity("2");
			break;
		case R.id.back_top:
			finish();
			break;

		}
	}

	private ListView message_viewp;
	private RelativeLayout yinpin,shequ;

	private void findview() {

		yinpin = (RelativeLayout) findViewById(R.id.yinpin);
		yinpin.setOnClickListener(this);
		
		shequ = (RelativeLayout) findViewById(R.id.shequ);
		shequ.setOnClickListener(this);
		
		message_viewp = (ListView) findViewById(R.id.message_viewp);
		message_viewp.setFocusable(false);

		findViewById(R.id.back_top).setOnClickListener(this);

	}

	protected void onRestart() {

		super.onRestart();
	}

	@Override
	protected void onResume() {
		getPushMes();
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

	private void getPushMes() {
		startProgressDialog();
		String strUrl = URLManager.getPushMessageByUser;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		map.put("pft", "3");
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}

	private void DelPushMes(String fuid, String pft) {
		String strUrl = URLManager.upPushMessageByDel;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fuid", fuid);
		map.put("tuid", userid);
		map.put("pft", pft);
		Futil.xutils(strUrl, map, handler, URLManager.six);
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
