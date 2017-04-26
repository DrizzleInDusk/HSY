package com.zm.hsy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.mm.sdk.modelbiz.JumpToBizProfile;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zm.hsy.R;
import com.zm.hsy.adapter.Tabvf1SjListAdapter;
import com.zm.hsy.entity.ADBean;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.BeanTu;
import com.zm.hsy.util.CustomProgressDialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TabVF1 extends Fragment implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;

				sjMap = new LinkedHashMap<String, ArrayList<Album>>();
				try {
					JSONArray xihuanArray = obj.getJSONArray("xihuanList");
					if (xihuanArray.length() != 0){
						for (int i = 0; i < xihuanArray.length(); i++) {
							JSONObject xhobj = xihuanArray.getJSONObject(i);
							String rid = xhobj.getString("rid");
							String rname = xhobj.getString("rname");
							JSONObject audioAlbum = xhobj
									.getJSONObject("audioAlbum");
							Album xihuanalbum = new Album();
							String cover = audioAlbum.getString("cover");
							String blurb = audioAlbum.getString("blurb");
							String albumid = audioAlbum.getString("id");
							ArrayList<Album> vlist = sjMap.get(rname);
							if (vlist == null) {
								vlist = new ArrayList<Album>();
								sjMap.put(rname, vlist);
							}
							xihuanalbum.setRname(rname);
							xihuanalbum.setLabel("猜你喜欢");
							xihuanalbum.setId(albumid);
							xihuanalbum.setBlurb(blurb);
							xihuanalbum.setCover(cover);
							xihuanalbum.setRid(rid);
							vlist.add(xihuanalbum);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					JSONArray jingpinArray = obj.getJSONArray("jingpinList");
					if (jingpinArray.length() != 0) {
						for (int i = 0; i < jingpinArray.length(); i++) {
							JSONObject jpobj = jingpinArray.getJSONObject(i);
							String rid = jpobj.getString("rid");
							String rname = jpobj.getString("rname");
							JSONObject audioAlbum = jpobj
									.getJSONObject("audioAlbum");
							Album jingpinalbum = new Album();
							String cover = audioAlbum.getString("cover");
							String blurb = audioAlbum.getString("blurb");
							String albumid = audioAlbum.getString("id");
							ArrayList<Album> vlist = sjMap.get(rname);
							if (vlist == null) {
								vlist = new ArrayList<Album>();
								sjMap.put(rname, vlist);
							}
							jingpinalbum.setRname(rname);
							jingpinalbum.setLabel("精品");
							jingpinalbum.setId(albumid);
							jingpinalbum.setBlurb(blurb);
							jingpinalbum.setCover(cover);
							jingpinalbum.setRid(rid);
							vlist.add(jingpinalbum);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					JSONArray sjarray = obj.getJSONArray("sjList");
					for (int i = 0; i < sjarray.length(); i++) {
						JSONObject sjobj = sjarray.getJSONObject(i);

						String rid = sjobj.getString("rid");
						String rname = sjobj.getString("rname");

						JSONObject audioAlbum = sjobj
								.getJSONObject("audioAlbum");

						Album sjalbum = new Album();
						String cover = audioAlbum.getString("cover");
						String blurb = audioAlbum.getString("blurb");
						String albumid = audioAlbum.getString("id");
						ArrayList<Album> vlist = sjMap.get(rname);
						if (vlist == null) {
							vlist = new ArrayList<Album>();
							sjMap.put(rname, vlist);
						}
						sjalbum.setId(albumid);
						sjalbum.setLabel("");
						sjalbum.setBlurb(blurb);
						sjalbum.setCover(cover);
						sjalbum.setRid(rid);
						vlist.add(sjalbum);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					stopProgressDialog();
				}
				sjadapter = new Tabvf1SjListAdapter(mContext, sjMap);
				sjlist_viewp.setAdapter(sjadapter);
			} else if (msg.what == URLManager.two) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray jsonArray = obj.getJSONArray("adsStartList");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String picture = jsonObject.getString("picture");
						picture = URLManager.Ads_URL + picture;

						String link = jsonObject.getString("link");

						ADBean bean = new ADBean();
						bean.setAdName("");
						bean.setId(i + "");
						bean.setImgUrl(picture);
						bean.setLink(link);
						listADbeans.add(bean);
					}
					initAD();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			stopProgressDialog();
		}
	};

	private ViewPager ad_viewPager;
	private TextView tv_msg;// 显示的文字Textview
	private LinearLayout ll_dian;// 添加小圆点的线性布局
	// 轮播banner的数据
	private ArrayList<ADBean> listADbeans;

	private Context mContext;
	private Tabvf1SjListAdapter sjadapter;
	private Map<String, ArrayList<Album>> sjMap;
	private ArrayList<Album> xhalbumList;
	private ArrayList<Album> jpalbumList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.tab_pager_tuijian, container,
				false);
		listADbeans = new ArrayList<ADBean>();
		xhalbumList = new ArrayList<Album>();
		jpalbumList = new ArrayList<Album>();
		mContext = getActivity();
		initView(view);
		addWXPlatform();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		gointo();
		listADbeans.clear();
		xhalbumList.clear();
		jpalbumList.clear();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_bbs:// 社区
			ActivityJumpControl.getInstance(getActivity()).gotoBBSActivity();
			break;
		case R.id.tab_renwu:// 任务
