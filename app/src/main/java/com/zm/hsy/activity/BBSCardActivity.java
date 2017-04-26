package com.zm.hsy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.adapter.ReplyLzTopicAdapter;
import com.zm.hsy.adapter.ReplyTopicAdapter;
import com.zm.hsy.entity.TopicContentList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
import com.zm.hsy.util.SipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 社区帖子详情页 */
@SuppressLint("SimpleDateFormat")
public class BBSCardActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    if (code.equals("1")) {
                        try {
                            String message = obj.getString("message");
                            if (message.contains("只扣一次")) {
                                opendialog(message);
                            } else {
                                Futil.showMessage(context, "" + message);
                                finish();
                            }
                        } catch (JSONException mes) {
                            mes.printStackTrace();
                            Log.i("帖子返回信息", "" + obj);
                        }
                    } else {
                        try {
                            JSONObject ct = obj
                                    .getJSONObject("communityTopicRank");
                            topic_reply_ll.setVisibility(View.VISIBLE);
                            JSONObject ctobj = ct.getJSONObject("communityTopic");
                            uid = ctobj.getString("uid");
                            reid = ctobj.getString("id");
                            communityid = ctobj.getString("communityId");
                            changeview(ct);

                        } catch (JSONException e1) {
                            topic_reply_ll.setVisibility(View.GONE);
                        }
                        try {
                            JSONArray tparray = obj
                                    .getJSONArray("topicContentList");
                            for (int i = 0; i < tparray.length(); i++) {
                                TopicContentList tp = new TopicContentList();
                                JSONObject tpdata = tparray.getJSONObject(i);
                                tp.setContent(tpdata.getString("content"));
                                tp.setHead(tpdata.getString("head"));
                                tp.setId(tpdata.getString("id"));
                                tp.setUid(tpdata.getString("uid"));
                                tp.setName(tpdata.getString("name"));
                                tp.setHeadStatus(tpdata.getString("headStatus"));
                                tp.setPicture(tpdata.getString("picture"));
                                tp.setTime(tpdata.getString("time"));
                                tp.setYycontent(tpdata.getString("yycontent"));
                                tp.setYyname(tpdata.getString("yyname"));
                                bbsList.add(tp);
                            }
                            topic_reply_ll.setVisibility(View.VISIBLE);
                            rtdapter = new ReplyTopicAdapter(context, bbsList,
                                    uid, handler);
                            Log.i("BBSCarduid", "" + uid);
                            rtdapter.notifyDataSetChanged();
                            all_viewp.setAdapter(rtdapter);

                            lzrtdapter = new ReplyLzTopicAdapter(context,
                                    bbsList, uid, handler);
                            lzrtdapter.notifyDataSetChanged();
                            lz_viewp.setAdapter(lzrtdapter);
                            all_viewp.setVisibility(View.VISIBLE);
                            lz_viewp.setVisibility(View.GONE);
                        } catch (JSONException e2) {
                            all_viewp.setVisibility(View.GONE);
                            lz_viewp.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else if (msg.what == URLManager.two) {// 回帖
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        String message = obj.getString("message");
                        Futil.showMessage(context, "" + message);
                        replyid = "0";
                        replyname = null;
                        reply_et.setText("");
                        po = -1;
                        onRestart();
                    } else {
                        String message1 = obj.getString("message");
                        Futil.showMessage(context, "" + message1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (msg.what == URLManager.three) {// 确认消费
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        try {
                            JSONObject ct = obj
                                    .getJSONObject("communityTopicRank");
                            topic_reply_ll.setVisibility(View.VISIBLE);
                            JSONObject ctobj = ct.getJSONObject("communityTopic");
                            uid = ctobj.getString("uid");
                            reid = ctobj.getString("id");
                            communityid = ctobj.getString("communityId");
                            System.out.println("communityid" + communityid);
                            changeview(ct);

                        } catch (JSONException e1) {
                            topic_reply_ll.setVisibility(View.GONE);
                        }
                        try {
                            JSONArray tparray = obj
                                    .getJSONArray("topicContentList");
                            for (int i = 0; i < tparray.length(); i++) {
                                TopicContentList tp = new TopicContentList();
                                JSONObject tpdata = tparray.getJSONObject(i);
                                tp.setContent(tpdata.getString("content"));
                                tp.setHead(tpdata.getString("head"));
                                tp.setId(tpdata.getString("id"));
                                tp.setUid(tpdata.getString("uid"));
                                tp.setName(tpdata.getString("name"));
                                tp.setHeadStatus(tpdata.getString("headStatus"));
                                tp.setPicture(tpdata.getString("picture"));
                                tp.setTime(tpdata.getString("time"));
                                tp.setYycontent(tpdata.getString("yycontent"));
                                tp.setYyname(tpdata.getString("yyname"));
                                bbsList.add(tp);
                            }
                            topic_reply_ll.setVisibility(View.VISIBLE);
                            rtdapter = new ReplyTopicAdapter(context, bbsList,
                                    uid, handler);
                            rtdapter.notifyDataSetChanged();
                            all_viewp.setAdapter(rtdapter);
                            lzrtdapter = new ReplyLzTopicAdapter(context,
                                    bbsList, uid, handler);
                            lzrtdapter.notifyDataSetChanged();
                            lz_viewp.setAdapter(lzrtdapter);

                            all_viewp.setVisibility(View.VISIBLE);
                            lz_viewp.setVisibility(View.GONE);
                        } catch (JSONException e2) {
                            all_viewp.setVisibility(View.GONE);
                            lz_viewp.setVisibility(View.GONE);
                        }
                    } else {
                        String message = obj.getString("message");
                        Futil.showMessage(context, "" + message);
                        finish();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (msg.what == URLManager.four) {// 点击回复
                int position = (int) msg.obj;
                if (po != position) {
                    String topuid = bbsList.get(position).getUid();
                    if (!topuid.equals(userid)) {
                        replyid = bbsList.get(position).getId();
                        System.out.println("replyid>>>" + replyid);
                        replyname = bbsList.get(position).getName();
                        replyname = "回复   " + replyname + ":";
                        reply_et.setText(replyname);
                    } else {
                        Futil.showMessage(context, "不能回复自己");
                    }
                }
            }
            stopProgressDialog();
        }

    };
    private AlertDialog dialog;
    private ReplyTopicAdapter rtdapter;
    private ReplyLzTopicAdapter lzrtdapter;
    private ArrayList<TopicContentList> bbsList;
    private String topicId, userid, communityid;
    private ListView all_viewp, lz_viewp;
    private String uid, reid;
    private String replyid = "0", replyname = null;
    private int po = -1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_topic_reply);
        bbsList = new ArrayList<TopicContentList>();
        context = this;
        Intent i = this.getIntent();
        topicId = i.getStringExtra("id");
        userid = Futil.getValue(context, "userid");
        all_viewp = (ListView) findViewById(R.id.reply_all_viewp);
        all_viewp.setFocusable(false);
        lz_viewp = (ListView) findViewById(R.id.reply_lz_viewp);
        lz_viewp.setFocusable(false);
        findview();
        gointo();
    }

    @Override
    protected void onRestart() {
        bbsList.clear();
        gointo();
        super.onRestart();
    }

    private LinearLayout reply_ll, topic_reply_ll;
    private TextView user_nickname, item_time, topic_title, topic_content,
            topic_count;
    private static TextView chongxinbofang;
    private RoundedImageView bbs_user_head;
    private ImageView topic_picture1, topic_picture2, topic_picture3;
    private ImageView back_top, reply_send;
    private LinearLayout topic_ll;
    private TextView reply_et;
    private TextView toptv_all, toptv_lz;
    private ImageView bbs_topic_pinglun;
    private LinearLayout res_ll;
    private ImageView res_image;
    private RelativeLayout mViewHolder = null;

    private void findview() {
        res_ll = (LinearLayout) findViewById(R.id.res_ll);
        res_image = (ImageView) findViewById(R.id.res_image);
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        chongxinbofang = (TextView) findViewById(R.id.chongxinbofang);
        chongxinbofang.setOnClickListener(this);
        /**
         *创建BVideoView和BMediaController
         */
        mViewHolder.addView(MainActivity.mVV);
        topic_reply_ll = (LinearLayout) findViewById(R.id.topic_reply_ll);
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);
        toptv_all = (TextView) findViewById(R.id.reply_toptv_all);
        toptv_all.setOnClickListener(this);
        toptv_lz = (TextView) findViewById(R.id.reply_toptv_lz);
        toptv_lz.setOnClickListener(this);

        reply_et = (TextView) findViewById(R.id.reply_et);
        reply_et.addTextChangedListener(new TextWatcher() {

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
        reply_send = (ImageView) findViewById(R.id.reply_send_lv);
        reply_send.setOnClickListener(this);

        topic_ll = (LinearLayout) findViewById(R.id.topic_ll);
        bbs_user_head = (RoundedImageView) findViewById(R.id.bbs_user_head);
        user_nickname = (TextView) findViewById(R.id.bbs_user_nickname);
        item_time = (TextView) findViewById(R.id.tv_item_time);
        topic_title = (TextView) findViewById(R.id.bbs_topic_title);
        topic_content = (TextView) findViewById(R.id.bbs_topic_content);
        topic_count = (TextView) findViewById(R.id.bbs_topic_count);
        bbs_topic_pinglun = (ImageView) findViewById(R.id.bbs_topic_pinglun);
        findViewById(R.id.reply_tu_lv).setOnClickListener(this);
        reply_ll = (LinearLayout) findViewById(R.id.reply_ll);
        topic_picture1 = (ImageView) findViewById(R.id.bbs_topic_picture1);
        topic_picture2 = (ImageView) findViewById(R.id.bbs_topic_picture2);
        topic_picture3 = (ImageView) findViewById(R.id.bbs_topic_picture3);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.reply_tu_lv:
                picopenDialog();
                break;
            case R.id.chongxinbofang:
                MainActivity.mVideoSource = vpath;
                App.setPlayerpath(vpath);
//                    App.setPlayerpath("http://192.168.1.79:8080/hsyfm/fmVideo/20160615100846824907548.3gp");

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
                break;
            case R.id.reply_toptv_all:
                toptv_all.setTextColor(Color.parseColor("#1abc9c"));
                toptv_all.setBackgroundResource(R.drawable.layout_yuanjiao);
                toptv_lz.setTextColor(Color.parseColor("#FFFFFF"));
                toptv_lz.setBackgroundResource(R.color.appblue);
                all_viewp.setVisibility(View.VISIBLE);
                lz_viewp.setVisibility(View.GONE);
                break;
            case R.id.reply_toptv_lz:
                toptv_all.setTextColor(Color.parseColor("#FFFFFF"));
                toptv_all.setBackgroundResource(R.color.appblue);
                toptv_lz.setTextColor(Color.parseColor("#1abc9c"));
                toptv_lz.setBackgroundResource(R.drawable.layout_yuanjiao);

                all_viewp.setVisibility(View.GONE);
                lz_viewp.setVisibility(View.VISIBLE);
                break;
            case R.id.reply_send_lv:
                String shield = Futil.getValue(context, "shield");
                if (shield.equals("1")) {
                    String st = reply_et.getText().toString().trim();
                    if (!st.equals("") && st != null) {
                        etsend(st);
                    } else {
                        Futil.showMessage(context, "请输入回复内容");
                    }
                } else {
                    Futil.showMessage(context, context.getString(R.string.shield_remind));
                }

                break;
        }
    }

    private String content = null;
    private String parentId = null;
    private boolean equ = false;
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

        String strUrl = URLManager.AddCommunityTopic;
        HashMap<String, String> map = new HashMap<String, String>();
        HashMap<String, File> mapfile = new HashMap<String, File>();
        map.put("user.id", userid);// 发评论人的id
        map.put("community.id", communityid);// 评论的社区
        map.put("communityTopic.topicId", topicId);// 评论的帖子
        map.put("communityTopic.content", content);// 评论内容
        map.put("communityTopic.parentId", parentId);// 对谁评论 没有为0
        map.put("communityTopic.score", "0");// 固定为0
        if (imgFile.exists()) {
            mapfile.put("pic", imgFile);
            map.put("type", "png");
        }
        Futil.xutils(strUrl, map, mapfile, handler, URLManager.two);
    }

    @Override
    protected void onDestroy() {
        onbbscardact = false;
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

    private String vpath;

    private void changeview(JSONObject ct) {
        try {
            // final String bbsuserid = ct.getString("uid");
            String head = ct.getString("head");
            String count = ct.getString("count");
            final String nickname = ct.getString("nickname");
            String type = ct.getString("headStatus");

            JSONObject obj = ct.getJSONObject("communityTopic");
            String addTime = obj.getString("addTime");
            String content = obj.getString("content");
            String title = obj.getString("title");
            String picture = obj.getString("picture");
            final String bbsuserid = obj.getString("uid");
            bbs_user_head.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ActivityJumpControl.getInstance((Activity) context)
                            .gotoZhuboActivity(bbsuserid);

                }
            });
            try {
                String attachment = obj.getString("attachment");
                if (!attachment.equals("") && attachment != null && !attachment.equals("null")) {
                    res_ll.setVisibility(View.VISIBLE);
                    int dianindex = attachment.indexOf(".");
                    attachment = URLManager.TopicCOVER_URL + attachment;
                    if (dianindex != -1) {
                        vpath = attachment;
                        res_image.setVisibility(View.GONE);
                        mViewHolder.setVisibility(View.VISIBLE);
                        chongxinbofang.setVisibility(View.GONE);
                        MainActivity.mVideoSource = vpath;
                        App.setPlayerpath(attachment);
//                    App.setPlayerpath("http://192.168.1.79:8080/hsyfm/fmVideo/20160615100846824907548.3gp");

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
                        res_image.setVisibility(View.VISIBLE);
                        mViewHolder.setVisibility(View.GONE);
                        chongxinbofang.setVisibility(View.GONE);
                        Picasso.with(context).load(attachment).into(res_image);
                    }
                } else {
                    res_ll.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                res_ll.setVisibility(View.GONE);
            }


            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date d1 = df.parse(addTime);
                Date curDate = new Date(System.currentTimeMillis());
                long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
                long hours = diff / (1000 * 60 * 60);
                if (hours > 24) {
                    long days = hours / 24;
                    if (30 >= days && days >= 1) {
                        item_time.setText(days + "天前");
                    } else if (days > 30) {
                        long m = days / 30;
                        if (m > 12) {
                            long y = m / 12;
                            item_time.setText(y + "年前");
                        } else {
                            item_time.setText(m + "个月前");
                        }
                    }
                } else {
                    if (hours < 1) {
                        item_time.setText("刚刚");
                    } else {
                        item_time.setText(hours + "小时前");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            user_nickname.setText(nickname);
            topic_title.setText(title);
            topic_content.setText(content);
            if (!type.equals("http")) {
                head = URLManager.Head_URL + head;
            }
            Picasso.with(this).load(head).resize(400, 400)
                    .placeholder(R.mipmap.xionghaiz)
                    .error(R.mipmap.xionghaiz).into(bbs_user_head);
            if (count != null && !count.equals("") && !count.equals("null")) {
                topic_count.setText(count);
            } else {
                topic_count.setText("0");

            }
            try {
                if (picture != null && !picture.equals("")
                        && !picture.equals("null")) {
                    picture = URLManager.TopicCOVER_URL + picture;
                    System.out.println("picture>>>>" + picture);
                    reply_ll.setVisibility(View.VISIBLE);
                    topic_picture1.setVisibility(View.VISIBLE);
                    topic_picture2.setVisibility(View.VISIBLE);
                    topic_picture3.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(picture).into(topic_picture1);
                } else {
                    reply_ll.setVisibility(View.GONE);
                    topic_picture1.setVisibility(View.GONE);
                    topic_picture2.setVisibility(View.GONE);
                    topic_picture3.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
            bbs_topic_pinglun.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!uid.equals(userid)) {
                        int position = -2;
                        if (po != position) {
                            replyname = nickname;
                            replyid = reid;
                            replyname = "回复   " + replyname + ":";
                            reply_et.setText(replyname);
                        }
                    } else {
                        Futil.showMessage(context, "不能回复自己");
                    }


                }
            });
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void gointo() {
        startProgressDialog();
        String strUrl = URLManager.TopicListByTopicId;
        HashMap<String, String> map = new HashMap<String, String>();
        if (userid != null && !userid.equals("") && !userid.equals("null")) {
            map.put("user.id", userid);
            map.put("communityTopic.id", topicId);
            Futil.xutils(strUrl, map, handler, URLManager.one);
            System.out.println("bbscard--topicId--" + topicId);
        } else {
            map.put("communityTopic.id", topicId);
            Futil.xutils(strUrl, map, handler, URLManager.one);
        }

    }

    private void gointo2() {
        startProgressDialog();
        String strUrl = URLManager.TopicListByTopicId2;
        HashMap<String, String> map = new HashMap<String, String>();
        if (userid != null && !userid.equals("") && !userid.equals("null")) {
            map.put("user.id", userid);
            map.put("communityTopic.id", topicId);
            Futil.xutils(strUrl, map, handler, URLManager.one);
            System.out.println("bbscard--topicId--" + topicId);
        } else {
            map.put("communityTopic.id", topicId);
            Futil.xutils(strUrl, map, handler, URLManager.three);
        }

    }

    public static Handler muhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 10086) {
                chongxinbofang.setVisibility(View.VISIBLE);

            }
        }
    };
    public static boolean onbbscardact = false;

    @Override
    protected void onResume() {
        onbbscardact = true;
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        onbbscardact = false;
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }

    private void opendialog(String message) {
        System.out.println("dialog在communitytopicadapter中");
        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_queding);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        WindowManager m = (this).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (ViewGroup.LayoutParams.WRAP_CONTENT); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        TextView tv1 = (TextView) dialog.findViewById(R.id.dialog_dueding_tv1);
        tv1.setText("" + message);
        RelativeLayout rl1 = (RelativeLayout) dialog
                .findViewById(R.id.dialog_dueding_rl1);
        RelativeLayout rl2 = (RelativeLayout) dialog
                .findViewById(R.id.dialog_dueding_rl2);
        rl1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 确定花费积分查看帖子
                gointo2();
                dialog.dismiss();
            }
        });
        rl2.setOnClickListener(new OnClickListener() { // 取消

            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
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

    private AlertDialog picdialog;
    private static final int PHOTO_CARMERA = 1;// 相机
    private static final int PHOTO_PICK = 2;// 相册
    private static final int PHOTO_CUT = 3;// 裁剪
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/hsyfm/recordimg/";
    public static File destDir = new File(SDPATH);
    // 创建一个以当前系统时间为名称的文件，防止重复
    private File imgFile = new File(destDir, getPhotoFileName());

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }

    private void picopenDialog() {
        if (issdkard()) {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        } else {
            Futil.showMessage(context, "请检查sd卡是否插入");
            return;
        }
        picdialog = new AlertDialog.Builder(context).create();
        picdialog.show();
        picdialog.setContentView(R.layout.dialog_photos);

        Window dialogWindow = picdialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.23); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        RelativeLayout rl1 = (RelativeLayout) picdialog
                .findViewById(R.id.dialog_photos_rl1);
        RelativeLayout rl2 = (RelativeLayout) picdialog
                .findViewById(R.id.dialog_photos_rl2);
        RelativeLayout rl3 = (RelativeLayout) picdialog
                .findViewById(R.id.dialog_photos_rl3);
        rl1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 拍照
                startCamera(picdialog);
                picdialog.dismiss();
            }
        });
        rl2.setOnClickListener(new OnClickListener() { // 相册

            @Override
            public void onClick(View v) {
                startPick(picdialog);
                picdialog.dismiss();
            }
        });
        rl3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                picdialog.dismiss();
            }
        });
    }

    protected void startCamera(DialogInterface dialog) {
        dialog.dismiss();
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的存储路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
        startActivityForResult(intent, PHOTO_CARMERA);
    }

    // 调用系统相册
    protected void startPick(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_PICK);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_CARMERA:
                startPhotoZoom(Uri.fromFile(imgFile), 300);
                break;
            case PHOTO_PICK:
                if (null != data) {
                    startPhotoZoom(data.getData(), 300);
                }
                break;
            case PHOTO_CUT:
                if (null != data) {
                    setPicToView(data);
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 调用系统裁剪
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以裁剪
        intent.putExtra("crop", true);
        // aspectX,aspectY是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY是裁剪图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        // 设置是否返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CUT);
    }

    // 将裁剪后的图片显示在ImageView上
    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (null != bundle) {
            final Bitmap bmp = bundle.getParcelable("data");
//			imgIcon.setImageBitmap(bmp);

            saveCropPic(bmp);
        }
    }

    // 把裁剪后的图片保存到sdcard上
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            fis = new FileOutputStream(imgFile);
            fis.write(baos.toByteArray());
            fis.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
