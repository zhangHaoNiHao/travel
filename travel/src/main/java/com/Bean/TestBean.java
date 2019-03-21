package com.Bean;

import java.util.List;

public class TestBean {
    private int num1;
    private int num2;
    private String city1;
    private List<String> meishis;
    private List<String> jingdians;
    private List<String> wupins;

    public List<String> getMeishis() {
        return meishis;
    }

    public void setMeishis(List<String> meishis) {
        this.meishis = meishis;
    }

    public List<String> getJingdians() {
        return jingdians;
    }

    public void setJingdians(List<String> jingdians) {
        this.jingdians = jingdians;
    }

    public List<String> getWupins() {
        return wupins;
    }

    public void setWupins(List<String> wupins) {
        this.wupins = wupins;
    }

    public TestBean(List<String> jingdians, List<String> meishis, List<String> wupins) {
        this.meishis = meishis;
        this.jingdians = jingdians;
        this.wupins = wupins;
    }

    public TestBean(int num1, int num2, String city1) {
        this.num1 = num1;
        this.num2 = num2;
        this.city1 = city1;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }


    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }
}
