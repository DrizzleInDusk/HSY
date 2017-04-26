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
 * Adapter 专辑详情页
 * 
 * @author Administrator
 * 
 */
public class DetailsListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<AudioList> albumList = new ArrayList<AudioList>();

	public DetailsListAdapter(Context context, ArrayList<AudioList> albumList) {
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
			convertView = minflater.inflate(R.layout.details_item, null);
			holder = new ViewHolder();
			holder.AddTime = (TextView) convertView
					.findViewById(R.id.details_addtime_tv);
			holder.AudioName = (TextView) convertView
					.findViewById(R.id.details_audioName_tv);
			holder.CommentNumber = (TextView) convertView
					.findViewById(R.id.details_commentNumber_tv);
			holder.PlayAmount = (TextView) convertView
					.findViewById(R.id.details_playAmount_tv);
			holder.TimeLong = (TextView) convertView
					.findViewById(R.id.details_timeLong_tv);
			holder.Path = (ImageView) convertView
					.findViewById(R.id.details_download_iv);
			holder.Cover = (RoundedImageView) convertView
					.findViewById(R.id.details_coverivi_iv);

			holder.details_ll = (LinearLayout) convertView
					.findViewById(R.id.details_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (albumList.size() > 0) {

			String addTime = albumList.get(position).getAddTime();// 添加时间
			String audioName = albumList.get(position).getAudioName();// 音频名称
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

			String cover = albumList.get(position).getCover();// 封面图
			cover = URLManager.COVER_URL + cover;
			Picasso.with(context).load(cover).resize(400, 400)
					.placeholder(R.mipmap.details_img3)
					.error(R.mipmap.details_img3).into(holder.Cover);

			holder.details_ll.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg1) {
					ActivityJumpControl.getInstance((Activity) context)
							.gotoDetailsPlayActivity(
									albumList.get(position).getId());
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		private LinearLayout details_ll;
		// private RelativeLayout letter_all_rl1;
		private RoundedImageView Cover;
		private ImageView Path;
		private TextView AddTime, AudioName, Blurb, CommentNumber, PlayAmount,
				TimeLong;
	}

}
