package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** z账号绑定---手机 */
public class BangdingSjActivity extends Activity implements OnClickListener {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            stopProgressDialog();
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String string = obj.getString("message");
                    String c = obj.getString("code");
                    if (c.equals("2")) {
                        ActivityJumpControl.getInstance((Activity)context)
                                .gotoBangdingSjnextActivity(phonenum);
                    } else {
                        Futil.showMessage(context, string);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };
    private String userid;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangdingsj);
        ActivityJumpControl.getInstance(this).pushActivity(this);
        context = this;
        userid = Futil.getValue(context, "userid");
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
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.bdsj_next:
                phonenum = mobile_et.getText().toString().trim();
                if(phonenum.length()!=11){
                    Futil.showMessage(context,"请输入正确手机号");
                    return;
                }
                getCode();
                break;
            case R.id.delete_mobile:
                mobile_et.setText("");
                break;
            case R.id.back_top:
                finish();
                break;

        }
    }

    private TextView mobile_et;

    private void findview() {
        mobile_et = (TextView) findViewById(R.id.mobile_et);
        findViewById(R.id.bdsj_next).setOnClickListener(this);
        findViewById(R.id.delete_mobile).setOnClickListener(this);
        findViewById(R.id.back_top).setOnClickListener(this);

    }

    private String phonenum;

    private void getCode() {
        startProgressDialog();
        String strUrl = URLManager.SendRCode;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.mobile", phonenum);
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
