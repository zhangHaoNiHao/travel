package com.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class jdbcUtils {
	
	public static Connection getConnection() throws Exception
	{
		/*
		ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
		String driverClass=bundle.getString("driverClass");
		String url = bundle.getString("url");
		String user = bundle.getString("user");
		String password = bundle.getString("password");
		*/
		String driverClass="com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/travel";
		String user = "root";
		String password = "123456";

		Class.forName(driverClass);
		Connection conn = null;
		conn = DriverManager.getConnection(url,user,password);
		return conn;
	}
	public static void closeResource(Connection conn,Statement st,ResultSet rs)
	{
		closeResultSet(rs);
		closeStatement(st);
		closeConnection(conn);
	}
	public static void closeResultSet(ResultSet rs)
	{
		if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		rs = null;
	}
	public static void closeStatement(Statement pstmt)
	{
		if(pstmt!=null)
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		pstmt = null;
	}
	public static void closeConnection(Connection conn)
	{
		if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		conn = null;
	}
	
	//增删改
	public static int executeUpdate(String sql, Object... params) throws Exception{
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
			closeResource(conn,pstmt,null);
		}
		return result;
	}
	
	//查询
	public  static <T> List<T> executeQuery(String sql,RowMap<T> rowMap,Object... params) throws Exception{
		List<T> list=new ArrayList<>();
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
			closeResource(con,pstmt,rs);
		}
		return list;
	}
	
}
