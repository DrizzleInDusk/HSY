package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 首页音频内容跳转页 */
public class Top50Activity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray TopList = obj.getJSONArray("albumDayTopList");
                    albumList = new ArrayList<>();
                    for (int i = 0; i < TopList.length(); i++) {
                        JSONObject Topalbum = TopList.getJSONObject(i);
                        Album album = new Album();
                        album.setAlbumName(Topalbum.getString("albumName"));
                        album.setCover(Topalbum.getString("cover"));
                        album.setEpisode(Topalbum.getString("episode"));
                        album.setNickname(Topalbum.getString("nickname"));
                        album.setId(Topalbum.getString("albumId"));
                        albumList.add(album);
                    }
                    adapter = new Top50Adapter(context, albumList);
                    top50_view.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }

    };

    private String userid, albumTypeId, TopName;
    private ArrayList<Album> albumList;
    private Context context;
    private Top50Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top50);
        context = this;
        Intent i = this.getIntent();
        albumTypeId = i.getStringExtra("albumTypeId");
        TopName = i.getStringExtra("TopName");
        userid = Futil.getValue(context, "userid");
        findview();
        gettop50();
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

    private TextView top50_top_tv;
    private ListView top50_view;

    private void findview() {
        top50_top_tv = (TextView) findViewById(R.id.top50_top_tv);
//        top50_top_tv.setText(TopName + "每日精选");
        top50_view = (ListView) findViewById(R.id.top50_view);

        findViewById(R.id.back_top).setOnClickListener(this);

    }

    private void gettop50() {
        startProgressDialog();
        String strUrl = URLManager.Top50;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("albumTypeId", albumTypeId);
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

    public class Top50Adapter extends BaseAdapter {
        private Context context;
        private LayoutInflater minflater;
        private ArrayList<Album> albumList;

        public Top50Adapter(Context context, ArrayList<Album> albumList) {
            super();
            this.minflater = LayoutInflater.from(context);
            this.albumList = albumList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return albumList.size();
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
                convertView = minflater.inflate(R.layout.activity_top50_item, null);
                holder = new ViewHolder();
                holder.top50_num = (TextView) convertView
                        .findViewById(R.id.top50_num);
                holder.top50_albumname = (TextView) convertView
                        .findViewById(R.id.top50_albumname);
                holder.top50_nickname = (TextView) convertView
                        .findViewById(R.id.top50_nickname);
                holder.top50_episode = (TextView) convertView
                        .findViewById(R.id.top50_episode);
                holder.top50_cover = (ImageView) convertView
                        .findViewById(R.id.top50_cover);
                holder.top50_ll = (LinearLayout) convertView
                        .findViewById(R.id.top50_ll);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String albumname = albumList.get(position).getAlbumName();
            String nickname = albumList.get(position).getNickname();
            String episode = albumList.get(position).getEpisode();//
            final String albumid = albumList.get(position).getId();//
            String cover = albumList.get(position).getCover();//

            if (position == 0) {
                holder.top50_num.setText(""+(position + 1));
                holder.top50_num.setTextColor(Color.parseColor("#ff8e1e"));
            } else if (position == 1) {
                holder.top50_num.setText(""+(position + 1));
                holder.top50_num.setTextColor(Color.parseColor("#ffbc1e"));
            } else if (position == 2) {
                holder.top50_num.setText(""+(position + 1));
                holder.top50_num.setTextColor(Color.parseColor("#1effac"));
            }else{
                holder.top50_num.setText(""+(position + 1));
                holder.top50_num.setTextColor(Color.parseColor("#757A7D"));
            }
            holder.top50_albumname.setText(albumname);
            holder.top50_nickname.setText("by  " + nickname);
            holder.top50_episode.setText(episode);
            cover= URLManager.COVER_URL+cover;
            Picasso.with(context).load(cover).error(R.mipmap.letter_item_img1).into(holder.top50_cover);
            holder.top50_ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityJumpControl.getInstance((Activity) context).gotoDetailsActivity(albumid);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private LinearLayout top50_ll;
            private ImageView top50_cover;
            private TextView top50_num, top50_albumname, top50_nickname, top50_episode;
        }

    }
}
