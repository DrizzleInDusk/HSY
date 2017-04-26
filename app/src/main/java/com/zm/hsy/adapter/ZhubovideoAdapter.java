package com.zm.hsy.adapter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zm.hsy.R;
import com.zm.hsy.entity.VideoList;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter 发布的声音
 * 
 * @author Administrator
 * 
 */
public class ZhubovideoAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<VideoList> albumList = new ArrayList<VideoList>();

	public ZhubovideoAdapter(Context context, ArrayList<VideoList> albumList) {
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
			convertView = minflater.inflate(
					R.layout.activity_zhubo_item_shengyin, null);
			holder = new ViewHolder();
			holder.zhubo_shengyin_shijian = (TextView) convertView
					.findViewById(R.id.zhubo_shengyin_shijian);
			holder.zhubo_shengyin_addTime = (TextView) convertView
					.findViewById(R.id.zhubo_shengyin_addTime);
			holder.zhubo_shengyin_name = (TextView) convertView
					.findViewById(R.id.zhubo_shengyin_name);
			holder.zhubo_shengyin_commentNumber = (TextView) convertView
					.findViewById(R.id.zhubo_shengyin_commentNumber);
			holder.zhubo_shengyin_playAmount = (TextView) convertView
					.findViewById(R.id.zhubo_shengyin_playAmount);
			holder.zhubo_shengyin_timeLong = (TextView) convertView
					.findViewById(R.id.zhubo_shengyin_timeLong);
			holder.Path = (ImageView) convertView
					.findViewById(R.id.zhubo_shengyin_down);
			holder.zhubo_shengyin_cover = (RoundedImageView) convertView
					.findViewById(R.id.zhubo_shengyin_cover);

			holder.zhubo_shengyin_rl = (RelativeLayout) convertView
					.findViewById(R.id.zhubo_shengyin_rl);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (albumList.size() > 0) {

			String addTime = albumList.get(position).getAddTime();// 添加时间
			String audioName = albumList.get(position).getVideoName();// 音频名称
			String commentNumber = albumList.get(position).getCommentNumber();// 评论数量
			String playAmount = albumList.get(position).getPlayAmount();// 播放数量
			String timeLong = albumList.get(position).getTimeLong();// 音频时长
			String path = albumList.get(position).getPath();// 音频路径

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date d1 = df.parse(addTime);
				Date curDate = new Date(System.currentTimeMillis());
				long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
				long hours = diff / (1000 * 60 * 60);
				if (hours > 24) {
					long days = hours / 24;
					if (30 >= days && days >= 1) {
						holder.zhubo_shengyin_shijian.setText("最近更新：" + days
								+ "天前");
						holder.zhubo_shengyin_addTime.setText(days + "天前");
					} else if (days > 30) {
						long m = days / 30;
						if (m > 12) {
							long y = m / 12;
							holder.zhubo_shengyin_addTime.setText("最近更新：" + y
									+ "年前");
							holder.zhubo_shengyin_addTime.setText(y + "年前");
						} else {
							holder.zhubo_shengyin_addTime.setText("最近更新：" + m
									+ "个月前");
							holder.zhubo_shengyin_addTime.setText(m + "个月前");
						}
					}
				} else {
					holder.zhubo_shengyin_shijian.setText("最近更新：1天前");
					if (hours < 1) {
						holder.zhubo_shengyin_addTime.setText("刚刚");
					} else {
						holder.zhubo_shengyin_addTime.setText(hours + "小时前");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (playAmount != null && playAmount != "null" && playAmount != "") {
				int p = Integer.parseInt(playAmount);
				if (p >= 10000) {
					int pp = p / 10000;
					BigDecimal bd = new BigDecimal(pp + "");
					bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
					holder.zhubo_shengyin_playAmount.setText(bd + "万人");
				} else if (p < 10000 && p > 0) {
					holder.zhubo_shengyin_playAmount.setText(p + "人");
				} else {
					holder.zhubo_shengyin_playAmount.setText("0人");
				}
			} else {
				holder.zhubo_shengyin_playAmount.setText("0人");
			}

			if (commentNumber != null && commentNumber != "null"
					&& commentNumber != "") {
				holder.zhubo_shengyin_commentNumber.setText(commentNumber);
			} else {
				holder.zhubo_shengyin_commentNumber.setText("0");
			}
			holder.zhubo_shengyin_name.setText(audioName);
			holder.zhubo_shengyin_timeLong.setText(timeLong);
			holder.Path.setTag(path);

			String head = albumList.get(position).getCover();// 封面图
			head = URLManager.COVER_URL + head;
			Picasso.with(context).load(head).resize(400, 400)
					.placeholder(R.mipmap.tu9854_2)
					.error(R.mipmap.tu9854_2)
					.into(holder.zhubo_shengyin_cover);
			holder.zhubo_shengyin_rl
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg1) {
							ActivityJumpControl.getInstance((Activity) context)
									.gotoVideoPlayActivity(
											albumList.get(position).getId());
						}
					});
		}
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout zhubo_shengyin_rl;
		private RoundedImageView zhubo_shengyin_cover;
		private ImageView Path;
		private TextView zhubo_shengyin_shijian, zhubo_shengyin_name,
				zhubo_shengyin_commentNumber, zhubo_shengyin_playAmount,
				zhubo_shengyin_timeLong, zhubo_shengyin_addTime;
	}

}
