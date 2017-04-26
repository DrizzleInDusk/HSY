package com.zm.hsy.activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.entity.Topic;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 社区发帖信息 */
public class MytieActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            stopProgressDialog();
            if (msg.what == URLManager.one) {
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    JSONArray mctList = obj.getJSONArray("mctList");
                    tbbsList = new ArrayList<>();
                    for (int j = 0; j < mctList.length(); j++) {
                        JSONObject htobj = mctList.getJSONObject(j);
                        Topic t = new Topic();
                        t.setNickname(htobj.getString("nickname"));
                        t.setName(htobj.getString("name"));
                        t.setContent(htobj.getString("content"));
                        t.setCommunityId(htobj.getString("id"));
                        t.setTitle(htobj.getString("title"));
                        t.setId(htobj.getString("ctid"));
                        t.setYycontent(htobj.getString("yycontent"));
                        tbbsList.add(t);
                    }
                    isnull.setVisibility(View.GONE);
                    tie_listview.setVisibility(View.VISIBLE);
                    tadapter = new MytieAdapter(context, tbbsList);
                    tie_listview.setAdapter(tadapter);
                } catch (Exception e) {
                    e.printStackTrace();
                    isnull.setVisibility(View.VISIBLE);
                    tie_listview.setVisibility(View.GONE);
                }

            }else if (msg.what == URLManager.two) {
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    String message = obj.getString("message");
                    Futil.showMessage(context,message);
                    String code = obj.getString("code");
                    if(code.equals("2")){
                        getMyTopic();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    };

    private String userid, mytitle, type;
    private ArrayList<Topic> tbbsList;
    private MytieAdapter tadapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytie);
        context = this;
        mytitle = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        userid = Futil.getValue(context, "userid");
        findview();
        tie_top_title.setText("" + mytitle);
        getMyTopic();
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
    private TextView tie_top_title,isnull;
    private ListView tie_listview;

    private void findview() {

        tie_top_title = (TextView) findViewById(R.id.tie_top_title);
        isnull = (TextView) findViewById(R.id.isnull);
        tie_listview = (ListView) findViewById(R.id.tie_listview);
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);

    }

    private void getMyTopic() {
        isnull.setVisibility(View.GONE);
        tie_listview.setVisibility(View.GONE);
        startProgressDialog();
        String strUrl = URLManager.getMyTopic;
        HashMap<String, String> map = new HashMap<>();
        map.put("user.id", userid);
        map.put("type", type);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }
    private void delMyTopic(String tid) {
        startProgressDialog();
        String strUrl = URLManager.delMyTopic;
        HashMap<String, String> map = new HashMap<>();
        map.put("communityTopic.id", tid);
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

    public class MytieAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater minflater;
        private ArrayList<Topic> bbsList;
        private AlertDialog dialog;

        public MytieAdapter(Context context, ArrayList<Topic> bbsList) {
            super();
            this.minflater = LayoutInflater.from(context);
            this.bbsList = bbsList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return bbsList.size();
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
                convertView = minflater
                        .inflate(R.layout.activity_mytie_item, null);
                holder = new ViewHolder();
                holder.shequbiaoti = (TextView) convertView
                        .findViewById(R.id.shequbiaoti);
                holder.tiezhibiaoti = (TextView) convertView
                        .findViewById(R.id.tiezhibiaoti);
                holder.tiezhineirong = (TextView) convertView
                        .findViewById(R.id.tiezhineirong);
                holder.delteiz = (TextView) convertView
                        .findViewById(R.id.delteiz);
                holder.mytieitem_ll = (LinearLayout) convertView
                        .findViewById(R.id.mytieitem_ll);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String title = bbsList.get(position).getTitle();
            String content = bbsList.get(position).getContent();
            String nickname = bbsList.get(position).getNickname();
            String yycontent = bbsList.get(position).getYycontent();
            String name = bbsList.get(position).getName();
            final String communityId = bbsList.get(position).getCommunityId();
            final String topicid = bbsList.get(position).getId();


            holder.shequbiaoti.setText("社区标题:" + name);
            holder.tiezhibiaoti.setText("原帖标题:" + title);
            if (type.equals("fabu")) {
                holder.tiezhineirong.setText("内容:" + content);
            } else if (type.equals("huitie")) {
                holder.tiezhineirong.setText("回复内容:" + content);
            } else if (type.equals("huiren")) {
                holder.tiezhineirong.setText("回复:" + nickname + "   内容:" + yycontent + "\n" + "回复内容:" +content);
            }

            holder.mytieitem_ll.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg1) {
                    ActivityJumpControl.getInstance((Activity) context)
                            .gotoBBSCardActivity(topicid);
                }
            });
            holder.delteiz.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg1) {
                    delMyTopic(topicid);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView shequbiaoti, tiezhibiaoti, tiezhineirong,delteiz;
            private LinearLayout mytieitem_ll;
        }
    }
}
