package com.zm.hsy.activity;

import android.annotation.SuppressLint;
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
import com.zm.hsy.adapter.SearchF2Adapter;
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
/**首页更多*/
@SuppressLint("SimpleDateFormat")
public class MoreAlbumActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				albumList=new ArrayList<Album>();
				try {
					JSONArray moreList= obj.getJSONArray("moreRecommendList");
					for(int i=0;i<moreList.length();i++){
						JSONObject audioAlbum=moreList.getJSONObject(i).getJSONObject("audioAlbum");
						Album myAlbum = new Album();
						myAlbum.setId(audioAlbum.getString("id"));
						myAlbum.setAlbumName(audioAlbum.getString("albumName"));
						myAlbum.setBlurb(audioAlbum.getString("blurb"));
						myAlbum.setCover(audioAlbum.getString("cover"));
						myAlbum.setPlayAmount(audioAlbum
								.getString("playAmount"));
						myAlbum.setEpisode(audioAlbum.getString("episode"));
						albumList.add(myAlbum);
					}
					adapter = new SearchF2Adapter(context, albumList);
					aublm_viewp.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}

	};

	private String userid, rid,itemname;
	private Context context;
	private SearchF2Adapter adapter;
	private ArrayList<Album> albumList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morealbum);
		context=this;
		Intent i = this.getIntent();
		rid = i.getStringExtra("rid");
		itemname = i.getStringExtra("itemname");
		userid = Futil.getValue(context, "userid");
		findview();
	}

	@Override
	protected void onResume() {
		getindexMore();
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
	private TextView top_tv;
	private ListView aublm_viewp;
	private void findview() {

		aublm_viewp = (ListView) findViewById(R.id.search_aublm_viewp);
		aublm_viewp.setFocusable(false);
		top_tv = (TextView) findViewById(R.id.morealbum_top_tv);
		top_tv.setText(itemname);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private void getindexMore() {
		startProgressDialog();
		String strUrl =  URLManager.IndexMore;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("rid",rid);
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
