package com.zm.hsy.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zm.hsy.R;
import com.zm.hsy.adapter.MypushMessageAdapter.ViewHolder;
import com.zm.hsy.entity.MyMessage;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter 社区音频消息
 * 
 * @author Administrator
 * 
 */
public class MyABMessageAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<MyMessage> messlist;
	private String Tag;

	public MyABMessageAdapter(Context context, ArrayList<MyMessage> messlist,
			String Tag) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.context = context;
		this.messlist = messlist;
		this.Tag = Tag;
	}

	@Override
	public int getCount() {
		return messlist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(
					R.layout.activity_audiomessage_item, null);
			holder = new ViewHolder();
			holder.messageitem1_count = (TextView) convertView
					.findViewById(R.id.messageitem1_count);
			holder.messageitem1_towhat = (TextView) convertView
					.findViewById(R.id.messageitem1_towhat);
			holder.messageitem1_time = (TextView) convertView
					.findViewById(R.id.messageitem1_time);
			holder.messageitem1_head = (RoundedImageView) convertView
					.findViewById(R.id.messageitem1_head);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MyMessage myMessage = messlist.get(position);
		String head = myMessage.getHead();
		String headStatus = myMessage.getHeadStatus();
		if (!headStatus.equals("http")) {
			head = URLManager.Head_URL + head;

		}
		Picasso.with(context).load(head).resize(400, 400)
				.error(R.mipmap.mess_img).centerCrop()
				.into(holder.messageitem1_head);
		String addTime = myMessage.getAddTime();
		if (addTime.equals("")) {
			holder.messageitem1_time.setText("");
		} else {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date d1 = df.parse(addTime);
				Date curDate = new Date(System.currentTimeMillis());
				long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
				long hours = diff / (1000 * 60 * 60);
				if (hours > 24) {
					long days = hours / 24;
					if (30 >= days && days >= 1) {
						holder.messageitem1_time.setText(days + "天前");
					} else if (days > 30) {
						long m = days / 30;
						if (m > 12) {
							long y = m / 12;
							holder.messageitem1_time.setText(y + "年前");
						} else {
							holder.messageitem1_time.setText(m + "个月前");
						}
					}
				} else {
					if (hours < 1) {
						holder.messageitem1_time.setText("刚刚");
					} else {
						holder.messageitem1_time.setText(hours + "小时前");
					}
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		holder.messageitem1_count.setText(myMessage.getContent());
		if (Tag.equals("1")) {
			holder.messageitem1_towhat
					.setText(Html.fromHtml("<font color=\"#1abc9c\"> "
							+ myMessage.getNickname()
							+ " </font><font color=\"black\">  评论了我的声音:</font><font color=\"#1abc9c\"> "
							+ myMessage.getAudioname() + " </font>"));
		} else {
			holder.messageitem1_towhat
					.setText(Html.fromHtml("<font color=\"#1abc9c\"> "
							+ myMessage.getNickname()
							+ " </font><font color=\"black\">  在帖子</font><font color=\"#1abc9c\"> "
							+ myMessage.getYycontent() +" </font><font color=\"#black\"> 中回复了我 </font>"));
		}

		return convertView;
	}

	class ViewHolder {
		private RoundedImageView messageitem1_head;
		private TextView messageitem1_count, messageitem1_towhat,
				messageitem1_time;
	}

}
