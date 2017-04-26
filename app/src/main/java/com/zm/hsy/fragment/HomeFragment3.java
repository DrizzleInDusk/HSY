package com.zm.hsy.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.activity.MainActivity;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class HomeFragment3 extends Fragment implements OnClickListener {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    audioCount = obj.getString("audioCount");
                    videoCount = obj.getString("videoCount");

                    JSONObject data = obj.getJSONObject("userRank");
                    fans = data.getString("fans");
                    guanzhu = data.getString("guanzhu");
                    JSONObject userdata = data.getJSONObject("user");
                    userhead = userdata.getString("head");
                    userblurb = userdata.getString("blurb");
                    App.score = userdata.getString("score");
                    usernickname = userdata.getString("nickname");
                    provinces = userdata.getString("provinces");
                    members = userdata.getString("members");
                    shield = userdata.getString("shield");
                    String type = data.getString("headStatus");
                    setuser(type);
                    setaliasandtag();
                } catch (JSONException e) {
                    e.printStackTrace();
                    stopProgressDialog();
                }
            } else if (msg.what == URLManager.three) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String vStatus = obj.getString("vStatus");
                    if (vStatus.equals("2")) {
                        App.vStatus = "2";
                    } else {
                        App.vStatus = "1";
                    }
                } catch (JSONException e) {
                    stopProgressDialog();
                    App.vStatus = "1";
                }
            }
            stopProgressDialog();
        }

    };
    private String userid, userhead, usernickname, userblurb, fans, guanzhu,
            audioCount, videoCount, members, provinces, shield;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.tab_frag5, container, false);

        findView(view);
       if (MainActivity.ishongdian){
           hongdian.setVisibility(View.VISIBLE);
       }

        api = WXAPIFactory.createWXAPI(context, URLManager.WXappId, true);
        mTencent = Tencent.createInstance(URLManager.QQappId,context);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getuser();

    }

    private String TAG = "HomeFragment3";
    private String logs;

    private void getuser() {
        userid = Futil.getValue(getActivity(), "userid");
        if (userid != null && !userid.equals("")) {
            gointo();
            ll.setVisibility(View.VISIBLE);
            myown_name.setVisibility(View.VISIBLE);
            blurbll.setVisibility(View.VISIBLE);
            getvStatus();
        } else {
            rImageView.setImageResource(R.mipmap.touxiang);
            ll.setVisibility(View.GONE);
            myown_name.setVisibility(View.GONE);
            blurbll.setVisibility(View.GONE);
            myown_member.setVisibility(View.GONE);
        }

    }

    private void getvStatus() {
        startProgressDialog();
        String strUrl = URLManager.jiaV;
        ;
        HashMap<String, String> map = new HashMap<String, String>();
        if (!userid.equals("")) {
            map.put("user.id", userid);
        }
        Futil.xutils(strUrl, map, handler, URLManager.three);
    }

    private void setaliasandtag() {
        String hasAlias = Futil.getValue(context, "hasAlias");
        String hasTags = Futil.getValue(context, "hasTags");
        if (!hasAlias.equals("1")) {
            if (userid != null && !userid.equals("")) {

                JPushInterface.setAlias(context, userid, new TagAliasCallback() {

                    @Override
                    public void gotResult(int code, String alias, Set<String> tags) {
                        switch (code) {
                            case 0:
                                logs = "Set tag and alias success";
                                Futil.saveValue(context, "hasAlias", "1");
                                break;

                            case 6002:
                                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                                Log.i(TAG, logs);
                                break;
                            default:
                                logs = "Failed with errorCode = " + code;
                                Log.e(TAG, logs);
                        }
                    }
                });
            }
        }
        if (!hasTags.equals("1")) {
            if (provinces != null && !provinces.equals("") && !provinces.equals("null")) {
                Set<String> arg1 = new HashSet<String>();
                arg1.add(provinces);
                JPushInterface.setTags(context, arg1, new TagAliasCallback() {

                    @Override
                    public void gotResult(int code, String alias, Set<String> tags) {
                        switch (code) {
                            case 0:
                                logs = "Set tag and alias success";
                                Futil.saveValue(context, "hasTags", "1");
                                System.out.println("hasTags--provinces---" + provinces);
                                break;

                            case 6002:
                                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                                Log.i(TAG, logs);
                                break;
                            default:
                                logs = "Failed with errorCode = " + code;
                                Log.e(TAG, logs);
                        }
                    }
                });
            }
        }
    }

    private RoundedImageView rImageView;

    private void setuser(String type) {
        Futil.saveValue(context, "shield", shield);

        myown_numtv_shengyin.setText(audioCount);
        myown_numtv_shipin.setText(videoCount);
        myown_numtv_guanzhu.setText(guanzhu);
        myown_numtv_fensi.setText(fans);
        if (!type.equals("http")) {
            userhead = URLManager.Head_URL + userhead;
        }
        System.out.println("" + userhead);
        Picasso.with(getActivity()).load(userhead).resize(400, 400)
                .error(R.mipmap.touxiang).centerCrop().into(rImageView);
        if (userblurb != null && !userblurb.equals("")
                && !userblurb.equals("null")) {
            myown_blurb.setText(userblurb);
        } else {
            myown_blurb.setText("这个人很懒不要理他~");
        }
        if (members.equals("2")) {
            myown_member.setVisibility(View.VISIBLE);
        } else {
            myown_member.setVisibility(View.GONE);
        }
        myown_name.setText(usernickname);

    }

    private void gointo() {
        startProgressDialog();
        String strUrl = URLManager.Myown;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        Futil.xutils(strUrl, map, handler, URLManager.one);

    }

    private ImageView setting_btn, share_btn, luyin_img, shexiang_img;
    private LinearLayout myaudio, myown_shipin, myown_guanzhu, myown_fans;
    private RelativeLayout myown_guanli, myown_xiaoxi, myown_zan,
            myown_dingyue, myown_lishi, myown_zhao, myown_bangding,
            myown_fankui, myown_setting;
    private LinearLayout ll, blurbll;
    private TextView myown_name, myown_blurb, myown_numtv_shengyin,
            myown_numtv_shipin, myown_numtv_guanzhu, myown_numtv_fensi;
    private ImageView myown_member;
    public static TextView hongdian;

    private void findView(View view) {
        //红点
        hongdian= (TextView) view.findViewById(R.id.myown_hongdian);

        // 设置
        setting_btn = (ImageView) view.findViewById(R.id.myown_setting_btn);
        setting_btn.setOnClickListener(this);
        share_btn = (ImageView) view.findViewById(R.id.myown_share_btn);
        share_btn.setOnClickListener(this);

        rImageView = (RoundedImageView) view.findViewById(R.id.myown_touxiang);
        rImageView.setOnClickListener(this);
        myown_name = (TextView) view.findViewById(R.id.myown_name);
        myown_blurb = (TextView) view.findViewById(R.id.myown_blurb);
        myown_numtv_shengyin = (TextView) view
                .findViewById(R.id.myown_numtv_shengyin);
        myown_numtv_shipin = (TextView) view
                .findViewById(R.id.myown_numtv_shipin);
        myown_numtv_guanzhu = (TextView) view
                .findViewById(R.id.myown_numtv_guanzhu);
        myown_numtv_fensi = (TextView) view
                .findViewById(R.id.myown_numtv_fensi);
        myown_member = (ImageView) view.findViewById(R.id.myown_member);

        ll = (LinearLayout) view.findViewById(R.id.ll);
        blurbll = (LinearLayout) view.findViewById(R.id.blurbll);

        // 我的声音
        myaudio = (LinearLayout) view.findViewById(R.id.myaudio);
        myaudio.setOnClickListener(this);
        // 我的视频
        myown_shipin = (LinearLayout) view.findViewById(R.id.myown_shipin);
        myown_shipin.setOnClickListener(this);
        // 我的关注
        myown_guanzhu = (LinearLayout) view.findViewById(R.id.myown_guanzhu);
        myown_guanzhu.setOnClickListener(this);
        // 我的粉丝
        myown_fans = (LinearLayout) view.findViewById(R.id.myown_fans);
        myown_fans.setOnClickListener(this);

        // 录音
        luyin_img = (ImageView) view.findViewById(R.id.myown_luyin_img);
        luyin_img.setOnClickListener(this);
        // 录音
        shexiang_img = (ImageView) view.findViewById(R.id.myown_shexiang_img);
        shexiang_img.setOnClickListener(this);

        // 管理中心
        myown_guanli = (RelativeLayout) view.findViewById(R.id.myown_guanli_rl);
        myown_guanli.setOnClickListener(this);
        // 消息中心
        myown_xiaoxi = (RelativeLayout) view.findViewById(R.id.myown_xiaoxi_rl);
        myown_xiaoxi.setOnClickListener(this);

        // 我赞过
        myown_zan = (RelativeLayout) view.findViewById(R.id.myown_zan_rl);
        myown_zan.setOnClickListener(this);
        // 我的订阅
        myown_dingyue = (RelativeLayout) view
                .findViewById(R.id.myown_dingyue_rl);
        myown_dingyue.setOnClickListener(this);
        // 播放历史
        myown_lishi = (RelativeLayout) view.findViewById(R.id.myown_lishi_rl);
        myown_lishi.setOnClickListener(this);
        // 找朋友
        myown_zhao = (RelativeLayout) view.findViewById(R.id.myown_zhao_rl);
        myown_zhao.setOnClickListener(this);
        // 账号绑定
        myown_bangding = (RelativeLayout) view
                .findViewById(R.id.myown_bangding_rl);
        myown_bangding.setOnClickListener(this);
        // 意见反馈
        myown_fankui = (RelativeLayout) view.findViewById(R.id.myown_fankui_rl);
        myown_fankui.setOnClickListener(this);
        // 设置
        myown_setting = (RelativeLayout) view
                .findViewById(R.id.myown_setting_rl);
        myown_setting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (userid == null || userid.equals("")) {
            ActivityJumpControl.getInstance(getActivity()).gotoLoginActivity();
            return;
        }
        switch (v.getId()) {
            case R.id.myown_setting_btn:// // 设置
                ActivityJumpControl.getInstance(getActivity())
                        .gotoSettingActivity();
                break;
            case R.id.myown_touxiang:// 修改个人信息
                ActivityJumpControl.getInstance(getActivity())
                        .gotoInformationActivity();
                break;
            case R.id.myaudio:// 我的声音
                ActivityJumpControl.getInstance(getActivity()).gotoMyAudioActivity(
                        "1", "1", "我的声音");
                break;
            case R.id.myown_shipin:// 我的视频
                ActivityJumpControl.getInstance(getActivity()).gotoMyAudioActivity(
                        "1", "3", "我的视频");
                break;

            case R.id.myown_fans:// 我的粉丝
                ActivityJumpControl.getInstance(getActivity())
                        .gotoMyConcemFansActivity("1", "粉丝");
                break;
            case R.id.myown_guanzhu:// 我的关注
                ActivityJumpControl.getInstance(getActivity())
                        .gotoMyConcemFansActivity("2", "关注");
                break;
            case R.id.myown_luyin_img:// 录音
                ActivityJumpControl.getInstance(getActivity()).gotoRecordActivity();
                luyin_img.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        luyin_img.setEnabled(true);
                    }
                }, 5000);
                break;
            case R.id.myown_shexiang_img:// 录视频
                videoPermissions(getActivity());
                ActivityJumpControl.getInstance(getActivity())
                        .gotoVideoTapeActivity();
                break;
            case R.id.myown_xiaoxi_rl:// 消息中心
                ActivityJumpControl.getInstance(getActivity())
                        .gotoMessageActivity();
                hongdian.setVisibility(View.GONE);
                MainActivity.ishongdian=false;
                break;
            case R.id.myown_guanli_rl:// 管理中心
                ActivityJumpControl.getInstance(getActivity()).gotoManageActivity();
                break;
            case R.id.myown_zan_rl:// 我赞过
                ActivityJumpControl.getInstance(getActivity()).gotoMyPHSActivity(
                        "1", "我赞过");
                break;
            case R.id.myown_dingyue_rl:// 我的订阅
                ActivityJumpControl.getInstance(getActivity()).gotoMyPHSActivity(
                        "2", " 我的订阅");
                break;
            case R.id.myown_lishi_rl:// 播放历史
                ActivityJumpControl.getInstance(getActivity()).gotoMyPHSActivity(
                        "3", "播放历史");
                break;
            case R.id.myown_fankui_rl:// 意见反馈
                ActivityJumpControl.getInstance(getActivity())
                        .gotoFeedbackActivity();
                break;
            case R.id.myown_zhao_rl:// 找朋友
                ActivityJumpControl.getInstance(getActivity())
                        .gotoFindFriendActivity();
                break;

            case R.id.myown_setting_rl:// // 设置
                ActivityJumpControl.getInstance(getActivity())
                        .gotoSettingActivity();
                break;
            case R.id.myown_bangding_rl:// z账号绑定
                ActivityJumpControl.getInstance(getActivity())
                        .gotoBangdingActivity();
                break;
            case R.id.myown_share_btn:// 分享
                openshare();
                break;
            default:
                break;
        }
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 0;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_SETTINGS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * @param activity
     */
    public static void videoPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA);
        int permission1 = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO);
        int permission2 = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission3 = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);

        if (permission != PackageManager.PERMISSION_GRANTED||permission1 != PackageManager.PERMISSION_GRANTED||permission2 != PackageManager.PERMISSION_GRANTED||permission3 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    /**
     * 等待页
     */
    private CustomProgressDialog progressDialog;

    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(getActivity());
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

    private AlertDialog dialog;

    private void openshare() {
        dialog = new AlertDialog.Builder(getActivity())
                .setTitle("title").setMessage("message").create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_share);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle);  //添加动画

        dialog.findViewById(R.id.share_qq).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_kj).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_wx).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                sendWx(1,"");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_pyq).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWx(2,"");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_qx).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
        });
    }
    /**
     * @param i(1:分享到微信好友，2分享到朋友圈)
     */
    IWXAPI api;

    private void sendWx(final int i, String bitmapurl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = URLManager.share;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "这是消息标题";
        msg.description = "这是消息内容";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (i == 1) req.scene = SendMessageToWX.Req.WXSceneSession;//
        if (i == 2) req.scene = SendMessageToWX.Req.WXSceneTimeline;//
        api.sendReq(req);
        /*
        Picasso.with(context).load(bitmapurl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                // 设置想要的大小
                int newWidth = 99;
                int newHeight = 99;
                // 计算缩放比例
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // 取得想要缩放的matrix参数
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的图片
                Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                        matrix, true);
                msg.thumbData = Bitmap2Bytes(newBitMap);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                if (i == 1) req.scene = SendMessageToWX.Req.WXSceneSession;//
                if (i == 2) req.scene = SendMessageToWX.Req.WXSceneTimeline;//
                api.sendReq(req);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });*/
    }
    Tencent mTencent;
    public void share() {
        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
//        bundle.putString(SocialConstants.PARAM_TARGET_URL, "http://60.174.234.45:8080/headlines/article/appArticleInfo?aid="+aid+"&uid=0");
        bundle.putString(SocialConstants.PARAM_TARGET_URL, URLManager.share);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(SocialConstants.PARAM_TITLE, "这是消息标题");
        //分享的图片URL
//        bundle.putString(SocialConstants.PARAM_IMAGE_URL, imgUrl);
        //分享的消息摘要，最长50个字
        bundle.putString(SocialConstants.PARAM_SUMMARY, "这是消息内容");
        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(SocialConstants.PARAM_APPNAME, "慧思语");
        //标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(SocialConstants.PARAM_APP_SOURCE, "慧思语" + URLManager.QQappId);

        mTencent.shareToQQ(getActivity(), bundle, new IUiListener() {
            @Override
            public void onComplete(Object o) {
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
