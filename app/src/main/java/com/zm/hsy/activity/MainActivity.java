package com.zm.hsy.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.baidu.cyberplayer.core.BVideoView;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.fragment.HomeFragment0;
import com.zm.hsy.fragment.HomeFragment1;
import com.zm.hsy.fragment.HomeFragment2;
import com.zm.hsy.fragment.HomeFragment3;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.GetRedRound;

import java.util.ArrayList;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends FragmentActivity implements OnClickListener, BVideoView.OnPreparedListener,
        BVideoView.OnCompletionListener, BVideoView.OnErrorListener, BVideoView.OnInfoListener,
        BVideoView.OnPlayingBufferCacheListener, BVideoView.OnCompletionWithParamListener {

    private ImageView hbt3;
    private RelativeLayout hrv1, hrv2, hrv3, hrv4, hrv5;

    private long mExitTime;
    private String userid;
    private Context context;
    public static String AK = "1950d3c283bc45f59ea6684afb1d7a43";   // 请录入您的AK !!!
    public static EventHandler mEventHandler;
    private HandlerThread mHandlerThread;

    public static TextView hongdian;
    public static boolean ishongdian=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 填充标题栏
        context = this;
        AMap();//高德定位
        userid = Futil.getValue(context, "userid");//试图从sp中取出userid
        setContentView(R.layout.activity_main);
        initView();//绑定组件设置监听
        gotoFragment(1, hrv1);
        hrv3.setClickable(true);//中间的开始播放图片
//        hrv3.setClickable(false);//中间的开始播放图片
        String shield = Futil.getValue(context, "shield");//保护罩？
        if (!shield.equals("1") && !userid.equals("")) {
            Futil.showMessage(context, context.getString(R.string.shield_remind));
        }
        //fir.im key
        UpdateKey.API_TOKEN = "dd2963de183d19e5bd484aa2f113c9c7";
        UpdateKey.APP_ID = "57f07f55959d6925b4000293";
        //下载方式:
        UpdateKey.DialogOrNotification = UpdateKey.WITH_DIALOG;//通过Dialog来进行下载
        //UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;通过通知栏来进行下载(默认)
        UpdateFunGO.init(this);
//        UpdateFunGO.manualStart(this);

        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread",
                Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());
        /**
         * 设置ak
         */
        BVideoView.setAK(AK);
        /**
         *创建BVideoView和BMediaController
         */
        mVV = new BVideoView(this);
        mVV.setLogLevel(4);
        /**
         * 注册listener
         */
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnCompletionWithParamListener(this);
        mVV.setOnErrorListener(this);
        mVV.setOnInfoListener(this);

    }


    @Override
    protected void onResume() {
        setcontextaudio();
        //如果电台id或者音乐封面不为空
        if (id != null && audioAlbumId != null) {
            hrv3.setClickable(true);//可以点击中间图标
        }
        UpdateFunGO.onResume(this);
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            UpdateFunGO.onStop(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
        mLocationClient.stopLocation();// 停止定位
        super.onPause();
    }

    private String id = null, audioAlbumId = null, //音乐id，封面图id
            mmsh = null, stationname = null, stationid = null;//电台路径 名称，id
    private int isstation=-1;

    private void setcontextaudio() {
        isstation = App.getIsstation();//是否播放过音乐电台 0音乐1电台
        AudioList contextaudio = App.getContextAudioList();//获取音乐信息类对象
        if (isstation == 0) {//播放过音乐节目
            if (contextaudio != null) {
                String cover = contextaudio.getCover();//获得封面
                id = contextaudio.getId();//音频id
                audioAlbumId = contextaudio.getAudioAlbumId();//获得所属专辑id
                //如果播放过音乐节目，就设置中间图标为封面
                cover = URLManager.COVER_URL + cover;
                Picasso.with(MainActivity.this).load(cover).resize(400, 400)
                        .placeholder(R.color.touming).error(R.mipmap.ic_launcher)
                        .into(hbt3);
            }

            //如果播放过电台节目，就设置中间图标为相声图片
        } else if (isstation == 1) {
            mmsh = App.getPlayerpath();
            stationname = App.getStationname();
            stationid = App.getStationid();
            Picasso.with(MainActivity.this).load(R.mipmap.yyp)
                    .resize(400, 400).placeholder(R.color.touming)
                    .error(R.mipmap.ic_launcher).into(hbt3);
        }

    }

    private Fragment contentFragment;
    // fragment管理者
    private FragmentManager fragmentManager;
    // 开启一个Fragment事务
    private FragmentTransaction transaction;

    private void gotoFragment(int tag, RelativeLayout hrv) {

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        if (tag == 1) {
            contentFragment = new HomeFragment0();
            transaction.replace(R.id.main_framelayout, contentFragment);
            transaction.commit();
        } else if (tag == 2) {

            contentFragment = new HomeFragment1();
            transaction.replace(R.id.main_framelayout, contentFragment);
            transaction.commit();

        } else if (tag == 3) {
            contentFragment = new HomeFragment2();
            transaction.replace(R.id.main_framelayout, contentFragment);
            transaction.commit();

        } else if (tag == 4) {
            contentFragment = new HomeFragment3();
            transaction.replace(R.id.main_framelayout, contentFragment);
            transaction.commit();
        }
        for (int i = 0; i < list.size(); i++) {
            list.remove(i).setSelected(false);
        }
        hrv.setSelected(true);
        list.add(hrv);
    }

    private void initView() {
        //红点
        hongdian= (TextView) this.findViewById(R.id.main_hongdian);

        // 快速注册
        // 导航页
        hrv1 = (RelativeLayout) this.findViewById(R.id.hrv1);
        hrv2 = (RelativeLayout) this.findViewById(R.id.hrv2);
        hrv4 = (RelativeLayout) this.findViewById(R.id.hrv4);
        hrv5 = (RelativeLayout) this.findViewById(R.id.hrv5);

        hrv3 = (RelativeLayout) this.findViewById(R.id.hrv3);
        hrv3.setOnClickListener(this);
        hbt3 = (ImageView) this.findViewById(R.id.hbt3);

        // title_login.setOnClickListener(this);
        hrv1.setOnClickListener(this);
        hrv2.setOnClickListener(this);
        hrv4.setOnClickListener(this);
        hrv5.setOnClickListener(this);

        //获取是否有消息，来判断红点是否显示
        new GetRedRound(this).getPushMes();

    }

    //按两下退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
                return true;
            }
        }
        return false;
    }

    private ArrayList<RelativeLayout> list = new ArrayList<RelativeLayout>();

    @Override
    public void onClick(View v) {
        String uid = Futil.getValue(context, "userid");
        System.out.println("Mainactivity>userid>>>" + userid);
        switch (v.getId()) {
            case R.id.hrv1:
                gotoFragment(1, hrv1);
                break;
            case R.id.hrv2:
                if (uid != null && !uid.equals("")) {
                    gotoFragment(2, hrv2);

                } else {
                    ActivityJumpControl.getInstance(MainActivity.this).gotoLoginActivity();

                }
                break;
            case R.id.hrv3:
                if (isstation == 0) {//音乐
                    Log.i("bigbtn", "跳转音乐");
                    ActivityJumpControl.getInstance(MainActivity.this)
                            .gotoDetailsPlayActivity(id);
                } else if (isstation == 1) {//电台
                    Log.i("bigbtn", "跳转电台");
                    ActivityJumpControl.getInstance(MainActivity.this)
                            .gotoRadioStationPlayerActivity(mmsh, stationname, stationid);
                }
                else {
                    //先点电台，再点音乐，在点回电台，中间大按钮就会一直保持音乐的图片，并且点击会说无播放电台，
                    //将onCompletion()中的setIsstation -1的方法注释就好了，但有可能会有别的问题，特此声明
                    Toast.makeText(context, "您没有正在播放的电台", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.hrv4:
                if (uid != null && !uid.equals("")) {
                    gotoFragment(3, hrv4);
                } else {
                    ActivityJumpControl.getInstance(MainActivity.this).gotoLoginActivity();
                }
                break;
            case R.id.hrv5:
                gotoFragment(4, hrv5);
                hongdian.setVisibility(View.GONE);
                break;

            default:
                break;
        }

    }

    public AMapLocationClient mLocationClient = null;
    public AMapLocationListener mLocationListener = new MyLocationListener();
    public static String province;

    private void AMap() {
        Log.i(TAG, "AMap: ");
        // 初始化定位
        mLocationClient = new AMapLocationClient(
                this.getApplicationContext());
//        InitLocation();
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        // 启动定位
        mLocationClient.startLocation();
    }


    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (location != null) {
                if (location.getErrorCode() == 0) {
                    Log.i(TAG, "成功");
                    if (location.getProvince() == null) {
                        mLocationClient.stopLocation();// 停止定位
                    } else {
                        String p = location.getProvince();
                        if (p.equals("北京市") || p.equals("上海市")
                                || p.equals("天津市") || p.equals("重庆市")) {
                            p = p.substring(0, p.indexOf("市"));
                        } else {
                            p = p.substring(0, p.indexOf("省"));
                        }
                        province = p;
                        Futil.saveValue(context, "province", province);
                        mLocationClient.stopLocation();// 停止定位
                    }

                } else {
                    Log.e("AmapError",
                            "location Error, ErrCode:"
                                    + location.getErrorCode() + ", errInfo:"
                                    + location.getErrorInfo());
                    mLocationClient.stopLocation();// 停止定位
                    province = "";
                    Futil.saveValue(context, "province", province);
                }
            }
        }
    }

    private void InitLocation() {
        // 初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

    }

    private final String TAG = "MainActivity";
    public static final int UI_EVENT_PLAY = 0;
    public static final Object SYNC_Playing = new Object();

    /**
     * 播放状态
     */
    //定义一个枚举
    public static enum PLAYER_STATUS {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,//空闲 准备中 准备完
    }
    //默认状态是空闲
    public static PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
    public static BVideoView mVV = null;
    public static String mVideoSource = null;//播放路径
    /**
     * 记录播放位置
     */
    public static int mLastPos = 0;//上次播放到的位置

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UI_EVENT_PLAY:
                    /**
                     * 如果已经播放了，等待上一次播放结束
                     */
                    if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
                        synchronized (SYNC_Playing) {
                            try {
                                SYNC_Playing.wait();
                                Log.v(TAG, "wait player status to idle");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mVV.setVideoPath(mVideoSource);
                    /**
                     * 续播，如果需要如此
                     */
                    if (mLastPos > 0) {
                        //跳到这个位置
                        mVV.seekTo(mLastPos);
                        mLastPos = 0;
                    }
                    /**
                     * 显示或者隐藏缓冲提示
                     */
                    mVV.showCacheInfo(true);
                    /**
                     * 开始播放
                     */
                    mVV.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
//                    mVV.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;//改变播放状态
                    mVV.start();

                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ((mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
            mLastPos = (int) mVV.getCurrentPosition();//获取播放位置
            /*停止播放*/
            mVV.stopPlayback();
        }
        /**
         * 结束后台事件处理线程
         */
        mHandlerThread.quit();
        Log.v(TAG, "onDestroy");
    }

    /**
     * 播放准备就绪
     *  BVideoView.OnPreparedListener,
     */
    @Override
    public void onPrepared() {
        //改变状态为准备好
        MainActivity.mPlayerStatus = MainActivity.PLAYER_STATUS.PLAYER_PREPARED;
    }

    /**
     * 播放完成
     */
    @Override
    public void onCompletion() {
        synchronized (MainActivity.SYNC_Playing) {
            MainActivity.SYNC_Playing.notify();
        }
        MainActivity.mPlayerStatus = MainActivity.PLAYER_STATUS.PLAYER_IDLE;
        System.out.println("onCompletion>>>>");
        try {
            if (DetailsPlayActivity.muplayonact) {
                DetailsPlayActivity.muhandler.sendEmptyMessage(10086);
            }
            if (BangdanTitleListview.bdonact) {
                BangdanTitleListview.muhandler.sendEmptyMessage(10086);
            }
            if (BBSCommunityActivity.bbsconact) {
                BBSCommunityActivity.muhandler.sendEmptyMessage(10086);
            }
            if (DetailsActivity.detonact) {
                DetailsActivity.muhandler.sendEmptyMessage(10086);
            }
            if (MyConcemFansActivity.mcfonact) {
                MyConcemFansActivity.muhandler.sendEmptyMessage(10086);
            }
            if (MyPHSActivity.mphsonact) {
                MyPHSActivity.muhandler.sendEmptyMessage(10086);
            }
            if (TabVF2Activity.mcfonact) {
                TabVF2Activity.muhandler.sendEmptyMessage(10086);
            }
            if (VideoPlayingActivity.vplayonact) {
                VideoPlayingActivity.muhandler.sendEmptyMessage(10086);
            }
            if (RecordActivity.reconact) {
                RecordActivity.muhandler.sendEmptyMessage(10086);
            }
            if (BBSCardActivity.onbbscardact) {
                BBSCardActivity.muhandler.sendEmptyMessage(10086);
            }
            //初始化状态
//            App.setIsstation(-1);
            App.setPlayerpath("");
            App.setPlaycode(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
     */
    @Override
    public void onPlayingBufferCache(int i) {

    }

    /****
     * 功能与参数
     */
    @Override
    public void OnCompletionWithParam(int param) {
        Log.v(TAG, "OnCompletionWithParam=" + param);
    }

    /***
     * 播放出错
     */
    @Override
    public boolean onError(int i, int i1) {
        synchronized (MainActivity.SYNC_Playing) {
            MainActivity.SYNC_Playing.notify();
        }
        MainActivity.mPlayerStatus = MainActivity.PLAYER_STATUS.PLAYER_IDLE;
        System.out.println("onError>>>>");
        App.setIsstation(-1);
        App.setPlayerpath("");
        App.setPlaycode(-1);
        try {
            if (DetailsPlayActivity.muplayonact) {
                DetailsPlayActivity.muhandler.sendEmptyMessage(10086);
            } else if (BangdanTitleListview.bdonact) {
                BangdanTitleListview.muhandler.sendEmptyMessage(10086);
            } else if (BBSCommunityActivity.bbsconact) {
                BBSCommunityActivity.muhandler.sendEmptyMessage(10086);
            } else if (DetailsActivity.detonact) {
                DetailsActivity.muhandler.sendEmptyMessage(10086);
            } else if (MyConcemFansActivity.mcfonact) {
                MyConcemFansActivity.muhandler.sendEmptyMessage(10086);
            } else if (MyPHSActivity.mphsonact) {
                MyPHSActivity.muhandler.sendEmptyMessage(10086);
            } else if (TabVF2Activity.mcfonact) {
                TabVF2Activity.muhandler.sendEmptyMessage(10086);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /***
     * 缓冲
     */
    @Override
    public boolean onInfo(int what, int extra) {
        switch (what) {
            /**
             * 开始缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_START:
                Log.i(TAG,
                        "caching start,now playing url : "
                                + MainActivity.mVV.getCurrentPlayingUrl());

                break;
            /**
             * 结束缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_END:
                Log.i(TAG,
                        "caching start,now playing url : "
                                + MainActivity.mVV.getCurrentPlayingUrl());

                break;
            default:
                break;
        }
        return false;
    }
}
