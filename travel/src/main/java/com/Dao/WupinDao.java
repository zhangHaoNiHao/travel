package com.Dao;

import com.Bean.WupinBean;
import com.utils.JDBCUtil1;
import com.utils.RowMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WupinDao {

    /**
     *  获得所有的物品及数量
     */
    public static List<WupinBean> WupinList(int currPage) throws Exception {
        String sql = "select * from wupinnum order by num desc limit ?,10";
        List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean wupin = new WupinBean(rs.getInt("id"),rs.getString("wupin"),rs.getString("city1"),rs.getInt("num"));
                return wupin;
            }
        },(currPage-1)*10);
        return wupins;
    }

    /**
     * 物品的数量
     */
    public static Integer WupinNum() throws Exception {
        String sql = "select count(*) num11 from wupinnum";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer num = rs.getInt("num11");
                return num;
            }
        });
        return nums.get(0);
    }

    /**
     *  获得城市所有的物品及其数量
     */
    public static List<WupinBean> WupinCityList(String city1,int currPage) throws Exception {
        String sql = "select * from wupinnum where city1=? order by num desc limit ?,10";
        List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean wupin = new WupinBean(rs.getInt("id"),rs.getString("wupin"),rs.getString("city1"),rs.getInt("num"));
                return wupin;
            }
        },city1,(currPage-1)*10);
        return wupins;
    }

    /**
     * 城市所有物品的数量
     */
    public static Integer WupinCityNum(String city1) throws Exception {
        String sql = "select count(*) num11 from wupinnum where city1=?";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer num = rs.getInt("num11");
                return num;
            }
        },city1);
        return nums.get(0);
    }

    /**
     * 物品模糊查询
     */
    public static List<WupinBean> WupinSearch(String wupin,int currPage) throws Exception {
        wupin = "%"+wupin+"%";
        String sql = "select * from wupinnum where wupin like ? order by num desc limit ?,10";
        List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean wupin = new WupinBean(rs.getInt("id"),rs.getString("wupin"),rs.getString("city1"),rs.getInt("num"));
                return wupin;
            }
        },wupin,(currPage-1)*10);
        return wupins;
    }
    /**
     * 模糊查询物品的个数
     */
    public static int WupinSearchNum(String wupin) throws Exception {
        wupin = "%"+wupin+"%";
        String sql = "select count(*) num11 from wupinnum where wupin like ?";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer a = rs.getInt("num11");
                return a;
            }
        },wupin);
        return nums.get(0);
    }

    /**
     * 根据物品id查找
     */
    public static WupinBean GetWupinById(int id) throws Exception {
        String sql = "select * from wupinnum where id=?";
        List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean bean = new WupinBean(rs.getInt("id"),rs.getString("wupin"),
                        rs.getString("city1"),rs.getInt("num"),rs.getString("photos"));
                return bean;
            }
        },id);
        return wupins.get(0);
    }

    /**
     * 根据城市和物品，找到wupintravelid表中的游记数量
     * @param wupin
     * @param city1
     * @return
     */
    public static WupinBean travelId(String wupin,String city1) throws Exception {
        String sql = "select * from wupintravelid where wupin=? and city1=?";
        List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean bean = new WupinBean(rs.getInt("id"),rs.getString("wupin"),
                        rs.getString("city1"),rs.getString("travelids"));
                return bean;
            }
        },wupin,city1);
        if(wupins.size()>0)
            return wupins.get(0);
        else
            return null;
    }

}
