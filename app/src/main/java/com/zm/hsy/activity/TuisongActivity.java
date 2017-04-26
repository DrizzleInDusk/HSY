package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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
/** 推送设置*/
public class TuisongActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                finish();
            } else if (msg.what == URLManager.two) {
                stopProgressDialog();
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                     ps = obj.getString("pushStatus");
                    findview();
                } catch (JSONException e) {
                    e.printStackTrace();
                    findview();
                }
            }
        }

    };

    private String userid,ps="-1";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuisong);
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
            case R.id.tsback_top:
                oversel();
                break;
            case R.id.shengyinpingl:
                setsele(shengyinpingl);
                break;
            case R.id.shequpingl:
                setsele(shequpingl);
                break;
            case R.id.jingyin:
                setsele(jingyin);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            oversel();
        }
        return false;
    }

    private void oversel() {
        if (!shengyinpingl.isSelected() && !shequpingl.isSelected()) {
            pushSet("0");
        } else if (shengyinpingl.isSelected() && shequpingl.isSelected()) {
            pushSet("3");
        } else if (!shengyinpingl.isSelected() && shequpingl.isSelected()) {
            pushSet("1");
        } else if (shengyinpingl.isSelected() &&! shequpingl.isSelected()) {
            pushSet("2");
        }
    }

    private ImageView back_top;
    private TextView shengyinpingl, shequpingl, jingyin;

    private void findview() {
        back_top = (ImageView) findViewById(R.id.tsback_top);
        back_top.setOnClickListener(this);
        shengyinpingl = (TextView) findViewById(R.id.shengyinpingl);
        shengyinpingl.setOnClickListener(this);
        shequpingl = (TextView) findViewById(R.id.shequpingl);
        shequpingl.setOnClickListener(this);
        jingyin = (TextView) findViewById(R.id.jingyin);
        jingyin.setOnClickListener(this);
        if(ps.equals("0")){
            shengyinpingl.setSelected(false);
            shequpingl.setSelected(false);
        }else if(ps.equals("1")){
            shengyinpingl.setSelected(false);
            shequpingl.setSelected(true);
        }else if(ps.equals("2")){
            shengyinpingl.setSelected(true);
            shequpingl.setSelected(false);
        }else {
            shengyinpingl.setSelected(true);
            shequpingl.setSelected(true);
        }

    }

    private void setsele(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
        } else {
            v.setSelected(true);
        }
    }

    private void pushSet(String pushStatus) {
        String strUrl = URLManager.pushSet;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        map.put("user.pushStatus", pushStatus);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    private void appUserSet() {
        startProgressDialog();
        String strUrl = URLManager.appUserSet;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        map.put("userType", "1");
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
