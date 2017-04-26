package com.zm.hsy.entity;

/**
 * entity实体类 电台
 * 
 * @author Administrator
 * 
 */
public class RadioStation {
	private String name;// 电台名字
	private String mms;// 电台地址
	private String city;// 电台城市

	@Override
	public String toString() {
		return "Person [name=" + name + ", mms=" + mms
				+ ", ctiy=" + city + "]";
	}

	public RadioStation(String city, String name, String mms) {
		this.city = city;
		this.name = name;
		this.mms = mms;
	}

	public RadioStation() {

	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMms() {
		return mms;
	}

	public void setMms(String mms) {
		this.mms = mms;
	}

}
