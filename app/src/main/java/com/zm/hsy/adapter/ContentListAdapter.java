package com.zm.hsy.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zm.hsy.R;
import com.zm.hsy.entity.ContentList;
import com.zm.hsy.https.URLManager;
import com.squareup.picasso.Picasso;
import com.zm.hsy.myview.RoundedImageView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter 评论列表
 * 
 * @author Administrator
 * 
 */
public class ContentListAdapter extends BaseAdapter {
	private Context context;
	private Handler handler;
	private LayoutInflater minflater;
	private ArrayList<ContentList> arraycList = new ArrayList<ContentList>();

	public ContentListAdapter(Context context, ArrayList<ContentList> arraycList,Handler handler) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.arraycList = arraycList;
		this.context = context;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraycList.size();
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

			convertView = minflater.inflate(
					R.layout.details_playpage_item_dianping, null);
			holder = new ViewHolder();

			holder.dianping_nickname1 = (TextView) convertView
					.findViewById(R.id.playpage_dianping_nickname1);
			holder.dianping_content1 = (TextView) convertView
					.findViewById(R.id.playpage_dianping_content1);
			holder.time_tv1 = (TextView) convertView
					.findViewById(R.id.dianping_time_tv1);
			holder.dianping_head1 = (RoundedImageView) convertView
					.findViewById(R.id.playpage_dianping_head1);

			holder.details_rl1 = (RelativeLayout) convertView
					.findViewById(R.id.details_rl1);
			holder.dianping_huifu_tv1 = (LinearLayout) convertView
					.findViewById(R.id.dianping_huifu_tv1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (arraycList.size() > 0) {

			String addtime = arraycList.get(position).getAddtime();
			String content = arraycList.get(position).getContent();
			String id = arraycList.get(position).getId();
			String nickname = arraycList.get(position).getNickname();// 评论人
			String replyname = arraycList.get(position).getReplyname();// 对谁评论

			holder.dianping_nickname1.setText(nickname);
			if (replyname != null && !replyname .equals("") &&!replyname .equals("null")) {
				holder.dianping_content1.setText("@" + replyname +" "+ content);
			} else {
				holder.dianping_content1.setText(content);

			}

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				Date d1 = df.parse(addtime);
				Date curDate = new Date(System.currentTimeMillis());
				long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
				long hours = diff / (1000 * 60 * 60);
				System.out.println("hours----" + hours);
				if (hours > 24) {
					long days = hours / 24;
					if (30 >= days && days >= 1) {
						holder.time_tv1.setText(days + "天前");
					} else if (days > 30) {
						long m = days / 30;
						if (m > 12) {
							long y = m / 12;
							holder.time_tv1.setText(y + "年前");
						} else {
							holder.time_tv1.setText(m + "个月前");
						}
					}
				} else {
					if (hours < 1) {
						holder.time_tv1.setText("刚刚");
					} else {
						holder.time_tv1.setText(hours + "小时前");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			String head = arraycList.get(position).getHead();// 封面图
			head = URLManager.COVER_URL + head;
			Picasso.with(context).load(head).resize(400, 400)
					.placeholder(R.mipmap.playpage_dianping_img)
					.error(R.mipmap.playpage_dianping_img)
					.into(holder.dianping_head1);

			holder.dianping_huifu_tv1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg1) {
					Message message =new Message();
					message.what=10086;
					message.obj=position;
					handler.sendMessage(message);
				}
			});

		} else {
			holder.details_rl1.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		private LinearLayout details_ll;

		private TextView dianping_commentNumber, dianping_nickname1,
				dianping_content1,time_tv1, details_more;
		private RelativeLayout details_rl1;
		private RoundedImageView dianping_head1;
		private LinearLayout dianping_huifu_tv1;

	}

}
