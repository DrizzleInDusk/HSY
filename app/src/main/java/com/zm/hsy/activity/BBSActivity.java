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
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.BBSCommunityAdapter;
import com.zm.hsy.adapter.BBSTopicAdapter;
import com.zm.hsy.entity.Community;
import com.zm.hsy.entity.Topic;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/*** 社区首页 */
public class BBSActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {// 社区
					JSONArray hotC = obj.getJSONArray("hotCommunityList");
					bbsList = new ArrayList<Community>();
					for (int i = 0; i < hotC.length(); i++) {
						Community c = new Community();
						JSONObject hcobj = hotC.getJSONObject(i);
						c.setMemCount(hcobj.getString("memCount"));
						c.setTopicCount(hcobj.getString("topicCount"));

						JSONObject hc = hcobj.getJSONObject("community");

						c.setBlurb(hc.getString("blurb"));
						c.setCover(hc.getString("cover"));
						c.setName(hc.getString("name"));
						c.setId(hc.getString("id"));
						bbsList.add(c);
					}
					cadapter = new BBSCommunityAdapter(context,
							bbsList);
					comlistView.setAdapter(cadapter);
					cadapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {// 热帖
					JSONArray hotT = obj.getJSONArray("hotTopicList");
					tbbsList = new ArrayList<Topic>();
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
					topiclistView.setAdapter(tadapter);
					tadapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {// 我的社区
					JSONArray myC = obj.getJSONArray("myCommunityList");
					mbbsList = new ArrayList<Community>();
					for (int k = 0; k < myC.length(); k++) {
						Community mc = new Community();
						JSONObject mcobj = myC.getJSONObject(k);
						mc.setMemCount(mcobj.getString("memCount"));
						mc.setTopicCount(mcobj.getString("topicCount"));
						JSONObject myc = mcobj.getJSONObject("community");
						mc.setBlurb(myc.getString("blurb"));
						mc.setCover(myc.getString("cover"));
						mc.setName(myc.getString("name"));
						mc.setId(myc.getString("id"));
						mbbsList.add(mc);
					}
					mcadapter = new BBSCommunityAdapter(context,
							mbbsList);
					mybbslistView.setAdapter(mcadapter);
					mcadapter.notifyDataSetChanged();
					bbs_mybbs.setVisibility(View.VISIBLE);
					stopProgressDialog();
				} catch (JSONException e) {
					bbs_mybbs.setVisibility(View.GONE);
				}
			}
			stopProgressDialog();
		}

	};

	private ArrayList<Community> bbsList;
	private ArrayList<Community> mbbsList;
	private ArrayList<Topic> tbbsList;
	private BBSCommunityAdapter cadapter;
	private BBSCommunityAdapter mcadapter;
	private BBSTopicAdapter tadapter;
	private ImageView back_top, bbs_build;
	private RelativeLayout bbs_mybbs,bbs_hotbbs,bbs_hottopic;
	private String userid;
	private Context context;
	private ListView mybbslistView, comlistView, topiclistView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bbs);
		context=this;
		Intent i = this.getIntent();
		userid = i.getStringExtra("id");
		userid = Futil.getValue(context, "userid");


		comlistView = (ListView) findViewById(R.id.bbs_bbs_viewp);
		topiclistView = (ListView) findViewById(R.id.bbs_bbs2_viewp);
		mybbslistView = (ListView) findViewById(R.id.bbs_mybbs_viewp);

		bbs_mybbs = (RelativeLayout) findViewById(R.id.bbs_mybbs);
		bbs_mybbs.setOnClickListener(this);
		bbs_hotbbs = (RelativeLayout) findViewById(R.id.bbs_hotbbs);
		bbs_hotbbs.setOnClickListener(this);
		bbs_hottopic = (RelativeLayout) findViewById(R.id.bbs_hottopic);
		bbs_hottopic.setOnClickListener(this);
		bbs_build = (ImageView) findViewById(R.id.bbs_build);
		bbs_build.setOnClickListener(this);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);
	}
	@Override
	protected void onResume() {

		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
		gointo();
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
		super.onRestart();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bbs_build:
			String shield= Futil.getValue(context,"shield");
			if(shield.equals("1")){
				ActivityJumpControl.getInstance((Activity) context)
						.gotoBuildBBSActivity();
			}else{
				Futil.showMessage(context,context.getString(R.string.shield_remind));
			}
			finish();
			break;
		case R.id.bbs_mybbs:
			ActivityJumpControl.getInstance((Activity) context)
					.gotoMoreMyCommunityActivity();
			break;
		case R.id.bbs_hotbbs:
			ActivityJumpControl.getInstance((Activity) context)
					.gotoMoreHotCommunityActivity();
			break;
		case R.id.bbs_hottopic:
			ActivityJumpControl.getInstance((Activity) context)
					.gotoMoreHotTopicActivity();
			break;
		case R.id.back_top:
			finish();
			break;
		}
	}

	
	private void gointo() {
		startProgressDialog();
		String strUrl = URLManager.CommunityIndex;
		HashMap<String, String> map = new HashMap<String, String>();
		if (!userid.equals("")) {
			map.put("user.id", userid);
		}
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
