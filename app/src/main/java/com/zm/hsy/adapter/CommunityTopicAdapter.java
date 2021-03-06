package com.zm.hsy.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.zm.hsy.R;
import com.zm.hsy.entity.Topic;
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
import android.widget.TextView;

/**
 * Adapter 专辑列表 没有表头
 * 
 * @author Administrator
 * 
 */
public class CommunityTopicAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<Topic> bbsList = new ArrayList<Topic>();;
	private Handler handler;

	public CommunityTopicAdapter(Context context, ArrayList<Topic> bbsList,
			Handler handler) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.bbsList = bbsList;
		this.context = context;
		this.handler = handler;

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
			convertView = minflater.inflate(R.layout.someonebbs_item2, null);
			holder = new ViewHolder();
			holder.user_nickname = (TextView) convertView
					.findViewById(R.id.item_user_nickname);
			holder.topic_title = (TextView) convertView
					.findViewById(R.id.item_title);
			holder.item_time = (TextView) convertView
					.findViewById(R.id.tv_item_time);
			holder.topic_content = (TextView) convertView
					.findViewById(R.id.item_content);
			holder.topic_count = (TextView) convertView
					.findViewById(R.id.item_count);

			holder.item_zhiding = (ImageView) convertView
					.findViewById(R.id.iv_item_zhiding);
			holder.user_head = (RoundedImageView) convertView
					.findViewById(R.id.item_user_head);
			holder.topic_picture1 = (ImageView) convertView
					.findViewById(R.id.item_picture1);
			holder.topic_picture2 = (ImageView) convertView
					.findViewById(R.id.item_picture2);
			holder.topic_picture3 = (ImageView) convertView
					.findViewById(R.id.item_picture3);

			holder.item_picturell = (LinearLayout) convertView
					.findViewById(R.id.item_picturell);
			holder.topic_ll = (LinearLayout) convertView
					.findViewById(R.id.topic_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String title = bbsList.get(position).getTitle();
		String content = bbsList.get(position).getContent();
		String count = bbsList.get(position).getCount();
		String addTime = bbsList.get(position).getAddTime();
		final String score = bbsList.get(position).getScore();
		final String communityId = bbsList.get(position).getCommunityId();
		final String topicid = bbsList.get(position).getId();

		String nickname = bbsList.get(position).getNickname();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date d1 = df.parse(addTime);
			Date curDate = new Date(System.currentTimeMillis());
			long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
			long hours = diff / (1000 * 60 * 60);
			if (hours > 24) {
				long days = hours / 24;
				if (30 >= days && days >= 1) {
					holder.item_time.setText(days + "天前");
				} else if (days > 30) {
					long m = days / 30;
					if (m > 12) {
						long y = m / 12;
						holder.item_time.setText(y + "年前");
					} else {
						holder.item_time.setText(m + "个月前");
					}
				}
			} else {
				if (hours < 1) {
					holder.item_time.setText("刚刚");
				} else {
					holder.item_time.setText(hours + "小时前");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		holder.user_nickname.setText(nickname);
		holder.topic_title.setText(title);
		holder.topic_content.setText(content);

		if (count != null && !count.equals("") && !count.equals("null")) {
			holder.topic_count.setText("回复数：" + count);
		} else {
			holder.topic_count.setText("回复数：0");

		}
		try {

			String head = bbsList.get(position).getHead();// 封面图
			String type = bbsList.get(position).getHeadStatus();
			if (!type.equals("http")) {
				head = URLManager.Head_URL + head;
			}
			Picasso.with(context).load(head).resize(400, 400)
					.placeholder(R.mipmap.img_fang)
					.error(R.mipmap.img_fang).into(holder.user_head);

			String picture = bbsList.get(position).getPicture();

			if (picture != null && !picture.equals("")
					&& !picture.equals("null")) {
				picture = URLManager.TopicCOVER_URL + picture;
				holder.item_picturell.setVisibility(View.VISIBLE);
				holder.topic_picture1.setVisibility(View.VISIBLE);
				holder.topic_picture2.setVisibility(View.VISIBLE);
				holder.topic_picture3.setVisibility(View.VISIBLE);
				Picasso.with(context).load(picture).into(holder.topic_picture1);
			} else {
				holder.item_picturell.setVisibility(View.GONE);
				holder.topic_picture1.setVisibility(View.GONE);
				holder.topic_picture2.setVisibility(View.GONE);
				holder.topic_picture3.setVisibility(View.GONE);
			}
		} catch (Exception e) {
		}

		holder.item_zhiding.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				String userid = Futil.getValue(context, "userid");
				String strUrl = URLManager.TopicTop;
				HashMap<String, String> map = new HashMap<String, String>();
				if(userid!=null&&!userid.equals("")){
					map.put("user.id", userid);
					map.put("communityTopic.id", topicid);
					map.put("communityTopic.communityId", communityId);
					Futil.xutils(strUrl, map, handler, 100002);
				}else{
					Futil.showMessage(context, "请先登录");
				}
				
			}
		});
		holder.topic_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoBBSCardActivity(topicid);

			}

		});
		return convertView;
	}

	class ViewHolder {
		private LinearLayout topic_ll, item_picturell;
		private ImageView item_zhiding;
		private RoundedImageView user_head;
		private ImageView topic_picture1, topic_picture2, topic_picture3;
		private TextView user_nickname, item_time, topic_title, topic_content,
				topic_count;
	}

}
