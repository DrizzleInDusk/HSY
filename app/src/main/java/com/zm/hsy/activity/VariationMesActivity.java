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
/*** 活动详细页 */
public class VariationMesActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    JSONObject ac = obj.getJSONObject("activity");
                    title = ac.getString("title");
                    status = ac.getString("status");
                    blurb = ac.getString("blurb");//简介
                    content = ac.getString("content");//内容
                    conPic = ac.getString("conPic");//内容图
                    conVideo = ac.getString("conVideo");//内容视频
                    cost = ac.getString("cost");//报名费用
                    quota = ac.getString("quota");//名额
                    rule = ac.getString("rule");//规则
                    score = ac.getString("score");//积分限制
                    String cover = ac.getString("propaganda");
                    cover = URLManager.VariationCOVER_URL + cover;
                    if (blurb.equals("")) {
                        blurb = "活动暂无简介";
                    }
                    if (content.equals("")) {
                        content = "暂无详细内容描述";
                    }
                    if (cost.equals("") || cost.equals("0")) {
                        cost = "不需要报名费";
                    }
                    if (quota.equals("") || quota.equals("0")) {
                        quota = "活动不限制名额";
                    }
                    if (rule.equals("")) {
                        rule = "活动没有规则限制";
                    }
                    if (score.equals("")) {
                        score = "活动不限制积分";
                    }
                    hdmes_jianjie.setText(blurb);
                    hdmes_neirong.setText(content);
                    hdmes_minge.setText("每人"+cost+"RMB");
                    hdmes_feiyong.setText(quota+"人");
                    hdmes_xzjf.setText("不低于"+score+"积分");
                    hdmes_guize.setText(rule);
                    hdmes_title.setText(title);
                    if (!conPic.equals("") && !conPic.equals("null")) {
                        hdmes_neirongimg.setVisibility(View.VISIBLE);
                        Picasso.with(context).load(cover).error(R.mipmap.vriation_item).into(hdmes_neirongimg);
                    } else {
                        hdmes_neirongimg.setVisibility(View.GONE);
                    }
                    if (!conVideo.equals("") && !conVideo.equals("null")) {
                        view_holder.setVisibility(View.VISIBLE);
                        /**
                         *创建BVideoView和BMediaController
                         */
                        view_holder.addView(MainActivity.mVV);
                        App.setPlayerpath(conVideo);

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
                        view_holder.setVisibility(View.GONE);
                    }
                    Picasso.with(context).load(cover).error(R.mipmap.vriation_item).into(hdmes_img);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            stopProgressDialog();
        }

    };

    private String userid, variationid, title = "", status = "", blurb = "", content = "", conPic = "", conVideo = "", cost = "", quota = "", rule = "", score = "";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variationmes);
        context = this;
        Intent i = this.getIntent();
        variationid = i.getStringExtra("variationid");
        userid = Futil.getValue(context, "userid");
        findview();
        getActivityInfo();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
        super.onResume();
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
        view_holder.removeAllViews();
        App.setIsstation(-1);
        App.setPlayerpath(null);
        App.setPlaycode(-1);
        if ((MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
            MainActivity.mLastPos = (int) MainActivity.mVV.getCurrentPosition();
            /*停止播放*/
            MainActivity.mVV.stopPlayback();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_top:
                finish();
                break;

        }
    }

    private ImageView hdmes_img, hdmes_neirongimg;
    private RelativeLayout view_holder;
    private TextView hdmes_title, hdmes_joinhd, hdmes_jianjie, hdmes_neirong, hdmes_minge, hdmes_feiyong, hdmes_xzjf, hdmes_guize;

    private void findview() {
        findViewById(R.id.back_top).setOnClickListener(this);

        hdmes_title = (TextView) findViewById(R.id.hdmes_title);
        hdmes_img = (ImageView) findViewById(R.id.hdmes_img);
        hdmes_neirongimg = (ImageView) findViewById(R.id.hdmes_neirongimg);
        view_holder = (RelativeLayout) findViewById(R.id.view_holder);
        hdmes_jianjie = (TextView) findViewById(R.id.hdmes_jianjie);
        hdmes_neirong = (TextView) findViewById(R.id.hdmes_neirong);
        hdmes_feiyong = (TextView) findViewById(R.id.hdmes_feiyong);
        hdmes_xzjf = (TextView) findViewById(R.id.hdmes_xzjf);
        hdmes_minge = (TextView) findViewById(R.id.hdmes_minge);
        hdmes_guize = (TextView) findViewById(R.id.hdmes_guize);

    }

    private void getActivityInfo() {
        startProgressDialog();
        String strUrl = URLManager.getActivityInfo;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("activity.id", variationid);
        Futil.xutils(strUrl, map, handler, URLManager.one);
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

}
