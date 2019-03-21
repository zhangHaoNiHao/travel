package com.Controller;

import com.Bean.*;
import com.Dao.JingdianDao;
import com.Dao.MeishiDao;
import com.Dao.TravelDao;
import com.Dao.WupinDao;
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
public class WupinController {


    @RequestMapping(value="/Wupin")
    public String Wupin(Model model,String wupin) throws Exception {
        System.out.println("/Wupin");
        model.addAttribute("wupin",wupin);
        return "wupin/wupinall";
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
        System.out.println("根据ID获得特产");
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

    /**
     * 查询前十的物品
     */
    @RequestMapping(value="/WupinTop")
    @ResponseBody//返回json格式数据
    public List<WupinBean> WupinTop(String city,String month) throws Exception {
        System.out.println("/WupinTop  city:"+city+"  month:"+month);

        WupinDao wupinDao = new WupinDao();
        //根据季节查询 全国前10的美食
        List<WupinBean> wupinBeans = wupinDao.WupinTop();

        /*
        List<JingdianNum> Kindjingdains = jigndiandap.SeasonJingdianTop(month);
        System.out.println("按季节查询最热门的十个景点："+Kindjingdains.toString());
        LuxianGuihua util = new LuxianGuihua();
        //存放排好序的景点
        List<JingdianNum> list = new ArrayList<>();

        //根据普利姆 算法规划路线
        if(city != null && (!city.equals(""))){//城市为空，查询全国的前10景点
            String result = JingdianTest.search(city,"中国");
            System.out.println("城市："+result);
            JSONObject json = JSON.parseObject(result);
            JSONArray results = json.getJSONArray("results");
            JingdianNum qidian = null;
            if(null != results && results.size() > 0){
                JSONObject item = results.getJSONObject(0);
                String name = item.get("name").toString();
                if(item.getJSONObject("location") != null){
                    String lng = item.getJSONObject("location").getString("lng");
                    String lat = item.getJSONObject("location").getString("lat");
                    qidian = new JingdianNum(city,lng,lat,true);
                    list.add(qidian);
                    util.ShortPoint(qidian,Kindjingdains,list);
                }
            }
        }else{ //城市不为空，从该点为起点,规划路线
            JingdianNum qidian = Kindjingdains.get(0);
            qidian.setVisit(false);
            util.ShortPoint(qidian,Kindjingdains,list);
        }
        System.out.println(list);
        */
        return wupinBeans;
    }

    /**
     * 美食数量最多的城市
     */
    @RequestMapping(value="/CityWupinTop")
    @ResponseBody//返回json格式数据
    public List<ProvinceBean> CityWupinTop() throws Exception {
        WupinDao wupinDao = new WupinDao();
        MeishiDao meishiDao = new MeishiDao();
        List<WupinBean> wupinBeans = wupinDao.CityWupinTop();
        List<ProvinceBean> pros = new ArrayList<>();
        for(WupinBean bean : wupinBeans){
            //根据拼音找到每个城市
            ProvinceBean provinceBean = meishiDao.getCityByCity1(bean.getCity1());
            provinceBean.setNum(bean.getNum());
            pros.add(provinceBean);
        }
        return pros;
    }

    /**
     * 每个省的特产
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ProWupinTop")
    @ResponseBody//返回json格式数据
    public HashMap<String,List<WupinBean>> ProWupinTop() throws Exception {
        System.out.println("/ProWupinTop ");
        //获得所有的省
        TravelDao dao = new TravelDao();
        List<String> provinces = dao.getProvinces();
        WupinDao wupinDao = new WupinDao();
        HashMap<String,List<WupinBean>> wupins = new HashMap<>();
        for(String pro: provinces) {
            List<WupinBean> wupinBeans = wupinDao.TopWupin(pro);
            wupins.put(pro,wupinBeans);
        }
        //System.out.println(meishis.get("台湾省"));
        return wupins;
    }

    /**
     * 查询每个省下边的每个市的美食
     */
    @RequestMapping(value="/WupinTopCity")
    @ResponseBody//返回json格式数据
    public HashMap<String,List<WupinBean>> WupinTopCity(@RequestParam(value = "cities[]") String[] cities) throws Exception {
        System.out.println("/WupinTopCity ");

        WupinDao wupinDao = new WupinDao();
        HashMap<String,List<WupinBean>> wupins = new HashMap<>();
        for(String city: cities) {
            //查询每个城市的前3美食
            List<WupinBean> wupinBeans = wupinDao.WupinTopCity(city);
            if(wupinBeans != null && wupinBeans.size()>0)
                wupins.put(city,wupinBeans);
        }
        //System.out.println("第一个城市的第一美食："+meishis.get(0).get(0).getMeishi());
        return wupins;
    }

}
