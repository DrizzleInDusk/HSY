package com.zm.hsy.entity;
/**
* entity实体类
* 分类页上标签
* 
* @author Administrator
*
*/
public class AudioAlbumRecommendList {
	private int id;
	private String recommendName;
	private String managementId;
	private String status; 
	private String albumTypeId;
	private String addTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRecommendName() {
		return recommendName;
	}
	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}
	public String getManagementId() {
		return managementId;
	}
	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAlbumTypeId() {
		return albumTypeId;
	}
	public void setAlbumTypeId(String albumTypeId) {
		this.albumTypeId = albumTypeId;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	
}
