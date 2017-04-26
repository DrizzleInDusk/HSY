package com.zm.hsy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.entity.AudioAlbumRecommendList;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.fragment.TabVF2Fragment1;
import com.zm.hsy.fragment.TabVF2Fragment2;
import com.zm.hsy.fragment.TabVF2Fragment3;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.myview.SyncHorizontalScrollView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
/** 首页音频内容跳转页 */
public class TabVF2Activity extends FragmentActivity {
	public static String Tag, TopName;
	private RelativeLayout tabvf2_rl;
	private SyncHorizontalScrollView tabvf2_mHsv;
	private RadioGroup tabvf2_rg_content;
	private ImageView tabvf2_back;
	private ImageView tabvf2_iv_left, tabvf2_iv_right;
	private ImageView tabvf2_iv_indicator;
	private ViewPager tabvf2_mViewPager;
	private TextView tabvf2_top_tv2;
	private int indicatorWidth;// 每个标签所占的宽度
	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;// 当前所在标签页面的位移

	public static List<String> itemids = new ArrayList<String>();
	private static ArrayList<AudioAlbumRecommendList> albumList = new ArrayList<AudioAlbumRecommendList>();

	// 固定两个标签
	public void addAlbumList() {
		AudioAlbumRecommendList titlelist1 = new AudioAlbumRecommendList();
		AudioAlbumRecommendList titlelist2 = new AudioAlbumRecommendList();
		titlelist1.setRecommendName("推荐");
		titlelist1.setId(0);
		titlelist2.setRecommendName("全部");
		titlelist2.setId(1);
		albumList.add(titlelist1);
		albumList.add(titlelist2);
	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.five) {
				JSONObject object = (JSONObject) msg.obj;
				try {
					// if (null !=
					// object.getString("audioAlbumRecommendList")&&!object.getString("audioAlbumRecommendList").equals("null"))
					// {
					System.out.println("--if------");
					JSONArray data = object
							.getJSONArray("audioAlbumRecommendList");
					for (int f = 0; f < data.length(); f++) {
						JSONObject objectData = data.getJSONObject(f);
						AudioAlbumRecommendList titlelist = new AudioAlbumRecommendList();
						String recommendName = objectData
								.getString("recommendName");
						int id = objectData.getInt("id");
						titlelist.setRecommendName(recommendName);
						titlelist.setId(id);
						albumList.add(titlelist);
					}

					mAdapter.notifyDataSetChanged();
					initNavigationHSV();
					// }else{
					// System.out.println("-else-------");
					// mAdapter.notifyDataSetChanged();
					// initNavigationHSV();
					// }
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("--Exception------");
					mAdapter.notifyDataSetChanged();
					initNavigationHSV();
				}

			}
			stopProgressDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabvf2);
		Intent i = this.getIntent();
		Tag = i.getStringExtra("Tag");
		TopName = i.getStringExtra("TopName");

		albumList.clear();
		addAlbumList();

		gointo(Tag);
		findViewById();
		initView();
		setListener();
		myListener();
	}

	protected void onRestart() {
		setcontextaudio(App.getPlaycode() == -1);
		super.onRestart();
	}
	public static boolean mcfonact = false;
	@Override
	protected void onResume() {
		mcfonact = true;
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);

		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mcfonact = false;
		JPushInterface.onPause(this);
		MobclickAgent.onPause(this);
	}
	private void myListener() {
		tabvf2_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void gointo(String TAG) {
		startProgressDialog();
		String strUrl = URLManager.TuiJian;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("albumTypeId", TAG);
		Futil.xutils(strUrl, map, handler, URLManager.five);

	}

	private void initView() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		indicatorWidth = dm.widthPixels / 4;

		LayoutParams cursor_Params = tabvf2_iv_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		tabvf2_iv_indicator.setLayoutParams(cursor_Params);
		tabvf2_mHsv.setSomeParam(tabvf2_rl, tabvf2_iv_left, tabvf2_iv_right,
				this);

		// 获取布局填充器
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		tabvf2_mViewPager.setAdapter(mAdapter);
	}

	// 添加顶部标签
	private void initNavigationHSV() {
		tabvf2_mViewPager.setVisibility(View.VISIBLE);
		tabvf2_rg_content.removeAllViews();
		itemids.clear();
		for (int i = 0; i < albumList.size(); i++) {
			RadioButton rb = (RadioButton) mInflater.inflate(
					R.layout.activity_tabvf2_top_title, null);
			String recommendname = albumList.get(i).getRecommendName();
			itemids.add("" + albumList.get(i).getId());

			rb.setId(i);
			rb.setText(recommendname);

			rb.setLayoutParams(new LayoutParams(indicatorWidth,
					LayoutParams.MATCH_PARENT));

			tabvf2_rg_content.addView(rb);
		}

		((RadioButton) tabvf2_rg_content.getChildAt(0)).performClick();
	}

	private RoundedImageView playbar_touxiang;
	private ImageView playbar_right;
	private static ImageView playbar_play;
	private TextView playbar_name;

	private void findViewById() {
		/** 后台播放信息 */
		playbar_touxiang = (RoundedImageView) findViewById(R.id.playbar_touxiang);
		playbar_right = (ImageView) findViewById(R.id.playbar_right);
		playbar_play = (ImageView) findViewById(R.id.playbar_play);
		playbar_name = (TextView) findViewById(R.id.playbar_name);
		setcontextaudio(App.getPlaycode() == -1);

		tabvf2_back = (ImageView) findViewById(R.id.back_top);
		tabvf2_rl = (RelativeLayout) findViewById(R.id.tabvf2_rl);

		tabvf2_mHsv = (SyncHorizontalScrollView) findViewById(R.id.tabvf2_mHsv);
		tabvf2_rg_content = (RadioGroup) findViewById(R.id.tabvf2_rg_content);
		tabvf2_iv_indicator = (ImageView) findViewById(R.id.tabvf2_iv_indicator);
		tabvf2_iv_left = (ImageView) findViewById(R.id.tabvf2_iv_left);
		tabvf2_iv_right = (ImageView) findViewById(R.id.tabvf2_iv_right);
		tabvf2_top_tv2 = (TextView) findViewById(R.id.tabvf2_top_tv2);
		tabvf2_top_tv2.setText(TopName);
		tabvf2_mViewPager = (ViewPager) findViewById(R.id.tabvf2_mViewPager);
		tabvf2_mViewPager.setVisibility(View.GONE);
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
				Picasso.with(TabVF2Activity.this).load(cover).resize(400, 400)
						.placeholder(R.color.touming)
						.error(R.mipmap.ic_launcher)
						.into(playbar_touxiang);
			}
		} else if (isstation == 1) {
			mmsh = App.getPlayerpath();
			stationname = App.getStationname();
			stationid = App.getStationid();
			Picasso.with(TabVF2Activity.this).load(R.mipmap.yyp)
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
					ActivityJumpControl.getInstance(TabVF2Activity.this)
							.gotoDetailsPlayActivity(id);
				} else if (isstation == 1) {
					ActivityJumpControl.getInstance(TabVF2Activity.this)
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
	private void setListener() {

		tabvf2_mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (tabvf2_rg_content != null
						&& tabvf2_rg_content.getChildCount() > position) {
					((RadioButton) tabvf2_rg_content.getChildAt(position))
							.performClick();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		tabvf2_rg_content
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (tabvf2_rg_content.getChildAt(checkedId) != null) {

							TranslateAnimation animation = new TranslateAnimation(
									currentIndicatorLeft,
									((RadioButton) tabvf2_rg_content
											.getChildAt(checkedId)).getLeft(),
									0f, 0f);
							animation.setInterpolator(new LinearInterpolator());
							animation.setDuration(100);
							animation.setFillAfter(true);
							// 执行位移动画
							tabvf2_iv_indicator.startAnimation(animation);

							// 跟随一起 切换
							tabvf2_mViewPager.setCurrentItem(checkedId); // ViewPager

							// // 记录当前 下标的距最左侧的 距离
							currentIndicatorLeft = ((RadioButton) tabvf2_rg_content
									.getChildAt(checkedId)).getLeft();

							tabvf2_mHsv
									.smoothScrollTo(
											(checkedId > 1 ? ((RadioButton) tabvf2_rg_content
													.getChildAt(checkedId))
													.getLeft() : 0)
													- ((RadioButton) tabvf2_rg_content
															.getChildAt(1))
															.getLeft(), 0);
						}
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Bundle args = new Bundle();
			Fragment ft = null;
			switch (arg0) {
			case 0:
				ft = new TabVF2Fragment2();
				args.putString("Tag", Tag);
				args.putString("TopName", TopName);
				ft.setArguments(args);

				break;
			case 1:
				ft = new TabVF2Fragment1();
				args.putString("Tag", Tag);
				ft.setArguments(args);
				break;

			default:
				ft = new TabVF2Fragment3();
				String itemid = itemids.get(arg0);

				args.putString("id", itemid);
				ft.setArguments(args);
				break;
			}
			return ft;
		}

		@Override
		public int getCount() {

			return albumList.size();
		}

	}

	/**
	 * 等待页
	 */
	private CustomProgressDialog progressDialog;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog
					.createDialog(TabVF2Activity.this);
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
