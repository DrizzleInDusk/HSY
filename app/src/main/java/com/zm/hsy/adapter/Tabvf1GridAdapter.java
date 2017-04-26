package com.zm.hsy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zm.hsy.R;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;

import java.util.ArrayList;

/**
 * 积分
 * Created by Kkan on 2016/7/9.
 */
public class Tabvf1GridAdapter extends BaseAdapter {
    private ArrayList<Album> albumList;
    private Context context;
    private LayoutInflater minflater;

    public Tabvf1GridAdapter(Context context, ArrayList<Album> albumList) {
        this.minflater = LayoutInflater.from(context);
        this.context = context;
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        if (null != albumList) {
            return albumList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.tab_pager_tuijian_item1, null);
            holder = new ViewHolder();
            holder.mView = convertView.findViewById(R.id.tuijian_item1_rl1);
            holder.dispaly_grid_img = (ImageView) convertView.findViewById(R.id.tuijian_item1_cover1);
            holder.tuijian_item1_blurb1 = (TextView) convertView.findViewById(R.id.tuijian_item1_blurb1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mView.setVisibility(View.VISIBLE);

        final Album album = albumList.get(position);
        holder.tuijian_item1_blurb1.setText(album.getBlurb());

        String head = album.getCover();// 封面图
        head = URLManager.COVER_URL + head;
        Picasso.with(context).load(head).resize(400, 400)
                .placeholder(R.mipmap.demo2).error(R.mipmap.demo2)
                .into(holder.dispaly_grid_img);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoDetailsActivity(album.getId());
            }
        });
        return convertView;
    }

    class ViewHolder {
        private View mView;
        private ImageView dispaly_grid_img;
        private TextView tuijian_item1_blurb1;

    }
}