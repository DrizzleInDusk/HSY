package com.zm.hsy.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.zm.hsy.R;
import com.zm.hsy.entity.Album;
import com.zm.hsy.util.ActivityJumpControl;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Tabvf1SjListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private Map<String, ArrayList<Album>> albumMap;

	public Tabvf1SjListAdapter(Context context,
			Map<String, ArrayList<Album>> albumMap) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.albumMap = albumMap;
		this.context = context;
	}

	@Override
	public int getCount() {
		return albumMap.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String title = null;
		ArrayList<Album> albumList = null;
		for (String key : albumMap.keySet()) {
			albumList = albumMap.get(key);
			if (position == 0) {
				title = key;
				break;
			}
			position--;
		}
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.tab_pager_tuijian_item,
					null);
			holder = new ViewHolder();
			holder.item_name = (TextView) convertView
					.findViewById(R.id.tuijian_item_name);
			holder.item_more = (RelativeLayout) convertView
					.findViewById(R.id.tuijian_item_more);
			holder.tabvf1_gridview = (GridView) convertView
					.findViewById(R.id.tabvf1_gridview);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Album album = null;
		final String itemname;
		if(!albumList.get(0).getLabel().equals("")){
			itemname=albumList.get(0).getLabel();
			holder.item_name.setText("" + albumList.get(0).getLabel());
		}else{
			itemname="听" + title;
			holder.item_name.setText("听" + title);
		}
		final Album oalbum = albumList.get(0);
		holder.item_more.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActivityJumpControl.getInstance(
						(Activity) context).gotoMoreAlbumActivity(
						oalbum.getRid(),itemname);
				System.out.println("oalbum.getRid()>>>>"+oalbum.getRid());
				System.out.println("itemname>>>>>>"+itemname);
			}
		});
		holder.tabvf1_gridview.setAdapter(new Tabvf1GridAdapter(context,albumList));

		return convertView;
	}

	class ViewHolder {
		private GridView tabvf1_gridview;
		private LinearLayout item_ll1, item_ll2;
		private RelativeLayout item_rl1, item_rl2, item_rl3, item_rl4,
				item_rl5, item_rl6;
		private RelativeLayout item_more;
		private TextView item_name;
		private ImageView item_cover1, item_cover2, item_cover3, item_cover4,
				item_cover5, item_cover6;
		private TextView item_blurb1, item_blurb2, item_blurb3, item_blurb4,
				item_blurb5, item_blurb6;

	}

}
