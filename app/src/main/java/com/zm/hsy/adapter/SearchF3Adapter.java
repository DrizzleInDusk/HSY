package com.zm.hsy.adapter;

import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
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

/**
 * Adapter 搜索主播
 * 
 * @author Administrator
 * 
 */
public class SearchF3Adapter extends BaseAdapter {
	Context context;
	private LayoutInflater minflater;
	private ArrayList<User> userList = new ArrayList<User>();

	public SearchF3Adapter(Context context, ArrayList<User> userList) {
		this.minflater = LayoutInflater.from(context);
		this.context = context;
		this.userList = userList;
	}

	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_search_item3, null);
			holder = new ViewHolder();
			holder.uesr_name = (TextView) convertView
					.findViewById(R.id.search_uesr_name);
			holder.uesr_audioCount = (TextView) convertView
					.findViewById(R.id.search_uesr_audioCount);
			holder.uesr_concemFans = (TextView) convertView
					.findViewById(R.id.search_uesr_concemFans);
			holder.uesr_zuiduo = (TextView) convertView
					.findViewById(R.id.search_uesr_zuiduo);

			holder.uesr_head = (RoundedImageView) convertView
					.findViewById(R.id.search_uesr_head);
			holder.uesr_members = (ImageView) convertView
					.findViewById(R.id.search_uesr_members);
			
			holder.rl = (RelativeLayout) convertView
					.findViewById(R.id.rl);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		
		User user = userList.get(position);
		final String user_id = user.getId();
		String nickname = user.getNickname();
		String dingwei = user.getDingwei();//定位
		String members = user.getMembers();//2会员标识
		String audioCount = user.getAudioCount();//声音数
		String concemFans = user.getConcemFans();//粉丝数
		
		
		if(dingwei!=null&&!dingwei.equals("null")&&!dingwei.equals("")){
			holder.uesr_zuiduo.setText(dingwei);// 定位？？？
		}else{
			holder.uesr_zuiduo.setVisibility(View.VISIBLE);//定位？？？
		}
		holder.uesr_name.setText(nickname);
		if(members.equals("2")){
			holder.uesr_members.setVisibility(View.VISIBLE);
		}else{
			holder.uesr_members.setVisibility(View.GONE);
		}
		holder.uesr_audioCount.setText("声音:"+audioCount);
		holder.uesr_concemFans.setText("粉丝:"+concemFans);
		
		String type = user.getHeadStatus();
		String head = user.getHead();
		if (!type.equals("http")) {
			head = URLManager.Head_URL + head;
		}
		System.out.println("head---"+head);
		Picasso.with(context).load(head).resize(400, 400)
				.placeholder(R.mipmap.touxiang).error(R.mipmap.touxiang)
				.into(holder.uesr_head);
		holder.rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
				.gotoZhuboActivity(user_id);
			}
		});
		
		return convertView;
	}

	static class ViewHolder {
		TextView uesr_name, uesr_audioCount, uesr_concemFans, uesr_zuiduo;
		RoundedImageView uesr_head;
		ImageView uesr_members;
		RelativeLayout rl;
	}
}
