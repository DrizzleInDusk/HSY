package com.zm.hsy.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.zm.hsy.entity.ADBean;

public class AdBeanAdapter extends PagerAdapter {
	// 界面列表
	private ArrayList<ADBean> listADbeans;
	private Handler handler;

	public AdBeanAdapter(ArrayList<ADBean> listADbeans, Handler handler) {
		this.listADbeans = listADbeans;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		if (listADbeans != null) {
			return listADbeans.size();
		} else {
			return 0;
		}
	}

	/**
	 * 初始化position位置的界面
	 */
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {

		// 根据位置取出某一个View
		ImageView view = listADbeans.get(position).getmImageView();
		view.setScaleType(ScaleType.FIT_XY);
		((ViewPager) container).addView(view);
		/**
		 * 添加点击事件
		 */
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Message msg1 = new Message();
				msg1.obj = position;
				msg1.what = 1;
				handler.sendMessage(msg1);
			}
		});

		return view;// 返回实例化的View
	}

	/**
	 * 销毁position位置的界面
	 */
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(listADbeans.get(position)
				.getmImageView());
	}

	/**
	 * 判断是否由对象生成界面
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {

	}

}