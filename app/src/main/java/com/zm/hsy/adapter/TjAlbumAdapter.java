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
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter 专辑列表 没有表头
 * 
 * @author Administrator
 * 
 */
public class TjAlbumAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<Album> albumList= new ArrayList<Album>() ;

	public TjAlbumAdapter(Context context, ArrayList<Album> albumList) {
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
			convertView = minflater.inflate(R.layout.details_playpage_tuijian,
					null);
			holder = new ViewHolder();
			holder.Blurb = (TextView) convertView
					.findViewById(R.id.playpage_tuijian_blurb);
			holder.Name = (TextView) convertView
					.findViewById(R.id.playpage_tuijian_name);
			holder.PlayAmount = (TextView) convertView
					.findViewById(R.id.playpage_tuijian_playAmount);
			holder.Episode = (TextView) convertView
					.findViewById(R.id.playpager_tuijian_episode);

			holder.audioAlbumCover = (ImageView) convertView
					.findViewById(R.id.playpager_tuijian_cover);
			
			holder.tj_item_rl = (RelativeLayout) convertView
					.findViewById(R.id.tj_item_rl);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Album mAudioAlbumList = albumList.get(position);
		holder.Name.setText(mAudioAlbumList.getAlbumName());
		String blurb = mAudioAlbumList.getBlurb();
		String payamount = mAudioAlbumList.getPlayAmount();
		String episode = mAudioAlbumList.getEpisode();
		if (blurb != null && blurb != "" && blurb != "null") {
			holder.Blurb.setText(albumList.get(position).getBlurb());
		} else {
			holder.Blurb.setText("暂无简介");
		}
		if (payamount != null && payamount != "null" && payamount != "") {
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
		if (episode != null && episode != "" && episode != "null") {
			holder.Episode.setText(episode + "集");
		} else {
			holder.Episode.setText("暂无");
		}
		String head = albumList.get(position).getCover();// 封面图
		head = URLManager.COVER_URL + head;
		Picasso.with(context).load(head).resize(400, 400)
				.placeholder(R.mipmap.letter_item_img1)
				.error(R.mipmap.letter_item_img1).into(holder.audioAlbumCover);
		holder.tj_item_rl.setOnClickListener(new View.OnClickListener() {

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
		private RelativeLayout tj_item_rl;
		private TextView Name, PlayAmount, Blurb, Episode;
	}

}
