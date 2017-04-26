package com.zm.hsy.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BMediaController;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

public class VideoPlayActivity extends Activity implements OnClickListener {

    AlertDialog dialog;
    private ListView mtglistView, mtjlistView, mclistView;
    private String userid;
    private String videoid;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("actname", "VideoPlayActivity: ");
        context = this;
        setContentView(R.layout.video_playpage);
        userid = Futil.getValue(context, "userid");

        Intent i = this.getIntent();
        videoid = i.getStringExtra("videoid");
        findview();
    }

    @Override
    protected void onResume() {
        gointo();
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

    String path = "";
    private int playcode;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.playpage_playbt:

                MainActivity.mVideoSource = path;
//                MainActivity.mVideoSource = "http://192.168.1.79:8080/hsyfm/fmVideo/20160615100846824907548.3gp";
                String apppath = App.getPlayerpath();
                if (!path.equals(apppath)) {
                    App.setPlayerpath(path);
//                    App.setPlayerpath("http://192.168.1.79:8080/hsyfm/fmVideo/20160615100846824907548.3gp");
                    playpage_playbt.setSelected(true);
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

                } else {
                    int pcode = App.getPlaycode();
                    if (pcode == 0) {// Playcode 0=在播放 1暂停
                        playcode = 1;
                        playpage_playbt.setSelected(false);
                        MainActivity.mVV.pause();
                    } else {
                        playcode = 0;
                        playpage_playbt.setSelected(true);
                        // 发起一次播放任务,当然您不一定要在这发起
                        if (!MainActivity.mVV.isPlaying() && (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
                            MainActivity.mVV.resume();
                        } else {
                            MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);//UI 事件  播放
                        }
                    }
                }
                App.setPlaycode(playcode);
                break;

            case R.id.playpage_audiomessage:
                break;
            case R.id.details_playpage_downiv1:
                finish();
                break;
            case R.id.playpage_liebiao:
                break;
            case R.id.playpage_item_dingyue:
//                String dy = dingyue.getText().toString();
//                if (dy.equals("订阅")) {
//                    Futil.showMessage(context, "点击了订阅");
//                }

                break;
        }
    }

    private ImageView down,  audiomessage;
    private TextView details_tv1, details_tv3, dianping_commentNumber;
    private RelativeLayout dianping_more;
    private ImageView playpage_userhead, playpager_Albumcover;
    private TextView playpage_username, playpage_Albumname,
            playpage_Albumblurb, playpage_playAmount, playpager_Albumepisode,
            playpage_audioblurb;
    private ImageView playpage_audio_cover;
    private Button  dingyue;
    private EditText pinglun_et;
    private ImageView playpage_playbt;

    private BMediaController mVVCtl = null;
    private RelativeLayout mViewHolder = null;
    private LinearLayout mControllerHolder = null;
    private void findview() {
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        /**
         *创建BVideoView和BMediaController
         */
        mViewHolder.addView(MainActivity.mVV);

        // 播放键
        playpage_playbt = (ImageView) findViewById(R.id.playpage_playbt);
        playpage_playbt.setOnClickListener(this);

//        // ListView
        mtglistView = (ListView) findViewById(R.id.video_playpage_tuiguang);
        mtglistView.setFocusable(false);

        dingyue = (Button) findViewById(R.id.playpage_item_dingyue);
        dingyue.setOnClickListener(this);
        audiomessage = (ImageView) findViewById(R.id.playpage_audiomessage);
        audiomessage.setOnClickListener(this);

        down = (ImageView) findViewById(R.id.details_playpage_downiv1);
        down.setOnClickListener(this);

        details_tv1 = (TextView) findViewById(R.id.details_playpage_tv1);
        details_tv3 = (TextView) findViewById(R.id.details_playpage_tv3);

        dianping_commentNumber = (TextView) findViewById(R.id.details_dianping_commentNumber);

        playpage_userhead = (ImageView) findViewById(R.id.playpage_userhead);
        playpage_username = (TextView) findViewById(R.id.playpage_username);

        playpager_Albumcover = (ImageView) findViewById(R.id.playpager_Albumcover);
        playpage_Albumname = (TextView) findViewById(R.id.playpage_Albumname);
        playpage_playAmount = (TextView) findViewById(R.id.playpage_playAmount);
        playpage_Albumblurb = (TextView) findViewById(R.id.playpage_Albumblurb);
        playpager_Albumepisode = (TextView) findViewById(R.id.playpager_Albumepisode);

        playpage_audioblurb = (TextView) findViewById(R.id.playpage_audioblurb);
        playpage_audio_cover = (ImageView) findViewById(R.id.playpage_video_cover);

    }

    String cover = null;

    private void gointo() {
        startProgressDialog();
        String strUrl = URLManager.GetVideoInfo;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("video.id", videoid);// 专辑id
        Futil.xutils(strUrl, map, handler, URLManager.one);

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    JSONObject videoAlbum = object.getJSONObject("videoAlbum");
                    JSONObject user = object.getJSONObject("user");
                    JSONObject video = object.getJSONObject("video");
                    String type = "";
                    try {
                        type = object.getString("headStatus");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String head = user.getString("head");
                    if (!type.equals("http")) {
                        head = URLManager.Head_URL + head;
                    }
                    playpage_username.setText(user.getString("nickname"));
                    Picasso.with(context).load(head).error(R.mipmap.playpage_iv1).into(playpage_userhead);

                    String cover = videoAlbum.getString("cover");
                    cover = URLManager.COVER_URL + cover;
                    Picasso.with(context).load(cover).error(R.mipmap.letter_item_img1).into(playpager_Albumcover);
                    Picasso.with(context).load(cover).error(R.mipmap.letter_item_img1).into(playpage_audio_cover);
                    playpage_Albumname.setText(videoAlbum.getString("albumName"));
                    playpage_Albumblurb.setText(videoAlbum.getString("blurb"));
                    playpage_playAmount.setText(videoAlbum.getString("playAmount"));
                    playpager_Albumepisode.setText(videoAlbum.getString("episode"));

                    playpage_audioblurb.setText(video.getString("blurb"));
                    details_tv1.setText(video.getString("videoName"));
                    details_tv3.setText(video.getString("playAmount"));
                    path = URLManager.Video_URL + video.getString("path");
                    System.out.println("path>>>>>>" + path);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.two) {
                JSONObject object = (JSONObject) msg.obj;

            }
            stopProgressDialog();
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewHolder.removeAllViews();
        App.setIsstation(-1);
        App.setPlayerpath(null);
        App.setPlaycode(-1);
        if ((MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
            MainActivity.mLastPos = (int) MainActivity.mVV.getCurrentPosition();
            /*停止播放*/
            MainActivity.mVV.stopPlayback();
        }
    }

    /**
     * 等待页
     */
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

}
