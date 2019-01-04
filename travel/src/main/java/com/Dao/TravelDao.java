package com.Dao;

import com.Bean.JingdianNum;
import com.Bean.ProvinceBean;
import com.Bean.TravelBean1;
import com.utils.JDBCUtil;
import com.utils.JDBCUtil1;
import com.utils.RowMap;
import org.junit.Test;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TravelDao {

    public static void main(String[] args) throws SQLException {
/*      //确定每个地区所属地省
        //System.out.println(City("杭州"));
        String province = City("宜兰");
        if(province == null || "".equals(province)){
            System.out.println("空");
            province = Area("宜");
        }
        System.out.println("省："+province);

        //getProvince();
        */
        //省集合
        //System.out.println(ProvinceList());

        //每个省的游记个数
        //ProvinceTravelNum();

        //统计每个城市的游记个数
        //saveCityNum11();
    }
//begin////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 有的城市的省是错误的，需要重新找到每个城市的省，并进行修改
     * 将每个城市的省份找到，并修改
     */
    public static void getProvince() throws SQLException {
        String sql = "select id,city from province";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        while(rs.next()){
            int id = rs.getInt("id");
            String city = rs.getString("city");
            //根据city找到省，再修改
            String province = City(city);
            if(province == null || "".equals(province)){
                System.out.println("空");
                province = Area(city);
            }
            upProvince(province,id);
        }
        JDBCUtil1.Close(conn,pstmt,rs);

    }
    public static  void upProvince(String province,int id) throws SQLException {
        String sql = "update province set province=? where id=?";
        Connection conn = null;
        conn = JDBCUtil1.getConnection();
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,province);
        pstmt.setInt(2,id);
        int a = pstmt.executeUpdate();
        JDBCUtil1.Close(conn,pstmt,null);
    }

//end////////////////////////////////////////////////////////////////////////////////////////

//begin////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 根据城市名，找到省
     * @param city
     * @return
     * @throws SQLException
     */
    public static String City(String city) throws SQLException {
        city = "%"+city+"%";
        String sql = "select * from cities where city like ?";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        //pstmt.setString(0, city);
        pstmt.setString(1, city);
        rs = pstmt.executeQuery();
        String provinceid = "";
        if(rs.next()){
            provinceid = rs.getString("provinceid");
            //System.out.println("provinceid: "+provinceid);
        }
        String province = Province(provinceid);
        return province;
    }



