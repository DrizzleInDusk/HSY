package com.zm.hsy.adapter;

import java.util.List;
import java.util.Map;

import com.zm.hsy.R;
import com.zm.hsy.entity.VideoList;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Tabvf3Adapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private Map<String, List<VideoList>> videoMap;

	public Tabvf3Adapter(Context context, Map<String, List<VideoList>> videoMap) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.videoMap = videoMap;
		this.context = context;
	}

	@Override
	public int getCount() {
		return videoMap.size();
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
		List<VideoList> videoList = null;
		for (String key : videoMap.keySet()) {
			videoList = videoMap.get(key);
			if (position == 0) {
				title = key;
				break;
			}
			position--;
		}
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater
					.inflate(R.layout.activity_shipin_item, null);
			holder = new ViewHolder();
			holder.title1_name = (TextView) convertView
					.findViewById(R.id.shipin_title1_name);
			holder.item_ll1 = (LinearLayout) convertView
					.findViewById(R.id.item_video_ll1);
			holder.item_ll2 = (LinearLayout) convertView
					.findViewById(R.id.item_video_ll2);
			holder.shipin_title1 = (RelativeLayout) convertView
					.findViewById(R.id.shipin_title1);

			holder.item_cover1 = (ImageView) convertView
					.findViewById(R.id.item_video_cover1);
			holder.item_name1 = (TextView) convertView
					.findViewById(R.id.item_video_name1);
			holder.item_rl1 = (RelativeLayout) convertView
					.findViewById(R.id.item_video_rl1);

			holder.item_cover2 = (ImageView) convertView
					.findViewById(R.id.item_video_cover2);
			holder.item_name2 = (TextView) convertView
					.findViewById(R.id.item_video_name2);
			holder.item_rl2 = (RelativeLayout) convertView
					.findViewById(R.id.item_video_rl2);

			holder.item_cover3 = (ImageView) convertView
					.findViewById(R.id.item_video_cover3);
			holder.item_name3 = (TextView) convertView
					.findViewById(R.id.item_video_name3);
			holder.item_rl3 = (RelativeLayout) convertView
					.findViewById(R.id.item_video_rl3);

			holder.item_cover4 = (ImageView) convertView
					.findViewById(R.id.item_video_cover4);
			holder.item_name4 = (TextView) convertView
					.findViewById(R.id.item_video_name4);
			holder.item_rl4 = (RelativeLayout) convertView
					.findViewById(R.id.item_video_rl4);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (videoList.size() > 0) {
			holder.item_ll1.setVisibility(View.VISIBLE);
			holder.item_ll2.setVisibility(View.VISIBLE);
			holder.shipin_title1.setVisibility(View.VISIBLE);
			holder.title1_name.setText(title);
		} else {
			holder.item_ll1.setVisibility(View.GONE);
			holder.item_ll2.setVisibility(View.GONE);
			holder.shipin_title1.setVisibility(View.GONE);
		}
		VideoList album = null;
		try {
			album = videoList.get(0);
			final VideoList album0 = videoList.get(0);
			holder.item_name1.setText(album.getVideoName());

			String head = album.getCover();// 封面图
			head = URLManager.COVER_URL + head;
			Picasso.with(context).load(head).resize(400, 400)
					.placeholder(R.mipmap.shipin_title1_img1).error(R.mipmap.shipin_title1_img1)
					.into(holder.item_cover1);

			holder.item_cover1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 ActivityJumpControl.getInstance((Activity) context)
					 .gotoVideoPlayActivity(album0.getId());
				}
			});
			holder.item_rl1.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			holder.item_rl1.setVisibility(View.INVISIBLE);
		}
		try {
			album = videoList.get(1);
			final VideoList album1 = videoList.get(1);
			holder.item_name2.setText(album.getVideoName());

			String head = album.getCover();// 封面图
			head = URLManager.COVER_URL + head;
			Picasso.with(context).load(head).resize(400, 400)
					.placeholder(R.mipmap.shipin_title1_img1).error(R.mipmap.shipin_title1_img1)
					.into(holder.item_cover2);

			holder.item_cover2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 ActivityJumpControl.getInstance((Activity) context)
					 .gotoVideoPlayActivity(album1.getId());
				}
			});
			holder.item_rl2.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			holder.item_rl2.setVisibility(View.INVISIBLE);
		}

		if (videoList.size() >= 3) {
			try {
				album = videoList.get(2);
				final VideoList album2 = videoList.get(2);
				holder.item_name3.setText(album.getVideoName());

				String head = album.getCover();// 封面图
				head = URLManager.COVER_URL + head;
				Picasso.with(context).load(head).resize(400, 400)
						.placeholder(R.mipmap.shipin_title1_img1).error(R.mipmap.shipin_title1_img1)
						.into(holder.item_cover3);

				holder.item_cover3
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								 ActivityJumpControl.getInstance((Activity) context)
								 .gotoVideoPlayActivity(album2.getId());
							}
						});
				holder.item_rl3.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				holder.item_rl3.setVisibility(View.INVISIBLE);
			}
			holder.item_ll2.setVisibility(View.VISIBLE);
			try {
				album = videoList.get(3);
				final VideoList album3 = videoList.get(3);
				holder.item_name4.setText(album.getVideoName());

				String head = album.getCover();// 封面图
				head = URLManager.COVER_URL + head;
				Picasso.with(context).load(head).resize(400, 400)
						.placeholder(R.mipmap.shipin_title1_img1).error(R.mipmap.shipin_title1_img1)
						.into(holder.item_cover4);

				holder.item_cover4
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								 ActivityJumpControl.getInstance((Activity) context)
								 .gotoVideoPlayActivity(album3.getId());
							}
						});
				holder.item_rl4.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				holder.item_rl4.setVisibility(View.INVISIBLE);
			}

		} else {
			holder.item_ll2.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		private LinearLayout item_ll1, item_ll2;
		private RelativeLayout item_rl1, item_rl2, item_rl3, item_rl4,shipin_title1;
		private TextView title1_name;
		private ImageView item_cover1, item_cover2, item_cover3, item_cover4;
		private TextView item_name1, item_name2, item_name3, item_name4;

	}

}
