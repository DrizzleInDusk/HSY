package com.zm.hsy.adapter;

import java.util.ArrayList;
import java.util.HashMap;

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
 * Adapter 我的TAG1粉丝与TAG2关注
 * 
 * @author Administrator
 * 
 */
public class MyCFAdapter extends BaseAdapter {
	private Context context;
	private Handler handler;
	private String TAG;
	private LayoutInflater minflater;
	private ArrayList<User> userList = new ArrayList<User>();
	private String userid;

	public MyCFAdapter(Context context, ArrayList<User> userList, String TAG,
			Handler handler) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.userList = userList;
		this.context = context;
		this.TAG = TAG;
		this.handler = handler;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_cf_item, null);
			holder = new ViewHolder();
			holder.cf_audio = (TextView) convertView
					.findViewById(R.id.cf_audio);
			holder.cf_nickname = (TextView) convertView
					.findViewById(R.id.cf_nickname);
			holder.cf_fans = (TextView) convertView.findViewById(R.id.cf_fans);
			holder.cf_zuiduo = (TextView) convertView
					.findViewById(R.id.cf_zuiduo);
			holder.cf_head = (RoundedImageView) convertView
					.findViewById(R.id.cf_head);
			holder.cf_cutandadd_press = (ImageView) convertView
					.findViewById(R.id.cf_cutandadd_press);
			holder.cf_ll = (LinearLayout) convertView.findViewById(R.id.cf_ll);
			holder.cf_rl1 = (RelativeLayout) convertView
					.findViewById(R.id.cf_rl1);

			holder.sing_line = (View) convertView.findViewById(R.id.sing_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.sing_line.setVisibility(View.GONE);
		}

		userid = Futil.getValue(context, "userid");
		final String concemId = userList.get(position).getId();
		if (TAG.equals("1")) {
			holder.cf_cutandadd_press.setSelected(true);
			holder.cf_cutandadd_press
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg1) {
							String strUrl = URLManager.addUserConcem;
							HashMap<String, String> map = new HashMap<String, String>();
							
							map.put("uid", userid);
							map.put("concemId", concemId);
							Futil.xutils(strUrl, map, handler, URLManager.three);

						}
					});
		} else {
			holder.cf_cutandadd_press.setSelected(false);
			holder.cf_cutandadd_press
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg1) {
							String strUrl = URLManager.delUserConcem;
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("uid", userid);
							map.put("concemId", concemId);
							Futil.xutils(strUrl, map, handler, URLManager.four);
							userList.remove(position);
							notifyDataSetChanged();
						}
					});

		}
		String nickname = userList.get(position).getNickname();// 音频路径
		String concemFans = userList.get(position).getConcemFans();// 音频路径
		String audioCount = userList.get(position).getAudioCount();// 音频路径

		holder.cf_audio.setText("声音  " + audioCount);
		holder.cf_nickname.setText(nickname);
		holder.cf_fans.setText("粉丝  " + concemFans);
		// holder.cf_zuiduo.setText(playAmount);
		holder.cf_zuiduo.setVisibility(View.GONE);
		String cover = userList.get(position).getHead();// 封面图
		String type = userList.get(position).getHeadStatus();
		if (!type.equals("http")) {
			cover = URLManager.Head_URL + cover;
		}
		Picasso.with(context).load(cover).resize(400, 400)
				.placeholder(R.mipmap.details_img3)
				.error(R.mipmap.details_img3).into(holder.cf_head);

		holder.cf_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoZhuboActivity(userList.get(position).getId());
			}
		});
		holder.cf_rl1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoZhuboActivity(userList.get(position).getId());

			}
		});
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout cf_rl1;
		private LinearLayout cf_ll;
		private View sing_line;
		private RoundedImageView cf_head;
		private ImageView cf_cutandadd_press;
		private TextView cf_audio, cf_nickname, cf_fans, cf_zuiduo;
	}

}
