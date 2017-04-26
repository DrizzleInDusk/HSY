package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
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
public class BangdingSjnextActivity extends Activity implements OnClickListener {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            stopProgressDialog();
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String string = obj.getString("message");
                    String c = obj.getString("code");
                    Futil.showMessage(context, string);
                    if (c.equals("2")) {
                        time = new TimeCount(60000, 1000);
                        time.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.two) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    String message = obj.getString("message");
                    Futil.showMessage(context, "" + message);
                    if (code.equals("2")) {
                        ActivityJumpControl.getInstance((Activity) context)
                                .gotoBangdingActivity();
                        ActivityJumpControl.getInstance((Activity) context).popAllActivity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    };
    private String userid;
    private Context context;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdsjnext);
        ActivityJumpControl.getInstance(this).pushActivity(this);
        context = this;
        phonenum = this.getIntent().getStringExtra("phonenum");
        userid = Futil.getValue(context, "userid");
        findview();
        time = new TimeCount(60000, 1000);
        time.start();
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
            case R.id.bdsj_next:
                yzCode = bdsj_yanzm.getText().toString().trim();
                password = bdsj_passwd.getText().toString().trim();
                if (yzCode.equals("")) {
                    Futil.showMessage(context, "请输入验证码");
                    return;
                }
                if (password.equals("")) {
                    Futil.showMessage(context, "请输入登陆密码");
                    return;
                }
                bindings();
                break;
            case R.id.bdsj_time:
                getCode();
                break;
            case R.id.back_top:
                finish();
                break;

        }
    }

    private TextView bdsj_yanzm, bdsj_passwd, bdsj_time,bdsj_tv;

    private void findview() {
        bdsj_tv = (TextView) findViewById(R.id.bdsj_tv);
        bdsj_tv.setText("验证码短信已发送至手机"+phonenum+"上,请输入短信中的验证码数字");
        bdsj_yanzm = (TextView) findViewById(R.id.bdsj_yanzm);
        bdsj_passwd = (TextView) findViewById(R.id.bdsj_passwd);
        bdsj_time = (TextView) findViewById(R.id.bdsj_time);
        bdsj_time.setOnClickListener(this);

        findViewById(R.id.bdsj_next).setOnClickListener(this);
        findViewById(R.id.back_top).setOnClickListener(this);

    }

    private String phonenum;
    private String yzCode;
    private String password;

    private void getCode() {
        startProgressDialog();
        String strUrl = URLManager.SendRCode;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.mobile", phonenum);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    private void bindings() {
        String strUrl = URLManager.bindings;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("loginType", "sj");
        map.put("openid", phonenum);
        map.put("user.id", userid);
        map.put("yzCode", yzCode);
        map.put("password", password);
        Futil.xutils(strUrl, map, handler, URLManager.two);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            bdsj_time.setText("重新获取验证码");
            bdsj_time.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bdsj_time.setClickable(false);
            bdsj_time.setText(millisUntilFinished / 1000 + "后重新获取");
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
