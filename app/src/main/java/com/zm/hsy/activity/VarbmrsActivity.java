package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/*** 活动报名人数 */
public class VarbmrsActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    JSONArray asList = obj.getJSONArray("asList");
                    userlist = new ArrayList<>();
                    for (int i = 0; i < asList.length(); i++) {
                        JSONObject as = asList.getJSONObject(i);
                        User user = new User();
                        user.setAge(as.getString("age"));
                        user.setHead(as.getString("head"));
                        user.setHeadStatus(as.getString("headStatus"));
                        user.setId(as.getString("id"));
                        user.setMembers(as.getString("members"));
                        user.setNickname(as.getString("nickname"));
                        user.setPhone(as.getString("phone"));
                        user.setSex(as.getString("sex"));
                        userlist.add(user);
                    }
                    isnull.setVisibility(View.GONE);
                    hdxq_listview.setVisibility(View.VISIBLE);
                    varbmrsAdapter = new VarbmrsAdapter(context, userlist);
                    hdxq_listview.setAdapter(varbmrsAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                    isnull.setVisibility(View.VISIBLE);
                    hdxq_listview.setVisibility(View.GONE);
                }

            }
            stopProgressDialog();
        }

    };

    private String userid, variationid;
    private ArrayList<User> userlist;
    private Context context;
    private VarbmrsAdapter varbmrsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varbmrs);
        context = this;
        Intent i = this.getIntent();
        variationid = i.getStringExtra("variationid");
        userid = Futil.getValue(context, "userid");
        findview();
        getMyVarSignList();
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
    private ListView hdxq_listview;
    private TextView isnull;

    private void findview() {
        isnull = (TextView) findViewById(R.id.isnull);
        hdxq_listview = (ListView) findViewById(R.id.hdxq_listview);
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

    }

    private void getMyVarSignList() {
        isnull.setVisibility(View.GONE);
        hdxq_listview.setVisibility(View.GONE);
        startProgressDialog();
        String strUrl = URLManager.getMyVarSignList;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("activity.id", variationid);
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

    public class VarbmrsAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater minflater;
        private ArrayList<User> userlist;
        ;

        public VarbmrsAdapter(Context context, ArrayList<User> userlist) {
            super();
            this.minflater = LayoutInflater.from(context);
            this.userlist = userlist;
            this.context = context;
        }

        @Override
        public int getCount() {
            return userlist.size();
        }

        @Override
        public Object getItem(int position) {
            return userlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = minflater.inflate(R.layout.activity_varbmrs_item, null);
                holder = new ViewHolder();
                holder.uesr_name = (TextView) convertView
                        .findViewById(R.id.bmrs_uesr_name);
                holder.uesr_sex = (TextView) convertView
                        .findViewById(R.id.bmrs_uesr_sex);
                holder.uesr_age = (TextView) convertView
                        .findViewById(R.id.bmrs_uesr_age);
                holder.uesr_tel = (TextView) convertView
                        .findViewById(R.id.bmrs_uesr_tel);

                holder.uesr_head = (RoundedImageView) convertView
                        .findViewById(R.id.bmrs_uesr_head);
                holder.uesr_members = (ImageView) convertView
                        .findViewById(R.id.bmrs_uesr_members);

                holder.rl = (RelativeLayout) convertView
                        .findViewById(R.id.rl);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            User user = userlist.get(position);
            final String user_id = user.getId();
            String nickname = user.getNickname();
            String phone = user.getPhone();//定位
            String members = user.getMembers();//2会员标识
            String sex = user.getSex();//声音数
            String age = user.getAge();//粉丝数


            holder.uesr_name.setText("姓名:" + nickname);
            holder.uesr_tel.setText("电话:" + phone);
            if (members.equals("2")) {
                holder.uesr_members.setVisibility(View.VISIBLE);
            } else {
                holder.uesr_members.setVisibility(View.GONE);
            }
            if (sex.equals("1")) {
                holder.uesr_sex.setText("性别:男");
            } else {
                holder.uesr_sex.setText("性别:女");
            }
            holder.uesr_age.setText("年龄:" + age);

            String type = user.getHeadStatus();
            String head = user.getHead();
            if (!type.equals("http")) {
                head = URLManager.Head_URL + head;
            }
            Picasso.with(context).load(head).resize(400, 400).error(R.mipmap.touxiang)
                    .into(holder.uesr_head);
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg1) {
                    ActivityJumpControl.getInstance((Activity) context)
                            .gotoZhuboActivity(user_id);
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView uesr_name, uesr_sex, uesr_age, uesr_tel;
            RoundedImageView uesr_head;
            ImageView uesr_members;
            RelativeLayout rl;
        }
    }
}
