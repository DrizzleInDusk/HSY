package com.zm.hsy.entity;
/**
* entity实体类
* 活动列表
* @author Administrator
*
*/
public class Variation {
	private String addTime;//
	private String blurb;//简介
	private String conPic;//内容图片
	private String conVideo;//内容视频
	private String content;//内容文字
	private String cost;//费用
	private String id;//
	private String pageviews;//
	private String propaganda;//宣传图
	private String quota;//名额
	private String rule;//规则
	private String status;//活动状态  1审核，2正常上线，可报名，3报名已满，不可报名，4活动结束
	private String title;//标题
	private String publisherId;//创建人id
	private String type;//
	
	public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getBlurb() {
		return blurb;
	}
	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}
	public String getConPic() {
		return conPic;
	}
	public void setConPic(String conPic) {
		this.conPic = conPic;
	}
	public String getConVideo() {
		return conVideo;
	}
	public void setConVideo(String conVideo) {
		this.conVideo = conVideo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPageviews() {
		return pageviews;
	}
	public void setPageviews(String pageviews) {
		this.pageviews = pageviews;
	}
	public String getPropaganda() {
		return propaganda;
	}
	public void setPropaganda(String propaganda) {
		this.propaganda = propaganda;
	}
	public String getQuota() {
		return quota;
	}
	public void setQuota(String quota) {
		this.quota = quota;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
