package com.zm.hsy.adapter;

import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter 找朋友
 * 
 * @author Administrator
 * 
 */
public class FindUserListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private Handler handler;
	private ArrayList<User> userList = new ArrayList<User>();

	public FindUserListAdapter(Context context, ArrayList<User> userList,
			Handler handler) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.userList = userList;
		this.context = context;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		return userList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_find_item, null);
			holder = new ViewHolder();
			holder.find_audio = (TextView) convertView
					.findViewById(R.id.find_audio);
			holder.find_nickname = (TextView) convertView
					.findViewById(R.id.find_nickname);
			holder.find_fans = (TextView) convertView
					.findViewById(R.id.find_fans);
			holder.find_zuiduo = (TextView) convertView
					.findViewById(R.id.find_zuiduo);
			holder.find_head = (RoundedImageView) convertView
					.findViewById(R.id.find_head);
			holder.find_cutandadd_press = (ImageView) convertView
					.findViewById(R.id.find_cutandadd_press);
			holder.find_ll = (LinearLayout) convertView
					.findViewById(R.id.find_ll);
			holder.find_rl1 = (RelativeLayout) convertView
					.findViewById(R.id.find_rl1);

			holder.sing_line = (View) convertView.findViewById(R.id.sing_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.sing_line.setVisibility(View.GONE);
		}
		String ifgz = userList.get(position).getIfgz();// 关注标识
		if (ifgz.equals("0")) {
			holder.find_cutandadd_press.setSelected(true);
		} else {
			holder.find_cutandadd_press.setSelected(false);
		}
		String nickname = userList.get(position).getNickname();// 音频路径
		String concemFans = userList.get(position).getConcemFans();// 粉丝
		String audioCount = userList.get(position).getAudioCount();// 发布的声音

		holder.find_audio.setText("声音  " + audioCount);
		holder.find_nickname.setText(nickname);
		holder.find_fans.setText("粉丝  " + concemFans);
		// holder.cf_zuiduo.setText(playAmount);
		holder.find_zuiduo.setVisibility(View.GONE);
		String cover = userList.get(position).getHead();// 封面图
		String type = userList.get(position).getHeadStatus();
		if (!type.equals("http")) {
			cover = URLManager.Head_URL + cover;
		}
		Picasso.with(context).load(cover).resize(400, 400)
				.placeholder(R.mipmap.details_img3)
				.error(R.mipmap.details_img3).into(holder.find_head);

		holder.find_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoZhuboActivity(userList.get(position).getId());

			}
		});
		holder.find_rl1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoZhuboActivity(userList.get(position).getId());

			}
		});
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout find_rl1;
		private LinearLayout find_ll;
		private View sing_line;
		private RoundedImageView find_head;
		private ImageView find_cutandadd_press;
		private TextView find_audio, find_nickname, find_fans, find_zuiduo;
	}

}
