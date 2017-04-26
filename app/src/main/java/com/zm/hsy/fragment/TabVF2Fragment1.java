package com.zm.hsy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zm.hsy.R;
import com.zm.hsy.adapter.AlbumListAdapter;
import com.zm.hsy.adapter.MyGridViewAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TabVF2Fragment1 extends Fragment implements OnClickListener {
	private TextView text;
	private ArrayList<Album> albumList;
	private ArrayList<Album> changeList;
	public String TAG;
	private AlbumListAdapter adapter;
	private AlbumListAdapter Conditionadapter;
	private ListView mlistView;

	private Handler handler = new Handler() {
		@SuppressLint("ResourceAsColor")
		public void handleMessage(android.os.Message msg) {
			JSONObject object = (JSONObject) msg.obj;
			if (msg.what == URLManager.one) {
				albumList.clear();
				llay1.removeAllViews();
				try {
					final JSONArray albunTypeList = object
							.getJSONArray("albumTypeList");
					for (int f = 0; f < albunTypeList.length(); f++) {
						JSONObject objectData = albunTypeList.getJSONObject(f);
						if (objectData.getString("parentId").equals(TAG)) {
							final TextView tv = new TextView(getActivity());
							tv.setText(objectData.getString("typeName"));
							tv.setPadding(15, 0, 15, 0);
							tv.setTextSize(15);
							tv.setTextColor(Color.parseColor("#000000"));
							tv.setId(objectData.getInt("id"));
							final int id = objectData.getInt("id");
							tv.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									llay1_quanbu.setSelected(false);
									if (v != text) {
										TextView temp = (TextView) v;
										((TextView) v).setTextColor(Color
												.parseColor("#1abc9c"));
										llay1_quanbu.setTextColor(Color.parseColor("#000000"));
										if (text != null) {
											text.setTextColor(Color
													.parseColor("#000000"));

										}
										text = temp;
									}
									gview1_3.setVisibility(View.GONE);
									goid1 = "" + id;
									ConditionId();
									goTow(id, albunTypeList);
								}
							});
							llay1.addView(tv);

						}

					}
					JSONArray audioAlbumList = object
							.getJSONArray("audioAlbumList");
					for (int i = 0; i < audioAlbumList.length(); i++) {
						Album album = new Album();
						JSONObject data = audioAlbumList.getJSONObject(i);

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
						albumList.add(album);
					}

					adapter = new AlbumListAdapter(getActivity(), albumList);
					mlistView.setAdapter(adapter);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			} else if (msg.what == URLManager.four) {
				changeList.clear();
				albumList.clear();
				try {
					JSONArray objconditionIdList = object
							.getJSONArray("audioAlbumConditionIdList");
					changeList.clear();
					for (int i = 0; i < objconditionIdList.length(); i++) {
						Album album = new Album();
						JSONObject data = objconditionIdList.getJSONObject(i);

						String cover = data.getString("cover");
						String albumName = data.getString("albumName");
						String blurb = data.getString("blurb");
						String playAmount = data.getString("playAmount");
						String episode = data.getString("episode");
						album.setId(data.getString("id"));
						album.setAlbumName(albumName);
						album.setBlurb(blurb);
						album.setPlayAmount(playAmount);
						album.setEpisode(episode);
						album.setCover(cover);
						changeList.add(album);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				albumList.addAll(changeList);
				Conditionadapter = new AlbumListAdapter(getActivity(),
						albumList);
				mlistView.setAdapter(Conditionadapter);
				Conditionadapter.notifyDataSetChanged();

			}
			stopProgressDialog();
		}

	};
	private HorizontalScrollView hscro1;
	private LinearLayout llay1;
	private TextView llay1_quanbu, llay2_quanbu, llay3_quanbu, llay4_zuihuo,
			llay4_zuijin, llay4_jingdian;
	private GridView gview1_2, gview1_3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle bundle = getArguments();
		TAG = bundle.getString("Tag");
		View view = inflater.inflate(R.layout.tabvf2_all, container, false);
		mlistView = (ListView) view.findViewById(R.id.letter_all_content_view);
		mlistView.setFocusable(false);

		albumList = new ArrayList<Album>();
		changeList = new ArrayList<Album>();
		gointo(TAG);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// 动态添加
		hscro1 = (HorizontalScrollView) view.findViewById(R.id.hscro1);
		llay1 = (LinearLayout) view.findViewById(R.id.llay1);
		gview1_2 = (GridView) view.findViewById(R.id.gview1_2);
		gview1_3 = (GridView) view.findViewById(R.id.gview1_3);

		// 固定组键
		llay1_quanbu = (TextView) view.findViewById(R.id.llay1_quanbu);
		llay2_quanbu = (TextView) view.findViewById(R.id.llay2_quanbu);
		llay3_quanbu = (TextView) view.findViewById(R.id.llay3_quanbu);
		llay4_zuihuo = (TextView) view.findViewById(R.id.llay4_zuihuo);
		llay4_zuijin = (TextView) view.findViewById(R.id.llay4_zuijin);
		llay4_jingdian = (TextView) view.findViewById(R.id.llay4_jingdian);

		llay1_quanbu.setOnClickListener(this);
		llay2_quanbu.setOnClickListener(this);
		llay3_quanbu.setOnClickListener(this);
		llay4_zuihuo.setOnClickListener(this);
		llay4_zuijin.setOnClickListener(this);
		llay4_jingdian.setOnClickListener(this);
		llay1_quanbu.setTextColor(Color.parseColor("#1abc9c"));
		list.add(llay1_quanbu);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llay1_quanbu:
			llay1_quanbu.setTextColor(Color.parseColor("#1abc9c"));
			gview1_2.setVisibility(View.GONE);
			gview1_3.setVisibility(View.GONE);
			if (text != null) {
				text.setTextColor(Color.parseColor("#000000"));
			}
			goid1 = "";
			ConditionId();
			break;
		case R.id.llay2_quanbu:
			// llay2_quanbu.setSelected(true);
//			ConditionId();
			break;
		case R.id.llay3_quanbu:
			// llay3_quanbu.setSelected(true);
//			ConditionId();
			break;
		case R.id.llay4_zuihuo:
			ll4(llay4_zuihuo);
			sortType = "zuihuo";
			ConditionId();
			break;
		case R.id.llay4_zuijin:
			ll4(llay4_zuijin);
			sortType = "zuixin";
			ConditionId();
			break;
		case R.id.llay4_jingdian:
			ll4(llay4_jingdian);
			sortType = "jingdian";
			ConditionId();
			break;
		}
	}
	private ArrayList<TextView> list = new ArrayList<TextView>();
	public void ll4(TextView tv) {
		for (int i = 0; i < list.size(); i++) {
			list.remove(i).setTextColor(Color.parseColor("#000000"));
		}
		tv.setTextColor(Color.parseColor("#1abc9c"));
		list.add(tv);
	}

	private String conditionId = "";// 多条件选择参数conditionId=1,2,3
	private String sortType = "zuihuo";// 多条件选择参数sortType标识唯一
	private String goid1 = null;// 类别 查询
	private String goid2 = null;// 分类查询
	private String goid3 = null;// 状态查询
	private String itemid;

	public void goTow(int pid, final JSONArray albunTypeList) {

		List<String> items = new ArrayList<String>();

		final List<String> itemids = new ArrayList<String>();

		for (int i = 0; i < albunTypeList.length(); i++) {
			JSONObject dataObject;
			try {
				dataObject = albunTypeList.getJSONObject(i);

				if (pid == dataObject.getInt("parentId")) {

					items.add(dataObject.getString("typeName"));
					itemids.add(dataObject.getString("id"));

				}
				if (items.size() != 0) {
					gview1_2.setVisibility(View.VISIBLE);
				} else {
					gview1_2.setVisibility(View.GONE);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		final MyGridViewAdapter gview1_2dapter = new MyGridViewAdapter(
				getActivity(), items);
		setGridView(gview1_2, items, gview1_2dapter);
		// gview1_2点击事件
		gview1_2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gview1_2dapter.setCheckItem(position);
				itemid = itemids.get(position);

				List<String> citems = new ArrayList<String>();
				final List<String> citemids = new ArrayList<String>();
				int p = Integer.parseInt(itemid);
				for (int i = 0; i < albunTypeList.length(); i++) {
					JSONObject dataObject;
					try {
						dataObject = albunTypeList.getJSONObject(i);

						if (p == dataObject.getInt("parentId")) {

							citems.add(dataObject.getString("typeName"));
							citemids.add(dataObject.getString("id"));

						}
						if (citems.size() != 0) {
							gview1_3.setVisibility(View.VISIBLE);
						} else {
							gview1_3.setVisibility(View.GONE);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				final MyGridViewAdapter gview1_3dapter = new MyGridViewAdapter(
						getActivity(), citems);
				setGridView(gview1_3, citems, gview1_3dapter);
				// gview1_3点击事件
				gview1_3.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						gview1_3dapter.setCheckItem(position);
						itemid = citemids.get(position);
						goid1 = "" + itemid;
						Log.i("goid1", "" + goid1);
						ConditionId();
						Log.i("ItemClickid", "" + conditionId);
					}
				});

				goid1 = "" + itemid;
				Log.i("goid1", "" + goid1);
				ConditionId();
				Log.i("ItemClickid", "" + conditionId);

			}
		});
	}

	/** 设置GirdView参数，绑定数据 */
	private void setGridView(GridView gview, final List<String> items,
			MyGridViewAdapter gviewadapter) {
		int size = items.size();
		int length = 70;
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (size * (length + 4) * density);
		int itemWidth = (int) (length * density);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
		gview.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		gview.setColumnWidth(itemWidth); // 设置列表项宽
		gview.setHorizontalSpacing(5); // 设置列表项水平间距
		gview.setStretchMode(GridView.NO_STRETCH);
		gview.setNumColumns(size); // 设置列数量=列表集合数

		gview.setAdapter(gviewadapter);

	}

	/**
	 * 条件查询
	 */
	public void ConditionId() {
		startProgressDialog();
		String s1, s2, s3;

		conditionId = goid1;

		String strUrl = URLManager.Condition;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("conditionId", conditionId);
		map.put("sortType", sortType);
		map.put("albumTypeId", TAG);
		Futil.xutils(strUrl, map, handler, URLManager.four);

	}

	/**
	 * 进入时调用
	 */
	private void gointo(String TAG) {
		startProgressDialog();

		String strUrl = URLManager.QuanBu;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("albumTypeId", TAG);
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
}
