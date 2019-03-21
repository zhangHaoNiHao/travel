package com.Dao;

import com.Bean.MeishiContentBean;
import com.utils.JDBCUtil1;
import com.utils.RowMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MeishiContentDao {

    public static boolean addJingdianContent(MeishiContentBean content){
        String sql = "insert into meishicontent(meishi,city1,content,travelid,month) values(?,?,?,?,?)";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,content.getmeishi(),content.getCity1(),content.getContent(),content.getTravelid(),content.getMonth());
        boolean f = false;
        if(a>0)
            f= true;
        return f;
    }

    public static List<MeishiContentBean> getMeishi() throws Exception {
        String sql = "select id,content from meishicontent where score is NULL";
        List<MeishiContentBean> wupinContents = JDBCUtil1.executeQuery(sql, new RowMap<MeishiContentBean>() {
            public MeishiContentBean rowMapping(ResultSet rs) throws SQLException {
                MeishiContentBean wupinContent = new MeishiContentBean(rs.getInt("id"),rs.getString("content"));
                return wupinContent;
            }
        });
        return wupinContents;
    }

    public static boolean updateMeishiScore(int id,String score){
        String sql = "update meishicontent set score=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,score,id);
        boolean f = false;
        if(a>0)
            f= true;
        return f;
    }
}
