package com.utils;

import com.Bean.MeishiBean;
import com.Bean.ProvinceBean;
import com.Bean.TravelBean1;
import com.Bean.WupinBean;
import com.Dao.JingdianDao;
import com.Dao.MeishiDao;
import com.Dao.TravelDao;
import com.Dao.WupinDao;
import com.JingdianUtil.WebmagicUpdate;
import com.TravelUtil.TravelUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.webmagic.All_travel3;

import java.io.*;
import java.util.*;

public class Task extends TimerTask {
    /*private Date date;
    public Task(Date date) {
        this.date =date;
    }*/

    @Override
    public void run() {
        //System.out.println("dd");
        //开启爬虫
        Calendar now = Calendar.getInstance();
        System.out.println("年: " + now.get(Calendar.YEAR));
        System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
        System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
        /*年: 2019
          月: 1
          日: 11*/
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        if(month == 1 && day==1){//新一年的1月1日
            year = year - 1;
            month = 12;
            //爬取前一年12月份的游记
        }else{
            //爬取今年的month-1月的游记
            month = month - 1;
        }
        //启动爬虫
        All_travel3.Webmagic(year,month);

        //读取敏感词
        List<String> minggans = new ArrayList<>();
        try {
            File min = new File("D:/data/minganku/1.txt");
            FileInputStream input1 = new FileInputStream(min);
            BufferedReader reader1=new BufferedReader(new InputStreamReader(input1,"utf-8"));
            String mingan  = reader1.readLine();
            while(mingan != null){
                minggans.add(mingan);
                mingan = reader1.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //读取美食
        List<String> meishis = new ArrayList<>();
        try {
            FileInputStream input1 = new FileInputStream(new File("D:/data/ciku/meishi.txt"));
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1, "utf-8"));
            String meishi = reader1.readLine();
            while (meishi != null) {
                meishis.add(meishi);
                meishi = reader1.readLine();
            }
            reader1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //读取物品文件
        List<String> wupins = new ArrayList<>();
        try {
            FileInputStream input = new FileInputStream(new File("D:/data/ciku/wupin.txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8"));
            String wupin = reader.readLine();
            while (wupin != null) {
                wupins.add(wupin);
                wupin = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JingdianDao jingdianDao = new JingdianDao();
        List<String> jingdians = new ArrayList<>();
        //所有的景点
        try {
            jingdians = jingdianDao.AllJingdianName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //分词
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);

        //从数据库找到分数为0或NULL的游记
        TravelDao travelDao = new TravelDao();
        WebmagicUpdate updateTravel = new WebmagicUpdate();
        try {
            //所有的市
            List<ProvinceBean> city1list = travelDao.allCity();
            for(ProvinceBean bean : city1list) {

                //每个城市的食物的数量
                HashMap<String,Integer> meishinum = new HashMap<String,Integer>();
                //每个城市物品的数量
                HashMap<String,Integer> wupinnum = new HashMap<String,Integer>();


                //查询每个城市的游记
                String city1 = bean.getCity1();
                //读取该城市的景点，并且统计该城市的景点数量，加到之前的数据中
                List<String> cityJingdians = new ArrayList<>();
                //1.读取每个城市的景点词库  2.直接读取数据表中的景点
                try {
                    File jingdianFile = new File("D:/data/jingdianAll_G/"+city1+".txt");
                    FileInputStream input9 = new FileInputStream(jingdianFile);
                    BufferedReader reader9 = new BufferedReader(new InputStreamReader(input9, "utf-8"));
                    String jing = reader9.readLine();
                    while (jing != null) {
                        cityJingdians.add(jing);
                        jing = reader9.readLine();
                    }
                    reader9.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                List<TravelBean1> travelBean1s = travelDao.cityTravelsUpdate(city1);
                //一个市的游记
                for(TravelBean1 travel : travelBean1s){
                    //审核敏感词
                    for(String minggan : minggans){
                        if(travel.getContent().contains(minggan)){
                            //如果内容含有敏感词
                            travelDao.deleteTravel(travel.getId());
                            return;
                        }
                    }

                    //提取其中的景点、特产、美食的图片
                    updateTravel.photosMonthUpdate1(city1,travel,jingdians,wupins,meishis);

                    //提取其中的景点、美食、特产的内容
                    updateTravel.Content(segment,travel,jingdians,wupins,meishis);
                    //给内容评分
                    updateTravel.UpdateWupinScore();
                    updateTravel.UpdateMeishiScore();
                    updateTravel.UpdateJingdianScore();
                    //修改景点出现的游记id //顺便记录每个市的美食特产数量
                    updateTravel.TravelId(segment,travel,jingdians,wupins,meishis,meishinum,wupinnum);
                    //将每个城市的美食、物品的数量，存到数据库
                    MeishiDao meishiDao = new MeishiDao();
                    for (Map.Entry<String, Integer> entry : meishinum.entrySet()) {
                        Integer value = entry.getValue();
                        String key = entry.getKey();
                        //存数据库
                        //MeishiBean meishiBean = new MeishiBean(city1,meishi,value);
                        MeishiBean meishiBean = meishiDao.getMeishi(city1, key);
                        if(meishiBean != null){
                            //修改
                            meishiDao.UpdateMeishiNum(meishiBean.getMeishi(),city1,value);
                        }else{
                            meishiDao.saveMeishiNum(key,city1,value);
                        }

                        System.out.println("美食数量："+city1+","+key+","+value);
                    }

                    WupinDao wupinDao = new WupinDao();
                    for (Map.Entry<String, Integer> entry : wupinnum.entrySet()) {
                        Integer value = entry.getValue();
                        String key = entry.getKey();
                        //存数据库
                        WupinBean wupinBean = wupinDao.getWupin(city1, key);
                        if(wupinBean != null){
                            //修改
                            wupinDao.UpdateWupinNum(wupinBean.getWupin(),city1,value);
                        }else{
                            wupinDao.saveWupinNum(key,city1,value);
                        }

                        System.out.println("美食数量："+city1+","+key+","+value);
                    }

                    //先对游记进行评分
                    double score = TravelUtil.articleQinggan(travel.getContent());
                    System.out.println("文章的满意度是："+score);
                    if(score != 0.0)
                        travelDao.addScore(score,travel.getId());
                    //根据评分，修改每个城市的游记数量，各个种类的数量  //最后的时候进行统计，根据haonum1/2/3/4 找到总数，再根据总数，找到市的总数，最后修改省的总数
                    //修改
                    travelDao.updateCityNum(city1,travel.getScore(),month);

                    //提取其中出现过的景点的数量，并修改数量
                    updateTravel.SeasonJingdianNum(segment,cityJingdians,travel);

                }

            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

