package com.zm.hsy.entity;

/**
 * entity实体类 社区
 * 
 * @author Administrator
 * 
 */
public class Community {
	private String addTime;//创建时间
	private String banner;//标语
	private String blurb;//简介
	private String cover;//封面
	private String id;//
	private String name;//名字
	private String status;//状态
	private String uid;//
	private String memCount;//人员数
	private String topicCount;//帖子数
	
	public String getMemCount() {
		return memCount;
	}
	public void setMemCount(String memCount) {
		this.memCount = memCount;
	}
	public String getTopicCount() {
		return topicCount;
	}
	public void setTopicCount(String topicCount) {
		this.topicCount = topicCount;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}
	public String getBlurb() {
		return blurb;
	}
	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

}
