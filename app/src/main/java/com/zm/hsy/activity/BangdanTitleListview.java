package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.adapter.BdAudioListAdapter;
import com.zm.hsy.adapter.BdUserListAdapter;
import com.zm.hsy.adapter.BdVideoListAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 榜单详情 标题栏+listview页 tv 标签名字 Tag audio user video */
public class BangdanTitleListview extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray data = obj.getJSONArray("audioAlbumList");
					for (int i = 0; i < data.length(); i++) {
						JSONObject aduio = data.getJSONObject(i);
						Album alist = new Album();
						alist.setAlbumName(aduio.getString("albumName"));
						alist.setBlurb(aduio.getString("blurb"));
						alist.setId(aduio.getString("id"));
						alist.setEpisode(aduio.getString("episode"));
						alist.setCover(aduio.getString("cover"));
						audioAlbumList.add(alist);
					}
					audioadapter = new BdAudioListAdapter(BangdanTitleListview.this,
							audioAlbumList);

					mclistView.setAdapter(audioadapter);

					audioadapter.notifyDataSetChanged();
					stopProgressDialog();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(msg.what == URLManager.two){
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray data = obj.getJSONArray("userRankList");
					for (int i = 0; i < data.length(); i++) {
						JSONObject udata = data.getJSONObject(i).getJSONObject("user");
						String fans = data.getJSONObject(i).getString("fans");
						String type = data.getJSONObject(i).getString("headStatus");
						fanslist.add(fans);
						User ulist = new User();
						ulist.setHeadStatus(type);
						ulist.setHead(udata.getString("head"));
						ulist.setHead(udata.getString("head"));
						ulist.setBlurb(udata.getString("blurb"));
						ulist.setId(udata.getString("id"));
						ulist.setNickname(udata.getString("nickname"));
						userList.add(ulist);
					}
					useradapter = new BdUserListAdapter(BangdanTitleListview.this,
							userList,fanslist);

					mclistView.setAdapter(useradapter);

					useradapter.notifyDataSetChanged();
					stopProgressDialog();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(msg.what == URLManager.three){
				JSONObject video = (JSONObject) msg.obj;
				try {
					JSONArray videoarray = video.getJSONArray("videoList");
					for (int i = 0; i < videoarray.length(); i++) {
						JSONObject vdata = videoarray.getJSONObject(i);
						Album alist = new Album();
						alist.setAlbumName(vdata.getString("videoName"));
						alist.setBlurb(vdata.getString("blurb"));
						alist.setId(vdata.getString("id"));
						alist.setEpisode(vdata.getString("playAmount"));
						alist.setCover(vdata.getString("cover"));
						audioAlbumList.add(alist);
					}
					videoadapter = new BdVideoListAdapter(BangdanTitleListview.this,
							audioAlbumList);
					mclistView.setAdapter(videoadapter);
					videoadapter.notifyDataSetChanged();
					stopProgressDialog();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}
	};
	private ListView mclistView;
	private ImageView back_top;
	private TextView bangdan_text_top;
	private BdAudioListAdapter audioadapter;
	private BdUserListAdapter useradapter;
	private BdVideoListAdapter videoadapter;
	private ArrayList<Album> audioAlbumList ;
	private ArrayList<User> userList ;
	private ArrayList<String> fanslist ;
	private String rankType, toptv,Tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bangdan_list);
		audioAlbumList = new ArrayList<Album>();
		userList = new ArrayList<User>();
		fanslist = new ArrayList<String>();
		findview();

	}

	private RoundedImageView playbar_touxiang;
	private ImageView playbar_right;
	public static ImageView playbar_play;
	private TextView playbar_name;
	public static boolean bdonact = false;
	private void findview() {

		/** 后台播放信息 */
		playbar_touxiang = (RoundedImageView) findViewById(R.id.playbar_touxiang);
		playbar_right = (ImageView) findViewById(R.id.playbar_right);
		playbar_play = (ImageView) findViewById(R.id.playbar_play);
		playbar_name = (TextView) findViewById(R.id.playbar_name);
		
		
		mclistView = (ListView) findViewById(R.id.bangdan_content_view);
		bangdan_text_top = (TextView) findViewById(R.id.bangdan_text_top);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

		mclistView.setFocusable(false);

		Intent i = this.getIntent();
		toptv = i.getStringExtra("toptv");
		bangdan_text_top.setText(toptv);
		rankType = i.getStringExtra("rankType");
		Tag = i.getStringExtra("Tag");
		if(Tag.equals("audio")){
			gointoaudio();
		}
		if(Tag.equals("user")){
			gointouser();
		}
		if(Tag.equals("video")){
			gointovideo();
		}
	}
	/** 后台播放信息 */
	private String id = null;
	private String AlbumId = null;
	private String mmsh = null;
	private String stationname = null;
	private String stationid = null;
	private int isstation;

	private void setcontextaudio(boolean ss) {
		isstation = App.getIsstation();
		AudioList contextaudio = App.getContextAudioList();
		if (isstation == 0) {
			if (contextaudio != null) {
				id = contextaudio.getId();
				AlbumId = contextaudio.getAudioAlbumId();
				String name = contextaudio.getAudioName();
				playbar_name.setText(name);
				String cover = contextaudio.getCover();
				cover = URLManager.COVER_URL + cover;
				Picasso.with(BangdanTitleListview.this).load(cover).resize(400, 400)
						.placeholder(R.color.touming)
						.error(R.mipmap.ic_launcher)
						.into(playbar_touxiang);
			}
		} else if (isstation == 1) {
			mmsh = App.getPlayerpath();
			stationname = App.getStationname();
			stationid = App.getStationid();
			Picasso.with(BangdanTitleListview.this).load(R.mipmap.yyp)
					.resize(400, 400).placeholder(R.color.touming)
					.error(R.mipmap.ic_launcher).into(playbar_touxiang);
		}
		int pcode = App.getPlaycode();
		System.out.println("pcode------" + pcode);
		if (pcode == 1) {
			playbar_play.setSelected(ss);
		} else if (pcode == 0) {
			playbar_play.setSelected(!ss);
		} else {
			playbar_play.setSelected(!ss);
		}
		setonlistener();
	}

	private int playcode;

	private void setonlistener() {

		playbar_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (playbar_play.isSelected()) {
					MainActivity.mVV.pause();
					playcode = 1;
					playbar_play.setSelected(false);
				} else {
// 发起一次播放任务,当然您不一定要在这发起
					if (!MainActivity.mVV.isPlaying() && (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
						MainActivity.mVV.resume();
					} else {
						MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);//UI 事件  播放
					}
					playcode = 0;
					playbar_play.setSelected(true);
				}
				App.setPlaycode(playcode);
			}
		});
		playbar_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isstation == 0) {
					ActivityJumpControl.getInstance(BangdanTitleListview.this)
							.gotoDetailsPlayActivity(id);
				} else if (isstation == 1) {
					ActivityJumpControl.getInstance(BangdanTitleListview.this)
							.gotoRadioStationPlayerActivity(mmsh, stationname,stationid);
				}
			}
		});
	}
	@Override
	protected void onResume() {
		bdonact=true;
		setcontextaudio(App.getPlaycode() == -1);
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		bdonact=false;
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bdonact=false;
	}

	private void gointoaudio() {
		startProgressDialog();

		String strUrl = URLManager.ManyRankList;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("rankType", rankType);
		Futil.xutils(strUrl, map, handler, URLManager.one);

	}
	private void gointouser() {
		startProgressDialog();

		String strUrl = URLManager.ManyRankList;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("rankType", rankType);
		Futil.xutils(strUrl, map, handler, URLManager.two);

	}
	private void gointovideo() {
		startProgressDialog();

		String strUrl = URLManager.ManyRankList;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("rankType", rankType);
		Futil.xutils(strUrl, map, handler, URLManager.three);

	}
	public static Handler muhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 10086) {
				playbar_play.setSelected(false);
			}
		}
	};
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.back_top:
			finish();
			break;
		}
	}

}
