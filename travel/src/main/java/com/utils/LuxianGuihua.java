package com.utils;

import com.Bean.JingdianNum;
import com.Bean.MeishiBean;
import com.Dao.TravelDao;
import com.JingdianUtil.JingdianTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LuxianGuihua {

    public void Luxian(String qidian, List<JingdianNum> jingdians){
        String result = JingdianTest.search(qidian,"北京");
        //String lng = "100";
        //String lat = "100";
        String lng = "100";
        String lat = "24";
        for(JingdianNum jingdian : jingdians){
            jingdian.setVisit(false);
        }
        //起点
        JingdianNum qidian1 = new JingdianNum("qidian",lng,lat,true);
        //计算哪个点距离该点最近
        //JingdianNum dangqian = qidian1;
        //普利姆算法，
        //JingdianNum dangqian2 = ShortPoint(dangqian,jingdians);
        List<JingdianNum> list = new ArrayList<>();
        //先加入起点
        /*for(JingdianNum jingdian : jingdians){
            JingdianNum dangqian = ShortPoint(qidian1,jingdians);//先计算距离起点最近的点，设为已访问
            list.add(dangqian);//加入第一个点
            JingdianNum dangqian

        }*/
    }

    public static double discount(String a,String b,String c,String d){
        double _a = Double.parseDouble(a);
        double _b = Double.parseDouble(b);
        double _c = Double.parseDouble(c);
        double _d = Double.parseDouble(d);
        double x = Math.abs(_a-_c);
        Double y = Math.abs(_b-_d);
        return Math.sqrt(x*x+y*y);
    }

    public void ShortPoint(JingdianNum qidian, List<JingdianNum> jingdians,List<JingdianNum> list){
        double min = 10000000;
        int x = 0;
        int a = 0;
        for(int i=0;i<jingdians.size();i++){
            if(!jingdians.get(i).isVisit()){//该景点没被访问
                double discount = discount(qidian.getLng(), qidian.getLat(), jingdians.get(i).getLng(), jingdians.get(i).getLat());
                if(discount < min){
                    min = discount;
                    x = i;
                }
                a++;
            }
        }
        if(a == 0)
            return;
        jingdians.get(x).setVisit(true);//将找到的点设为已访问
        list.add(jingdians.get(x));
        ShortPoint(jingdians.get(x),jingdians,list);
    }
    //美食最短路径
    public void ShortPointMeishi(MeishiBean qidian, List<MeishiBean> meishis, List<MeishiBean> list){
        double min = 10000000;
        int x = 0;
        int a = 0;
        for(int i=0;i<meishis.size();i++){
            if(!meishis.get(i).isVisit()){//该景点没被访问
                double discount = discount(qidian.getLng(), qidian.getLat(), meishis.get(i).getLng(), meishis.get(i).getLat());
                if(discount < min){
                    min = discount;
                    x = i;
                }
                a++;
            }
        }
        if(a == 0)
            return;
        meishis.get(x).setVisit(true);//将找到的点设为已访问
        list.add(meishis.get(x));
        ShortPointMeishi(meishis.get(x),meishis,list);
    }
    @Test
    public void test() throws Exception {
        TravelDao dao = new TravelDao();
        List<JingdianNum> list = new ArrayList<>();
        List<JingdianNum> jingdians = dao.JingdianTop();
        JingdianNum qidian = new JingdianNum("北京","116","39",false);

        ShortPoint(qidian,jingdians,list);
        for(JingdianNum j : list)
            System.out.println(j.getJingdian());
    }

}
