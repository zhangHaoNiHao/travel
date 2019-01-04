package com.Controller;

import com.Bean.*;
import com.Dao.JingdianDao;
import com.Dao.MeishiDao;
import com.Dao.TravelDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MeishiController {


    @RequestMapping(value="/Meishi")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> monthJingdianNum(Model model) throws Exception {
        /**
         * 1. 查找出3-6月份，每个城市的游记
         * 2. 给每一篇游记分词，找出每个景点出现的频率
         * 3. 或者现将每一篇游记进行分析，找到其中每一篇游记出现的频率
         * id jingdian num 游记的城市 游记的月份
         */
        return null;
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
        System.out.println("根据ID获得美食");
        TravelDao travelDao = new TravelDao();

        MeishiDao meishiDao = new MeishiDao();
        MeishiBean meishi = meishiDao.GetMeishiById(id);
        String city1 = meishi.getCity1();
        String mei = meishi.getMeishi();
        System.out.println("city1:"+city1);

        //根据city1和meishi 找到关于该meishi的游记
        MeishiBean travelId = meishiDao.travelId(mei,city1);
        String[] travelIds = travelId.getTravelids().split(",");
        List<String> ids = Arrays.asList(travelIds);//把数组转成集合

        int zongye1 = 1;
        int num1 = travelIds.length;//总数
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
        List<TravelBean1> travels = new ArrayList<>();

        for(String idd : id10){
            travels.add(travelDao.searchTravelById(idd));
        }

        System.out.println("Travels1"+travels.toString());
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

}
