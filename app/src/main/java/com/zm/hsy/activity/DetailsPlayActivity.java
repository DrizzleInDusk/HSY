package com.zm.hsy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.adapter.ContentListAdapter;
import com.zm.hsy.adapter.TjAlbumAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.entity.ContentList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.myview.WordWrapViewYuan;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.BeanTu;
import com.zm.hsy.util.CustomProgressDialog;
import com.zm.hsy.util.SipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
/** 专辑播放详情页 */
public class DetailsPlayActivity extends Activity implements OnClickListener {
    private ArrayList<Album> albumList;
    private TjAlbumAdapter tadapter;
    private ContentListAdapter cadapter;
    private ArrayList<ContentList> arraycList ;
    private ListView mtglistView, mtjlistView, mclistView;

    private String id = "", userid, shield, playerpath = "", playerpath1;
    private LayoutInflater mInflater;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_playpage);
        Log.i("sbcs", "专辑播放详情页");
        context = this;
        // 获取布局填充器
        mInflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        userid = Futil.getValue(context, "userid");
        shield = Futil.getValue(context, "shield");
        Intent i = this.getIntent();
        id = i.getStringExtra("id");
        api = WXAPIFactory.createWXAPI(context, URLManager.WXappId, true);
        mTencent = Tencent.createInstance(URLManager.QQappId, context);
        findview();
    }

    public static boolean onepath = true;
    public static boolean muplayonact = false;

    @Override
    protected void onResume() {
        muplayonact = true;
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
        super.onResume();
        gointo(id);

    }

    @Override
    protected void onPause() {
        muplayonact = false;
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewHolder.removeAllViews();
        muplayonact = false;
    }

    private String mid = null, maudioAlbumId = null, mcover = null, sname = null, mblurb = null;
    private int playcode;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.playpage_playbt:
                startplayer();
                break;

            case R.id.playpage_audiomessage:
                break;
            case R.id.details_playpage_downiv1:
                finish();
                break;
            case R.id.playpage_dianping_send:
                if (userid.equals("") || userid == null) {
                    Futil.showMessage(context, "请先登录");
                    break;
                }
                if (shield.equals("1")) {
                    String st = pinglun_et.getText().toString().trim();
                    etsend(st);
                } else {
                    Futil.showMessage(context, context.getString(R.string.shield_remind));
                }
                break;
            case R.id.playpage_liebiao:
                into();
                break;
            case R.id.playpage_item_dingyue:
                String dy = dingyue.getText().toString();
                if (userid.equals("") || userid == null) {
                    Futil.showMessage(context, "请先登录");
                    return;
                }
                if (dy.equals("订阅")) {
                    dingyue.setText("已订阅");
                    String strUrl = URLManager.AddSubscribe;
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("userSubscribe.userId", userid);// 用户
                    map.put("userSubscribe.audioAlbumId", AlbumId);// 专辑id
                    Futil.xutils(strUrl, map, handler, URLManager.three);
                }

                break;
            case R.id.dianping_more:
                String stv = "全部评论";
                String Tag = "1";
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoYinpintitleListview(audioId, stv, Tag);
                break;
            case R.id.playpage_dianping_xihuan: {
                cickPraise();
            }
            break;
            case R.id.moretj_rl: { //更多推荐
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoMoreTjActivity(id, "2");
            }
            break;
            case R.id.playpage_dashang: {// 打赏
                if (!userid.equals("")) {
                    if (shield.equals("1")) {
                        ActivityJumpControl.getInstance((Activity) context)
                                .gotoDaShangActivity(id, zhuboid);
                    } else {
                        Futil.showMessage(context, context.getString(R.string.shield_remind));
                    }
                } else {
                    Futil.showMessage(context, "请先登录");
                }

            }
            break;
            case R.id.details_more: {
                openDialog();
            }
            break;
            case R.id.details_playpage_dingshi:
                opendings();
                break;
            case R.id.dialog_gd_one:
                dialog.dismiss();
                openshare();
                break;
            case R.id.dialog_gd_two:
                dialog.dismiss();
                ActivityJumpControl.getInstance(this).gotoFeedbackActivity();
                finish();
                break;
            case R.id.dialog_gd_qx:
                dialog.dismiss();
                break;
            case R.id.dialog_gd_fanhui:
                finish();
                break;
        }
    }

    private ImageView down, details_more, audiomessage, liebiao;
    private TextView details_tv1, details_tv3, dianping_commentNumber,
            playpage_dashangnum;
    private RelativeLayout dianping_more;
    private ImageView playpage_userhead, playpager_Albumcover,
            playpage_dashang;
    private TextView playpage_username, playpage_Albumname,
            playpage_Albumblurb, playpage_playAmount, playpager_Albumepisode,
            playpage_audioblurb;
    private ImageView playpage_audio_cover;
    private Button dingyue;
    private WordWrapViewYuan details_playpage_label;
    private TextView pinglun_et;
    private LinearLayout dialog_ll;
    private LinearLayout dangshang_mess;
    private ImageView dianping_xihuan;
    private TextView playpage_dianping_send;
    private RelativeLayout moretj_rl;
    public static SeekBar playpage_progresss;
    public static ImageView playpage_playbt;

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
        //进度条
        playpage_progresss = (SeekBar) findViewById(R.id.playpage_progresss);

        playpage_progresss.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        if (mTimer != null) {
            if (timerTask != null) {
                timerTask.cancel();  //将原任务从队列中移除
            }
            timerTask = new MyTimerTask();
            // 每一秒触发一次
            mTimer.schedule(timerTask, 0, 1000);
        }

        //更多推荐
        moretj_rl = (RelativeLayout) findViewById(R.id.moretj_rl);
        moretj_rl.setOnClickListener(this);
        // 播放键
        playpage_playbt = (ImageView) findViewById(R.id.playpage_playbt);
        playpage_playbt.setOnClickListener(this);
        dianping_xihuan = (ImageView) findViewById(R.id.playpage_dianping_xihuan);
        dianping_xihuan.setOnClickListener(this);

        // ListView
        mtglistView = (ListView) findViewById(R.id.details_playpage_tuiguang);
        mtglistView.setFocusable(false);
        mtjlistView = (ListView) findViewById(R.id.details_playpage_tuijian);
        mtjlistView.setFocusable(false);
        mclistView = (ListView) findViewById(R.id.playpager_contentlist_view);
        mclistView.setFocusable(false);
        // 弹窗
        dialog_ll = (LinearLayout) findViewById(R.id.dialog_ll);
        // 打赏
        dangshang_mess = (LinearLayout) findViewById(R.id.dangshang_mess);
        playpage_dashangnum = (TextView) findViewById(R.id.playpage_dashangnum);
        playpage_dashang = (ImageView) findViewById(R.id.playpage_dashang);
        playpage_dashang.setOnClickListener(this);

        dingyue = (Button) findViewById(R.id.playpage_item_dingyue);
        dingyue.setOnClickListener(this);
        audiomessage = (ImageView) findViewById(R.id.playpage_audiomessage);
        audiomessage.setOnClickListener(this);
        liebiao = (ImageView) findViewById(R.id.playpage_liebiao);
        liebiao.setOnClickListener(this);

        playpage_dianping_send = (TextView) findViewById(R.id.playpage_dianping_send);
        playpage_dianping_send.setOnClickListener(this);
        pinglun_et = (TextView) findViewById(R.id.playpage_pinglun_et);
        pinglun_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable ed) {
                int length = ed.toString().trim().length();
                if (replyname != null) {
                    if (length < replyname.length()) {
                        equ = false;
                    } else {
                        equ = true;
                    }
                }

            }
        });

        findViewById(R.id.details_playpage_dingshi).setOnClickListener(this);
        down = (ImageView) findViewById(R.id.details_playpage_downiv1);
        down.setOnClickListener(this);
        details_tv1 = (TextView) findViewById(R.id.details_playpage_tv1);
        details_tv3 = (TextView) findViewById(R.id.details_playpage_tv3);

        dianping_commentNumber = (TextView) findViewById(R.id.details_dianping_commentNumber);
        dianping_more = (RelativeLayout) findViewById(R.id.dianping_more);
        dianping_more.setOnClickListener(this);

        playpage_userhead = (ImageView) findViewById(R.id.playpage_userhead);
        playpage_username = (TextView) findViewById(R.id.playpage_username);

        playpager_Albumcover = (ImageView) findViewById(R.id.playpager_Albumcover);
        playpage_Albumname = (TextView) findViewById(R.id.playpage_Albumname);
        playpage_playAmount = (TextView) findViewById(R.id.playpage_playAmount);
        playpage_Albumblurb = (TextView) findViewById(R.id.playpage_Albumblurb);
        playpager_Albumepisode = (TextView) findViewById(R.id.playpager_Albumepisode);

        playpage_audioblurb = (TextView) findViewById(R.id.playpage_audioblurb);
        playpage_audio_cover = (ImageView) findViewById(R.id.playpage_audio_cover);
        details_playpage_label = (WordWrapViewYuan) findViewById(R.id.details_playpage_label);

       findViewById(R.id.details_more).setOnClickListener(this);
        playpage_playbt.setSelected(false);
    }
    private AlertDialog dialog;

    private void openDialog() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_gengduo);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setAttributes(lp);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (ActionBar.LayoutParams.WRAP_CONTENT); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        TextView gd_one = (TextView) dialog.findViewById(R.id.dialog_gd_one);
        TextView gd_two = (TextView) dialog.findViewById(R.id.dialog_gd_two);
        TextView zbgd_fanhui = (TextView) dialog.findViewById(R.id.dialog_gd_fanhui);
        gd_one.setOnClickListener(this);
        gd_two.setOnClickListener(this);
        zbgd_fanhui.setOnClickListener(this);
        dialog.findViewById(R.id.dialog_gd_qx).setOnClickListener(this);
    }
    private void openshare() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("title").setMessage("message").create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_share);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle);  //添加动画

        dialog.findViewById(R.id.share_qq).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_kj).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_wx).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                sendWx(1, "");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_pyq).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWx(2, "");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_qx).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * @param i(1:分享到微信好友，2分享到朋友圈)
     */
    IWXAPI api;

    private void sendWx(final int i, String bitmapurl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = URLManager.share;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "这是消息标题";
        msg.description = "这是消息内容";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (i == 1) req.scene = SendMessageToWX.Req.WXSceneSession;//
        if (i == 2) req.scene = SendMessageToWX.Req.WXSceneTimeline;//
        api.sendReq(req);
        /*
        Picasso.with(context).load(bitmapurl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                // 设置想要的大小
                int newWidth = 99;
                int newHeight = 99;
                // 计算缩放比例
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // 取得想要缩放的matrix参数
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的图片
                Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                        matrix, true);
                msg.thumbData = Bitmap2Bytes(newBitMap);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                if (i == 1) req.scene = SendMessageToWX.Req.WXSceneSession;//
                if (i == 2) req.scene = SendMessageToWX.Req.WXSceneTimeline;//
                api.sendReq(req);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });*/
    }

    Tencent mTencent;

    public void share() {
        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
//        bundle.putString(SocialConstants.PARAM_TARGET_URL, "http://60.174.234.45:8080/headlines/article/appArticleInfo?aid="+aid+"&uid=0");
        bundle.putString(SocialConstants.PARAM_TARGET_URL, URLManager.share);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(SocialConstants.PARAM_TITLE, "这是消息标题");
        //分享的图片URL
//        bundle.putString(SocialConstants.PARAM_IMAGE_URL, imgUrl);
        //分享的消息摘要，最长50个字
        bundle.putString(SocialConstants.PARAM_SUMMARY, "这是消息内容");
        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(SocialConstants.PARAM_APPNAME, "慧思语");
        //标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(SocialConstants.PARAM_APP_SOURCE, "慧思语" + URLManager.QQappId);

        mTencent.shareToQQ(this, bundle, new IUiListener() {
            @Override
            public void onComplete(Object o) {
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    private static TextView time, time1, time2, time3, time4, time5;
    private static RelativeLayout timer_10, timer_cancel, timer_20, timer_30,
            timer_60, timer_90;
    private static ImageView checkedView, checkedView1, checkedView2,
            checkedView3, checkedView4, checkedView5;
    private static Handler mhandler;
    private static int ci = 0;

    public void opendings() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_timer);
        timer_cancel = (RelativeLayout) dialog.findViewById(R.id.timer_cancel);// 不开启
        timer_10 = (RelativeLayout) dialog.findViewById(R.id.timer_10);// 10分钟
        timer_20 = (RelativeLayout) dialog.findViewById(R.id.timer_20);// 20分钟
        timer_30 = (RelativeLayout) dialog.findViewById(R.id.timer_30);// 30分钟
        timer_60 = (RelativeLayout) dialog.findViewById(R.id.timer_60);// 60分钟
        timer_90 = (RelativeLayout) dialog.findViewById(R.id.timer_90);// 90分钟
        RelativeLayout timer_quxiao = (RelativeLayout) dialog
                .findViewById(R.id.timer_quxiao);// 关闭
        checkedView = (ImageView) dialog.findViewById(R.id.checkedView);
        checkedView1 = (ImageView) dialog.findViewById(R.id.checkedView1);
        checkedView2 = (ImageView) dialog.findViewById(R.id.checkedView2);
        checkedView3 = (ImageView) dialog.findViewById(R.id.checkedView3);
        checkedView4 = (ImageView) dialog.findViewById(R.id.checkedView4);
        checkedView5 = (ImageView) dialog.findViewById(R.id.checkedView5);
        time = (TextView) dialog.findViewById(R.id.time);// 倒计时
        time1 = (TextView) dialog.findViewById(R.id.time1);// 倒计时
        time2 = (TextView) dialog.findViewById(R.id.time2);// 倒计时
        time3 = (TextView) dialog.findViewById(R.id.time3);// 倒计时
        time4 = (TextView) dialog.findViewById(R.id.time4);// 倒计时
        time5 = (TextView) dialog.findViewById(R.id.time5);// 倒计时
        if (ci == 0) {
            gonevis(time, "", checkedView);
        }

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setAttributes(lp);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (LayoutParams.WRAP_CONTENT); // 高度设置为屏幕的0.7
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
        timer_cancel.setOnClickListener(new OnClickListener() {// 不开启

            @Override
            public void onClick(View v) {
                App.stopTime();
                gonevis(time, "", checkedView);
                ci = 0;
            }
        });
        timer_10.setOnClickListener(new OnClickListener() {// 10分钟

            @Override
            public void onClick(View v) {
                App.startTime(600000, mhandler, 1);
                ci = 1;
            }

        });
        timer_20.setOnClickListener(new OnClickListener() {// 20分钟

            @Override
            public void onClick(View v) {
                App.startTime(1200000, mhandler, 2);
                ci = 1;
            }
        });
        timer_30.setOnClickListener(new OnClickListener() {// 30分钟

            @Override
            public void onClick(View v) {
                App.startTime(1800000, mhandler, 3);
                ci = 1;
            }
        });
        timer_60.setOnClickListener(new OnClickListener() {// 60分钟

            @Override
            public void onClick(View v) {
                App.startTime(3600000, mhandler, 4);
                ci = 1;
            }
        });
        timer_90.setOnClickListener(new OnClickListener() {// 90分钟

            @Override
            public void onClick(View v) {
                App.startTime(5400000, mhandler, 5);
                ci = 1;
            }
        });
        timer_quxiao.setOnClickListener(new OnClickListener() {// 关闭

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mhandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 1) {
                    String obj = (String) msg.obj;
                    gonevis(time1, obj, checkedView1);
                } else if (msg.what == 2) {
                    String obj = (String) msg.obj;
                    gonevis(time2, obj, checkedView2);
                } else if (msg.what == 3) {
                    String obj = (String) msg.obj;
                    gonevis(time3, obj, checkedView3);
                } else if (msg.what == 4) {
                    String obj = (String) msg.obj;
                    gonevis(time4, obj, checkedView4);
                } else if (msg.what == 5) {
                    String obj = (String) msg.obj;
                    gonevis(time5, obj, checkedView5);
                } else if (msg.what == 0) {
                    String obj = (String) msg.obj;
                    gonevis(time, obj, checkedView);
                    ci = 0;
                }
            }

        };
    }

    private static ArrayList<TextView> list = new ArrayList<TextView>();
    private static ArrayList<ImageView> rblist = new ArrayList<ImageView>();

    public void gonevis(TextView tv, String obj, ImageView rl) {
        for (int i = 0; i < list.size(); i++) {
            list.remove(i).setText("");
        }
        tv.setVisibility(View.VISIBLE);
        tv.setText(obj);
        list.add(tv);
        for (int i = 0; i < rblist.size(); i++) {
            rblist.remove(i).setSelected(false);
        }
        rl.setSelected(true);
        rblist.add(rl);
    }

    private String parentId = "0", content = null, zhuboid = null;

    private void change(JSONObject audioAlbum, JSONObject user) {

        try {
            playpage_username.setText(user.getString("nickname"));
            zhuboid = user.getString("id");

            playpage_Albumname.setText(audioAlbum.getString("albumName"));
            String label = audioAlbum.getString("label");// 专辑标签
            String s1 = audioAlbum.getString("blurb");
            String s2 = audioAlbum.getString("episode");
            String s3 = audioAlbum.getString("playAmount");
            if (s3 != null && !s3.equals("") && !s3.equals("null")) {
                int p = Integer.parseInt(s3);
                if (p >= 10000) {
                    int pp = p / 10000;
                    BigDecimal bd = new BigDecimal(pp + "");
                    bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

                    playpage_playAmount.setText(bd + "万人");
                } else if (p < 10000 && p >= 0) {
                    playpage_playAmount.setText(p + "人");
                } else {
                    playpage_playAmount.setText("0人");
                }
            } else {
                playpage_playAmount.setText("0人");
            }

            if (s1 != null && s1 != "" && s1 != "null") {
                playpage_Albumblurb.setText(s1);
            } else {
                playpage_Albumblurb.setText("暂无简介");
            }
            if (s2 != null && s2 != "" && s2 != "null") {
                playpager_Albumepisode.setText(s2 + "集");
            } else {
                playpager_Albumepisode.setText("暂无");
            }
            // 分割字符串
            String a[] = label.split(",");
            if (label != null && !label.equals("") && !label.equals("null")) {
                details_playpage_label.removeAllViews();
                for (int i = 0; i < a.length; i++) {
                    final String value = a[i].trim();
                    TextView textview = (TextView) mInflater.inflate(
                            R.layout.details_playpage_label_item, null);
                    textview.setText(value);
                    textview.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityJumpControl.getInstance((Activity) context)
                                    .gotoXGAlbumActivity(value);
                        }
                    });
                    details_playpage_label.addView(textview);
                }
            } else {
                details_playpage_label.removeAllViews();
                TextView textview = (TextView) mInflater.inflate(
                        R.layout.details_playpage_label_item, null);
                textview.setText("暂无简介");
                details_playpage_label.addView(textview);
            }
        } catch (JSONException e) {
        }
    }

    String cover = null;
    String audioName = null;

    private void audio(JSONObject audio) throws JSONException {
        audioName = audio.getString("audioName");// 音频名称
        String playAmount = audio.getString("playAmount");// 播放数量

        String audioAlbumId = audio.getString("audioAlbumId");// 所属专辑id
        AlbumId = audioAlbumId;
        String blurb = audio.getString("blurb");// 音频-简介
        cover = audio.getString("cover");// 音频-封面
        mcover = cover;
        sname = audioName;
        mblurb = blurb;
        cover = URLManager.COVER_URL + cover;
        Picasso.with(context).load(cover).resize(400, 400)
                .placeholder(R.mipmap.details_img3)
                .error(R.mipmap.details_img3).into(playpage_audio_cover);

        String commentNumber = audio.getString("commentNumber");// 评论数量
        String id = audio.getString("id");// 音频id
        audioId = id;
        String path = audio.getString("path");// 音频路径
        playerpath = URLManager.AUDIO_URL + path;
        int pcode = App.getPlaycode();
        String apppath = App.getPlayerpath();
//
        if (playerpath.equals(apppath)) {
            onepath = true;
            if (pcode == 1) {
                playpage_playbt.setSelected(App.getPlaycode() == -1);
            } else if (pcode == 0) {
                playpage_playbt.setSelected(!(App.getPlaycode() == -1));
            } else {
                playpage_playbt.setSelected(!(App.getPlaycode() == -1));
            }
        } else {
            onepath = false;
            playpage_playbt.setSelected(false);
            playpage_progresss.setProgress(0);
        }
        String timeLong = audio.getString("timeLong");// 音频时长
        if (blurb != null && !blurb.equals("") && !blurb.equals("null")) {
            playpage_audioblurb.setText(blurb);
        } else {
            playpage_audioblurb.setText("暂无简介");
        }

        int c = Integer.parseInt(commentNumber);

        if (c > 5) {
            dianping_more.setVisibility(View.VISIBLE);
        } else {
            dianping_more.setVisibility(View.GONE);
        }
        dianping_commentNumber.setText(commentNumber);
        details_tv1.setText(audioName);
        if (playAmount != null && !playAmount.equals("")
                && !playAmount.equals("null")) {
            int p = Integer.parseInt(playAmount);
            if (p >= 10000) {
                int pp = p / 10000;
                BigDecimal bd = new BigDecimal(pp + "");
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

                details_tv3.setText(bd + "万人");
            } else if (p < 10000 && p >= 0) {
                details_tv3.setText(p + "人");
            } else {
                details_tv3.setText("0人");
            }
        } else {
            playpage_playAmount.setText("0人");
        }

    }

    private String AlbumId;
    private String audioId;

    private void gointo(String id) {
        startProgressDialog();
        String strUrl = URLManager.GetAudio;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("audio.id", id);
        map.put("uid", userid);
        Futil.xutils(strUrl, map, handler, URLManager.one);

    }

    private void cickPraise() {
        startProgressDialog();
        String strUrl = URLManager.ClickPraise;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("audioId", id);
        map.put("user.id", userid);
        Futil.xutils(strUrl, map, handler, URLManager.four);

    }

    private void into() {
        startProgressDialog();
        String Url = URLManager.AudioListByAlbumId;
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("audioAlbum.id", AlbumId);// 专辑id
        Futil.xutils(Url, map1, handler, URLManager.two);

    }

    private boolean equ = false;
    private String replyid = "0", replyname = null;
    private int po = -1;
    private int rn;

    private void etsend(String st) {
        if (equ) {
            rn = replyname.length();
            String substringL = SipUtils.substringL(st, rn);
            System.out.println("---substringL>>>>>>>>>>" + substringL);
            if (substringL.equals(replyname)) {
                parentId = replyid;
                content = SipUtils.substringR(st, rn);
                System.out.println("---substringR>>>>>>>>>>" + content);
                System.out.println("---replyid>>>>>>>>>>" + replyid);
            } else {
                parentId = "0";
                content = st;
            }
        } else {
            parentId = "0";
            content = st;
        }
        startProgressDialog();
        System.out.println("content--" + content);
        String strUrl = URLManager.Comment;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("audioComment.uid", userid);// 发评论人的id
        map.put("audioComment.audioId", audioId);// 评论的音频id
        map.put("audioComment.content", content);// 评论内容
        map.put("audioComment.parentId", parentId);// 对谁评论 没有为0
        map.put("audioComment.ip", URLManager.ip);//
        Futil.xutils(strUrl, map, handler, URLManager.three);
        pinglun_et.setText("");
    }

    private int indicatorWidth;// 每个标签所占的宽度

    private void setuserPay(JSONArray userPayList) {
        dangshang_mess.removeAllViews();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        indicatorWidth = dm.widthPixels / 8;
        dangshang_mess = (LinearLayout) findViewById(R.id.dangshang_mess);
        for (int i = 0; i < userPayList.length(); i++) {
            LinearLayout child = (LinearLayout) mInflater.inflate(
                    R.layout.details_playpage_item_dashang, null);
            RoundedImageView iv = (RoundedImageView) child
                    .findViewById(R.id.playpage_dashangimg);
            TextView tv = (TextView) child.findViewById(R.id.playpage_money);
            child.setLayoutParams(new LayoutParams(indicatorWidth,
                    LayoutParams.MATCH_PARENT));
            JSONObject jsonObject = null;
            try {
                jsonObject = userPayList.getJSONObject(i);

            } catch (JSONException e) {
            }
            try {
                String money = jsonObject.getString("money");
                tv.setText("￥" + money);
            } catch (JSONException e) {
                tv.setText("￥0");
            }

            try {
                JSONObject user = jsonObject.getJSONObject("user");
                String type = jsonObject.getString("headStatus");
                String head = user.getString("head");
                if (!type.equals("http")) {
                    head = URLManager.Head_URL + head;
                }
                int d = BeanTu.dip2px(this, 25);
                Picasso.with(this).load(head).resize(d, d)
                        .placeholder(R.mipmap.playpage_dianping_img)
                        .error(R.mipmap.playpage_dianping_img).into(iv);
            } catch (JSONException e) {
                iv.setImageResource(R.mipmap.playpage_dianping_img);
            }

            dangshang_mess.addView(child);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 10086) {
                int position = (int) msg.obj;
                if (po != position) {
                    String topuid = arraycList.get(position).getId();
                    if (!topuid.equals(userid)) {
                        replyid = arraycList.get(position).getId();
                        replyname = arraycList.get(position).getNickname();
                        replyname = "回复   " + replyname + ":";
                        pinglun_et.setText(replyname);
                    } else {
                        Futil.showMessage(context, "不能回复自己");
                    }
                }
            } else if (msg.what == URLManager.one) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    String userPayCount = object.getString("userPayCount");
                    // 多少人打赏
                    playpage_dashangnum.setText("已有" + userPayCount + "人打赏");
                } catch (Exception e) {
                    e.printStackTrace();
                    playpage_dashangnum.setText("已有0人打赏");
                }
                try {
                    String userPraise = object.getString("userPraise");
                    System.out.println("userPraise>>>" + userPraise);
                    if (userPraise.equals("1")) {
                        dianping_xihuan.setSelected(true);
                    } else {
                        dianping_xihuan.setSelected(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dianping_xihuan.setSelected(false);
                }
                try {
                    JSONArray userPayList = object.getJSONArray("userPayList");
                    // 打赏信息
                    setuserPay(userPayList);
                } catch (Exception e) {
                }
                try {
                    JSONObject audiodata = object.getJSONObject("audio");
                    // 目标的音频信息
                    audio(audiodata);
                } catch (Exception e) {
                }
                try {// 评论列表
                    JSONArray audioContentList = object
                            .getJSONArray("audioCommentContentList");
                    arraycList= new ArrayList<ContentList>();
                    if (audioContentList.length() > 6) {
                        for (int i = 0; i < 5; i++) {
                            ContentList clist = new ContentList();
                            JSONObject data = audioContentList.getJSONObject(i);

                            clist.setAddtime(data.getString("addtime"));
                            clist.setContent(data.getString("content"));
                            clist.setHead(data.getString("head"));
                            clist.setId(data.getString("id"));
                            clist.setNickname(data.getString("nickname"));// 评论人
                            clist.setReplyname(data.getString("replyname"));// 对谁评论
                            arraycList.add(clist);
                        }
                    } else {
                        for (int i = 0; i < audioContentList.length(); i++) {
                            ContentList clist = new ContentList();
                            JSONObject data = audioContentList.getJSONObject(i);

                            clist.setAddtime(data.getString("addtime"));
                            clist.setContent(data.getString("content"));
                            clist.setHead(data.getString("head"));
                            clist.setId(data.getString("id"));
                            clist.setNickname(data.getString("nickname"));// 评论人
                            clist.setReplyname(data.getString("replyname"));// 对谁评论
                            arraycList.add(clist);
                        }
                    }
                    cadapter = new ContentListAdapter(context, arraycList, handler);
                    mclistView.setAdapter(cadapter);

                    cadapter.notifyDataSetChanged();

                } catch (Exception e) {
                }
                try {
                    // albumList.clear();
                    JSONArray audioList = object.getJSONArray("audioAlbumList");
                    albumList = new ArrayList<Album>();
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
                    mtjlistView.setAdapter(tadapter);

                    tadapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
                try {
                    JSONObject audioAlbum = object.getJSONObject("audioAlbum");

                    JSONObject user = object.getJSONObject("user");
                    change(audioAlbum, user);

                    String userSubscribe = object.getString("userSubscribe");
                    if (userSubscribe.equals("未订阅")) {
                        dingyue.setText("订阅");
                        dingyue.setClickable(true);
                    } else {
                        dingyue.setText("已订阅");
                        dingyue.setClickable(false);
                    }

                } catch (JSONException e) {
                }
            } else if (msg.what == URLManager.two) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    JSONArray audioList = object.getJSONArray("audioList");
                    dialog(audioList);
                    stopProgressDialog();
                } catch (Exception e) {
                }

            } else if (msg.what == URLManager.three) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    String code = object.getString("code");
                    String message = object.getString("message");
                    Futil.showMessage(context, message);
                    if (code.equals("2")) {
                        gointo(id);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.four) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    String code = object.getString("code");
                    String message = object.getString("message");
                    if (code.equals("2")) {
                        if (message.contains("取消")) {
                            dianping_xihuan.setSelected(false);
                        } else {
                            dianping_xihuan.setSelected(true);
                        }
                        Futil.showMessage(context, message);
                    } else {
                        Futil.showMessage(context, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }

    };
    public static Handler muhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 10086) {
                playpage_playbt.setSelected(false);
                playpage_progresss.setProgress(0);
            }
        }

    };
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

    private final String TAG = "DetailsPlayActivity";

    private RelativeLayout mViewHolder = null;

    private static final String POWER_LOCK = "VideoViewPlayingActivity";

    private String[] mAvailableResolution = null;


    private void startplayer() {
        MainActivity.mVideoSource = playerpath;
        String apppath = App.getPlayerpath();
        if (!onepath) {
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

            mid = id;
            maudioAlbumId = AlbumId;
            playcode = 0;
            loadkey();
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

        if (!playerpath.equals(apppath)) {// 是否在播放---判断url是否相同
            playerpath1 = playerpath;
            App.setPlayerpath(playerpath1);
            AudioList al = new AudioList();
            mid = id;
            maudioAlbumId = AlbumId;
            onepath = true;
            al.setPath(playerpath);
            al.setBlurb(mblurb);
            al.setAudioName(sname);
            al.setCover(mcover);
            al.setId(mid);
            al.setAudioAlbumId(maudioAlbumId);
            App.setContextAudioList(al);
            App.setIsstation(0);
            App.setPlaycode(playcode);
        } else {


        }
    }

    /**
     * 注册listener
     */
    // 计时器
    private MyTimerTask timerTask;
    private Timer mTimer = new Timer(); // 计时器


    // 计时器
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (MainActivity.mVV == null)
                return;
            if (MainActivity.mVV.isPlaying() && playpage_progresss.isPressed() == false && DetailsPlayActivity.onepath) {
                timehandler.sendEmptyMessage(0); // 发送消息
            }
        }
    }

    private int num = 0;
    private int n = 0;
    Handler timehandler = new Handler() {
        public void handleMessage(Message msg) {
            long position = MainActivity.mVV.getCurrentPosition();
            long duration = MainActivity.mVV.getDuration();
            if (duration > 0) {
                num++;
                if (num != 0) {
                    // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
                    long pos = playpage_progresss.getMax() * position / duration;
                    if (pos == 0) {
                        num = -1;
                    }
                    playpage_progresss.setProgress((int) pos);
                }
            }
        }
    };

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = (int) (progress * MainActivity.mVV.getDuration()
                    / seekBar.getMax());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
