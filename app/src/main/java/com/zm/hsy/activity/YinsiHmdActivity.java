package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/** 隐私黑名单*/
public class YinsiHmdActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray ucfList = obj.getJSONArray("ucfList");
                    userlist = new ArrayList<>();
                    for (int i = 0; i < ucfList.length(); i++) {
                        JSONObject ucf = ucfList.getJSONObject(i);
                        User user = new User();
                        user.setAudioCount(ucf.getString("audioCount"));
                        user.setConcemFans(ucf.getString("concemFans"));
                        user.setDingwei(ucf.getString("dingwei"));
                        user.setHeadStatus(ucf.getString("headStatus"));
                        JSONObject us = ucf.getJSONObject("user");
                        user.setHead(us.getString("head"));
                        user.setId(us.getString("id"));
                        user.setNickname(us.getString("nickname"));
                        userlist.add(user);
                    }
                    adapter=new HmdAdapter(context,userlist);
                    hmd_listview.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }

    };

    private String userid;
    private HmdAdapter adapter;
    private ArrayList<User> userlist;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yinsiheimd);
        context = this;
        userid = Futil.getValue(context, "userid");
//        Log.i("uid", userid);
        findview();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
        super.onResume();
        blackRosterList();
    }

    @Override
    protected void onPause() {
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

        }
    }

    private ListView hmd_listview;

    private void findview() {
        findViewById(R.id.back_top).setOnClickListener(this);
        hmd_listview = (ListView) findViewById(R.id.hmd_listview);
    }

    private void blackRosterList() {
        startProgressDialog();
        String strUrl = URLManager.blackRosterList;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", userid);
        Futil.xutils(strUrl, map, handler, URLManager.one);
        stopProgressDialog();
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

    public class HmdAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater minflater;
        private ArrayList<User> userList ;

        public HmdAdapter(Context context, ArrayList<User> userList) {
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
                convertView = minflater.inflate(R.layout.activity_find_item, null);
                holder = new ViewHolder();
                holder.find_audio = (TextView) convertView
                        .findViewById(R.id.find_audio);
                holder.find_nickname = (TextView) convertView
                        .findViewById(R.id.find_nickname);
                holder.find_fans = (TextView) convertView
                        .findViewById(R.id.find_fans);
                holder.find_zuiduo = (TextView) convertView
                        .findViewById(R.id.find_zuiduo);
                holder.find_head = (RoundedImageView) convertView
                        .findViewById(R.id.find_head);
                holder.find_cutandadd_press = (ImageView) convertView
                        .findViewById(R.id.find_cutandadd_press);
                holder.find_ll = (LinearLayout) convertView
                        .findViewById(R.id.find_ll);
                holder.find_rl1 = (RelativeLayout) convertView
                        .findViewById(R.id.find_rl1);

                holder.sing_line = (View) convertView.findViewById(R.id.sing_line);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.sing_line.setVisibility(View.GONE);
            }
            String ifgz = userList.get(position).getIfgz();// 关注标识
            holder.find_cutandadd_press.setVisibility(View.GONE);
            String nickname = userList.get(position).getNickname();// 音频路径
            String concemFans = userList.get(position).getConcemFans();// 粉丝
            String audioCount = userList.get(position).getAudioCount();// 发布的声音
            String dingwei = userList.get(position).getDingwei();// 发布的声音

            holder.find_audio.setText("声音  " + audioCount);
            holder.find_nickname.setText(nickname);
            holder.find_fans.setText("粉丝  " + concemFans);
            // holder.cf_zuiduo.setText(playAmount);
            if(!dingwei.equals("")&&!dingwei.equals("null")){
                holder.find_zuiduo.setVisibility(View.VISIBLE);
                holder.find_zuiduo.setText(dingwei);
            }else{
                holder.find_zuiduo.setVisibility(View.GONE);
            }
            String cover = userList.get(position).getHead();// 封面图
            String type = userList.get(position).getHeadStatus();
            if (!type.equals("http")) {
                cover = URLManager.Head_URL + cover;
            }
            Picasso.with(context).load(cover).resize(400, 400).error(R.mipmap.details_img3).into(holder.find_head);

            holder.find_ll.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg1) {
                    ActivityJumpControl.getInstance((Activity) context)
                            .gotoZhuboActivity(userList.get(position).getId());
                }
            });
            holder.find_rl1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg1) {
                    ActivityJumpControl.getInstance((Activity) context)
                            .gotoZhuboActivity(userList.get(position).getId());

                }
            });
            return convertView;
        }

        class ViewHolder {
            private RelativeLayout find_rl1;
            private LinearLayout find_ll;
            private View sing_line;
            private RoundedImageView find_head;
            private ImageView find_cutandadd_press;
            private TextView find_audio, find_nickname, find_fans, find_zuiduo;
        }
    }

}
