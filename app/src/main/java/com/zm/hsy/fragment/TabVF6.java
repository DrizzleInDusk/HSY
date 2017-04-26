package com.zm.hsy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zm.hsy.R;
import com.zm.hsy.adapter.Tabvf6Adapter;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class TabVF6 extends Fragment implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject object = (JSONObject) msg.obj;
				userMap = new LinkedHashMap<>();
				try {
					JSONArray zbarray = object.getJSONArray("announcerList");
					for (int i = 0; i < zbarray.length(); i++) {
						JSONObject zbobj = zbarray.getJSONObject(i);
						
						String titlename = zbobj.getString("name");
						String headStatus = zbobj.getString("headStatus");
						String gzStatus = zbobj.getString("gzStatus");
						String titleid = zbobj.getString("id");
						
						JSONObject titleuser = zbobj.getJSONObject("user");

						User zbuser = new User();
						String uname = titleuser.getString("nickname");
						String uhead = titleuser.getString("head");
						String belong = titleuser.getString("belong");
						String uid = titleuser.getString("id");
						List<User> ulist = userMap.get(titlename);
						if (ulist == null) {
							ulist = new ArrayList<User>();
							userMap.put(titlename, ulist);
						}
						zbuser.setTitlename(titlename);
						zbuser.setNickname(uname);
						zbuser.setHead(uhead);
						zbuser.setBelong(belong);
						zbuser.setId(uid);
						zbuser.setHeadStatus(headStatus);
						zbuser.setIfgz(gzStatus);
						ulist.add(zbuser);
					}
					adapter = new Tabvf6Adapter(context, userMap);
					mlistView.setAdapter(adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(msg.what == URLManager.three){//关注
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
					stopProgressDialog();
					e.printStackTrace();
				}
			}else if(msg.what == URLManager.four){//取消关注
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
					stopProgressDialog();
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}
	};
	private Map<String, List<User>> userMap;
	private Tabvf6Adapter adapter;
	private ListView mlistView;
	private Context context;
	private String userid;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.tab_pager_zhubo, container, false);
		context=getActivity();
		userid = Futil.getValue(context, "userid");
		mlistView = (ListView) view.findViewById(R.id.zhubo_content_viewp);
		mlistView.setFocusable(false);
		gointo();
		return view;
	}

	/**
	 * 进入时调用
	 */
	private void gointo() {
		startProgressDialog();
		String strUrl = URLManager.AnnouncerPage;
		HashMap<String, String> map = new HashMap<String, String>();
		String uid ="0";
		if(userid!=null&&!userid.equals("")){
			uid=userid;
		}
		map.put("user.id",uid);
		Futil.xutils(strUrl, map, handler, URLManager.one);

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.button1:
		// break;
		default:
			break;
		}
	}

}
