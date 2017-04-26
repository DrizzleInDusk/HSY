package com.zm.hsy.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.adapter.MyPHSHistoryAdapter;
import com.zm.hsy.adapter.MyPHSPraiseAdapter;
import com.zm.hsy.adapter.MyPHSSubAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/** 我的PHS--1.赞过 2.订阅 3.播放历史 */
public class MyPHSActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {// 我赞过
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray myalArray = obj.getJSONArray("myAudioList");
					for (int i = 0; i < myalArray.length(); i++) {
						JSONObject myalobj = myalArray.getJSONObject(i);
						AudioList al = new AudioList();
						al.setCount(myalobj.getString("count"));
						al.setNickname(myalobj.getString("nickname"));
						al.setUid(myalobj.getString("uid"));

						JSONObject audio = myalobj.getJSONObject("audio");

						al.setAddTime(audio.getString("addTime"));
						al.setAudioAlbumId(audio.getString("audioAlbumId"));
						al.setAudioName(audio.getString("audioName"));
						al.setBlurb(audio.getString("blurb"));
						al.setCover(audio.getString("cover"));
						al.setId(audio.getString("id"));
						al.setPath(audio.getString("path"));
						al.setPlayAmount(audio.getString("playAmount"));
						al.setTimeLong(audio.getString("timeLong"));
						praiselist.add(al);
					}
					phs_praise_tv.setText("共赞过" + myalArray.length() + "个声音");
					praiseadapter = new MyPHSPraiseAdapter(context,
							praiselist);
					phs_viewp.setAdapter(praiseadapter);
					praiseadapter.notifyDataSetChanged();

					stopProgressDialog();

