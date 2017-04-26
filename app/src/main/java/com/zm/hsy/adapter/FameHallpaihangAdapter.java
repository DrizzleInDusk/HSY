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
public class FameHallpaihangAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<User> userList = new ArrayList<User>();

	public FameHallpaihangAdapter(Context context, ArrayList<User> userList) {
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
					R.layout.activity_bbs_famehall_paihang, null);
			holder = new ViewHolder();
			holder.paihang_name = (TextView) convertView
					.findViewById(R.id.famehall_paihang_name);
			holder.paihang_fensnum = (TextView) convertView
					.findViewById(R.id.famehall_paihang_fensnum);
			holder.paihang_num = (TextView) convertView
					.findViewById(R.id.famehall_paihang);

			holder.paihang_head = (RoundedImageView) convertView
					.findViewById(R.id.famehall_paihang_head);

			holder.paihang_rl1 = (RelativeLayout) convertView
					.findViewById(R.id.famehall_paihang_rl1);
			
			holder.single_line = (View) convertView
					.findViewById(R.id.famehall_single_line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position==0){
			holder.single_line.setVisibility(View.GONE);
		}
		String nickname = userList.get(position).getNickname();
		String fans = userList.get(position).getFans();

		holder.paihang_name.setText(nickname);
		if (fans != null && !fans.equals("") && !fans.equals("null")) {
			holder.paihang_fensnum.setText(fans);
		} else {
			holder.paihang_fensnum.setText("0");
		}
		int n = position + 1;
		holder.paihang_num.setText(""+n);

		String head = userList.get(position).getHead();// 封面图
		String type = userList.get(position).getHeadStatus();
		if (!type.equals("http")) {
			head = URLManager.Head_URL + head;
		}
		Picasso.with(context).load(head).resize(400, 400)
				.placeholder(R.mipmap.xionghaiz)
				.error(R.mipmap.xionghaiz).into(holder.paihang_head);

		holder.paihang_rl1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
				.gotoZhuboActivity(userList.get(position).getId());
			}
		});
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout paihang_rl1;
		private RoundedImageView paihang_head;
		private TextView paihang_name, paihang_num, paihang_fensnum;
		private View single_line;
	}

}
