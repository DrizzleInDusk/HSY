package com.zm.hsy.adapter;

import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BdVideoListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<Album> albumList = new ArrayList<Album>();;

	public BdVideoListAdapter(Context context, ArrayList<Album> albumList) {
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
			convertView = minflater.inflate(R.layout.bangdan_list_audiolist,
					null);
			holder = new ViewHolder();
			holder.list_num = (TextView) convertView
					.findViewById(R.id.bangdan_audiolist_num);
			holder.list_name = (TextView) convertView
					.findViewById(R.id.bangdan_audiolist_name);
			holder.list_blurb = (TextView) convertView
					.findViewById(R.id.bangdan_audiolist_blurb);
			holder.list_episode = (TextView) convertView
					.findViewById(R.id.bangdan_audiolist_episode);
			holder.list_tv3 = (ImageView) convertView
					.findViewById(R.id.bangdan_list_tv3);
			holder.list_img1 = (ImageView) convertView
					.findViewById(R.id.bangdan_audiolist_img1);

			holder.bangdan_list = (RelativeLayout) convertView
					.findViewById(R.id.bangdan_audiolist);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String blurb = albumList.get(position).getBlurb();
		int n=position+1;
		holder.list_num.setText("" + n);
		if(n>4){
			holder.list_num.setTextColor(Color.parseColor("#494949"));
		}else{
			holder.list_num.setTextColor(Color.parseColor("#ff8e1e"));
		}
		holder.list_name.setText(albumList.get(position).getAlbumName());

		if (blurb != null && !blurb.equals("") && !blurb.equals("null")) {
			holder.list_blurb.setText(albumList.get(position).getBlurb());
		} else {
			holder.list_blurb.setText("暂无简介");
		}

		
			holder.list_episode.setVisibility(View.GONE);
			holder.list_tv3.setVisibility(View.GONE);
	
		String cover = albumList.get(position).getCover();// 封面图
		cover = URLManager.COVER_URL + cover;
		Picasso.with(context).load(cover).resize(400, 400)
				.placeholder(R.color.touming)
				.error(R.mipmap.ic_launcher).into(holder.list_img1);

		holder.bangdan_list.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				 ActivityJumpControl.getInstance((Activity) context)
				 .gotoVideoPlayActivity(albumList.get(position).getId());
			}
		});
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout bangdan_list;
		private ImageView list_img1,list_tv3;
		private TextView list_num, list_name, list_blurb, list_episode;
	}

}