					phs_praise_rl.setVisibility(View.VISIBLE);
					phs_history_ll.setVisibility(View.GONE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					phs_praise_tv.setText("共赞过0个声音");
					stopProgressDialog();
					phs_praise_rl.setVisibility(View.GONE);
					phs_history_ll.setVisibility(View.GONE);
				}

			} else if (msg.what == URLManager.two) {// 我的订阅
				JSONObject obj = (JSONObject) msg.obj;
				subscribelist = new ArrayList<Album>();
				try {
					JSONArray myaalArray = obj.getJSONArray("audioAlbumList");
					for (int i = 0; i < myaalArray.length(); i++) {
						JSONObject myalobj = myaalArray.getJSONObject(i);
						Album aal = new Album();
						aal.setAddTime(myalobj.getString("addTime"));
						aal.setAlbumName(myalobj.getString("albumName"));
						aal.setAlbumTypeId(myalobj.getString("albumTypeId"));
						aal.setBlurb(myalobj.getString("blurb"));
						aal.setCover(myalobj.getString("cover"));
						aal.setId(myalobj.getString("id"));
						aal.setUserId(myalobj.getString("userId"));
						subscribelist.add(aal);
					}
					subadapter = new MyPHSSubAdapter(context,
							subscribelist,handler);
					phs_viewp.setAdapter(subadapter);
					subadapter.notifyDataSetChanged();

					stopProgressDialog();
					phs_praise_rl.setVisibility(View.GONE);
					phs_history_ll.setVisibility(View.GONE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					phs_viewp.setVisibility(View.GONE);
					phs_praise_rl.setVisibility(View.GONE);
					phs_history_ll.setVisibility(View.GONE);
				}

			}else if(msg.what == URLManager.three){
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String code=obj.getString("code");
					String message=obj.getString("message");
					if(code.equals("2")){
						getsubscribe();
					}
					Futil.showMessage(context,message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if(msg.what == URLManager.seven){

				int position = (int) msg.obj;
				String alid=subscribelist.get(position).getId();
				delsubscribe(userid,alid);
			}
			stopProgressDialog();
		}

	};
	private String userid, PHS, toptv;
	private MyPHSPraiseAdapter praiseadapter;
	private ArrayList<AudioList> praiselist;
	private MyPHSSubAdapter subadapter;
	private ArrayList<Album> subscribelist;
	private MyPHSHistoryAdapter historyapter;
	private ArrayList<AudioList> historylist;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_praise_history_subscribe);
		context=this;
		praiselist = new ArrayList<AudioList>();
		historylist = new ArrayList<AudioList>();
		praiselist.clear();
		historylist.clear();
		Intent i = this.getIntent();
		PHS = i.getStringExtra("PHS");
		toptv = i.getStringExtra("toptv");
		userid = Futil.getValue(context, "userid");

		findview();
		if (PHS.equals("1")) {
			// 我赞过
			getpraise();
			setpraiselistener();
		} else if (PHS.equals("2")) {
			// 我的订阅
			getsubscribe();
		} else if (PHS.equals("3")) {
			// 播放历史
			gethistory();
		}

	}
	public static boolean mphsonact = false;
	@Override
	protected void onResume() {
		mphsonact = true;
		setcontextaudio(App.getPlaycode() == -1);
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		mphsonact = false;
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

	private ListView phs_viewp;
	private ImageView back_top;
	private TextView phs_top_tv1, phs_praise_tv;
	private RelativeLayout  phs_praise_rl;
	private LinearLayout phs_history_ll;

	private RoundedImageView playbar_touxiang;
	private ImageView playbar_right;
	private static ImageView playbar_play;
	private TextView playbar_name;

	private void findview() {
		phs_viewp = (ListView) findViewById(R.id.phs_viewp);
		phs_viewp.setFocusable(false);
		phs_praise_tv = (TextView) findViewById(R.id.phs_praise_tv);
		phs_praise_rl = (RelativeLayout) findViewById(R.id.phs_praise_rl);
		phs_history_ll = (LinearLayout) findViewById(R.id.phs_history_ll);

		phs_top_tv1 = (TextView) findViewById(R.id.phs_top_tv1);
		phs_top_tv1.setText(toptv);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

		/** 后台播放信息 */
		playbar_touxiang = (RoundedImageView) findViewById(R.id.playbar_touxiang);
		playbar_right = (ImageView) findViewById(R.id.playbar_right);
		playbar_play = (ImageView) findViewById(R.id.playbar_play);
		playbar_name = (TextView) findViewById(R.id.playbar_name);

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
				Picasso.with(context).load(cover).resize(400, 400)
						.placeholder(R.color.touming)
						.error(R.mipmap.ic_launcher)
						.into(playbar_touxiang);
			}
		} else if (isstation == 1) {
			mmsh = App.getPlayerpath();
			stationname = App.getStationname();
			stationid = App.getStationid();
			Picasso.with(context).load(R.mipmap.yyp)
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
					ActivityJumpControl.getInstance((Activity)context)
					.gotoDetailsPlayActivity(id);
				} else if (isstation == 1) {
					ActivityJumpControl.getInstance((Activity)context)
							.gotoRadioStationPlayerActivity(mmsh, stationname,stationid);
				}
			}
		});
	}

	public static Handler muhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 10086) {
				playbar_play.setSelected(false);
			}
		}
	};
	private void getpraise() {
		startProgressDialog();
		String strUrl = URLManager.MyPraise;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}

	String playerpath;
	String playerpath1;

	private void setpraiselistener() {
		phs_viewp.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				RoundedImageView zanguo_cover = (RoundedImageView) v
						.findViewById(R.id.zanguo_cover_iv);
				final String path = praiselist.get(position).getPath();
				final String mblurb = praiselist.get(position).getBlurb();
				final String sname = praiselist.get(position).getAudioName();
				final String mid = praiselist.get(position).getId();
				final String maudioAlbumId = praiselist.get(position)
						.getAudioAlbumId();
				final String mcover = praiselist.get(position).getCover();// 封面图
				/** 记得放开 */
				playerpath = path;
				zanguo_cover.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String apppath = App.getPlayerpath();
						if (!playerpath.equals(apppath)) {// 是否在播放---判断url是否相同

//							MainActivity.player.playUrl(playerpath);
							playerpath1 = playerpath;
							App.setPlayerpath(playerpath1);
//							MainActivity.player.play();
							playcode = 0;
							AudioList al = new AudioList();
							al.setPath(playerpath);
							al.setBlurb(mblurb);
							al.setAudioName(sname);
							al.setCover(mcover);
							al.setId(mid);
							al.setAudioAlbumId(maudioAlbumId);
							App.setContextAudioList(al);
							App.setIsstation(0);
							ArrayList<String> sKey = Futil.loadKeyArray(
									context, "2");
							String key = maudioAlbumId + "_" + mid;
							sKey.remove(key);
							sKey.add(key);
							Futil.saveKeyArray(context, sKey, "2");
							Futil.saveValue(context, key + "id", mid);
							Futil.saveValue(context, key
									+ "audioName", sname);
							Futil.saveValue(context, key + "cover",
									mcover);
							Futil.saveValue(context, key + "path",
									playerpath);
							Futil.saveValue(context, key + "blurb",
									mblurb);
							Futil.saveValue(context, key
									+ "audioAlbumId", maudioAlbumId);
							Date date = new Date(System.currentTimeMillis());
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							App.setIsstation(0);
							final String tim = sdf.format(date);
							Futil.saveValue(context,
									key + "addTime", tim);
						} else {

							int pcode = App.getPlaycode();
							System.out.println("pcode--" + pcode);
							if (pcode == 1) {// Playcode 0=已播放 1已暂停
								setcontextaudio(true);
								System.out.println("播放--" + pcode);
//								MainActivity.player.play();
								playcode = 0;
							} else {
								setcontextaudio(true);
								System.out.println("暂停--" + pcode);
//								MainActivity.player.pause();
								playcode = 1;
							}
							App.setPlaycode(playcode);

						}
					}
				});
			}
		});
	}

	private void getsubscribe() {
		startProgressDialog();
		String strUrl = URLManager.MySubscribe;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		Futil.xutils(strUrl, map, handler, URLManager.two);
	}
	private void delsubscribe(String uid,String alid) {
		startProgressDialog();
		String strUrl = URLManager.DelSubscribe;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userSubscribe.userId", uid);
		map.put("userSubscribe.audioAlbumId", alid);
		Futil.xutils(strUrl, map, handler, URLManager.three);
	}

	private void gethistory() {
		ArrayList<String> sKey = Futil.loadKeyArray(context, "2");
		System.out.println("history----" + sKey.size());
		for (int i = 0; i < sKey.size(); i++) {
			AudioList a = new AudioList();
			String key = sKey.get(i);
			String id = (String) Futil.getValue(context, key + "id");
			String audioName = (String) Futil.getValue(context, key
					+ "audioName");
			String cover = (String) Futil.getValue(context, key
					+ "cover");
			String path = (String) Futil.getValue(context, key
					+ "path");
			String blurb = (String) Futil.getValue(context, key
					+ "blurb");
			String audioAlbumId = (String) Futil.getValue(context,
					key + "audioAlbumId");
			String addTime = (String) Futil.getValue(context, key
					+ "addTime");
			System.out.println("key---" + key);
			a.setKey(key);
			a.setId(id);
			a.setAddTime(addTime);
			a.setAudioName(audioName);
			a.setCover(cover);
			a.setPath(path);
			a.setBlurb(blurb);
			a.setAudioAlbumId(audioAlbumId);
			historylist.add(a);
		}

		historyapter = new MyPHSHistoryAdapter(context, historylist);
		phs_viewp.setAdapter(historyapter);
		historyapter.notifyDataSetChanged();

		historylistener();
		stopProgressDialog();
		phs_praise_rl.setVisibility(View.GONE);
		phs_history_ll.setVisibility(View.VISIBLE);
	}

	private TextView phs_clear_tv, phs_delete_tv;
	private boolean visflag = false;

	private void historylistener() {
		phs_clear_tv = (TextView) findViewById(R.id.phs_clear_tv);
		phs_delete_tv = (TextView) findViewById(R.id.phs_delete_tv);
		phs_clear_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				while (Futil.loadKeyArray(context, "2").size() != 0) {
					ArrayList<String> keyArray = Futil.loadKeyArray(
							context, "2");
					System.out.println("keyArray.size()--" + keyArray.size());

					for (int i = 0; i < keyArray.size(); i++) {
						String key = keyArray.get(i);
						System.out.println("key--" + key + "----" + i);
						Futil.romveValue(context, key, "2");
						keyArray.remove(key);
						System.out.println("keyArray.size()--"
								+ keyArray.size());
						Futil.saveKeyArray(context, keyArray, "2");
					}
				}

				onRestart();
			}
		});
		phs_delete_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (visflag) {
					visflag = false;
				} else {
					visflag = true;
				}
				historyapter.setVisflag(visflag);
			}
		});

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
