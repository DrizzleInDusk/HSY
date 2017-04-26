package com.zm.hsy.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;

public class MyPlayBar extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	/** 后台播放信息 */
	private String id = null;
	private String AlbumId = null;
	private App mapplication;
	private int playcode;
	public MyPlayBar(Context context) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
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
			convertView = minflater
					.inflate(R.layout.activity_playbar, null);
			holder = new ViewHolder();
			holder.playbar_touxiang = (RoundedImageView)convertView.findViewById(R.id.playbar_touxiang);
			holder.playbar_right = (ImageView) convertView.findViewById(R.id.playbar_right);
			holder.playbar_play = (ImageView) convertView.findViewById(R.id.playbar_play);
			holder.playbar_name = (TextView) convertView.findViewById(R.id.playbar_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AudioList contextaudio = mapplication.getContextAudioList();
		if (contextaudio != null) {
			id = contextaudio.getId();
			AlbumId = contextaudio.getAudioAlbumId();
			String name = contextaudio.getAudioName();
			holder.playbar_name.setText(name);
			String cover = contextaudio.getCover();
			cover = URLManager.COVER_URL + cover;
			Picasso.with(context).load(cover).resize(400, 400)
					.placeholder(R.mipmap.playbar_touxiang)
					.error(R.mipmap.playbar_touxiang).into(holder.playbar_touxiang);
		}
		int pcode = mapplication.getPlaycode();
//		if (MainActivity.player.isplay()) {
//			holder.playbar_play.setSelected(true);
//		} else {
//			holder.playbar_play.setSelected(false);
//		}
		holder.playbar_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.isSelected()) {
//					MainActivity.player.pause();
					playcode = 1;
					v.setSelected(false);
				} else {
//					MainActivity.player.play();
					playcode = 0;
					v.setSelected(true);
				}
				mapplication.setPlaycode(playcode);
			}
		});
		holder.playbar_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoDetailsPlayActivity(id);
			}
		});
		return convertView;
	}

	class ViewHolder {
		private RoundedImageView playbar_touxiang;
		private ImageView playbar_right, playbar_play;
		private TextView playbar_name;
	}

}