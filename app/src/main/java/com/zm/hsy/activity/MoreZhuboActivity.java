package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.MyZbListAdapter;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/**主播更多*/
public class MoreZhuboActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
                ulist=new ArrayList<User>();
                try {
                    JSONArray usercfList= obj.getJSONArray("userConcemFansList");
                    for(int i=0;i<usercfList.length();i++){
                        JSONObject userlist=  usercfList.getJSONObject(i);
                        User user =new User();
                        user.setHeadStatus(userlist.getString("headStatus"));
                        user.setConcemFans(userlist.getString("concemFans"));
                        user.setAudioCount( userlist.getString("audioCount"));
                        user.setIfgz( userlist.getString("ifgz"));
                        JSONObject usercf=userlist.getJSONObject("user");
                        user.setHead(usercf.getString("head"));
                        user.setId(usercf.getString("id"));
                        user.setNickname( usercf.getString("nickname"));
                        user.setMembers(usercf.getString("members"));
                        user.setBlurb( usercf.getString("blurb"));
                        ulist.add(user);
                    }
                    adapter =new MyZbListAdapter(context,ulist);
					morezb_viewp.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
			stopProgressDialog();
		}

	};

	private String userid, belong,name;
	private Context context;
	private MyZbListAdapter adapter;
	private ArrayList<User> ulist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morezhubo);
		context=this;
		Intent i = this.getIntent();
		belong = i.getStringExtra("belong");
		name = i.getStringExtra("name");
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
	private TextView morezb_top_tv;
	private ListView morezb_viewp;

	private void findview() {
        morezb_viewp = (ListView) findViewById(R.id.morezb_viewp);
        morezb_viewp.setFocusable(false);
		morezb_top_tv = (TextView) findViewById(R.id.morezb_top_tv);
		morezb_top_tv.setText(name);
		findViewById(R.id.back_top).setOnClickListener(this);

	}

	private void getindexMore() {
		startProgressDialog();
		String strUrl =  URLManager.UserByBelong;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("belong",belong);
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