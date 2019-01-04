package com.Bean;

public class JingdianNum {

	private int id;
	private String jingdian;
	private String name;
	private String province;
	private String city;
	private String city1;

	private boolean visit;
	private  String photo;
	private  String photo3;
	private  String photo6;
	private  String photo9;
	private  String photo12;
	private int num3;
	private int num6;
	private int num9;
	private int num12;
	private String area;
	private String address;
	private String lng;
	private String lat;
	private int num;
	private Double score;
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getCity1() {
		return city1;
	}

	public void setCity1(String city1) {
		this.city1 = city1;
	}

	public boolean isVisit() {return visit;}
	public void setVisit(boolean visit) {
		this.visit = visit;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getPhoto6() {
		return photo6;
	}

	public void setPhoto6(String photo6) {
		this.photo6 = photo6;
	}

	public String getPhoto9() {
		return photo9;
	}

	public void setPhoto9(String photo9) {
		this.photo9 = photo9;
	}

	public String getPhoto12() {
		return photo12;
	}

	public void setPhoto12(String photo12) {
		this.photo12 = photo12;
	}

	public int getNum3() {
		return num3;
	}

	public void setNum3(int num3) {
		this.num3 = num3;
	}

	public int getNum6() {
		return num6;
	}

	public void setNum6(int num6) {
		this.num6 = num6;
	}

	public int getNum9() {
		return num9;
	}

	public void setNum9(int num9) {
		this.num9 = num9;
	}

	public int getNum12() {
		return num12;
	}

	public void setNum12(int num12) {
		this.num12 = num12;
	}

	public JingdianNum(int id, String jingdian, String name, String province, String city, String area, String address,
					   String lng, String lat, int num, Double score) {
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
		this.num = num;
		this.score = score;
	}
	
	//获取
	public JingdianNum(String jingdian, String name, String province, String city, String area, String address,
			String lng, String lat, int num, Double score) {
		super();
		this.jingdian = jingdian;
		this.name = name;
		this.province = province;
		this.city = city;
		this.area = area;
		this.address = address;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
		this.score = score;
	}
	
	//添加，不包括满意度分数
	public JingdianNum(String jingdian, String name, String province, String city, String area, String address,
			String lng, String lat, int num) {
		super();
		this.jingdian = jingdian;
		this.name = name;
		this.province = province;
		this.city = city;
		this.area = area;
		this.address = address;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
	}

	public JingdianNum(String jingdian, String name, String province, String city, String city1,String area,
					   int num, String lng, String lat ) {
		super();
		this.jingdian = jingdian;
		this.name = name;
		this.province = province;
		this.city = city;
		this.area = area;
		this.city1 = city1;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
	}

	public JingdianNum(String jingdian, String name, String lng, String lat, int num) {
		super();
		this.jingdian = jingdian;
		this.name = name;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
	}
	public JingdianNum() {}


	@Override
	public String toString() {
		return "JingdianNum{" +
				"jingdian='" + jingdian + '\'' +
				", name='" + name + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", city1='" + city1 + '\'' +
				", area='" + area + '\'' +
				", lng='" + lng + '\'' +
				", lat='" + lat + '\'' +
				", num=" + num +
				'}';
	}

	public JingdianNum(String jingdian, String lng, String lat, int num) {
		this.jingdian = jingdian;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
	}

	public JingdianNum(String jingdian, String lng, String lat, int num,boolean visit) {
		this.jingdian = jingdian;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
		this.visit = visit;
	}
	public JingdianNum(String jingdian, String lng, String lat, int num,boolean visit,String photo) {
		this.jingdian = jingdian;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
		this.visit = visit;
		this.photo = photo;
	}
	public JingdianNum(int id,String jingdian, String lng, String lat, int num,boolean visit,String photo) {
		this.id = id;
		this.jingdian = jingdian;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
		this.visit = visit;
		this.photo = photo;
	}
	public JingdianNum(int id,String jingdian, String city1,String lng, String lat, int num,boolean visit,String photo) {
		this.id = id;
		this.jingdian = jingdian;
		this.city1 = city1;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
		this.visit = visit;
		this.photo = photo;
	}

	public JingdianNum(int id,String jingdian, String city1,String lng, String lat, int num,boolean visit,String photo,String photo3,String photo6,String photo9,String photo12) {
		this.id = id;
		this.jingdian = jingdian;
		this.city1 = city1;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
		this.visit = visit;
		this.photo = photo;
		this.photo3 = photo3;
		this.photo6 = photo6;
		this.photo9 = photo9;
		this.photo12 = photo12;
	}

	public JingdianNum(int id, String jingdian, String lng, String lat, int num) {
		this.id = id;
		this.jingdian = jingdian;
		this.lng = lng;
		this.lat = lat;
		this.num = num;
	}
	public JingdianNum(String jingdian, String lng, String lat,boolean visit) {
		this.jingdian = jingdian;
		this.lng = lng;
		this.lat = lat;
		this.visit = visit;
	}

	public JingdianNum(int id, String jingdian, String city1) {
		this.id = id;
		this.jingdian = jingdian;
		this.city1 = city1;
	}
	public JingdianNum(int id,String jingdian,String city1,String photo3,String photo6,String photo9,String photo12) {
		this.id = id;
		this.jingdian = jingdian;
		this.city1 = city1;
		this.photo3 = photo3;
		this.photo6 = photo6;
		this.photo9 = photo9;
		this.photo12 = photo12;
	}

	public JingdianNum(int id,String jingdian,String city1,String city,int num,int num3,int num6,int num9,int num12,String photo3,String photo6,String photo9,String photo12) {
		this.id = id;
		this.jingdian = jingdian;
		this.city1 = city1;
		this.city = city;
		this.photo3 = photo3;
		this.photo6 = photo6;
		this.photo9 = photo9;
		this.photo12 = photo12;
		this.num = num;
		this.num3 = num3;
		this.num6 = num6;
		this.num9 = num9;
		this.num12 = num12;

	}

	public JingdianNum(int id, String photo) {
		this.id = id;
		this.photo = photo;
	}
	public JingdianNum(int id) {
		this.id = id;
	}


}
