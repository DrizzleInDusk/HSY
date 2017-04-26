package com.zm.hsy.entity;

/**
 * entity实体类
 * 二级分类
 * id 分类ID
 * parentId所属上级级分类id
 * typeName分类名称
 * @author Administrator
 * 
 */
public class AlbumType {
	private String id;
	private String managementId;
	private String parentId; // =1是一级分类
	private String typeName;
	private String aorv;
	private String addTime;

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAorv() {
		return aorv;
	}

	public void setAorv(String aorv) {
		this.aorv = aorv;
	}

	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
