package com.Controller;

import com.Bean.*;
import com.Dao.JingdianContentDao;
import com.Dao.JingdianDao;
import com.Dao.TravelDao;
import com.JingdianUtil.JingdianTest;
import com.Service.JingdianService;
import com.Service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utils.LuxianGuihua;
import org.apache.commons.collections.list.PredicatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class JingdianController {

    @Autowired //自动注入
    private UserService userService;
    private JingdianService jigndianService;

    /**
     * 根据月份，找到全国的游记中，景点出现的频率
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/monthJingdianNum")
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

    /**
     * 查询全国景点频率前10的景点
     */
    @RequestMapping(value="/JingdianTop100")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> JingdianTop100(String month) throws Exception {
        //System.out.println("city:"+city+"  month:"+month);
        JingdianDao jigndiandap = new JingdianDao();
        //根据季节查询 全国前100的景点
        List<JingdianNum> Kindjingdains = jigndiandap.SeasonJingdianTop(month);
        for(JingdianNum j : Kindjingdains){
            Double score = Double.parseDouble(j.getScore());
            DecimalFormat df = new DecimalFormat("0.00");
            String s = df.format(score);
            j.setScore(s);
        }
        System.out.println("按季节查询最热门的百个景点："+Kindjingdains.toString());
        return Kindjingdains;
    }

    /**
     * 查询全国景点频率前10的景点
     */
    //@RequestMapping(value = "/loginCheck",produces="application/json;charset=utf-8",consumes="application/x-www-form-urlencoded")
    //,produces="application/json;charset=utf-8",consumes="application/x-www-form-urlencoded"
    @RequestMapping(value="/JingdianTop")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> JingdianTop(int month) throws Exception {
        //System.out.println("city:"+city+"  month:"+month);
        JingdianDao jigndiandao = new JingdianDao();
        //根据季节查询 全国前10的景点
        List<JingdianNum> Kindjingdains = jigndiandao.JingdianTop10(month);
        System.out.println("长度："+Kindjingdains.size());
        for(JingdianNum j : Kindjingdains){
            Double score = Double.parseDouble(j.getScore());
            DecimalFormat df = new DecimalFormat("0.00");
            String s = df.format(score);
            j.setScore(s);
        }
        System.out.println("按季节查询最热门的十个景点："+Kindjingdains.toString());
        return Kindjingdains;
    }

    @RequestMapping(value="/JingdianTopSSS")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> JingdianTopSSS(int month) throws Exception {
        //System.out.println("city:"+city+"  month:"+month);
        JingdianDao jigndiandao = new JingdianDao();
        //根据季节查询 全国前10的景点
        List<JingdianNum> Kindjingdains = jigndiandao.JingdianTop10(month);
        System.out.println("长度1111："+Kindjingdains.size());
        for(JingdianNum j : Kindjingdains){
            Double score = Double.parseDouble(j.getScore());
            DecimalFormat df = new DecimalFormat("0.00");
            String s = df.format(score);
            j.setScore(s);
        }
        System.out.println("按季节查询最热门的十个景点："+Kindjingdains.toString());
        return Kindjingdains;
    }

    /**
     * 查询全国景点频率前10的景点
     */
    /*@RequestMapping(value="/JingdianTop")
    @ResponseBody//返回json格式数据*/
    /*public List<JingdianNum> JingdianTop(String city,String month) throws Exception {
        System.out.println("city:"+city+"  month:"+month);
        TravelDao dao = new TravelDao();
        JingdianDao jigndiandap = new JingdianDao();
        //全年 全国前10的景点
        //List<JingdianNum> jingdians = dao.JingdianTop();
        //根据季节查询 全国前10的景点
        List<JingdianNum> Kindjingdains = jigndiandap.SeasonJingdianTop(month);
        for(JingdianNum j : Kindjingdains){
            Double score = Double.parseDouble(j.getScore());
            DecimalFormat df = new DecimalFormat("0.00");
            String s = df.format(score);
            j.setScore(s);
        }
        System.out.println("按季节查询最热门的十个景点："+Kindjingdains.toString());
        LuxianGuihua util = new LuxianGuihua();
        //存放排好序的景点
        List<JingdianNum> list = new ArrayList<>();

        //根据普利姆 算法规划路线
        if(city != null && (!city.equals(""))){//城市为空，查询全国的前10景点
            System.out.println("城市为空");
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
            System.out.println("城市不为空");
            JingdianNum qidian = Kindjingdains.get(0);
            qidian.setVisit(false);
            util.ShortPoint(qidian,Kindjingdains,list);
        }
        System.out.println(list);
        return list;
    }*/

    /**
     * 查询每个城市景点频率前10的景点
     */
    @RequestMapping(value="/JingdianTop1")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> JingdianTop1(String city1,String month) throws Exception {
        System.out.println(" /JingdianTop1  city1:"+city1+"  month:"+month);
        JingdianDao jigndiandao = new JingdianDao();
        //根据季节查询 城市前10的景点
        List<JingdianNum> Kindjingdains = jigndiandao.SeasonCityJingdianTop1(city1,month);
        System.out.println("每个城市的景点个数："+Kindjingdains.size());
        for(JingdianNum j : Kindjingdains){
            Double score = Double.parseDouble(j.getScore());
            DecimalFormat df = new DecimalFormat("0.00");
            String s = df.format(score);
            j.setScore(s);
        }
        System.out.println("按季节查询城市最热门的十个景点："+Kindjingdains.toString());
        LuxianGuihua util = new LuxianGuihua();
        //存放排好序的景点
        List<JingdianNum> list = new ArrayList<>();
        JingdianNum qidian = Kindjingdains.get(0);
        qidian.setVisit(false);
        util.ShortPoint(qidian,Kindjingdains,list);
        System.out.println(list);
        return list;
    }

    /**
     * 根据id查找景点的详细信息
     * 再找到关于该景点的游记
     */
    @RequestMapping(value="/getJingdianByid")
    public String Jingdian(Integer id,Model model,int currPage) throws Exception {
        //顺便查询关于该景点的所有游记
        System.out.println("根据ID获得景点");
        TravelDao travelDao = new TravelDao();

        JingdianDao dao = new JingdianDao();
        JingdianNum jingdian = dao.GetJingdainNumById(id);
        System.out.println("6月份图片："+jingdian.getPhoto6());
        String city1 = jingdian.getCity1();
        String jing = jingdian.getJingdian();
        System.out.println("city1:"+city1+"  景点："+jing);

        //根据city1和jingdian 找到关于该景点的游记
        JingdianTravelId travelId = dao.travelId(city1, jing);

        int num1 = 0;
        List<TravelBean1> travels = new ArrayList<>();
        if(travelId != null){
            String[] travelIds = travelId.getTravelids().split(",");
            List<String> ids = Arrays.asList(travelIds);
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
            //查询游记

            for(String idd : id10){
                travels.add(travelDao.searchTravelById(idd));
            }
            System.out.println("Travels1"+travels.toString());
        }else{
            System.out.println("没有该景点的游记");
        }

        PageBean<TravelBean1> bean = new PageBean<>(travels,currPage,10,num1);
        model.addAttribute("pageBean",bean);
        model.addAttribute("jingdian",jingdian);
        model.addAttribute("id",id);

        return "jingdian/jingdianlist";
    }

    /**
     *根据景点id,找到该景点所有的照片
     */
    @RequestMapping(value="/PhotosByid")
    public String PhotosByid(int id,String month,Model model) throws Exception {
        System.out.println("PhotosByid");
        JingdianDao dao = new JingdianDao();
        //根据id获得景点
        JingdianNum jingdian = dao.GetJingdainNumById(id);
        if(month.equals("3")){
            System.out.println("3:"+month);
            model.addAttribute("photos",jingdian.getPhoto3());
        }else if(month.equals("6")){
            System.out.println("6:"+month);
            model.addAttribute("photos",jingdian.getPhoto6());
        }else if(month.equals("9")){
            System.out.println("9:"+month);
            model.addAttribute("photos",jingdian.getPhoto9());
        }else if(month.equals("12")){
            model.addAttribute("photos",jingdian.getPhoto12());
            System.out.println("12:"+month);
        }
        model.addAttribute("month",month);
        model.addAttribute("jingdian",jingdian);
        return "travel/Photots";
    }

    /**
     * 查询景点，查询本城市的所有景点
     */
    @RequestMapping(value="/JingdianCityAll")
    @ResponseBody//返回json格式数据
    public PageBean<JingdianNum> JingdianCityAll(String city1,int currPage) throws Exception {
        System.out.println("/JingdianCityAll 查询本城市的所有景点: "+city1);
        //根据景点，进行模糊查询
        JingdianDao dao = new JingdianDao();
        //根据城市获得该城市的所有景点
        List<JingdianNum> jingdians = null;
        Integer jingdianNum = 0;
        if(city1.equals("all")){
            jingdians = dao.JingdianAll(currPage);
            jingdianNum = dao.JingdianAllNum();
        }else {
            jingdians = dao.JingdianCity(city1,currPage);
            jingdianNum = dao.JingdianCityNum(city1);
        }
        System.out.println("城市："+jingdians.get(0).getCity());
        //获得该城市的景点数量
        PageBean<JingdianNum> jingdianNums = new PageBean<>(jingdians,currPage,10,jingdianNum);
        return jingdianNums;
    }

    /**
     * 从左侧栏直接点景点,找到全部的景点按照数量顺序查找
     */
    @RequestMapping(value="/JingdianList")
    public String JingdianList(String city1,Model model,int currPage) throws Exception {
        JingdianDao dao = new JingdianDao();
        TravelDao travelDao = new TravelDao();

        model.addAttribute("city1",city1);
        model.addAttribute("currPage",currPage);

        //根据城市获得该城市的所有景点 all全国、城市
        //List<JingdianNum> jingdians = dao.JingdianCity(city1,currPage);
        //***如果是所有的景点，则显示关于这些景点的所有信息
        // 所有景点，则显示所有关于这些景点的游记，按照顺序查找
        //获得关于该景点的所有游记   查询景点，获得景点的详细信息，照片，关于该景点的信息，关于该景点的游记
        //List<TravelBean1> travels = travelDao.TravelList(currPage,city1);
        //根据景点，查询游记

        /*int num = 0;
        num = travelDao.cityNum(city1);
        PageBean<TravelBean1> bean = new PageBean<>(travels,currPage,10,num);
        model.addAttribute("pageBean",bean);
        model.addAttribute("jingdian",jingdian);
        model.addAttribute("id",id);*/

        return "jingdian/jingdianall";
    }

    /**
     * 模糊查询景点
     */
    @RequestMapping(value="/JingdianSearch")
    @ResponseBody//返回json格式数据
    public PageBean<JingdianNum> JingdianSearch(String jingdian,Model model,int currPage) throws Exception {
        JingdianDao dao = new JingdianDao();
        //TravelDao travelDao = new TravelDao();
        List<JingdianNum> jingdians = dao.JingdianSearch(jingdian, currPage);
        Integer num = dao.JingdianSearchNum(jingdian);
        //获得该城市的景点数量
        PageBean<JingdianNum> jingdianNums = new PageBean<>(jingdians,currPage,10,num);

        return jingdianNums;
    }

    /**
     * 模糊查询景点
     */
    @RequestMapping(value="/JingdianSearch1")
    @ResponseBody//返回json格式数据
    public TestBean JingdianSearch1(String jingdian) throws Exception {
        JingdianDao dao = new JingdianDao();
        TravelDao travelDao = new TravelDao();
        List<JingdianNum> jingdians = dao.JingdianSearch(jingdian, 1);
        int num = 0;
        if(jingdians != null && jingdians.size()>0){
            num = jingdians.size();
            System.out.println(jingdians.get(0).toString());
        }

        int num2 = 0;
        String city1 = "";
        if(num == 0){
            ProvinceBean provinceBean= travelDao.MoHuCity(jingdian);
            city1 = provinceBean.getCity1();
            if(city1 != null && !city1.equals("")){
                num2 = 1;
            }
        }
        //num 景点的长度，num2城市的长度
        TestBean data = new TestBean(num,num2,city1);
        //查询城市
        return data;
    }

    /**
     * 跳转到关于景点的地图中
     */
    @RequestMapping(value="/JingdianMap")
    public String JingdianMap() throws Exception {
        System.out.println("/JingdianMap");
        return "Jingdian/jingdianMap";
    }

    /**
     * 每个省份前5的景点
     * @return
     */
    @RequestMapping(value="/AllprovinceTop")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> AllprovinceTop(int kind) throws Exception {
        TravelDao dao = new TravelDao();
        System.out.println("/AllprovinceTop  参数kind月份"+kind);
        //获取所有的省，根据省查询每个省的前5条景点

        List<String> provinces = dao.getProvinces();
        List<JingdianNum> jingdianNums = new ArrayList<>();
        for(String pro : provinces){
            jingdianNums.addAll(dao.ProvinceJingdianTop(pro,kind));
        }
        System.out.println("搜索省的前10景点");
        return jingdianNums;
    }
    @RequestMapping(value="/Jingdian")
    public String MapJingdian(String jingdian,Model model){
        System.out.println("/Jingdian: "+jingdian);
        model.addAttribute("jingdian",jingdian);
        return "Jingdian/jingdianall";
    }

    /**
     * 根据景点、城市获得该景点的内容
     */
    @RequestMapping(value="/JingdianContent")
    @ResponseBody//返回json格式数据
    public PageBean<JingdianContentBean> JingdianContent(String jingdian,String city1,int currPage) throws Exception {
        System.out.println("/JingdianContent"+jingdian);
        JingdianContentDao dao = new JingdianContentDao();
        List<JingdianContentBean> jingdianContentBeans = dao.GetjingdianContent(jingdian, city1, currPage);
        int num = dao.GetjingdianContentNum(jingdian,city1);
        System.out.println("景点内容的数量："+num);
        PageBean<JingdianContentBean> pageBean = new PageBean<>(jingdianContentBeans,currPage,10,num);
        return pageBean;
    }

}
