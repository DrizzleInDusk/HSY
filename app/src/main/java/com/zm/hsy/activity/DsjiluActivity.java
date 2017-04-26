package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 打赏记录 */
public class DsjiluActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            stopProgressDialog();
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray dsList = obj.getJSONArray("dsList");
                    userList = new ArrayList<>();
                    for (int i = 0; i < dsList.length(); i++) {
                        JSONObject ds = dsList.getJSONObject(i);
                        User user = new User();
                        user.setAudioName(ds.getString("audioName"));
                        user.setNickname(ds.getString("nickName"));
                        user.setPayMode(ds.getString("payMode"));
                        user.setPayMoney(ds.getString("payMoney"));
                        user.setAddTime(ds.getString("time"));
                        userList.add(user);
                    }
                    isno.setVisibility(View.GONE);
                    dsjilu_viewp.setVisibility(View.VISIBLE);
                    adapter = new DsjiluAdapter(context, userList);
                    dsjilu_viewp.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    isno.setVisibility(View.VISIBLE);
                    dsjilu_viewp.setVisibility(View.GONE);
                }

            }
        }

    };

    private String userid;
    private DsjiluAdapter adapter;
    private ArrayList<User> userList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsjilu);
        context = this;
        userid = Futil.getValue(context, "userid");
        findview();
        getaccountInfo();
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
            case R.id.back_top:
                finish();
                break;

        }
    }

    private ImageView back_top;
    private ListView dsjilu_viewp;
    private TextView isno;

    private void findview() {
        isno = (TextView) findViewById(R.id.isno);
        dsjilu_viewp = (ListView) findViewById(R.id.dsjilu_viewp);
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

    }

    private void getaccountInfo() {
        startProgressDialog();
        String strUrl = URLManager.PayListByUid;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", userid);
        System.out.println("uid"+userid);
        Futil.xutils(strUrl, map, handler, URLManager.one);
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

    public class DsjiluAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater minflater;
        private ArrayList<User> userList;

        public DsjiluAdapter(Context context, ArrayList<User> userList) {
            super();
            this.minflater = LayoutInflater.from(context);
            this.userList = userList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = minflater.inflate(R.layout.activity_dsjilu_item, null);
                holder = new ViewHolder();
                holder.dsjilu_addtime = (TextView) convertView
                        .findViewById(R.id.dsjilu_addtime);
                holder.dsjilu_item_content = (TextView) convertView
                        .findViewById(R.id.dsjilu_item_content);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String audioName = userList.get(position).getAudioName();
            String nickname = userList.get(position).getNickname();
            String payMode = userList.get(position).getPayMode();
            String payMoney = userList.get(position).getPayMoney();
            String addTime = userList.get(position).getAddTime();
            holder.dsjilu_addtime.setText("打赏时间: " + addTime);
            holder.dsjilu_item_content.setText(Html.fromHtml("<font color=\"#1abc9c\">" + nickname + " </font><font color=\"black\"> " +
                    "通过 </font><font color=\"#FF0000\"> " + payMode + "</font><font color=\"black\"> 给你的声音 </font><font color=\"#1abc9c\">" + audioName + " </font>" +
                    "</font><font color=\"black\"> 打赏了 </font><font color=\"#FF0000\"> " + payMoney + "</font><font color=\"black\"> 元 </font>"));
//            holder.dsjilu_item_content.setText(nickname + "通过" + payMode + "给你的声音" + audioName + "打赏了" + payMoney + "元");

            return convertView;
        }

        class ViewHolder {
            private TextView dsjilu_addtime, dsjilu_item_content;
        }

    }
}
