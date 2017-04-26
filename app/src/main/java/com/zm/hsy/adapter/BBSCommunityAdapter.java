package com.zm.hsy.adapter;

import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.Community;
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

public class BBSCommunityAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<Community> bbsList = new ArrayList<Community>();;

	public BBSCommunityAdapter(Context context, ArrayList<Community> bbsList) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.bbsList = bbsList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bbsList.size();
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
			convertView = minflater.inflate(R.layout.activity_bbs_mybbsitem,
					null);
			holder = new ViewHolder();
			holder.bbs_num = (TextView) convertView.findViewById(R.id.bbs_num);
			holder.bbs_blurb = (TextView) convertView
					.findViewById(R.id.bbs_blurb);
			holder.bbs_name = (TextView) convertView
					.findViewById(R.id.bbs_name);
			holder.bbs_memCount = (TextView) convertView
					.findViewById(R.id.bbs_memCount);
			holder.bbs_topicCount = (TextView) convertView
					.findViewById(R.id.bbs_topicCount);
			holder.bbs_cover = (ImageView) convertView
					.findViewById(R.id.bbs_cover);
			holder.bbs_community = (RelativeLayout) convertView
					.findViewById(R.id.bbs_community);
			
			holder.single_line = (View) convertView
					.findViewById(R.id.bbs_single_line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position==0){
			holder.single_line.setVisibility(View.GONE);
		}
		String name = bbsList.get(position).getName();
		String blurb = bbsList.get(position).getBlurb();
		String memCount = bbsList.get(position).getMemCount();
		String topicCount = bbsList.get(position).getTopicCount();
		int n = position + 1;
		holder.bbs_num.setText("" + n);
		holder.bbs_name.setText(name);
		holder.bbs_blurb.setText(blurb);
		if (memCount != null && !memCount.equals("")
				&& !memCount.equals("null")) {
			holder.bbs_memCount.setText("关注：" + memCount);
		} else {
			holder.bbs_memCount.setText("关注：0");
		}
		if (topicCount != null && !topicCount.equals("")
				&& !topicCount.equals("null")) {
			holder.bbs_topicCount.setText("帖子：" + topicCount);
		} else {
			holder.bbs_topicCount.setText("帖子：0");
		}

		String cover = bbsList.get(position).getCover();// 封面图
		cover = URLManager.COVER_URL + cover;
		Picasso.with(context).load(cover).resize(400, 400).error(R.mipmap.bbs_img2).into(holder.bbs_cover);
		holder.bbs_community.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl
						.getInstance((Activity) context)
						.gotoBBSCommunityActivity(bbsList.get(position).getId());
			}
		});
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout bbs_community;
		private ImageView bbs_cover;
		private TextView bbs_num, bbs_name, bbs_blurb, bbs_memCount,
				bbs_topicCount;
		private View single_line;
	}

}
