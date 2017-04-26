package com.zm.hsy.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.adapter.MyCFAdapter;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
import com.squareup.picasso.Picasso;

import android.app.ActionBar;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/** 我的CF-- 1.粉丝 2.关注 */
public class MyConcemFansActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {// 粉丝
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray ufarray = obj.getJSONArray("userConcemFansList");
                    fanslist = new ArrayList<User>();
                    for (int i = 0; i < ufarray.length(); i++) {
                        User uf = new User();
                        JSONObject ufobj = ufarray.getJSONObject(i);
                        uf.setConcemFans(ufobj.getString("concemFans"));
                        uf.setAudioCount(ufobj.getString("audioCount"));
                        uf.setHeadStatus(ufobj.getString("headStatus"));
                        JSONObject user = ufobj.getJSONObject("user");

                        uf.setHead(user.getString("head"));
                        uf.setNickname(user.getString("nickname"));
                        uf.setId(user.getString("id"));
                        fanslist.add(uf);
                    }
                    cf_tv.setText("共有" + ufarray.length() + "个粉丝");
                    if(ufarray.length()>0){
                        sendmesg.setVisibility(View.VISIBLE);
                    }else{
                        sendmesg.setVisibility(View.GONE);
                    }
                    cfadapter = new MyCFAdapter(context,
                            fanslist, CF, handler);
                    cf_viewp.setAdapter(cfadapter);
                    cfadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendmesg.setVisibility(View.GONE);
                    cf_tv.setText("共有0个粉丝");
                }
            } else if (msg.what == URLManager.two) {// 关注
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray ucarray = obj.getJSONArray("userConcemFansList");
                    concemlist = new ArrayList<User>();
                    for (int i = 0; i < ucarray.length(); i++) {
                        User uc = new User();
                        JSONObject ucobj = ucarray.getJSONObject(i);
                        uc.setConcemFans(ucobj.getString("concemFans"));
                        uc.setAudioCount(ucobj.getString("audioCount"));
                        uc.setHeadStatus(ucobj.getString("headStatus"));
                        JSONObject userc = ucobj.getJSONObject("user");

                        uc.setHead(userc.getString("head"));
                        uc.setNickname(userc.getString("nickname"));
                        uc.setId(userc.getString("id"));
                        concemlist.add(uc);
                    }
                    concemnum = ucarray.length();
                    cf_tv.setText("共关注了" + ucarray.length() + "个人");
                    cfadapter = new MyCFAdapter(context,
                            concemlist, CF, handler);
                    cf_viewp.setAdapter(cfadapter);
                    cfadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    cf_tv.setText("共关注了0个人");
                }
            } else if (msg.what == URLManager.three) {//关注
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String message = obj.getString("message");
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        Toast.makeText(context, "" + message,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "" + message,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.four) {//取消关注
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String message = obj.getString("message");
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        concemnum = concemnum - 1;
                        cf_tv.setText("共关注了" + concemnum + "个人");
                        Toast.makeText(context, "" + message,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "" + message,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.five) {//
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String message = obj.getString("message");
                    Futil.showMessage(context, message);
                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }

    };
    private int concemnum;
    private String userid, CF, toptv;
    private ArrayList<User> fanslist;
    private ArrayList<User> concemlist;
    private MyCFAdapter cfadapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concemfans);
        context = this;
        Intent i = this.getIntent();
        CF = i.getStringExtra("CF");
        toptv = i.getStringExtra("toptv");
        userid = Futil.getValue(context, "userid");
        findview();
        if (CF.equals("1")) {
            // 粉丝
            getfans();
            sendmesg.setVisibility(View.VISIBLE);
        } else if (CF.equals("2")) {
            // 关注
            getconcem();
            sendmesg.setVisibility(View.GONE);
        }
    }

    public static boolean mcfonact = false;

    @Override
    protected void onResume() {
        mcfonact = true;
        setcontextaudio(App.getPlaycode() == -1);
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mcfonact = false;
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.back_top:
                finish();
                break;
            case R.id.sendmesg:
                openDialog();
                break;
            case R.id.dialog_smesg_imgrl:
                openphotoDialog();
                break;
            case R.id.dialog_smesg_qx:
                dialog.dismiss();
                break;
            case R.id.dialog_smesg_yes:
                String mess = smesg_mess.getText().toString().trim();
                if(mess.length()==0){
                    Futil.showMessage(context,"消息不能为空");
                    return;
                }
                pushMessageToFan(mess);
                dialog.dismiss();
                break;

        }
    }

    private ListView cf_viewp;
    private TextView cf_tv, cf_top_tv;
    private ImageView back_top;

    private RoundedImageView playbar_touxiang;
    private ImageView playbar_right;
    private static ImageView playbar_play;
    private TextView playbar_name, sendmesg;

    private void findview() {
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);
        sendmesg = (TextView) findViewById(R.id.sendmesg);
        sendmesg.setOnClickListener(this);
        cf_top_tv = (TextView) findViewById(R.id.cf_top_tv);
        cf_top_tv.setText(toptv);
        cf_tv = (TextView) findViewById(R.id.cf_tv);

        cf_viewp = (ListView) findViewById(R.id.cf_viewp);
        cf_viewp.setFocusable(false);

        /** 后台播放信息 */
        playbar_touxiang = (RoundedImageView) findViewById(R.id.playbar_touxiang);
        playbar_right = (ImageView) findViewById(R.id.playbar_right);
        playbar_play = (ImageView) findViewById(R.id.playbar_play);
        playbar_name = (TextView) findViewById(R.id.playbar_name);
    }

    /**
     * 后台播放信息
     */
    private String id = null;
    private String AlbumId = null;
    private String mmsh = null;
    private String stationname = null;
    private String stationid = null;
    private int isstation;

    private void setcontextaudio(boolean ss) {
        isstation = App.getIsstation();
        AudioList contextaudio = App.getContextAudioList();
        if (isstation == 0) {
            if (contextaudio != null) {
                id = contextaudio.getId();
                AlbumId = contextaudio.getAudioAlbumId();
                String name = contextaudio.getAudioName();
                playbar_name.setText(name);
                String cover = contextaudio.getCover();
                cover = URLManager.COVER_URL + cover;
                Picasso.with(context).load(cover)
                        .resize(400, 400).placeholder(R.color.touming)
                        .error(R.mipmap.ic_launcher).into(playbar_touxiang);
            }
        } else if (isstation == 1) {
            mmsh = App.getPlayerpath();
            stationname = App.getStationname();
            stationid = App.getStationid();
            Picasso.with(context).load(R.mipmap.yyp)
                    .resize(400, 400).placeholder(R.color.touming)
                    .error(R.mipmap.ic_launcher).into(playbar_touxiang);
        }
        int pcode = App.getPlaycode();
        System.out.println("pcode------" + pcode);
        if (pcode == 1) {
            playbar_play.setSelected(ss);
        } else if (pcode == 0) {
            playbar_play.setSelected(!ss);
        } else {
            playbar_play.setSelected(!ss);
        }
        setonlistener();
    }

    private int playcode;

    private void setonlistener() {

        playbar_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (playbar_play.isSelected()) {
                    MainActivity.mVV.pause();
                    playcode = 1;
                    playbar_play.setSelected(false);
                } else {
                    // 发起一次播放任务,当然您不一定要在这发起
                    if (!MainActivity.mVV.isPlaying() && (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
                        MainActivity.mVV.resume();
                    } else {
                        MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);//UI 事件  播放
                    }
                    playcode = 0;
                    playbar_play.setSelected(true);
                }
                App.setPlaycode(playcode);
            }
        });
        playbar_name.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isstation == 0) {
                    ActivityJumpControl.getInstance(MyConcemFansActivity.this)
                            .gotoDetailsPlayActivity(id);
                } else if (isstation == 1) {
                    ActivityJumpControl.getInstance(MyConcemFansActivity.this)
                            .gotoRadioStationPlayerActivity(mmsh, stationname, stationid);
                }
            }
        });
    }

    private AlertDialog dialog;
    private TextView smesg_mess;
    private ImageView smesg_img;
    private RelativeLayout smesg_imgrl;

    private void openDialog() {
        dialog = new AlertDialog.Builder(context).create();
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_sendmesg, null);
        dialog.setView(v);
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (ActionBar.LayoutParams.WRAP_CONTENT); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        smesg_mess = (TextView) dialog.findViewById(R.id.dialog_smesg_mess);
        smesg_imgrl = (RelativeLayout) dialog.findViewById(R.id.dialog_smesg_imgrl);
        smesg_img = (ImageView) dialog.findViewById(R.id.dialog_smesg_img);
        smesg_imgrl.setOnClickListener(this);
        dialog.findViewById(R.id.dialog_smesg_qx).setOnClickListener(this);
        dialog.findViewById(R.id.dialog_smesg_yes).setOnClickListener(this);

    }

    private AlertDialog photodialog;
    private File tempFile=null;

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }

    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/hsyfm/img";
    public static File destDir = new File(SDPATH);
    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;
    private static final int PHOTO_CUT = 3;

    private boolean issdkard() {
        // 首先判断sdcard是否插入
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            return true;
        } else {
            Futil.showMessage(context, "请检查sd卡是否插入");
            return false;
        }
    }

    private void openphotoDialog() {
        if (!issdkard()) {
            return;
        }
        tempFile = new File(destDir, getPhotoFileName());
        photodialog = new AlertDialog.Builder(context).create();
        photodialog.show();
        photodialog.setContentView(R.layout.dialog_photos);

        Window dialogWindow = photodialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (ActionBar.LayoutParams.WRAP_CONTENT); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        RelativeLayout rl1 = (RelativeLayout) photodialog
                .findViewById(R.id.dialog_photos_rl1);
        RelativeLayout rl2 = (RelativeLayout) photodialog
                .findViewById(R.id.dialog_photos_rl2);
        RelativeLayout rl3 = (RelativeLayout) photodialog
                .findViewById(R.id.dialog_photos_rl3);
        rl1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 拍照
                startCamera(photodialog);
                photodialog.dismiss();
            }
        });
        rl2.setOnClickListener(new OnClickListener() { // 相册

            @Override
            public void onClick(View v) {
                startPick(photodialog);
                photodialog.dismiss();
            }
        });
        rl3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                photodialog.dismiss();
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
            smesg_img.setImageBitmap(bmp);
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

    public static Handler muhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 10086) {
                playbar_play.setSelected(false);
            }
        }
    };

    private void getfans() {
        startProgressDialog();
        String strUrl = URLManager.MyFans;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    private void pushMessageToFan(String content) {
        startProgressDialog();
        String strUrl = URLManager.pushMessageToFan;
        HashMap<String, String> map = new HashMap<String, String>();
        HashMap<String, File> mapfile = new HashMap<String, File>();
        map.put("pushMessage.pushFrom", userid);
        map.put("pushMessage.content", content);
        if(tempFile!=null){
            mapfile.put("picture", tempFile);
        }
        Futil.xutils(strUrl, map, mapfile, handler, URLManager.three);
    }

    private void getconcem() {
        startProgressDialog();
        String strUrl = URLManager.MyConcem;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
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
