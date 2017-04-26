package com.zm.hsy.entity;

/**
 * entity实体类 社区
 * 
 * @author Administrator
 * 
 */
public class Topic {
	private String addTime;//
	private String attachment;//
	private String communityId;//
	private String content;//内容
	private String id;//
	private String parentId;//
	private String picture;//
	private String score;//
	private String status;//
	private String title;//标题
	private String topicId;//
	private String uid;//
	private String count;//回复数
	private String head;//发帖人头像
	private String nickname;//发帖人
	private String headStatus;//头像地址标识
	private String name;//头像地址标识
	private String yycontent;//头像地址标识

	public String getYycontent() {
		return yycontent;
	}

	public void setYycontent(String yycontent) {
		this.yycontent = yycontent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadStatus() {
		return headStatus;
	}
	public void setHeadStatus(String headStatus) {
		this.headStatus = headStatus;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
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
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

}
