package com.zm.hsy.adapter;

import java.util.List;

import com.zm.hsy.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter 二级分类
 * 
 * @author Administrator
 * 
 */
public class MyGridViewAdapter extends BaseAdapter {
	List<String> items;
	Context context;
	private int checkItemPosition = -1;

	public void setCheckItem(int position) {
		checkItemPosition = position;
		notifyDataSetChanged();
	}

	public MyGridViewAdapter(Context context, List<String> items) {
		this.context = context;
		this.items = items;

	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
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
					R.layout.gridview_down_list, null);
			viewHolder = new ViewHolder();
			viewHolder.mText = (TextView) convertView.findViewById(R.id.gridtv);
			viewHolder.mText.setText(items.get(position));
			viewHolder.mText.setTextColor(Color.parseColor("#000000"));
			convertView.setTag(viewHolder);
		}
		fillValue(position, viewHolder);
		return convertView;

	}

	private void fillValue(int position, ViewHolder viewHolder) {

		if (checkItemPosition != -1) {
			if (checkItemPosition == position) {
				viewHolder.mText.setTextColor(Color.parseColor("#1abc9c"));
			} else {
				viewHolder.mText.setTextColor(Color.parseColor("#000000"));

			}
		}
	}

	static class ViewHolder {
		TextView mText;
	}
}