//begin////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 统计每个省的游记个数
     */
    public static void ProvinceTravelNum() throws SQLException {
        //所有的省
        List<String> provinceList = ProvinceList();
        for(String pro : provinceList){
            //每个省，所有的市
            List<String> citites = provinceCity(pro);
            int num = 0;
            for(String c : citites){
                int a = TravelNum(c);
                num += a;
                //System.out.println("城市："+c+" 数量："+a);
                //城市每个季节的数量
                for(int x=1;x<5;x++){
                    CitySeason(c,x);
                }
            }
            System.out.println("省："+pro+" 数量："+num);
            //将每个省的总数量存到数据库

            //将每个省所有的好的一般的，差的总量放到数据库

            //将每个季节的数量放到数据库

        }
    }

    /**
     * 统计每个城市的游记总数
     * @throws SQLException
     */
    @Test
    public void saveCityNum11() throws SQLException {
        //province表的所有城市
        List<ProvinceBean> city1list = allCity();
        for(ProvinceBean bean : city1list){
            //province中的每个城市拼音
            //统计每个城市的数量，并将它们保存到数据库
            int num = TravelNum(bean.getCity1());
            System.out.println("城市："+bean.getCity1()+"  数量："+num);
            //保存
            boolean f = saveCityNum(bean.getId(),num);
            if(f){
                System.out.println("添加成功");
            }
        }
    }

    /**
     * 根据id,修改城市的游记总个数
     */
    public static boolean saveCityNum(int id,int num) throws SQLException {
        String sql = "update province set num=? where id=?";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,num);
        pstmt.setInt(2,id);
        int a = 0;
        a = pstmt.executeUpdate();
        JDBCUtil1.Close(conn,pstmt,null);
        boolean flag = false;
        if(a > 0)
            flag = true;
        return flag;
    }

    /**
     * 根据id修改城市的三种类型，四个季度的游记个数
     */
    public static boolean saveCityNum(ProvinceBean bean) throws SQLException {
        String sql = "update province set haonum=?,haonum1=?,haonum2=?,haonum3=?,haonum4=?,normalnum=?,normalnum1=?,normalnum2=?,normalnum3=?,normalnum4=?,badnum=?,badnum1=?,badnum2=?,badnum3=?,badnum4=? where id=?";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,bean.getHaonum());
        pstmt.setInt(2,bean.getHaonum1());
        pstmt.setInt(3,bean.getHaonum2());
        pstmt.setInt(4,bean.getHaonum3());
        pstmt.setInt(5,bean.getHaonum4());
        pstmt.setInt(6,bean.getNormalnum());
        pstmt.setInt(7,bean.getNormalnum1());
        pstmt.setInt(8,bean.getNormalnum2());
        pstmt.setInt(9,bean.getNormalnum3());
        pstmt.setInt(10,bean.getNormalnum4());
        pstmt.setInt(11,bean.getBadnum());
        pstmt.setInt(12,bean.getBadnum1());
        pstmt.setInt(13,bean.getBadnum2());
        pstmt.setInt(14,bean.getBadnum3());
        pstmt.setInt(15,bean.getBadnum4());
        pstmt.setInt(16,bean.getId());
        int a = 0;
        a = pstmt.executeUpdate();
        JDBCUtil1.Close(conn,pstmt,null);
        boolean flag = false;
        if(a > 0)
            flag = true;
        return flag;
    }

    /**
     * 查询所有的城市
     */
    public static List<ProvinceBean>  allCity() throws SQLException {
        String sql = "select id,city1 from province";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        List<ProvinceBean> list = new ArrayList<>();

        while(rs.next()){
            int id = rs.getInt("id");
            String city1 = rs.getString("city1");
            ProvinceBean bean = new ProvinceBean(id,city1);
            list.add(bean);
        }
        JDBCUtil1.Close(conn,pstmt,rs);
        return list;
    }

    /**
     * 查询所有的城市,所有信息
     */
    public static List<ProvinceBean>  allCityDetail() throws Exception {
        String sql = "select * from province";
        List<ProvinceBean> list = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
            public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                ProvinceBean bean = new ProvinceBean(rs.getInt("id"),rs.getString("province"),rs.getString("city"),rs.getString("city1"));
                return bean;
            }
        });
        return list;
    }

    /**
     * 获得省下边的市及其信息
     * @param province
     * @return
     */
    public static List<ProvinceBean> ProvinceCity(String province) throws Exception {
        province = province+"%";
        String sql = "select * from province where province like ?";
        List<ProvinceBean> provinceBeans = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
            public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                ProvinceBean bean = new ProvinceBean(rs.getInt("id"),rs.getString("province"),rs.getString("city"),
                        rs.getString("city1"),rs.getInt("num"),rs.getInt("haonum"),rs.getInt("haonum1"),
                        rs.getInt("haonum2"),rs.getInt("haonum3"),rs.getInt("haonum4"),rs.getInt("badnum"),
                        rs.getInt("badnum1"),rs.getInt("badnum2"),rs.getInt("badnum3"),rs.getInt("badnum4"),
                        rs.getInt("normalnum"),rs.getInt("normalnum1"),rs.getInt("normalnum2"),rs.getInt("normalnum3"),
                        rs.getInt("normalnum4"),rs.getString("lng"),rs.getString("lat"));
                return bean;
            }
        },province);
        return provinceBeans;
    }

    /**
     * 修改城市经纬度
     */
    public static boolean  UpdateProvince(ProvinceBean province) throws Exception {
        String sql = "update province set lng=?,lat=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,province.getLng(),province.getLat(),province.getId());
        boolean f = false;
        if(a>0)
            f = true;
        return f;
    }

    /**
     * 根据城市，查询游记个数
     */
    public static int TravelNum(String city1,String sql) throws SQLException {

        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,city1);
        rs = pstmt.executeQuery();
        int num = 0;
        if(rs.next()){
            num = rs.getInt("num");
        }
        JDBCUtil1.Close(conn,pstmt,rs);
        return num;
    }

    /**
     * 根据城市，获得该城市每个季节的游记个数
     */
    public static int CitySeason(String city1 , int i) throws SQLException {
        String sql = "";
        if(i == 1){
            sql = "select count(*) num from travel where city=? and month=3 or month=4 or month=5";
        }else if(i == 2){
            sql = "select count(*) num from travel where city=? and month=6 or month=7 or month=8";
        }else if(i == 3){
            sql = "select count(*) num from travel where city=? and month=9 or month=10 or month=11";
        }else if(i == 4){
            sql = "select count(*) num from travel where city=? and month=12 or month=1 or month=2";
        }
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,city1);
        rs = pstmt.executeQuery();
        int num = 0;
        if(rs.next()){
            num = rs.getInt("num");
        }
        JDBCUtil1.Close(conn,pstmt,rs);
        return num;
    }

    /**
     * 根据省份，找到每个省的城市(拼音)，再根据城市找到每个城市的游记个数
     */
    public static List<String> provinceCity(String province) throws SQLException {
        String sql = "select city1 from province where province=?";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,province);
        rs = pstmt.executeQuery();
        List<String> city1List = new ArrayList<>();
        while(rs.next()){
            city1List.add(rs.getString("city1"));
        }
        JDBCUtil1.Close(conn,pstmt,rs);
        return city1List;
    }

    /**
     * 统计每个省的所有城市的游记数量信息
     * 每个省的城市集合
     */
    public static List<ProvinceBean> provinceCity1(String province) throws SQLException {
        String sql = "select * from province where province=?";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,province);
        rs = pstmt.executeQuery();
        List<ProvinceBean> city1List = new ArrayList<>();
        while(rs.next()){
            int id = rs.getInt("id");
            String city = rs.getString("city");
            String city1 = rs.getString("city1");
            int num = rs.getInt("num");
            int haonum = rs.getInt("haonum");
            int haonum1 = rs.getInt("haonum1");
            int haonum2 = rs.getInt("haonum2");
            int haonum3 = rs.getInt("haonum3");
            int haonum4 = rs.getInt("haonum4");
            int normalnum = rs.getInt("normalnum");
            int normalnum1 = rs.getInt("normalnum1");
            int normalnum2 = rs.getInt("normalnum2");
            int normalnum3 = rs.getInt("normalnum3");
            int normalnum4 = rs.getInt("normalnum4");
            int badnum = rs.getInt("badnum");
            int badnum1 = rs.getInt("badnum1");
            int badnum2 = rs.getInt("badnum2");
            int badnum3 = rs.getInt("badnum3");
            int badnum4 = rs.getInt("badnum4");

            ProvinceBean bean = new ProvinceBean(id,province,city,city1,num,haonum,haonum1,haonum2,haonum3,haonum4,normalnum,normalnum1,normalnum2,normalnum3,normalnum4,badnum,badnum1,badnum2,badnum3,badnum4);
            city1List.add(bean);
        }
        JDBCUtil1.Close(conn,pstmt,rs);
        return city1List;
    }

    /**
     * 查询每个城市的游记总数
     */
    public static int TravelNum(String city1) throws SQLException {
        city1 = "%"+city1+"%";
        String sql = "select count(*) num from travel where city like  ?";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,city1);
        rs = pstmt.executeQuery();
        int a = 0;
        if(rs.next()){
            a = rs.getInt("num");
        }
        JDBCUtil1.Close(conn,pstmt,rs);
        return a;
    }

    /**
     * 从provinces表获取所有的省份
     */
    public static List<String> ProvinceList() throws SQLException {
        String sql = "select province from provinces ";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        List<String> provinceList = new ArrayList<>();
        while(rs.next()){
            provinceList.add(rs.getString("province"));
        }
        JDBCUtil1.Close(conn,pstmt,rs);
        return provinceList;
    }


    /**
     * 根据地区找到省
     */
    public static String Area(String area) throws SQLException {
        area = "%"+area+"%";
        String sql = "select * from areas where area like ?";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        //pstmt.setString(0, city);
        pstmt.setString(1, area);
        rs = pstmt.executeQuery();
        String cityid = "";
        if(rs.next()){
            cityid = rs.getString("cityid");
            System.out.println("cityid: "+cityid);
        }
        String province = Area_cityid(cityid);

        return province;
    }

    /**
     * 根据城市id找到省
     */
    public static String Area_cityid(String cityid) throws SQLException {
        String sql = "select * from cities where cityid=?";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cityid);
        rs = pstmt.executeQuery();
        String provinceid = "";
        if(rs.next()){
            provinceid = rs.getString("provinceid");
            System.out.println("provinceid: "+provinceid);
        }
        String province = Province(provinceid);

        return province;
    }

    /**
     * 根据省id，找到省名
     */
    public static String Province(String provinceid) throws SQLException{
        String sql = "select * from provinces where provinceid=?";
        Connection conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, provinceid);
        rs = pstmt.executeQuery();
        String province = "";
        while(rs.next()){
            province = rs.getString("province");
        }
        return province;
    }
