package com.Bean;

public class WupinBean {

    private int id;
    private String wupin;
    private String city1;
    private String city;
    private int num;
    private String travelids;
    private String photos;

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWupin() {
        return wupin;
    }

    public void setWupin(String wupin) {
        this.wupin = wupin;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTravelids() {
        return travelids;
    }

    public void setTravelids(String travelids) {
        this.travelids = travelids;
    }

    public WupinBean(int id, String wupin, String city1, String city, int num) {
        this.id = id;
        this.wupin = wupin;
        this.city1 = city1;
        this.city = city;
        this.num = num;
    }
    public WupinBean(int id, String wupin, String city1, int num, String photos) {
        this.id = id;
        this.wupin = wupin;
        this.city1 = city1;
        this.city = city;
        this.num = num;
        this.photos = photos;
    }
    public WupinBean(int id, String wupin, String city1, int num) {
        this.id = id;
        this.wupin = wupin;
        this.city1 = city1;
        this.num = num;
    }

    public WupinBean(int id, String wupin, String city1, String travelids) {
        this.id = id;
        this.wupin = wupin;
        this.city1 = city1;
        this.travelids = travelids;
    }

    public WupinBean(String city1) {
        this.city1 = city1;
    }
    public WupinBean(String city1,int num) {
        this.city1 = city1;
        this.num = num;
    }

    public WupinBean(int id, String wupin, int num, String city1, String photos) {
        this.id = id;
        this.wupin = wupin;
        this.num = num;
        this.city1 = city1;
        this.photos = photos;
    }
}
