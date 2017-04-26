package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
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
/** z账号绑定 */
public class BangdingActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            stopProgressDialog();
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    String message = obj.getString("message");
                    Futil.showMessage(context, "" + message);
                    if (code.equals("2")) {
                            getuser();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (msg.what == URLManager.two) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    accessToken = object.getString("access_token");
                    openId = object.getString("openid");
                    userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="
                            + accessToken + "&openid=" + openId;
                    getWXUserId(userUrl);
                } catch (JSONException e) {
                    sns = "";
                    sns_id = "";
                    sns_avatar = "";
                    sns_loginname = "";
                }

            } else if (msg.what == URLManager.three) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    sns = "wx";
                    sns_id = object.getString("openid");
                    System.out.println("--sns_id--" + sns_id);
                    sns_avatar = object.getString("headimgurl");
                    System.out.println("--sns_avatar--" + sns_avatar);
                    sns_loginname = object.getString("nickname");
                    System.out.println("--sns_loginname--" + sns_loginname);

                    thirdlogin();
                } catch (JSONException e) {
                    sns = "";
                    sns_id = "";
                    sns_avatar = "";
                    sns_loginname = "";
                }

            } else if (msg.what == URLManager.four) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONObject data = obj.getJSONObject("userRank");
                    JSONObject userdata = data.getJSONObject("user");
                    String qqtype = userdata.getString("qq");
                    String wbtype = userdata.getString("weibo");
                    String wxtype = userdata.getString("weixin");
                    String sjtype = userdata.getString("mobile");
                    if (!qqtype.equals("") && !qqtype.equals("null")) {
                        qqbd_tv.setVisibility(View.VISIBLE);
                        qqbd_rl.setEnabled(false);
                        System.out.println("qqtype>>" + qqtype);
                    } else {
                        qqbd_tv.setVisibility(View.GONE);
                        qqbd_rl.setEnabled(true);
                    }
                    if (!wxtype.equals("") && !wxtype.equals("null")) {
                        wxbd_tv.setVisibility(View.VISIBLE);
                        wxbd_rl.setEnabled(false);
                        System.out.println("wxtype>>" + wxtype);

                    } else {
                        wxbd_tv.setVisibility(View.GONE);
                        wxbd_rl.setEnabled(true);
                    }
                    if (!wbtype.equals("") && !wbtype.equals("null")) {
                        wbbd_tv.setVisibility(View.VISIBLE);
                        wbbd_rl.setEnabled(false);
                        System.out.println("wbtype>>" + wbtype);
                    } else {
                        wbbd_tv.setVisibility(View.GONE);
                        wbbd_rl.setEnabled(true);
                    }
                    if (!sjtype.equals("") && !sjtype.equals("null")) {
                        sjbd_tv.setVisibility(View.VISIBLE);
                        sjbd_rl.setEnabled(false);
                        System.out.println("sjtype>>" + sjtype);
                    } else {
                        sjbd_tv.setVisibility(View.GONE);
                        sjbd_rl.setEnabled(true);
                    }
                } catch (JSONException e) {
                    wxbd_tv.setVisibility(View.GONE);
                    qqbd_tv.setVisibility(View.GONE);
                    wbbd_tv.setVisibility(View.GONE);
                    sjbd_tv.setVisibility(View.GONE);
                    sjbd_rl.setEnabled(true);
                    qqbd_rl.setEnabled(true);
                    wxbd_rl.setEnabled(true);
                    wbbd_rl.setEnabled(true);
                }

            }
        }

    };

    private String userid;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangding);
        ActivityJumpControl.getInstance(this).pushActivity(this);
        context = this;
        Intent i = this.getIntent();
        userid = Futil.getValue(context, "userid");
        // 配置需要分享的相关平台
        configPlatforms();
        findview();
        getuser();
    }

    @Override
    protected void onResume() {
        while (isWXLogin) {
            if (WX_CODE != null) {
                loadWXUserInfo();
                isWXLogin = false;
            }
        }
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
            case R.id.wxbd_rl: {
                if (!api.isWXAppInstalled()) {
                    Futil.showMessage(context, "您还未安装微信客户端");
                    return;
                }
                goWXApi();
            }
            break;
            case R.id.wbbd_rl:

                break;
            case R.id.qqbd_rl: {
                if (!mTencent.isSessionValid()) {
                    mTencent.login(this, scope, loginListener);
                }
            }
            break;
            case R.id.sjbd_rl:
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoBangdingSjActivity();
                break;
            case R.id.back_top:
                finish();
                break;
        }
    }

    private RelativeLayout wxbd_rl, wbbd_rl, qqbd_rl, sjbd_rl;
    private TextView wxbd_tv, wbbd_tv, qqbd_tv, sjbd_tv;

    private void findview() {

        wxbd_tv = (TextView) findViewById(R.id.wxbd_tv);
        wxbd_rl = (RelativeLayout) findViewById(R.id.wxbd_rl);
        wxbd_rl.setOnClickListener(this);
        wxbd_rl.setEnabled(false);

        wbbd_tv = (TextView) findViewById(R.id.wbbd_tv);
        wbbd_rl = (RelativeLayout) findViewById(R.id.wbbd_rl);
        wbbd_rl.setOnClickListener(this);
        wbbd_rl.setEnabled(false);

        sjbd_tv = (TextView) findViewById(R.id.sjbd_tv);
        sjbd_rl = (RelativeLayout) findViewById(R.id.sjbd_rl);
        sjbd_rl.setOnClickListener(this);
        sjbd_rl.setEnabled(false);

        qqbd_tv = (TextView) findViewById(R.id.qqbd_tv);
        qqbd_rl = (RelativeLayout) findViewById(R.id.qqbd_rl);
        qqbd_rl.setOnClickListener(this);
        qqbd_rl.setEnabled(false);
        findViewById(R.id.back_top).setOnClickListener(this);

    }

    public static boolean isWXLogin = false;

    private void goWXApi() {
        isWXLogin = true;
        SendAuth.Req req = new SendAuth.Req();
        // 授权读取用户信息
        req.scope = "snsapi_userinfo";
        // 自定义信息
        req.state = "hsy_login";
        // 向微信发送请求
        api.sendReq(req);

    }

    /**
     * 配置分享平台参数
     */
    private void configPlatforms() {
        // 添加QQ、QZone平台
        addQQQZonePlatform();
        // 添加微信平台
        addWXPlatform();

    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private Tencent mTencent;
    private IUiListener loginListener; // 授权登录监听器
    private IUiListener userInfoListener; // 获取用户信息监听器
    private String scope; // 获取信息的范围参数
    private UserInfo userInfo; // qq用户信息

    private void addQQQZonePlatform() {
        // 初始化qq主操作对象
        mTencent = Tencent.createInstance(URLManager.QQappId,
                this.getApplicationContext());
        // 要所有权限，不然会再次申请增量权限，这里不要设置成get_user_info,add_t
        scope = "all";

        loginListener = new IUiListener() {

            @Override
            public void onError(UiError arg0) {
                Futil.showMessage(context, "授权出错");
            }

            @Override
            public void onComplete(Object value) {

                if (value == null) {
                    return;
                }

                try {
                    JSONObject jo = (JSONObject) value;

                    int ret = jo.getInt("ret");

                    System.out.println("json=" + String.valueOf(jo));

                    if (ret == 0) {

                        sns = "qq";

                        String openID = jo.getString("openid");

                        sns_id = openID;
                        String accessToken = jo.getString("access_token");
                        String expires = jo.getString("expires_in");
                        mTencent.setOpenId(openID);
                        mTencent.setAccessToken(accessToken, expires);
                        System.out.println("开始获取用户信息");
                        userInfo = new UserInfo(context,
                                mTencent.getQQToken());
                        userInfo.getUserInfo(userInfoListener);
                    }

                } catch (Exception e) {
                }

            }

            @Override
            public void onCancel() {
                Futil.showMessage(context, "取消授权");
                sns = "";
                sns_id = "";
                sns_avatar = "";
                sns_loginname = "";

            }
        };

        userInfoListener = new IUiListener() {

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub

                Futil.showMessage(context, "授权出错");
            }

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                if (arg0 == null) {
                    return;
                }
                try {
                    JSONObject jo = (JSONObject) arg0;
                    int ret = jo.getInt("ret");
                    System.out.println("json=" + String.valueOf(jo));

                    sns_avatar = jo.getString("figureurl_qq_2");
                    sns_loginname = jo.getString("nickname");
                    thirdlogin();

                } catch (Exception e) {
                }

            }

            @Override
            public void onCancel() {
                Futil.showMessage(context, "取消授权");
                sns = "";
                sns_id = "";
                sns_avatar = "";
                sns_loginname = "";
            }
        };
    }

    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    public static String WX_CODE;

    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, URLManager.WXappId, false);
        // 将该app注册到微信
        api.registerApp(URLManager.WXappId);

    }

    /**
     * QQ登陆回掉
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN
                || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data,
                    loginListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取用户信息
     *
     * @param platform
     */
    private String sns = "";
    private String sns_id = "";
    private String sns_avatar = "";
    private String sns_loginname = "";

    private void thirdlogin() {
        String strUrl = URLManager.bindings;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("loginType", sns);
        map.put("openid", sns_id);
        map.put("user.id", userid);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    /**
     * @methods: 获得微信用户信息
     * @author: lianzhi
     * @Date: 2015-3-5
     */
    String userUrl, openId, accessToken;

    private void loadWXUserInfo() {
        final String strUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + URLManager.WXappId
                + "&secret="
                + URLManager.WXappSecret
                + "&code=" + WX_CODE + "&grant_type=authorization_code";
        Futil.xutils(strUrl, handler, URLManager.two);
        isWXLogin = false;
    }

    private void getWXUserId(String userUrl) {
        HashMap<String, String> map = new HashMap<String, String>();
        Futil.xutils(userUrl, handler, URLManager.three);
    }

    private void getuser() {
        startProgressDialog();
        String strUrl = URLManager.Myown;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        Futil.xutils(strUrl, map, handler, URLManager.four);
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
