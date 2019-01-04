package com.Dao;

import com.Bean.TravelBean;
import com.Bean.UserBean;
import com.utils.JDBCUtil1;
import com.utils.RowMap;
import com.utils.jdbcUtils;
import org.junit.Test;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao{
	
	/**
	 * 删除用户
	 * @param username
	 * @return
	 */

	public Boolean delete(String username) throws Exception {
		Boolean f = false;
		int a = 0;
		String sql = "delete * from user where username=?";
		
		a = jdbcUtils.executeUpdate(sql, username);
		if(a > 0){
			f = true;
		}
		return f; 
	}

    /**
     * 查询所有的游记
     * @return
     */
    @Override
    public List<TravelBean> TravelList() throws Exception {
    	String sql = "select * from travel";
        List<TravelBean> list = jdbcUtils.executeQuery(sql, new RowMap<TravelBean>(){
            public TravelBean rowMapping(ResultSet rs) {
                TravelBean u = null;
                try {
                    u = new TravelBean(rs.getInt("id"),rs.getString("title"),rs.getString("content"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return u;
            }
        }, null);
        return list;
    }

	public List<TravelBean> searchTravel(Integer id) throws Exception {
		System.out.println("dao: "+id);
		String sql = "select * from travel where id=?";
		List<TravelBean> list = jdbcUtils.executeQuery(sql, new RowMap<TravelBean>(){
			public TravelBean rowMapping(ResultSet rs) {
				TravelBean u = null;
				try {
					u = new TravelBean(rs.getInt("id"),rs.getString("title"),rs.getString("content"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return u;
			}
		}, id);
		return list;
	}

    @Override
	public Boolean checkIsExit(String username) throws Exception {
		return null;
	}

	public List<UserBean> search(String username) throws Exception{
		
		List<UserBean> list = jdbcUtils.executeQuery("select * from user where username=?", new RowMap<UserBean>(){
			public UserBean rowMapping(ResultSet rs) {
				UserBean u = null;
				try {
					u = new UserBean(rs.getString("username"),rs.getString("password"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return u;
			}
		}, username);
		
		return list;	
	}

	/***
	 * 查询所有用户
	 */
	public List<UserBean> Userlist() throws Exception{
		List<UserBean> list = JDBCUtil1.executeQuery("select * from user", new RowMap<UserBean>(){
			public UserBean rowMapping(ResultSet rs) {
				UserBean u = null;
				try {
					u = new UserBean(rs.getString("username"),rs.getString("password"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return u;
			}
		}, null);
		return list;
	}

	/**
	 * 根据id查找用户信息
	 */
	public UserBean getUserInfoById(int id) throws Exception {
		String sql = "select * from user where id=?";
		List<UserBean> users = JDBCUtil1.executeQuery(sql, new RowMap<UserBean>() {
			public UserBean rowMapping(ResultSet rs) throws SQLException {
				UserBean bean = new UserBean(id,rs.getString("username"),rs.getString("password"),rs.getString("email"));
				return bean;

			}
		},id);
		return users.get(0);
	}


	public List<String> listTxt() throws IOException {
		String path = "D:/data/xiecheng1";

		File file = new File(path);
		List<String> list = new ArrayList<>();
		if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i <= 0; i++) {
				String path2 = path + "/" + filelist[i];

				System.out.println(path2 + "  " + filelist[i]);
				File readfile = new File(path2);
				if (!readfile.isDirectory()) {
					InputStreamReader reader = new InputStreamReader(new FileInputStream(readfile), "UTF-8");
					BufferedReader bf = new BufferedReader(reader);
					String line = bf.readLine();
					int a = 1;

					while (null != line ) {
						if("" != null) {
							System.out.println("第 " + a + "行：" + line);
							list.add(line);
							a++;
						}
						line = bf.readLine();
					}
				}
			}
		}
		return list;
	}

}
