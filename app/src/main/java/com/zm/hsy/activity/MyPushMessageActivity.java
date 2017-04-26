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
import com.zm.hsy.adapter.MypushMessageAdapter;
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
/** 消息中心-查看某个消息 */
public class MyPushMessageActivity extends Activity implements OnClickListener {

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
						me.setNum(messageCentre.getString("num"));
						me.setId(messageCentre.getString("uid"));

						JSONObject pushMessage = messageCentre
								.getJSONObject("pushMessage");
						me.setAddTime(pushMessage.getString("addTime"));
						me.setContent(pushMessage.getString("content"));
						me.setPicture(pushMessage.getString("picture"));
						messlist.add(me);
					}
					madapter=new MypushMessageAdapter(context, messlist);
					pushmess_viewp.setAdapter(madapter);
					scrollMyListViewToBottom();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}

	};

	private String userid, fuid, pft,nickname;
	private ArrayList<MyMessage> messlist;
	private MypushMessageAdapter madapter;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypushmess);
		context=this;
		Intent i = this.getIntent();
		fuid = i.getStringExtra("fuid");
		pft = i.getStringExtra("pft");
		nickname = i.getStringExtra("nickname");
		userid = Futil.getValue(context, "userid");
		findview();
	}

	

	private ImageView back_top;
	private TextView mypushmess_top_tv;
	private ListView pushmess_viewp;

	private void findview() {
		mypushmess_top_tv = (TextView) findViewById(R.id.mypushmess_top_tv);
		mypushmess_top_tv.setText(nickname);
		
		pushmess_viewp = (ListView) findViewById(R.id.pushmess_viewp);
		
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}
	private void scrollMyListViewToBottom() {
		pushmess_viewp.post(new Runnable() {
	        @Override
	        public void run() {
	            // Select the last row so it will scroll into view...
	        	pushmess_viewp.setSelection(madapter.getCount() - 1);
	        }
	    });
	}
	private void getupPush() {
		startProgressDialog();
		String strUrl = URLManager.upPushMessageByRead;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fuid", fuid);
		map.put("tuid", userid);
		map.put("pft", pft);
		System.out.println("fuid>>>>"+fuid);
		System.out.println("tuid>>>>"+userid);
		System.out.println("pft>>>>"+pft);
		Futil.xutils(strUrl, map, handler, URLManager.one);

	}
	protected void onRestart() {

		super.onRestart();
	}

	@Override
	protected void onResume() {
		getupPush();
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

		case R.id.back_top:
			finish();
			break;

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
