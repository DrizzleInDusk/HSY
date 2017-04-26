package com.zm.hsy.adapter;

import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FameHalladminAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<User> userList = new ArrayList<User>();

	public FameHalladminAdapter(Context context, ArrayList<User> userList) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.userList = userList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(
					R.layout.activity_bbs_famehall_guanli, null);
			holder = new ViewHolder();
			holder.guanli_name = (TextView) convertView
					.findViewById(R.id.famehall_guanli_name);
			holder.guanli_blurb = (TextView) convertView
					.findViewById(R.id.famehall_guanli_blurb);

			holder.guanli_head = (RoundedImageView) convertView
					.findViewById(R.id.famehall_guanli_head);

			holder.guanli_rl1 = (RelativeLayout) convertView
					.findViewById(R.id.famehall_guanli_rl1);
			holder.single_line = (View) convertView
					.findViewById(R.id.famehall_single_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.single_line.setVisibility(View.GONE);
		}

		String blurb = userList.get(position).getBlurb();

		holder.guanli_name.setText(userList.get(position).getNickname());

		if (blurb != null && !blurb.equals("") &&!blurb.equals("null") ){
			holder.guanli_blurb.setText(userList.get(position).getBlurb());
		} else {
			holder.guanli_blurb.setText("暂无简介");
		}

		String head = userList.get(position).getHead();// 封面图
		String type = userList.get(position).getHeadStatus();
		if (!type.equals("http")) {
			head = URLManager.Head_URL + head;
		}
		Picasso.with(context).load(head).resize(400, 400)
				.placeholder(R.mipmap.xionghaiz)
				.error(R.mipmap.xionghaiz).into(holder.guanli_head);

		holder.guanli_rl1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoZhuboActivity(userList.get(position).getId());
			}
		});
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout guanli_rl1;
		private RoundedImageView guanli_head;
		private TextView guanli_name, guanli_blurb;
		private View single_line;
	}

}
