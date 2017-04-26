package com.zm.hsy.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import android.widget.TextView;

/**
 * Adapter 专辑列表 有表头
 * 
 * @author Administrator
 * 
 */
public class LF2Adapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private Map<String, List<Album>> albumMap;
	private int size;

	public LF2Adapter(Context context,
					  Map<String, List<Album>> albumMap, int size) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.albumMap = albumMap;
		this.context = context;
		this.size = size;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return size;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Album album = null;
		for (String key : albumMap.keySet()) {
			List<Album> list = albumMap.get(key);
			int num = list.size();
			if ((position + 1) <= num) {
				album = list.get(position);
				break;
			} else {
				position -= num;
			}
		}
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater
					.inflate(R.layout.tabvf2_tuijian_item, null);
			holder = new ViewHolder();
			holder.commendName = (TextView) convertView
					.findViewById(R.id.letter_tuijian_commendName);
			holder.Name = (TextView) convertView
					.findViewById(R.id.letter_tuijian_audioAlbumName);
			holder.Blurb = (TextView) convertView
					.findViewById(R.id.letter_tuijian_audioAlbumBlurb);
			holder.PlayAmount = (TextView) convertView
					.findViewById(R.id.letter_tuijian_audioAlbumPlayAmount);
			holder.Episode = (TextView) convertView
					.findViewById(R.id.letter_tuijian_audioAlbumEpisode);
			holder.audioAlbumCover = (ImageView) convertView
					.findViewById(R.id.letter_tuijian_cover);

			holder.titleView = convertView
					.findViewById(R.id.letter_tuijian_title);
			holder.single_title = convertView.findViewById(R.id.single_title);

			holder.contentView = convertView
					.findViewById(R.id.letter_tuijian_content);

			holder.letter_tuijian_ll = (LinearLayout) convertView
					.findViewById(R.id.letter_tuijian_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String title = album.getCommendName();
		if (title != null) {

			holder.single_title.setVisibility(View.VISIBLE);
			holder.titleView.setVisibility(View.VISIBLE);
			holder.contentView.setVisibility(View.GONE);
			holder.commendName.setText(album.getCommendName());
		} else {
			holder.single_title.setVisibility(View.GONE);
			holder.titleView.setVisibility(View.GONE);
			holder.contentView.setVisibility(View.VISIBLE);

			String blurb = album.getBlurb();
			String payamount = album.getPlayAmount();
			String episode = album.getEpisode();

			holder.Name.setText(album.getAlbumName());
			if (blurb != null && blurb != "null" && blurb != "") {
				holder.Blurb.setText(album.getBlurb());

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
			String head =  album.getCover();// 封面图
			head = URLManager.COVER_URL + head;
			Picasso.with(context).load(head).resize(400, 400)
					.placeholder(R.mipmap.letter_item_img1)
					.error(R.mipmap.letter_item_img1).into(holder.audioAlbumCover);

		}
		final Album a = album;
		holder.letter_tuijian_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoDetailsActivity(a.getId());
			}
		});
		return convertView;
	}

	class ViewHolder {
		private ImageView audioAlbumCover;
		private LinearLayout letter_tuijian_ll;
		private TextView commendName, Name, PlayAmount, Blurb, Episode;
		private View titleView, contentView, single_title;
	}

}
