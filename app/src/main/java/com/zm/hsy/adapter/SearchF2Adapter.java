package com.zm.hsy.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.Album;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter 搜索专辑
 * 
 * @author Administrator
 * 
 */
public class SearchF2Adapter extends BaseAdapter {
	Context context;
	private LayoutInflater minflater;
	private ArrayList<Album> albumList = new ArrayList<Album>();

	public SearchF2Adapter(Context context, ArrayList<Album> albumList) {
		this.minflater = LayoutInflater.from(context);
		this.context = context;
		this.albumList = albumList;
	}

	@Override
	public int getCount() {
		return albumList.size();
	}

	@Override
	public Object getItem(int position) {
		return albumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_search_item2, null);
			holder = new ViewHolder();
			holder.aublm_name = (TextView) convertView
					.findViewById(R.id.search_aublm_name);
			holder.aublm_blurb = (TextView) convertView
					.findViewById(R.id.search_aublm_blurb);
			holder.aublm_playAmount = (TextView) convertView
					.findViewById(R.id.search_aublm_playAmount);
			holder.aublm_episode = (TextView) convertView
					.findViewById(R.id.search_aublm_episode);

			holder.album_cover = (RoundedImageView) convertView
					.findViewById(R.id.search_album_cover);
			holder.rl = (RelativeLayout) convertView
					.findViewById(R.id.rl);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Album audioAlbumList = albumList.get(position);
		final String aublm_id = audioAlbumList.getId();
		String albumName = audioAlbumList.getAlbumName();
		String blurb = audioAlbumList.getBlurb();
		String playAmount = audioAlbumList.getPlayAmount();
		String episode = audioAlbumList.getEpisode();
		
		holder.aublm_name.setText(albumName);
		if (blurb != null && !blurb.equals("") && !blurb.equals("null")) {
			holder.aublm_blurb.setText(albumList.get(position).getBlurb());
		} else {
			holder.aublm_blurb.setText("暂无简介");
		}

		if (playAmount != null && playAmount != "null" && playAmount != "") {
			int p = Integer.parseInt(playAmount);
			if (p >= 10000) {
				int pp = p / 10000;
				BigDecimal bd = new BigDecimal(pp + "");
				bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

				holder.aublm_playAmount.setText(bd + "万人");
			} else if (p < 10000 && p > 0) {
				holder.aublm_playAmount.setText(p + "人");
			} else {
				holder.aublm_playAmount.setText("0人");
			}
		} else {
			holder.aublm_playAmount.setText("0人");
		}
		if (episode != null && !episode.equals("") && !episode.equals("null")) {
			holder.aublm_episode.setText(episode + "集");
		} else {
			holder.aublm_episode.setText("暂无");
		}
		String cover = audioAlbumList.getCover();
		cover = URLManager.COVER_URL + cover;
		Picasso.with(context).load(cover).resize(400, 400)
				.placeholder(R.mipmap.img_fang).error(R.mipmap.img_fang)
				.into(holder.album_cover);
		holder.rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoDetailsActivity(aublm_id);
			}
		});
		
		return convertView;
	}

	static class ViewHolder {
		TextView aublm_name, aublm_blurb, aublm_playAmount, aublm_episode;
		RoundedImageView album_cover;
		RelativeLayout rl;
	}
}
