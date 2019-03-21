package com.Dao;

import com.Bean.ProvinceBean;
import com.utils.JDBCUtil1;
import com.utils.RowMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProvinceDao {

    /**
     * 根据城市拼音，找到省
     * @param city1
     */
    public static ProvinceBean getProvinceByCity1(String city1) throws Exception {
        String sql = "select * from province where city1=?";
        List<ProvinceBean> provinceBeans = JDBCUtil1.executeQuery(sql, new RowMap<ProvinceBean>() {
            public ProvinceBean rowMapping(ResultSet rs) throws SQLException {
                ProvinceBean bean = new ProvinceBean(rs.getInt("id"),rs.getString("city"),rs.getString("city1"),
                        rs.getString("lng"),rs.getString("lat"));
                return bean;
            }
        },city1);
        if(provinceBeans != null && provinceBeans.size()>0)
            return provinceBeans.get(0);
        else{
            return null;
        }
    }
}
