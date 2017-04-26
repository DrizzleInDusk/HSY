package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.BBSCommunityAdapter;
import com.zm.hsy.entity.Community;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/*** 更多我的社区 */
public class MoreMyCommunityActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                mbbsList = new ArrayList<Community>();

                try {
                    JSONArray myC = obj.getJSONArray("myCommunityList");

                    for (int k = 0; k < myC.length(); k++) {
                        Community mc = new Community();
                        JSONObject mcobj = myC.getJSONObject(k);
                        mc.setMemCount(mcobj.getString("memCount"));
                        mc.setTopicCount(mcobj.getString("topicCount"));
                        JSONObject myc = mcobj.getJSONObject("community");
                        mc.setBlurb(myc.getString("blurb"));
                        mc.setCover(myc.getString("cover"));
                        mc.setName(myc.getString("name"));
                        mc.setId(myc.getString("id"));
                        mbbsList.add(mc);
                    }
                    mcadapter = new BBSCommunityAdapter(context,
                            mbbsList);
                    moremc_viewp.setAdapter(mcadapter);
                    stopProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }

    };
    private ArrayList<Community> mbbsList;
    private String userid;
    private Context context;
    private BBSCommunityAdapter mcadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moremycommunity);
        context = this;
        userid = Futil.getValue(context, "userid");
        findview();
    }

    @Override
    protected void onResume() {
        getMoreComm();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_top:
                finish();
                break;

        }
    }
    private ListView moremc_viewp;
    private void findview() {
        moremc_viewp = (ListView) findViewById(R.id.moremc_viewp);
        moremc_viewp.setFocusable(false);
        findViewById(R.id.back_top).setOnClickListener(this);

    }

    private void getMoreComm() {
        startProgressDialog();
        String strUrl = URLManager.GetMoreCommunity;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
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
