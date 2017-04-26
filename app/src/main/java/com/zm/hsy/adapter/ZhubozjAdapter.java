package com.zm.hsy.adapter;

import java.math.BigDecimal;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter 发布的音频专辑
 * 
 * @author Administrator
 * 
 */
public class ZhubozjAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<Album> albumList = new ArrayList<Album>();;

	public ZhubozjAdapter(Context context, ArrayList<Album> albumList) {
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
					R.layout.activity_zhubo_item_zhuanji, null);
			holder = new ViewHolder();
			holder.zhubo_zhuanji_name = (TextView) convertView
					.findViewById(R.id.zhubo_zhuanji_name);
			holder.zhubo_zhuanji_shijian = (TextView) convertView
					.findViewById(R.id.zhubo_zhuanji_shijian);
			holder.zhubo_zhuanji_playAmount = (TextView) convertView
					.findViewById(R.id.zhubo_zhuanji_playAmount);
			holder.zhubo_zhuanji_episode = (TextView) convertView
					.findViewById(R.id.zhubo_zhuanji_episode);
			holder.zhubo_zhuanji_cover = (ImageView) convertView
					.findViewById(R.id.zhubo_zhuanji_cover);

			holder.rv1 = (RelativeLayout) convertView.findViewById(R.id.rv1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (albumList.size() > 0) {
			holder.rv1.setVisibility(View.VISIBLE);

			String addTime = albumList.get(position).getAddTime();
			String payamount = albumList.get(position).getPlayAmount();
			String episode = albumList.get(position).getEpisode();

			holder.zhubo_zhuanji_name.setText(albumList.get(position)
					.getAlbumName());

			if (payamount != null && payamount != "null" && payamount != "") {
				int p = Integer.parseInt(payamount);
				if (p >= 10000) {
					int pp = p / 10000;
					BigDecimal bd = new BigDecimal(pp + "");
					bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

					holder.zhubo_zhuanji_playAmount.setText(bd + "万人");
				} else if (p < 10000 && p > 0) {
					holder.zhubo_zhuanji_playAmount.setText(p + "人");
				} else {
					holder.zhubo_zhuanji_playAmount.setText("0人");
				}
			} else {
				holder.zhubo_zhuanji_playAmount.setText("0人");
			}

			if (episode != null && episode != "" && episode != "null") {
				holder.zhubo_zhuanji_episode.setText(episode + "集");
			} else {
				holder.zhubo_zhuanji_episode.setText("暂无");
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
						holder.zhubo_zhuanji_shijian.setText("最近更新：" + days
								+ "天前");
					} else if (days > 30) {
						long m = days / 30;
						if (m > 12) {
							long y = m / 12;
							holder.zhubo_zhuanji_shijian.setText("最近更新：" + y
									+ "年前");
						} else {
							holder.zhubo_zhuanji_shijian.setText("最近更新：" + m
									+ "个月前");
						}
					}
				} else {
					holder.zhubo_zhuanji_shijian.setText("最近更新：1天前");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			String head = albumList.get(position).getCover();// 封面图
			head = URLManager.COVER_URL + head;
			Picasso.with(context).load(head).resize(400, 400)
					.placeholder(R.mipmap.tu9854_1)
					.error(R.mipmap.tu9854_1)
					.into(holder.zhubo_zhuanji_cover);
			holder.rv1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg1) {
					ActivityJumpControl.getInstance((Activity) context)
							.gotoDetailsActivity(
									albumList.get(position).getId());
				}
			});
		} else {
			holder.rv1.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView zhubo_zhuanji_cover;
		private RelativeLayout rv1;
		private TextView zhubo_zhuanji_name, zhubo_zhuanji_playAmount,
				zhubo_zhuanji_shijian, zhubo_zhuanji_episode;
	}

}
