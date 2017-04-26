package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/*** 电台播放页 */
public class RadioStationPlayerActivity extends Activity implements
        OnClickListener {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
            }
        }
    };
    private String userid, mmsh, stationname, stationid;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
//            return;
        setContentView(R.layout.activity_radiostationplayer);
        context = this;
        Intent i = this.getIntent();
        stationname = i.getStringExtra("stationname");
        mmsh = i.getStringExtra("mmsh");
        stationid = i.getStringExtra("id");
        userid = Futil.getValue(RadioStationPlayerActivity.this, "userid");
        findview();
    }

    @Override
    protected void onResume() {
        upHits();
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
        super.onResume();
        String apppath = App.getPlayerpath();
        int pcode = App.getPlaycode();

        if (mmsh.equals(apppath)) {
            if (pcode == 1) {

                station_playbt.setSelected(App.getPlaycode() == -1);
            } else if (pcode == 0) {
                station_playbt.setSelected(!(App.getPlaycode() == -1));
            } else {
                station_playbt.setSelected(!(App.getPlaycode() == -1));
            }
        } else {
            station_playbt.setSelected(false);
        }
    }

    private void upHits() {
        String strUrl = URLManager.upRadioHits;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("radioId", stationid);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewHolder.removeAllViews();
    }

    private App mapplication;
    String playerpath1;
    private int playcode;
    private int isstation;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.station_playbt:
                MainActivity.mVideoSource = mmsh;
                int pcode = mapplication.getPlaycode();
                String apppath = mapplication.getPlayerpath();
                System.out.println("mapplication.getPlayerpath()--" + apppath);
                System.out.println("mmsh--" + mmsh);
                if (!mmsh.equals(apppath)) {// 是否在播放---判断url是否相同

                    station_playbt.setSelected(true);
                    // 如果已经开发播放，先停止播放
                    if (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE) {
                        MainActivity.mVV.stopPlayback();
                    }
                    /**
                     * 发起一次新的播放任务
                     */
                    if (MainActivity.mEventHandler.hasMessages(MainActivity.UI_EVENT_PLAY)) {
                        MainActivity.mEventHandler.removeMessages(MainActivity.UI_EVENT_PLAY);
                    }
                    MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);
                    playerpath1 = mmsh;
                    mapplication.setPlayerpath(playerpath1);
                    ArrayList<String> sKey = Futil.loadKeyArray(context, "5");
                    String key = stationname;
                    sKey.remove(key);
                    sKey.add(key);
                    Futil.saveKeyArray(context, sKey, "5");
                    Futil.saveValue(context, key + "name", stationname);
                    Futil.saveValue(context, key + "mmsh", mmsh);
                    Futil.saveValue(context, key + "id", stationid);
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    final String tim = sdf.format(date);
                    Futil.saveValue(context, key + "addTime", tim);

                    playcode = 0;
                    isstation = 1;
                    mapplication.setPlaycode(playcode);
                    mapplication.setIsstation(isstation);
                    mapplication.setStationname(stationname);
                    mapplication.setStationid(stationid);

                } else {

                    if (pcode == 1) {// Playcode 0=在播放 1暂停
                        // if (!playpage_playbt.isSelected()) {// 没选中=播放键
                        System.out.println("播放--" + pcode);
                        // 发起一次播放任务,当然您不一定要在这发起
                        if (!MainActivity.mVV.isPlaying() && (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
                            MainActivity.mVV.resume();
                        } else {
                            MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);//UI 事件  播放
                        }
                        playcode = 0;
                        station_playbt.setSelected(true);

                    } else {
                        System.out.println("暂停--" + pcode);
                        MainActivity.mVV.pause();
                        playcode = 1;
                        station_playbt.setSelected(false);
                    }
                    mapplication.setPlaycode(playcode);

                }
                break;
            case R.id.back_top:
                finish();
                break;

        }
    }

    private ImageView back_top, station_playbt;
    private TextView top_tv, station_name;
    private RelativeLayout mViewHolder = null;
    private void findview() {

        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        /**
         *创建BVideoView和BMediaController
         */
        mViewHolder.addView(MainActivity.mVV);
        /**
         * 设置解码模式
         */
        MainActivity.mVV.setDecodeMode(BVideoView.DECODE_SW);
        MainActivity.mVV.selectResolutionType(BVideoView.RESOLUTION_TYPE_AUTO);

        top_tv = (TextView) findViewById(R.id.staiton_top_tv);
        top_tv.setText(stationname);
        station_name = (TextView) findViewById(R.id.station_name);
        station_name.setText(stationname);

        station_playbt = (ImageView) findViewById(R.id.station_playbt);
        station_playbt.setOnClickListener(this);
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

    }

}
