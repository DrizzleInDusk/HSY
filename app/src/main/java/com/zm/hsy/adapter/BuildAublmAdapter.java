package com.zm.hsy.adapter;

import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.AlbumType;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter 二级分类
 * 
 * @author Administrator
 * 
 */
public class BuildAublmAdapter extends BaseAdapter {
	private ArrayList<AlbumType> albumTypeList;
	Context context;
	private int checkItemPosition = -1;

	public void setCheckItem(int position) {
		checkItemPosition = position;
		notifyDataSetChanged();
	}

	public BuildAublmAdapter(Context context, ArrayList<AlbumType> albumTypeList) {
		this.context = context;
		this.albumTypeList = albumTypeList;

	}

	@Override
	public int getCount() {
		return albumTypeList.size();
	}

	@Override
	public Object getItem(int position) {
		return albumTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.aublmtype_gridview, null);
			viewHolder = new ViewHolder();
			viewHolder.mText = (TextView) convertView.findViewById(R.id.gridtv);
			viewHolder.mText.setText(albumTypeList.get(position).getTypeName());
			convertView.setTag(viewHolder);
		}
		fillValue(position, viewHolder);
		return convertView;

	}

	private void fillValue(int position, ViewHolder viewHolder) {

		if (checkItemPosition != -1) {
			if (checkItemPosition == position) {
				viewHolder.mText.setTextColor(Color.parseColor("#000000"));
			} else {
				viewHolder.mText.setTextColor(Color.parseColor("#7B7D7C"));

			}
		}
	}

	static class ViewHolder {
		TextView mText;
	}
}
