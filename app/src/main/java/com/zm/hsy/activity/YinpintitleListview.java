package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.ContentListAdapter;
import com.zm.hsy.entity.ContentList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;
import com.zm.hsy.util.SipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 标题栏+listview页 */
public class YinpintitleListview extends Activity implements OnClickListener {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    JSONArray audioContentList = object
                            .getJSONArray("audioCommentContentList");
                    arraycList = new ArrayList<ContentList>();
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
                    cadapter = new ContentListAdapter(YinpintitleListview.this,
                            arraycList, handler);
                    mclistView.setAdapter(cadapter);
                    cadapter.notifyDataSetChanged();
                    stopProgressDialog();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (msg.what == 10086) {
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
            } else if (msg.what == URLManager.three) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    String code = object.getString("code");
                    String message = object.getString("message");
                    Futil.showMessage(context, message);
                    if (code.equals("2")) {
                        gointo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }
    };
    private ListView mclistView;
    private ImageView back_top;
    private TextView tabvf_text_top;
    private ContentListAdapter cadapter;
    private ArrayList<ContentList> arraycList ;
    private String audioId, tv, Tag;
    private String userid, shield;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yinpinpinglun);
        context = this;
        userid = Futil.getValue(context, "userid");
        shield = Futil.getValue(context, "shield");
        findview();
        Intent i = this.getIntent();
        audioId = i.getStringExtra("audioId");
        tv = i.getStringExtra("toptv");
        Tag = i.getStringExtra("Tag");
        tabvf_text_top.setText(tv);
        if (Tag.equals("1")) {
            //全部评论
            mclistView.setVisibility(View.VISIBLE);
            gointo();

        } else {
            mclistView.setVisibility(View.GONE);
        }
    }

    private TextView pinglun_et;

    private void findview() {
        mclistView = (ListView) findViewById(R.id.tabvf_onelist_content_view);
        tabvf_text_top = (TextView) findViewById(R.id.tabvf_text_top);
        pinglun_et = (TextView) findViewById(R.id.playpage_pinglun_et);
       findViewById(R.id.playpage_dianping_send).setOnClickListener(this);

        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

        mclistView.setFocusable(false);
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
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_top:
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
        }
    }
    private boolean equ = false;
    private String replyid = "0", replyname = null;
    private int po = -1;
    private int rn;
    private String content = null;
    private String parentId = "0";

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

    private void gointo() {
        startProgressDialog();

        String strUrl = URLManager.GetComment;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("audio.id", audioId);
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
