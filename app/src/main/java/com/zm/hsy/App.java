package com.zm.hsy;


import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.zm.hsy.activity.MainActivity;
import com.zm.hsy.entity.AudioList;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;


/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-7-23
 * Copyright @ 2013 BU
 * Description: 全局application
 *
 * History:
 */
public class App extends Application {
	public static CountDownTimer timer;
	private static Context context;
	public static String score;
	public static String vStatus;
	private static AudioList contextAudioList;// 音乐audio信息
	private static int playcode = -1;// Playcode 0=已播放 1已暂停
	private static String playerpath="";// 播放路径
	private static int isstation=-1;// 是否播放过 0音乐  1电台
	private static String stationname;//电台名字
	private static String stationid;//电台id

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		x.Ext.init(this);
		JPushInterface.init(this);
	}
	public static Context getContext() {
		return context;
	}

	public static String getStationname() {
		return stationname;
	}

	public static void setStationname(String stationname) {
		App.stationname = stationname;
	}


	public static AudioList getContextAudioList() {
		return contextAudioList;
	}

	public static void setContextAudioList(AudioList contextAudioList) {

		App.contextAudioList = contextAudioList;
	}
	public static String getStationid() {
		return stationid;
	}

	public static void setStationid(String stationid) {
		App.stationid = stationid;
	}
	public static int getPlaycode() {
		return playcode;
	}

	public static void setPlaycode(int playcode) {
		App.playcode = playcode;
	}

	public static String getPlayerpath() {
		return playerpath;
	}

	public static void setPlayerpath(String setPlayerpath) {
		App.playerpath = setPlayerpath;
		System.out.println("App-setPlayerpath---"+setPlayerpath);
	}
	public static int getIsstation() {
		return isstation;
	}

	public static void setIsstation(int isstation) {
		Log.i("bigbtn", "执行set"+isstation);
		App.isstation = isstation;
	}

	public static String remnanttime;// 剩余
	public static String timecode = "0";// 倒计时状态1开始0结束
	public static CountDownTimer countdown;
	
	private static int fen = 0;
	private static int miao = 0;
	private static TextView ctv;
	public static void startTime(long timelong,final Handler handler, final int ms) {
		if (countdown != null) {
			if (!timecode.equals("0")) {
				countdown.cancel();
			}
		}
		
		countdown = new CountDownTimer(timelong, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				fen = (int) (millisUntilFinished / 1000 / 60);
				miao = (int) (millisUntilFinished / 1000 % 60);
				String s="倒计时";
				if (fen < 10) {
					s += ":0" + fen;
				} else {
					s += ":" + fen;
				}
				if (miao < 10) {
					s += ":0" + miao;
				} else {
					s += ":" + miao;
				}
				Message msg1 = new Message();
				msg1.obj = s;
				msg1.what = ms;
				handler.sendMessage(msg1);
			}

			@Override
			public void onFinish() {
				// 如果已经开发播放，先停止播放
				if (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE) {
					MainActivity.mVV.stopPlayback();
				}
				App.setIsstation(-1);
				App.setPlayerpath(null);
				App.setPlaycode(-1);
				Message msg1 = new Message();
				msg1.obj = "";
				msg1.what = 0;
				handler.sendMessage(msg1);
			}
		};
		countdown.start();
		timecode = "1";
	}

	public static void stopTime() {
		if (!timecode.equals("0")) {
			countdown.cancel();
		}
	}

	
}
