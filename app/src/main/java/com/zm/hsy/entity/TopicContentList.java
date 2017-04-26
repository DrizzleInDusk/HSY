package com.zm.hsy.entity;

/**
 * entity实体类 评论列表
 * 
 * @author Administrator
 * 
 */
public class TopicContentList {
	private String content;
	private String head;
	private String id;
	private String uid;
	private String name;// 评论人
	private String picture;
	private String time;
	private String yycontent;// 引用回复的内容
	private String yyname;// 引用谁回复
	private String headStatus;// 头像地址标识

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHeadStatus() {
		return headStatus;
	}

	public void setHeadStatus(String headStatus) {
		this.headStatus = headStatus;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
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

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getYycontent() {
		return yycontent;
	}

	public void setYycontent(String yycontent) {
		this.yycontent = yycontent;
	}

	public String getYyname() {
		return yyname;
	}

	public void setYyname(String yyname) {
		this.yyname = yyname;
	}

}
