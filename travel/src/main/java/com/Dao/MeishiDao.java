package com.Dao;

import com.Bean.*;
import com.utils.JDBCUtil1;
import com.utils.RowMap;
import org.junit.Test;

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

    /**
     * 根据城市和景点，修改该美食对应的游记id
     */
    public static boolean UpdatetravelId(MeishiBean travelId) throws Exception {
        String sql = "update meishitravelid set travelids=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,travelId.getTravelids(),travelId.getId());
        if(a>0)
            return true;
        else
            return false;
    }

    /**
     * 查询全国前10美食
     */
    public List<MeishiBean> SeasonMeishiTop() throws Exception {
        String sql = "select * from meishinum order by num desc limit 10";
        List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean meishi = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),rs.getString("city1"),rs.getInt("num"),rs.getString("photos"),false);
                return meishi;
            }
        },null);
        return meishis;
    }

    // select count(*) num1,city1 from meishinum GROUP BY city1 ORDER BY num1 desc limit 10

    /**
     * 根据季节查找美食最多的10个城市
     */
    public static List<MeishiBean> CityMeishiTop() throws Exception {
        String sql = "select count(*) num1,city1 from meishinum GROUP BY city1 ORDER BY num1 desc limit 10";
        List<MeishiBean> meishiBeans = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean bean = new MeishiBean(rs.getString("city1"));
                return bean;
            }
        });
        return meishiBeans;
    }

    /**
     * 根据城市拼音找到汉字，如果有重复的，按照游记数量多的
     */
    public static ProvinceBean getCityByCity1(String city1) throws Exception {
        String sql = "select * from province where city1=? order by num desc";
        List<ProvinceBean> provinceBeans = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
            public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                ProvinceBean bean = new ProvinceBean(rs.getString("city"),rs.getString("city1"),rs.getInt("num"));
                return bean;
            }
        },city1);
        return provinceBeans.get(0);
    }

    /**
     * 查询每个省前5的美食
     * @return
     */
    public static List<MeishiBean> TopMeishi(String pro) throws Exception {
        //1.找到所有的省
        pro = pro +"%";
        String sql = "select * from meishinum where province like ? order by num desc limit 5";
        List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean meishi = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),rs.getString("city1"),rs.getString("province"),rs.getInt("num"));
                return meishi;
            }
        },pro);
        return meishis;
    }

    /**
     * 查询每个市的前3美食
     * 汉字城市
     */
    public static List<MeishiBean> MeishiTopCity(String city) throws Exception {
        TravelDao dao = new TravelDao();
        //将汉字转为拼音
        String city1 = dao.CityPinyin(city);
        System.out.println("city："+city+" city1"+city1);
        if(city != null && !city.equals("")){
            String sql = "select * from meishinum where city1=? order by num desc limit 3";
            List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
                public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                    MeishiBean meishi = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),rs.getString("city1"),city,rs.getInt("num"));
                    return meishi;
                }
            },city1);
            return meishis;
        }else{
            return null;
        }
    }





    @Test
    public void test222() throws Exception {
        System.out.println(TopMeishi("台湾省"));
    }


    //添加美食表中城市的省份
    @Test
    public void UpdateMeishiProvince() throws Exception {
        List<MeishiBean> meishis = allMeishi();
        for(MeishiBean bean : meishis){
            String pro = getProvince(bean.getCity1());
            if(pro != null)
                System.out.println(updatePro(pro,bean));
        }
    }

    //查询所有的美食
    public static List<MeishiBean> allMeishi() throws Exception {
        String sql = "select * from meishinum";
        List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean meishi = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),rs.getString("city1"),rs.getInt("num"));
                return meishi;
            }
        },null);
        return meishis;
    }
    //根据城市，在jingdiannum1中查找province
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
    public static boolean updatePro(String pro,MeishiBean bean){
        String sql = "update meishinum set province=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,pro,bean.getId());
        if(a>0)
            return true;
        else
            return false;
    }

    //根据城市和美食查找美食


    public static MeishiBean getMeishi(String city1,String meishi) throws Exception {
        String sql = "select * from meishinum where city1=? and meishi=?";
        List<MeishiBean> meishis = JDBCUtil1.executeQuery(sql, new RowMap<MeishiBean>() {
            public MeishiBean rowMapping(ResultSet rs) throws SQLException {
                MeishiBean meishiBean = new MeishiBean(rs.getInt("id"),rs.getString("meishi"),rs.getString("city1"),rs.getString("photos"));
                return meishiBean;
            }
        },city1,meishi);
        if(meishis.size() > 0)
            return meishis.get(0);
        else
            return null;
    }

    //修改美食图片
    public static boolean UpdateMeishiPhotos(MeishiBean meishi){
        String sql = "update meishinum set photos=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,meishi.getPhotos(),meishi.getId());
        boolean f = false;
        if(a>0)
            f= true;
        return f;
    }

    public static boolean insertMeishi(String meishi,String city1,String travelids){
        String sql = "insert into meishitravelid(meishi,city1,travelids) values(?,?,?)";
        int a = JDBCUtil1.executeUpdate(sql, meishi, city1, travelids);
        boolean f = false;
        if(a>0)
            f = true;
        return f;
    }

    //先查询，如果没有，就插入，否则修改
    //保存美食数量
    public static boolean saveMeishiNum(String meishi,String city1,int num){
        String sql = "insert into meishinum(meishi,city1,num) values(?,?,?)";
        int a = JDBCUtil1.executeUpdate(sql,meishi,city1,num);
        boolean f = false;
        if(a > 0){
            f = true;
        }
        return f;
    }
    //修改
    public static boolean UpdateMeishiNum(String meishi,String city1,int num){
        String sql = "update meishinum set num=num+? where meishi=? and city1=?";
        int a = JDBCUtil1.executeUpdate(sql,meishi,city1,num);
        boolean f = false;
        if(a > 0){
            f = true;
        }
        return f;
    }
}
