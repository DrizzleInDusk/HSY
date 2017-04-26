package com.zm.hsy.activity;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;

import java.io.File;

public class WelfirstActivity extends Activity {
    private final int REQUSET_CODE_WRITE_EXTERNAL_STORAGE = 1008611;
    private String value;
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/hsyfm/";
    public static File destDir = new File(SDPATH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        value = Futil.getValue(WelfirstActivity.this, "isfirst");
        if (issdkard()) {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        }

//检查SD卡写权限授予情况
//        if (ContextCompat.checkSelfPermission(WelfirstActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(WelfirstActivity.this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUSET_CODE_WRITE_EXTERNAL_STORAGE);
//        }else{
        goac();
//        }


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
    /**
     * 权限授权回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUSET_CODE_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goac();
                } else {
                    //禁止授权
                    finish();
                }
                return;
            }
        }
    }

    private void  goac(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!value.equals("ok")) {// 启动欢迎页
                    Intent intent = new Intent(WelfirstActivity.this,
                            WelActivity.class);
                    Futil.saveValue(WelfirstActivity.this, "isfirst", "ok");
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } else {// 启动主应用
                    Intent intent = new Intent(WelfirstActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);

                }
            }
        }, 2000);
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
}
