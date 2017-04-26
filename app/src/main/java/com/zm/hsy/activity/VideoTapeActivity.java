package com.zm.hsy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.util.ActivityJumpControl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;
/** 录视频 */
public class VideoTapeActivity extends Activity implements OnClickListener {

    private String userid;
    private MediaRecorder recorder;// 录制
    private MediaPlayer mPlayer;// 播放
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/hsyfm/videorecord/";
    public static File destDir = new File(SDPATH);
    // 创建一个以当前系统时间为名称的文件，防止重复
    public File myRecAudioFile = null;

    // 使用系统当前日期加以调整作为照片的名称
    private static String get3GPFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'MP4'_yyyyMMdd_HHmmss");
        return sdf.format(date) + ".mp4";
    }

    private Context context;

    private boolean mIsSufaceCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videotape);
        Log.i("actname", "VideoTapeActivity");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        userid = Futil.getValue(context, "userid");

        // 如果已经开发播放，先停止播放
        if (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE) {
            MainActivity.mVV.stopPlayback();
        }
        findview();
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
        stopPreview();
    }

    private AlertDialog dialog;
    private int p = 0;// 0暂停1播放
    private int bt = 0;// 0未开始不可执行其他操作，1反之
    private String videopath;
    private static int camera_orientation=0;//摄像头用前边还是后边

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.videotape_bt:// 开始/暂停录制
                String tapetv = videotape_bttv.getText().toString();
                if (tapetv.equals("点击开始录制")) {
                    video_fanzhuan.setVisibility(View.GONE);//隐藏翻转
                    recorder();
                    bt = 0;
                    // 计时器重置
                    videotape_time.setBase(SystemClock.elapsedRealtime());
                    // 开始计时
                    videotape_time.start();
                    videotape_point.setSelected(true);
                    videotape_bttv.setText("点击结束录制");
                } else if (tapetv.equals("点击结束录制")) {
                    // 停止计时
                    videotape_time.stop();
                    //设置后不会崩
                    recorder.setOnErrorListener(null);
                    recorder.setPreviewDisplay(null);
                    try {
                        recorder.stop();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    recorder.reset();
                    recorder.release();
                    recorder = null;
                    System.out.println("结束录制");
                    videotape_point.setSelected(false);
                    bt = 1;
                    videotape_bttv.setText("已结束录制");
                    video_fanzhuan.setVisibility(View.VISIBLE);//显示翻转
                }

                break;
            case R.id.videotape_play:// 视频播放/暂停
                System.out.println("bt>>>" + bt);
                if (bt == 1) {
                    if (p == 0) {
                        videopath = myRecAudioFile.getPath();
                        System.out.println("videopath>>>" + videopath);
                        play(videopath, playView);
                        p = 1;
                        videotape_play.setSelected(true);
                    } else if (p == 1) {
                        stop();
                        p = 0;
                        videotape_play.setSelected(false);
                    }
                } else if (bt == 0) {
                    Futil.showMessage(context, "还未开始录制");
                }
                break;
            case R.id.videotape_R:// 保存
                if (bt != 1) {
                    //设置后不会崩
                    recorder.setOnErrorListener(null);
                    recorder.setPreviewDisplay(null);
                    // 停止计时
                    videotape_time.stop();
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                    System.out.println("结束录制");
                    videotape_point.setSelected(false);
                    bt = 1;
                    videotape_bttv.setText("已结束录制");
                }
                if (p != 0) {
                    stop();
                    videotape_play.setSelected(false);
                    p = 0;
                }
                openDialog();
                break;
            case R.id.videotape_L:// 重录
                if (bt == 1) {
                    video_fanzhuan.setVisibility(View.GONE);//点击重录隐藏翻转
                    if (recorder == null) {
                        recorder = new MediaRecorder();
                    }
                    recorder();
                    bt = 0;
                    // 计时器重置
                    videotape_time.setBase(SystemClock.elapsedRealtime());
                    // 开始计时
                    videotape_time.start();
                    videotape_point.setSelected(true);
                    videotape_bttv.setText("点击结束录制");
                }
                break;
            case R.id.back_top:
                Futil.showDialog(context, "要放弃本次录制么?", "确定", "取消", backlistenner);
                break;
            case R.id.video_fanzhuan://翻转摄像头
                if (camera_orientation==0){
                    camera_orientation=1;
                }else {
                    camera_orientation=0;
                }
                mCamera=null;//设为空不然被return

                startPreview();//重启预览
                break;

        }
    }

    private ImageView back_top, videotape_play, videotape_point, videotape_bt;
    private SurfaceView mSurfaceView, playView;
    private SurfaceHolder mSurfaceHolder;
    private Chronometer videotape_time;
    private TextView videotape_L, videotape_R, videotape_bttv;
    private TextView video_fanzhuan;

    private void findview() {
        mSurfaceView = (SurfaceView) findViewById(R.id.videotape_videoView);
        playView = (SurfaceView) findViewById(R.id.videotape_playView);

        //设置SurfaceView自己不管理的缓冲区
        playView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(mSurfaceCallback);
        videotape_time = (Chronometer) findViewById(R.id.videotape_time);
        videotape_time
                .setOnChronometerTickListener(new OnChronometerTickListenerImpl()); // 给计时组件设置舰艇对象
        videotape_bttv = (TextView) findViewById(R.id.videotape_bttv);
        videotape_point = (ImageView) findViewById(R.id.videotape_point);
        videotape_L = (TextView) findViewById(R.id.videotape_L);
        videotape_L.setOnClickListener(this);// 重录
        videotape_R = (TextView) findViewById(R.id.videotape_R);
        videotape_R.setOnClickListener(this);// 保存
        videotape_play = (ImageView) findViewById(R.id.videotape_play);
        videotape_play.setOnClickListener(this);// 视频播放/暂停
        videotape_bt = (ImageView) findViewById(R.id.videotape_bt);
        videotape_bt.setOnClickListener(this);// 开始/暂停录制
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);// 返回
        video_fanzhuan= (TextView) findViewById(R.id.video_fanzhuan);
        video_fanzhuan.setOnClickListener(this);

    }

    DialogInterface.OnClickListener backlistenner = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == AlertDialog.BUTTON_POSITIVE) {
                if (mCamera != null) {
                    mCamera.lock();
                }
                if (mPlayer != null) {
                    mPlayer.release();
                    mPlayer = null;
                }
                if (myRecAudioFile != null) {
                    Futil.deleteFile(myRecAudioFile);
                }
                finish();
            } else if (i == AlertDialog.BUTTON_NEGATIVE) {

            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Futil.showDialog(context, "要放弃本次录制么?", "确定", "取消", backlistenner);
        }
        return false;
    }

    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mIsSufaceCreated = false;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mIsSufaceCreated = true;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            startPreview();
        }
    };

    private Camera mCamera;

    //        	public void recorder() {
