package com.zm.hsy.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zm.hsy.R;
import com.zm.hsy.entity.MyMessage;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
 * Adapter 消息
 * 
 * @author Administrator
 * 
 */
public class MyMessageAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<MyMessage> messlist;
	private TextView deletetv; // 用于执行删除的button
	private GestureDetector detector;
	private MessFlingListeber listener;
	private Handler handler;

	public MyMessageAdapter(Context context, ArrayList<MyMessage> messlist,
			Handler handler) {
		super();
		this.context = context;
		this.minflater = LayoutInflater.from(context);
		this.messlist = messlist;
		this.handler = handler;

		minflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listener = new MessFlingListeber();
		detector = new GestureDetector(listener);
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

	private ArrayList<TextView> list = new ArrayList<TextView>();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_message_item,
					null);
			holder = new ViewHolder();
			holder.message_name = (TextView) convertView
					.findViewById(R.id.message_name);
			holder.message_content = (TextView) convertView
					.findViewById(R.id.message_content);
			holder.message_time = (TextView) convertView
					.findViewById(R.id.message_time);
			holder.message_num = (TextView) convertView
					.findViewById(R.id.message_num);
			holder.mess_img = (ImageView) convertView
					.findViewById(R.id.mess_img);
			holder.message_num_rl = (RelativeLayout) convertView
					.findViewById(R.id.message_num_rl);
			holder.messrl = (RelativeLayout) convertView
					.findViewById(R.id.messrl);

			holder.deletetv = (TextView) convertView
					.findViewById(R.id.message_delete);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MyMessage myMessage = messlist.get(position);
		String head = myMessage.getHead();
		String headStatus = myMessage.getHeadStatus();
		String nickname = myMessage.getNickname();
		if (!headStatus.equals("http")) {
			head = URLManager.Head_URL + head;
		}
		Picasso.with(context).load(head).resize(400, 400)
				.error(R.mipmap.mess_img2).centerCrop().into(holder.mess_img);
		holder.message_name.setText(nickname);
		holder.message_content.setText(myMessage.getContent());
		String num = myMessage.getNum();
		if (num.equals("0")) {
			holder.message_num_rl.setVisibility(View.GONE);
		} else {
			holder.message_num_rl.setVisibility(View.VISIBLE);
			holder.message_num.setText(myMessage.getNum());
		}
		String addTime = myMessage.getAddTime();
		if (addTime.equals("")) {
			holder.message_time.setText("");
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
						holder.message_time.setText(days + "天前");
					} else if (days > 30) {
						long m = days / 30;
						if (m > 12) {
							long y = m / 12;
							holder.message_time.setText(y + "年前");
						} else {
							holder.message_time.setText(m + "个月前");
						}
					}
				} else {
					if (hours < 1) {
						holder.message_time.setText("刚刚");
					} else {
						holder.message_time.setText(hours + "小时前");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		holder.item = myMessage;
		deletetv = holder.deletetv; // 赋值给全局button，一会儿用
		convertView.setOnTouchListener(new View.OnTouchListener() { // 为每个item设置setOnTouchListener事件

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						detector.onTouchEvent(event);
						ViewHolder h = (ViewHolder) v.getTag();
						deletetv.setVisibility(View.GONE);
						listener.setItem(h.item);
						listener.setHolder(h);
						return true;
					}

				});
		final String fuid = myMessage.getId();
		holder.deletetv.setOnClickListener(new OnClickListener() { // 为button绑定事件

					@Override
					public void onClick(View v) {
						if (deletetv != null) {
							deletetv.setVisibility(View.GONE); // 点击删除按钮后，影藏按钮

							Message msg1 = new Message();
							msg1.obj = fuid;
							msg1.what = URLManager.two;
							handler.sendMessage(msg1);
						}

					}
				});

		return convertView;
	}

	// private void getPushMes(String fuid,String tuid,String pft) {
	// String strUrl = URLManager.upPushMessageByDel;
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("fuid", fuid);
	// map.put("tuid", tuid);
	// map.put("pft", pft);
	// System.out.println("---fuid---"+fuid);
	// System.out.println("---tuid---"+tuid);
	// System.out.println("---pft---"+pft);
	// Futil.xutils(strUrl, map, handler, URLManager.two);
	// }
	class MessFlingListeber implements GestureDetector.OnGestureListener {

		MyMessage item;
		ViewHolder holder;

		public MyMessage getItem() {
			return item;
		}

		public void setItem(MyMessage item) {
			this.item = item;
		}

		public ViewHolder getHolder() {
			return holder;
		}

		public void setHolder(ViewHolder holder) {
			this.holder = holder;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if (e2.getX() - e1.getX() > 20) {// 右滑
				holder.deletetv.setVisibility(View.GONE);
				list.remove(holder.deletetv);
			} else if (e1.getX() - e2.getX() > 20) {// 左滑
				for (int i = 0; i < list.size(); i++) {
					list.remove(i).setVisibility(View.GONE);
				}
				holder.deletetv.setVisibility(View.VISIBLE);
				list.add(holder.deletetv);
			}

			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			String fuid = item.getId();
			String nickname = item.getNickname();
			if (fuid.equals("0")) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoMyPushMessageActivity("4", fuid, nickname);
			} else {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoMyPushMessageActivity("3", fuid, nickname);
			}
			return false;
		}

	}

	class ViewHolder {
		private TextView deletetv;// 用于执行删除的button
		private RelativeLayout message_num_rl, messrl;
		private ImageView mess_img;
		private TextView message_name, message_content, message_time,
				message_num;
		private MyMessage item;
	}

}
