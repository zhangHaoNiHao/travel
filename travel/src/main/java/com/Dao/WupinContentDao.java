package com.Dao;

import com.Bean.WupinContentBean;
import com.utils.JDBCUtil1;
import com.utils.RowMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WupinContentDao {

    public static boolean addWupinContent(WupinContentBean content){
        String sql = "insert into wupincontent(wupin,city1,content,travelid,month) values(?,?,?,?,?)";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,content.getwupin(),content.getCity1(),content.getContent(),content.getTravelid(),content.getMonth());
        boolean f = false;
        if(a>0)
            f= true;
        return f;
    }


    public static List<WupinContentBean> getWupin() throws Exception {
        String sql = "select id,content from wupincontent where score is NULL or score=0";
        List<WupinContentBean> wupinContents = JDBCUtil1.executeQuery(sql, new RowMap<WupinContentBean>() {
            public WupinContentBean rowMapping(ResultSet rs) throws SQLException {
                WupinContentBean wupinContent = new WupinContentBean(rs.getInt("id"),rs.getString("content"));
                return wupinContent;
            }
        });
        return wupinContents;
    }

    public static boolean updateWupinScore(int id,String score){
        String sql = "update wupincontent set score=? where id=?";
        int a = 0;
        a = JDBCUtil1.executeUpdate(sql,score,id);
        boolean f = false;
        if(a>0)
            f= true;
        return f;
    }
}
