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
import com.zm.hsy.adapter.BBSCommunityAdapter;
import com.zm.hsy.entity.Community;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/*** 更多热门社区 */
public class MoreHotCommunityActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				bbsList =new ArrayList<Community>();
				try {// 社区
					JSONArray hotC = obj.getJSONArray("hotCommunityList");
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
					morehc_viewp.setAdapter(cadapter);
					cadapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
			stopProgressDialog();
		}

	};

	private String userid;
	private Context context;
	private ArrayList<Community> bbsList;
	private BBSCommunityAdapter cadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morehotcommunity);
		context=this;
		userid = Futil.getValue(context, "userid");
		findview();
	}

	@Override
	protected void onResume() {
        getMoreHotComm();
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
	private ListView morehc_viewp;

	private void findview() {
		morehc_viewp = (ListView) findViewById(R.id.morehc_viewp);
		morehc_viewp.setFocusable(false);
		findViewById(R.id.back_top).setOnClickListener(this);

	}

	private void getMoreHotComm() {
		startProgressDialog();
		String strUrl =  URLManager.GetMoreHotCommunity;
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
