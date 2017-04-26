package com.zm.hsy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zm.hsy.R;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

/**
 * Adapter 我赞过的
 * 
 * @author Administrator
 * 
 */
public class MyPHSHistoryAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<AudioList> audioList = new ArrayList<AudioList>();
	private TextView deletetv; // 用于执行删除的button
	private GestureDetector detector;
	FlingListeber listener;
	private boolean visflag = false;

	public MyPHSHistoryAdapter(Context context, ArrayList<AudioList> audioList) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.audioList = audioList;
		this.context = context;
		minflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listener = new FlingListeber();
		detector = new GestureDetector(listener);
	}

	public void setVisflag(boolean flag) {
		visflag = flag;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return audioList.size();
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

	private ArrayList<TextView> list = new ArrayList<TextView>();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_phs_history_item,
					null);
			holder = new ViewHolder();
			holder.history_addtime = (TextView) convertView
					.findViewById(R.id.history_addtime);
			holder.history_audioname = (TextView) convertView
					.findViewById(R.id.history_audioname);
			holder.history_blurb = (TextView) convertView
					.findViewById(R.id.history_blurb);
			holder.deletetv = (TextView) convertView
					.findViewById(R.id.history_delete);
			holder.history_cover = (RoundedImageView) convertView
					.findViewById(R.id.history_cover);
			holder.history_choice = (ImageView) convertView
					.findViewById(R.id.history_choice);

			holder.history_ll = (LinearLayout) convertView
					.findViewById(R.id.history_ll);

			holder.sing_line = (View) convertView.findViewById(R.id.sing_line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.sing_line.setVisibility(View.GONE);
		}
		if (visflag) {
			holder.history_choice.setVisibility(View.VISIBLE);
		} else {
			holder.history_choice.setVisibility(View.INVISIBLE);
		}
		final AudioList item = audioList.get(position);
		final String key = audioList.get(position).getKey();// 封面图
		String cover = audioList.get(position).getCover();// 封面图
		String path = audioList.get(position).getPath();
		String blurb = audioList.get(position).getBlurb();
		String audioName = audioList.get(position).getAudioName();
		String addTime = audioList.get(position).getAddTime();

		holder.history_addtime.setText("上次播放时间：" + addTime);
		holder.history_audioname.setText(audioName);
		holder.history_blurb.setText(blurb);
		holder.item = item;

		cover = URLManager.COVER_URL + cover;
		Picasso.with(context).load(cover).resize(400, 400)
				.placeholder(R.mipmap.details_img3)
				.error(R.mipmap.details_img3).into(holder.history_cover);

		deletetv = holder.deletetv; // 赋值给全局button，一会儿用
		convertView.setOnTouchListener(new OnTouchListener() { // 为每个item设置setOnTouchListener事件

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

		holder.deletetv.setOnClickListener(new OnClickListener() { // 为button绑定事件

					@Override
					public void onClick(View v) {

						if (deletetv != null) {
							deletetv.setVisibility(View.GONE); // 点击删除按钮后，影藏按钮
							audioList.remove(position); // 把数据源里面相应数据删除
							ArrayList<String> sKey = Futil.loadKeyArray(
									(Activity) context, "2");
							sKey.remove(key);

							Futil.saveKeyArray((Activity) context, sKey, "2");
							Futil.romveValue((Activity) context, key, "2");// 把缓存里面相应数据删除
							notifyDataSetChanged();
						}

					}
				});
		holder.history_choice.setOnClickListener(new OnClickListener() { // 为button绑定事件

					@Override
					public void onClick(View v) {
						audioList.remove(position); // 把数据源里面相应数据删除
						ArrayList<String> sKey = Futil.loadKeyArray(
								(Activity) context, "2");
						sKey.remove(key);

						Futil.saveKeyArray((Activity) context, sKey, "2");
						Futil.romveValue((Activity) context, key, "2");// 把缓存里面相应数据删除
						notifyDataSetChanged();
					}
				});
		return convertView;
	}

	class FlingListeber implements GestureDetector.OnGestureListener {

		AudioList item;
		ViewHolder holder;

		public AudioList getItem() {
			return item;
		}

		public void setItem(AudioList item) {
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
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			ActivityJumpControl.getInstance((Activity) context)
					.gotoDetailsPlayActivity(item.getId());
			return false;
		}

	}

	class ViewHolder {
		private LinearLayout history_ll;
		private View sing_line;
		private TextView deletetv;// 用于执行删除的button
		private ImageView history_choice;
		private RoundedImageView history_cover;
		private TextView history_addtime, history_audioname, history_blurb;
		private AudioList item;
	}

}