package com.zm.hsy.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zm.hsy.R;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Adapter 我的订阅
 * 
 * @author Administrator
 * 
 */
public class MyPHSSubAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<Album> albumList = new ArrayList<Album>();;
	private Handler handler;

	public MyPHSSubAdapter(Context context, ArrayList<Album> albumList, Handler handler) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.albumList = albumList;
		this.context = context;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return albumList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(
					R.layout.activity_phs_subscribe_item, null);
			holder = new ViewHolder();
			holder.subscribe_audioAlbumName = (TextView) convertView
					.findViewById(R.id.subscribe_audioAlbumName);
			holder.subscribe_blurb = (TextView) convertView
					.findViewById(R.id.subscribe_blurb);
			holder.subscribe_addtime = (TextView) convertView
					.findViewById(R.id.subscribe_addtime);

			holder.subscribe_cover = (ImageView) convertView
					.findViewById(R.id.subscribe_cover);
			holder.subscribe_clear = (ImageView) convertView
					.findViewById(R.id.subscribe_clear);

			holder.subscribe_ll = (LinearLayout) convertView
					.findViewById(R.id.subscribe_ll);
			holder.sing_line = (View) convertView.findViewById(R.id.sing_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.sing_line.setVisibility(View.GONE);
		}
		String blurb = albumList.get(position).getBlurb();
		String addTime = albumList.get(position).getAddTime();

		holder.subscribe_audioAlbumName.setText(albumList.get(position)
				.getAlbumName());
		if (blurb != null && blurb != "" && blurb != "null") {
			holder.subscribe_blurb.setText(albumList.get(position).getBlurb());
		} else {
			holder.subscribe_blurb.setText("暂无简介");
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date d1 = df.parse(addTime);
			Date curDate = new Date(System.currentTimeMillis());
			long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			if (days >= 1) {
				holder.subscribe_addtime.setText(days + "天前");
			} else {
				long hours = diff / (1000 * 60 * 60);
				if (hours < 1) {
					holder.subscribe_addtime.setText("刚刚");
				} else {
					holder.subscribe_addtime.setText(hours + "小时前");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		String cover = albumList.get(position).getCover();// 封面图
		cover = URLManager.COVER_URL + cover;
		Picasso.with(context).load(cover).resize(400, 400)
				.placeholder(R.mipmap.letter_item_img1)
				.error(R.mipmap.letter_item_img1)
				.into(holder.subscribe_cover);

		holder.subscribe_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoDetailsActivity(albumList.get(position).getId());
			}
		});
		holder.subscribe_clear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg1) {
				Message msg1 = new Message();
				msg1.obj = position;
				msg1.what = URLManager.seven;
				handler.sendMessage(msg1);
				
			}
		});
		return convertView;
	}

	class ViewHolder {
		private View sing_line;
		private ImageView subscribe_cover,subscribe_clear;
		private LinearLayout subscribe_ll;
		private TextView subscribe_audioAlbumName, subscribe_addtime,
				subscribe_blurb;
	}

}
