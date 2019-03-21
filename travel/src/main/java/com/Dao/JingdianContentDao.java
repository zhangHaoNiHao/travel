package com.Dao;

import com.Bean.JingdianContentBean;
import com.utils.JDBCUtil1;
import com.utils.RowMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JingdianContentDao {

    /**
     * 将景点内容插入数据库
     */
    public static boolean addJingdianContent(JingdianContentBean content){
        String sql = "insert into jingdiancontent(jingdian,city1,content,travelid,month) values(?,?,?,?,?)";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,content.getJingdian(),content.getCity1(),content.getContent(),content.getTravelid(),content.getMonth());
        boolean f = false;
        if(a>0)
            f= true;
        return f;
    }

    /**
     * 获得还未进行评分的内容
     */
    public static List<JingdianContentBean> getJingdian() throws Exception {
        String sql = "select id,content from jingdiancontent where score is NULL order by id desc limit 0,10000 ";
        List<JingdianContentBean> jingdianContents = JDBCUtil1.executeQuery(sql, new RowMap<JingdianContentBean>() {
            public JingdianContentBean rowMapping(ResultSet rs) throws SQLException {
                JingdianContentBean jingdianContent = new JingdianContentBean(rs.getInt("id"),rs.getString("content"));
                return jingdianContent;
            }
        });
        return jingdianContents;
    }

    /**
     * 修改景点内容的评分
     */
    public static boolean updateJingdianScore(int id,String score){
        String sql = "update jingdiancontent set score=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,score,id);
        boolean f = false;
        if(a>0)
            f= true;
        return f;
    }

    /**
     * 根据景点和城市获取景点的内容
     */
    public static List<JingdianContentBean> GetjingdianContent(String jingdian,String city1,int currPage) throws Exception {
        String sql = "select * from jingdiancontent where jingdian=? and city1=? order by score desc limit ?,10";
        List<JingdianContentBean> jingdianContentBeans = JDBCUtil1.executeQuery(sql, new RowMap<JingdianContentBean>() {
            public JingdianContentBean rowMapping(ResultSet rs) throws SQLException {
                JingdianContentBean bean = new JingdianContentBean(rs.getInt("id"),rs.getString("jingdian"),rs.getString("city1"),
                        rs.getString("content"),rs.getInt("travelid"),rs.getInt("month"),rs.getString("score"));
                return bean;
            }
        },jingdian,city1,(currPage-1)*10);
        return jingdianContentBeans;
    }

    /**
     * 根据景点和城市获取景点的内容的总数量
     */
    public static Integer GetjingdianContentNum(String jingdian,String city1) throws Exception {
        String sql = "select count(*) num11 from jingdiancontent where jingdian=? and city1=?";
        List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
            public Integer rowMapping(ResultSet rs) throws SQLException {
                Integer num = rs.getInt("num11");
                return num;
            }
        },jingdian,city1);
        return nums.get(0);
    }

    /**
     * 删除jingdianContent表中评分为0的记录
     */
    public static void deleteScore0(){
        String sql = "delete from jingdiancontent where score=0";
        int a = 0;
        a  = JDBCUtil1.executeUpdate(sql);
    }


    /**
     * 查询每个景点的评分的平均分
     */
    public static List<JingdianContentBean> JingdianContentScore() throws Exception {
        String sql = "select jingdian,city1,AVG(score) avgnum from jingdiancontent GROUP BY jingdian,city1";
        List<JingdianContentBean> jingdianContentBeans = JDBCUtil1.executeQuery(sql, new RowMap<JingdianContentBean>() {
            public JingdianContentBean rowMapping(ResultSet rs) throws SQLException {
                JingdianContentBean jingdianContent = new JingdianContentBean(rs.getString("jingdian"),rs.getString("city1"),rs.getString("avgnum"));
                return jingdianContent;
            }
        });
        return jingdianContentBeans;
    }
}
