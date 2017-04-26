package com.zm.hsy.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zm.hsy.R;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Adapter 搜索音频
 * 
 * @author Administrator
 * 
 */
public class SearchF4Adapter extends BaseAdapter {
	Context context;
	private LayoutInflater minflater;
	private ArrayList<AudioList> audioList = new ArrayList<AudioList>();

	public SearchF4Adapter(Context context, ArrayList<AudioList> audioList) {
		this.minflater = LayoutInflater.from(context);
		this.context = context;
		this.audioList = audioList;
	}

	@Override
	public int getCount() {
		return audioList.size();
	}

	@Override
	public Object getItem(int position) {
		return audioList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_search_item4, null);
			holder = new ViewHolder();
			holder.AddTime = (TextView) convertView
					.findViewById(R.id.search_audio_addtime);
			holder.AudioName = (TextView) convertView
					.findViewById(R.id.search_audio_audioName);
			holder.Blurb = (TextView) convertView
					.findViewById(R.id.search_audio_blurb);
			holder.CommentNumber = (TextView) convertView
					.findViewById(R.id.search_audio_commentNumber);
			holder.PlayAmount = (TextView) convertView
					.findViewById(R.id.search_audio_playAmount);
			holder.TimeLong = (TextView) convertView
					.findViewById(R.id.search_audio_timeLong);
			holder.Path = (ImageView) convertView
					.findViewById(R.id.search_audio_download);
			holder.Cover = (RoundedImageView) convertView
					.findViewById(R.id.search_audio_cover);

			holder.details_ll = (LinearLayout) convertView
					.findViewById(R.id.search_audio_ll);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final String id = audioList.get(position).getId();// 添加时间
		final String audioAlbumId = audioList.get(position).getAudioAlbumId();// 添加时间
		String addTime = audioList.get(position).getAddTime();// 添加时间
		String audioName = audioList.get(position).getAudioName();// 音频名称
		String commentNumber = audioList.get(position).getCommentNumber();// 评论数量
		String playAmount = audioList.get(position).getPlayAmount();// 播放数量
		String timeLong = audioList.get(position).getTimeLong();// 音频时长
		String path = audioList.get(position).getPath();// 音频路径
		String blurb = audioList.get(position).getBlurb();

		if (blurb != null && !blurb.equals("") && !blurb.equals("null")) {
			holder.Blurb.setText(blurb);
		} else {
			holder.Blurb.setText("暂无简介");
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date d1 = df.parse(addTime);
			Date curDate = new Date(System.currentTimeMillis());
			long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
			long hours = diff / (1000 * 60 * 60);
			if (hours > 24) {
				long days = hours / 24;
				if (30 >= days && days >= 1) {
					holder.AddTime.setText(days + "天前");
				} else if (days > 30) {
					long m = days / 30;
					if (m > 12) {
						long y = m / 12;
						holder.AddTime.setText(y + "年前");
					} else {
						holder.AddTime.setText(m + "个月前");
					}
				}
			} else {
				if (hours < 1) {
					holder.AddTime.setText("刚刚");
				} else {
					holder.AddTime.setText(hours + "小时前");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.AudioName.setText(audioName);
		holder.CommentNumber.setText(commentNumber);
		holder.PlayAmount.setText(playAmount);
		holder.TimeLong.setText(timeLong);
		holder.Path.setTag(path);

		String cover = audioList.get(position).getCover();// 封面图
		cover = URLManager.COVER_URL + cover;
		Picasso.with(context).load(cover).resize(400, 400)
				.placeholder(R.mipmap.details_img3)
				.error(R.mipmap.details_img3).into(holder.Cover);

		holder.details_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoDetailsPlayActivity(id);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		private LinearLayout details_ll;
		// private RelativeLayout letter_all_rl1;
		private RoundedImageView Cover;
		private ImageView Path;
		private TextView AddTime, AudioName, Blurb, CommentNumber, PlayAmount,
				TimeLong;
	}
}
