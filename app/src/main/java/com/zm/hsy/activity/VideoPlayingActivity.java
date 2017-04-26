package com.zm.hsy.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.baidu.cyberplayer.core.BVideoView;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

import static com.zm.hsy.activity.MainActivity.mLastPos;
import static com.zm.hsy.activity.MainActivity.mPlayerStatus;
import static com.zm.hsy.activity.MainActivity.mVV;
import static com.zm.hsy.activity.MainActivity.mVideoSource;

/** 视频播放详情页 */
public class VideoPlayingActivity extends Activity implements View.OnClickListener {
    private String userid;
    private String videoid;
    private Context context;
    private static VideoPlayingActivity activity;
    private MediaPlayer mPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("actname", "VideoPlayingActivity: ");
        context = this;
        activity = this;
        //隐藏标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_videoplaying);
        //隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userid = Futil.getValue(context, "userid");
        Intent i = this.getIntent();
        videoid = i.getStringExtra("videoid");
        findview();
        gointo();
    }

    public static boolean vplayonact = false;
    @Override
    protected void onResume() {
        vplayonact = true;
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        vplayonact = false;
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        if (mVV.isPlaying() && (mPlayerStatus !=MainActivity. PLAYER_STATUS.PLAYER_IDLE)) {
            mLastPos = (int) mVV.getCurrentPosition();//当前位置
//when scree lock,paus is good select than stop
            // don't stop pause
            // mVV.stopPlayback();
            mVV.pause();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.previre_play:
                startplayer();
                break;
            case R.id.view_holder:
                startplayer();
                break;
        }
    }
    private String path = "";
    private RelativeLayout mViewHolder = null;
    private SurfaceView surfaceView;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void findview() {
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);

        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(-1, -1);
        rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        rllp.setLayoutDirection(View.ROTATION.);
        mViewHolder.addView(mVV, rllp);
//        mViewHolder.addView(mVV);//将播放器放进去

        mViewHolder.setOnClickListener(this);

//        surfaceView= (SurfaceView) findViewById(R.id.test_surface);
//        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        /**
         * 设置解码模式
         */
        mVV.setDecodeMode(BVideoView.DECODE_SW);
        mVV.selectResolutionType(BVideoView.RESOLUTION_TYPE_AUTO);

    }



    String cover = null;
    private void gointo() {
        String strUrl = URLManager.GetVideoInfo;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("video.id", videoid);// 专辑id
        Futil.xutils(strUrl, map, handler, URLManager.one);

    }
    private int playcode;
    private void startplayer() {
        String apppath = App.getPlayerpath();
        if (apppath != path) {
            App.setPlayerpath(path);
            mVideoSource = path;
            // 如果已经开发播放，先停止播放
            if (mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE) {
                mVV.stopPlayback();
            }
            /**
             * 发起一次新的播放任务
             */
            if (MainActivity.mEventHandler.hasMessages(MainActivity.UI_EVENT_PLAY)) {
                MainActivity.mEventHandler.removeMessages(MainActivity.UI_EVENT_PLAY);
            }
            MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);

            playcode = 0;
        } else {
            if (playcode == 0) {// Playcode 0=在播放 1暂停
                playcode = 1;
                mVV.pause();
            } else {
                playcode = 0;
                // 发起一次播放任务,当然您不一定要在这发起
                if (!mVV.isPlaying() && (mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
                    mVV.resume();
                } else {
                    MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);//UI 事件  播放
                }
            }
        }

    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    JSONObject video = object.getJSONObject("video");
                    path = URLManager.Video_URL + video.getString("path");
                    mVideoSource = path;
                    System.out.println("path>>"+path);
                    App.setPlayerpath(path);

                    // 如果已经开发播放，先停止播放
                    if (mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE) {
                        mVV.stopPlayback();
                    }
                    /**
                     * 发起一次新的播放任务
                     */
//                    if (MainActivity.mEventHandler.hasMessages(MainActivity.UI_EVENT_PLAY)) {
//                        MainActivity.mEventHandler.removeMessages(MainActivity.UI_EVENT_PLAY);
//                    }
//                    MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);
//
                    //用原生方式，但有个缺点，就是加载视频非常慢，因为是网络获取的资源
//                    mPlayer = new MediaPlayer();
//                    mPlayer.reset();
//                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    // 设置需要播放的视频
//                    try {
//                        mPlayer.setDataSource(mVideoSource);
//                        mPlayer.setDisplay(surfaceView.getHolder()); // 定义一个SurfaceView播放它
//                        mPlayer.setLooping(true);
//                        mPlayer.prepare();
//                        mPlayer.seekTo(0);
//                        mPlayer.start();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

//                    surfaceView.setVisibility(View.GONE);
                    mVV.setVideoPath(mVideoSource);
//                    SurfaceHolder surfaceHolder= surfaceView.getHolder();
//                    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    mVV.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
//                    mVV.surfaceCreated(surfaceHolder);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mVV.showCacheInfo(true);
                    mVV.start();
//                    mVV.setRotation(45f);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    public static Handler muhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 10086) {
                activity.finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewHolder.removeAllViews();
        App.setIsstation(-1);
        App.setPlayerpath(null);
        App.setPlaycode(-1);
        if ((mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
            mLastPos = (int) mVV.getCurrentPosition();
            /*停止播放*/
            mVV.stopPlayback();
        }
    }

}
