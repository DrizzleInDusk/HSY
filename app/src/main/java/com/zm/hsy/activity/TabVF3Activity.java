package com.zm.hsy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.zm.hsy.adapter.Tabvf3Adapter;
import com.zm.hsy.entity.VideoList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
/** 首页视频内容跳转页 */
@SuppressLint("SimpleDateFormat")
public class TabVF3Activity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray tjarray = obj.getJSONArray("tjList");
					for (int i = 0; i < tjarray.length(); i++) {
						JSONObject tjobject = tjarray.getJSONObject(i);
						VideoList valbum = new VideoList();
						String id = tjobject.getString("id");
						String cover = tjobject.getString("cover");
						String videoAlbumId = tjobject
								.getString("videoAlbumId");
						String videoName = tjobject.getString("videoName");
						String titlename = "推荐";
						List<VideoList> vlist = videoMap.get(titlename);
						if (vlist == null) {
							vlist = new ArrayList<VideoList>();
							videoMap.put(titlename, vlist);
						}
						valbum.setId(id);
						valbum.setCover(cover);
						valbum.setVideoAlbumId(videoAlbumId);
						valbum.setVideoName(videoName);
						vlist.add(valbum);
					}
					videoadapter = new Tabvf3Adapter(TabVF3Activity.this,
							videoMap);
					item_viewp.setAdapter(videoadapter);

				} catch (JSONException e) {
				}
				try {
					JSONArray tjarray = obj.getJSONArray("zrList");
					for (int i = 0; i < tjarray.length(); i++) {
						JSONObject tjobject = tjarray.getJSONObject(i);
						VideoList valbum = new VideoList();
						String id = tjobject.getString("id");
						String cover = tjobject.getString("cover");
						String videoAlbumId = tjobject
								.getString("videoAlbumId");
						String videoName = tjobject.getString("videoName");
						String titlename = "最热";
						List<VideoList> vlist = videoMap.get(titlename);
						if (vlist == null) {
							vlist = new ArrayList<VideoList>();
							videoMap.put(titlename, vlist);
						}
						valbum.setId(id);
						valbum.setCover(cover);
						valbum.setVideoAlbumId(videoAlbumId);
						valbum.setVideoName(videoName);
						vlist.add(valbum);
					}
					videoadapter = new Tabvf3Adapter(TabVF3Activity.this,
							videoMap);
					item_viewp.setAdapter(videoadapter);
					
				} catch (JSONException e) {
				}
				try {
					JSONArray tjarray = obj.getJSONArray("zxList");
					for (int i = 0; i < tjarray.length(); i++) {
						JSONObject tjobject = tjarray.getJSONObject(i);
						VideoList valbum = new VideoList();
						String id = tjobject.getString("id");
						String cover = tjobject.getString("cover");
						String videoAlbumId = tjobject
								.getString("videoAlbumId");
						String videoName = tjobject.getString("videoName");
						String titlename = "最新";
						List<VideoList> vlist = videoMap.get(titlename);
						if (vlist == null) {
							vlist = new ArrayList<VideoList>();
							videoMap.put(titlename, vlist);
						}
						valbum.setId(id);
						valbum.setCover(cover);
						valbum.setVideoAlbumId(videoAlbumId);
						valbum.setVideoName(videoName);
						vlist.add(valbum);
					}
					videoadapter = new Tabvf3Adapter(TabVF3Activity.this,
							videoMap);
					item_viewp.setAdapter(videoadapter);
					
				} catch (JSONException e) {
				}
			}
			stopProgressDialog();
		}

	};
	private Map<String, List<VideoList>> videoMap;
	private Tabvf3Adapter videoadapter;
	private String Tag, TopName;
	private String scoreid = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shipin);
		videoMap = new HashMap<String, List<VideoList>>();
		
		Intent i = this.getIntent();
		Tag = i.getStringExtra("Tag");
		TopName = i.getStringExtra("TopName");
		findview();
		getmovie();
		shipin_top_tv2.setText(TopName);
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.back_top:
			finish();
			break;

		}
	}

	private ImageView back_top;
	private TextView shipin_top_tv2;
	private ListView item_viewp;

	private void findview() {
		item_viewp = (ListView) findViewById(R.id.shipin_item_viewp);
		item_viewp.setFocusable(false);

		shipin_top_tv2 = (TextView) findViewById(R.id.shipin_top_tv2);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private void getmovie() {
		startProgressDialog();
		String strUrl = URLManager.GetVideoIndex;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("albumTypeId", Tag);
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
