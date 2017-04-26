package com.zm.hsy.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.FindUserListAdapter;
import com.zm.hsy.entity.PhoneContacts;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
import com.zm.hsy.wxapi.WXEntryActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
/** 找朋友 */
public class FindFriendActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray uListArray = obj.getJSONArray("uList");
                    for (int i = 0; i < uListArray.length(); i++) {
                        User u = new User();
                        JSONObject uList = uListArray.getJSONObject(i);
                        u.setAudioCount(uList.getString("audioCount"));
                        u.setConcemFans(uList.getString("concemFans"));
                        u.setHeadStatus(uList.getString("headStatus"));
                        u.setIfgz(uList.getString("ifgz"));

                        JSONObject user = uList.getJSONObject("user");
                        u.setHead(user.getString("head"));
                        u.setBlurb(user.getString("blurb"));
                        u.setId(user.getString("id"));
                        u.setNickname(user.getString("nickname"));
                        u.setMobile(user.getString("mobile"));
                        userList.add(u);
                    }
                    find_viewptv.setVisibility(View.VISIBLE);
                    adapter = new FindUserListAdapter(context,
                            userList, handler);
                    friend_viewp.setAdapter(adapter);
                    friendlistener();

                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                    find_viewptv.setVisibility(View.GONE);

                }
            } else if (msg.what == URLManager.two) {// 关注
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String message = obj.getString("message");
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        Toast.makeText(context, "" + message,
                                Toast.LENGTH_LONG).show();
                        find_cutandadd_press.setSelected(false);
                    } else {
                        Toast.makeText(context, "" + message,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.three) {// 取消关注
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String message = obj.getString("message");
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        Toast.makeText(context, "" + message,
                                Toast.LENGTH_LONG).show();
                        find_cutandadd_press.setSelected(true);
                    } else {
                        Toast.makeText(context, "" + message,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }

    };
    private FindUserListAdapter adapter;
    private ArrayList<User> userList;
    private String userid;
    private String pns;
    private Context context;
    private ArrayList<PhoneContacts> plist = new ArrayList<PhoneContacts>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_find);
        userList = new ArrayList<User>();
        /** 得到手机通讯录联系人信息 **/
        getPhoneContacts();
        userid = Futil.getValue(context, "userid");

        findview();
        for (int i = 0; i < plist.size(); i++) {
            PhoneContacts phoneContacts = plist.get(i);
            String phoneNumber = phoneContacts.getPhoneNumber();
            if (i + 1 == plist.size()) {
                pns = pns + phoneNumber;
            } else {
                if (i == 0) {
                    pns = phoneNumber + ",";
                } else {
                    pns = pns + phoneNumber + ",";
                }
            }
        }
        mf();
    }

    protected void onRestart() {

        super.onRestart();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.find_wx:
                share2weixin();
                break;
            case R.id.find_contacts:
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoContactsActivity();
                break;
            case R.id.back_top:
                finish();
                break;

        }
    }

    private ImageView back_top;
    private RelativeLayout find_contacts, find_wx;
    private RelativeLayout find_viewptv;
    private ListView friend_viewp;

    private void findview() {

        find_viewptv = (RelativeLayout) findViewById(R.id.find_viewptv);
        friend_viewp = (ListView) findViewById(R.id.friend_viewp);
        friend_viewp.setFocusable(false);

        find_wx = (RelativeLayout) findViewById(R.id.find_wx);
        find_wx.setOnClickListener(this);
        find_contacts = (RelativeLayout) findViewById(R.id.find_contacts);
        find_contacts.setOnClickListener(this);

        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

    }

    private IWXAPI api;

    private void share2weixin() {
        api = WXAPIFactory.createWXAPI(this, URLManager.WXappId, true);
        api.registerApp(URLManager.WXappId);
        if (!api.isWXAppInstalled()) {
            Futil.showMessage(context, "您还未安装微信客户端");
            return;
        }

        WXTextObject textobj = new WXTextObject();
        textobj.text = getResources().getString(
                R.string.app_share_weixin_txt);
//        textobj.text ="我正在使用慧思语,快来一起使用吧";

        WXMediaMessage msg = new WXMediaMessage(textobj);
        msg.mediaObject = textobj;
        // 发送文本类型的消息时，title字段不起作用
        msg.title = "";
        //描述
        msg.description = "推荐";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = buildTransaction("hsy");
//        朋友圈
//        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        //        好友
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    private ImageView find_cutandadd_press;

    private void friendlistener() {
        friend_viewp.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                find_cutandadd_press = null;
                find_cutandadd_press = (ImageView) view
                        .findViewById(R.id.find_cutandadd_press);
                final ImageView cutandadd = find_cutandadd_press;
                find_cutandadd_press.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String concemId = userList.get(position).getId();
                        if (cutandadd.isSelected()) {
                            String strUrl = URLManager.addUserConcem;
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("uid", userid);
                            map.put("concemId", concemId);
                            Futil.xutils(strUrl, map, handler, URLManager.two);
                        } else if (!cutandadd.isSelected()) {
                            String strUrl = URLManager.delUserConcem;
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("uid", userid);
                            map.put("concemId", concemId);
                            Futil.xutils(strUrl, map, handler, URLManager.three);
                        }
                    }
                });

            }
        });
    }

    private void mf() {
        startProgressDialog();
        String strUrl = URLManager.FindFriend;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        map.put("addressBook", pns);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            Phone.DISPLAY_NAME, Phone.NUMBER};

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 得到手机通讯录联系人信息
     **/
    private void getPhoneContacts() {
        ContentResolver resolver = FindFriendActivity.this.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                PhoneContacts p = new PhoneContacts();
                p.setContactName(contactName);
                p.setPhoneNumber(phoneNumber);
                plist.add(p);
            }

            phoneCursor.close();
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
