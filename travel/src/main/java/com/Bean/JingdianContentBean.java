package com.Bean;

public class JingdianContentBean {
    private int id;
    private String jingdian;
    private String city1;
    private String content;
    private int travelid;
    private int month;
    private String score;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTravelid() {
        return travelid;
    }

    public void setTravelid(int travelid) {
        this.travelid = travelid;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public JingdianContentBean(String jingdian, String city1, String content, int travelid, int month) {
        this.jingdian = jingdian;
        this.city1 = city1;
        this.content = content;
        this.travelid = travelid;
        this.month = month;
    }

    public JingdianContentBean(int id, String content){
        this.id = id;
        this.content = content;
    }

    public JingdianContentBean(int id, String jingdian, String city1, String content, int travelid, int month, String score) {
        this.id = id;
        this.jingdian = jingdian;
        this.city1 = city1;
        this.content = content;
        this.travelid = travelid;
        this.month = month;
        this.score = score;
    }

    public JingdianContentBean(String jingdian, String city1,String score){
        this.jingdian = jingdian;
        this.city1 = city1;
        this.score = score;
    }
}
