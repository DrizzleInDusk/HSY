package com.zm.hsy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.SearchF2Adapter;
import com.zm.hsy.adapter.SearchF3Adapter;
import com.zm.hsy.adapter.SearchF4Adapter;
import com.zm.hsy.adapter.SearchF5Adapter;
import com.zm.hsy.entity.ADBean;
import com.zm.hsy.entity.Album;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.WordWrapView;
import com.zm.hsy.util.BeanTu;
import com.zm.hsy.util.CustomProgressDialog;
/** 搜索 */
public class SearchActivity extends FragmentActivity implements OnClickListener {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray KeywordArray = obj
                            .getJSONArray("searchKeywordList");
                    searchword = new ArrayList<String>();
                    for (int i = 0; i < KeywordArray.length(); i++) {
                        JSONObject skobj = KeywordArray.getJSONObject(i);
                        String string = skobj.getString("word");
                        searchword.add(string);
                    }
                    if (KeywordArray.length() == 0) {
                        share_recommend_tv.setVisibility(View.VISIBLE);
                        share_recommend.setVisibility(View.GONE);
                    }
                    share_recommend_tv.setVisibility(View.GONE);
                    share_recommend.setVisibility(View.VISIBLE);
                    initrecommend();
                } catch (JSONException e) {
                    share_recommend_tv.setVisibility(View.VISIBLE);
                    share_recommend.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.two) {// 全部
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        requestsucess();
                        qb(obj);
                        changesearchciew(search_tv1, search_title1, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    visibleORgone();
                }
            } else if (msg.what == URLManager.three) {// 专辑
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        requestsucess();
                        zj(obj, 1);
                        changesearchciew(search_tv2, search_title2, 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    visibleORgone();
                }
            } else if (msg.what == URLManager.four) {// 主播
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        requestsucess();
                        zb(obj, 1);
                        changesearchciew(search_tv3, search_title3, 2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    visibleORgone();
                }
            } else if (msg.what == URLManager.five) {// 音频
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        requestsucess();
                        yp(obj, 1);
                        changesearchciew(search_tv4, search_title4, 3);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    visibleORgone();
                }
            } else if (msg.what == URLManager.six) {// 视频
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        requestsucess();
                        sp(obj, 1);
                        changesearchciew(search_tv5, search_title5, 4);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    visibleORgone();
                }
            } else if (msg.what == URLManager.seven) {// 轮播图
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray jsonArray = obj.getJSONArray("adsStartList");
                    listADbeans = new ArrayList<ADBean>();
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            stopProgressDialog();
        }

    };
    private static ViewPager ad_viewPager;
    private static TextView tv_msg;// 显示的文字Textview
    private static LinearLayout ll_dian;// 添加小圆点的线性布局
    // 轮播banner的数据
    private  ArrayList<ADBean> listADbeans;
    private Context context;
    private ArrayList<String> searchword;
    private SearchF2Adapter mSearchF2Adapter;
    private ArrayList<Album> albumList;
    private SearchF3Adapter mSearchF3Adapter;
    private ArrayList<User> userList;
    private SearchF4Adapter mSearchF4Adapter;
    private ArrayList<AudioList> audioList;
    private SearchF5Adapter mSearchF5Adapter;
    private ArrayList<Album> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_search);
        // 获取布局填充器
        mInflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        findview();
        findfragmentview();
        myspinner();
        creatdb();

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
        MobclickAgent.onResume(this);
        getword();
        gointo();
    }

    private String keyword;
    private String keyType = null;
    private String orderType = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.morenrl:// 默认
                orderType = "";
                searchcResult();
                civ(moreniv);
                break;
            case R.id.bofanglrl:// 播放量
                orderType = keyType + "bf";
                searchcResult();
                civ(bofangliv);
                break;
            case R.id.zuixinrl:// 最新上传
                orderType = keyType + "sj";
                searchcResult();
                civ(zuixiniv);
                break;
            case R.id.fansrl:// 粉丝数
                orderType = keyType + "fs";
                searchcResult();
                civ(fansiv);
                break;
            case R.id.zhucesjrl:// 注册时间
                orderType = keyType + "sj";
                searchcResult();
                civ(zhucesjiv);
                break;
            case R.id.search_tv1:// 全部
                changesearchciew(search_tv1, search_title1, 0);
                orderType = "";
                keyType = "qb";
                morenrl.performClick();
                break;
            case R.id.search_tv2:// 专辑
                changesearchciew(search_tv2, search_title2, 1);
                keyType = "zj";
                morenrl.performClick();
                break;
            case R.id.search_tv3:// 主播
                changesearchciew(search_tv3, search_title3, 2);
                keyType = "zb";
                morenrl.performClick();
                break;
            case R.id.search_tv4:// 音频
                changesearchciew(search_tv4, search_title4, 3);
                keyType = "yp";
                morenrl.performClick();
                break;
            case R.id.search_tv5:// 视频
                changesearchciew(search_tv5, search_title5, 4);
                keyType = "sp";
                morenrl.performClick();
                break;
            case R.id.share_bt:
                if (keyType.equals("zj")) {
                    search_tv2.performClick();
                } else if (keyType.equals("zb")) {
                    search_tv3.performClick();
                } else if (keyType.equals("yp")) {
                    search_tv4.performClick();
                } else if (keyType.equals("sp")) {
                    search_tv5.performClick();
                } else {
                    search_tv1.performClick();
                }
                String text = mAutoEdit.getText().toString().trim();
                saveSearchHistory(text);
                break;
            case R.id.share_backbt:
                finish();
                break;
            default:

                break;
        }
    }

    private Spinner spinner;
    private String wt = "";
    private List<String> data_list;
    private List<String> keyType_list;
    private ArrayAdapter<String> arr_adapter;

    /* 搜索下拉选择条件 */
    private void myspinner() {
        spinner = (Spinner) findViewById(R.id.spinner);

        // 数据
        data_list = new ArrayList<String>();
        keyType_list = new ArrayList<String>();

        data_list.add("全部");
        data_list.add("专辑");
        data_list.add("主播");
        data_list.add("音频");
        data_list.add("视频");
        keyType_list.add("qb");
        keyType_list.add("zj");
        keyType_list.add("zb");
        keyType_list.add("yp");
        keyType_list.add("sp");

        // 适配器
        arr_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                data_list);
        // 设置样式
        arr_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // 加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if (!wt.equals("")) {
                    String s = data_list.get(pos);
                    if (s.equals("专辑")) {
                        search_tv2.performClick();
                    } else if (s.equals("主播")) {
                        search_tv3.performClick();
                    } else if (s.equals("音频")) {
                        search_tv4.performClick();
                    } else if (s.equals("视频")) {
                        search_tv5.performClick();
                    } else {
                        search_tv1.performClick();
                    }
                }
                keyType = keyType_list.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private ArrayList<ImageView> clistiv = new ArrayList<ImageView>();

    private void civ(ImageView iv) {
        for (int i = 0; i < clistiv.size(); i++) {
            clistiv.remove(i).setVisibility(View.GONE);
        }
        iv.setVisibility(View.VISIBLE);
        clistiv.add(iv);
    }

    private ArrayList<TextView> listtv = new ArrayList<TextView>();
    private ArrayList<ImageView> listiv = new ArrayList<ImageView>();

    private void changesearchciew(TextView searchtv, ImageView searchiv,
                                  int choose) {
        for (int i = 0; i < listtv.size(); i++) {
            listtv.remove(i).setTextColor(Color.parseColor("#000000"));
        }
        searchtv.setTextColor(Color.parseColor("#1abc9c"));
        listtv.add(searchtv);
        for (int i = 0; i < listiv.size(); i++) {
            listiv.remove(i).setVisibility(View.GONE);
        }
        searchiv.setVisibility(View.VISIBLE);
        listiv.add(searchiv);
        if (choose == 0) {
            searchf1.setVisibility(View.VISIBLE);
            llbar.setVisibility(View.GONE);
        } else {
            searchf1_item1.setVisibility(View.GONE);
            searchf1_item2.setVisibility(View.GONE);
            searchf1_item3.setVisibility(View.GONE);
            searchf1_item4.setVisibility(View.GONE);
            // searchf1.setVisibility(View.GONE);
            llbar.setVisibility(View.VISIBLE);
            if (choose == 1) {
                morenrl.setVisibility(View.VISIBLE);
                bofanglrl.setVisibility(View.VISIBLE);
                zuixinrl.setVisibility(View.VISIBLE);
                fansrl.setVisibility(View.GONE);
                zhucesjrl.setVisibility(View.GONE);
            } else if (choose == 2) {
                morenrl.setVisibility(View.VISIBLE);
                bofanglrl.setVisibility(View.GONE);
                zuixinrl.setVisibility(View.GONE);
                fansrl.setVisibility(View.VISIBLE);
                zhucesjrl.setVisibility(View.VISIBLE);
            } else if (choose == 3) {
                morenrl.setVisibility(View.VISIBLE);
                bofanglrl.setVisibility(View.VISIBLE);
                zuixinrl.setVisibility(View.VISIBLE);
                fansrl.setVisibility(View.GONE);
                zhucesjrl.setVisibility(View.GONE);
            } else if (choose == 4) {
                morenrl.setVisibility(View.VISIBLE);
                bofanglrl.setVisibility(View.VISIBLE);
                zuixinrl.setVisibility(View.VISIBLE);
                fansrl.setVisibility(View.GONE);
                zhucesjrl.setVisibility(View.GONE);
            }
        }

    }

    private void getword() {
        startProgressDialog();
        String strUrl = URLManager.KeywordList;
        HashMap<String, String> map = new HashMap<String, String>();
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    /**
     * 条件搜索
     */
    private void searchcResult() {
        clear();
        keyword = mAutoEdit.getText().toString().trim();
        startProgressDialog();
        String strUrl = URLManager.SearchResultList;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("keyType", keyType);
        map.put("keyword", keyword);
        map.put("orderType", orderType);
        if (keyType.equals("qb")) {
            Futil.xutils(strUrl, map, handler, URLManager.two);
        } else if (keyType.equals("zj")) {
            Futil.xutils(strUrl, map, handler, URLManager.three);
        } else if (keyType.equals("zb")) {
            Futil.xutils(strUrl, map, handler, URLManager.four);
        } else if (keyType.equals("yp")) {
            Futil.xutils(strUrl, map, handler, URLManager.five);
        } else if (keyType.equals("sp")) {
            Futil.xutils(strUrl, map, handler, URLManager.six);
        }
    }

    private TextView search_tv1, search_tv2, search_tv3, search_tv4,
            search_tv5;
    private TextView searchf1_item1_tv3, searchf1_item2_tv3, searchf1_item3_tv3, searchf1_item4_tv3;
    private ImageView search_title1, search_title2, search_title3,
            search_title4, search_title5;
    private View searchf1;
    private RelativeLayout morenrl, bofanglrl, zuixinrl, fansrl, zhucesjrl;
    private ImageView moreniv, bofangliv, zuixiniv, fansiv, zhucesjiv;
    private RelativeLayout searchf1_item1, searchf1_item2, searchf1_item3,
            searchf1_item4;
    private ListView aublm_viewp, user_viewp, audio_viewp, video_viewp;
    private LinearLayout llbar;

    private void findfragmentview() {
        /* 广告轮播图 */
        ad_viewPager = (ViewPager) findViewById(R.id.search_ad_viewPage);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        ll_dian = (LinearLayout) findViewById(R.id.tj_dian);
		/* 搜索全部 */
        searchf1 = findViewById(R.id.searchf1);
        aublm_viewp = (ListView) findViewById(R.id.search_aublm_viewp);
        aublm_viewp.setFocusable(false);
        user_viewp = (ListView) findViewById(R.id.search_user_viewp);
        user_viewp.setFocusable(false);
        audio_viewp = (ListView) findViewById(R.id.search_audio_viewp);
        audio_viewp.setFocusable(false);
        video_viewp = (ListView) findViewById(R.id.search_video_viewp);
        video_viewp.setFocusable(false);
        searchf1_item1 = (RelativeLayout) findViewById(R.id.searchf1_item1);
        searchf1_item1_tv3 = (TextView) findViewById(R.id.searchf1_item1_tv3);
        searchf1_item2 = (RelativeLayout) findViewById(R.id.searchf1_item2);
        searchf1_item2_tv3 = (TextView) findViewById(R.id.searchf1_item2_tv3);
        searchf1_item3 = (RelativeLayout) findViewById(R.id.searchf1_item3);
        searchf1_item3_tv3 = (TextView) findViewById(R.id.searchf1_item3_tv3);
        searchf1_item4 = (RelativeLayout) findViewById(R.id.searchf1_item4);
        searchf1_item4_tv3 = (TextView) findViewById(R.id.searchf1_item4_tv3);

		/* 搜索其他分类五个按钮 */
        llbar = (LinearLayout) findViewById(R.id.llbar);
        // 默认
        moreniv = (ImageView) findViewById(R.id.moreniv);
        morenrl = (RelativeLayout) findViewById(R.id.morenrl);
        morenrl.setOnClickListener(this);
        // 播放量
        bofangliv = (ImageView) findViewById(R.id.bofangliv);
        bofanglrl = (RelativeLayout) findViewById(R.id.bofanglrl);
        bofanglrl.setOnClickListener(this);
        // 最新上传
        zuixiniv = (ImageView) findViewById(R.id.zuixiniv);
        zuixinrl = (RelativeLayout) findViewById(R.id.zuixinrl);
        zuixinrl.setOnClickListener(this);
        // 粉丝数
        fansiv = (ImageView) findViewById(R.id.fansiv);
        fansrl = (RelativeLayout) findViewById(R.id.fansrl);
        fansrl.setOnClickListener(this);
        // 注册时间
        zhucesjiv = (ImageView) findViewById(R.id.zhucesjiv);
        zhucesjrl = (RelativeLayout) findViewById(R.id.zhucesjrl);
        zhucesjrl.setOnClickListener(this);

		/* 切换按钮 */
        search_tv1 = (TextView) findViewById(R.id.search_tv1);
        search_title1 = (ImageView) findViewById(R.id.search_title1);
        search_tv1.setOnClickListener(this);

        search_tv2 = (TextView) findViewById(R.id.search_tv2);
        search_title2 = (ImageView) findViewById(R.id.search_title2);
        search_tv2.setOnClickListener(this);

        search_tv3 = (TextView) findViewById(R.id.search_tv3);
        search_title3 = (ImageView) findViewById(R.id.search_title3);
        search_tv3.setOnClickListener(this);

        search_tv4 = (TextView) findViewById(R.id.search_tv4);
        search_title4 = (ImageView) findViewById(R.id.search_title4);
        search_tv4.setOnClickListener(this);

        search_tv5 = (TextView) findViewById(R.id.search_tv5);
        search_title5 = (ImageView) findViewById(R.id.search_title5);
        search_tv5.setOnClickListener(this);

    }

    private WordWrapView share_history, share_recommend;
    private TextView share_bt, share_backbt, tv;
    private TextView mAutoEdit, share_clean, share_recommend_tv,
            share_history_tv;
    private View search_item, search_item_bar;
    private LinearLayout search_sou_ll;

    private void findview() {
        search_sou_ll = (LinearLayout) findViewById(R.id.search_sou_ll);
        search_item = findViewById(R.id.search_item);
        search_item_bar = findViewById(R.id.search_item_bar);

        tv = (TextView) findViewById(R.id.tv);
        tv.setVisibility(View.GONE);
        share_history = (WordWrapView) findViewById(R.id.share_history);
        share_history_tv = (TextView) findViewById(R.id.share_history_tv);
        share_recommend = (WordWrapView) findViewById(R.id.share_recommend);
        share_recommend_tv = (TextView) findViewById(R.id.share_recommend_tv);

        initSearchHistory();
        share_bt = (TextView) findViewById(R.id.share_bt);
        share_bt.setOnClickListener(this);
        share_backbt = (TextView) findViewById(R.id.share_backbt);
        share_backbt.setOnClickListener(this);

        share_clean = (TextView) findViewById(R.id.share_clean);
        share_clean.setOnClickListener(this);
        mAutoEdit = (TextView) findViewById(R.id.share_et);
        mAutoEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().trim().length();
                if (length == 0) {
                    bean.destroyView();
                    share_backbt.setVisibility(View.VISIBLE);
                    share_bt.setVisibility(View.GONE);
                    search_item.setVisibility(View.VISIBLE);
                    search_item_bar.setVisibility(View.GONE);
                    search_sou_ll.setBackgroundColor(Color
                            .parseColor("#E9F6FC"));
                    searchf1.setVisibility(View.GONE);
                    searchf1_item1.setVisibility(View.GONE);
                    searchf1_item2.setVisibility(View.GONE);
                    searchf1_item3.setVisibility(View.GONE);
                    searchf1_item4.setVisibility(View.GONE);
                    keyType = "qb";
                    wt = "";
                    creatdb();
                    initSearchHistory();

                } else {
                    initAD();
                    bean.startViewPager(4000);
                    share_backbt.setVisibility(View.GONE);
                    share_bt.setVisibility(View.VISIBLE);
                }
            }
        });

        share_clean.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                while (Futil.loadKeyArray(context, "4").size() != 0) {
                    ArrayList<String> sKey = Futil.loadKeyArray(context, "4");

                    for (int i = 0; i < sKey.size(); i++) {
                        String key = sKey.get(i);
                        Futil.romveValue(context, key, "4");
                        sKey.remove(key);
                        Futil.saveKeyArray(context, sKey, "4");
                    }
                }
                initSearchHistory();
            }
        });
    }

    /**
     * 读取热搜记录
     */
    private void initrecommend() {
        share_recommend.removeAllViews();
        for (int i = 0; i < searchword.size(); i++) {
            final String value = searchword.get(i);
            TextView textview = (TextView) mInflater.inflate(
                    R.layout.textviewgroup, null);
            textview.setText(value);
            textview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAutoEdit.setText(value);
                    saveSearchHistory(value);
                    share_bt.performClick();
                }
            });
            share_recommend.addView(textview);
        }
    }

    /**
     * 进入时调用-轮播图
     */
    private void gointo() {
        startProgressDialog();
        String strUrl1 = URLManager.adsList;
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("places", "3");
        Futil.xutils(strUrl1, map1, handler, URLManager.seven);

    }

    /**
     * 初始化轮播图
     */
    private BeanTu bean;

    private void initAD() {
        bean = new BeanTu(ad_viewPager, tv_msg, ll_dian, context, listADbeans);
    }

    /**
     * 读取历史搜索记录
     */
    private LayoutInflater mInflater;

    private void initSearchHistory() {
        share_history.removeAllViews();
        ArrayList<String> sKey = Futil.loadKeyArray(context, "4");
        if (sKey.size() == 0) {
            share_history_tv.setVisibility(View.VISIBLE);
            share_history.setVisibility(View.GONE);
        } else {
            share_history.setVisibility(View.VISIBLE);
            share_history_tv.setVisibility(View.GONE);
            for (int i = 0; i < sKey.size(); i++) {
                final String value = Futil.getValue(context, sKey.get(i));
                TextView textview = (TextView) mInflater.inflate(
                        R.layout.textviewgroup, null);
                textview.setText(value);
                textview.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAutoEdit.setText(value);
                        saveSearchHistory(value);
                        share_bt.performClick();
                    }
                });
                share_history.addView(textview);
            }
        }
    }

    /*
     * 保存搜索记录
     */
    private void saveSearchHistory(String keyword) {
        if (keyword.length() < 1) {
            return;
        }
        ArrayList<String> sKey = Futil.loadKeyArray(context, "4");
        if (sKey.size() > 0) {
            for (int i = 0; i < sKey.size(); i++) {
                if (keyword.equals(sKey.get(i))) {
                    sKey.remove(i);
                    break;
                }
            }
        }
        sKey.add(0, keyword);
        int size = sKey.size();
        if (size > 5) {
            for (int i = 5; i < sKey.size(); i++) {
                sKey.remove(i);
            }
        }
        Futil.saveKeyArray(context, sKey, "4");
        Futil.saveValue(context, keyword, keyword);
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

    private void creatdb() {
        clear();
        mSearchF2Adapter = null;
        mSearchF3Adapter = null;
        mSearchF4Adapter = null;
        mSearchF5Adapter = null;
    }

    private void clear() {
        aublm_viewp.setVisibility(View.GONE);
        user_viewp.setVisibility(View.GONE);
        audio_viewp.setVisibility(View.GONE);
        video_viewp.setVisibility(View.GONE);
    }

    private void visibleORgone() {
        search_item.setVisibility(View.GONE);
        search_item_bar.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        searchf1.setVisibility(View.GONE);
    }

    private void requestsucess() {
        wt = "1";
        tv.setVisibility(View.GONE);
        search_item.setVisibility(View.GONE);
        search_item_bar.setVisibility(View.VISIBLE);
        search_sou_ll.setBackgroundColor(Color.WHITE);
    }

    /* 解析全部页面 */
    private void qb(JSONObject obj) {
        zj(obj, 0);
        zb(obj, 0);
        yp(obj, 0);
        sp(obj, 0);
    }

    /* 解析专辑页面 */
    private void zj(JSONObject obj, int co) {
        try {
            JSONArray audioAlbumArray = obj.getJSONArray("audioAlbumList");// 专辑
            albumList = new ArrayList<Album>();
            if (audioAlbumArray.length() != 0) {
                for (int i = 0; i < audioAlbumArray.length(); i++) {
                    JSONObject audioAlbumList = audioAlbumArray
                            .getJSONObject(i);

                    Album myAlbum = new Album();
                    myAlbum.setId(audioAlbumList.getString("id"));
                    myAlbum.setAlbumName(audioAlbumList.getString("albumName"));
                    myAlbum.setBlurb(audioAlbumList.getString("blurb"));
                    myAlbum.setCover(audioAlbumList.getString("cover"));
                    myAlbum.setPlayAmount(audioAlbumList
                            .getString("playAmount"));
                    myAlbum.setEpisode(audioAlbumList.getString("episode"));
                    albumList.add(myAlbum);
                }
                mSearchF2Adapter = new SearchF2Adapter(context, albumList);
                aublm_viewp.setAdapter(mSearchF2Adapter);
                mSearchF2Adapter.notifyDataSetChanged();
                if (co == 0) {
                    searchf1_item1.setVisibility(View.VISIBLE);
                    searchf1_item1_tv3.setText(" "+audioAlbumArray.length()+" ");
                }
                aublm_viewp.setVisibility(View.VISIBLE);
            } else {
                searchf1_item1.setVisibility(View.GONE);
                aublm_viewp.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            searchf1_item1.setVisibility(View.GONE);
        }
    }

    /* 解析主播页面 */
    private void zb(JSONObject obj, int co) {
        try {

            JSONArray userConcemFansArray = obj
                    .getJSONArray("userConcemFansList");// 主播

            userList = new ArrayList<User>();
            if (userConcemFansArray.length() != 0) {
                for (int i = 0; i < userConcemFansArray.length(); i++) {
                    JSONObject userobj = userConcemFansArray.getJSONObject(i);
                    User user = new User();
                    user.setDingwei(userobj.getString("dingwei"));
                    user.setAudioCount(userobj.getString("audioCount"));
                    user.setConcemFans(userobj.getString("concemFans"));
                    user.setHeadStatus(userobj.getString("headStatus"));

                    JSONObject usercfList = userobj.getJSONObject("user");

                    user.setId(usercfList.getString("id"));
                    user.setNickname(usercfList.getString("nickname"));
                    user.setMembers(usercfList.getString("members"));
                    user.setHead(usercfList.getString("head"));

                    userList.add(user);
                }
                mSearchF3Adapter = new SearchF3Adapter(context, userList);
                user_viewp.setAdapter(mSearchF3Adapter);
                mSearchF3Adapter.notifyDataSetChanged();
                if (co == 0) {
                    searchf1_item2.setVisibility(View.VISIBLE);
                    searchf1_item2_tv3.setText(" "+userConcemFansArray.length()+" ");
                }
                user_viewp.setVisibility(View.VISIBLE);
            } else {
                searchf1_item2.setVisibility(View.GONE);
                user_viewp.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            searchf1_item2.setVisibility(View.GONE);
        }
    }

    /* 解析音频页面 */
    private void yp(JSONObject obj, int co) {
        try {
            JSONArray myAudioArray = obj.getJSONArray("myAudioList");// 音频
            audioList = new ArrayList<AudioList>();
            if (myAudioArray.length() != 0) {
                for (int i = 0; i < myAudioArray.length(); i++) {
                    JSONObject myAudioList = myAudioArray.getJSONObject(i);
                    JSONObject myaudio = myAudioList.getJSONObject("audio");
                    AudioList audio = new AudioList();
                    audio.setId(myaudio.getString("id"));
                    audio.setAudioAlbumId(myaudio.getString("audioAlbumId"));
                    audio.setAddTime(myaudio.getString("addTime"));
                    audio.setAudioName(myaudio.getString("audioName"));
                    audio.setCover(myaudio.getString("cover"));
                    audio.setCommentNumber(myaudio.getString("commentNumber"));
                    audio.setPlayAmount(myaudio.getString("playAmount"));
                    audio.setTimeLong(myaudio.getString("timeLong"));
                    audio.setPath(myaudio.getString("path"));
                    audio.setBlurb(myaudio.getString("blurb"));
                    audioList.add(audio);
                }
                mSearchF4Adapter = new SearchF4Adapter(context, audioList);
                audio_viewp.setAdapter(mSearchF4Adapter);
                mSearchF4Adapter.notifyDataSetChanged();
                if (co == 0) {
                    searchf1_item3.setVisibility(View.VISIBLE);
                    searchf1_item3_tv3.setText(" "+myAudioArray.length()+" ");
                }
                audio_viewp.setVisibility(View.VISIBLE);
            } else {
                searchf1_item3.setVisibility(View.GONE);
                audio_viewp.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            searchf1_item3.setVisibility(View.GONE);
        }
    }

    /* 解析视频页面 */
    private void sp(JSONObject obj, int co) {
        try {
            JSONArray myVideoArray = obj.getJSONArray("myVideoList");// 视频
            videoList=new ArrayList<>();
            if (myVideoArray.length() != 0) {
                for (int i = 0; i < myVideoArray.length(); i++) {
                    JSONObject myAudioList = myVideoArray.getJSONObject(i);
                    Album myAlbum = new Album();
                    myAudioList.getString("nickname");
                    myAudioList.getString("uid");
                    JSONObject myAudio = myAudioList.getJSONObject("video");

                    myAlbum.setId(myAudio.getString("id"));
                    myAlbum.setAlbumName(myAudio.getString("videoName"));
                    myAlbum.setBlurb(myAudio.getString("blurb"));
                    myAlbum.setCover(myAudio.getString("cover"));
                    myAlbum.setPlayAmount(myAudio.getString("playAmount"));
                    videoList.add(myAlbum);

                }
                mSearchF5Adapter = new SearchF5Adapter(context, videoList);
                video_viewp.setAdapter(mSearchF5Adapter);
                mSearchF5Adapter.notifyDataSetChanged();
                if (co == 0) {
                    searchf1_item4.setVisibility(View.VISIBLE);
                    searchf1_item4_tv3.setText(" "+myVideoArray.length()+" ");
                }
                video_viewp.setVisibility(View.VISIBLE);
            } else {
                searchf1_item4.setVisibility(View.GONE);
                video_viewp.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            searchf1_item4.setVisibility(View.GONE);
        }
    }
}
