package com.Controller;

import com.Bean.JingdianNum;
import com.Bean.PageBean;
import com.Bean.ProvinceBean;
import com.Bean.TravelBean1;
import com.Dao.TravelDao;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

@Controller
public class TravelController {

    /**
     * 获得每个省好的、差的、一般的游记总数
     * 还有每个季度，这三种游记的数量
     * 每个省份前十的景点
     * @return
     */
    @RequestMapping(value="/provinceNum")
    @ResponseBody//返回json格式数据
    public List<ProvinceBean> provinceNum(Model model) throws Exception {
        TravelDao dao = new TravelDao();
        List<ProvinceBean> provinceBeans = dao.provinceNum();
        return provinceBeans;
    }





    //统计每个省景点热度前十的景点，查询出来
    @RequestMapping(value="/proJingdian")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> provinceJingdian(String pro) throws Exception {
        TravelDao dao = new TravelDao();
        System.out.println("con pro:"+pro);
        List<JingdianNum> jingdians = dao.ProvinceJingdianTop1(pro);
        return jingdians;
    }

    /**
     * 查询每个省的城市的前5个景点
     */
    @RequestMapping(value="/cityJingdian")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> cityJingdian(@RequestParam(value = "cities[]") String[] cities) throws Exception {
        TravelDao dao = new TravelDao();
        //每个城市的前5个景点
        List<JingdianNum> jingdians = dao.CityJingdianTop(cities);
        return jingdians;
    }

    /**
     * 查询每个省的城市的游记数量
     * 如果为0，设置默认值
     */
    @RequestMapping(value="/cityTravel")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> cityTravel(@RequestParam(value = "cities[]") String[] cities) throws Exception {
        TravelDao dao = new TravelDao();
        //每个城市的游记个数(但是有的地区游记个数为0)
        List<JingdianNum> jingdians = dao.CityJingdianTop(cities);
        return jingdians;
    }

    /**
     * 游记数量最多的城市
     */
    @RequestMapping(value="/CityTravelTop")
    @ResponseBody//返回json格式数据
    public List<ProvinceBean> CityTravelTop(String month) throws Exception {
        TravelDao dao = new TravelDao();
        List<ProvinceBean> cities = dao.CityTravelTop(month);
        return cities;
    }
    /**
     * 分页查询每个城市的游记
     */
    @RequestMapping(value="/TravelList")
    public String CityTravel(int currPage,String city1, Model model) throws Exception {
        TravelDao dao = new TravelDao();
        List<TravelBean1> travels = null;
        int num = 0;
        //查询该城市的第一页的游记
        if(city1.equals("all")){
            travels = dao.TravelAllList(currPage);
            num = dao.TravelAllNum();
        }else {
            travels = dao.TravelList(currPage,city1);
            //查询该城市的游记数量
             num = dao.cityNum(city1);
        }
        PageBean<TravelBean1> bean = new PageBean<>(travels,currPage,10,num);
        //根据城市拼音，找到城市
        String city = dao.getCity1ByCity(city1);
        model.addAttribute("pageBean",bean);
        model.addAttribute("city1",city1);
        model.addAttribute("city",city);
        System.out.println("TravelController 城市："+city1+"  汉字"+city);
        return "travel/travelcity";
    }

    /**
     * 分页查询所有的游记
     */
    @RequestMapping(value="/TravelAllList")
    public String TravelAllList(int currPage, Model model) throws Exception {

        System.out.println("/TravelAllList");

        TravelDao dao = new TravelDao();
        //查询该城市的第一页的游记
        List<TravelBean1> travels = dao.TravelAllList(currPage);
        //查询该城市的游记数量
        int num = dao.TravelAllNum();
        PageBean<TravelBean1> bean = new PageBean<>(travels,currPage,10,num);
        model.addAttribute("pageBean",bean);
        //System.out.println("TravelList:分页查询每个城市的所有游记"+city);
        return "travel/travelcity"; //list
    }

    //travelMap
    @RequestMapping(value="/travelMap")
    public String travelMap(){
        System.out.println("/travelMap");
        return "travel/TravelMap";
    }

    //provinceCityNum

    /**
     * 获取省下边的城市的经纬度
     * @return
     */
    @RequestMapping(value="/provinceCity")
    @ResponseBody//返回json格式数据
    public List<ProvinceBean> provinceCityNum(String province) throws Exception {
        TravelDao dao = new TravelDao();
        //获得该省的所有城市，及其信息
        List<ProvinceBean> provinceBeans = dao.ProvinceCity(province);
        return provinceBeans;
    }

    /**
     * 根据城市拼音，找到对应的城市，并转到该城市的列表
     */
    //
    @RequestMapping(value="/CitySearch")
    @ResponseBody//返回json格式数据
    public PageBean<ProvinceBean> CitySearch(String chengshi,int currPage) throws Exception {
        //根据拼音找到对应城市
        TravelDao dao = new TravelDao();
        List<ProvinceBean> provinceBeans = dao.getCityByCity(chengshi, currPage);
        Integer num = dao.getCityByCityNum(chengshi);
        PageBean<ProvinceBean> pageBean = new PageBean<>(provinceBeans,currPage,10,num);
        return pageBean;
    }

    //CityAll
    //查询所有的城市
    @RequestMapping(value="/CityAll")
    @ResponseBody//返回json格式数据
    public List<ProvinceBean> CityAll() throws Exception {
        System.out.println("/CityAll");
        //根据拼音找到对应城市
        TravelDao dao = new TravelDao();
        List<ProvinceBean> provinceBeans = dao.allCityDetail();
        return provinceBeans;
    }
}
