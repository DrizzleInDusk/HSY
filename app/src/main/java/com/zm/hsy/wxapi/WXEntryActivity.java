package com.zm.hsy.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zm.hsy.activity.BangdingActivity;
import com.zm.hsy.activity.LoginActivity;
import com.zm.hsy.https.URLManager;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static String TAG = "WXEntryActivity";
    private IWXAPI api;
    public static BaseResp mResp = null;

    // 是否有新的认证请求
    public static boolean hasNewAuth = false;
    private TextView mTvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate<-----------------------------");
        mTvCode = new TextView(this);
        mTvCode.setText("no code");
        setContentView(mTvCode);
        api = WXAPIFactory.createWXAPI(this, URLManager.WXappId, true);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq arg0) {
    }

    /**
     * 认证后会回调该方法
     */
    @Override
    public void onResp(BaseResp resp) {
        Log.e(TAG, "onResp...");
        String code = null;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK://用户同意,只有这种情况的时候code是有效的
                code = ((SendAuth.Resp) resp).code;
                LoginActivity.WX_CODE = code;


                BangdingActivity.WX_CODE = code;

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消

                break;

            default://发送返回

                break;
        }
        finish();
    }
}

