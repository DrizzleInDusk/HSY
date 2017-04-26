package com.zm.hsy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.czt.mp3recorder.MP3Recorder;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.tools.ringdroid.MarkerView;
import com.zm.hsy.tools.ringdroid.SeekTest;
import com.zm.hsy.tools.ringdroid.SongMetadataReader;
import com.zm.hsy.tools.ringdroid.WaveformView;
import com.zm.hsy.tools.ringdroid.soundfile.CheapSoundFile;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CaoZuoMp3Utils;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 录音 */
public class RecordActivity extends Activity implements OnClickListener, MarkerView.MarkerListener,
        WaveformView.WaveformListener {

    private Context context;
    private AudioManager audioManager;
    private int currentVolume;// 当前音量值
    private int maxVolume;//最大音量

    private UIHandler uiHandler;
    private UIThread uiThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;
        setContentView(R.layout.activity_recording);
        audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        uiHandler = new UIHandler();
        context = this;
        path = "";
        soundName = "";
        if (issdkard()) {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        } else {
            Futil.showMessage(RecordActivity.this, "请检查sd卡是否插入");
        }
        // 如果已经开发播放，先停止播放
        if (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE) {
            MainActivity.mVV.stopPlayback();
        }
        App.setIsstation(-1);
        App.setPlayerpath(null);
        App.setPlaycode(-1);
        mKeyDown = false;
        mHandler = new Handler();
        findview();
        record_null.performClick();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        final int saveZoomLevel = mWaveformView.getZoomLevel();
        super.onConfigurationChanged(newConfig);
        mHandler.postDelayed(new Runnable() {
            public void run() {
                mStartMarker.requestFocus();
                markerFocus(mStartMarker);
                mWaveformView.setZoomLevel(saveZoomLevel);
                mWaveformView.recomputeHeights(mDensity);

                updateDisplay();
            }
        }, 500);
    }

    public boolean issdkard() {
        // 首先判断sdcard是否插入
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String path = "";
    public static String soundName = "";
    public static boolean reconact = false;

    @Override
    protected void onResume() {
        reconact = true;
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
        super.onResume();
        if (path != null && !path.equals("")) {
            record_null.setSelected(false);
            record_tv1.setText("配乐：" + soundName);
            if (isrecord == 1) {
                setplayerpath();
            }
        }
    }

    @Override
    protected void onPause() {
        reconact = false;
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }

    private AlertDialog dialog;
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/hsyfm/record/";
    public static File destDir = new File(SDPATH);
    private String audiopath = null;
    private MediaPlayer mPlayer = null;
    private MP3Recorder mRecorder = null;

    // 创建一个以当前系统时间为名称的文件，防止重复
    public static File audioFile;

    // 使用系统当前日期加以调整作为照片的名称
    private static String getMP3FileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'MP3'_yyyyMMdd_HHmmss");
        return sdf.format(date);
    }

    private int isrecord = 0;// 0初始1开始2结束3暂停
    private int isnum = 0;//

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_bt:// 开始录音
                if (isrecord == 0) {
                    startrecord(getMP3FileName());
                } else if (isrecord == 1) {
                    recpause();
                } else if (isrecord == 3) {
                    startrecord(Markmp3name);
                }
                break;
            case R.id.record_restar:// 重录
                if (audioFile != null) {
                    isrecord = 0;
                    Futil.showDialog(context, "确定要重录么?", "确定", "取消", listenner);
                }
                break;
            case R.id.rec_save:// 保存录音
                if (audioFile != null) {
                    recpause();
                    openDialog();
                }
                break;
            case R.id.record_add:
                seleiv(record_add);
                ActivityJumpControl.getInstance((Activity) context).gotoSoundtrackActivity();
                break;
            case R.id.record_null:
                path = "";
                soundName = "";
                record_tv1.setText("配乐：" + soundName);
                MainActivity.mVV.pause();
                seleiv(record_null);
                break;
            case R.id.record_zhiyu:// 治愈
                getsoundtrack("治愈");
                seleiv(record_zhiyu);
                break;
            case R.id.record_dew:// 清新
                getsoundtrack("清新");
                seleiv(record_dew);
                break;
            case R.id.record_happy:// 欢乐
                getsoundtrack("欢乐");
                seleiv(record_happy);
                break;
            case R.id.record_yrico:// 抒情
                getsoundtrack("抒情");
                seleiv(record_yrico);
                break;
            case R.id.back_top:
                Futil.showDialog(context, "要放弃本次录制么?", "确定", "取消", backlistenner);
                break;
            case R.id.jianqie:
                onSave();
                break;
        }
    }

    private ImageView back_top, record_point, record_add, record_zhiyu,
            record_dew, record_null, record_happy, record_yrico, record_bt, line_lviv;
    private TextView record_tv1, record_bttv, jianqie, record_time_tv, rec_seektv;
    private RelativeLayout mViewHolder = null;
    private LinearLayout rec_save, record_restar;
    private WaveformView mWaveformView;
    private MarkerView mStartMarker;
    private MarkerView mEndMarker;
    private CheapSoundFile mSoundFile;
    private SeekBar seekbar = null;

    private void findview() {
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        rec_seektv = (TextView) findViewById(R.id.rec_seektv);
        seekbar.setMax(maxVolume);
        seekbar.setProgress(currentVolume);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        rec_seektv.setText("配乐音量:" + (currentVolume * 100 / maxVolume) + "%");

        record_time_tv = (TextView) findViewById(R.id.record_time_tv);
        mWaveformView = (WaveformView) findViewById(R.id.waveform);
        mWaveformView.setListener(this);

        mMarkerLeftInset = (int) (46 * mDensity);
        mMarkerRightInset = (int) (48 * mDensity);
        mMarkerTopOffset = (int) (10 * mDensity);
        mMarkerBottomOffset = (int) (10 * mDensity);
        mMaxPos = 0;
        mLastDisplayedStartPos = -1;
        mLastDisplayedEndPos = -1;
        mStartMarker = (MarkerView) findViewById(R.id.startmarker);
        mStartMarker.setListener(this);
        mStartMarker.setAlpha(255);
        mStartMarker.setFocusable(true);
        mStartMarker.setFocusableInTouchMode(true);
        mStartVisible = true;

        mEndMarker = (MarkerView) findViewById(R.id.endmarker);
        mEndMarker.setListener(this);
        mEndMarker.setAlpha(255);
        mEndMarker.setFocusable(true);
        mEndMarker.setFocusableInTouchMode(true);
        mEndVisible = true;

        mHandler.postDelayed(mTimerRunnable, 100);
        jianqie = (TextView) findViewById(R.id.jianqie);
        line_lviv = (ImageView) findViewById(R.id.line_lviv);
        jianqie.setOnClickListener(this);

        jianqie.setVisibility(View.GONE);
        line_lviv.setVisibility(View.GONE);
        mStartMarker.setVisibility(View.GONE);
        mEndMarker.setVisibility(View.GONE);
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
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);
        // 配乐
        record_tv1 = (TextView) findViewById(R.id.record_tv1);

        // 开始录音
        record_bt = (ImageView) findViewById(R.id.record_bt);
        record_bt.setOnClickListener(this);
        // 停止录音
        record_restar = (LinearLayout) findViewById(R.id.record_restar);
        record_restar.setOnClickListener(this);
        // 保存录音
        rec_save = (LinearLayout) findViewById(R.id.rec_save);
        rec_save.setOnClickListener(this);

        // 录音指示灯
        record_point = (ImageView) findViewById(R.id.record_point);
        record_bttv = (TextView) findViewById(R.id.record_bttv);
        // 背景音乐
        record_add = (ImageView) findViewById(R.id.record_add);
        record_add.setOnClickListener(this);// 添加自定义
        record_null = (ImageView) findViewById(R.id.record_null);
        record_null.setOnClickListener(this);// 无
        record_zhiyu = (ImageView) findViewById(R.id.record_zhiyu);
        record_zhiyu.setOnClickListener(this);// 治愈
        record_dew = (ImageView) findViewById(R.id.record_dew);
        record_dew.setOnClickListener(this);// 清新
        record_happy = (ImageView) findViewById(R.id.record_happy);
        record_happy.setOnClickListener(this);// 欢乐
        record_yrico = (ImageView) findViewById(R.id.record_yrico);
        record_yrico.setOnClickListener(this);// 抒情

    }

    private class OnSeekBarChangeListenerImp implements
            SeekBar.OnSeekBarChangeListener {

        // 触发操作，拖动
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            rec_seektv.setText("配乐音量:" + (progress * 100 / maxVolume) + "%");
        }

        // 表示进度条刚开始拖动，开始拖动时候触发的操作
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        // 停止拖动时候
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
    }

    private ArrayList<ImageView> ivlist = new ArrayList<>();

    private void seleiv(ImageView iv) {
        for (int i = 0; i < ivlist.size(); i++) {
            ivlist.remove(i).setSelected(false);
        }
        iv.setSelected(true);
        ivlist.add(iv);
    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONObject soundtrack = obj.getJSONObject("soundtrack");
                    soundName = soundtrack.getString("soundName");
                    path = URLManager.fmSound + soundtrack.getString("path");
                    record_tv1.setText("配乐：" + soundName);
                    MainActivity.mVV.pause();
                    if (isrecord == 1) {
                        setplayerpath();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    private void getsoundtrack(String soundtype) {
        HashMap<String, String> map = new HashMap<String, String>();
        String strUrl = URLManager.SoundtrackList;
        map.put("soundName", soundtype);
        Futil.xutils(strUrl, map, handler, URLManager.one);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Futil.showDialog(context, "要放弃本次录制么?", "确定", "取消", backlistenner);
        }
        return false;
    }

    //    IDLNAServiceProvider idlnaServiceProvider;
    private void recordstop() {
        try {
            MainActivity.mVV.pause();
            if (uiThread != null) {
                uiThread.stopThread();
            }
            if (uiHandler != null)
                uiHandler.removeCallbacks(uiThread);
            line_lviv.setVisibility(View.GONE);
            record_point.setSelected(false);
            mRecorder.stop();
            record_bt.setImageResource(R.mipmap.record_bt);
            mPlayer.pause();
            isrecord = 3;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DialogInterface.OnClickListener backlistenner = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == AlertDialog.BUTTON_POSITIVE) {
                isnum = 0;
                recordstop();
                if (audioFile != null) {
                    Futil.deleteFile(audioFile);
                }
                finish();
            } else if (i == AlertDialog.BUTTON_NEGATIVE) {

            }
        }
    };
    DialogInterface.OnClickListener listenner = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int pe) {
            if (pe == AlertDialog.BUTTON_POSITIVE) {
                if (audioFile != null) {
                    if (Markmp3path != null) {
                        try {
                            if (Markmp3path.size() > 0) {
                                CaoZuoMp3Utils.deletefile(Markmp3path);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mRecorder.stop();
                    if (uiThread != null) {
                        uiThread.stopThread();
                    }
                    if (uiHandler != null)
                        uiHandler.removeCallbacks(uiThread);
                    for (int i = 0; i < Markmp3path.size(); i++) {
                        Markmp3path.remove(i);
                    }
                    MainActivity.mVideoSource = "";
                    isnum = 0;
                    record_time_tv.setText(getCheckTimeBySeconds(0));
                    startrecord(Markmp3name);
                }
            } else if (pe == AlertDialog.BUTTON_NEGATIVE) {

            }
        }
    };
    private Handler mHandler;
    private String Markmp3name;
    private ArrayList<String> Markmp3path;
    private float mDensity;

    private void startrecord(String mp3name) {
        record_restar.setVisibility(View.GONE);
        rec_save.setVisibility(View.GONE);
        record_bt.setImageResource(R.mipmap.record_bts);
        if (isnum == 0) {
            System.out.println("isnum>>" + isnum);
            mTimeMill = 0;
            Markmp3path = new ArrayList<>();
            Markmp3name = mp3name;

        }
        mp3name = mp3name + "_" + isnum;
        isnum++;

        mStartMarker.setVisibility(View.GONE);
        mEndMarker.setVisibility(View.GONE);
        jianqie.setVisibility(View.GONE);
        mWaveformView.setVisibility(View.INVISIBLE);
        audioFile = new File(destDir, mp3name + ".mp3");
        Markmp3path.add(audioFile.getPath());
        mRecorder = new MP3Recorder(audioFile);
        record_bttv.setText("麦克风正在录制中");
        try {
            mRecorder.start();
            record_bt.setSelected(true);
            record_point.setSelected(true);
            isrecord = 1;
            if (path != null && !path.equals("")) {
                setplayerpath();
            }
            line_lviv.setVisibility(View.VISIBLE);
            uiThread = new UIThread();
            new Thread(uiThread).start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void recpause() {
        record_bt.setImageResource(R.mipmap.record_bt);
        record_restar.setVisibility(View.VISIBLE);
        rec_save.setVisibility(View.VISIBLE);
        try {
            String s = CaoZuoMp3Utils.heBingMp3(Markmp3path, Markmp3name, destDir);
            audioFile = new File(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadFromFile();
        jianqie.setVisibility(View.VISIBLE);
        line_lviv.setVisibility(View.GONE);
        mRecorder.stop();
        record_bt.setSelected(false);
        record_point.setSelected(false);
        record_bttv.setText("恢复录制");
        MainActivity.mVV.pause();
        isrecord = 3;
        if (uiThread != null) {
            uiThread.stopThread();
        }
        if (uiHandler != null)
            uiHandler.removeCallbacks(uiThread);
    }

    private void setplayerpath() {
        if (!path.equals(MainActivity.mVideoSource)) {
            MainActivity.mVideoSource = path;
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
            // 发起一次播放任务,当然您不一定要在这发起
            if (!MainActivity.mVV.isPlaying() && (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
                MainActivity.mVV.resume();
            } else {
                MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);//UI 事件  播放
            }
        }

    }

    public static Handler muhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 10086) {
                /**
                 * 发起一次新的播放任务
                 */
                if (MainActivity.mEventHandler.hasMessages(MainActivity.UI_EVENT_PLAY)) {
                    MainActivity.mEventHandler.removeMessages(MainActivity.UI_EVENT_PLAY);
                }
                MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);
            }
        }
    };

    @Override
    protected void onDestroy() {
        reconact = false;
        super.onDestroy();
        if (Markmp3path != null) {
            try {
                if (Markmp3path.size() > 0) {
                    CaoZuoMp3Utils.deletefile(Markmp3path);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mPlayer != null) {
            try {
                mPlayer.pause();
                mPlayer.release();
                mPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 如果已经开发播放，先停止播放
        if (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE) {
            MainActivity.mVV.stopPlayback();
        }
        mViewHolder.removeAllViews();
    }

    private void openDialog() {
        // //设置sdcard的路径
        audiopath = audioFile.getPath();
        System.out.println("audiopath"+audiopath);
        dialog = new AlertDialog.Builder(RecordActivity.this).create();
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_et, null);
        dialog.setView(v);
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (ActionBar.LayoutParams.WRAP_CONTENT); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        final EditText et = (EditText) dialog
                .findViewById(R.id.publish_name_et);
        // 使用系统当前日期加以调整作为文件的名称
        Date date = new Date(System.currentTimeMillis());
        Date date2 = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        final String tim = sdf.format(date);
        final String tim2 = sdf2.format(date);
        try {
            et.setText(tim2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RelativeLayout fanhui = (RelativeLayout) dialog
                .findViewById(R.id.dialog_fanhui_rl2);
        RelativeLayout baocun = (RelativeLayout) dialog
                .findViewById(R.id.dialog_baocun_rl1);
        fanhui.setOnClickListener(new OnClickListener() { // 返回
            @Override
            public void onClick(View v) {
                MainActivity.mVV.pause();
                dialog.dismiss();
            }
        });
        baocun.setOnClickListener(new OnClickListener() {// 保存

            @Override
            public void onClick(View v) {
                String time = recordtime;
                System.out.println("recordtime"+recordtime);
                String str1 = et.getText().toString();

                File srcDir = new File(audiopath);  //就文件路径
                File file = new File(destDir, str1 + ".mp3");//新文件路径，通过renameto修改
                srcDir.renameTo(file);
                Futil.deleteFile(audioFile);
                ArrayList<String> sKey = Futil.loadKeyArray(
                        RecordActivity.this, "1");
                sKey.add(tim);
                Futil.saveKeyArray(RecordActivity.this, sKey, "1");
                Futil.saveValue(RecordActivity.this, tim + "time", time);// 时长
                Futil.saveValue(RecordActivity.this, tim + "addtime", tim);// 添加时间
                Futil.saveValue(RecordActivity.this, tim + "name", str1);// 音乐名
                Futil.saveValue(RecordActivity.this, tim + "path",
                        file.getPath());// 在本地的音乐路径
                ActivityJumpControl.getInstance(RecordActivity.this)
                        .gotoMyAudioActivity("0", "1", "我的声音");
                dialog.dismiss();
                RecordActivity.this.finish();
            }
        });
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


    private boolean mIsPlaying;
    private boolean mStartVisible;
    private boolean mEndVisible;
    private boolean mTouchDragging;
    private boolean mKeyDown;
    private boolean mCanSeekAccurately;
    private boolean mWasGetContentIntent = false;
    private float mTouchStart;
    private int mTouchInitialStartPos;
    private int mTouchInitialEndPos;
    private int mStartPos;
    private int mEndPos;
    private int mPlayStartOffset;
    private int mPlayEndMsec;
    private int mFlingVelocity;
    private int mMaxPos;
    private int mOffset;
    private int mMarkerLeftInset;
    private int mMarkerBottomOffset;
    private int mMarkerRightInset;
    private int mMarkerTopOffset;
    private int mTouchInitialOffset;
    private int mPlayStartMsec;
    private int mLastDisplayedStartPos;
    private int mLastDisplayedEndPos;
    private long mWaveformTouchStartMsec;

    @Override
    public void markerTouchStart(MarkerView marker, float pos) {
        mTouchDragging = true;
        mTouchStart = pos;
        mTouchInitialStartPos = mStartPos;
        mTouchInitialEndPos = mEndPos;
    }

    @Override
    public void markerTouchMove(MarkerView marker, float pos) {
        float delta = pos - mTouchStart;

        if (marker == mStartMarker) {
            mStartPos = trap((int) (mTouchInitialStartPos + delta));
            mEndPos = trap((int) (mTouchInitialEndPos + delta));
        } else {
            mEndPos = trap((int) (mTouchInitialEndPos + delta));
            if (mEndPos < mStartPos)
                mEndPos = mStartPos;
        }
        updateDisplay();
    }

    @Override
    public void markerTouchEnd(MarkerView marker) {
        mTouchDragging = false;
        if (marker == mStartMarker) {
            setOffsetGoalStart();
        } else {
            setOffsetGoalEnd();
        }
    }

    @Override
    public void markerFocus(MarkerView marker) {
        mKeyDown = false;
        if (marker == mStartMarker) {
            setOffsetGoalStartNoUpdate();
        } else {
            setOffsetGoalEndNoUpdate();
        }

        // Delay updaing the display because if this focus was in
        // response to a touch event, we want to receive the touch
        // event too before updating the display.
        mHandler.postDelayed(new Runnable() {
            public void run() {
                updateDisplay();
            }
        }, 100);
    }

    @Override
    public void markerLeft(MarkerView marker, int velocity) {
        mKeyDown = true;
        if (marker == mStartMarker) {
            int saveStart = mStartPos;
            mStartPos = trap(mStartPos - velocity);
            mEndPos = trap(mEndPos - (saveStart - mStartPos));
            setOffsetGoalStart();
        }

        if (marker == mEndMarker) {
            if (mEndPos == mStartPos) {
                mStartPos = trap(mStartPos - velocity);
                mEndPos = mStartPos;
            } else {
                mEndPos = trap(mEndPos - velocity);
            }

            setOffsetGoalEnd();
        }

        updateDisplay();
    }

    @Override
    public void markerRight(MarkerView marker, int velocity) {
        mKeyDown = true;
        if (marker == mStartMarker) {
            int saveStart = mStartPos;
            mStartPos += velocity;
            if (mStartPos > mMaxPos)
                mStartPos = mMaxPos;
            mEndPos += (mStartPos - saveStart);
            if (mEndPos > mMaxPos)
                mEndPos = mMaxPos;

            setOffsetGoalStart();
        }

        if (marker == mEndMarker) {
            mEndPos += velocity;
            if (mEndPos > mMaxPos)
                mEndPos = mMaxPos;

            setOffsetGoalEnd();
        }
        updateDisplay();
    }

    @Override
    public void markerEnter(MarkerView marker) {

    }

    @Override
    public void markerKeyUp() {
        mKeyDown = false;
        updateDisplay();
    }

    @Override
    public void markerDraw() {

    }

    @Override
    public void waveformTouchStart(float x) {
        mTouchDragging = true;
        mTouchStart = x;
        mTouchInitialOffset = mOffset;
        mFlingVelocity = 0;
        mWaveformTouchStartMsec = System.currentTimeMillis();
    }

    @Override
    public void waveformTouchMove(float x) {
        mOffset = trap((int) (mTouchInitialOffset + (mTouchStart - x)));
        updateDisplay();
    }

    @Override
    public void waveformTouchEnd() {
        mTouchDragging = false;
        mOffsetGoal = mOffset;

        long elapsedMsec = System.currentTimeMillis() -
                mWaveformTouchStartMsec;
        if (elapsedMsec < 300) {
            if (mIsPlaying) {
                int seekMsec = mWaveformView.pixelsToMillisecs(
                        (int) (mTouchStart + mOffset));
                if (seekMsec >= mPlayStartMsec &&
                        seekMsec < mPlayEndMsec) {
                    mPlayer.seekTo(seekMsec - mPlayStartOffset);
                } else {
                    handlePause();
                }
            } else {
                onPlay((int) (mTouchStart + mOffset));
            }
        }
    }

    @Override
    public void waveformFling(float vx) {
        mTouchDragging = false;
        mOffsetGoal = mOffset;
        mFlingVelocity = (int) (-vx);
        updateDisplay();
    }

    @Override
    public void waveformDraw() {
        mWidth = mWaveformView.getMeasuredWidth();
        if (mOffsetGoal != mOffset && !mKeyDown)
            updateDisplay();
        else if (mIsPlaying) {
            updateDisplay();
        } else if (mFlingVelocity != 0) {
            updateDisplay();
        }
    }

    private void setOffsetGoalEnd() {
        setOffsetGoal(mEndPos - mWidth / 2);
    }

    private void setOffsetGoalStart() {
        setOffsetGoal(mStartPos - mWidth / 2);
    }

    private void setOffsetGoal(int offset) {
        setOffsetGoalNoUpdate(offset);
        updateDisplay();
    }

    private void setOffsetGoalStartNoUpdate() {
        setOffsetGoalNoUpdate(mStartPos - mWidth / 2);
    }

    private void setOffsetGoalEndNoUpdate() {
        setOffsetGoalNoUpdate(mEndPos - mWidth / 2);
    }

    private int trap(int pos) {
        if (pos < 0)
            return 0;
        if (pos > mMaxPos)
            return mMaxPos;
        return pos;
    }

    private synchronized void updateDisplay() {
        if (mIsPlaying) {
            int now = mPlayer.getCurrentPosition() + mPlayStartOffset;
            int frames = mWaveformView.millisecsToPixels(now);
            mWaveformView.setPlayback(frames);
            setOffsetGoalNoUpdate(frames - mWidth / 2);
            if (now >= mPlayEndMsec) {
                handlePause();
            }
        }

        if (!mTouchDragging) {
            int offsetDelta;

            if (mFlingVelocity != 0) {
                float saveVel = mFlingVelocity;
                offsetDelta = mFlingVelocity / 30;
                if (mFlingVelocity > 80) {
                    mFlingVelocity -= 80;
                } else if (mFlingVelocity < -80) {
                    mFlingVelocity += 80;
                } else {
                    mFlingVelocity = 0;
                }

                mOffset += offsetDelta;

                if (mOffset + mWidth / 2 > mMaxPos) {
                    mOffset = mMaxPos - mWidth / 2;
                    mFlingVelocity = 0;
                }
                if (mOffset < 0) {
                    mOffset = 0;
                    mFlingVelocity = 0;
                }
                mOffsetGoal = mOffset;
            } else {
                offsetDelta = mOffsetGoal - mOffset;

                if (offsetDelta > 10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta > 0)
                    offsetDelta = 1;
                else if (offsetDelta < -10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta < 0)
                    offsetDelta = -1;
                else
                    offsetDelta = 0;

                mOffset += offsetDelta;
            }
        }

        mWaveformView.setParameters(mStartPos, mEndPos, mOffset);
        mWaveformView.invalidate();

        mStartMarker.setContentDescription(
                getResources().getText(R.string.start_marker) + " " +
                        formatTime(mStartPos));
        mEndMarker.setContentDescription(
                getResources().getText(R.string.end_marker) + " " +
                        formatTime(mEndPos));

        int startX = mStartPos - mOffset - mMarkerLeftInset;
        if (startX + mStartMarker.getWidth() >= 0) {
            if (!mStartVisible) {
                // Delay this to avoid flicker
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        mStartVisible = true;
                        mStartMarker.setAlpha(255);
                    }
                }, 0);
            }
        } else {
            if (mStartVisible) {
                mStartMarker.setAlpha(0);
                mStartVisible = false;
            }
            startX = 0;
        }

        int endX = mEndPos - mOffset - mEndMarker.getWidth() +
                mMarkerRightInset;
        if (endX + mEndMarker.getWidth() >= 0) {
            if (!mEndVisible) {
                // Delay this to avoid flicker
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        mEndVisible = true;
                        mEndMarker.setAlpha(255);
                    }
                }, 0);
            }
        } else {
            if (mEndVisible) {
                mEndMarker.setAlpha(0);
                mEndVisible = false;
            }
            endX = 0;
        }

        mStartMarker.setLayoutParams(
                new AbsoluteLayout.LayoutParams(
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT,
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT,
                        startX,
                        mMarkerTopOffset));

        mEndMarker.setLayoutParams(
                new AbsoluteLayout.LayoutParams(
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT,
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT,
                        endX,
                        mWaveformView.getMeasuredHeight() -
                                mEndMarker.getHeight() - mMarkerBottomOffset));
    }

    private String formatTime(int pixels) {
        if (mWaveformView != null && mWaveformView.isInitialized()) {
            return formatDecimal(mWaveformView.pixelsToSeconds(pixels));
        } else {
            return "";
        }
    }

    private String formatDecimal(double x) {
        int xWhole = (int) x;
        int xFrac = (int) (100 * (x - xWhole) + 0.5);

        if (xFrac >= 100) {
            xWhole++; //Round up
            xFrac -= 100; //Now we need the remainder after the round up
            if (xFrac < 10) {
                xFrac *= 10; //we need a fraction that is 2 digits long
            }
        }

        if (xFrac < 10)
            return xWhole + ".0" + xFrac;
        else
            return xWhole + "." + xFrac;
    }

    private int mOffsetGoal;
    private int mWidth;

    private void setOffsetGoalNoUpdate(int offset) {
        if (mTouchDragging) {
            return;
        }

        mOffsetGoal = offset;
        if (mOffsetGoal + mWidth / 2 > mMaxPos)
            mOffsetGoal = mMaxPos - mWidth / 2;
        if (mOffsetGoal < 0)
            mOffsetGoal = 0;
    }

    private synchronized void handlePause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        mWaveformView.setPlayback(-1);
        mIsPlaying = false;

        /**
         * 设置播放状态
         */
//        enableDisableButtons();
    }

    /*从截取的位置开始播放*/
    private synchronized void onPlay(int startPosition) {
        if (mIsPlaying) {
            handlePause();
            return;
        }

        if (mPlayer == null) {
            // Not initialized yet
            return;
        }

        try {
            mPlayStartMsec = mWaveformView.pixelsToMillisecs(startPosition);
            if (startPosition < mStartPos) {
                mPlayEndMsec = mWaveformView.pixelsToMillisecs(mStartPos);
            } else if (startPosition > mEndPos) {
                mPlayEndMsec = mWaveformView.pixelsToMillisecs(mMaxPos);
            } else {
                mPlayEndMsec = mWaveformView.pixelsToMillisecs(mEndPos);
            }

            mPlayStartOffset = 0;

            int startFrame = mWaveformView.secondsToFrames(
                    mPlayStartMsec * 0.001);
            int endFrame = mWaveformView.secondsToFrames(
                    mPlayEndMsec * 0.001);
            int startByte = mSoundFile.getSeekableFrameOffset(startFrame);
            int endByte = mSoundFile.getSeekableFrameOffset(endFrame);
            if (mCanSeekAccurately && startByte >= 0 && endByte >= 0) {
                try {
                    mPlayer.reset();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    FileInputStream subsetInputStream = new FileInputStream(
                            audioFile.getAbsolutePath());
                    mPlayer.setDataSource(subsetInputStream.getFD(),
                            startByte, endByte - startByte);
                    mPlayer.prepare();
                    mPlayStartOffset = mPlayStartMsec;
                } catch (Exception e) {
                    System.out.println("Exception trying to play file subset");
                    mPlayer.reset();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayer.setDataSource(audioFile.getAbsolutePath());
                    mPlayer.prepare();
                    mPlayStartOffset = 0;
                }
            }

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public synchronized void onCompletion(MediaPlayer arg0) {
                    handlePause();
                }
            });
            mIsPlaying = true;

            if (mPlayStartOffset == 0) {
                mPlayer.seekTo(mPlayStartMsec);
            }
            mPlayer.start();
            updateDisplay();
//            enableDisableButtons();
        } catch (Exception e) {
            return;
        }
    }

    private String mFilename;
    private String mExtension;
    private String mDstFilename;
    private String mArtist;
    private String mAlbum;
    private String mGenre;
    private String mTitle;
    private int mYear;
    private long mLoadingStartTime;
    private long mLoadingLastUpdateTime;
    private boolean mLoadingKeepGoing;
    private ProgressDialog mProgressDialog;

    private void loadFromFile() {
        mWaveformView.setVisibility(View.VISIBLE);
        mStartMarker.setVisibility(View.VISIBLE);
        mEndMarker.setVisibility(View.VISIBLE);
        mFilename = audioFile.getPath();
        mExtension = getExtensionFromFilename(mFilename);

        SongMetadataReader metadataReader = new SongMetadataReader(RecordActivity.this, mFilename);
        mTitle = metadataReader.mTitle;
        mArtist = metadataReader.mArtist;
        mAlbum = metadataReader.mAlbum;
        mYear = metadataReader.mYear;
        mGenre = metadataReader.mGenre;

        String titleLabel = mTitle;
        if (mArtist != null && mArtist.length() > 0) {
            titleLabel += " - " + mArtist;
        }
        setTitle(titleLabel);

        mLoadingStartTime = System.currentTimeMillis();
        mLoadingLastUpdateTime = System.currentTimeMillis();
        mLoadingKeepGoing = true;
        mCanSeekAccurately = false;
        new Thread() {
            public void run() {
                mCanSeekAccurately = SeekTest.CanSeekAccurately(
                        getPreferences(Context.MODE_PRIVATE));

                System.out.println("Seek test done, creating media player.");
                try {
                    MediaPlayer player = new MediaPlayer();
                    player.setDataSource(audioFile.getAbsolutePath());
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.prepare();
                    mPlayer = player;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }.start();

        // Load the sound file in a background thread
        new Thread() {
            public void run() {
                try {
//                    mSoundFile = CheapSoundFile.create(audioFile.getAbsolutePath(),
//                            listener);
                    mSoundFile = CheapSoundFile.create(audioFile.getAbsolutePath(),
                            null);

                    if (mSoundFile == null) {
//                        mProgressDialog.dismiss();
                        String name = audioFile.getName().toLowerCase();
                        String[] components = name.split("\\.");
                        String err;
                        if (components.length < 2) {
                            err = getResources().getString(
                                    R.string.no_extension_error);
                        } else {
                            err = getResources().getString(
                                    R.string.bad_extension_error) + " " +
                                    components[components.length - 1];
                        }
                        return;
                    }
                } catch (final Exception e) {
//                    mProgressDialog.dismiss();
                    e.printStackTrace();
                    return;
                }
//                mProgressDialog.dismiss();
//                if (mLoadingKeepGoing) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        finishOpeningSoundFile();
                    }
                };
                mHandler.post(runnable);
//                }
            }
        }.start();
        while (mWaveformView.canZoomIn()) {
            mWaveformView.zoomIn();
            mStartPos = mWaveformView.getStart();
            mEndPos = mWaveformView.getEnd();
            mMaxPos = mWaveformView.maxPos();
            mOffset = mWaveformView.getOffset();
            mOffsetGoal = mOffset;
            updateDisplay();
        }
    }

    private String mCaption = "";

    private void finishOpeningSoundFile() {
        mWaveformView.setSoundFile(mSoundFile);
        mWaveformView.recomputeHeights(mDensity);

        mMaxPos = mWaveformView.maxPos();
        mLastDisplayedStartPos = -1;
        mLastDisplayedEndPos = -1;

        mTouchDragging = false;

        mOffset = 0;
        mOffsetGoal = 0;
        mFlingVelocity = 0;
        resetPositions();
        if (mEndPos > mMaxPos)
            mEndPos = mMaxPos;

        mCaption =
                mSoundFile.getFiletype() + ", " +
                        mSoundFile.getSampleRate() + " Hz, " +
                        mSoundFile.getAvgBitrateKbps() + " kbps, " +
                        formatTime(mMaxPos) + " " + "秒数";
//        mInfo.setText(mCaption);

        System.out.println("mCaption>>>" + mCaption);
        updateDisplay();
    }

    private void resetPositions() {
        mStartPos = mWaveformView.secondsToPixels(0.0);
        mEndPos = mWaveformView.secondsToPixels(mMaxPos);
    }

    private Runnable mTimerRunnable = new Runnable() {
        public void run() {
            // Updating an EditText is slow on Android.  Make sure
            // we only do the update if the text has actually changed.
            if (mStartPos != mLastDisplayedStartPos) {
                mLastDisplayedStartPos = mStartPos;
            }

            if (mEndPos != mLastDisplayedEndPos) {
                mLastDisplayedEndPos = mEndPos;
            }

            mHandler.postDelayed(mTimerRunnable, 100);
        }
    };

    /**
     * Return extension including dot, like ".mp3"
     */
    private String getExtensionFromFilename(String filename) {
        return filename.substring(filename.lastIndexOf('.'),
                filename.length());
    }

    private void onSave() {
        if (mIsPlaying) {
            handlePause();
        }
        saveRingtone(getMP3FileName());
    }

    private String makeRingtoneFilename(String title, String extension) {


        // Turn the title into a filename
        String filename = title + "_00";

        // Try to make the filename unique
        String path = null;
        for (int i = 0; i < 100; i++) {
            String testPath;
            if (i > 0)
                testPath = destDir + "/" + filename + i + extension;
            else
                testPath = destDir + "/" + filename + extension;

            try {
                RandomAccessFile f = new RandomAccessFile(
                        new File(testPath), "r");
            } catch (Exception e) {
                // Good, the file didn't exist
                path = testPath;
                break;
            }
        }
        return path;
    }

    /*保存截取的铃声*/
    private void saveRingtone(final String title) {
        final String outPath = makeRingtoneFilename(title, mExtension);

        if (outPath == null) {
            Futil.showMessage(context, "无法找到文件名");
            return;
        }

        mDstFilename = outPath;

        double startTime = mWaveformView.pixelsToSeconds(mStartPos);
        double endTime = mWaveformView.pixelsToSeconds(mEndPos);
        final int startFrame = mWaveformView.secondsToFrames(startTime);
        final int endFrame = mWaveformView.secondsToFrames(endTime);
        final int duration = (int) (endTime - startTime + 0.5);
        mTimeMill = (int) endTime;
        recordtime = getCheckTimeBySeconds(mTimeMill);
        // Create an indeterminate progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("保存中...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        // Save the sound file in a background thread
        new Thread() {
            public void run() {
                final File outFile = new File(outPath);
                try {
                    // Write the new file
                    mSoundFile.WriteFile(outFile,
                            startFrame,
                            endFrame - startFrame);

                    // Try to load the new file to make sure it worked
                    final CheapSoundFile.ProgressListener listener =
                            new CheapSoundFile.ProgressListener() {
                                public boolean reportProgress(double frac) {
                                    // Do nothing - we're not going to try to
                                    // estimate when reloading a saved sound
                                    // since it's usually fast, but hard to
                                    // estimate anyway.
                                    return true;  // Keep going
                                }
                            };
                    CheapSoundFile.create(outPath, null);
                } catch (Exception e) {
                    mProgressDialog.dismiss();

                    CharSequence errorMessage;
                    if (e.getMessage().equals("No space left on device")) {
                        Futil.showMessage(context, "您的SD卡已满，没有足够空间保存此文件。");
                        e = null;
                    } else {
                        Futil.showMessage(context, "写文件时出错");
                    }
                    return;
                }

                mProgressDialog.dismiss();

                Runnable runnable = new Runnable() {
                    public void run() {
                        afterSavingRingtone(title,
                                outPath,
                                outFile,
                                duration);
                    }
                };
                mHandler.post(runnable);
            }
        }.start();
    }

    public static final String PREF_SUCCESS_COUNT = "success_count";

    /*保存截取的文件*/
    private void afterSavingRingtone(String title,
                                     String outPath,
                                     File outFile,
                                     int duration) {
        long length = outFile.length();
        if (length <= 1) {
            outFile.delete();
            new AlertDialog.Builder(this)
                    .setTitle(R.string.alert_title_failure)
                    .setMessage(R.string.too_small_error)
                    .setPositiveButton(R.string.alert_ok_button, null)
                    .setCancelable(false)
                    .show();
            return;
        }

        // Create the database record, pointing to the existing file path

        long fileSize = outFile.length();
        String mimeType = "audio/mpeg";

        String artist = "" + getResources().getText(R.string.artist_name);

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, outPath);
        values.put(MediaStore.MediaColumns.TITLE, title);
        values.put(MediaStore.MediaColumns.SIZE, fileSize);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

        values.put(MediaStore.Audio.Media.ARTIST, artist);
        values.put(MediaStore.Audio.Media.DURATION, duration);

        values.put(MediaStore.Audio.Media.IS_MUSIC, true);

        // Insert it into the database
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
        final Uri newUri = getContentResolver().insert(uri, values);
        setResult(RESULT_OK, new Intent().setData(newUri));

        // Update a preference that counts how many times we've
        // successfully saved a ringtone or other audio
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        int successCount = prefs.getInt(PREF_SUCCESS_COUNT, 0);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt(PREF_SUCCESS_COUNT, successCount + 1);
        prefsEditor.commit();

        // If Ringdroid was launched to get content, just return
        if (mWasGetContentIntent) {
//            sendStatsToServerIfAllowedAndFinish();
            return;
        }
        // There's nothing more to do with music or an alarm.  Show a
        // success message and then quit.
        if(audioFile!=null){
            try{
                Futil.deleteFile(audioFile);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        audioFile = outFile;
        Markmp3name = title;
        if (Markmp3path != null) {
            try {
                if (Markmp3path.size() > 0) {
                    CaoZuoMp3Utils.deletefile(Markmp3path);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (Markmp3path.size()!=0){
            for (int i = 0; i < Markmp3path.size(); i++) {
                Markmp3path.remove(i);
            }
        }
        Markmp3path.add(outFile.getPath());
        System.out.println("Markmp3path>>>"+Markmp3path.size());
        record_time_tv.setText(getCheckTimeBySeconds(mTimeMill));
        loadFromFile();
    }

    private String recordtime;

    class UIHandler extends Handler {
        public UIHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            //TODO Auto-generated method stub
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int type = b.getInt("msg");
            int vTime = b.getInt("msg");
            recordtime = getCheckTimeBySeconds(vTime);
            System.out.println("UIHandler...recordtime>>>"+recordtime);
            if ("10:00".equals(recordtime)) {// 判断10分钟之后，停止录音
                try {
                    recpause();
                    loadFromFile();
                    if (uiThread != null) {
                        uiThread.stopThread();
                    }
                    if (uiHandler != null)
                        uiHandler.removeCallbacks(uiThread);
                    line_lviv.setVisibility(View.GONE);
                    record_point.setSelected(false);
                    mRecorder.stop();
                    record_bttv.setText("录音已结束");
                    record_bt.setImageResource(R.mipmap.record_bt);
                    record_restar.setVisibility(View.VISIBLE);
                    rec_save.setVisibility(View.VISIBLE);
                    isrecord = 2;
                    isnum = 0;
                    recordstop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            record_time_tv.setText(recordtime);
            if (type % 2 == 0) {
                line_lviv.setImageResource(R.mipmap.line_lv);
            } else {
                line_lviv.setImageResource(R.mipmap.line_lv2);
            }
        }
    }

    /**
     * 根据当前选择的秒数还原时间点
     *
     * @param startTime
     */

    private static String getCheckTimeBySeconds(int startTime) {
        String return_m = "", return_s = "";
        int m = (startTime % 3600) / 60;
        int s = startTime % 60;
        if (s >= 10) {
            return_s = s + "";
        } else {
            return_s = "0" + (s);
        }
        if (m >= 10) {
            return_m = m + "";
        } else {
            return_m = "0" + (m);
        }
        return return_m + ":" + return_s;
    }

    private int mTimeMill = 0;

    class UIThread implements Runnable {
        boolean vRun = true;

        public void stopThread() {
            vRun = false;
        }

        public void run() {
            while (vRun) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mTimeMill++;
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
//                b.putInt("cmd", CMD_RECORDING_TIME);
                b.putInt("msg", mTimeMill);
                msg.setData(b);

                RecordActivity.this.uiHandler.sendMessage(msg);
                // 向Handler发送消息,更新UI
            }

        }
    }
}
