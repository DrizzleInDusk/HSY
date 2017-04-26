package com.zm.hsy.fragment;

import com.squareup.picasso.Picasso;
import com.zm.hsy.R;
import com.zm.hsy.entity.Community;
import com.zm.hsy.entity.Variation;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment2 extends Fragment implements OnClickListener {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    JSONArray mccList = obj.getJSONArray("mccList");
                    bbsList = new ArrayList<>();
                    for (int i = 0; i < mccList.length(); i++) {
                        JSONObject mcc = mccList.getJSONObject(i);
                        Community community = new Community();
                        community.setBlurb(mcc.getString("blurb"));
                        community.setMemCount(mcc.getString("count"));
                        community.setCover(mcc.getString("cover"));
                        community.setId(mcc.getString("id"));
                        community.setName(mcc.getString("name"));
                        bbsList.add(community);
                    }
                    frag4_listview.setVisibility(View.VISIBLE);
                    fragmentadapter = new Fragmentadapter(context, bbsList);
                    frag4_listview.setAdapter(fragmentadapter);
                } catch (JSONException e) {
                    stopProgressDialog();
                }
            } else if (msg.what == URLManager.two) {
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    JSONArray mcaList = obj.getJSONArray("mcaList");
                    frag4_listview.setVisibility(View.GONE);
                    vList = new ArrayList<Variation>();
                    for (int i = 0; i < mcaList.length(); i++) {
                        JSONObject variation = mcaList.getJSONObject(i);
                        Variation variations = new Variation();

                        variations.setTitle(variation.getString("title"));
                        variations.setStatus(variation.getString("status"));
                        variations.setPropaganda(variation
                                .getString("propaganda"));
                        variations.setId(variation.getString("id"));
                        vList.add(variations);
                    }
                    frag4_listview.setVisibility(View.VISIBLE);
                    fragVarAdapter = new FragVarAdapter(context, vList);
                    frag4_listview.setAdapter(fragVarAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    stopProgressDialog();
                }
            }
            stopProgressDialog();
        }

    };
    private String userid, Tag;
    private Context context;
    private ArrayList<Community> bbsList;
    private Fragmentadapter fragmentadapter;
    private ArrayList<Variation> vList;
    private FragVarAdapter fragVarAdapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_frag4, container, false);
        context = getActivity();
        userid = Futil.getValue(context, "userid");

        findview();

        frag4_top_rl1.performClick();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag4_top_rl1:// 我的社区
                frag4_listview.setVisibility(View.GONE);
                frag4_top_tv1.setVisibility(View.VISIBLE);
                frag4_shequll.setVisibility(View.VISIBLE);
                frag4_top_tv2.setVisibility(View.INVISIBLE);
                getCommunity();
                break;
            case R.id.frag4_top_rl2:// 我的活动
                frag4_shequll.setVisibility(View.GONE);
                frag4_listview.setVisibility(View.GONE);
                frag4_top_tv1.setVisibility(View.INVISIBLE);
                frag4_top_tv2.setVisibility(View.VISIBLE);
                getVariation();
                break;
            case R.id.frag4_fabu:// 发布的帖子
                ActivityJumpControl.getInstance((Activity) context).gotoMytieActivity("发布的帖子", "fabu");
                break;
            case R.id.frag4_huitie:// 回复的帖子
                ActivityJumpControl.getInstance((Activity) context).gotoMytieActivity("回复的帖子", "huitie");
                break;
            case R.id.frag4_huiren:// 我＠的人
                ActivityJumpControl.getInstance((Activity) context).gotoMytieActivity("我＠的人", "huiren");
                break;
        }
    }

    private LinearLayout frag4_top_rl1, frag4_top_rl2, frag4_shequll;
    private TextView frag4_top_tv1, frag4_top_tv2;
    private ListView frag4_listview;

    private void findview() {
        frag4_listview = (ListView) view.findViewById(R.id.frag4_listview);
        // 我的社区
        frag4_top_rl1 = (LinearLayout) view.findViewById(R.id.frag4_top_rl1);
        frag4_top_rl1.setOnClickListener(this);
        frag4_top_tv1 = (TextView) view.findViewById(R.id.frag4_top_tv1);
        // 我的活动
        frag4_top_rl2 = (LinearLayout) view.findViewById(R.id.frag4_top_rl2);
        frag4_top_rl2.setOnClickListener(this);
        frag4_top_tv2 = (TextView) view.findViewById(R.id.frag4_top_tv2);

        frag4_shequll = (LinearLayout) view.findViewById(R.id.frag4_shequll);
        view.findViewById(R.id.frag4_fabu).setOnClickListener(this);
        view.findViewById(R.id.frag4_huitie).setOnClickListener(this);
        view.findViewById(R.id.frag4_huiren).setOnClickListener(this);


    }

    private void getCommunity() {
        startProgressDialog();
        HashMap<String, String> map = new HashMap<>();
        String strUrl = URLManager.getMyCommunity;
        map.put("user.id", userid);
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }

    private void getVariation() {
        startProgressDialog();
        HashMap<String, String> map = new HashMap<>();
        String strUrl = URLManager.getMyVariation;
        map.put("user.id", userid);
        Futil.xutils(strUrl, map, handler, URLManager.two);
    }


    /**
     * 等待页
     */
    private CustomProgressDialog progressDialog;

    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(context);
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

    public class Fragmentadapter extends BaseAdapter {
        private Context context;
        private LayoutInflater minflater;
        private ArrayList<Community> bbsList = new ArrayList<Community>();
        ;

        public Fragmentadapter(Context context, ArrayList<Community> bbsList) {
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
                convertView = minflater.inflate(R.layout.fragment2_mybbsitem,
                        null);
                holder = new ViewHolder();
                holder.bbs_name = (TextView) convertView
                        .findViewById(R.id.bbs_name);
                holder.bbs_blurb = (TextView) convertView
                        .findViewById(R.id.bbs_blurb);
                holder.bbs_memCount = (TextView) convertView
                        .findViewById(R.id.bbs_memCount);
                holder.bbs_cover = (RoundedImageView) convertView
                        .findViewById(R.id.bbs_cover);
                holder.bbs_community = (LinearLayout) convertView
                        .findViewById(R.id.bbs_community);

                holder.single_line = (View) convertView
                        .findViewById(R.id.bbs_single_line);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.single_line.setVisibility(View.GONE);
            } else {
                holder.single_line.setVisibility(View.VISIBLE);
                String name = bbsList.get(position).getName();
                String blurb = bbsList.get(position).getBlurb();
                String memCount = bbsList.get(position).getMemCount();
                holder.bbs_name.setText(name);
                holder.bbs_blurb.setText(blurb);
                if (memCount != null && !memCount.equals("")
                        && !memCount.equals("null")) {
                    holder.bbs_memCount.setText("关注：" + memCount);
                } else {
                    holder.bbs_memCount.setText("关注：0");
                }
                String cover = bbsList.get(position).getCover();// 封面图
                cover = URLManager.COVER_URL + cover;
                Picasso.with(context).load(cover).resize(400, 400).error(R.mipmap.bbs_img2).into(holder.bbs_cover);
                holder.bbs_community.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg1) {
                        ActivityJumpControl.getInstance((Activity) context)
                                .gotoBBSCommunityActivity(bbsList.get(position).getId());
                    }
                });
            }
            return convertView;
        }

        class ViewHolder {
            private LinearLayout bbs_community;
            private RoundedImageView bbs_cover;
            private TextView bbs_name, bbs_blurb, bbs_memCount;
            private View single_line;
        }
    }

    public class FragVarAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater minflater;
        private ArrayList<Variation> vList;
        ;

        public FragVarAdapter(Context context, ArrayList<Variation> vList) {
            super();
            this.minflater = LayoutInflater.from(context);
            this.vList = vList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return vList.size();
        }

        @Override
        public Object getItem(int position) {
            return vList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = minflater.inflate(R.layout.fragment2_vriation_item,
                        null);
                holder = new ViewHolder();
                holder.join = (TextView) convertView.findViewById(R.id.join);
                holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);// 活动名称
                holder.vriation_item = (ImageView) convertView
                        .findViewById(R.id.vriation_item);
                holder.rl1 = (RelativeLayout) convertView
                        .findViewById(R.id.rl1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Variation variations = vList.get(position);
            final String status = variations.getStatus();
            final String title = variations.getTitle();
            holder.tv_msg.setText(title);

            String cover = variations.getPropaganda();
            cover = URLManager.VariationCOVER_URL + cover;
            Picasso.with(context).load(cover).error(R.mipmap.vriation_item).into(holder.vriation_item);

            holder.vriation_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityJumpControl.getInstance((Activity) context)
                            .gotoVariationMesActivity(vList.get(position).getId());
                }
            });
            holder.rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityJumpControl.getInstance((Activity) context)
                            .gotoVarbmrsActivity(vList.get(position).getId());

                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView join, tv_msg;
            ImageView vriation_item;
            RelativeLayout rl1;
        }
    }
}
