package com.zm.hsy.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zm.hsy.R;
import com.zm.hsy.adapter.MyPHSHistoryAdapter;
import com.zm.hsy.adapter.MyPHSSubAdapter;
import com.zm.hsy.adapter.MySubRAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

public class HomeFragment1 extends Fragment implements OnClickListener {
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			stopProgressDialog();
			if (msg.what == URLManager.one) {
                subscribelist = new ArrayList<Album>();
				JSONObject obj = (JSONObject) msg.obj;
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
					tabvf2_dy_viewp.setVisibility(View.VISIBLE);
					subadapter = new MyPHSSubAdapter(getActivity(),
							subscribelist,handler);
					tabvf2_dy_viewp.setAdapter(subadapter);
					subadapter.notifyDataSetChanged();

					stopProgressDialog();
				} catch (JSONException e) {
					e.printStackTrace();
					tabvf2_dy_viewp.setVisibility(View.GONE);
					System.out.println("tabvf2_dy_viewp--GONE");
					stopProgressDialog();
				}
			}else if(msg.what == URLManager.two){
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
				albumid=subscribelist.get(position).getId();
				delsubscribe(userid,albumid);
			}else  if(msg.what == URLManager.three){
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray srArray = obj.getJSONArray("srList");
					subscribelist = new ArrayList<Album>();
					for (int i = 0; i < srArray.length(); i++) {
						Album album = new Album();
						JSONObject data = srArray.getJSONObject(i);

						String cover = data.getString("cover");
						String albumName = data.getString("albumName");
						String blurb = data.getString("blurb");
						String playAmount = data.getString("playAmount");
						String episode = data.getString("episode");
						String id = data.getString("id");

						album.setId(id);
						album.setAlbumName(albumName);
						album.setBlurb(blurb);
						album.setPlayAmount(playAmount);
						album.setEpisode(episode);
						album.setCover(cover);
						subscribelist.add(album);
					}
					tabvf2_dy_viewp.setVisibility(View.VISIBLE);
					subradapter = new MySubRAdapter(getActivity(),
							subscribelist);
					tabvf2_dy_viewp.setAdapter(subradapter);
					subradapter.notifyDataSetChanged();

					stopProgressDialog();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	};
	private String albumid,userid;
	private MyPHSSubAdapter subadapter;
	private MySubRAdapter subradapter;
	private ArrayList<Album> subscribelist;
	private Context context;
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tab_frag2, container, false);
		context=getActivity();

		userid = Futil.getValue(getActivity(), "userid");
		initView(view);
		getuser();
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frag2_top_rl1:
			top_tv1.setVisibility(View.VISIBLE);
			top_tv2.setVisibility(View.GONE);
			tabvf2_ll.setVisibility(View.GONE);
			top_tv3.setVisibility(View.GONE);
			history_ll.setVisibility(View.GONE);
			tabvf2_dy_viewp.setVisibility(View.GONE);
			getRecommend();
			break;
		case R.id.frag2_top_rl2:
			top_tv1.setVisibility(View.GONE);
			top_tv2.setVisibility(View.VISIBLE);
			tabvf2_ll.setVisibility(View.VISIBLE);
			top_tv3.setVisibility(View.GONE);
			history_ll.setVisibility(View.GONE);
			tabvf2_dy_viewp.setVisibility(View.GONE);
			getsubscribe();
			break;
		case R.id.frag2_top_rl3:
			top_tv1.setVisibility(View.GONE);
			top_tv2.setVisibility(View.GONE);
			tabvf2_ll.setVisibility(View.GONE);
			top_tv3.setVisibility(View.VISIBLE);
			history_ll.setVisibility(View.VISIBLE);
			tabvf2_dy_viewp.setVisibility(View.GONE);
			// 播放历史
			gethistory();
			break;
		case R.id.tabvf2_ll:
			ActivityJumpControl.getInstance(getActivity()).gotoXinxianshiActivity();
			break;
		case R.id.inform:
			ActivityJumpControl.getInstance(getActivity()).gotoLoginActivity();
			break;
		}
	}

	private ListView tabvf2_dy_viewp;
	private TextView inform;
	private ScrollView sl;
	private TextView top_tv1,top_tv2,top_tv3;
	private LinearLayout history_ll,top_rl1,top_rl2,top_rl3,tabvf2_ll,frag2_top;

	private void initView(View view) {
		tabvf2_dy_viewp = (ListView) view.findViewById(R.id.tabvf2_dy_viewp);
		tabvf2_dy_viewp.setFocusable(false);

		sl = (ScrollView) view.findViewById(R.id.sl);
		frag2_top = (LinearLayout) view.findViewById(R.id.frag2_top);
		history_ll = (LinearLayout) view.findViewById(R.id.tabvf2_history_ll);
		top_rl1= (LinearLayout) view.findViewById(R.id.frag2_top_rl1);
		top_rl1.setOnClickListener(this);
		top_rl2= (LinearLayout) view.findViewById(R.id.frag2_top_rl2);
		top_rl2.setOnClickListener(this);
		tabvf2_ll= (LinearLayout) view.findViewById(R.id.tabvf2_ll);
		tabvf2_ll.setOnClickListener(this);
		top_rl3= (LinearLayout) view.findViewById(R.id.frag2_top_rl3);
		top_rl3.setOnClickListener(this);
		top_tv1 = (TextView) view.findViewById(R.id.frag2_top_tv1);
		top_tv2 = (TextView) view.findViewById(R.id.frag2_top_tv2);
		top_tv3 = (TextView) view.findViewById(R.id.frag2_top_tv3);

		inform = (TextView) view.findViewById(R.id.inform);
		inform.setOnClickListener(this);
	}

	private void getuser() {
		if (userid != null && !userid.equals("")) {
			inform.setVisibility(View.GONE);
			sl.setVisibility(View.VISIBLE);
			frag2_top.setVisibility(View.VISIBLE);
			tabvf2_ll.setVisibility(View.VISIBLE);
			history_ll.setVisibility(View.VISIBLE);
			top_rl2.performClick();
		} else {
			ActivityJumpControl.getInstance(getActivity()).gotoLoginActivity();
//			inform.setVisibility(View.VISIBLE);
//			sl.setVisibility(View.GONE);
//			frag2_top.setVisibility(View.GONE);
//			tabvf2_ll.setVisibility(View.GONE);
//			history_ll.setVisibility(View.GONE);
		}

	}

	private void getsubscribe() {
		startProgressDialog();
		String strUrl = URLManager.MySubscribe;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}
	private void getRecommend() {
		startProgressDialog();
		String strUrl = URLManager.SubscribeRecommend;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		Futil.xutils(strUrl, map, handler, URLManager.three);
	}
	private void delsubscribe(String uid,String alid) {
		startProgressDialog();
		String strUrl = URLManager.DelSubscribe;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userSubscribe.userId", uid);
		map.put("userSubscribe.audioAlbumId", alid);
		Futil.xutils(strUrl, map, handler, URLManager.two);
	}


	private MyPHSHistoryAdapter historyapter;
	private ArrayList<AudioList> historylist;
	private void gethistory() {
		historylist = new ArrayList<AudioList>();
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
		tabvf2_dy_viewp.setVisibility(View.VISIBLE);
		historyapter = new MyPHSHistoryAdapter(context, historylist);
		tabvf2_dy_viewp.setAdapter(historyapter);
		historyapter.notifyDataSetChanged();

		historylistener();
		stopProgressDialog();
	}

	private TextView phs_clear_tv, phs_delete_tv;
	private boolean visflag = false;

	private void historylistener() {
		phs_clear_tv = (TextView) view.findViewById(R.id.phs_clear_tv);
		phs_delete_tv = (TextView)  view.findViewById(R.id.phs_delete_tv);
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
				top_rl2.performClick();
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
			progressDialog = CustomProgressDialog.createDialog(getActivity());
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
