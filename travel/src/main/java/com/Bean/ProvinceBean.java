package com.Bean;

/**
 * 每个省的游记数量
 */
public class ProvinceBean {

    private int  id;
    private String province;
    private String city;
    private String city1;
    private int num;//城市总的游记数
    private int haonum; //好的游记总数
    private int haonum1;
    private int haonum2;
    private int haonum3;
    private int haonum4;
    private int normalnum;
    private int normalnum1;
    private int normalnum2;
    private int normalnum3;
    private int normalnum4;
    private int badnum;
    private int badnum1;
    private int badnum2;
    private int badnum3;
    private int badnum4;

    private String lng;
    private String lat;


    public ProvinceBean(int id, String city, String city1, String lng, String lat) {
        this.id = id;
        this.city = city;
        this.city1 = city1;
        this.lng = lng;
        this.lat = lat;
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

    public ProvinceBean(int id, String city1) {
        this.id = id;
        this.city1 = city1;
    }
    public ProvinceBean(String city, int num) {
        this.city = city;
        this.num = num;
    }
    public ProvinceBean(String city,String city1, int num) {
        this.city = city;
        this.city1 = city1;
        this.num = num;
    }

    public ProvinceBean(int id,String province, String city) {
        this.city = city;
        this.id = id;
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getHaonum() {
        return haonum;
    }

    public void setHaonum(int haonum) {
        this.haonum = haonum;
    }

    public int getHaonum1() {
        return haonum1;
    }

    public void setHaonum1(int haonum1) {
        this.haonum1 = haonum1;
    }

    public int getHaonum2() {
        return haonum2;
    }

    public void setHaonum2(int haonum2) {
        this.haonum2 = haonum2;
    }

    public int getHaonum3() {
        return haonum3;
    }

    public void setHaonum3(int haonum3) {
        this.haonum3 = haonum3;
    }

    public int getHaonum4() {
        return haonum4;
    }

    public void setHaonum4(int haonum4) {
        this.haonum4 = haonum4;
    }

    public int getNormalnum() {
        return normalnum;
    }

    public void setNormalnum(int normalnum) {
        this.normalnum = normalnum;
    }

    public int getNormalnum1() {
        return normalnum1;
    }

    public void setNormalnum1(int normalnum1) {
        this.normalnum1 = normalnum1;
    }

    public int getNormalnum2() {
        return normalnum2;
    }

    public void setNormalnum2(int normalnum2) {
        this.normalnum2 = normalnum2;
    }

    public int getNormalnum3() {
        return normalnum3;
    }

    public void setNormalnum3(int normalnum3) {
        this.normalnum3 = normalnum3;
    }

    public int getNormalnum4() {
        return normalnum4;
    }

    public void setNormalnum4(int normalnum4) {
        this.normalnum4 = normalnum4;
    }

    public int getBadnum() {
        return badnum;
    }

    public void setBadnum(int badnum) {
        this.badnum = badnum;
    }

    public int getBadnum1() {
        return badnum1;
    }

    public void setBadnum1(int badnum1) {
        this.badnum1 = badnum1;
    }

    public int getBadnum2() {
        return badnum2;
    }

    public void setBadnum2(int badnum2) {
        this.badnum2 = badnum2;
    }

    public int getBadnum3() {
        return badnum3;
    }

    public void setBadnum3(int badnum3) {
        this.badnum3 = badnum3;
    }

    public int getBadnum4() {
        return badnum4;
    }

    public void setBadnum4(int badnum4) {
        this.badnum4 = badnum4;
    }

    public ProvinceBean(int id, String province, String city, String city1, int num, int haonum, int haonum1, int haonum2, int haonum3, int haonum4, int normalnum, int normalnum1, int normalnum2, int normalnum3, int normalnum4, int badnum, int badnum1, int badnum2, int badnum3, int badnum4) {
        this.id = id;
        this.province = province;
        this.city = city;
        this.city1 = city1;
        this.num = num;
        this.haonum = haonum;
        this.haonum1 = haonum1;
        this.haonum2 = haonum2;
        this.haonum3 = haonum3;
        this.haonum4 = haonum4;
        this.normalnum = normalnum;
        this.normalnum1 = normalnum1;
        this.normalnum2 = normalnum2;
        this.normalnum3 = normalnum3;
        this.normalnum4 = normalnum4;
        this.badnum = badnum;
        this.badnum1 = badnum1;
        this.badnum2 = badnum2;
        this.badnum3 = badnum3;
        this.badnum4 = badnum4;
    }

    public ProvinceBean(int id, int haonum, int haonum1, int haonum2, int haonum3, int haonum4, int normalnum, int normalnum1, int normalnum2, int normalnum3, int normalnum4, int badnum, int badnum1, int badnum2, int badnum3, int badnum4) {
        this.id = id;
        this.haonum = haonum;
        this.haonum1 = haonum1;
        this.haonum2 = haonum2;
        this.haonum3 = haonum3;
        this.haonum4 = haonum4;
        this.normalnum = normalnum;
        this.normalnum1 = normalnum1;
        this.normalnum2 = normalnum2;
        this.normalnum3 = normalnum3;
        this.normalnum4 = normalnum4;
        this.badnum = badnum;
        this.badnum1 = badnum1;
        this.badnum2 = badnum2;
        this.badnum3 = badnum3;
        this.badnum4 = badnum4;
    }


    public ProvinceBean(String province, int num,int haonum, int haonum1, int haonum2, int haonum3, int haonum4, int normalnum, int normalnum1, int normalnum2, int normalnum3, int normalnum4, int badnum, int badnum1, int badnum2, int badnum3, int badnum4) {
        this.province = province;
        this.num = num;
        this.haonum = haonum;
        this.haonum1 = haonum1;
        this.haonum2 = haonum2;
        this.haonum3 = haonum3;
        this.haonum4 = haonum4;
        this.normalnum = normalnum;
        this.normalnum1 = normalnum1;
        this.normalnum2 = normalnum2;
        this.normalnum3 = normalnum3;
        this.normalnum4 = normalnum4;
        this.badnum = badnum;
        this.badnum1 = badnum1;
        this.badnum2 = badnum2;
        this.badnum3 = badnum3;
        this.badnum4 = badnum4;
    }

    public ProvinceBean(int id, String province, String city, String city1, int num, int haonum, int haonum1, int haonum2, int haonum3, int haonum4, int normalnum, int normalnum1, int normalnum2, int normalnum3, int normalnum4, int badnum, int badnum1, int badnum2, int badnum3, int badnum4, String lng, String lat) {
        this.id = id;
        this.province = province;
        this.city = city;
        this.city1 = city1;
        this.num = num;
        this.haonum = haonum;
        this.haonum1 = haonum1;
        this.haonum2 = haonum2;
        this.haonum3 = haonum3;
        this.haonum4 = haonum4;
        this.normalnum = normalnum;
        this.normalnum1 = normalnum1;
        this.normalnum2 = normalnum2;
        this.normalnum3 = normalnum3;
        this.normalnum4 = normalnum4;
        this.badnum = badnum;
        this.badnum1 = badnum1;
        this.badnum2 = badnum2;
        this.badnum3 = badnum3;
        this.badnum4 = badnum4;
        this.lng = lng;
        this.lat = lat;
    }

    public ProvinceBean(int id, String city, String city1, int num) {
        this.id = id;
        this.city = city;
        this.city1 = city1;
        this.num = num;
    }
    public ProvinceBean(int id, String province, String city, String city1) {
        this.id = id;
        this.province = province;
        this.city = city;
        this.city1 = city1;
    }
}
