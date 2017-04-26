package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.util.ActivityJumpControl;

import java.io.File;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
/** 播放本地视频窗口 */
public class VideoviewActivity extends Activity implements OnClickListener {

	private String userid, path,addtime,Tag;
	private Context context;
	private MediaPlayer player;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videoview);
		Log.i("actname", "VideoviewActivity");
		context=this;
		Intent i = this.getIntent();
		userid = Futil.getValue(context, "userid");
		path = i.getStringExtra("path");
		addtime = i.getStringExtra("addtime");
		Tag = i.getStringExtra("Tag");
		File file =new File(path);
		if(!file.exists()){
			Futil.showDialog(context, "视频文件不存在!", "", null, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}else{

		}
		MainActivity.mVideoSource = path;
		findview();
	}

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

	protected void onRestart() {

		super.onRestart();
	}
	@Override
	protected void onResume() {

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
		case R.id.videoview_rl1:
			ActivityJumpControl.getInstance(this)
					.gotoPublishRecordActivity(addtime, Tag);
			finish();
			break;
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

	private ImageView back_top,previre_play;
	private RelativeLayout videoview_rl1;
	private RelativeLayout mViewHolder = null;
	private SurfaceView surfaceView;
	private void findview() {

		videoview_rl1 = (RelativeLayout) findViewById(R.id.videoview_rl1);
		videoview_rl1.setOnClickListener(this);//发布
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);//返回
		previre_play = (ImageView) findViewById(R.id.previre_play);
		previre_play.setOnClickListener(this);

		mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
		/**
		 *创建BVideoView和BMediaController
		 */
		mViewHolder.addView(MainActivity.mVV);//把BVideoView添加进这个相对布局
		mViewHolder.setOnClickListener(this);
		App.setPlayerpath(path);
//                    App.setPlayerpath("http://192.168.1.79:8080/hsyfm/fmVideo/20160615100846824907548.3gp");

		// 如果已经开发播放，先停止播放
		if (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE) {
			MainActivity.mVV.stopPlayback();
		}
//		/**
//		 * 发起一次新的播放任务
//		 */
//		if (MainActivity.mEventHandler.hasMessages(MainActivity.UI_EVENT_PLAY)) {
//			MainActivity.mEventHandler.removeMessages(MainActivity.UI_EVENT_PLAY);
//		}
//		MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);
		surfaceView= (SurfaceView) findViewById(R.id.act_vidoeview_surface);
		SurfaceHolder holder=surfaceView.getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				player=new MediaPlayer();
				player.setDisplay(holder);
				myplayer();
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}
		});

		playcode = 0;
	}
	private int playcode;
	private void startplayer() {
		String apppath = App.getPlayerpath();
		if (apppath != path) {
			App.setPlayerpath(path);
			MainActivity.mVideoSource = path;
			// 如果已经开发播放，先停止播放
//			if (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE) {
//				MainActivity.mVV.stopPlayback();
//			}
			/**
			 * 发起一次新的播放任务
			 */
//			if (MainActivity.mEventHandler.hasMessages(MainActivity.UI_EVENT_PLAY)) {
//				MainActivity.mEventHandler.removeMessages(MainActivity.UI_EVENT_PLAY);
//			}
//			MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);
			myplayer();
			playcode = 0;
		} else {
			if (playcode == 0) {// Playcode 0=在播放 1暂停
				playcode = 1;
//				MainActivity.mVV.pause();
				player.pause();
			} else {
				playcode = 0;
				// 发起一次播放任务,当然您不一定要在这发起
//				if (!MainActivity.mVV.isPlaying() && (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
//					MainActivity.mVV.resume();
//				} else {
//					MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);//UI 事件  播放
//				}
				if (player.isPlaying()){
					myplayer();
				}
			}
		}

	}


	private void myplayer(){
		try {
			mViewHolder.setVisibility(View.GONE);
			surfaceView.setVisibility(View.VISIBLE);
			player.reset();
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setDataSource(path);
			player.setLooping(true);//设置循环
			player.prepare();
			player.seekTo(0);
			player.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
