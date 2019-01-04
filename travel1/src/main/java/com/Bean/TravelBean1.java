package com.Bean;

public class TravelBean1 {
	private int id;
	private String title;
	private String content;
	private String score;
	private int month;
	private int day;
	private double money;
	private String city;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public TravelBean1(int id, String title, String content, String city, int month, int day, String score) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.city = city;
		this.score = score;
		this.month = month;
		this.day = day;
	}
	public TravelBean1(int id, String title, String city, int month, int day, String score) {
		super();
		this.id = id;
		this.title = title;
		this.city = city;
		this.score = score;
		this.month = month;
		this.day = day;
	}
	@Override
	public String toString() {
		return "TravelBean1 [id=" + id + ", title=" + title +  ", city=" + city + ", month="
				+ month + ", day=" + day + "]";
	}
	public TravelBean1(String title, String content, String city,int month, int day) {
		super();
		this.title = title;
		this.content = content;
		this.month = month;
		this.day = day;
		this.city = city;
		
	}
	public TravelBean1(String title, String content, String city,String score,int month, int day) {
		super();
		this.title = title;
		this.content = content;
		this.month = month;
		this.day = day;
		this.city = city;
		this.score = score;

	}
	public TravelBean1(int id, String title, String content,String city,int month, int day) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.city = city;
		this.month = month;
		this.day = day;
	}
	public TravelBean1(int id, String title, String content) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
	}
	
	

}
