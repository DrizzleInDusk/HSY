package com.zm.hsy.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.adapter.CommunityTopicAdapter;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.entity.Topic;
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
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/** 社区页 */
public class BBSCommunityActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray tarray = obj.getJSONArray("TopicTopList");
					zdbbsList = new ArrayList<Topic>();
					for (int i = 0; i < tarray.length(); i++) {
						Topic t = new Topic();
						JSONObject tdata = tarray.getJSONObject(i);
						t.setTitle(tdata.getString("title"));
						t.setContent(tdata.getString("content"));
						t.setId(tdata.getString("id"));
						zdbbsList.add(t);
					}
					changezd(zdbbsList);

				} catch (JSONException e) {
					item_layout1.setVisibility(View.GONE);
					item_layout2.setVisibility(View.GONE);
					item_layout3.setVisibility(View.GONE);
				}
				try {
					JSONArray tparray = obj.getJSONArray("TopicPutongList");
					bbsList = new ArrayList<Topic>();
					for (int i = 0; i < tparray.length(); i++) {
						Topic tp = new Topic();
						JSONObject tpdata = tparray.getJSONObject(i);
						tp.setCount(tpdata.getString("count"));
						tp.setHead(tpdata.getString("head"));
						tp.setHeadStatus(tpdata.getString("headStatus"));
						tp.setNickname(tpdata.getString("nickname"));
						JSONObject ht = tpdata.getJSONObject("communityTopic");
						tp.setContent(ht.getString("content"));
						tp.setAddTime(ht.getString("addTime"));
						tp.setScore(ht.getString("score"));
						String s1 = ht.getString("communityId");
						String s2 = ht.getString("id");
						tp.setCommunityId(ht.getString("communityId"));
						tp.setTitle(ht.getString("title"));
						tp.setPicture(ht.getString("picture"));
						tp.setId(ht.getString("id"));
						bbsList.add(tp);
					}
					comadapter = new CommunityTopicAdapter(
							context, bbsList, handler);
					community_bbs_viewp.setAdapter(comadapter);
					comadapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONObject cobj = obj.getJSONObject("communityRank");
					changeview(cobj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					stopProgressDialog();
				}
			} else if (msg.what == URLManager.two) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String message = obj.getString("message");
					String code = obj.getString("code");
					if (code.equals("2")) {
						Toast.makeText(context, "" + message,
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(context, "" + message,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == URLManager.three) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String message = obj.getString("message");
					String code = obj.getString("code");
					if (code.equals("2")) {
						Toast.makeText(context, "" + message,
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(context, "" + message,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == 100002) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String message = obj.getString("message");
					String code = obj.getString("code");
					if (code.equals("2")) {
						Toast.makeText(context, "" + message,
								Toast.LENGTH_LONG).show();
						onRestart();
					} else {
						Toast.makeText(context, "" + message,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}

	};
	private CommunityTopicAdapter comadapter;
	private ArrayList<Topic> bbsList;
	private ArrayList<Topic> zdbbsList;
	private String communityid;
	private ListView community_bbs_viewp;
	private String userid;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.someonebbs);
		context =this;
		Intent i = this.getIntent();
		communityid = i.getStringExtra("id");
		userid = Futil.getValue(context, "userid");

		community_bbs_viewp = (ListView) findViewById(R.id.community_bbs_viewp);
		community_bbs_viewp.setFocusable(false);
		findview();
	}

	public static boolean bbsconact = false;
	@Override
	protected void onResume() {
		gointo();
		bbsconact=true;
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		bbsconact=false;
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bbsconact=false;
	}


	private ImageView back_top, addbbstopic, person, photos, sign_in, join;
	private TextView top_name, name, blure, memCount, topicCount, item_title1,
			item_title2, item_title3;
	private ImageView bbs_cover;
	private LinearLayout item_layout1, item_layout2, item_layout3;

	private RoundedImageView playbar_touxiang;
	private ImageView playbar_right;
	public static ImageView playbar_play;
	private TextView playbar_name;
	private void findview() {
		/** 后台播放信息 */
		playbar_touxiang = (RoundedImageView) findViewById(R.id.playbar_touxiang);
		playbar_right = (ImageView) findViewById(R.id.playbar_right);
		playbar_play = (ImageView) findViewById(R.id.playbar_play);
		playbar_name = (TextView) findViewById(R.id.playbar_name);

		top_name = (TextView) findViewById(R.id.community_top_name);
		bbs_cover = (ImageView) findViewById(R.id.community_cover);
		name = (TextView) findViewById(R.id.community_name);
		blure = (TextView) findViewById(R.id.community_blure);
		memCount = (TextView) findViewById(R.id.community_memCount);
		topicCount = (TextView) findViewById(R.id.community_topicCount);
		item_title1 = (TextView) findViewById(R.id.bbs_item_title1);
		item_title2 = (TextView) findViewById(R.id.bbs_item_title2);
		item_title3 = (TextView) findViewById(R.id.bbs_item_title3);
		item_layout1 = (LinearLayout) findViewById(R.id.bbs_item_layout1);
		item_layout2 = (LinearLayout) findViewById(R.id.bbs_item_layout2);
		item_layout3 = (LinearLayout) findViewById(R.id.bbs_item_layout3);

		addbbstopic = (ImageView) findViewById(R.id.community_addbbstopic);
		back_top = (ImageView) findViewById(R.id.back_top);
		person = (ImageView) findViewById(R.id.community_person);
		photos = (ImageView) findViewById(R.id.community_photos);
		sign_in = (ImageView) findViewById(R.id.community_sign_in);
		join = (ImageView) findViewById(R.id.community_join);

		back_top.setOnClickListener(this);
		person.setOnClickListener(this);
		photos.setOnClickListener(this);
		sign_in.setOnClickListener(this);
		join.setOnClickListener(this);
		addbbstopic.setOnClickListener(this);

	}

	private void changezd(ArrayList<Topic> zdbbsList) {
		int length = zdbbsList.size();
		try {
			final String cid1 = zdbbsList.get(0).getCommunityId();
			final String tid1 = zdbbsList.get(0).getId();
			item_layout1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ActivityJumpControl.getInstance((Activity) context)
					.gotoBBSCardActivity(tid1);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			final String cid2 = zdbbsList.get(1).getCommunityId();
			final String tid2 = zdbbsList.get(1).getId();
			
			item_layout2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ActivityJumpControl.getInstance((Activity) context)
					.gotoBBSCardActivity(tid2);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			final String cid3 = zdbbsList.get(2).getCommunityId();
			final String tid3 = zdbbsList.get(2).getId();
			item_layout3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ActivityJumpControl.getInstance((Activity) context)
					.gotoBBSCardActivity(tid3);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (length == 1) {
			item_title1.setText(zdbbsList.get(0).getTitle());
			item_layout1.setVisibility(View.VISIBLE);
			item_layout2.setVisibility(View.GONE);
			item_layout3.setVisibility(View.GONE);
		} else if (length == 2) {
			item_title1.setText(zdbbsList.get(0).getTitle());
			item_title2.setText(zdbbsList.get(1).getTitle());
			item_layout1.setVisibility(View.VISIBLE);
			item_layout2.setVisibility(View.VISIBLE);
			item_layout3.setVisibility(View.GONE);
		} else if (length >= 3) {
			item_title1.setText(zdbbsList.get(0).getTitle());
			item_title2.setText(zdbbsList.get(1).getTitle());
			item_title3.setText(zdbbsList.get(2).getTitle());
			item_layout1.setVisibility(View.VISIBLE);
			item_layout2.setVisibility(View.VISIBLE);
			item_layout3.setVisibility(View.VISIBLE);
		} else {
			item_layout1.setVisibility(View.GONE);
			item_layout2.setVisibility(View.GONE);
			item_layout3.setVisibility(View.GONE);
		}
	
		
		
	}

	private void changeview(JSONObject cobj) {
		try {
			String m = cobj.getString("memCount");
			String t = cobj.getString("topicCount");
			memCount.setText("成员  " + m);
			topicCount.setText("帖子  " + t);
			JSONObject ctobj = cobj.getJSONObject("community");
			top_name.setText(ctobj.getString("name"));
			name.setText(ctobj.getString("name"));
			blure.setText(ctobj.getString("blurb"));

			String cover = ctobj.getString("cover");// 封面图
			cover = URLManager.COVER_URL + cover;
			Picasso.with(this).load(cover).resize(400, 400)
					.placeholder(R.mipmap.bbs_img2)
					.error(R.mipmap.bbs_img2).into(bbs_cover);
			
			ctobj.getString("id");

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void gointo() {
		startProgressDialog();
		String strUrl = URLManager.CommunityInfo;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("community.id", communityid);
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}

	/** 后台播放信息 */
	private String id = null;
	private String AlbumId = null;
	private String mmsh = null;
	private String stationid = null;
	private String stationname = null;
	private int isstation;

	private void setcontextaudio(boolean ss) {
		isstation = App.getIsstation();
		AudioList contextaudio = App.getContextAudioList();
		if(isstation == 0){
			if (contextaudio != null) {
				id = contextaudio.getId();
				AlbumId = contextaudio.getAudioAlbumId();
				String name = contextaudio.getAudioName();
				playbar_name.setText(name);
				String cover = contextaudio.getCover();
				cover = URLManager.COVER_URL + cover;
				Picasso.with(context).load(cover).resize(400, 400)
						.error(R.mipmap.playbar_touxiang).into(playbar_touxiang);
			}
		}else if(isstation == 1){
			mmsh = App.getPlayerpath();
			stationname = App.getStationname();
			stationid = App.getStationid();
			Picasso.with(context).load(R.mipmap.yyp)
					.error(R.mipmap.playbar_touxiang).into(playbar_touxiang);
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
					ActivityJumpControl.getInstance((Activity) context)
							.gotoDetailsPlayActivity(id);
				} else if (isstation == 1) {
					ActivityJumpControl.getInstance((Activity) context)
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
		case R.id.community_photos:
			ActivityJumpControl.getInstance((Activity) context)
					.gotoCommunityPhotosActivity(communityid);
			break;
		case R.id.community_sign_in:
			String strUrl = URLManager.Sign;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("user.id", userid);
			map.put("community.id", communityid);
			Futil.xutils(strUrl, map, handler, URLManager.two);

			break;
		case R.id.community_join:
			String strUrl1 = URLManager.Join;
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("user.id", userid);
			map1.put("community.id", communityid);
			Futil.xutils(strUrl1, map1, handler, URLManager.three);

			break;
		case R.id.community_addbbstopic:
			String shield= Futil.getValue(context,"shield");
			if(shield.equals("1")){
				ActivityJumpControl.getInstance((Activity) context)
						.gotoAddBBSTopicActivity(communityid);
			}else{
				Futil.showMessage(context,context.getString(R.string.shield_remind));
			}
			break;
		case R.id.community_person:
			ActivityJumpControl.getInstance((Activity) context)
					.gotoBBSFameHallActivity(communityid);
			break;
		}
	}

}
