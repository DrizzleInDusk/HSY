package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
/** 帮助中心 */
public class BangzhuActivity extends Activity implements OnClickListener {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangzhu);
        context = this;
        findview();
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
        switch (v.getId()) {

            case R.id.back_top:
                back();
                break;

            case R.id.ziliao:
                if (!ziliao) {
                    bzll.setVisibility(View.GONE);
                    bangzhu1.setVisibility(View.VISIBLE);
                    ziliao = true;
                }
                break;
            case R.id.bofang:
                break;
            case R.id.luzhi:
                break;
            case R.id.banquan:
                break;
            case R.id.zh1:
                chetv(zh1_tv);
                break;
            case R.id.zh2:
                chetv(zh2_tv);
                break;
            case R.id.zh3:
                chetv(zh3_tv);
                break;
            case R.id.zh4:
                chetv(zh4_tv);
                break;

        }
    }

    private boolean ziliao = false, bofang = false, luzhi = false, banquan = false;
    private View bangzhu1;
    private LinearLayout bzll;
    private TextView zh1_tv, zh2_tv, zh3_tv, zh4_tv;

    private void findview() {
        bangzhu1 = findViewById(R.id.bangzhu1);
        bzll = (LinearLayout) findViewById(R.id.bzll);
        findViewById(R.id.back_top).setOnClickListener(this);
        findViewById(R.id.ziliao).setOnClickListener(this);
        findViewById(R.id.bofang).setOnClickListener(this);
        findViewById(R.id.luzhi).setOnClickListener(this);
        findViewById(R.id.banquan).setOnClickListener(this);
        findViewById(R.id.zh1).setOnClickListener(this);
        findViewById(R.id.zh2).setOnClickListener(this);
        findViewById(R.id.zh3).setOnClickListener(this);
        findViewById(R.id.zh4).setOnClickListener(this);
        zh1_tv = (TextView) findViewById(R.id.zh1_tv);
        zh2_tv = (TextView) findViewById(R.id.zh2_tv);
        zh3_tv = (TextView) findViewById(R.id.zh3_tv);
        zh4_tv = (TextView) findViewById(R.id.zh4_tv);
        bangzhu1.setVisibility(View.GONE);

    }

    private ArrayList<TextView> tvlist = new ArrayList<>();

    private void chetv(TextView tv) {
        tvgone();
        tv.setVisibility(View.VISIBLE);
        tvlist.add(tv);
    }
    private void tvgone(){
        for (int i = 0; i < tvlist.size(); i++) {
            tvlist.remove(i).setVisibility(View.GONE);
        }
    }
    private void back() {
        tvgone();
        if (!ziliao && !bofang && !luzhi && !banquan) {
            finish();
        } else if (ziliao && !bofang && !luzhi && !banquan) {
            bzll.setVisibility(View.VISIBLE);
            bangzhu1.setVisibility(View.GONE);
            ziliao = false;
        } else if (!ziliao && bofang && !luzhi && !banquan) {
            bofang = false;
        } else if (!ziliao && !bofang && luzhi && !banquan) {
            luzhi = false;
        } else if (!ziliao && !bofang && !luzhi && banquan) {
            banquan = false;
        }
    }
}
