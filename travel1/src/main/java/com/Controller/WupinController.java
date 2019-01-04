package com.Controller;

import com.Bean.*;
import com.Dao.MeishiDao;
import com.Dao.TravelDao;
import com.Dao.WupinDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class WupinController {


    @RequestMapping(value="/Wupin")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> monthJingdianNum(Model model) throws Exception {
        return null;
    }

    @RequestMapping(value="/WupinList")
    public String MeishiList(int currPage,String city1,Model model) throws Exception {

        model.addAttribute("city1",city1);
        System.out.println("WupinList:"+city1);
        model.addAttribute("currPage",currPage);
        return "wupin/wupinall";
    }

    /**
     * 模糊查询物品
     */
    @RequestMapping(value="/WupinSearch")
    @ResponseBody//返回json格式数据
    public PageBean<WupinBean> WupinSearch(String wupin, int currPage) throws Exception {
        WupinDao dao = new WupinDao();
        List<WupinBean> wupins = dao.WupinSearch(wupin, currPage);
        Integer num = dao.WupinSearchNum(wupin);
        PageBean<WupinBean> meishiNums = new PageBean<>(wupins,currPage,10,num);
        return meishiNums;
    }

    //MeishiCityAll
    @RequestMapping(value="/WupinCityAll")
    @ResponseBody//返回json格式数据
    public PageBean<WupinBean> WupinCityAll(String city1,int currPage,Model model) throws Exception {

        WupinDao dao = new WupinDao();
        List<WupinBean> wupins = new ArrayList<>();
        int num = 0;
        if(city1.equals("all")){//查询所有的美食
            wupins = dao.WupinList(currPage);
            num = dao.WupinNum();
        }else{
            wupins = dao.WupinCityList(city1,currPage);
            num = dao.WupinCityNum(city1);
        }
        PageBean<WupinBean> pageBean = new PageBean<>(wupins,currPage,10,num);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("city1",city1);
        model.addAttribute("currPage",currPage);

        return pageBean;
    }

    /**
     * 根据id查找美食的详细信息
     * 再找到关于该美食的游记
     */
    @RequestMapping(value="/getWupinByid")
    public String getWupinByid(Integer id,Model model,int currPage) throws Exception {
        //顺便查询关于该景点的所有游记
        System.out.println("根据ID获得美食");
        TravelDao travelDao = new TravelDao();

        WupinDao wupinDao = new WupinDao();
        WupinBean wupin = WupinDao.GetWupinById(id);
        String city1 = wupin.getCity1();
        String wu = wupin.getWupin();
        System.out.println("city1:"+city1);

        //根据city1和meishi 找到关于该meishi的游记
        WupinBean travelId = wupinDao.travelId(wu,city1);
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
        model.addAttribute("wupin",wupin);
        model.addAttribute("id",id);

        return "wupin/wupinlist";
    }

    /**
     *根据美食id,找到该美食的所有的照片
     */
    @RequestMapping(value="/WupinPhotosByid")
    public String WupinPhotosByid(int id,Model model) throws Exception {
        System.out.println("WupinPhotosByid");
        WupinDao wupinDao = new WupinDao();
        WupinBean wupinBean = wupinDao.GetWupinById(id);
        model.addAttribute("photos",wupinBean.getPhotos());

        model.addAttribute("wupin",wupinBean);

        return "wupin/wupinPhotots";
    }

    /**
     * 跳转到关于景点的地图中
     */
    @RequestMapping(value="/WupinMap")
    public String JingdianMap() throws Exception {
        System.out.println("/WupinMap");
        return "wupin/wupinMap";
    }
}
