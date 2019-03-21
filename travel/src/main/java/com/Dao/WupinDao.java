package com.Dao;

import com.Bean.MeishiBean;
import com.Bean.ProvinceBean;
import com.Bean.WupinBean;
import com.utils.JDBCUtil1;
import com.utils.RowMap;
import org.junit.Test;

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

    /**
     * 根据城市和景点，修改该美食对应的游记id
     */
    public static boolean UpdatetravelId(WupinBean travelId) throws Exception {
        String sql = "update wupintravelid set travelids=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,travelId.getTravelids(),travelId.getId());
        if(a>0)
            return true;
        else
            return false;
    }

    /**
     * 查询前10美食
     */
    public List<WupinBean> WupinTop() throws Exception {
        String sql = "select * from wupinnum order by num desc limit 10";
        List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean bean = new WupinBean(rs.getInt("id"),rs.getString("wupin"),
                        rs.getString("city1"),rs.getInt("num"),rs.getString("photos"));
                return bean;
            }
        },null);
        return wupins;
    }
    // select count(*) num1,city1 from meishinum GROUP BY city1 ORDER BY num1 desc limit 10

    /**
     * 根据季节查找游记最多的10个城市
     */
    public static List<WupinBean> CityWupinTop() throws Exception {
        String sql = "select count(*) num1,city1 from wupinnum GROUP BY city1 ORDER BY num1 desc limit 10";
        List<WupinBean> wupinBeans = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean bean = new WupinBean(rs.getString("city1"),rs.getInt("num1"));
                return bean;
            }
        });
        return wupinBeans;
    }



    //添加美食表中城市的省份
    @Test
    public void UpdateMeishiProvince() throws Exception {
        List<WupinBean> wupins = allWupin();
        for(WupinBean bean : wupins){
            String pro = getProvince(bean.getCity1());
            if(pro != null)
                System.out.println(updatePro(pro,bean));
        }
    }

    //查询所有的物品
    public static List<WupinBean> allWupin() throws Exception {
        String sql = "select * from wupinnum";
        List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean wupin = new WupinBean(rs.getInt("id"),rs.getString("wupin"),rs.getString("city1"),rs.getInt("num"));
                return wupin;
            }
        },null);
        return wupins;
    }
    //根据城市，在province中查找province
    public static String getProvince(String city1) throws Exception {
        String sql = "select province from province where city1=?";
        List<String> pros = JDBCUtil1.executeQuery(sql, new RowMap<String>() {
            public String rowMapping(ResultSet rs) throws SQLException {
                String pro = rs.getString("province");
                return pro;
            }
        },city1);
        if(pros.size()>0)
            return pros.get(0);
        else
            return null;
    }
    //修改省份
    public static boolean updatePro(String pro,WupinBean bean){
        String sql = "update wupinnum set province=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,pro,bean.getId());
        if(a>0)
            return true;
        else
            return false;
    }

    //根据城市和物品查找物品
    public static WupinBean getWupin(String city1, String wupin) throws Exception {
        String sql = "select * from wupinnum where city1=? and wupin=?";
        List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean wupinBean = new WupinBean(rs.getInt("id"),rs.getString("wupin"),rs.getInt("num"),rs.getString("city1"),rs.getString("photos"));
                return wupinBean;
            }
        },city1,wupin);
        if(wupins.size()>0)
            return wupins.get(0);
        else
            return null;
    }

    //修改物品图片
    public static boolean UpdateWupinPhotos(WupinBean wupin){
        String sql = "update wupinnum set photos=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,wupin.getPhotos(),wupin.getId());
        boolean f = false;
        if(a>0)
            f= true;
        return f;
    }

    public static boolean saveWupinNum(String wupin,String city1,int num){
        String sql = "insert into wupinnum(wupin,city1,num) values(?,?,?)";
        int a = JDBCUtil1.executeUpdate(sql,wupin,city1,num);
        boolean f = false;
        if(a > 0){
            f = true;
        }
        return f;
    }
    //修改
    public static boolean UpdateWupinNum(String wupin,String city1,int num){
        String sql = "update wupinnum set num=num+? where wupin=? and city1=?";
        int a = JDBCUtil1.executeUpdate(sql,wupin,city1,num);
        boolean f = false;
        if(a > 0){
            f = true;
        }
        return f;
    }

    /**
     * 查询每个省前5的特产
     * @return
     */
    public static List<WupinBean> TopWupin(String pro) throws Exception {
        //1.找到所有的省
        pro = pro +"%";
        String sql = "select * from wupinnum where province like ? order by num desc limit 5";
        List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
            public WupinBean rowMapping(ResultSet rs) throws SQLException {
                WupinBean wupin = new WupinBean(rs.getInt("id"),rs.getString("wupin"),rs.getString("city1"),rs.getString("province"),rs.getInt("num"));
                return wupin;
            }
        },pro);
        return wupins;
    }

    /**
     * 查询每个市的前3美食
     * 汉字城市
     */
    public static List<WupinBean> WupinTopCity(String city) throws Exception {
        TravelDao dao = new TravelDao();
        //将汉字转为拼音
        String city1 = dao.CityPinyin(city);
        System.out.println("city："+city+" city1"+city1);
        if(city != null && !city.equals("")){
            String sql = "select * from wupinnum where city1=? order by num desc limit 3";
            List<WupinBean> wupins = JDBCUtil1.executeQuery(sql, new RowMap<WupinBean>() {
                public WupinBean rowMapping(ResultSet rs) throws SQLException {
                    WupinBean wupin = new WupinBean(rs.getInt("id"),rs.getString("wupin"),rs.getString("city1"),city,rs.getInt("num"));
                    return wupin;
                }
            },city1);
            return wupins;
        }else{
            return null;
        }
    }
}
