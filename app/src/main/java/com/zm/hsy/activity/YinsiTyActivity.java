package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 隐私通用*/
public class YinsiTyActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;

            }else if (msg.what == URLManager.two) {
                stopProgressDialog();
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    cs = obj.getString("commentStatus()");
                    findview();
                } catch (JSONException e) {
                    e.printStackTrace();
                    findview();
                }
            }
            stopProgressDialog();
        }

    };

    private String userid,cs="-1";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yinsity);
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

            case R.id.allman:
                currencySet("0");
                chicktv(allman);
                break;
            case R.id.wogzdr:
                currencySet("1");
                chicktv(wogzdr);
                break;
            case R.id.renzzb:
                currencySet("3");
                chicktv(renzzb);
                break;
            case R.id.gzwodr:
                currencySet("2");
                chicktv(gzwodr);
                break;
            case R.id.guanbipl:
                currencySet("4");
                chicktv(guanbipl);
                break;
            case R.id.back_top:
                finish();
                break;

        }
    }

    private ImageView back_top;
    private TextView allman, wogzdr, renzzb, gzwodr,  guanbipl;

    private void findview() {

        allman = (TextView) findViewById(R.id.allman);
        allman.setOnClickListener(this);

        wogzdr = (TextView) findViewById(R.id.wogzdr);
        wogzdr.setOnClickListener(this);

        renzzb = (TextView) findViewById(R.id.renzzb);
        renzzb.setOnClickListener(this);

        gzwodr = (TextView) findViewById(R.id.gzwodr);
        gzwodr.setOnClickListener(this);

        guanbipl = (TextView) findViewById(R.id.guanbipl);
        guanbipl.setOnClickListener(this);

        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);
        if(cs.equals("0")){
            chicktv(allman);
        }else if(cs.equals("1")){
            chicktv(wogzdr);
        }else if(cs.equals("2")){
            chicktv(renzzb);
        }else if(cs.equals("3")){
            chicktv(gzwodr);
        }else if(cs.equals("4")){
            chicktv(guanbipl);
        }
    }

    private ArrayList<TextView> tvlist = new ArrayList<>();

    private void chicktv(TextView tv) {
        for (int i = 0; i < tvlist.size(); i++) {
            tvlist.remove(i).setSelected(false);
        }
        tv.setSelected(true);
        tvlist.add(tv);
    }

    private void currencySet(String commentStatus) {
        startProgressDialog();
        String strUrl = URLManager.currencySet;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        map.put("user.commentStatus", commentStatus);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    private void appUserSet() {
        startProgressDialog();
        String strUrl = URLManager.appUserSet;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        map.put("userType", "2");
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
