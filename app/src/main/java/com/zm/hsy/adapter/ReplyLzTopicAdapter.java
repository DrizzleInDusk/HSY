package com.zm.hsy.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zm.hsy.R;
import com.zm.hsy.entity.TopicContentList;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReplyLzTopicAdapter extends BaseAdapter {
	private Context context;
	private String uid;
	private int num = 0;
	private LayoutInflater minflater;
	private ArrayList<TopicContentList> bbsList = new ArrayList<TopicContentList>();;
	private Handler handler;
	public ReplyLzTopicAdapter(Context context,
			ArrayList<TopicContentList> bbsList, String uid,Handler handler) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.bbsList = bbsList;
		this.context = context;
		this.uid = uid;
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
			convertView = minflater
					.inflate(R.layout.bbs_topic_reply_item, null);
			holder = new ViewHolder();
			holder.user_head = (RoundedImageView) convertView
					.findViewById(R.id.reply_user_head);
			holder.user_nickname = (TextView) convertView
					.findViewById(R.id.reply_user_nickname);
			holder.item_floor = (TextView) convertView
					.findViewById(R.id.tv_item_floor);
			holder.item_time = (TextView) convertView
					.findViewById(R.id.reply_item_time);
			holder.reply_content = (TextView) convertView
					.findViewById(R.id.reply_content);
			holder.reply_yyname = (TextView) convertView
					.findViewById(R.id.reply_yyname);
			holder.reply_yycontent = (TextView) convertView
					.findViewById(R.id.reply_yycontent);
			holder.reply_iv = (ImageView) convertView
					.findViewById(R.id.reply_iv);
			holder.reply_tv = (TextView) convertView
					.findViewById(R.id.reply_tv);

			holder.item_picturell = (LinearLayout) convertView
					.findViewById(R.id.reply_item_picturell);
			holder.topic_picture1 = (ImageView) convertView
					.findViewById(R.id.reply_item_picture1);
			holder.topic_picture2 = (ImageView) convertView
					.findViewById(R.id.reply_item_picture2);
			holder.topic_picture3 = (ImageView) convertView
					.findViewById(R.id.reply_item_picture3);

			holder.reply_yy = (LinearLayout) convertView
					.findViewById(R.id.reply_yy);
			holder.reply_landlord_tv = (TextView) convertView
					.findViewById(R.id.reply_landlord_tv);

			holder.reply_ll = (LinearLayout) convertView
					.findViewById(R.id.reply_ll);
			holder.single_line = (View) convertView
					.findViewById(R.id.reply_single_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String id = bbsList.get(position).getUid();
		if (uid.equals(id)) {
			if (num == 0) {
				holder.single_line.setVisibility(View.GONE);
				num++;
			}
			holder.reply_landlord_tv.setVisibility(View.VISIBLE);
			holder.reply_ll.setVisibility(View.VISIBLE);
			String content = bbsList.get(position).getContent();
			String name = bbsList.get(position).getName();
			String time = bbsList.get(position).getTime();
			String yycontent = bbsList.get(position).getYycontent();
			String yyname = bbsList.get(position).getYyname();
			if (yyname != null && !yyname.equals("") && !yyname.equals("null")) {
				holder.reply_yy.setVisibility(View.VISIBLE);
				holder.reply_yyname.setText(yyname);
				holder.reply_yycontent.setText(yycontent);
			} else {
				holder.reply_yy.setVisibility(View.GONE);

			}

			int f = position + 1;
			holder.item_floor.setText(f + "楼");
			holder.user_nickname.setText(name);
			holder.reply_content.setText(content);

			try {


				String head = bbsList.get(position).getHead();// 封面图
				String type = bbsList.get(position).getHeadStatus();
				if (!type.equals("http")) {
					head = URLManager.Head_URL + head;
				}
				Picasso.with(context).load(head).resize(400, 400)
						.placeholder(R.mipmap.xionghaiz)
						.error(R.mipmap.xionghaiz).into(holder.user_head);
				String picture = bbsList.get(position).getPicture();
				if (picture != null && !picture.equals("")
						&& !picture.equals("null")) {
					picture = URLManager.TopicCOVER_URL + picture;
					System.out.println("picture>>>>"+picture);
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

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				Date d1 = df.parse(time);
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
		} else {
			holder.reply_ll.setVisibility(View.GONE);
		}
		// holder.reply_iv.setOnClickListener(l)
		holder.reply_tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.obj = position;
				msg.what = URLManager.four;
				handler.sendMessage(msg);
			}
		});
		holder.user_head.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActivityJumpControl.getInstance((Activity) context)
						.gotoZhuboActivity(bbsList.get(position).getId());
				Log.i("bbscard", "" + bbsList.get(position).getId());
			}
		});
		return convertView;
	}

	class ViewHolder {
		private LinearLayout item_picturell, reply_yy, reply_ll;
		private RoundedImageView user_head;
		private ImageView  topic_picture1, topic_picture2,
				topic_picture3;
		private ImageView reply_iv;
		private TextView user_nickname, item_floor, item_time, reply_content,
				reply_landlord_tv, reply_yycontent, reply_yyname, reply_tv;
		private View single_line;
	}

}
