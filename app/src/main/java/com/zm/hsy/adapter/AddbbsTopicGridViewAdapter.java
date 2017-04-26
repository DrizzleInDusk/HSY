package com.zm.hsy.adapter;

import java.util.List;

import com.zm.hsy.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter 资源分数
 * 
 * @author Administrator
 * 
 */
public class AddbbsTopicGridViewAdapter extends BaseAdapter {
	List<String> items;
	Context context;
	private LayoutInflater minflater;
	private int checkItemPosition = 0;

	public void setCheckItem(int position) {
		checkItemPosition = position;
		notifyDataSetChanged();
	}

	public AddbbsTopicGridViewAdapter(Context context, List<String> items) {
		this.minflater = LayoutInflater.from(context);
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
		if (convertView == null) {
			convertView = minflater.inflate(
					R.layout.gridview_score_yuanquanlist, null);
			viewHolder = new ViewHolder();
			viewHolder.score_tv = (TextView) convertView
					.findViewById(R.id.score_tv);
			viewHolder.yuanquan_bottom = (ImageView) convertView
					.findViewById(R.id.yuanquan_bottom);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.score_tv.setText(items.get(position)+"分");
		viewHolder.score_tv.setGravity(Gravity.CENTER);
		viewHolder.yuanquan_bottom.setPadding(10, 10, 10, 10);
		fillValue(position, viewHolder);
		return convertView;

	}

	private void fillValue(int position, ViewHolder viewHolder) {

		if (checkItemPosition != -1) {
			if (checkItemPosition == position) {
				viewHolder.yuanquan_bottom.setSelected(true);
			} else {
				viewHolder.yuanquan_bottom.setSelected(false);

			}
		}
	}

	static class ViewHolder {
		TextView score_tv;
		ImageView yuanquan_bottom;
	}
}
