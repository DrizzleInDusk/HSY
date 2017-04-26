package com.zm.hsy.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
/*** 创建活动页 */
public class BuildVariationActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;

                try {
                    String code = obj.getString("code");
                    String message = obj.getString("message");
                    if (code.equals("2")) {
                        Futil.showMessage(context, message);
                        finish();
                    } else if (message.equals("VIP用户并且积分大于两万才能发起活动！")) {
                        Futil.showDialog(context, "积分不足或没有权限", "去查看", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == AlertDialog.BUTTON_POSITIVE) {
                                    ActivityJumpControl.getInstance(BuildVariationActivity.this)
                                            .gotoManageActivity();
                                    finish();
                                } else if (which == AlertDialog.BUTTON_NEGATIVE) {
                                    finish();
                                }
                            }
                        });
                    } else {
                        Futil.showMessage(context, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }

    };
    private AlertDialog dialog;
    private static final int PHOTO_CARMERA = 1;// 相机
    private static final int PHOTO_PICK = 2;// 相册
    private static final int PHOTO_CUT = 3;// 裁剪
    private String userid, communityid;
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/hsyfm/recordimg/";
    public static File destDir = new File(SDPATH);
    private String imgpath = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildvariat);
        context = this;
        Intent i = this.getIntent();
        communityid = i.getStringExtra("communityid");
        userid = Futil.getValue(context, "userid");
        findview();

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

    public boolean issdkard() {
        // 首先判断sdcard是否插入
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    protected void onRestart() {

        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.addphoto:
                openDialog();
                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.variat_publish:
                if (userid != null && !userid.equals("")) {
                    getet();
                } else {
                    Futil.showMessage(context, "请先登录");
                }
                break;

        }
    }

    private ImageView back_top, photo_img;
    private RelativeLayout addphoto;
    private TextView variat_publish;
    private EditText title_et, blurb_et, rule_et, quota_et, cost_et, jinf_et,
            content_et;

    private void findview() {
        photo_img = (ImageView) findViewById(R.id.photo_img);

        variat_publish = (TextView) findViewById(R.id.variat_publish);
        variat_publish.setOnClickListener(this);
        addphoto = (RelativeLayout) findViewById(R.id.addphoto);
        addphoto.setOnClickListener(this);
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

        title_et = (EditText) findViewById(R.id.title_et);
        blurb_et = (EditText) findViewById(R.id.blurb_et);
        rule_et = (EditText) findViewById(R.id.rule_et);
        quota_et = (EditText) findViewById(R.id.quota_et);
        cost_et = (EditText) findViewById(R.id.cost_et);
        jinf_et = (EditText) findViewById(R.id.jinf_et);
        content_et = (EditText) findViewById(R.id.content_et);
    }

    private void getet() {
        title = title_et.getText().toString().trim();
        blurb = blurb_et.getText().toString().trim();
        rule = rule_et.getText().toString().trim();
        quota = quota_et.getText().toString().trim();
        cost = cost_et.getText().toString().trim();
        content = content_et.getText().toString().trim();
        score = jinf_et.getText().toString().trim();
        if (title == null) {
            Futil.showMessage(context, "请输入活动标题");
        } else if (blurb.length() == 0) {
            Futil.showMessage(context, "请输入活动简介");
        } else if (rule.length() == 0) {
            Futil.showMessage(context, "请输入活动规则");
        } else if (quota.length() == 0) {
            Futil.showMessage(context, "请限定活动名额");
        } else if (cost.length() == 0) {
            Futil.showMessage(context, "请输入报名费用");
        } else if (content.length() == 0) {
            Futil.showMessage(context, "请输入活动内容");
        } else if (!imgFile.exists()) {
            Futil.showMessage(context, "请添加宣传照片");
        } else {
            getscore();
        }
    }

    private String title = null, content = null, rule = null, cost = null,
            quota = null, blurb = null, score = null;

    private void getscore() {
        startProgressDialog();
        String strUrl = URLManager.addActivity;
        HashMap<String, String> map = new HashMap<String, String>();
        HashMap<String, File> mapfile = new HashMap<String, File>();
        map.put("activity.title", title);
        map.put("activity.content", content);
        map.put("activity.rule", rule);
        map.put("activity.cost", cost);
        map.put("activity.quota", quota);
        map.put("activity.blurb", blurb);
        map.put("activity.publisherId", userid);
        map.put("activity.score", score);
        mapfile.put("propaganda", imgFile);
        // if(conPic!=null){
        // mapfile.put("conPic", conPic);
        // }
        // if(conVideo!=null){
        // mapfile.put("conVideo", conVideo);
        // }

        Futil.xutils(strUrl, map, mapfile, handler, URLManager.one);
    }

    // 创建一个以当前系统时间为名称的文件，防止重复
    private File imgFile = new File(destDir, getPhotoFileName());

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }

    private void openDialog() {
        if (issdkard()) {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        } else {
            Futil.showMessage(context, "请检查sd卡是否插入");
            return;
        }

        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_photos);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.23); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        RelativeLayout rl1 = (RelativeLayout) dialog
                .findViewById(R.id.dialog_photos_rl1);
        RelativeLayout rl2 = (RelativeLayout) dialog
                .findViewById(R.id.dialog_photos_rl2);
        RelativeLayout rl3 = (RelativeLayout) dialog
                .findViewById(R.id.dialog_photos_rl3);
        rl1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 拍照
                startCamera(dialog);
                dialog.dismiss();
            }
        });
        rl2.setOnClickListener(new OnClickListener() { // 相册

            @Override
            public void onClick(View v) {
                startPick(dialog);
                dialog.dismiss();
            }
        });
        rl3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    protected void startCamera(DialogInterface dialog) {
        dialog.dismiss();
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的存储路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
        startActivityForResult(intent, PHOTO_CARMERA);
    }

    // 调用系统相册
    protected void startPick(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_PICK);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_CARMERA:
                startPhotoZoom(Uri.fromFile(imgFile), 300);
                break;
            case PHOTO_PICK:
                if (null != data) {
                    startPhotoZoom(data.getData(), 300);
                }
                break;
            case PHOTO_CUT:
                if (null != data) {
                    setPicToView(data);
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 调用系统裁剪
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以裁剪
        intent.putExtra("crop", true);
        // aspectX,aspectY是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY是裁剪图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        // 设置是否返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CUT);
    }

    // 将裁剪后的图片显示在ImageView上
    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (null != bundle) {
            final Bitmap bmp = bundle.getParcelable("data");
            photo_img.setImageBitmap(bmp);

            saveCropPic(bmp);
        }
    }

    // 把裁剪后的图片保存到sdcard上
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            fis = new FileOutputStream(imgFile);
            imgpath = imgFile.getPath();
            fis.write(baos.toByteArray());
            fis.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
