package com.Bean;

public class JingdianBean {

	private Integer id;
	private String jingdian;
	private String name;
	private String province;
	private String city;
	private String area;
	private String address;
	private String lng;
	private String lat;
	private String description;
	private float score;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getJingdian() {
		return jingdian;
	}
	public void setJingdian(String jingdian) {
		this.jingdian = jingdian;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	//��ѯ��ȡ
	public JingdianBean(Integer id, String jingdian, String name, String province, String city, String area, String address,
			String lng, String lat, String description, float score) {
		super();
		this.id = id;
		this.jingdian = jingdian;
		this.name = name;
		this.province = province;
		this.city = city;
		this.area = area;
		this.address = address;
		this.lng = lng;
		this.lat = lat;
		this.description = description;
		this.score = score;
	}
	
	//���
	public JingdianBean(String jingdian, String name, String province, String city, String area, String address,
			String lng, String lat, String description) {
		super();
		this.jingdian = jingdian;
		this.name = name;
		this.province = province;
		this.city = city;
		this.area = area;
		this.address = address;
		this.lng = lng;
		this.lat = lat;
		this.description = description;
	}
	public JingdianBean() {}
	@Override
	public String toString() {
		return "jingdian [id=" + id + ", jingdian=" + jingdian + ", name=" + name + ", province=" + province + ", city="
				+ city + ", area=" + area + ", address=" + address + ", lng=" + lng + ", lat=" + lat + ", description="
				+ description + ", score=" + score + "]";
	}
	
	
	
}
