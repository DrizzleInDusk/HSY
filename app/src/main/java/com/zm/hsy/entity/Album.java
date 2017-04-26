package com.zm.hsy.entity;
/**
* entity实体类
* 专辑
* commendId;//推荐id
* commendName;//推荐名
* @author Administrator
*
*/
public class Album {
	private String id;//专辑ID
	private String cover;//专辑-封面图
	private String albumName;//专辑名
	private String blurb;//专辑-简介
	private String playAmount;//专辑——播放数量
	private String episode;//专辑-多少集
	private String label;//专辑标签
	private String userId;//专辑UserId
	private String nickname;//专辑Username
	private String albumTypeId;//专辑TypeId
	private String status;//专辑-状态  1正常上线
	private String addTime;//专辑添加时间
	
	private String commendId;//推荐id
	private String commendName;//推荐名
	private String rid;//属于首页推荐
	private String rname;//属于首页推荐

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getCommendId() {
		return commendId;
	}
	public void setCommendId(String commendId) {
		this.commendId = commendId;
	}
	public String getCommendName() {
		return commendName;
	}
	public void setCommendName(String commendName) {
		this.commendName = commendName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getBlurb() {
		return blurb;
	}
	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}
	public String getPlayAmount() {
		return playAmount;
	}
	public void setPlayAmount(String playAmount) {
		this.playAmount = playAmount;
	}
	public String getEpisode() {
		return episode;
	}
	public void setEpisode(String episode) {
		this.episode = episode;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getAlbumTypeId() {
		return albumTypeId;
	}
	public void setAlbumTypeId(String albumTypeId) {
		this.albumTypeId = albumTypeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	
	
}
