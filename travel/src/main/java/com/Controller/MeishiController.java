package com.Controller;

import com.Bean.*;
import com.Dao.JingdianDao;
import com.Dao.MeishiDao;
import com.Dao.ProvinceDao;
import com.Dao.TravelDao;
import com.JingdianUtil.JingdianTest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utils.LuxianGuihua;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
public class MeishiController {


    @RequestMapping(value="/Meishi")
    public String Meishi(Model model,String meishi) throws Exception {
        System.out.println("/Meishi");
        model.addAttribute("meishi",meishi);
        return "meishi/meishiall";
    }

    @RequestMapping(value="/MeishiList")
    public String MeishiList(int currPage,String city1,Model model) throws Exception {

        model.addAttribute("city1",city1);
        System.out.println("MeishiList:"+city1);
        model.addAttribute("currPage",currPage);
        return "meishi/meishiall";
    }

    /**
     * 模糊查询景点
     */
    @RequestMapping(value="/MeishiSearch")
    @ResponseBody//返回json格式数据
    public PageBean<MeishiBean> MeishiSearch(String meishi,int currPage) throws Exception {
        MeishiDao dao = new MeishiDao();
        List<MeishiBean> meishis = dao.MeishiSearch(meishi, currPage);
        Integer num = dao.MeishiSearchNum(meishi);
        PageBean<MeishiBean> meishiNums = new PageBean<>(meishis,currPage,10,num);
        return meishiNums;
    }

    //MeishiCityAll
    @RequestMapping(value="/MeishiCityAll")
    @ResponseBody//返回json格式数据
    public PageBean<MeishiBean> MeishiCityAll(String city1,int currPage,Model model) throws Exception {

        MeishiDao dao = new MeishiDao();
        List<MeishiBean> meishis = new ArrayList<>();
        int num = 0;
        if(city1.equals("all")){//查询所有的美食
            meishis = dao.MeishiList(currPage);
            num = dao.MeishiNum();
        }else{
            meishis = dao.MeishiCityList(city1,currPage);
            num = dao.MeishiCityNum(city1);
        }
        PageBean<MeishiBean> pageBean = new PageBean<>(meishis,currPage,10,num);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("city1",city1);
        model.addAttribute("currPage",currPage);

        return pageBean;
    }

    /**
     * 根据id查找美食的详细信息
     * 再找到关于该美食的游记
     */
    @RequestMapping(value="/getMeishiByid")
    public String getMeishiByid(Integer id,Model model,int currPage) throws Exception {
        //顺便查询关于该景点的所有游记
        System.out.println("/getMeishiByid  根据ID获得美食");
        TravelDao travelDao = new TravelDao();

        MeishiDao meishiDao = new MeishiDao();
        MeishiBean meishi = meishiDao.GetMeishiById(id);
        String city1 = meishi.getCity1();
        String mei = meishi.getMeishi();
        System.out.println("city1:"+city1);

        //根据city1和meishi 找到关于该meishi的游记
        MeishiBean travelId = meishiDao.travelId(mei,city1);

        int num1 = 0;
        List<TravelBean1> travels = new ArrayList<>();
        if(travelId != null){
            String[] travelIds = travelId.getTravelids().split(",");
            List<String> ids = Arrays.asList(travelIds);//把数组转成集合

            int zongye1 = 1;
            num1 = travelIds.length;//总数
            if(num1 % 10 == 0){//整页
                zongye1 = num1 / 10;
            }else{
                zongye1 = num1 / 10 + 1;
            }
            List<String> id10 = new ArrayList<>();
            if(currPage == zongye1){
                id10 = ids.subList((currPage-1)*10,num1);
            }else{
                id10 = ids.subList((currPage-1)*10,currPage*10);
            }
            for(String idd : id10){
                travels.add(travelDao.searchTravelById(idd));
            }
        }
        //System.out.println("Travels1"+travels.toString());
        PageBean<TravelBean1> bean = new PageBean<>(travels,currPage,10,num1);
        model.addAttribute("pageBean",bean);
        model.addAttribute("meishi",meishi);
        model.addAttribute("id",id);

        return "Meishi/meishilist";
    }

    /**
     *根据美食id,找到该美食的所有的照片
     */
    @RequestMapping(value="/MeishiPhotosByid")
    public String MeishiPhotosByid(int id,Model model) throws Exception {
        System.out.println("MeishiPhotosByid");
        MeishiDao meishiDao = new MeishiDao();
        MeishiBean meishiBean = meishiDao.GetMeishiById(id);
        model.addAttribute("photos",meishiBean.getPhotos());

        model.addAttribute("meishi",meishiBean);

        return "meishi/meishiPhotots";
    }

    @RequestMapping(value="/MeishiMap")
    public String MeishiMap(){
        System.out.println("/MeishiMap");
        return "Meishi/meishiMap";
    }

