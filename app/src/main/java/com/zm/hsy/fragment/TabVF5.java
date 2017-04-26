package com.zm.hsy.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zm.hsy.R;
import com.zm.hsy.entity.Album;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

public class TabVF5 extends Fragment implements OnClickListener {
	private ArrayList<Album> bsalbumList;
	private ArrayList<Album> rbalbumList;
	private ArrayList<Album> valbumList;
	private ArrayList<Album> xsalbumList;
	private ArrayList<Album> fcalbumList;
	private ArrayList<User> zbalbumList;
	private ArrayList<Album> qlalbumList;
	private ArrayList<User> xralbumList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.tab_pager_bangdan, container,
				false);
		bsalbumList = new ArrayList<Album>();
		rbalbumList = new ArrayList<Album>();
		valbumList = new ArrayList<Album>();
		xsalbumList = new ArrayList<Album>();
		fcalbumList = new ArrayList<Album>();
		zbalbumList = new ArrayList<User>();
		qlalbumList = new ArrayList<Album>();
		xralbumList = new ArrayList<User>();

		initView(view);

		gointo();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bangdan_biaosheng:
			ActivityJumpControl.getInstance(getActivity())
			.gotoBangdanTitleListview("biaosheng","一周飙升榜","audio");
			break;
		case R.id.bangdan_rebo:
			ActivityJumpControl.getInstance(getActivity())
			.gotoBangdanTitleListview("rebo","热播节目榜","audio");
			break;
		case R.id.bangdan_video:
			ActivityJumpControl.getInstance(getActivity())
			.gotoBangdanTitleListview("video","热门影视榜","video");
			break;
		case R.id.bangdan_xiaoshuo:
			ActivityJumpControl.getInstance(getActivity())
			.gotoBangdanTitleListview("xiaoshuo","小说风云榜","audio");
			break;
		case R.id.bangdan_fanchang:
			ActivityJumpControl.getInstance(getActivity())
			.gotoBangdanTitleListview("fanchang","K歌翻唱榜","audio");
			break;
		case R.id.bangdan_qianli:
			ActivityJumpControl.getInstance(getActivity())
			.gotoBangdanTitleListview("qianli","潜力新声榜","audio");
			break;
		case R.id.bangdan_zhubo:
			ActivityJumpControl.getInstance(getActivity())
			.gotoBangdanTitleListview("zhubo","主播排行榜","user");
			break;
		case R.id.bangdan_xinren:
			ActivityJumpControl.getInstance(getActivity())
			.gotoBangdanTitleListview("xinren","新人榜","user");
			break;
		default:
			break;
		}
	}

	private TextView biaosheng_tv1, biaosheng_tv2, biaosheng_tv3, rebo_tv1,
			rebo_tv2, rebo_tv3;
	private TextView video_tv1, video_tv2, video_tv3, xiaoshuo_tv1,
			xiaoshuo_tv2, xiaoshuo_tv3;
	private TextView fanchang_tv1, fanchang_tv2, fanchang_tv3, qianli_tv1,
			qianli_tv2, qianli_tv3;
	private TextView zhubo_tv1, zhubo_tv2, zhubo_tv3, xinren_tv1, xinren_tv2,
			xinren_tv3;
	private RelativeLayout bangdan_biaosheng, bangdan_rebo, bangdan_video, bangdan_xiaoshuo,
	bangdan_fanchang, bangdan_qianli, bangdan_zhubo, bangdan_xinren;

	private void initView(View v) {
		biaosheng_tv1 = (TextView) v.findViewById(R.id.bangdan_biaosheng_tv1);
		biaosheng_tv2 = (TextView) v.findViewById(R.id.bangdan_biaosheng_tv2);
		biaosheng_tv3 = (TextView) v.findViewById(R.id.bangdan_biaosheng_tv3);
		rebo_tv1 = (TextView) v.findViewById(R.id.bangdan_rebo_tv1);
		rebo_tv2 = (TextView) v.findViewById(R.id.bangdan_rebo_tv2);
		rebo_tv3 = (TextView) v.findViewById(R.id.bangdan_rebo_tv3);
		video_tv1 = (TextView) v.findViewById(R.id.bangdan_video_tv1);
		video_tv2 = (TextView) v.findViewById(R.id.bangdan_video_tv2);
		video_tv3 = (TextView) v.findViewById(R.id.bangdan_video_tv3);
		xiaoshuo_tv1 = (TextView) v.findViewById(R.id.bangdan_xiaoshuo_tv1);
		xiaoshuo_tv2 = (TextView) v.findViewById(R.id.bangdan_xiaoshuo_tv2);
		xiaoshuo_tv3 = (TextView) v.findViewById(R.id.bangdan_xiaoshuo_tv3);
		fanchang_tv1 = (TextView) v.findViewById(R.id.bangdan_fanchang_tv1);
		fanchang_tv2 = (TextView) v.findViewById(R.id.bangdan_fanchang_tv2);
		fanchang_tv3 = (TextView) v.findViewById(R.id.bangdan_fanchang_tv3);
		qianli_tv1 = (TextView) v.findViewById(R.id.bangdan_qianli_tv1);
		qianli_tv2 = (TextView) v.findViewById(R.id.bangdan_qianli_tv2);
		qianli_tv3 = (TextView) v.findViewById(R.id.bangdan_qianli_tv3);
		zhubo_tv1 = (TextView) v.findViewById(R.id.bangdan_zhubo_tv1);
		zhubo_tv2 = (TextView) v.findViewById(R.id.bangdan_zhubo_tv2);
		zhubo_tv3 = (TextView) v.findViewById(R.id.bangdan_zhubo_tv3);
		xinren_tv1 = (TextView) v.findViewById(R.id.bangdan_xinren_tv1);
		xinren_tv2 = (TextView) v.findViewById(R.id.bangdan_xinren_tv2);
		xinren_tv3 = (TextView) v.findViewById(R.id.bangdan_xinren_tv3);

		bangdan_biaosheng = (RelativeLayout) v
				.findViewById(R.id.bangdan_biaosheng);
		bangdan_biaosheng.setOnClickListener(this);
		bangdan_rebo = (RelativeLayout) v.findViewById(R.id.bangdan_rebo);
		bangdan_rebo.setOnClickListener(this);
		bangdan_video = (RelativeLayout) v.findViewById(R.id.bangdan_video);
		bangdan_video.setOnClickListener(this);
		bangdan_xiaoshuo = (RelativeLayout) v.findViewById(R.id.bangdan_xiaoshuo);
		bangdan_xiaoshuo.setOnClickListener(this);
		bangdan_fanchang = (RelativeLayout) v.findViewById(R.id.bangdan_fanchang);
		bangdan_fanchang.setOnClickListener(this);
		bangdan_qianli = (RelativeLayout) v.findViewById(R.id.bangdan_qianli);
		bangdan_qianli.setOnClickListener(this);
		bangdan_zhubo = (RelativeLayout) v.findViewById(R.id.bangdan_zhubo);
		bangdan_zhubo.setOnClickListener(this);
		bangdan_xinren = (RelativeLayout) v.findViewById(R.id.bangdan_xinren);
		bangdan_xinren.setOnClickListener(this);

	}

	/**
	 * 进入时调用
	 */
	private void gointo() {
		startProgressDialog();

		String strUrl = URLManager.RankList;
		HashMap<String, String> map = new HashMap<String, String>();
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

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			JSONObject object = (JSONObject) msg.obj;
			if (msg.what == URLManager.one) {
				try {
					JSONArray biaoshengList = object
							.getJSONArray("biaoshengList");
					if (biaoshengList != null && biaoshengList.length() > 0) {
						for (int i = 0; i < biaoshengList.length(); i++) {
							Album bsmaudio = new Album();
							JSONObject bsdata = biaoshengList.getJSONObject(i);

							bsmaudio.setId(bsdata.getString("id"));// 专辑ID
							bsmaudio.setAlbumName(bsdata.getString("albumName"));// 专辑名
							bsmaudio.setBlurb(bsdata.getString("blurb"));// 专辑-简介
							bsalbumList.add(bsmaudio);

						}
						if (bsalbumList.size() == 1) {
							biaosheng_tv3.setVisibility(View.GONE);
							biaosheng_tv2.setVisibility(View.GONE);
							String n = bsalbumList.get(0).getAlbumName();
							String b = bsalbumList.get(0).getBlurb();
							if (b != null && b != "" && b != "null") {
								biaosheng_tv1.setText("1." + n + ":" + b);
							} else {

								biaosheng_tv1.setText("1." + n);
							}
						} else if (bsalbumList.size() == 2) {
							biaosheng_tv3.setVisibility(View.GONE);

							String n = bsalbumList.get(0).getAlbumName();
							String b = bsalbumList.get(0).getBlurb();
							String n1 = bsalbumList.get(1).getAlbumName();
							String b1 = bsalbumList.get(1).getBlurb();
							if (b != null && b != "" && b != "null") {
								biaosheng_tv1.setText("1." + n + ":" + b);
							} else {

								biaosheng_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								biaosheng_tv2.setText("2." + n1 + ":" + b1);
							} else {

								biaosheng_tv2.setText("2." + n1);
							}

						} else {
							String n = bsalbumList.get(0).getAlbumName();
							String b = bsalbumList.get(0).getBlurb();
							String n1 = bsalbumList.get(1).getAlbumName();
							String b1 = bsalbumList.get(1).getBlurb();
							String n2 = bsalbumList.get(2).getAlbumName();
							String b2 = bsalbumList.get(2).getBlurb();
							if (b != null && b != "" && b != "null") {
								biaosheng_tv1.setText("1." + n + ":" + b);
							} else {

								biaosheng_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								biaosheng_tv2.setText("2." + n1 + ":" + b1);
							} else {

								biaosheng_tv2.setText("2." + n1);
							}
							if (b2 != null && b2 != "" && b2 != "null") {
								biaosheng_tv3.setText("3." + n2 + ":" + b2);
							} else {

								biaosheng_tv3.setText("3." + n2);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray reboList = object.getJSONArray("reboList");
					if (reboList != null && reboList.length() > 0) {
						for (int i = 0; i < reboList.length(); i++) {
							Album rbmaudio = new Album();
							JSONObject rbdata = reboList.getJSONObject(i);
							rbmaudio.setId(rbdata.getString("id"));// 专辑ID
							rbmaudio.setAlbumName(rbdata.getString("albumName"));// 专辑名
							rbmaudio.setBlurb(rbdata.getString("blurb"));// 专辑-简介

							rbalbumList.add(rbmaudio);

						}
						if (rbalbumList.size() == 1) {
							rebo_tv3.setVisibility(View.GONE);
							rebo_tv2.setVisibility(View.GONE);
							String n = rbalbumList.get(1).getAlbumName();
							String b = rbalbumList.get(1).getBlurb();
							if (b != null && b != "" && b != "null") {
								rebo_tv1.setText("1." + n + ":" + b);
							} else {

								rebo_tv1.setText("1." + n);
							}
						} else if (rbalbumList.size() == 2) {
							rebo_tv3.setVisibility(View.GONE);
							String n = rbalbumList.get(0).getAlbumName();
							String b = rbalbumList.get(0).getBlurb();
							String n1 = rbalbumList.get(1).getAlbumName();
							String b1 = rbalbumList.get(1).getBlurb();
							if (b != null && b != "" && b != "null") {
								rebo_tv1.setText("1." + n + ":" + b);
							} else {

								rebo_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								rebo_tv2.setText("2." + n1 + ":" + b1);
							} else {

								rebo_tv2.setText("2." + n1);
							}
						} else {
							String n = rbalbumList.get(0).getAlbumName();
							String b = rbalbumList.get(0).getBlurb();
							String n1 = rbalbumList.get(1).getAlbumName();
							String b1 = rbalbumList.get(1).getBlurb();
							String n2 = rbalbumList.get(2).getAlbumName();
							String b2 = rbalbumList.get(2).getBlurb();
							if (b != null && b != "" && b != "null") {
								rebo_tv1.setText("1." + n + ":" + b);
							} else {

								rebo_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								rebo_tv2.setText("2." + n1 + ":" + b1);
							} else {

								rebo_tv2.setText("2." + n1);
							}
							if (b2 != null && b2 != "" && b2 != "null") {
								rebo_tv3.setText("3." + n2 + ":" + b2);
							} else {

								rebo_tv3.setText("3." + n2);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray videoList = object.getJSONArray("videoList");
					if (videoList != null && videoList.length() > 0) {
						for (int i = 0; i < videoList.length(); i++) {
							Album vmaudio = new Album();
							JSONObject vdata = videoList.getJSONObject(i);
							vmaudio.setId(vdata.getString("id"));// 专辑ID
							vmaudio.setAlbumName(vdata.getString("videoName"));// 专辑名
							vmaudio.setBlurb(vdata.getString("blurb"));// 专辑-简介

							valbumList.add(vmaudio);

						}

						if (valbumList.size() == 1) {
							video_tv3.setVisibility(View.GONE);
							video_tv2.setVisibility(View.GONE);
							String n = valbumList.get(0).getAlbumName();
							String b = valbumList.get(0).getBlurb();
							if (b != null && b != "" && b != "null") {
								video_tv1.setText("1." + n + ":" + b);
							} else {

								video_tv1.setText("1." + n);
							}
						} else if (valbumList.size() == 2) {
							video_tv3.setVisibility(View.GONE);
							String n = valbumList.get(0).getAlbumName();
							String b = valbumList.get(0).getBlurb();
							String n1 = valbumList.get(1).getAlbumName();
							String b1 = valbumList.get(1).getBlurb();
							if (b != null && b != "" && b != "null") {
								video_tv1.setText("1." + n + ":" + b);
							} else {

								video_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								video_tv2.setText("2." + n1 + ":" + b1);
							} else {

								video_tv2.setText("2." + n1);
							}
						} else {
							String n = valbumList.get(0).getAlbumName();
							String b = valbumList.get(0).getBlurb();
							String n1 = valbumList.get(1).getAlbumName();
							String b1 = valbumList.get(1).getBlurb();
							String n2 = valbumList.get(2).getAlbumName();
							String b2 = valbumList.get(2).getBlurb();
							if (b != null && b != "" && b != "null") {
								video_tv1.setText("1." + n + ":" + b);
							} else {

								video_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								video_tv2.setText("2." + n1 + ":" + b1);
							} else {

								video_tv2.setText("2." + n1);
							}
							if (b2 != null && b2 != "" && b2 != "null") {
								video_tv3.setText("3." + n2 + ":" + b2);
							} else {

								video_tv3.setText("3." + n2);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray xiaoshuoList = object
							.getJSONArray("xiaoshuoList");
					if (xiaoshuoList != null && xiaoshuoList.length() > 0) {
						for (int i = 0; i < xiaoshuoList.length(); i++) {
							Album xsmaudio = new Album();
							JSONObject xsdata = xiaoshuoList.getJSONObject(i);
							xsmaudio.setId(xsdata.getString("id"));// 专辑ID
							xsmaudio.setAlbumName(xsdata.getString("albumName"));// 专辑名
							xsmaudio.setBlurb(xsdata.getString("blurb"));// 专辑-简介

							xsalbumList.add(xsmaudio);

						}

						if (xsalbumList.size() == 1) {
							xiaoshuo_tv3.setVisibility(View.GONE);
							xiaoshuo_tv2.setVisibility(View.GONE);
							String n = xsalbumList.get(0).getAlbumName();
							String b = xsalbumList.get(0).getBlurb();
							if (b != null && b != "" && b != "null") {
								xiaoshuo_tv1.setText("1." + n + ":" + b);
							} else {

								xiaoshuo_tv1.setText("1." + n);
							}
						} else if (xsalbumList.size() == 2) {
							xiaoshuo_tv3.setVisibility(View.GONE);
							String n = xsalbumList.get(0).getAlbumName();
							String b = xsalbumList.get(0).getBlurb();
							String n1 = xsalbumList.get(1).getAlbumName();
							String b1 = xsalbumList.get(1).getBlurb();
							if (b != null && b != "" && b != "null") {
								xiaoshuo_tv1.setText("1." + n + ":" + b);
							} else {

								xiaoshuo_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								xiaoshuo_tv2.setText("2." + n1 + ":" + b1);
							} else {

								xiaoshuo_tv2.setText("2." + n1);
							}
						} else {
							String n = xsalbumList.get(0).getAlbumName();
							String b = xsalbumList.get(0).getBlurb();
							String n1 = xsalbumList.get(1).getAlbumName();
							String b1 = xsalbumList.get(1).getBlurb();
							String n2 = xsalbumList.get(2).getAlbumName();
							String b2 = xsalbumList.get(2).getBlurb();
							if (b != null && b != "" && b != "null") {
								xiaoshuo_tv1.setText("1." + n + ":" + b);
							} else {

								xiaoshuo_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								xiaoshuo_tv2.setText("2." + n1 + ":" + b1);
							} else {

								xiaoshuo_tv2.setText("2." + n1);
							}
							if (b2 != null && b2 != "" && b2 != "null") {
								xiaoshuo_tv3.setText("3." + n2 + ":" + b2);
							} else {

								xiaoshuo_tv3.setText("3." + n2);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray fanchangList = object
							.getJSONArray("fanchangList");
					if (fanchangList != null && fanchangList.length() > 0) {
						for (int i = 0; i < fanchangList.length(); i++) {
							Album fcmaudio = new Album();
							JSONObject fcdata = fanchangList.getJSONObject(i);
							fcmaudio.setId(fcdata.getString("id"));// 专辑ID
							fcmaudio.setAlbumName(fcdata.getString("albumName"));// 专辑名
							fcmaudio.setBlurb(fcdata.getString("blurb"));// 专辑-简介

							fcalbumList.add(fcmaudio);

						}

						if (fcalbumList.size() == 1) {
							fanchang_tv3.setVisibility(View.GONE);
							fanchang_tv2.setVisibility(View.GONE);
							String n = fcalbumList.get(0).getAlbumName();
							String b = fcalbumList.get(0).getBlurb();
							if (b != null && b != "" && b != "null") {
								fanchang_tv1.setText("1." + n + ":" + b);
							} else {

								fanchang_tv1.setText("1." + n);
							}
						} else if (fcalbumList.size() == 2) {
							fanchang_tv3.setVisibility(View.GONE);
							String n = fcalbumList.get(0).getAlbumName();
							String b = fcalbumList.get(0).getBlurb();
							String n1 = fcalbumList.get(1).getAlbumName();
							String b1 = fcalbumList.get(1).getBlurb();
							if (b != null && b != "" && b != "null") {
								fanchang_tv1.setText("1." + n + ":" + b);
							} else {

								fanchang_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								fanchang_tv2.setText("2." + n1 + ":" + b1);
							} else {

								fanchang_tv2.setText("2." + n1);
							}
						} else {
							String n = fcalbumList.get(0).getAlbumName();
							String b = fcalbumList.get(0).getBlurb();
							String n1 = fcalbumList.get(1).getAlbumName();
							String b1 = fcalbumList.get(1).getBlurb();
							String n2 = fcalbumList.get(2).getAlbumName();
							String b2 = fcalbumList.get(2).getBlurb();
							if (b != null && b != "" && b != "null") {
								fanchang_tv1.setText("1." + n + ":" + b);
							} else {

								fanchang_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								fanchang_tv2.setText("2." + n1 + ":" + b1);
							} else {

								fanchang_tv2.setText("2." + n1);
							}
							if (b2 != null && b2 != "" && b2 != "null") {
								fanchang_tv3.setText("3." + n2 + ":" + b2);
							} else {

								fanchang_tv3.setText("3." + n2);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray qianliList = object.getJSONArray("qianliList");
					if (qianliList != null && qianliList.length() > 0) {
						for (int i = 0; i < qianliList.length(); i++) {
							Album qlmaudio = new Album();
							JSONObject qldata = qianliList.getJSONObject(i);
							qlmaudio.setId(qldata.getString("id"));// 专辑ID
							qlmaudio.setAlbumName(qldata.getString("albumName"));// 专辑名
							qlmaudio.setBlurb(qldata.getString("blurb"));// 专辑-简介

							qlalbumList.add(qlmaudio);

						}

						if (qlalbumList.size() == 1) {
							qianli_tv3.setVisibility(View.GONE);
							qianli_tv2.setVisibility(View.GONE);
							String n = qlalbumList.get(0).getAlbumName();
							String b = qlalbumList.get(0).getBlurb();
							if (b != null && b != "" && b != "null") {
								qianli_tv1.setText("1." + n + ":" + b);
							} else {

								qianli_tv1.setText("1." + n);
							}
						} else if (fcalbumList.size() == 2) {
							qianli_tv3.setVisibility(View.GONE);
							String n = qlalbumList.get(0).getAlbumName();
							String b = qlalbumList.get(0).getBlurb();
							String n1 = qlalbumList.get(1).getAlbumName();
							String b1 = qlalbumList.get(1).getBlurb();
							if (b != null && b != "" && b != "null") {
								qianli_tv1.setText("1." + n + ":" + b);
							} else {

								qianli_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								qianli_tv2.setText("2." + n1 + ":" + b1);
							} else {

								qianli_tv2.setText("2." + n1);
							}
						} else {
							String n = qlalbumList.get(0).getAlbumName();
							String b = qlalbumList.get(0).getBlurb();
							String n1 = qlalbumList.get(1).getAlbumName();
							String b1 = qlalbumList.get(1).getBlurb();
							String n2 = qlalbumList.get(2).getAlbumName();
							String b2 = qlalbumList.get(2).getBlurb();
							if (b != null && b != "" && b != "null") {
								qianli_tv1.setText("1." + n + ":" + b);
							} else {

								qianli_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								qianli_tv2.setText("2." + n1 + ":" + b1);
							} else {

								qianli_tv2.setText("2." + n1);
							}
							if (b2 != null && b2 != "" && b2 != "null") {
								qianli_tv3.setText("3." + n2 + ":" + b2);
							} else {

								qianli_tv3.setText("3." + n2);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray zhuBoList = object.getJSONArray("zhuBoList");
					if (zhuBoList != null && zhuBoList.length() > 0) {
						for (int i = 0; i < zhuBoList.length(); i++) {
							User zbmaudio = new User();
							JSONObject zbdata = zhuBoList.getJSONObject(i).getJSONObject("user");
							zbmaudio.setId(zbdata.getString("id"));// 专辑ID
							zbmaudio.setNickname(zbdata.getString("nickname"));// 专辑名
							zbmaudio.setBlurb(zbdata.getString("blurb"));// 专辑-简介

							zbalbumList.add(zbmaudio);

						}

						if (zbalbumList.size() == 1) {
							zhubo_tv3.setVisibility(View.GONE);
							zhubo_tv2.setVisibility(View.GONE);
							String n = zbalbumList.get(0).getNickname();
							String b = zbalbumList.get(0).getBlurb();
							if (b != null && b != "" && b != "null") {
								zhubo_tv1.setText("1." + n + ":" + b);
							} else {

								zhubo_tv1.setText("1." + n);
							}
						} else if (fcalbumList.size() == 2) {
							zhubo_tv3.setVisibility(View.GONE);
							String n = zbalbumList.get(0).getNickname();
							String b = zbalbumList.get(0).getBlurb();
							String n1 = zbalbumList.get(1).getNickname();
							String b1 = zbalbumList.get(1).getBlurb();
							if (b != null && b != "" && b != "null") {
								zhubo_tv1.setText("1." + n + ":" + b);
							} else {

								zhubo_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								zhubo_tv2.setText("2." + n1 + ":" + b1);
							} else {

								zhubo_tv2.setText("2." + n1);
							}
						} else {
							String n = zbalbumList.get(0).getNickname();
							String b = zbalbumList.get(0).getBlurb();
							String n1 = zbalbumList.get(1).getNickname();
							String b1 = zbalbumList.get(1).getBlurb();
							String n2 = zbalbumList.get(2).getNickname();
							String b2 = zbalbumList.get(2).getBlurb();
							if (b != null && b != "" && b != "null") {
								zhubo_tv1.setText("1." + n + ":" + b);
							} else {

								zhubo_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								zhubo_tv2.setText("2." + n1 + ":" + b1);
							} else {

								zhubo_tv2.setText("2." + n1);
							}
							if (b2 != null && b2 != "" && b2 != "null") {
								zhubo_tv3.setText("3." + n2 + ":" + b2);
							} else {

								zhubo_tv3.setText("3." + n2);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray xinRenList = object.getJSONArray("xinRenList");
					if (xinRenList != null && xinRenList.length() > 0) {
						for (int i = 0; i < xinRenList.length(); i++) {
							User xrmaudio = new User();
							JSONObject xrdata = xinRenList.getJSONObject(i).getJSONObject("user");
							xrmaudio.setId(xrdata.getString("id"));// 专辑ID
							xrmaudio.setNickname(xrdata.getString("nickname"));// 专辑名
							xrmaudio.setBlurb(xrdata.getString("blurb"));// 专辑-简介

							xralbumList.add(xrmaudio);

						}
						if (xralbumList.size() == 1) {
							xinren_tv3.setVisibility(View.GONE);
							xinren_tv2.setVisibility(View.GONE);
							String n = xralbumList.get(0).getNickname();
							String b = xralbumList.get(0).getBlurb();
							if (b != null && b != "" && b != "null") {
								xinren_tv1.setText("1." + n + ":" + b);
							} else {

								xinren_tv1.setText("1." + n);
							}
						} else if (fcalbumList.size() == 2) {
							xinren_tv3.setVisibility(View.GONE);
							String n = xralbumList.get(0).getNickname();
							String b = xralbumList.get(0).getBlurb();
							String n1 = xralbumList.get(1).getNickname();
							String b1 = xralbumList.get(1).getBlurb();
							if (b != null && b != "" && b != "null") {
								xinren_tv1.setText("1." + n + ":" + b);
							} else {

								xinren_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								xinren_tv2.setText("2." + n1 + ":" + b1);
							} else {

								xinren_tv2.setText("2." + n1);
							}
						} else {
							String n = xralbumList.get(0).getNickname();
							String b = xralbumList.get(0).getBlurb();
							String n1 = xralbumList.get(1).getNickname();
							String b1 = xralbumList.get(1).getBlurb();
							String n2 = xralbumList.get(2).getNickname();
							String b2 = xralbumList.get(2).getBlurb();
							if (b != null && b != "" && b != "null") {
								xinren_tv1.setText("1." + n + ":" + b);
							} else {

								xinren_tv1.setText("1." + n);
							}
							if (b1 != null && b1 != "" && b1 != "null") {
								xinren_tv2.setText("2." + n1 + ":" + b1);
							} else {

								xinren_tv2.setText("2." + n1);
							}
							if (b2 != null && b2 != "" && b2 != "null") {
								xinren_tv3.setText("3." + n2 + ":" + b2);
							} else {

								xinren_tv3.setText("3." + n2);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			stopProgressDialog();
		}

	};
}
