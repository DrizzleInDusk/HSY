package com.zm.hsy.activity;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.myview.SelectorScrollView;
import com.zm.hsy.util.CustomProgressDialog;
import com.zm.hsy.util.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 个人资料 */
public class InformationActivity extends Activity implements OnClickListener, SelectorScrollView.OnSelectedListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONObject data = obj.getJSONObject("userRank");
                    JSONObject userdata = data.getJSONObject("user");
                    userhead = userdata.getString("head");
                    userblurb = userdata.getString("blurb");
                    usernickname = userdata.getString("nickname");
                    provinces = userdata.getString("provinces");
                    members = userdata.getString("members");
                    belong = userdata.getString("belong");

                    String type = data.getString("headStatus");
                    setuser(type);
                    getType();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.two) {
                JSONObject obj = (JSONObject) msg.obj;

                try {
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        finish();
                    } else {
                        String message = obj.getString("message");
                        Futil.showMessage(context, message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.three) {
                JSONObject obj = (JSONObject) msg.obj;
                typeNames = new ArrayList<>();
                typeids = new ArrayList<>();
                try {
                    JSONArray typeList = obj
                            .getJSONArray("albumTypeList");
                    for (int i = 0; i < typeList.length(); i++) {
                        JSONObject albumobj = typeList
                                .getJSONObject(i);
                        String typeName = albumobj.getString("typeName");
                        typeNames.add(typeName);
                        String typeid = albumobj.getString("id");
                        typeids.add(typeid);
                        if (belong.equals(typeid)) {
                            type_et.setText(typeName);
//                            type_et.setEnabled(false);
                        }
//                        else {
//                            type_et.setEnabled(true);
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }
    };
    private String userhead, usernickname, userblurb="", members, provinces, belong = "-1";
    private Context context;
    private String userid, headurl;
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/hsyfm/img";
    public static File destDir = new File(SDPATH);
    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;
    private static final int PHOTO_CUT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        context = this;
        userid = Futil.getValue(context, "userid");
        findview();

        if (issdkard()) {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        } else {
            Futil.showMessage(context, "请检查sd卡是否插入");
        }

        getuser();
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

    private PopupWindow mTipWindow;
    private String tip = "";
    private ArrayList<String> typeNames;
    private ArrayList<String> typeids;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.isok:
                isOk();
                break;
            case R.id.information_head:
                openDialog();
                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.type_et: {
                if (mTipWindow == null) {
                    mTipWindow = createPopupWindow();
                }
                mTipWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                setAlpha(0.45f);
            }
            break;
            case R.id.pop_tip_cancle: {
                mTipWindow.dismiss();
                setAlpha(1f);
            }
            break;
            case R.id.pop_tip_ok: {
                tid = typeids.get(co);
                tip = typeNames.get(co);
                type_et.setText(tip);
                mTipWindow.dismiss();
                setAlpha(1f);
            }
            break;
        }
    }

    private ImageView back_top;
    private TextView nickname_et, blurb_et;
    private TextView  isok, type_et;
    private RoundedImageView information_head;

    private void findview() {
        type_et = (TextView) findViewById(R.id.type_et);
        type_et.setOnClickListener(this);
        blurb_et = (TextView) findViewById(R.id.blurb_et);
        blurb_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                blurb = editable.toString().trim();
            }
        });
        nickname_et = (TextView) findViewById(R.id.nickname_et);
        nickname_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = editable.toString().trim();
            }
        });

        isok = (TextView) findViewById(R.id.isok);
        isok.setOnClickListener(this);
        information_head = (RoundedImageView) findViewById(R.id.information_head);
        information_head.setOnClickListener(this);
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

    }

    private void getuser() {
        startProgressDialog();
        String strUrl = URLManager.Myown;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        Futil.xutils(strUrl, map, handler, URLManager.one);

    }

    private void getType() {
        startProgressDialog();
        String strUrl = URLManager.GetAudioAlbumType;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        Futil.xutils(strUrl, map, handler, URLManager.three);

    }

    private void setuser(String type) {
        if (!type.equals("http")) {
            userhead = URLManager.Head_URL + userhead;
        }
        System.out.println("" + userhead);
        Picasso.with(context).load(userhead).resize(400, 400)
                .error(R.mipmap.touxiang).centerCrop().into(information_head);
        if (userblurb != null && !userblurb.equals("")
                && !userblurb.equals("null")) {
            blurb_et.setText(userblurb);
        } else {
            blurb_et.setText("");
        }

        nickname_et.setText(usernickname);

    }

    private String name="";
    private String tid="";
    private String blurb="";
    private int co = 0;
    private int ia = 0;

    private void isOk() {
        startProgressDialog();
        String strUrl = URLManager.UpUser;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        HashMap<String, File> mapfile = new HashMap<String, File>();
        if (FileUtil.getFile(tempFile.getPath()) != null) {
            mapfile.put("headPic", tempFile);
            ia++;
        }
        if (!name.equals(usernickname)) {
            System.out.println("nickname>>"+name);
            map.put("nickname", name);
            ia++;
        }
        if (!blurb.equals(userblurb)) {
            System.out.println("blurb>>"+blurb);
            map.put("blurb", blurb);
        }
        if (!tip.equals(belong) && !tip.equals("")) {
            System.out.println("belong>>"+tid);
            map.put("belong", tid);
            ia++;
        }
        if (ia==0) {
            finish();
        } else {
            Futil.xutils(strUrl, map, mapfile, handler, URLManager.two);
        }
    }

    private void setAlpha(float f) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = f;
        getWindow().setAttributes(params);
    }

    private PopupWindow createPopupWindow() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View mTipView = mLayoutInflater.inflate(R.layout.popupwindow, null);
        mTipView.findViewById(R.id.pop_tip_cancle).setOnClickListener(this);
        mTipView.findViewById(R.id.pop_tip_ok).setOnClickListener(this);
        SelectorScrollView mSelectorScrollView = (SelectorScrollView) mTipView.findViewById(R.id.pop_tip_list);
        mSelectorScrollView.setOffset(2);
        mSelectorScrollView.setItems(typeNames);
        mSelectorScrollView.setOnSelectedListener(this);
        PopupWindow mPopupWindow = new PopupWindow(mTipView, getWindow().getDecorView().getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(false);
        return mPopupWindow;
    }

    @Override
    public synchronized void onSelected(int selectedIndex, String item) {
        co = selectedIndex - 2;
    }

    // 创建一个以当前系统时间为名称的文件，防止重复
    private File tempFile = new File(destDir, getPhotoFileName());

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }

    private AlertDialog dialog;

    private void openDialog() {
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
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
                startPhotoZoom(Uri.fromFile(tempFile), 300);
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
            information_head.setImageBitmap(bmp);

            saveCropPic(bmp);
        }
    }

    // 把裁剪后的图片保存到sdcard上
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            fis = new FileOutputStream(tempFile);
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