////////////////////////////////////////////////////////////////////////////////////
    /**
     * 查找travel表中没有打分的游记
     */
    public static List<TravelBean1> TravelList() throws Exception {
        String sql = "select * from travel where score is NULL";
        List<TravelBean1> list = JDBCUtil1.executeQuery(sql, new RowMap<TravelBean1>(){
            public TravelBean1 rowMapping(ResultSet rs) {
                TravelBean1 u = null;
                try {
                    u = new TravelBean1(rs.getInt("id"),rs.getString("title"),rs.getString("content"),rs.getString("city"),rs.getInt("month"),rs.getInt("day"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return u;
            }
        }, null);
        return list;
    }
    /**
     * 修改每个游记的情感值
     * @param score
     */
    public static Boolean addScore(double score, int id) {
        Boolean flag = false;
        String sql = "update travel set score=? where id=?";
        int a = 0;
        String score1 = ""+score;
        System.out.println("插入数据库的数据："+score1);
        a = JDBCUtil1.executeUpdate(sql, score1,id);
        if(a > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 将每个省的总游记数，三个类的总游记数，每个季度的三个类的总游记数保存到数据库
     * 修改每个省的的总的游记数
     */
    public boolean saveProvinceNum(ProvinceBean bean) {
        //将每个省的总游记数，三个类的总游记数，每个季度的三个类的总游记数保存到数据库
        //String sql = "insert into provincenum(province,num,haonum,haonum1,haonum2,haonum3,haonum4,normalnum,normalnum1,normalnum2,normalnum3,normalnum4,badnum,badnum1,badnum2,badnum3,badnum4) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sql1 = "update provincenum set num=? where province=?";
        int a = 0;
        boolean f = false;
        //a = JDBCUtil1.executeUpdate(sql,bean.getNum(),bean.getHaonum(),bean.getHaonum1(),bean.getHaonum2(),bean.getHaonum3(),bean.getHaonum4(),bean.getNormalnum(),bean.getNormalnum1(),bean.getNormalnum2(),bean.getNormalnum3(),bean.getNormalnum4(),bean.getBadnum(),bean.getBadnum1(),bean.getBadnum2(),bean.getBadnum3(),bean.getBadnum4());
        a = JDBCUtil1.executeUpdate(sql1,bean.getNum(),bean.getProvince());
        if(a>0)
            f = true;
        return f;
    }

    /**
     * 获取每个省的所有的 好的，坏的、一般的游记的总个数
     * 还有每个季度的三类的总数 static List<ProvinceBean>
     * @return
     */
    public List<ProvinceBean> provinceNum() throws Exception {
        String sql = "select * from provincenum";
        List<ProvinceBean> list = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
            public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                ProvinceBean bean = null;
                bean = new ProvinceBean(rs.getString("province"),rs.getInt("num"),rs.getInt("haonum"),
                        rs.getInt("haonum1"),rs.getInt("haonum2"),rs.getInt("haonum3"),
                        rs.getInt("haonum4"),rs.getInt("normalnum"),rs.getInt("normalnum1"),
                        rs.getInt("normalnum2"),rs.getInt("normalnum3"),rs.getInt("normalnum4"),
                        rs.getInt("badnum"),rs.getInt("badnum1"),rs.getInt("badnum2"),
                        rs.getInt("badnum3"),rs.getInt("badnum4"));
                return bean;
            }
        },null);
        return list;
    }


    /**
     *  统计每个省景点热度前十的景点，查询出来
     */
    public static List<JingdianNum> ProvinceJingdianTop1(String pro) throws Exception {
        String pro1 = pro+"%";
        String sql = "select * from jingdiannum1 where province like ? order by num desc limit 5";
        List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
            public JingdianNum rowMapping(ResultSet rs) throws SQLException {
                JingdianNum jingdian = null;
                //jingdian、lng、lat、num、
                jingdian = new JingdianNum(rs.getString("jingdian"),rs.getString("lng"),rs.getString("lat"),rs.getInt("num"));
                return jingdian;
            }
        },pro1);
        System.out.println("省："+pro);
        if(jingdians.size() > 0)
            return jingdians;
        else{
            System.out.println("没有该省的景点");
            return null;
        }

    }

    /**
     *  统计每个省景点热度前十的景点，查询出来
     */
    public static List<JingdianNum> ProvinceJingdianTop(String pro,int kind) throws Exception {
        String pro1 = pro+"%";
        String sql = "";
        if(kind == 0){
            sql = "select * from jingdiannum1 where province like ? order by num desc limit 5";
        }else if(kind == 1){
            sql = "select * from jingdiannum1 where province like ? order by num3 desc limit 5";
        }else if(kind == 2){
            sql = "select * from jingdiannum1 where province like ? order by num6 desc limit 5";
        }else if(kind == 3){
            sql = "select * from jingdiannum1 where province like ? order by num9 desc limit 5";
        }else if(kind == 4){
            sql = "select * from jingdiannum1 where province like ? order by num12 desc limit 5";
        }

        List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
            public JingdianNum rowMapping(ResultSet rs) throws SQLException {
                JingdianNum jingdian = null;
                //jingdian、lng、lat、num、
                jingdian = new JingdianNum(rs.getString("jingdian"),rs.getString("lng"),rs.getString("lat"),rs.getInt("num"));
                return jingdian;
            }
        },pro1);
        System.out.println("省："+pro);
        if(jingdians.size() > 0)
            return jingdians;
        else{
            System.out.println("没有该省的景点");
            return null;
        }

    }

    /**
     *  统计每个市景点热度前5的景点，查询出来
     */
    public static List<JingdianNum> CityJingdianTop(String[] cities) throws Exception {
        List<JingdianNum> cityjingdians= new ArrayList<>();
        for(String city : cities){
            if(city.contains("市"))
                city = city.replace("市","");
            if(city.contains("县"))
                city = city.replace("县","");
            String pro1 = "%"+city+"%";
            String sql = "select * from jingdiannum1 where city like ? order by num desc limit 3";
            List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
                public JingdianNum rowMapping(ResultSet rs) throws SQLException {
                    JingdianNum jingdian = null;
                    //jingdian、lng、lat、num、
                    jingdian = new JingdianNum(rs.getString("jingdian"),rs.getString("lng"),rs.getString("lat"),rs.getInt("num"));
                    return jingdian;
                }
            },pro1);
            cityjingdians.addAll(jingdians);
        }
        return cityjingdians;
    }

    /**
     *  统计每个地区的游记数量
     */
    public static List<ProvinceBean> CityTravelTop(String[] cities) throws Exception {
        List<ProvinceBean> beans= new ArrayList<>();
        for(String city : cities){
            if(city.contains("市"))
                city = city.replace("市","");
            if(city.contains("县"))
                city = city.replace("县","");
            String pro1 = "%"+city+"%";
            String sql = "select * from province where city like ? ";
            List<ProvinceBean> bean1 = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
                public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                    ProvinceBean bean = null;
                    bean = new ProvinceBean(rs.getString("province"),rs.getInt("num"),rs.getInt("haonum"),
                            rs.getInt("haonum1"),rs.getInt("haonum2"),rs.getInt("haonum3"),
                            rs.getInt("haonum4"),rs.getInt("normalnum"),rs.getInt("normalnum1"),
                            rs.getInt("normalnum2"),rs.getInt("normalnum3"),rs.getInt("normalnum4"),
                            rs.getInt("badnum"),rs.getInt("badnum1"),rs.getInt("badnum2"),
                            rs.getInt("badnum3"),rs.getInt("badnum4"));
                    return bean;
                }
            }, pro1);
            beans.addAll(bean1);
        }
        return beans;
    }
    /**
     * 游记最多的10个城市
     */
    public static List<ProvinceBean> CityTravelTop() throws Exception {
        //List<ProvinceBean> cities = new Array
        String sql = "select * from province order by haonum+badnum+normalnum desc limit 10";
        List<ProvinceBean> cities = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
            public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                ProvinceBean bean = new ProvinceBean(rs.getString("city"),rs.getInt("num"));
                return bean;
            }
        });
        return cities;
    }

    /**
     * 根据季节查找游记最多的10个城市
     */
    public static List<ProvinceBean> CityTravelTop(String month) throws Exception {
        //List<ProvinceBean> cities = new Array
        String sql = "";
        if(month.equals("0")){
            sql = "select * from province order by haonum+badnum+normalnum desc limit 10";
        }else if(month.equals("1")){//3-5
            sql = "select * from province order by haonum1+badnum1+normalnum1 desc limit 10";
        }else if(month.equals("2")){//6-8
            sql = "select * from province order by haonum2+badnum2+normalnum2 desc limit 10";
        }else if(month.equals("3")){//9-11
            sql = "select * from province order by haonum3+badnum3+normalnum3 desc limit 10";
        }else if(month.equals("4")){//12-2
            sql = "select * from province order by haonum4+badnum4+normalnum4 desc limit 10";
        }

        List<ProvinceBean> cities = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
            public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                ProvinceBean bean = new ProvinceBean(rs.getString("city"),rs.getString("city1"),rs.getInt("num"));
                return bean;
            }
        });
        return cities;
    }

    /**
     * 景点频率最多的10个景点
     */
    public static List<JingdianNum> JingdianTop() throws Exception {
        String sql = "select * from jingdiannum1 order by num desc limit 10";
        List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
            public JingdianNum rowMapping(ResultSet rs) throws SQLException {
                JingdianNum jingdian = null;
                //jingdian、lng、lat、num、
                jingdian = new JingdianNum(rs.getString("jingdian"),rs.getString("lng"),rs.getString("lat"),rs.getInt("num"),false);
                return jingdian;
            }
        },null);
        return jingdians;
    }

    /**
     * 将汉字的城市名转化为拼音
     */
    public static String CityPinyin(String city) throws Exception {
        city = city+"%";
        String sql = "select * from province where city like ?";
        List<String> list = JDBCUtil1.executeQuery(sql, new RowMap<String>() {
            public String rowMapping(ResultSet rs) throws SQLException {
                String city1 = rs.getString("city1");
                return city1;
            }
        },city);
        return list.get(0);
    }
    /**
     * 分页查询每个城市的游记
     * 后期看看是否需要文章内容
     */
    public static  List<TravelBean1> TravelList(int currPage,String city) throws Exception {
        //将中文转为拼音
        //city = CityPinyin(city);
        String sql = "select * from travel where city=? limit ?,10";
        //String sql = "select * from travel where city=?";
        List<TravelBean1> travels = JDBCUtil1.executeQuery(sql, new RowMap<TravelBean1>() {
            public TravelBean1 rowMapping(ResultSet rs) throws SQLException {
                TravelBean1 travel = new TravelBean1(rs.getInt("id"),rs.getString("title"),
                       rs.getString("city"),rs.getInt("month"),
                        rs.getInt("day"),rs.getString("score"));
                return travel;  // rs.getString("content"),
            }
        },city,(currPage-1)*10);//
        return travels;
    }
    /**
     * 查询每个城市的游记数量
     */
    public int cityNum(String city) throws Exception {
        System.out.println("city:"+city);
        String sql = "select num from province where city1=?";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer num = rs.getInt("num");
                return num;
            }
        },city);
        return nums.get(0);
    }

    /**
     * 分页查询所有的游记
     */
    public static  List<TravelBean1> TravelAllList(int currPage) throws Exception {
        String sql = "select * from travel limit ?,10";
        //String sql = "select * from travel where city=?";
        List<TravelBean1> travels = JDBCUtil1.executeQuery(sql, new RowMap<TravelBean1>() {
            public TravelBean1 rowMapping(ResultSet rs) throws SQLException {
                TravelBean1 travel = new TravelBean1(rs.getInt("id"),rs.getString("title"),
                        rs.getString("city"),rs.getInt("month"),
                        rs.getInt("day"),rs.getString("score"));
                return travel;
            }
        },(currPage-1)*10);//
        return travels;
    }

    /**
     * 查询所有的游记数量
     */
    public int TravelAllNum() throws Exception {
        String sql ="select count(*) num1 from travel";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer num = rs.getInt("num1");
                return num;
            }
        },null);
        return nums.get(0);
    }

    /**
     * 根据id查找游记内容
     */
    public TravelBean1 searchTravel(Integer id) throws Exception {
        String sql = "select * from travel where id=?";
        List<TravelBean1> travels = JDBCUtil1.executeQuery(sql, new RowMap<TravelBean1>() {
            public TravelBean1 rowMapping(ResultSet rs) throws SQLException {
                TravelBean1 travel = new TravelBean1(rs.getInt("id"),rs.getString("title"),
                        rs.getString("content"),rs.getString("city"),rs.getInt("month"),
                        rs.getInt("day"),rs.getString("score"));
                return travel;
            }
        },id);
        return travels.get(0);
    }

    /**
     * 根据id查找游记内容
     */
    public TravelBean1 searchTravelById(String id) throws Exception {
        String sql = "select * from travel where id=?";
        List<TravelBean1> travels = JDBCUtil1.executeQuery(sql, new RowMap<TravelBean1>() {
            public TravelBean1 rowMapping(ResultSet rs) throws SQLException {
                TravelBean1 travel = new TravelBean1(rs.getInt("id"),rs.getString("title"),
                        rs.getString("city"),rs.getInt("month"),
                        rs.getInt("day"),rs.getString("score"));
                return travel;
            }
        },id);
        return travels.get(0);
    }

    /**
     * 根据城市名，查找出该城市的所有游记信息
     */
    public static List<TravelBean1> cityTravels(String city1) throws Exception {
        String sql = "select * from travel where city=?";
        List<TravelBean1> travels = JDBCUtil1.executeQuery(sql, new RowMap<TravelBean1>() {
            public TravelBean1 rowMapping(ResultSet rs) throws SQLException {
                TravelBean1 travel = new TravelBean1(rs.getInt("id"),rs.getString("title"),
                        rs.getString("content"),rs.getString("city"),rs.getInt("month"),
                        rs.getInt("day"),rs.getString("score"));
                return travel;
            }
        },city1);
        return travels;
    }

    /**
     * 根据城市汉字 找到城市
     * @return
     */
    public static List<ProvinceBean> getCityByCity(String city,int currPage) throws Exception {
        city = "%"+city+"%";
        String sql = "select * from province where city like ? limit ?,10";
        List<ProvinceBean> provinceBeans = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
            public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                ProvinceBean bean = new ProvinceBean(rs.getInt("id"),rs.getString("city"),rs.getString("city1"),rs.getInt("num"));
                return bean;
            }
        },city,(currPage-1)*10);
        return provinceBeans;
    }

    /**
     * 根据城市拼音 找到城市汉字
     * @return
     */
    public static String getCity1ByCity(String city1) throws Exception {
        String sql = "select * from province where city1=?";
        List<ProvinceBean> provinceBeans = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
            public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                ProvinceBean bean = new ProvinceBean(rs.getInt("id"),rs.getString("city"),rs.getString("city1"),rs.getInt("num"));
                return bean;
            }
        },city1);
        return provinceBeans.get(0).getCity();
    }
    public static Integer getCityByCityNum(String city) throws Exception {
        city = "%"+city+"%";
        String sql = "select count(*) num11 from province where city like ?";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer num = rs.getInt("num11");
                return num;
            }
        },city);
        return nums.get(0);
    }

    //获得所有的省
    public static List<String> getProvinces() throws Exception {
        String sql = "select province from provinces";
        List<String> provinces = JDBCUtil1.executeQuery(sql, new RowMap<String>() {
            public String rowMapping(ResultSet rs) throws SQLException {
                String province = rs.getString("province");
                return province;
            }
        });
        return provinces;
    }



}
