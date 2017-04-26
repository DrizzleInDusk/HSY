package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.TjAlbumAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 专辑播放详情页  更多推荐 */
public class MoreTjActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;

                try {
                    albumList=new ArrayList<>();
                    JSONArray audioList = obj.getJSONArray("audioAlbumList");
                    for (int i = 0; i < audioList.length(); i++) {
                        Album maudio = new Album();
                        JSONObject data = audioList.getJSONObject(i);
                        maudio.setId(data.getString("id"));// 专辑ID
                        maudio.setCover(data.getString("cover"));// 专辑-封面图
                        maudio.setAlbumName(data.getString("albumName"));// 专辑名
                        maudio.setBlurb(data.getString("blurb"));// 专辑-简介
                        maudio.setPlayAmount(data.getString("playAmount"));// 专辑——播放数量
                        maudio.setEpisode(data.getString("episode"));// 专辑-多少集
                        maudio.setLabel(data.getString("label"));// 专辑标签
                        maudio.setUserId(data.getString("userId"));// 专辑UserId
                        maudio.setAlbumTypeId(data.getString("albumTypeId"));// 专辑TypeId
                        maudio.setStatus(data.getString("status"));// 专辑-状态1正常上线
                        maudio.setAddTime(data.getString("addTime"));// 专辑添加时间
                        albumList.add(maudio);
                    }
                    tadapter = new TjAlbumAdapter(context, albumList);
                    more_tuijian.setAdapter(tadapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }

    };
    private ArrayList<Album> albumList;
    private String userid, audioid,tag;
    private Context context;
    private TjAlbumAdapter tadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moretj);
        context = this;
        Intent i = this.getIntent();
        audioid = i.getStringExtra("id");
        tag = i.getStringExtra("tag");
        userid = Futil.getValue(MoreTjActivity.this, "userid");
        findview();
    }

    @Override
    protected void onResume() {
        getmore();
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

    private ImageView back_top;
    private ListView more_tuijian;

    private void findview() {
        more_tuijian = (ListView) findViewById(R.id.more_tuijian);
        more_tuijian.setFocusable(false);

        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

    }

    private void getmore() {
        startProgressDialog();
        HashMap<String, String> map = new HashMap<String, String>();
        String strUrl ;
        if(tag.equals("2")){
           strUrl = URLManager.MoreTJ;
            map.put("audio.id", audioid);
        }else {
            strUrl = URLManager.MoreTJ2;
            map.put("audioAlbum.id", audioid);
        }
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