//		try {
////取消录制后再次进行录制时必须加如下两步操作，不然会报错
//			mCamera.lock();
//			mCamera.unlock();
//			recorder.setCamera(mCamera);
//			recorder.setPreviewDisplay(mSurfaceHolder.getSurface());
//			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 摄像头为视频源
//			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风为音频源
//			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 视频输出格式为MP4
//			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 音频格式
//
//			//根据屏幕分辨率设置录制尺寸
//			CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
//			recorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
//
//			recorder.setVideoFrameRate(30); // 视频帧频率,显著提高录像时的流畅度
//			recorder.setVideoEncodingBitRate(profile.videoFrameWidth * profile.videoFrameHeight);// 设置帧频率，然后就清晰了
//			recorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
//			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式
//			recorder.setOutputFile(myRecAudioFile.getAbsolutePath());// 保存路径
//			recorder.prepare();
//			recorder.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
    public void recorder() {
        if (myRecAudioFile == null) {
            myRecAudioFile = new File(destDir, get3GPFileName());

        }

        recorder = new MediaRecorder();
        recorder.reset();
        mCamera.lock();
        mCamera.unlock();
        //给Recorder设置Camera对象，保证录像跟预览的方向保持一致
        recorder.setCamera(mCamera);
        recorder.setOrientationHint(90);    //旋转90°
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        recorder.setProfile(profile);
//
//        profile.videoFrameWidth = 640;// 设置视频大小
//
//        profile.videoFrameHeight = 480;

        profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;// 输出格式为mp4
        profile.videoCodec = MediaRecorder.VideoEncoder.H264;// 视频编码
        profile.audioCodec = MediaRecorder.AudioEncoder.AAC;// 音频编码
//        profile.videoFrameRate = 15;//设置录制的视频帧率


        recorder.setOutputFile(myRecAudioFile.getAbsolutePath());// 保存路径

        recorder.setPreviewDisplay(mSurfaceHolder.getSurface());// 预览

        try {

            recorder.prepare();
            recorder.start();

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }

    //启动预览
    private void startPreview() {
        //保证只有一个Camera对象
        if (mCamera != null || !mIsSufaceCreated) {
            Log.d("VideoTapeActivity", "startPreview will return");
            return;
        }
        mSurfaceView.setVisibility(View.VISIBLE);
        playView.setVisibility(View.GONE);
        mCamera = Camera.open(camera_orientation);//打开摄像头，0是后置1是前置
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        parameters.setPreviewFrameRate(20);//预览帧速率
        //设置相机预览方向
        mCamera.setDisplayOrientation(90);

        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);//通过surfaceholder显示取景画面
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCamera.startPreview();//开始预览
    }


    public void play(String fileName, SurfaceView view) {
        try {
            mSurfaceView.setVisibility(View.GONE);
            playView.setVisibility(View.VISIBLE);
            mPlayer = new MediaPlayer();
            mPlayer.reset();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置需要播放的视频
            mPlayer.setDataSource(fileName);
            mPlayer.setDisplay(view.getHolder()); // 定义一个SurfaceView播放它
            mPlayer.setLooping(true);
            mPlayer.prepare();
            mPlayer.seekTo(0);
            mPlayer.start();
            mPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer arg0) {
                    stop();
                    videotape_play.setSelected(false);
                    p = 0;
                }
            });
            System.out.println("mPlayer.start>>>");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mCamera != null) {
            mCamera.lock();
        }
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        System.out.println("mPlayer.stop>>>");
        //重启预览
        startPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera_orientation=0;
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public class OnChronometerTickListenerImpl implements // 计时监听事件，随时随地的监听时间的变化
            OnChronometerTickListener {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            String time = chronometer.getText().toString();
            if ("10:00".equals(time)) {// 判断10分钟之后，停止录音
                try {
                    // 停止计时
                    videotape_time.stop();
                    recorder.stop();
                    recorder.reset();
                    recorder.release();
                    recorder = null;
                    videotape_point.setSelected(false);
                    bt = 1;
                    videotape_bttv.setText("点击重新录制");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void stopPreview() {
        //释放Camera对象
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void openDialog() {
        dialog = new AlertDialog.Builder(context).create();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String tim = sdf.format(date);
        try {
            et.setText(tim);
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
                dialog.dismiss();
            }
        });
        baocun.setOnClickListener(new OnClickListener() {// 保存

            @Override
            public void onClick(View v) {
                String time = videotape_time.getText().toString();
                String str1 = et.getText().toString();
                File srcDir = new File(myRecAudioFile.getPath());  //就文件路径
                File file = new File(destDir, str1 + ".mp4");//新文件路径，通过renameto修改
                srcDir.renameTo(file);
                Futil.deleteFile(myRecAudioFile);
                ArrayList<String> sKey = Futil.loadKeyArray(context, "3");
                sKey.add(tim);
                Futil.saveKeyArray(context, sKey, "3");
                Futil.saveValue(context, tim + "time", time);// 时长
                Futil.saveValue(context, tim + "addtime", tim);// 添加时间
                Futil.saveValue(context, tim + "name", str1);// 视频名
                Futil.saveValue(context, tim + "path", file.getPath());// 在本地的视频路径
                ActivityJumpControl.getInstance(VideoTapeActivity.this)
                        .gotoMyAudioActivity("0", "3", "我的视频");
                dialog.dismiss();
                VideoTapeActivity.this.finish();
            }
        });
    }

}
