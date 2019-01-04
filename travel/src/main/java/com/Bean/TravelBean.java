package com.Bean;

public class TravelBean {
    private Integer id;
    private String title;
    private String travel;

    public TravelBean(){}


    public TravelBean(Integer id, String title,String travel) {
        this.id = id;
        this.title = title;
        this.travel = travel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public String getTravel() {
        return travel;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    @Override
    public String toString() {
        return "TravelBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", travel='" + travel + '\'' +
                '}';
    }

}
