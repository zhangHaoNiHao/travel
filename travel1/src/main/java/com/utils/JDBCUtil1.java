package com.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JDBCUtil1 {
	public static String URL="jdbc:mysql://localhost:3306/travel2?useUnicode=true&characterEncoding=utf8";
	public static String USER="root";
	public static String PWD="123456";
	
	public static Connection getConnection(){
		Connection conn=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(URL, USER, PWD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	public static void Close(Connection con,PreparedStatement pstmt,ResultSet rs){
		try {
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
			if(con!=null)
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//增删改操作
	public static int executeUpdate(String sql,Object... params){
		Connection conn=getConnection();
		PreparedStatement pstmt=null;
		int result=0;
		try {
			pstmt=conn.prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pstmt.setObject(i+1, params[i]);
				}
			}
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			Close(conn,pstmt,null);
		}
		return result;
	}
	/*
	 * 查询
	 */
	/*
	public static UserBean executeSelect(String sql,Object... params){
		Connection con=getConnection();
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		UserBean userbean = null;
		try {
			pstmt=con.prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pstmt.setObject(i+1, params[i]);
				}
			}
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				String username = rs.getString("username");
				String password = rs.getString("password");
				userbean = new UserBean(username,password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			Close(con,pstmt,null);
		}
		return userbean;
	}
	*/

	/**
	 * 封装数据库查询操作
	 * @param sql		待执行的SQL语句
	 * @param handler	结果集处理接口
	 * @param args		SQL语句中的参数
	 * @return			handler处理的结果
	 * @throws SQLException
	 */
	public static <T> List<T> query(String sql,ResultSetHandler<T> handler,Object...args) throws SQLException{
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)){
			ParameterMetaData parameterMetaData = ps.getParameterMetaData();
			int parameterCount = parameterMetaData.getParameterCount();
			for(int i = 1; i <= parameterCount; i++){
				ps.setObject(i, args[i-1]);
			}
			ResultSet rs = ps.executeQuery();
			List<T> list = new ArrayList<>();
			if(handler != null){
				int i = 1;
				while(rs.next()){
					T target = handler.handler(rs, i++);
					list.add(target);
				}
			}
			return list;
		}
	}

	//查询
	public  static <T> List<T> executeQuery(String sql,RowMap<T> rowMap,Object... params) throws Exception{
		List<T> list=new ArrayList<T>();
		Connection con=getConnection();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=con.prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pstmt.setObject(i+1, params[i]);
				}
			}
			rs=pstmt.executeQuery();
			while(rs.next()){
				T t=rowMap.rowMapping(rs);
				list.add(t);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			Close(con,pstmt,rs);
		}
		return list;
	}
	
}
