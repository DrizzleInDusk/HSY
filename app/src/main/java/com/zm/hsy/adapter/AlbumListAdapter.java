package com.zm.hsy.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.Album;
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

/**
 * Adapter 专辑列表 没有表头
 * 
 * @author Administrator
 * 
 */
public class AlbumListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<Album> albumList = new ArrayList<Album>();

	public AlbumListAdapter(Context context, ArrayList<Album> albumList) {
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

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.tabvf2_all_item, null);
			holder = new ViewHolder();
			holder.Name = (TextView) convertView
					.findViewById(R.id.letter_all_audioAlbumName);
			holder.Blurb = (TextView) convertView
					.findViewById(R.id.letter_all_audioAlbumBlurb);
			holder.PlayAmount = (TextView) convertView
					.findViewById(R.id.letter_all_audioAlbumPlayAmount);
			holder.Episode = (TextView) convertView
					.findViewById(R.id.letter_all_audioAlbumEpisode);
			holder.audioAlbumCover = (ImageView) convertView
					.findViewById(R.id.letter_all_cover);

			holder.letter_all_itemll = (LinearLayout) convertView
					.findViewById(R.id.letter_all_itemll);
			holder.letter_all_rl1 = (RelativeLayout) convertView
					.findViewById(R.id.letter_all_rl1);
			holder.letter_all_tv1 = (TextView) convertView
					.findViewById(R.id.letter_all_tv1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (albumList.size() > 0) {
			holder.letter_all_itemll.setVisibility(View.VISIBLE);
			holder.letter_all_rl1.setVisibility(View.GONE);
			holder.letter_all_tv1.setVisibility(View.GONE);

			String blurb = albumList.get(position).getBlurb();
			String payamount = albumList.get(position).getPlayAmount();
			String episode = albumList.get(position).getEpisode();

			holder.Name.setText(albumList.get(position).getAlbumName());
			if (blurb != null && !blurb.equals("") && !blurb.equals("null")) {
				holder.Blurb.setText(albumList.get(position).getBlurb());
			} else {
				holder.Blurb.setText("暂无简介");
			}

			if (payamount != null && !payamount.equals("")
					&& !payamount.equals("null")) {
				int p = Integer.parseInt(payamount);
				if (p >= 10000) {
					int pp = p / 10000;
					BigDecimal bd = new BigDecimal(pp + "");
					bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

					holder.PlayAmount.setText(bd + "万人");
				} else if (p < 10000 && p > 0) {
					holder.PlayAmount.setText(p + "人");
				} else {
					holder.PlayAmount.setText("0人");
				}
			} else {
				holder.PlayAmount.setText("0人");
			}

			if (episode != null && !episode.equals("")
					&& !episode.equals("null")) {
				holder.Episode.setText(episode + "集");
			} else {
				holder.Episode.setText("暂无");
			}
			String cover = albumList.get(position).getCover();// 封面图
			cover = URLManager.COVER_URL + cover;
			Picasso.with(context).load(cover).resize(400, 400)
					.placeholder(R.mipmap.letter_item_img1)
					.error(R.mipmap.letter_item_img1)
					.into(holder.audioAlbumCover);

		} else {
			holder.letter_all_itemll.setVisibility(View.GONE);
			holder.letter_all_rl1.setVisibility(View.VISIBLE);
			holder.letter_all_tv1.setVisibility(View.VISIBLE);
		}
		holder.letter_all_itemll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoDetailsActivity(albumList.get(position).getId());
			}
		});
		return convertView;
	}

	class ViewHolder {
		private ImageView audioAlbumCover;
		private LinearLayout letter_all_itemll;
		private RelativeLayout letter_all_rl1;
		private TextView Name, PlayAmount, Blurb, Episode, letter_all_tv1;
	}

}