//            if (App.getPlaycode() != -1) {
            MainActivity.mVV.seekTo(progress);
            System.out.println("mediaPlayer.seekTo>>>>" + progress);
//            }
        }
    }

    /**
     * 存播放记录
     */
    private void loadkey() {
        ArrayList<String> sKey = Futil.loadKeyArray(context, "2");
        String key = maudioAlbumId + "_" + mid;
        sKey.remove(key);
        sKey.add(key);
        Futil.saveKeyArray(context, sKey, "2");
        Futil.saveValue(context, key + "id", mid);
        Futil.saveValue(context, key + "audioName", sname);
        Futil.saveValue(context, key + "cover", mcover);
        Futil.saveValue(context, key + "path", playerpath);
        Futil.saveValue(context, key + "blurb", mblurb);
        Futil.saveValue(context, key + "audioAlbumId", maudioAlbumId);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        final String tim = sdf.format(date);
        Futil.saveValue(context, key + "addTime", tim);
    }

    private void dialog(JSONArray audioList) {
        try {

            dialog = new AlertDialog.Builder(context).create();
            dialog.show();
            dialog.setContentView(R.layout.dialog_liebiao);
            TextView tv1 = (TextView) dialog
                    .findViewById(R.id.dialog_content);
            dialog_ll = (LinearLayout) dialog.findViewById(R.id.dialog_ll);
            if (audioList.length() > 0) {
                tv1.setVisibility(View.GONE);

                for (int i = 0; i < audioList.length(); i++) {
                    JSONObject data = audioList.getJSONObject(i);
                    final String id = data.getString("id");
                    final String bumId = data.getString("audioAlbumId");
                    TextView dtv = new TextView(context);
                    dtv.setText(data.getString("audioName"));
                    dtv.setPadding(15, 10, 15, 10);
                    dtv.setTextSize(20);
                    dtv.setTextColor(Color.parseColor("#000000"));
                    dtv.setId(data.getInt("id"));
                    dtv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mViewHolder.removeAllViews();
                            ActivityJumpControl.getInstance(
                                    (Activity) context)
                                    .gotoDetailsPlayActivity(id);

                        }
                    });
                    dialog_ll.addView(dtv);

                }
            } else {
                tv1.setVisibility(View.VISIBLE);
            }
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setAttributes(lp);

            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
            p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
            dialogWindow.setAttributes(p);
            RelativeLayout rl1 = (RelativeLayout) dialog
                    .findViewById(R.id.dialog_quxiao);
            rl1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
        }
        stopProgressDialog();
    }

}
