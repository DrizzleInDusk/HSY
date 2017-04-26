package com.zm.hsy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Adapter 专辑列表 没有表头
 *
 * @author Administrator
 */
public class MyZbListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater minflater;
    private ArrayList<User> ulist;

    public MyZbListAdapter(Context context, ArrayList<User> ulist) {
        super();
        this.minflater = LayoutInflater.from(context);
        this.ulist = ulist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return ulist.size();
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
            convertView = minflater.inflate(R.layout.activity_morezhubo_item, null);
            holder = new ViewHolder();
            holder.morezhubo_name = (TextView) convertView
                    .findViewById(R.id.morezhubo_name);
            holder.morezhubo_blurb = (TextView) convertView
                    .findViewById(R.id.morezhubo_blurb);
            holder.morezhubo_shengyin = (TextView) convertView
                    .findViewById(R.id.morezhubo_shengyin);
            holder.morezhubo_fans = (TextView) convertView
                    .findViewById(R.id.morezhubo_fans);
            holder.morezhubo_head = (ImageView) convertView
                    .findViewById(R.id.morezhubo_head);
            holder.hasfocus = (ImageView) convertView
                    .findViewById(R.id.hasfocus);
            holder.morezhubo_iv = (ImageView) convertView
                    .findViewById(R.id.morezhubo_iv);
            holder.morezhubo_member = (ImageView) convertView
                    .findViewById(R.id.morezhubo_member);

            holder.morezhubo_ll = (LinearLayout) convertView
                    .findViewById(R.id.morezhubo_ll);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String blurb = ulist.get(position).getBlurb();
        String audioCount = ulist.get(position).getAudioCount();
        String concemFans = ulist.get(position).getConcemFans();
        String members = ulist.get(position).getMembers();
        String ifgz = ulist.get(position).getIfgz();
        if(members.equals("2")){
            holder.morezhubo_member.setVisibility(View.VISIBLE);
        }else{
            holder.morezhubo_member.setVisibility(View.INVISIBLE);
        }
        if(ifgz.equals("0")){
            holder.hasfocus.setSelected(false);
        }else{
            holder.hasfocus.setSelected(true);
        }
        holder.morezhubo_name.setText(ulist.get(position).getNickname());
        if (blurb != null && !blurb.equals("") && !blurb.equals("null")) {
            holder.morezhubo_blurb.setText(ulist.get(position).getBlurb());
        } else {
            holder.morezhubo_blurb.setText("暂无简介");
        }

        if (audioCount != null && !audioCount.equals("")
                && !audioCount.equals("null")) {
            holder.morezhubo_shengyin.setText(audioCount);
        } else {
            holder.morezhubo_shengyin.setText("0人");
        }

        if (concemFans != null && !concemFans.equals("")
                && !concemFans.equals("null")) {
            holder.morezhubo_fans.setText(concemFans);
        } else {
            holder.morezhubo_fans.setText("0");
        }
        String head = ulist.get(position).getHead();// 封面图
        String type = ulist.get(position).getHeadStatus();// 封面图
        if (!type.equals("http")) {
            head = URLManager.Head_URL + head;
        }
        Picasso.with(context).load(head).resize(400, 400)
                .placeholder(R.mipmap.letter_item_img1)
                .error(R.mipmap.letter_item_img1)
                .into(holder.morezhubo_head);


        holder.morezhubo_ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg1) {
                System.out.println("点击了主播>>>id>>"+ulist.get(position).getId());
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoZhuboActivity(ulist.get(position).getId());
            }
        });
        holder.morezhubo_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg1) {
                System.out.println("点击了主播>>>id>>"+ulist.get(position).getId());
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoZhuboActivity(ulist.get(position).getId());
            }
        });
        holder.morezhubo_head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg1) {
                System.out.println("点击了主播>>>id>>"+ulist.get(position).getId());
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoZhuboActivity(ulist.get(position).getId());
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView morezhubo_head, morezhubo_member, hasfocus, morezhubo_iv;
        private LinearLayout morezhubo_ll;
        private TextView morezhubo_name, morezhubo_shengyin, morezhubo_blurb, morezhubo_fans;
    }

}
