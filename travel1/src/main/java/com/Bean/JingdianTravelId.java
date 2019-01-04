package com.Bean;

public class JingdianTravelId {
    private int id;
    private String jingdian;
    private String city1;
    private String travelids;
    private String month3ids;
    private String month6ids;
    private String month9ids;
    private String month12ids;

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

    public JingdianTravelId(int id, String jingdian, String city1) {
        this.id = id;
        this.jingdian = jingdian;
        this.city1 = city1;
    }

    public JingdianTravelId(int id, String jingdian, String city1, String travelids) {
        this.id = id;
        this.jingdian = jingdian;
        this.city1 = city1;
        this.travelids = travelids;
    }
}
