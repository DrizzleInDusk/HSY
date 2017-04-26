package com.zm.hsy.myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void scrollTo(int x, int y) {
		if (true) {
			super.scrollTo(x, y);
		}
	}

	/**
	 * (首先调用) 拦截点击事件一般在这个方法里面就行了
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
		// return false;
	}

	/**
	 * (最后调用)
	 */
	// 触摸没有反应就可以了
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// return super.onTouchEvent(event);
		return false;
	}

	/**
	 * (其次调用) (这个在viewgroup中也行)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		// return super.onInterceptTouchEvent(event);
		return false;
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item);
	}
}
