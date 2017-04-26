package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 隐私新鲜事*/
public class YinsiXxsActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                finish();
            } else if (msg.what == URLManager.two) {
                stopProgressDialog();
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    fs = obj.getString("freshStatus()");
                    findview();
                } catch (JSONException e) {
                    e.printStackTrace();
                    findview();
                }
            }
        }

    };

    private String userid, fs = "-1";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yinsixxs);
        context = this;
        userid = Futil.getValue(context, "userid");
        appUserSet();
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
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.back_top:
                getselt();
                break;
            case R.id.dingyuzhuanji:
                setsele(dingyuzhuanji);
                break;
            case R.id.zanshengyin:
                setsele(zanshengyin);
                break;
            case R.id.guanzhutaren:
                setsele(guanzhutaren);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            getselt();
        }
        return false;
    }

    private String freshStatus = "4";

    private void getselt() {
        if (!dingyuzhuanji.isSelected()) {
            if (freshStatus.equals("4")) {
                freshStatus = "1";
            } else {
                freshStatus = freshStatus + ",1";
            }
        }
        if (!zanshengyin.isSelected()) {
            if (freshStatus.equals("4")) {
                freshStatus = "2";
            } else {
                freshStatus = freshStatus + ",2";
            }
        }
        if (!guanzhutaren.isSelected()) {
            if (freshStatus.equals("4")) {
                freshStatus = "3";
            } else {
                freshStatus = freshStatus + ",3";
            }
        }
        freshSet(freshStatus);
    }

    private TextView dingyuzhuanji, zanshengyin, guanzhutaren;

    private void findview() {
        findViewById(R.id.back_top).setOnClickListener(this);
        dingyuzhuanji = (TextView) findViewById(R.id.dingyuzhuanji);
        dingyuzhuanji.setOnClickListener(this);
        zanshengyin = (TextView) findViewById(R.id.zanshengyin);
        zanshengyin.setOnClickListener(this);
        guanzhutaren = (TextView) findViewById(R.id.guanzhutaren);
        guanzhutaren.setOnClickListener(this);
        if (fs != null && !fs.equals("") && !fs.equals("null")) {
            if (fs.contains("1")) {
                dingyuzhuanji.setSelected(false);
            } else {
                dingyuzhuanji.setSelected(true);
            }
            if (fs.contains("2")) {
                zanshengyin.setSelected(false);
            } else {
                zanshengyin.setSelected(true);
            }
            if (fs.contains("3")) {
                guanzhutaren.setSelected(false);
            } else {
                guanzhutaren.setSelected(true);
            }
        } else {
            dingyuzhuanji.setSelected(false);
            zanshengyin.setSelected(false);
            guanzhutaren.setSelected(false);
        }
    }

    private void setsele(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
        } else {
            v.setSelected(true);
        }
    }

    private void freshSet(String freshStatus) {
        String strUrl = URLManager.freshSet;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        map.put("user.freshStatus", freshStatus);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    private void appUserSet() {
        startProgressDialog();
        String strUrl = URLManager.appUserSet;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        map.put("userType", "3");
        Futil.xutils(strUrl, map, handler, URLManager.two);
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
