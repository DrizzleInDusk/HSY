package com.zm.hsy.util;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.squareup.picasso.Picasso;
import com.zm.hsy.R;
import com.zm.hsy.adapter.AdBeanAdapter;
import com.zm.hsy.entity.ADBean;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class BeanTu {
	Handler BeanTuhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 0) {
				if (listADbeans.size() > 1) {
					if (currentItem == listADbeans.size()) {
						currentItem = 0;
					}
					mViewPager.setCurrentItem(currentItem, true);
				}

			} else if (what == 1) {
				int position = (int) msg.obj;
				String link = listADbeans.get(position).getLink();
				System.out.println("BeanTu-listADbeans-link---" + link);
			}
		};
	};
	private ViewPager mViewPager;
	private TextView mTextView;
	private LinearLayout mLinearLayout;
	private ArrayList<ADBean> listADbeans=new ArrayList<ADBean>();
	private Context mContext;

	public BeanTu(ViewPager mViewPager, TextView mTextView,
			LinearLayout mLinearLayout, Context mContext,
			ArrayList<ADBean> listbanner) {
		if (this.listADbeans != null) {
			this.listADbeans.clear();
		}
		this.mContext = mContext;
		listADbeans.addAll(listbanner);
		this.mViewPager = mViewPager;
		this.mTextView = mTextView;
		this.mLinearLayout = mLinearLayout;
		initADViewpager();

	}

	private AdBeanAdapter myPagerAdapter;
	private int newPosition = 0;
	private int lastPoint = 0;
	private int currentItem = 0; // 当前图片的索引号
	boolean stat = false;
	private static Timer timer;
	private static TimerTask timerTask;
	private long delay;
	private boolean isRunning = false;

	private void initADViewpager() {
		mLinearLayout.removeAllViews();
		for (int i = 0; i < listADbeans.size(); i++) {
			ImageView imageview = new ImageView(mContext);
			String ImageUrl = listADbeans.get(i).getImgUrl();
			imageview = constructViews(ImageUrl);
			// 把图片添加到列表
			listADbeans.get(i).setmImageView(imageview);
			// 实例化指示点
			ImageView point = new ImageView(mContext);
			point.setImageResource(R.drawable.point_seletor);
			LayoutParams params = new LayoutParams(dip2px(mContext, 6), dip2px(
					mContext, 6));
			params.leftMargin = dip2px(mContext, 10);
			point.setLayoutParams(params);
			// 将指示点添加到线性布局里
			mLinearLayout.addView(point);
			// 设置第一个高亮显示
			if (i == 0) {
				point.setEnabled(true);
			} else {
				point.setEnabled(false);
			}
		}
		// 设置适配器
		myPagerAdapter = new AdBeanAdapter(listADbeans, BeanTuhandler);
		mViewPager.setAdapter(myPagerAdapter);
		// 设置默认文字信息
		if (listADbeans != null && listADbeans.size() > 0 && mTextView != null) {
			mTextView.setText(listADbeans.get(0).getAdName());
		}
		mViewPager.setCurrentItem(0);
		// 设置页面改变监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			/**
			 * 当某个页面被选中的时候回调
			 */
			@Override
			public void onPageSelected(int position) {
				newPosition = position % listADbeans.size();
				// 取出广告文字
				String msg = listADbeans.get(position % listADbeans.size())
						.getAdName();
				if (mTextView != null) {
					mTextView.setText(msg);
				}

				// 设置对应的页面高亮
				mLinearLayout.getChildAt(newPosition).setEnabled(true);
				// 是上次的点不显示高亮
				mLinearLayout.getChildAt(lastPoint).setEnabled(false);

				lastPoint = newPosition;
			}

			/**
			 * 当某个页面被滚动的时候回调
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			/**
			 * 当状态发生改变的时候回调, 静止-滚动-静止
			 */
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == 1) {
					timer.cancel();
					// timerTask.cancel();
					stat = true;
				}
				if (state == 0) {
					if (stat) {
						startViewPager(delay);
					}
					stat = false;
				}

			}
		});
		isRunning = true;
	}

	/**
	 * 开启轮播图
	 * 
	 * @param delay
	 */
	public void startViewPager(long delay) {
		this.delay = delay;
		destroyView();
		timer = new Timer();
		timerTask = new TimerTask() {
			public void run() {
				BeanTuhandler.sendEmptyMessage(0);
				currentItem = (currentItem + 1) % listADbeans.size();
			}
		};
		timer.schedule(timerTask, delay, delay);
	}

	public void destroyView() {
		if (timer != null) {
			timer.cancel();
		}
		if (timerTask != null) {
			timer.cancel();
		}
	}

	/* 加载图片 */
	private ImageView constructViews(String ImageUrl) {
		ImageView imageView = new ImageView(mContext);
		Picasso.with(mContext).load(ImageUrl).into(imageView);
		return imageView;
	}

	/**
	 * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
