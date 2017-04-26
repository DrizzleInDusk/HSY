package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zm.hsy.util.Code;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/** 登陆 */
public class LoginActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            stopProgressDialog();
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        try {
                            JSONObject data = obj.getJSONObject("userRank");
                            JSONObject user = data.getJSONObject("user");

                            String userid = user.getString("id");
                            Futil.romveValue(LoginActivity.this, "userid", "4");
                            String value = Futil.getValue(LoginActivity.this,
                                    "userid");
                            System.out.println("----" + value);
                            Futil.saveValue(LoginActivity.this, "userid",
                                    userid);
                            System.out.println("----" + userid);
                            LoginActivity.this.finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopProgressDialog();
                        }
                    } else {
                        String message = obj.getString("message");
                        Futil.showMessage(LoginActivity.this, "" + message);
                    }
                } catch (JSONException e) {
                    stopProgressDialog();
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

            }
        }

    };

    Code code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 配置需要分享的相关平台
        configPlatforms();
        initFindId();
        yanzhengma_tv1.setVisibility(View.VISIBLE);
        yanzhengma_img.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        while (isWXLogin) {
            if (WX_CODE != null) {
                loadWXUserInfo();
                isWXLogin = false;
            }
        }
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

    private ImageView back_top, login_share, login_password, yanzhengma_img;
    private ImageView login_log_in, login_register, login_qq, login_wx,
            login_wb;
    private TextView forget_password, yanzhengma_tv1;
    private EditText name_et;
    private EditText password_et;
    private EditText yanzhengma_val;

    private void initFindId() {
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);
        login_share = (ImageView) findViewById(R.id.login_share);
        login_share.setOnClickListener(this);

        name_et = (EditText) findViewById(R.id.login_name_et);
        password_et = (EditText) findViewById(R.id.login_password_et);
        yanzhengma_val = (EditText) findViewById(R.id.login_yanzhengma_val);
        yanzhengma_tv1 = (TextView) findViewById(R.id.yanzhengma_tv1);
        yanzhengma_tv1.setOnClickListener(this);
        yanzhengma_img = (ImageView) findViewById(R.id.login_yanzhengma_img);
        yanzhengma_img.setOnClickListener(this);
        login_password = (ImageView) findViewById(R.id.login_password);
        login_password.setOnClickListener(this);

        login_log_in = (ImageView) findViewById(R.id.login_log_in);
        login_register = (ImageView) findViewById(R.id.login_register);
        forget_password = (TextView) findViewById(R.id.login_forget_password);

        login_log_in.setOnClickListener(this);
        login_register.setOnClickListener(this);
        forget_password.setOnClickListener(this);

        login_qq = (ImageView) findViewById(R.id.login_qq);
        login_qq.setOnClickListener(this);
        login_wx = (ImageView) findViewById(R.id.login_wx);
        login_wx.setOnClickListener(this);
        login_wb = (ImageView) findViewById(R.id.login_wb);
        login_wb.setOnClickListener(this);

    }

    private String username, password;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 显示隐藏密码
            case R.id.login_password:
                if (!login_password.isSelected()) {
                    // 显示密码
                    password_et
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    login_password.setSelected(true);
                } else {
                    // 隐藏密码
                    password_et.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    login_password.setSelected(false);
                }
                break;

            // 显示验证随机数
            case R.id.yanzhengma_tv1:
                username = name_et.getText().toString();
                password = password_et.getText().toString();
                if (username.length() == 0) {
                    Futil.showMessage(LoginActivity.this, "请输入昵称或手机号");
                } else if (password.length() == 0) {
                    Futil.showMessage(LoginActivity.this, "请输入密码");
                } else {
                    yanzhengma_tv1.setVisibility(View.GONE);
                    yanzhengma_img.setVisibility(View.VISIBLE);
                    yanzhengma_img
                            .setImageBitmap(code.getInstance().createBitmap());
                }
                break;
            // 更换验证随机数
            case R.id.login_yanzhengma_img:
                yanzhengma_img.setImageBitmap(code.getInstance().createBitmap());
                break;
            // 登陆
            case R.id.login_log_in:
                String yanzhengma = yanzhengma_val.getText().toString();
                String ycode = code.getInstance().getCode();
                if (yanzhengma.length() == 0) {
                    Futil.showMessage(LoginActivity.this, "请输入验证码");
                } else if (!yanzhengma.equals(ycode)) {
                    Futil.showMessage(LoginActivity.this, "验证码错误");
                } else {
                    username = name_et.getText().toString().trim();
                    password = password_et.getText().toString().trim();
                    login();
                }
                break;
            case R.id.login_forget_password:
                ActivityJumpControl.getInstance(LoginActivity.this)
                        .gotoForgetPWActivity("1", "忘记密码");
                finish();
                break;
            case R.id.login_register:
                ActivityJumpControl.getInstance(LoginActivity.this)
                        .gotoForgetPWActivity("2", "注册");
                finish();
                break;
            case R.id.login_qq: {
                if (!mTencent.isSessionValid()) {
                    mTencent.login(this, scope, loginListener);
                }
            }
            break;
            case R.id.login_wx: {
                if (!api.isWXAppInstalled()) {
                    Toast.makeText(LoginActivity.this, "您还未安装微信客户端",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                goWXApi();
            }
            break;
            case R.id.login_wb:
                Toast.makeText(this, "暂不支持微博登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.back_top:
                finish();
                break;
        }

    }

    private void login() {
        startProgressDialog();
        String strUrl = URLManager.Login;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("loginContent", username);
        map.put("password", password);
        System.out.println("username---" + username);
        System.out.println("password---" + password);
        Futil.xutils(strUrl, map, handler, URLManager.one);
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
                Futil.showMessage(LoginActivity.this, "登录出错");
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
                        Futil.showMessage(LoginActivity.this, "登录成功");

                        sns = "qq";

                        String openID = jo.getString("openid");

                        sns_id = openID;
                        String accessToken = jo.getString("access_token");
                        String expires = jo.getString("expires_in");
                        mTencent.setOpenId(openID);
                        mTencent.setAccessToken(accessToken, expires);
                        System.out.println("开始获取用户信息");
                        userInfo = new UserInfo(LoginActivity.this,
                                mTencent.getQQToken());
                        userInfo.getUserInfo(userInfoListener);
                    }

                } catch (Exception e) {
                }

            }

            @Override
            public void onCancel() {
                Futil.showMessage(LoginActivity.this, "登录取消");
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

                Futil.showMessage(LoginActivity.this, "登录出错");
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
                    System.out.println("sns_avatar---" + sns_avatar);
                    sns_loginname = jo.getString("nickname");
                    System.out.println("sns_loginname---" + sns_loginname);
                    thirdlogin();

                } catch (Exception e) {
                }

            }

            @Override
            public void onCancel() {
                Futil.showMessage(LoginActivity.this, "登录取消");
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
        String strUrl = URLManager.ThirdLogin;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("loginType", sns);
        map.put("openid", sns_id);
        map.put("head", sns_avatar);
        map.put("nickname", sns_loginname);
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