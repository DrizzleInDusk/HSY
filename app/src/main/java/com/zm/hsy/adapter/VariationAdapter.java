package com.zm.hsy.adapter;

import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.Variation;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter
 *
 * @author Administrator
 */
public class VariationAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater minflater;
    private ArrayList<Variation> vList = new ArrayList<Variation>();
    ;

    public VariationAdapter(Context context, ArrayList<Variation> vList) {
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
            convertView = minflater.inflate(R.layout.activity_vriation_item,
                    null);
            holder = new ViewHolder();
            holder.join = (TextView) convertView.findViewById(R.id.join);
            holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);// 活动名称
            holder.vriation_item = (ImageView) convertView
                    .findViewById(R.id.vriation_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Variation variations = vList.get(position);
        final String status = variations.getStatus();
        final String title = variations.getTitle();
        holder.tv_msg.setText(title);
        if (status.equals("1")) {
            holder.join.setText("审核中");
        } else if (status.equals("2")) {
            holder.join.setText("我要参加");
        } else if (status.equals("3")) {
            holder.join.setText("报名已满");
        } else if (status.equals("4")) {
            holder.join.setText("活动结束");
        }

        String cover = variations.getPropaganda();
        cover = URLManager.VariationCOVER_URL + cover;
        Picasso.with(context).load(cover).error(R.mipmap.vriation_item).into(holder.vriation_item);

        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status.equals("4")) {
                    String userid = Futil.getValue(context, "userid");
                    if (userid != null && !userid.equals("")) {
                        ActivityJumpControl.getInstance((Activity) context)
                                .gotoJoinVariationActivity(title, vList.get(position).getId());
                    } else {
                        Futil.showMessage(context, "请先登录");
                    }
                }
            }
        });
        holder.vriation_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status.equals("4")) {
                    ActivityJumpControl.getInstance((Activity) context)
                            .gotoGotoVariationActivity(vList.get(position).getId());
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView join, tv_msg;
        ImageView vriation_item;
    }

}
