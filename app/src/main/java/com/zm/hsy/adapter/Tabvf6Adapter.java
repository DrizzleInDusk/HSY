package com.zm.hsy.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Adapter 主播
 *
 * @author Administrator
 */
public class Tabvf6Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater minflater;
    private Map<String, List<User>> userMap;
    private ImageView iv;
    public Tabvf6Adapter(final Context mcontext, Map<String, List<User>> userMap) {
        super();
        this.minflater = LayoutInflater.from(mcontext);
        this.userMap = userMap;
        this.context = mcontext;

    }

    @Override
    public int getCount() {
        return userMap.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        String title = null;
        List<User> userlist = null;
        for (String key : userMap.keySet()) {
            userlist = userMap.get(key);

            if (position == 0) {
                title = key;
                break;
            }
            position--;
        }
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = minflater
                    .inflate(R.layout.tab_pager_zhubo_item, null);
            holder = new ViewHolder();
            holder.zhubo_title_name = (TextView) convertView
                    .findViewById(R.id.zhubo_title_name);
            holder.zhubo_title_more = (TextView) convertView
                    .findViewById(R.id.zhubo_title_more);

            holder.tabvf6_gridview = (GridView) convertView
                    .findViewById(R.id.tabvf6_gridview);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.zhubo_title_name.setText(title);
        final String name = title;
        final String belong= userlist.get(0).getBelong();
        holder.tabvf6_gridview.setAdapter(new Tabvf6GridAdapter(context, userlist));
        holder.zhubo_title_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityJumpControl.getInstance((Activity)context).gotoMoreZhuboActivity(belong,name);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView zhubo_title_name, zhubo_title_more;
        private GridView tabvf6_gridview;

    }

}