    /**
     * 查询前十的美食
     */
    @RequestMapping(value="/MeishiTop")
    @ResponseBody//返回json格式数据
    public List<MeishiBean> MeishiTop(String city,String month) throws Exception {
        System.out.println("/MeishiTop  city:"+city+"  month:"+month);

        ProvinceDao dao = new ProvinceDao();
        MeishiDao meishiDao = new MeishiDao();
        //根据季节查询 全国前10的美食
        List<MeishiBean> meishiBeans = meishiDao.SeasonMeishiTop();
        List<MeishiBean> meishiList = new ArrayList<>();
        for(MeishiBean meishi : meishiBeans){
            String city1 = meishi.getCity1();
            //根据city1找到城市，和经纬度
            ProvinceBean provicne = dao.getProvinceByCity1(city1);
            if(provicne != null){
                meishi.setLng(provicne.getLng());
                meishi.setLat(provicne.getLat());
                meishiList.add(meishi);
            }
        }
        LuxianGuihua util = new LuxianGuihua();
        //存放排好序的景点
        List<MeishiBean> list = new ArrayList<>();

        //根据普利姆 算法规划路线
        if(city != null && (!city.equals(""))){//城市为空，查询全国的前10景点
            String result = JingdianTest.search(city,"中国");
            System.out.println("城市："+result);
            JSONObject json = JSON.parseObject(result);
            JSONArray results = json.getJSONArray("results");
            MeishiBean qidian = null;
            if(null != results && results.size() > 0){
                JSONObject item = results.getJSONObject(0);
                String name = item.get("name").toString();
                if(item.getJSONObject("location") != null){
                    String lng = item.getJSONObject("location").getString("lng");
                    String lat = item.getJSONObject("location").getString("lat");
                    qidian = new MeishiBean(city,lng,lat,true);
                    list.add(qidian);
                    util.ShortPointMeishi(qidian,meishiList,list);
                }
            }
        }
        //if(city.equals("")){ //城市为空，从该点为起点,规划路线
        else{
            MeishiBean qidian = meishiList.get(0);
            qidian.setVisit(false);
            util.ShortPointMeishi(qidian,meishiList,list);
        }
        System.out.println(list);

        return meishiBeans;
    }

    /**
     * 美食数量最多的城市
     */
    @RequestMapping(value="/CityMeishiTop")
    @ResponseBody//返回json格式数据
    public List<ProvinceBean> CityMeishiTop() throws Exception {
        MeishiDao meishiDao = new MeishiDao();
        List<MeishiBean> meishiBeans = meishiDao.CityMeishiTop();
        List<ProvinceBean> pros = new ArrayList<>();
        for(MeishiBean bean : meishiBeans){
            //根据拼音找到每个城市
            ProvinceBean provinceBean = meishiDao.getCityByCity1(bean.getCity1());
            pros.add(provinceBean);
        }
        return pros;
    }

    @RequestMapping(value="/ProMeishiTop")
    @ResponseBody//返回json格式数据
    public HashMap<String,List<MeishiBean>> ProMeishiTop() throws Exception {
        System.out.println("/ProMeishiTop ");
        //获得所有的省
        TravelDao dao = new TravelDao();
        List<String> provinces = dao.getProvinces();
        MeishiDao meishiDao = new MeishiDao();
        HashMap<String,List<MeishiBean>> meishis = new HashMap<>();
        for(String pro: provinces) {
            List<MeishiBean> meishiBeans = meishiDao.TopMeishi(pro);
            meishis.put(pro,meishiBeans);
        }
        //System.out.println(meishis.get("台湾省"));
        return meishis;
    }

    /**
     * 查询每个省下边的每个市的美食
     */
    @RequestMapping(value="/MeishiTopCity")
    @ResponseBody//返回json格式数据
    public HashMap<String,List<MeishiBean>> MeishiTopCity(@RequestParam(value = "cities[]") String[] cities) throws Exception {
        System.out.println("/MeishiTopCity ");

        MeishiDao meishiDao = new MeishiDao();
        HashMap<String,List<MeishiBean>> meishis = new HashMap<>();
        for(String city: cities) {
            //查询每个城市的前3美食
            List<MeishiBean> meishiBeans = meishiDao.MeishiTopCity(city);
            if(meishiBeans != null && meishiBeans.size()>0)
                meishis.put(city,meishiBeans);
        }
        //System.out.println("第一个城市的第一美食："+meishis.get(0).get(0).getMeishi());
        return meishis;
    }

    /**
     * 查询每个城市的美食频度总数
     */
    @RequestMapping(value="/MeishiCityNum")
    @ResponseBody//返回json格式数据
    public HashMap<String,List<MeishiBean>> MeishiCityNum(@RequestParam(value = "cities[]") String[] cities) throws Exception {
        System.out.println("/MeishiCityNum ");

        return null;
    }

}
