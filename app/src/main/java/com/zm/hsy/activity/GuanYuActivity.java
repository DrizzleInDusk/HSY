package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import cn.jpush.android.api.JPushInterface;
/** 关于 */
public class GuanYuActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;

            }
            stopProgressDialog();
        }

    };

    private String userid;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guanyu);
        context = this;
        userid = Futil.getValue(context, "userid");
        findview();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
        UpdateFunGO.onResume(this);
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            UpdateFunGO.onStop(this);
        }catch (Exception e){
            e.printStackTrace();
        }
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
            case R.id.guanyu_back:
                finish();
                break;
            case R.id.xiao:
            case R.id.ku:
                Futil.showMessage(context, "感谢评论~~");
                break;
            case R.id.banben:{
                UpdateKey.API_TOKEN = "dd2963de183d19e5bd484aa2f113c9c7";
                UpdateKey.APP_ID = "57f07f55959d6925b4000293";
                //下载方式:
                UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;//通过Dialog来进行下载
                //UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;通过通知栏来进行下载(默认)
//                UpdateFunGO.init(this);
                UpdateFunGO.manualStart(this);
            }
//                Futil.showDialog(context, "待开发~~~", "确定", null, null);
                break;
            case R.id.lianxiwomen:
                ActivityJumpControl.getInstance(GuanYuActivity.this)
                        .gotoLianxiActivity();
                break;

        }
    }

    private ImageView xiao, ku;
    private RelativeLayout banben, lianxiwomen;

    private void findview() {

        findViewById(R.id.guanyu_back).setOnClickListener(this);
        xiao = (ImageView) findViewById(R.id.xiao);
        xiao.setOnClickListener(this);
        ku = (ImageView) findViewById(R.id.ku);
        ku.setOnClickListener(this);
        banben = (RelativeLayout) findViewById(R.id.banben);
        banben.setOnClickListener(this);
        lianxiwomen = (RelativeLayout) findViewById(R.id.lianxiwomen);
        lianxiwomen.setOnClickListener(this);

    }

    private void getscore(String e) {
        startProgressDialog();
        String strUrl = "http://192.168.1.79:8080/hsyfm/getIpAddr";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", e);
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
