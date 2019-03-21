package com.Bean;

public class MeishiBean {

    private int id;
    private String meishi;
    private String city1;
    private String city;
    private String province;
    private int num;
    private String travelids;
    private String photos;

    private String lng;
    private String lat;
    private boolean visit;

    public MeishiBean(String city, String lng, String lat, boolean visit) {
        this.city = city;
        this.lng = lng;
        this.lat = lat;
        this.visit = visit;
    }

    public boolean isVisit() {
        return visit;
    }

    public void setVisit(boolean visit) {
        this.visit = visit;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

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

    public String getMeishi() {
        return meishi;
    }

    public void setMeishi(String meishi) {
        this.meishi = meishi;
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

    /*public MeishiBean(int id, String meishi, String city1, String city, int num) {
        this.id = id;
        this.meishi = meishi;
        this.city1 = city1;
        this.city = city;
        this.num = num;
    }*/
    public MeishiBean(int id, String meishi, String city1, int num, String photos) {
        this.id = id;
        this.meishi = meishi;
        this.city1 = city1;
        this.num = num;
        this.photos = photos;
    }
    public MeishiBean(int id, String meishi, String city1, int num, String photos,boolean visit) {
        this.id = id;
        this.meishi = meishi;
        this.city1 = city1;
        this.num = num;
        this.photos = photos;
        this.visit = visit;
    }

    public MeishiBean(int id, String meishi, String city1, int num) {
        this.id = id;
        this.meishi = meishi;
        this.city1 = city1;
        this.num = num;
    }
    public MeishiBean(int id, String meishi, String city1, String province,int num) {
        this.id = id;
        this.meishi = meishi;
        this.city1 = city1;
        this.num = num;
        this.province = province;
    }

    public MeishiBean(int id, String meishi, String city1,String city, String province,int num) {
        this.id = id;
        this.meishi = meishi;
        this.city1 = city1;
        this.city = city;
        this.num = num;
        this.province = province;
    }


    public MeishiBean(int id, String meishi, String city1, String travelids) {
        this.id = id;
        this.meishi = meishi;
        this.city1 = city1;
        this.travelids = travelids;
    }

    public MeishiBean(String city1) {
        this.city1 = city1;
    }
}
