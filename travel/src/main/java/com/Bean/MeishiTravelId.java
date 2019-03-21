package com.Bean;

public class MeishiTravelId {
    private int id;
    private String meishi;
    private String city1;
    private String travelids;

    public MeishiTravelId(int id, String meishi, String city1, String travelids) {
        this.id = id;
        this.meishi = meishi;
        this.city1 = city1;
        this.travelids = travelids;
    }

    public MeishiTravelId(String meishi, String city1, String travelids) {
        this.meishi = meishi;
        this.city1 = city1;
        this.travelids = travelids;
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

    public String getTravelids() {
        return travelids;
    }

    public void setTravelids(String travelids) {
        this.travelids = travelids;
    }
}
