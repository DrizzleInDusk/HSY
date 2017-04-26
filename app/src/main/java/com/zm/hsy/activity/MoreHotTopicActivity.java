package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.BBSTopicAdapter;
import com.zm.hsy.entity.Topic;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/*** 更多热门帖子 */
public class MoreHotTopicActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				tbbsList=new  ArrayList<Topic>();
				try {// 热帖
					JSONArray hotT = obj.getJSONArray("hotTopicList");
					for (int j = 0; j < hotT.length(); j++) {
						JSONObject htobj = hotT.getJSONObject(j);
						Topic t = new Topic();
						t.setCount(htobj.getString("count"));
						t.setHead(htobj.getString("head"));
						t.setNickname(htobj.getString("nickname"));
						t.setHeadStatus(htobj.getString("headStatus"));
						JSONObject ht = htobj.getJSONObject("communityTopic");
						t.setContent(ht.getString("content"));
						t.setAddTime(ht.getString("addTime"));
						t.setScore(ht.getString("score"));
						t.setCommunityId(ht.getString("communityId"));
						t.setTitle(ht.getString("title"));
						t.setPicture(ht.getString("picture"));
						t.setId(ht.getString("id"));
						tbbsList.add(t);
					}
					tadapter = new BBSTopicAdapter(context, tbbsList);
					moretc_viewp.setAdapter(tadapter);
					tadapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
			stopProgressDialog();
		}

	};

	private String userid;
	private Context context;
	private ArrayList<Topic> tbbsList;
	private BBSTopicAdapter tadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morehottopic);
		context=this;
		userid = Futil.getValue(context, "userid");
		findview();
	}

	@Override
	protected void onResume() {
        getMoreHotTc();
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
		switch (v.getId()) {

		case R.id.back_top:
			finish();
			break;

		}
	}
	private ListView moretc_viewp;

	private void findview() {
		moretc_viewp = (ListView) findViewById(R.id.moretc_viewp);
		findViewById(R.id.back_top).setOnClickListener(this);

	}

	private void getMoreHotTc() {
		startProgressDialog();
		String strUrl =  URLManager.GetMoreHotCommunityTopic;
		HashMap<String, String> map = new HashMap<String, String>();
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
