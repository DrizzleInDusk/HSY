package com.zm.hsy.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.VariationAdapter;
import com.zm.hsy.entity.Variation;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/** 活动 */
public class VariationActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray jsonArray = obj.getJSONArray("activityList");
					vList = new ArrayList<Variation>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject variation = jsonArray.getJSONObject(i);
						Variation variations = new Variation();

						variations.setTitle(variation.getString("title"));
						variations.setStatus(variation.getString("status"));
						variations.setPropaganda(variation
								.getString("propaganda"));
						variations.setId(variation.getString("id"));
						vList.add(variations);
					}

					variationadapter = new VariationAdapter(context, vList);
					vriation_viewp.setAdapter(variationadapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}

	};

	private String userid;
	private ArrayList<Variation> vList;
	private VariationAdapter variationadapter;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vriation);
		context = this;
		userid = Futil.getValue(context, "userid");
		findview();
	}
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
		getscore();
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

		case R.id.build_vriation:// 发布活动

			if (userid != null && !userid.equals("")) {
				String shield= Futil.getValue(context,"shield");
				if(shield.equals("1")){
					ActivityJumpControl.getInstance(VariationActivity.this)
							.gotoBuildVariationActivity();
				}else{
					Futil.showMessage(context,context.getString(R.string.shield_remind));
				}
			} else {
				Futil.showMessage(context, "请先登录");
			}

			break;
		case R.id.back_top:
			finish();
			break;

		}
	}

	private ImageView back_top;
	private TextView build_vriation;
	private ListView vriation_viewp;

	private void findview() {
		vriation_viewp = (ListView) findViewById(R.id.vriation_viewp);
		vriation_viewp.setFocusable(false);

		build_vriation = (TextView) findViewById(R.id.build_vriation);
		build_vriation.setOnClickListener(this);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private void getscore() {
		startProgressDialog();
		String strUrl = URLManager.ActivityList;
		HashMap<String, String> map = new HashMap<String, String>();
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
