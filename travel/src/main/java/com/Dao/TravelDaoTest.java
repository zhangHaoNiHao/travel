package com.Dao;

import com.Bean.ProvinceBean;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TravelDaoTest {

    /**
     * 查询出每个市，好的、一般的、坏的游记的个数
     * 1.获得该省下边的所有城市，拼音
     * 2.根据拼音，获得所有的游记个数
     * 3.查询各个种类的游记的个数
     *
     */
    @Test
    public void travelKindNum() throws SQLException {

        List<String> sqls = new ArrayList<>();
        //每个城市满意度好的游记总数
        String sql1 = "select count(*) num from travel where city=? and score>=0.75 ";
        sqls.add(sql1);
        //每个季度的满意度好的游记个数
        String sql1_1 = "select count(*) num from travel where city=? and score>=0.75 and (month=3 or month=4 or month=5)";
        sqls.add(sql1_1);
        String sql1_2 = "select count(*) num from travel where city=? and score>=0.75 and (month=6 or month=7 or month=8)";
        sqls.add(sql1_2);
        String sql1_3 = "select count(*) num from travel where city=? and score>=0.75 and (month=9 or month=10 or month=11)";
        sqls.add(sql1_3);
        String sql1_4 = "select count(*) num from travel where city=? and score>=0.75 and (month=12 or month=1 or month=2)";
        sqls.add(sql1_4);
        //每个城市一般的游记总数
        String sql2 = "select count(*) num from travel where city=? and score>=0.6 and score<0.75 ";
        sqls.add(sql2);
        String sql2_1 = "select count(*) num from travel where city=? and score>=0.6 and score<0.75 and (month=3 or month=4 or month=5)";
        sqls.add(sql2_1);
        String sql2_2 = "select count(*) num from travel where city=? and score>=0.6 and score<0.75 and (month=6 or month=7 or month=8)";
        sqls.add(sql2_2);
        String sql2_3 = "select count(*) num from travel where city=? and score>=0.6 and score<0.75 and (month=9 or month=10 or month=11)";
        sqls.add(sql2_3);
        String sql2_4 = "select count(*) num from travel where city=? and score>=0.6 and score<0.75 and (month=12 or month=1 or month=2)";
        sqls.add(sql2_4);

        //每个城市体验差的游记总数
        String sql3 = "select count(*) num from travel where city=? and score<0.6";
        sqls.add(sql3);
        String sql3_1 = "select count(*) num from travel where city=? and score<0.6 and (month=3 or month=4 or month=5)";
        sqls.add(sql3_1);
        String sql3_2 = "select count(*) num from travel where city=? and score<0.6 and (month=6 or month=7 or month=8)";
        sqls.add(sql3_2);
        String sql3_3 = "select count(*) num from travel where city=? and score<0.6 and (month=9 or month=10 or month=11)";
        sqls.add(sql3_3);
        String sql3_4 = "select count(*) num from travel where city=? and score<0.6 and (month=12 or month=1 or month=2)";
        sqls.add(sql3_4);
        TravelDao dao = new TravelDao();

        //所有的市
        List<ProvinceBean> city1list = dao.allCity();
        for(ProvinceBean bean : city1list){
            //province中的每个城市拼音
            //每个城市所有的游记数量
            //int num = dao.TravelNum(bean.getCity1());
            String city1 = bean.getCity1();

            int haonum = dao.TravelNum(city1, sql1);
            int haonum1 = dao.TravelNum(city1, sql1_1);
            int haonum2 = dao.TravelNum(city1, sql1_2);
            int haonum3 = dao.TravelNum(city1, sql1_3);
            int haonum4 = dao.TravelNum(city1, sql1_4);

            int normalnum = dao.TravelNum(city1, sql2);
            int normalnum1 = dao.TravelNum(city1, sql2_1);
            int normalnum2 = dao.TravelNum(city1, sql2_2);
            int normalnum3 = dao.TravelNum(city1, sql2_3);
            int normalnum4 = dao.TravelNum(city1, sql2_4);

            int badnum = dao.TravelNum(city1, sql3);
            int badnum1 = dao.TravelNum(city1, sql3_1);
            int badnum2 = dao.TravelNum(city1, sql3_2);
            int badnum3 = dao.TravelNum(city1, sql3_3);
            int badnum4 = dao.TravelNum(city1, sql3_4);

            ProvinceBean provinceBean = new ProvinceBean(bean.getId(),haonum,haonum1,haonum2,haonum3,haonum4,normalnum,normalnum1,normalnum2,normalnum3,normalnum4,badnum,badnum1,badnum2,badnum3,badnum4);

            //保存
            boolean f = dao.saveCityNum(provinceBean);
            if(f){
                System.out.println("添加成功");
            }
        }


    }

    /**
     * 已验证
     * 统计每个省的所有的 好的，坏的、一般的游记的总个数
     * 还有每个季度的三类的总数 static List<ProvinceBean>
     */
    @Test
    public void ProvinceNum() throws SQLException {
        TravelDao dao = new TravelDao();
        //所有的省
        List<String> provinceList = dao.ProvinceList();

        List<ProvinceBean> beans = new ArrayList<>();
        for(String pro : provinceList){
            //每个省，所有的市
            List<ProvinceBean> citites = dao.provinceCity1(pro);
            //整个省，所有的
            int num = 0;
            int haonum = 0; //好的游记数量
            int normalnum =0 ; //一般的游记数量
            int badnum = 0; //差的游记数量
            //每个季度的
            int haonum1 = 0;
            int haonum2 = 0;
            int haonum3 = 0;
            int haonum4 = 0;
            //
            int normalnum1 = 0;
            int normalnum2 = 0;
            int normalnum3 = 0;
            int normalnum4 = 0;
            int badnum1 = 0;
            int badnum2 = 0;
            int badnum3 = 0;
            int badnum4 = 0;
            for(ProvinceBean city : citites){
                num += city.getNum();
                haonum += city.getHaonum();
                haonum1 += city.getHaonum1();
                haonum2 += city.getHaonum2();
                haonum3 += city.getHaonum3();
                haonum4 += city.getHaonum4();

                normalnum += city.getNormalnum();
                normalnum1 += city.getNormalnum1();
                normalnum2 += city.getNormalnum2();
                normalnum3 += city.getNormalnum3();
                normalnum4 += city.getNormalnum4();

                badnum += city.getBadnum();
                badnum1 += city.getBadnum1();
                badnum2 += city.getBadnum2();
                badnum3 += city.getBadnum3();
                badnum4 += city.getBadnum4();
            }
            //每个省，所有的好的游记个数、差的游记个数、一般的游记个数，一季度的好的游记个数、、、、
            ProvinceBean bean1 = new ProvinceBean(pro,num,haonum,haonum1,haonum2,haonum3,haonum4,normalnum,normalnum1,normalnum2,normalnum3,normalnum4,badnum,badnum1,badnum2,badnum3,badnum4);
            beans.add(bean1);
            boolean f = false;
            f = dao.saveProvinceNum(bean1);
            if(f){
                System.out.println("添加成功");
            }
        }
        //return beans;
    }



}
