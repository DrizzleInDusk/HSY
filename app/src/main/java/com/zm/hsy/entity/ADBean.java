package com.zm.hsy.entity;

import android.widget.ImageView;

/**
 * entity实体类 轮播图
 * 
 * @author Administrator
 * 
 */
public class ADBean {

	private String id;
	private String adName;// 广告词
	private String imgUrl;// 网络图片资源
	private ImageView mImageView;
	private String link;// 跳转链接

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public ImageView getmImageView() {
		return mImageView;
	}

	public void setmImageView(ImageView mImageView) {
		this.mImageView = mImageView;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
