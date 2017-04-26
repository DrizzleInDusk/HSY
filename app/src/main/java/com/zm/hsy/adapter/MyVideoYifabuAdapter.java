package com.zm.hsy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zm.hsy.R;
import com.zm.hsy.entity.VideoList;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Adapter 视频待发布
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ResourceAsColor")
public class MyVideoYifabuAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<VideoList> albumList = new ArrayList<VideoList>();;
	private MediaPlayer mPlayer = null;
	private boolean coverse = false;

	public MyVideoYifabuAdapter(Context context, ArrayList<VideoList> albumList) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.albumList = albumList;
		this.context = context;
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
			convertView = minflater.inflate(R.layout.activity_myaudio_item1,
					null);
			holder = new ViewHolder();
			holder.audioName_tv = (TextView) convertView
					.findViewById(R.id.yifabu_audioName_tv);
			holder.username_tv = (TextView) convertView
					.findViewById(R.id.yifabu_username_tv);
			holder.yifabu_addtime = (TextView) convertView
					.findViewById(R.id.yifabu_addtime_tv);
			holder.timeLong_tv = (TextView) convertView
					.findViewById(R.id.yifabu_timeLong_tv);
			holder.playAmount = (TextView) convertView
					.findViewById(R.id.yifabu_playAmount_tv);
			holder.yifabu_pinglun = (TextView) convertView
					.findViewById(R.id.yifabu_pinglun_tv);
			holder.yifabu_statustv = (TextView) convertView
					.findViewById(R.id.yifabu_statustv);

			holder.yifabu_statusiv = (ImageView) convertView
					.findViewById(R.id.yifabu_statusiv);
			holder.coverivi_iv = (RoundedImageView) convertView
					.findViewById(R.id.yifabu_coverivi_iv);

			holder.single_line = (View) convertView
					.findViewById(R.id.yifabu_single_line);
			holder.yifabu_ll = (LinearLayout) convertView
					.findViewById(R.id.yifabu_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.single_line.setVisibility(View.GONE);
		}
		final String addtime = albumList.get(position).getAddTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date d1 = df.parse(addtime);
			Date curDate = new Date(System.currentTimeMillis());
			long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			if (days >= 1) {
				holder.yifabu_addtime.setText(days + "天前");
			} else {
				long hours = diff / (1000 * 60 * 60);
				if (hours >= 1) {
					holder.yifabu_addtime.setText(hours + "小时前");
				} else {
					holder.yifabu_addtime.setText("刚刚");

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		final String path = albumList.get(position).getPath();

		String timeLong = albumList.get(position).getTimeLong();
		String audioName = albumList.get(position).getVideoName();
		String nickname = albumList.get(position).getNickname();
		String playAmount = albumList.get(position).getPlayAmount();
		String count = albumList.get(position).getCommentNumber();
		String status = albumList.get(position).getStatus();

		holder.audioName_tv.setText(audioName);
		holder.username_tv.setText("by" + nickname);
		holder.timeLong_tv.setText(timeLong);
		holder.playAmount.setText(playAmount);
		holder.yifabu_pinglun.setText(count);
		if (status.equals("3")) {
			holder.yifabu_statustv.setText("待审核");
			holder.yifabu_statustv.setTextColor(Color.RED);
			holder.yifabu_statustv.setVisibility(View.VISIBLE);
			holder.yifabu_statusiv.setVisibility(View.GONE);
		} else if (status.equals("2")) {
			holder.yifabu_statustv.setText("已下线");
			holder.yifabu_statustv.setTextColor(Color.parseColor("#D3DCE3"));
			holder.yifabu_statustv.setVisibility(View.VISIBLE);
			holder.yifabu_statusiv.setVisibility(View.GONE);
		} else if (status.equals("1")) {
			holder.yifabu_statustv.setVisibility(View.GONE);
			holder.yifabu_statusiv.setVisibility(View.VISIBLE);
		}

		String cover = albumList.get(position).getCover();// 封面图
		cover = URLManager.COVER_URL + cover;
		Picasso.with(context).load(cover).resize(400, 400)
				.placeholder(R.mipmap.details_img3)
				.error(R.mipmap.details_img3).into(holder.coverivi_iv);

		holder.coverivi_iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				Log.i("spid", albumList.get(position).getVideoAlbumId()+"+++++++++");
				ActivityJumpControl.getInstance((Activity) context)
						.gotoVideoPlayActivity(
								albumList.get(position).getId());
			}

		});
		holder.yifabu_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				Log.i("spid", albumList.get(position).getVideoAlbumId()+"+++++++++");
				ActivityJumpControl.getInstance((Activity) context)
						.gotoVideoPlayActivity(
								albumList.get(position).getId());
			}

		});
		return convertView;
	}

	class ViewHolder {
		private LinearLayout yifabu_ll;
		private RoundedImageView coverivi_iv;
		private ImageView yifabu_statusiv;
		private TextView audioName_tv, username_tv, timeLong_tv, playAmount,
				yifabu_pinglun, yifabu_statustv, yifabu_addtime;
		private View single_line;
	}

}