//			ActivityJumpControl.getInstance(getActivity()).gotoTaskActivity();
			if (!api.isWXAppInstalled()) {
				Futil.showMessage(mContext,"您还未安装微信客户端");
				return;
			}
			gotowxGZH();
			break;
		case R.id.tab_huodong:// 活动
			ActivityJumpControl.getInstance(getActivity())
					.gotoVariationActivity();
			break;
		case R.id.tab_lingsheng:// 铃声
			ActivityJumpControl.getInstance(getActivity()).gotoRingActivity();
			break;
		case R.id.search_rl:// 搜索
			ActivityJumpControl.getInstance(getActivity()).gotoSearchActivity();
			break;
		default:
			break;
		}

	}
	private IWXAPI api;
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(mContext, URLManager.WXappId, false);
		// 将该app注册到微信
		api.registerApp(URLManager.WXappId);

	}
	private void gotowxGZH() {
		JumpToBizProfile.Req req = new JumpToBizProfile.Req();
		req.toUserName = URLManager.WXghid; //公众号原始ID
		req.profileType = JumpToBizProfile.JUMP_TO_NORMAL_BIZ_PROFILE;
		req.extMsg = "extMsg";
		api.sendReq(req);
	}


	/**
	 * 初始化布局
	 */
	private RelativeLayout  search_rl;
	private ListView  sjlist_viewp;
	private ImageView tab_bbs, tab_renwu, tab_huodong, tab_lingsheng;

	private void initView(View view) {
		ad_viewPager = (ViewPager) view.findViewById(R.id.tabvf1_ad_viewPage);
		tv_msg = (TextView) view.findViewById(R.id.tv_msg);
		ll_dian = (LinearLayout) view.findViewById(R.id.tj_dian);


		sjlist_viewp = (ListView) view
				.findViewById(R.id.pager_tuijian_sjlist_viewp);
		sjlist_viewp.setFocusable(false);

		tab_bbs = (ImageView) view.findViewById(R.id.tab_bbs);
		tab_bbs.setOnClickListener(this);// 社区
		tab_renwu = (ImageView) view.findViewById(R.id.tab_renwu);
		tab_renwu.setOnClickListener(this);// 任务
		tab_huodong = (ImageView) view.findViewById(R.id.tab_huodong);
		tab_huodong.setOnClickListener(this);// 活动
		tab_lingsheng = (ImageView) view.findViewById(R.id.tab_lingsheng);
		tab_lingsheng.setOnClickListener(this);// 铃声
		search_rl = (RelativeLayout) view.findViewById(R.id.search_rl);
		search_rl.setOnClickListener(this);// 搜索
	}

	/**
	 * 进入时调用-
	 */
	private void gointo() {
		startProgressDialog();
		String strUrl = URLManager.InitIndex;
		HashMap<String, String> map = new HashMap<String, String>();
		Futil.xutils(strUrl, map, handler, URLManager.one);
		// 轮播图
		String strUrl1 = URLManager.adsList;
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("places", "2");
		Futil.xutils(strUrl1, map1, handler, URLManager.two);

	}

	/**
	 * 初始化轮播图
	 */
	private BeanTu bean;

	private void initAD() {
		bean = new BeanTu(ad_viewPager, tv_msg, ll_dian, mContext, listADbeans);
		bean.startViewPager(4000);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (bean != null) {
			bean.destroyView();
		}
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
