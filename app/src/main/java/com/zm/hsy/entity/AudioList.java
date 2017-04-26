package com.zm.hsy.entity;
/**
* entity实体类
* 音频详情
* 
* @author Administrator
*
*/
public class AudioList {
	private String  id;//音频id
	private String  addTime;//添加时间
	private String  audioName;//音频名称
	private String  blurb;//音频-简介
	private String  commentNumber;//评论数量
	private String  playAmount;//播放数量
	private String  timeLong;//音频时长
	private String  cover;//封面图
	private String  path;//音频路径
	private String  audioAlbumId;//所属专辑id
	private String  status;//专辑-状态  //1上线2下线3待审核
	private String  nickname;//
	private String  count;//
	private String  uid;//
	private String  key;//
	private double filesSize;//
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public double getFilesSize() {
		return filesSize;
	}
	public void setFilesSize(double filesSize) {
		this.filesSize = filesSize;
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
	public String getAudioAlbumId() {
		return audioAlbumId;
	}
	public void setAudioAlbumId(String audioAlbumId) {
		this.audioAlbumId = audioAlbumId;
	}
	public String getAudioName() {
		return audioName;
	}
	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}
	public String getBlurb() {
		return blurb;
	}
	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}
	public String getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(String commentNumber) {
		this.commentNumber = commentNumber;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPlayAmount() {
		return playAmount;
	}
	public void setPlayAmount(String playAmount) {
		this.playAmount = playAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTimeLong() {
		return timeLong;
	}
	public void setTimeLong(String timeLong) {
		this.timeLong = timeLong;
	}
	
	
	
}
