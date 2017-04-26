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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BdUserListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<User> userList = new ArrayList<User>();
	private ArrayList<String> fanslist = new ArrayList<String>();

	public BdUserListAdapter(Context context, ArrayList<User> userList,ArrayList<String> fanslist) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.userList = userList;
		this.context = context;
		this.fanslist = fanslist;
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
			convertView = minflater.inflate(R.layout.bangdan_list_userlist,
					null);
			holder = new ViewHolder();
			holder.list_num = (TextView) convertView
					.findViewById(R.id.bangdan_userlist_num);
			holder.list_name = (TextView) convertView
					.findViewById(R.id.bangdan_userlist_name);
			holder.list_blurb = (TextView) convertView
					.findViewById(R.id.bangdan_userlist_blurb);
			holder.fenstv = (TextView) convertView
					.findViewById(R.id.bangdan_userlist_fenstv);
			holder.list_img1 = (RoundedImageView) convertView
					.findViewById(R.id.bangdan_userlist_head);

			holder.bangdan_list = (RelativeLayout) convertView
					.findViewById(R.id.bangdan_userlist);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String blurb = userList.get(position).getBlurb();
		int n=position+1;
		holder.list_num.setText("" + n);
		if(n>4){
			holder.list_num.setTextColor(Color.parseColor("#494949"));
		}else{
			holder.list_num.setTextColor(Color.parseColor("#ff8e1e"));
		}
		
		holder.list_name.setText(userList.get(position).getNickname());

		if (blurb != null && !blurb.equals("") && !blurb.equals("null")) {
			holder.list_blurb.setText(userList.get(position).getBlurb());
		} else {
			holder.list_blurb.setText("暂无简介");
		}
		holder.fenstv.setText(fanslist.get(position));
		String head = userList.get(position).getHead();// 封面图
		String type = userList.get(position).getHeadStatus();// 封面图
		if (!type.equals("http")) {
			head = URLManager.Head_URL + head;
		}
		Picasso.with(context).load(head).resize(400, 400).placeholder(R.color.touming)
				.error(R.mipmap.ic_launcher).centerCrop().into(holder.list_img1);
		holder.bangdan_list.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				 ActivityJumpControl.getInstance((Activity) context)
				 .gotoZhuboActivity(userList.get(position).getId());
			}
		});
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout bangdan_list;
		private RoundedImageView list_img1;
		private TextView list_num, list_name, list_blurb, fenstv;
	}

}
