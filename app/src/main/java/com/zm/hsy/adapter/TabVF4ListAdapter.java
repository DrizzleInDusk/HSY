package com.zm.hsy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zm.hsy.R;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TabVF4ListAdapter extends BaseAdapter {
	private Context context;
	private String tag;
	private LayoutInflater minflater;
	private ArrayList<AudioList> audioList = new ArrayList<AudioList>();

	public TabVF4ListAdapter(Context context, ArrayList<AudioList> audioList,String tag) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.audioList = audioList;
		this.context = context;
		this.tag = tag;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return audioList.size();
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
					R.layout.tab_pager_guangbo_radiorank, null);
			holder = new ViewHolder();
			holder.AudioName = (TextView) convertView
					.findViewById(R.id.guangbo_radioname);
			holder.PlayAmount = (TextView) convertView
					.findViewById(R.id.guangbo_radiohits);
			
			holder.guangbo_addtime = (TextView) convertView
					.findViewById(R.id.guangbo_addtime);

			holder.guangbo_radioll = (LinearLayout) convertView
					.findViewById(R.id.guangbo_radioll);
			
			holder.guangbo_radiohitsll = (LinearLayout) convertView
					.findViewById(R.id.guangbo_radiohitsll);
			//封面图
			holder.cover= (ImageView) convertView.findViewById(R.id.guangbo_item1_img1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (audioList.size() > 0) {
			
			final String audioName = audioList.get(position).getAudioName();// 音频名称
			String playAmount = audioList.get(position).getPlayAmount();// 播放数量
			String addTime = audioList.get(position).getAddTime();// 添加时间
			final String path = audioList.get(position).getPath();// 音频路径
			final String id = audioList.get(position).getId();// 音频路径
			String cover=audioList.get(position).getCover();//封面图

			Picasso.with(context).load(URLManager.COVER_URL+cover).into(holder.cover);
			holder.AudioName.setText(audioName);
			if(tag.equals("his")){
				holder.guangbo_radiohitsll.setVisibility(View.GONE);
				holder.guangbo_addtime.setVisibility(View.VISIBLE);
				holder.guangbo_addtime.setText("上次收听时间："+addTime);
			}else{
				holder.guangbo_radiohitsll.setVisibility(View.VISIBLE);
				holder.guangbo_addtime.setVisibility(View.GONE);
				if (playAmount != null && !playAmount.equals("")
						&& !playAmount.equals("null")) {
					int p = Integer.parseInt(playAmount);
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
			}
			holder.guangbo_radioll
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							ActivityJumpControl.getInstance((Activity)context)
							.gotoRadioStationPlayerActivity(path,audioName,id);
						}
					});
		}
		return convertView;
	}

	class ViewHolder {
		private LinearLayout guangbo_radioll,guangbo_radiohitsll;
		private TextView AudioName, PlayAmount,guangbo_addtime;
		private ImageView cover;//封面图
	}

}