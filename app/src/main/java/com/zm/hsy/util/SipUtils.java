package com.zm.hsy.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class SipUtils {
	/** 加载图片 */
	public static ImageView constructViews(String ImageUrl, Context conntext) {
		ImageView imageView = new ImageView(conntext);
		Picasso.with(conntext).load(ImageUrl).into(imageView);
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
	/**
	 * 截取字符串
	 * @param string 字符
	 * @param end 字符长度
	 * @return String
	 */
	public static String substringL(String string,int end) {
		String cacheL = "";
		int cs = string.length();
		cacheL = string.substring(0, end);
		return cacheL;
	}
	/**
	 * 截取字符串
	 * @param string 字符
	 * @param start 字符长度
	 * @return String
	 */
	public static String substringR(String string,int start) {
		String cacheR = "";
		int cs = string.length();
		cacheR = string.substring(start, cs);
		return cacheR;
	}
}
