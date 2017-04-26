package com.zm.hsy.entity;
/**
* entity实体类
* 评论列表
* 
* @author Administrator
*
*/
public class ContentList {
	private String addtime;
	private String content;
	private String head;
	private String id;
	private String nickname;//评论人
	private String replyname;//对谁评论
	
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getReplyname() {
		return replyname;
	}
	public void setReplyname(String replyname) {
		this.replyname = replyname;
	}
	
}
