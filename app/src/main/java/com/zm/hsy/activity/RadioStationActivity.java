package com.zm.hsy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.TabVF4ListAdapter;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.SyncHorizontalScrollView;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/*** 电台列表1本地2省市 */
@SuppressLint("SimpleDateFormat")
public class RadioStationActivity extends Activity implements OnClickListener {
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {//本地台
				JSONObject obj = (JSONObject) msg.obj;
				try {
					audioList = new ArrayList<AudioList>();
					JSONArray jsonArray = obj.getJSONArray("radioList");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject radioRank = jsonArray.getJSONObject(i);
						AudioList audio = new AudioList();
						audio.setPlayAmount(radioRank.getString("hits"));
						audio.setPath(radioRank.getString("address"));
						audio.setId(radioRank.getString("id"));
						audio.setAudioName(radioRank.getString("name"));
						audioList.add(audio);
					}
					f4adapter = new TabVF4ListAdapter(context, audioList,"rad");
					rank_viewp.setAdapter(f4adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (msg.what == URLManager.two) {//省市台
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray jsonArray = obj.getJSONArray("radioRankList");
					audioList = new ArrayList<AudioList>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject radioRank = jsonArray.getJSONObject(i);
						AudioList audio = new AudioList();
						audio.setPlayAmount(radioRank.getString("hits"));
						audio.setPath(radioRank.getString("address"));
						audio.setId(radioRank.getString("id"));
						audio.setAudioName(radioRank.getString("name"));
						audioList.add(audio);
					}
					f4adapter = new TabVF4ListAdapter(context, audioList,"rad");
					rank_viewp.setAdapter(f4adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray jsonArray = obj.getJSONArray("provinceList");
					stringList = new ArrayList<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						String radioRank = jsonArray.getString(i);
						stringList.add(radioRank);
					}
					initNavigationHSV();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if (msg.what == URLManager.three) {//国家台
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray jsonArray = obj.getJSONArray("radioList");
					audioList = new ArrayList<AudioList>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject radioRank = jsonArray.getJSONObject(i);
						AudioList audio = new AudioList();
						audio.setPlayAmount(radioRank.getString("hits"));
						audio.setPath(radioRank.getString("address"));
						audio.setId(radioRank.getString("id"));
						audio.setAudioName(radioRank.getString("name"));
						audioList.add(audio);
					}
					f4adapter = new TabVF4ListAdapter(context, audioList,"rad");
					rank_viewp.setAdapter(f4adapter);
					radiostation_mHsv.setVisibility(View.GONE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (msg.what == URLManager.four) {//网络台
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray jsonArray = obj.getJSONArray("radioList");
					audioList = new ArrayList<AudioList>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject radioRank = jsonArray.getJSONObject(i);
						AudioList audio = new AudioList();
						audio.setPlayAmount(radioRank.getString("hits"));
						audio.setPath(radioRank.getString("address"));
						audio.setId(radioRank.getString("id"));
						audio.setAudioName(radioRank.getString("name"));
						audioList.add(audio);
					}
					f4adapter = new TabVF4ListAdapter(context, audioList,"rad");
					rank_viewp.setAdapter(f4adapter);
					radiostation_mHsv.setVisibility(View.GONE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}
	};

	private String userid, tag, province;
	private Context context;
	private ArrayList<AudioList> audioList;
	private ArrayList<String> stringList;
	private TabVF4ListAdapter f4adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_radiostation);
		context = this;
		userid = Futil.getValue(RadioStationActivity.this, "userid");
		findview();
		Intent i = this.getIntent();
		tag = i.getStringExtra("tag");
		province = i.getStringExtra("province");
		if (tag.equals("1")) {
			radiostation_top_tv.setText("本地电台");
			gointo(province);
			radiostation_rl_tab.setVisibility(View.GONE);
			if (province.equals("") || province==null) {
				if ((MainActivity.province).isEmpty()){
					Toast.makeText(context, "获取位置失败，请在隐私设置中允许惠思语进行定位", Toast.LENGTH_SHORT).show();
				}else {
					gointo(MainActivity.province);
				}
			}
		} else if (tag.equals("3")) {
			radiostation_top_tv.setText("省市电台");
			gointoall();
			radiostation_rl_tab.setVisibility(View.VISIBLE);
		} else if (tag.equals("2")) {
			radiostation_top_tv.setText("国家电台");
			gointoguojia();
			radiostation_rl_tab.setVisibility(View.VISIBLE);
		} else if (tag.equals("4")) {
			radiostation_top_tv.setText("网络电台");
			gointonetwork();
			radiostation_rl_tab.setVisibility(View.VISIBLE);
		}
		setListener();
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

	private int indicatorWidth;// 每个标签所占的宽度
	private LayoutInflater mInflater;
	private int currentIndicatorLeft = 0;// 当前所在标签页面的位移

	private ImageView back_top;
	private TextView radiostation_top_tv;
	private ListView rank_viewp;
	private SyncHorizontalScrollView radiostation_mHsv;
	private RadioGroup radiostation_rg_content;
	private ImageView radiostation_iv_left, radiostation_iv_right;
	private ImageView radiostation_iv_indicator;
	private RelativeLayout radiostation_rl, radiostation_rl_tab;

	private void findview() {

		radiostation_rl_tab = (RelativeLayout) findViewById(R.id.radiostation_rl_tab);
		radiostation_rl = (RelativeLayout) findViewById(R.id.radiostation_rl);
		radiostation_mHsv = (SyncHorizontalScrollView) findViewById(R.id.radiostation_mHsv);
		radiostation_rg_content = (RadioGroup) findViewById(R.id.radiostation_rg_content);
		radiostation_iv_indicator = (ImageView) findViewById(R.id.radiostation_iv_indicator);
		radiostation_iv_left = (ImageView) findViewById(R.id.radiostation_iv_left);
		radiostation_iv_right = (ImageView) findViewById(R.id.radiostation_iv_right);

		radiostation_top_tv = (TextView) findViewById(R.id.radiostation_top_tv);
		rank_viewp = (ListView) findViewById(R.id.radiostation_viewp);
		rank_viewp.setFocusable(false);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

		// 获取布局填充器
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		indicatorWidth = dm.widthPixels / 4;

		LayoutParams cursor_Params = radiostation_iv_indicator
				.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		radiostation_iv_indicator.setLayoutParams(cursor_Params);
		radiostation_mHsv.setSomeParam(radiostation_rl, radiostation_iv_left,
				radiostation_iv_right, this);

	}

	// 添加顶部标签
	private void initNavigationHSV() {
		radiostation_rg_content.removeAllViews();
		for (int i = 0; i < stringList.size(); i++) {
			RadioButton rb = (RadioButton) mInflater.inflate(
					R.layout.activity_tabvf2_top_title, null);
			String name = stringList.get(i);
			rb.setId(i);
			rb.setText(name);

			rb.setLayoutParams(new LayoutParams(indicatorWidth,
					LayoutParams.MATCH_PARENT));

			radiostation_rg_content.addView(rb);
		}

		((RadioButton) radiostation_rg_content.getChildAt(0)).performClick();
	}

	private void setListener() {

		radiostation_rg_content
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (radiostation_rg_content.getChildAt(checkedId) != null) {

							TranslateAnimation animation = new TranslateAnimation(
									currentIndicatorLeft,
									((RadioButton) radiostation_rg_content
											.getChildAt(checkedId)).getLeft(),
									0f, 0f);
							animation.setInterpolator(new LinearInterpolator());
							animation.setDuration(100);
							animation.setFillAfter(true);
							// 执行位移动画
							radiostation_iv_indicator.startAnimation(animation);
							String name = stringList.get(checkedId);
							gointo(name);
							// // 记录当前 下标的距最左侧的 距离
							currentIndicatorLeft = ((RadioButton) radiostation_rg_content
									.getChildAt(checkedId)).getLeft();

							radiostation_mHsv
									.smoothScrollTo(
											(checkedId > 1 ? ((RadioButton) radiostation_rg_content
													.getChildAt(checkedId))
													.getLeft() : 0)
													- ((RadioButton) radiostation_rg_content
															.getChildAt(1))
															.getLeft(), 0);
						}
					}
				});

	}

	/**
	 * 进入本地电台时调用-
	 */
	private void gointo(String province) {
		startProgressDialog();
		String strUrl = URLManager.radioListByProvince;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("province", province);
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}

	/**
	 * 进入省市电台时调用-
	 */
	private void gointoall() {
		startProgressDialog();
		String strUrl = URLManager.defaultPacList;
		HashMap<String, String> map = new HashMap<String, String>();
		Futil.xutils(strUrl, map, handler, URLManager.two);
	}

	/**
	 * 国家台
	 */
	private void gointoguojia() {
		startProgressDialog();
		String strUrl = URLManager.radioListByProvince;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("province","国家");
		Futil.xutils(strUrl, map, handler, URLManager.three);
	}

	/**
	 * 网络台
	 */
	private void gointonetwork() {
		startProgressDialog();
		String strUrl = URLManager.radioListByProvince;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("province","网络");
		Futil.xutils(strUrl, map, handler, URLManager.four);
	}

	private CustomProgressDialog progressDialog;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context);
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

}
