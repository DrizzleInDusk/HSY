package com.zm.hsy.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.util.ActivityJumpControl;
/** 设置页 */
public class SettingActivity extends Activity implements OnClickListener {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context = this;
        findview();
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

    public static boolean isGprs = false;
    public static boolean isWifi = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.setting_clock:// 特色闹铃
                ActivityJumpControl.getInstance(SettingActivity.this)
                        .gotoAlarmActivity();
                break;
            case R.id.setting_dingshi:// 定时关闭
                opendialog();
                break;
            case R.id.setting_duandian:// 断点续听
                if (setting_duandian.isSelected()) {
                    setting_duandian.setSelected(false);
                } else {
                    setting_duandian.setSelected(true);
                }
                break;
            case R.id.setting_liuliang:// 流量下载
                if (isGprs == true) {
                    isGprs = false;
                    setting_liuliang.setSelected(false);
                } else {
                    isGprs = true;
                    setting_liuliang.setSelected(true);
                }
                break;
            case R.id.setting_clear:// 清理空间
                ActivityJumpControl.getInstance(SettingActivity.this)
                        .gotoSettingCleanActivity();
                break;
            case R.id.setting_tuisong:// 推送设置
                ActivityJumpControl.getInstance(SettingActivity.this)
                        .gotoTuisongActivity();
                break;
            case R.id.setting_yinsi:// 隐私设置
                ActivityJumpControl.getInstance(SettingActivity.this)
                        .gotoYinsiActivity();
                break;
            case R.id.setting_help:// 帮助中心
                ActivityJumpControl.getInstance(SettingActivity.this)
                        .gotoBangzhuActivity();
                break;
            case R.id.setting_guanyu:// 关于
                ActivityJumpControl.getInstance(SettingActivity.this)
                        .gotoGuanYuActivity();
                break;
            case R.id.logout:// 注销登陆
                setaliasandtag();
                break;

            case R.id.back_top:
                finish();
                break;
        }
    }

    private void setaliasandtag() {

        JPushInterface.setAlias(context, "", new TagAliasCallback() {

            @Override
            public void gotResult(int code, String alias, Set<String> tags) {
                switch (code) {
                    case 0:
                        Futil.saveValue(context, "hasAlias", "0");
                        break;

                    case 6002:


                        break;
                    default:

                        break;
                }
            }
        });

        Set<String> arg1 = new HashSet<String>();
        arg1.add("");
        JPushInterface.setTags(context, arg1, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String alias, Set<String> tags) {
                switch (code) {
                    case 0:
                        Futil.saveValue(context, "hasTags", "0");
                        break;
                    case 6002:
                        break;
                    default:
                        break;
                }
            }
        });

        Futil.romveValue(SettingActivity.this, "userid", "4");
        String value = Futil.getValue(context, "userid");
        Futil.saveValue(context, "shield", "1");
        System.out.println("----" + value);
        finish();
    }

    private ImageView back_top, logout;
    private TextView setting_timer;
    private LinearLayout ll1, ll2, ll3;
    private RelativeLayout setting_clock, setting_dingshi, setting_duandian,
            setting_liuliang, setting_clear, setting_tuisong, setting_yinsi,
            setting_help, setting_guanyu;

    private void findview() {
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);

        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(this);// 注销登陆

        setting_clock = (RelativeLayout) findViewById(R.id.setting_clock);
        setting_clock.setOnClickListener(this);// 闹铃
        setting_dingshi = (RelativeLayout) findViewById(R.id.setting_dingshi);
        setting_dingshi.setOnClickListener(this);

        setting_duandian = (RelativeLayout) findViewById(R.id.setting_duandian);
        setting_duandian.setOnClickListener(this);
        setting_liuliang = (RelativeLayout) findViewById(R.id.setting_liuliang);
        setting_liuliang.setOnClickListener(this);
        setting_clear = (RelativeLayout) findViewById(R.id.setting_clear);
        setting_clear.setOnClickListener(this);
        setting_tuisong = (RelativeLayout) findViewById(R.id.setting_tuisong);
        setting_tuisong.setOnClickListener(this);
        setting_yinsi = (RelativeLayout) findViewById(R.id.setting_yinsi);
        setting_yinsi.setOnClickListener(this);

        setting_help = (RelativeLayout) findViewById(R.id.setting_help);
        setting_help.setOnClickListener(this);
        setting_guanyu = (RelativeLayout) findViewById(R.id.setting_guanyu);
        setting_guanyu.setOnClickListener(this);

        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

    }

    private static AlertDialog dialog;
    private static TextView time, time1, time2, time3, time4, time5;
    private static RelativeLayout timer_10, timer_cancel, timer_20, timer_30,
            timer_60, timer_90;
    private static ImageView checkedView, checkedView1, checkedView2,
            checkedView3, checkedView4, checkedView5;
    private static Handler handler;
    private static int ci = 0;

    public void opendialog() {
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
        p.height = (int) (ViewGroup.LayoutParams.WRAP_CONTENT); // 高度设置为屏幕的0.7
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
                App.startTime(600000, handler, 1);
                ci = 1;
            }

        });
        timer_20.setOnClickListener(new OnClickListener() {// 20分钟

            @Override
            public void onClick(View v) {
                App.startTime(1200000, handler, 2);
                ci = 1;
            }
        });
        timer_30.setOnClickListener(new OnClickListener() {// 30分钟

            @Override
            public void onClick(View v) {
                App.startTime(1800000, handler, 3);
                ci = 1;
            }
        });
        timer_60.setOnClickListener(new OnClickListener() {// 60分钟

            @Override
            public void onClick(View v) {
                App.startTime(3600000, handler, 4);
                ci = 1;
            }
        });
        timer_90.setOnClickListener(new OnClickListener() {// 90分钟

            @Override
            public void onClick(View v) {
                App.startTime(5400000, handler, 5);
                ci = 1;
            }
        });
        timer_quxiao.setOnClickListener(new OnClickListener() {// 关闭

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        handler = new Handler() {
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

}
