package com.Bean;

public class MeishiContentBean {
    private int id;
    private String meishi;
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

    public String getmeishi() {
        return meishi;
    }

    public void setmeishi(String meishi) {
        this.meishi = meishi;
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

    public MeishiContentBean(String meishi, String city1, String content, int travelid, int month) {
        this.meishi = meishi;
        this.city1 = city1;
        this.content = content;
        this.travelid = travelid;
        this.month = month;
    }
    public MeishiContentBean(int id, String content){
        this.id = id;
        this.content = content;
    }
}
