package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.XinxianshiAdapter;
import com.zm.hsy.entity.Fresh;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 订阅_新鲜事 */
public class XinxianshiActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					freshArrayList=new ArrayList<>();
					JSONArray freshThingsList=obj.getJSONArray("freshThingsList");
					for(int i=0;i<freshThingsList.length();i++){
						JSONObject freshThings =freshThingsList.getJSONObject(i);
						Fresh fresh =new Fresh();
						fresh.setAlbumCover(freshThings.getString("albumCover"));
						fresh.setAlbumId(freshThings.getString("albumId" ));
						fresh.setAlbumName(freshThings.getString("albumName" ));
						fresh.setAlbumPlay(freshThings.getString("albumPlay" ));
						fresh.setAudioCount(freshThings.getString("audioCount" ));
						fresh.setAudioCover(freshThings.getString("audioCover" ));
						fresh.setAudioId(freshThings.getString("audioId" ));
						fresh.setAudioName(freshThings.getString("audioName" ));
						fresh.setBelong(freshThings.getString("belong" ));
						fresh.setByName( freshThings.getString("byName" ));
						fresh.setFans( freshThings.getString("fans" ));
						fresh.setGhead( freshThings.getString("ghead" ));
						fresh.setGheadStatus( freshThings.getString("gheadStatus" ));
						fresh.setGname( freshThings.getString("gname" ));
						fresh.setConcemStatus( freshThings.getString("concemStatus" ));
						fresh.setGid( freshThings.getString("gid" ));
						fresh.setSubscribeStatus( freshThings.getString("subscribeStatus" ));
						fresh.setTime( freshThings.getString("time" ));
						fresh.setWhead( freshThings.getString("whead" ));
						fresh.setWheadStatus( freshThings.getString("wheadStatus"  ));
						fresh.setWname(freshThings.getString("wname" ));
						freshArrayList.add(fresh);
					}
					adapter=new XinxianshiAdapter(context,freshArrayList);
					xinxianshi_view.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			stopProgressDialog();
		}

	};

	private ArrayList<Fresh> freshArrayList;
	private XinxianshiAdapter adapter;
	private String userid;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xinxianshi);
		context=this;
		userid = Futil.getValue(context, "userid");
		findview();
	}

	@Override
	protected void onResume() {
		getFreshThings();
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
	private ImageView back_top;
	private ListView xinxianshi_view;

	private void findview() {

		xinxianshi_view = (ListView) findViewById(R.id.xinxianshi_view);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private void getFreshThings() {
		startProgressDialog();
		String strUrl = URLManager.GetFreshThings;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
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
