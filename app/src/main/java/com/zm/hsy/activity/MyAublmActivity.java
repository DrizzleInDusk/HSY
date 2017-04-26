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
import com.zm.hsy.adapter.ZhubozjAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 我的专辑 1音频2视频 */
@SuppressLint("SimpleDateFormat")
public class MyAublmActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray myAudioAlbum = obj.getJSONArray("audioAlbumList");
					// 专辑
					for (int i = 0; i < myAudioAlbum.length(); i++) {
						Album album = new Album();
						JSONObject z = myAudioAlbum.getJSONObject(i);

						String cover = z.getString("cover");
						String albumName = z.getString("albumName");
						String playAmount = z.getString("playAmount");
						String episode = z.getString("episode");
						String id = z.getString("id");
						String addTime = z.getString("addTime");

						album.setId(id);
						album.setAlbumName(albumName);
						album.setAddTime(addTime);
						album.setPlayAmount(playAmount);
						album.setEpisode(episode);
						album.setCover(cover);
						audioalbumList.add(album);
					}

					zjadapter = new ZhubozjAdapter(MyAublmActivity.this,
							audioalbumList);
					myaublm_viewp.setAdapter(zjadapter);
				} catch (JSONException e) {
					stopProgressDialog();
				}
			} else if (msg.what == URLManager.two) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray myVideoAlbum = obj.getJSONArray("videoAlbumList");
					// 专辑
					for (int i = 0; i < myVideoAlbum.length(); i++) {
						Album album = new Album();
						JSONObject z = myVideoAlbum.getJSONObject(i);

						String cover = z.getString("cover");
						String albumName = z.getString("albumName");
						String playAmount = z.getString("playAmount");
						String episode = z.getString("episode");
						String id = z.getString("id");
						String addTime = z.getString("addTime");

						album.setId(id);
						album.setAlbumName(albumName);
						album.setAddTime(addTime);
						album.setPlayAmount(playAmount);
						album.setEpisode(episode);
						album.setCover(cover);
						videoalbumList.add(album);
					}

					zjadapter = new ZhubozjAdapter(MyAublmActivity.this,
							videoalbumList);
					myaublm_viewp.setAdapter(zjadapter);
				} catch (JSONException e) {
					stopProgressDialog();
				}
			}
			stopProgressDialog();
		}

	};

	private String userid, Tag;
	private ArrayList<Album> audioalbumList;
	private ArrayList<Album> videoalbumList;
	private ZhubozjAdapter zjadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myaublm);
		audioalbumList = new ArrayList<Album>();
		videoalbumList = new ArrayList<Album>();
		audioalbumList.clear();
		videoalbumList.clear();
		
		findview();
		userid = Futil.getValue(MyAublmActivity.this, "userid");

		Intent i = this.getIntent();
		Tag = i.getStringExtra("Tag");
		if (Tag.equals("1")) {
			myaublm_top_tv.setText("音频专辑");
			getAudioAlbum();
		} else if (Tag.equals("2")) {
			myaublm_top_tv.setText("视频专辑");
			getVideoAlbum();
		}
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
	protected void onRestart() {

		super.onRestart();
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
	private ListView myaublm_viewp;
	private TextView myaublm_top_tv;

	private void findview() {
		myaublm_top_tv = (TextView) findViewById(R.id.myaublm_top_tv);

		myaublm_viewp = (ListView) findViewById(R.id.myaublm_viewp);
		myaublm_viewp.setFocusable(false);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private void getAudioAlbum() {
		startProgressDialog();
		String strUrl = URLManager.MyAudioAlbum;
		;
		HashMap<String, String> map = new HashMap<String, String>();
		if (!userid.equals("")) {
			map.put("user.id", userid);
		}
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}

	private void getVideoAlbum() {
		startProgressDialog();
		String strUrl = URLManager.MyVideoAlbum;
		;
		HashMap<String, String> map = new HashMap<String, String>();
		if (!userid.equals("")) {
			map.put("user.id", userid);
		}
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
