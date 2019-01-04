package com.Dao;

import com.Bean.MeishiBean;
import com.utils.JDBCUtil1;
import com.utils.RowMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MeishiDao {

    /**
     *  获得所有的美食及数量
     */
    public static List<MeishiBean> MeishiList(int currPage) throws Exception {
        String sql = "select * from meishinum order by num desc limit ?,10";
        List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean meishi = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),rs.getString("city1"),rs.getInt("num"));
                return meishi;
            }
        },(currPage-1)*10);
        return meishis;
    }

    /**
     * 所有美食的数量
     */
    public static Integer MeishiNum() throws Exception {
        String sql = "select count(*) num11 from meishinum";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer num = rs.getInt("num11");
                return num;
            }
        });
        return nums.get(0);
    }

    /**
     *  获得城市所有的美食及其数量
     */
    public static List<MeishiBean> MeishiCityList(String city1,int currPage) throws Exception {
        String sql = "select * from meishinum where city1=? order by num desc limit ?,10";
        List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean meishi = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),rs.getString("city1"),rs.getInt("num"));
                return meishi;
            }
        },city1,(currPage-1)*10);
        return meishis;
    }

    /**
     * 城市所有美食的数量
     */
    public static Integer MeishiCityNum(String city1) throws Exception {
        String sql = "select count(*) num11 from meishinum where city1=?";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer num = rs.getInt("num11");
                return num;
            }
        },city1);
        return nums.get(0);
    }

    /**
     * 美食模糊查询
     */
    public static List<MeishiBean> MeishiSearch(String meishi,int currPage) throws Exception {
        meishi = "%"+meishi+"%";
        String sql = "select * from meishinum where meishi like ? order by num desc limit ?,10";
        List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean meishi = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),rs.getString("city1"),rs.getInt("num"));
                return meishi;
            }
        },meishi,(currPage-1)*10);
        return meishis;
    }
    /**
     * 模糊查询美食的个数
     */
    public static int MeishiSearchNum(String meishi) throws Exception {
        meishi = "%"+meishi+"%";
        String sql = "select count(*) num11 from meishinum where meishi like ?";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer a = rs.getInt("num11");
                return a;
            }
        },meishi);
        return nums.get(0);
    }

    /**
     * 根据美食id查找
     */
    public static MeishiBean GetMeishiById(int id) throws Exception {
        String sql = "select * from meishinum where id=?";
        List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean bean = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),
                        rs.getString("city1"),rs.getInt("num"),rs.getString("photos"));
                return bean;
            }
        },id);
        return meishis.get(0);
    }

    /**
     * 根据城市和美食，找到meishitravelid表中的游记数量
     * @param meishi
     * @param city1
     * @return
     */
    public static MeishiBean travelId(String meishi,String city1) throws Exception {
        String sql = "select * from meishitravelid where meishi=? and city1=?";
        List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean bean = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),
                        rs.getString("city1"),rs.getString("travelids"));
                return bean;
            }
        },meishi,city1);
        if(meishis.size()>0)
            return meishis.get(0);
        else
            return null;
    }

}
