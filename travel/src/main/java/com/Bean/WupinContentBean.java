package com.Bean;

public class WupinContentBean {
    private int id;
    private String wupin;
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

    public String getwupin() {
        return wupin;
    }

    public void setwupin(String wupin) {
        this.wupin = wupin;
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

    public WupinContentBean(String wupin, String city1, String content, int travelid, int month) {
        this.wupin = wupin;
        this.city1 = city1;
        this.content = content;
        this.travelid = travelid;
        this.month = month;
    }
    public WupinContentBean(int id,String content){
        this.id = id;
        this.content = content;
    }
}
