package com.zm.hsy.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.adapter.TabVF4ListAdapter;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TabVF4 extends Fragment implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
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
                    f4adapter = new TabVF4ListAdapter(getActivity(), audioList,
                            "rad");
                    rank_viewp.setAdapter(f4adapter);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.two) {
                JSONObject obj = (JSONObject) msg.obj;
                try {//tuijian_name1,tuijian_name2,tuijian_name3
                    JSONArray jsonArray = obj.getJSONArray("radioRankList");
                    try {
                        JSONObject radioRank = jsonArray.getJSONObject(0);
                        tuijian_ll1.setVisibility(View.VISIBLE);
                        address1 = radioRank.getString("address");
                        fmid1 = radioRank.getString("id");
                        name1 = radioRank.getString("name");
                        province1 = radioRank.getString("province");
                        if(!address1.equals("")){
                            tuijian_name1.setText(name1);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        tuijian_ll1.setVisibility(View.INVISIBLE);
                    }
                    try {
                        tuijian_ll2.setVisibility(View.VISIBLE);
                        JSONObject radioRank1 = jsonArray.getJSONObject(1);
                        address2 = radioRank1.getString("address");
                        fmid2 = radioRank1.getString("id");
                        name2 = radioRank1.getString("name");
                        province2 = radioRank1.getString("province");
                        if(!address2.equals("")){
                            tuijian_name2.setText(name2);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        tuijian_ll2.setVisibility(View.INVISIBLE);
                    }
                    try {
                        tuijian_ll3.setVisibility(View.VISIBLE);
                        JSONObject radioRank2 = jsonArray.getJSONObject(2);
                        address3 = radioRank2.getString("address");
                        fmid3 = radioRank2.getString("id");
                        name3= radioRank2.getString("name");
                        province3 = radioRank2.getString("province");
                        if(!address3.equals("")){
                            tuijian_name3.setText(name3);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        tuijian_ll3.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }
    };

    // 省份
    private static String province;
    private String address1 = "", address2 = "", address3 = "", name1 = "", name2 = "", name3 = "", fmid1="",fmid2="",fmid3="",province1 = "", province2 = "", province3 = "";

    private ArrayList<AudioList> audioList;
    private ArrayList<AudioList> audioList1;
    private TabVF4ListAdapter f4adapter;
    private TabVF4ListAdapter f4hisadapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_pager_guangbo, container,
                false);
        context = getActivity();
        province = Futil.getValue(context, "province");//获取当前定位到的省份
        initView(view);

        gointo();
        radioRecommendList();
        gethistory();
        return view;
    }

    private LinearLayout ll_bendi, ll_shengshi,tuijian_ll1,tuijian_ll2,tuijian_ll3;
    private LinearLayout ll_guojia,ll_wangluo;
    private ListView rank_viewp, his_viewp;
    private TextView guangbo_his_tv;
    private TextView tuijian_name1, tuijian_name2, tuijian_name3;

    private void initView(View v) {
        tuijian_name1 = (TextView) v.findViewById(R.id.tuijian_name1);
        tuijian_ll1 = (LinearLayout) v.findViewById(R.id.tuijian_ll1);
        tuijian_ll1.setOnClickListener(this);
        tuijian_name2 = (TextView) v.findViewById(R.id.tuijian_name2);
        tuijian_ll2 = (LinearLayout) v.findViewById(R.id.tuijian_ll2);
        tuijian_ll2.setOnClickListener(this);
        tuijian_name3 = (TextView) v.findViewById(R.id.tuijian_name3);
        tuijian_ll3 = (LinearLayout) v.findViewById(R.id.tuijian_ll3);
        tuijian_ll3.setOnClickListener(this);
        guangbo_his_tv = (TextView) v.findViewById(R.id.guangbo_his_tv);
        rank_viewp = (ListView) v.findViewById(R.id.guangbo_rank_viewp);
        rank_viewp.setFocusable(false);
        his_viewp = (ListView) v.findViewById(R.id.guangbo_his_viewp);
        his_viewp.setFocusable(false);
        ll_shengshi = (LinearLayout) v.findViewById(R.id.ll_shengshi);
        ll_shengshi.setOnClickListener(this);
        ll_bendi = (LinearLayout) v.findViewById(R.id.ll_bendi);
        ll_bendi.setOnClickListener(this);
        ll_guojia = (LinearLayout) v.findViewById(R.id.ll_guojia);
        ll_guojia.setOnClickListener(this);
        ll_wangluo = (LinearLayout) v.findViewById(R.id.ll_wangluo);
        ll_wangluo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shengshi://省市台
                ActivityJumpControl.getInstance(getActivity())
                        .gotoRadioStationActivity("3", province);
                break;
            case R.id.ll_bendi://本地台
                Log.i("pro",  province+"++++++++");
                if (province == null || (province.trim()).equals("")) {
                    ActivityJumpControl.getInstance(getActivity())
                            .gotoRadioStationActivity("1", "");
                }else {
                    ActivityJumpControl.getInstance(getActivity())
                            .gotoRadioStationActivity("1", province);
                }
                break;
            case R.id.ll_guojia://国家台
                if (province != null) {
                    ActivityJumpControl.getInstance(getActivity())
                            .gotoRadioStationActivity("2", province);
                }
                break;
            case R.id.ll_wangluo://网络台
                if (province != null) {
                    ActivityJumpControl.getInstance(getActivity())
                            .gotoRadioStationActivity("4", province);
                }
                break;
            case R.id.tuijian_ll1:
                if (!address1.equals("") ){
                    ActivityJumpControl.getInstance((Activity)context)
                            .gotoRadioStationPlayerActivity(address1, name1,fmid1);
                }
                break;
            case R.id.tuijian_ll2:
                if (!address2.equals("")) {
                    ActivityJumpControl.getInstance((Activity)context)
                            .gotoRadioStationPlayerActivity(address2, name2,fmid2);
                }
                break;
            case R.id.tuijian_ll3:
                if (!address3.equals("")) {
                    ActivityJumpControl.getInstance((Activity)context)
                            .gotoRadioStationPlayerActivity(address3, name3,fmid3);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 进入时调用-
     */
    private void gointo() {
        startProgressDialog();
        String strUrl = URLManager.radioRankList;
        HashMap<String, String> map = new HashMap<String, String>();
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    /**
     * 进入时调用-
     */
    private void radioRecommendList() {
        startProgressDialog();
        String strUrl = URLManager.radioRecommendList;
        HashMap<String, String> map = new HashMap<String, String>();
        Futil.xutils(strUrl, map, handler, URLManager.two);
    }

    private void gethistory() {
        ArrayList<String> sKey = Futil.loadKeyArray(context, "5");
        System.out.println("history----" + sKey.size());
        audioList1 = new ArrayList<AudioList>();
        for (int i = 0; i < sKey.size(); i++) {
            AudioList a = new AudioList();
            String key = sKey.get(i);
            String audioName = (String) Futil.getValue(context, key + "name");
            String mmsh = (String) Futil.getValue(context, key + "mmsh");
            String stationid = (String) Futil.getValue(context, key + "id");
            String addTime = (String) Futil.getValue(context, key + "addTime");
            System.out.println("key---" + key);
            a.setKey(key);
            a.setPath(mmsh);
            a.setId(stationid);
            a.setAddTime(addTime);
            a.setAudioName(audioName);
            audioList1.add(a);
        }
        if (audioList1.size() > 0) {
            guangbo_his_tv.setVisibility(View.GONE);
            his_viewp.setVisibility(View.VISIBLE);
            System.out.println("audioList1.size()---" + audioList1.size());
            f4hisadapter = new TabVF4ListAdapter(context, audioList1, "his");
            his_viewp.setAdapter(f4hisadapter);
        } else {
            guangbo_his_tv.setVisibility(View.VISIBLE);
            his_viewp.setVisibility(View.GONE);
        }

    }

    private App mapplication;
    private String playerpath1;
    private String playerpath;
    private int playcode;

    private void setguangboradiolistener() {
        rank_viewp.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                LinearLayout guangbo_radioll = (LinearLayout) v
                        .findViewById(R.id.guangbo_radioll);
                final String audioName = audioList.get(position).getAudioName();// 音频名称
                String playAmount = audioList.get(position).getPlayAmount();// 播放数量
                final String path = audioList.get(position).getPath();// 音频路径
                playerpath = path;
                guangbo_radioll.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pcode = mapplication.getPlaycode();
                        System.out.println("pcode--" + pcode);
                        String apppath = mapplication.getPlayerpath();
                        System.out.println("apppath--" + apppath);
                        System.out.println("playerpath--" + playerpath);
                        if (!playerpath.equals(apppath)) {// 是否在播放---判断url是否相同

                            //	MainActivity.player.playUrl(playerpath);
                            playerpath1 = playerpath;
                            mapplication.setPlayerpath(playerpath1);
                            ArrayList<String> sKey = Futil.loadKeyArray(
                                    context, "5");
                            String key = audioName;
                            sKey.remove(key);
                            sKey.add(key);
                            Futil.saveKeyArray(context, sKey, "5");
                            Futil.saveValue(context, key + "name", audioName);
                            Futil.saveValue(context, key + "mmsh", playerpath);
                            Date date = new Date(System.currentTimeMillis());
                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm:ss");
                            final String tim = sdf.format(date);
                            Futil.saveValue(context, key + "addTime", tim);

                            //	MainActivity.player.play();
                            playcode = 0;
                            mapplication.setPlaycode(playcode);
                            mapplication.setIsstation(1);
                            mapplication.setStationname(audioName);
                            v.setSelected(true);

                        } else {

                            if (pcode == 1) {// Playcode 0=在播放
                                // 1暂停
                                System.out.println("播放--" + pcode);
                                //MainActivity.player.play();
                                playcode = 0;
                                v.setSelected(true);

                            } else {
                                System.out.println("暂停--" + pcode);
                                //MainActivity.player.pause();
                                playcode = 1;
                                v.setSelected(false);
                            }
                            mapplication.setPlaycode(playcode);

                        }
                    }
                });
            }
        });

    }

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
