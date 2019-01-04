package com.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {

    @RequestMapping(value="/index2")
    public String index2() {
        System.out.println("/index2");
        return "index/indexMap2";
    }

    @RequestMapping(value="/echars")
    public String echars() {
        System.out.println("/echars");
        return "Test/EcharsMap";
    }

    @RequestMapping(value="/echars2")
    public String echars2() {
        System.out.println("/echars2");
        return "Test/EcharsMap2";
    }
    @RequestMapping(value="/echars3")
    public String echars3() {
        System.out.println("/echars3");
        return "Test/EcharsMap3";
    }

    @RequestMapping(value="/echars3_1")
    public String echars3_1() {
        System.out.println("/echars3_1");
        return "Test/EcharsMap3_1";
    }

    @RequestMapping(value="/echars4")
    public String echars4() {
        System.out.println("/echars4");
        return "Test/EcharsMap4";
    }

    @RequestMapping(value="/echars4_1")
    public String echars4_1() {
        System.out.println("/echars4_1");
        return "Test/EcharsMap4_1";
    }

    @RequestMapping(value="/echars4_2")
    public String echars4_2() {
        System.out.println("/echars4_2");
        return "Test/EcharsMap4_2";
    }

    @RequestMapping(value="/echars4_3Point")
    public String echars4_3Point() {
        System.out.println("/echars4_3Point");
        return "Test/EcharsMap4_3Point";
    }

    @RequestMapping(value="/indexPoint")
    public String echars4_3Point1() {
        System.out.println("/echars4_3Point1");
        return "Test/EcharsMap4_3Point1";
    }

    @RequestMapping(value="/point2")
    public String echars4_3Point2() {
        System.out.println("/point2");
        return "Test/EcharsMap4_3Point2";
    }


    @RequestMapping(value="/wc")
    public String wc(HttpServletRequest req) throws Exception {
        System.out.println("/wc");
        return "wc";
    }

    @RequestMapping(value="/first")
    public String first(HttpServletRequest req) throws Exception {
        System.out.println("/first");
        return "first";
    }
    @RequestMapping(value="/second")
    public String second(HttpServletRequest req) throws Exception {
        System.out.println("/second");
        return "second";
    }

    @RequestMapping(value="/map")
    public String map(HttpServletRequest req) throws Exception {
        System.out.println("/map");
        return "map/map";
    }

    @RequestMapping(value="/map2")
    public String map2(HttpServletRequest req) throws Exception {
        System.out.println("/map2");
        return "map/map2";
    }

    @RequestMapping(value="/map3")
    public String map3(HttpServletRequest req) throws Exception {
        System.out.println("/map3");
        return "map/map3";
    }
    @RequestMapping(value="/map4")
    public String map4(HttpServletRequest req) throws Exception {
        System.out.println("/map4");
        return "map/map4";
    }
    @RequestMapping(value="/map5")
    public String map5(HttpServletRequest req) throws Exception {
        System.out.println("/map5");
        return "map/map5";
    }
    @RequestMapping(value="/map5_1")
    public String map5_1(HttpServletRequest req) throws Exception {
        System.out.println("/map5_1");
        return "map/map5_1";
    }
    @RequestMapping(value="/map6")
    public String map6(HttpServletRequest req) throws Exception {
        System.out.println("/map6");
        return "map/map6";
    }
    //youjilist
    @RequestMapping(value="/youjilist")
    public String youjilist() {
        System.out.println("/youjilist");
        return "travel/youjilist";
    }

    @RequestMapping(value="/search111")
    public String search1() {
        System.out.println("/search1");
        return "Test/search1";
    }

}
