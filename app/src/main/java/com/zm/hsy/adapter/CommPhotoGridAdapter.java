package com.zm.hsy.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Adapter
 *
 * @author Administrator
 */
public class CommPhotoGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater minflater;
    private ArrayList<String> plist;

    public CommPhotoGridAdapter(Context context, ArrayList<String> plist) {
        super();
        this.minflater = LayoutInflater.from(context);
        this.plist = plist;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (null != plist) {
            return plist.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int arg0) {
        return plist.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = minflater
                    .inflate(R.layout.activity_communityphoto_item, null);
            holder = new ViewHolder();
            holder.commphoto_iv = (ImageView) convertView
                    .findViewById(R.id.commphoto_iv);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String head = plist.get(position);
        head = URLManager.COVER_URL + head;
        Picasso.with(context).load(head).resize(400, 400).error(R.mipmap.photo_img).into(holder.commphoto_iv);
        System.out.println("photos----------"+head);

        return convertView;
    }

    class ViewHolder {
        private ImageView commphoto_iv;

    }

}
