package com.Dao;

import com.Bean.JingdianBean;
import com.Bean.JingdianNum;
import com.Bean.JingdianTravelId;
import com.Bean.TravelBean1;
import com.utils.JDBCUtil;
import com.utils.JDBCUtil1;
import com.utils.RowMap;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JingdianDao {

	/**
	 * 添加景点信息
	 */
	public static int addjingdian(JingdianBean jingdian){
		
		String sql = "insert into jingdian(jingdian,name,province,city,area,address,lng,lat,description) values(?,?,?,?,?,?,?,?,?)";
		int a = 0;
		a = JDBCUtil.executeUpdate(sql, jingdian.getJingdian(),jingdian.getName(),jingdian.getProvince(),jingdian.getCity(),
				jingdian.getArea(),jingdian.getAddress(),jingdian.getLng(),jingdian.getLat(),jingdian.getDescription());
		return a;
	}

	/**
	 * 添加景点信息,和数量
	 */
	public static Boolean addjingdian1(JingdianNum jingdian){

		String sql = "insert into jingdian(jingdian,name,province,city,city1,area,lng,lat) values(?,?,?,?,?,?,?,?)";
		int a = 0;
		a = JDBCUtil1.executeUpdate(sql, jingdian.getJingdian(),jingdian.getName(),jingdian.getProvince(),jingdian.getCity(),
				jingdian.getCity1(),jingdian.getArea(),jingdian.getLng(),jingdian.getLat());
		boolean f = false;
		if(a > 0){
			f = true;
		}
		return f;
	}
	
	/**
	 * 添加景点数量
	 */
	public static int addjingdianNum(JingdianNum jingdiannum){
		
		String sql = "insert into jingdiannum(jingdian,name,province,city,area,address,lng,lat,num) values(?,?,?,?,?,?,?,?,?)";
		int a = 0;
		a = JDBCUtil.executeUpdate(sql, jingdiannum.getJingdian(),jingdiannum.getName(),jingdiannum.getProvince(),jingdiannum.getCity(),
				jingdiannum.getArea(),jingdiannum.getAddress(),jingdiannum.getLng(),jingdiannum.getLat(),jingdiannum.getNum());
		return a;
	}

	/**
	 * 添加景点数量
	 */
	public static boolean addjingdianNum1(JingdianNum jingdiannum){

		String sql = "insert into jingdiannum(jingdian,name,province,city,city1,area,lng,lat,num) values(?,?,?,?,?,?,?,?,?)";
		int a = 0;
		a = JDBCUtil1.executeUpdate(sql, jingdiannum.getJingdian(),jingdiannum.getName(),jingdiannum.getProvince(),jingdiannum.getCity(),
				jingdiannum.getCity1(),jingdiannum.getArea(),jingdiannum.getLng(),jingdiannum.getLat(),jingdiannum.getNum());
		boolean f = false;
		if(a>0){
			f= true;
		}
		return f;
	}

	/**
	 * 查询所有的景点数量
	 * @return
	 */
	public static List<JingdianNum> jingdianNumList() throws SQLException {
		System.out.println("查询所有的 景点数量");
		String sql = "select * from jingdiannum ";

		List<JingdianNum> jingdianNums = new ArrayList<>();
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sql);
		ResultSet rs = null;
		rs = pstmt.executeQuery();

		String jingdian = "";
		String name = "";
		String lng = "";
		String lat = "";
		int num = 0;
		while(rs.next()){
			jingdian = rs.getString("jingdian");
			name = rs.getString("name");
			lng = rs.getString("lng");
			lat = rs.getString("lat");
			num = rs.getInt("num");
			System.out.println("name:"+name);
			if(!(lng.equals("") || lat.equals("")))
			{
				JingdianNum jingdianNum = new JingdianNum(jingdian,name,lng,lat,num);
				jingdianNums.add(jingdianNum);
			}
		}
		JDBCUtil.Close( conn,pstmt, rs);
		return jingdianNums;
	}

	/**
	 * 查询所有的景点
	 */
	public static List<JingdianNum> AllJingdianNum() throws Exception {
		String sql = "select * from jingdiannum1";
		List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
			public JingdianNum rowMapping(ResultSet rs) throws SQLException {
				JingdianNum jingdian = new JingdianNum(rs.getInt("id"),rs.getString("jingdian"),rs.getString("city1"),
						rs.getString("photo3"),rs.getString("photo6"),rs.getString("photo9"),rs.getString("photo12"));
				return jingdian;
			}
		});
		return jingdians;
	}

	//修改景点在各个季节的数量
	public static Boolean updateNum(String jingdian,int num,int month,String city1){
		String sql = "";
		if(month>=3 && month <=5){
			sql = "update jingdiannum1 set num3=? where jingdian=? and city1=?";
		}
		else if(month>=6 && month<=8){
			sql = "update jingdiannum1 set num6=? where jingdian=? and city1=?";
		}else if(month>=9 && month<=11){
			sql = "update jingdiannum1 set num9=? where jingdian=? and city1=?";
		}else if(month==12 || month<=2){
			sql = "update jingdiannum1 set num12=? where jingdian=? and city1=?";
		}
		int a = 0;
		a = JDBCUtil1.executeUpdate(sql,num,jingdian,city1);
		boolean f = false;
		if(a>0)
			f = true;
		return f;
	}

	/**
	 * 根据城市拼音和景点名查找景点
	 */
	public static JingdianNum jingdianNumSearch(String city1,String jingdian) throws Exception {
		String sql = "select * from jingdiannum1 where city1=? and jingdian=?";
		List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
			public JingdianNum rowMapping(ResultSet rs) throws SQLException {
				JingdianNum jingdian = new JingdianNum(rs.getInt("id"),rs.getString("jingdian"),rs.getString("city1"),rs.getString("photo3"),rs.getString("photo6"),
						rs.getString("photo9"),rs.getString("photo12"));
				return jingdian;
			}
		},city1,jingdian);
		if(jingdians.size()>0){
			return jingdians.get(0);
		}else
			return null;
	}
	/**
	 * 根据景点名查找景点(可能不唯一)
	 */
	public static JingdianNum jingdianNumSearch(String jingdian) throws Exception {
		String sql = "select id from jingdiannum1 where jingdian=?";
		List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
			public JingdianNum rowMapping(ResultSet rs) throws SQLException {
				JingdianNum jingdian = new JingdianNum(rs.getInt("id"));
				return jingdian;
			}
		},jingdian);
		if(jingdians.size()>0){
			return jingdians.get(0);
		}else
			return null;
	}

	/**
	 * 模糊查询景点
	 */
	public static List<JingdianNum> JingdianSearch(String jingdian,int currPage) throws Exception {
		jingdian = "%"+jingdian+"%";
		String sql = "select * from jingdiannum1 where jingdian like ? limit ?,10";
		List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
			public JingdianNum rowMapping(ResultSet rs) throws SQLException {
				JingdianNum jingdian = new JingdianNum(rs.getInt("id"),rs.getString("jingdian"),rs.getString("city1"),rs.getString("city"),
						rs.getInt("num"),rs.getInt("num3"),rs.getInt("num6"),rs.getInt("num9"),rs.getInt("num12"),
						rs.getString("photo3"),rs.getString("photo6"),
						rs.getString("photo9"),rs.getString("photo12"));
				return jingdian;
			}
		},jingdian,(currPage-1)*10);
		return jingdians;
	}
	/**
	 * 模糊查询景点的个数
	 */
	public static int JingdianSearchNum(String jingdian) throws Exception {
		jingdian = "%"+jingdian+"%";
		String sql = "select count(*) num11 from jingdiannum1 where jingdian like ?";
		List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
			public Integer rowMapping(ResultSet rs) throws SQLException {
				Integer a = rs.getInt("num11");
				return a;
			}
		},jingdian);
		return nums.get(0);
	}

	/**
	 * 查询城市的所有景点
	 */
	public static List<JingdianNum> JingdianCity(String city1,int currPage) throws Exception {
		String sql = "select * from jingdiannum1 where city1=? order by num desc limit ?,10";
		List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
			public JingdianNum rowMapping(ResultSet rs) throws SQLException {
				JingdianNum jingdian = new JingdianNum(rs.getInt("id"),rs.getString("jingdian"),rs.getString("city1"),rs.getString("city"),
						rs.getInt("num"),rs.getInt("num3"),rs.getInt("num6"),rs.getInt("num9"),rs.getInt("num12"),
						rs.getString("photo3"),rs.getString("photo6"),
						rs.getString("photo9"),rs.getString("photo12"));
				return jingdian;
			}
		},city1,(currPage-1)*10);
		return jingdians;
	}

	/**
	 * 分页查询所有景点
	 */
	public static List<JingdianNum> JingdianAll(int currPage) throws Exception {
		String sql = "select * from jingdiannum1 order by num desc limit ?,10";
		List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
			public JingdianNum rowMapping(ResultSet rs) throws SQLException {
				JingdianNum jingdian = new JingdianNum(rs.getInt("id"),rs.getString("jingdian"),rs.getString("city1"),rs.getString("city"),
						rs.getInt("num"),rs.getInt("num3"),rs.getInt("num6"),rs.getInt("num9"),rs.getInt("num12"),
						rs.getString("photo3"),rs.getString("photo6"),
						rs.getString("photo9"),rs.getString("photo12"));
				return jingdian;
			}
		},(currPage-1)*10);
		return jingdians;
	}

	/**
	 * 查询城市的景点数量
	 */
	public static Integer JingdianCityNum(String city1) throws Exception {
		String sql = "select count(*) num1 from jingdiannum1 where city1=?";
		List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
			public Integer rowMapping(ResultSet rs) throws SQLException {
				int num = rs.getInt("num1");
				return num;
			}
		},city1);
		return nums.get(0);
	}

	/**
	 * 查询景点的总数量
	 */
	public static Integer JingdianAllNum() throws Exception {
		String sql = "select count(*) num1 from jingdiannum1";
		List<Integer> nums = JDBCUtil1.executeQuery(sql, new RowMap<Integer>() {
			public Integer rowMapping(ResultSet rs) throws SQLException {
				int num = rs.getInt("num1");
				return num;
			}
		},null);
		return nums.get(0);
	}

	/**
	 * 添加景点图片
	 */
	public static boolean updatePhoto(int id,String photo){
		String sql = "update jingdiannum1 set photo=? where id=?";
		int a = 0;
		a = JDBCUtil1.executeUpdate(sql,photo,id);
		boolean f = false;
		if(a > 0)
			f = true;
		return f;
	}

	/**
	 * 根据景点获得景点查询
	 */
	public static JingdianNum JingdianDetail(String jingdian) throws Exception {
		String sql = "select * from jingdiannum1 where jingdian=?";
		List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
			public JingdianNum rowMapping(ResultSet rs) throws SQLException {
				JingdianNum jingdianNum = null;
				if(rs != null)
					jingdianNum = new JingdianNum(rs.getInt("id"),jingdian,rs.getString("lng"),rs.getString("lat"),rs.getInt("num"));
				return jingdianNum;
			}
		},jingdian);
		if(jingdians.size()>0){
			return jingdians.get(0);
		}else
			return null;
	}

	/**
	 * 	添加情感分析分数
	 */
	public static boolean qinggan(int id,String score){
		String sql = "update jingdian set score=? where id=?";
		int a = 0;
		a = JDBCUtil.executeUpdate(sql, score,id);
		return true;
	}


	/**
	 * 添加游记
	 */
	public Boolean addTravel(TravelBean1 travel){
		Boolean flag = false;
		String sql = "insert into travel(title,content,city,score,month,day) values(?,?,?,?,?)";
		int a = 0;
		a = JDBCUtil.executeUpdate(sql, travel.getTitle(),travel.getContent(),travel.getCity(),travel.getScore(),travel.getMonth(),travel.getDay());
		if(a > 0){
			flag = true;
		}
		return flag;
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
	 * 根据季节查询前10景点
	 */
	public List<JingdianNum> SeasonJingdianTop(String month) throws Exception {
		String sql = "";
		if(month.equals("0")){
			sql = "select * from jingdiannum1 order by num desc limit 10";
		}else if(month.equals("1")){ //3-5
			sql = "select * from jingdiannum1 order by num3 desc limit 10";
		}else if(month.equals("2")){ //6-8
			sql = "select * from jingdiannum1 order by num6 desc limit 10";
		}else if(month.equals("3")){ //9-11
			sql = "select * from jingdiannum1 order by num9 desc limit 10";
		}else if(month.equals("4")){ //12-2
			sql = "select * from jingdiannum1 order by num12 desc limit 10";
		}
		List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
			public JingdianNum rowMapping(ResultSet rs) throws SQLException {
				JingdianNum jingdian = null;
				//jingdian、lng、lat、num、
				jingdian = new JingdianNum(rs.getInt("id"),rs.getString("jingdian"),rs.getString("city1"),rs.getString("lng"),rs.getString("lat"),rs.getInt("num"),false,rs.getString("photo"));
				return jingdian;
			}
		},null);
		return jingdians;

	}

	/**
	 * 根据id获得景点
	 */
	public static JingdianNum GetJingdainNumById(int id) throws Exception {
		System.out.println("id:"+id);
		String sql = "select * from jingdiannum1 where id=?";
		List<JingdianNum> jingdians = JDBCUtil1.executeQuery(sql, new RowMap<JingdianNum>() {
			public JingdianNum rowMapping(ResultSet rs) throws SQLException {
				JingdianNum jingdian = null;
				//jingdian、lng、lat、num、
				jingdian = new JingdianNum(rs.getInt("id"),rs.getString("jingdian"),rs.getString("city1"),rs.getString("lng"),rs.getString("lat"),
						rs.getInt("num"),false,rs.getString("photo"),rs.getString("photo3"),rs.getString("photo6"),rs.getString("photo9"),
						rs.getString("photo12"));
				System.out.println(rs.getString("photo"));
				return jingdian;
			}
		},id);
		return jingdians.get(0);
	}

	/**
	 * 将获取的景点图片链接,存到数据库
	 * 已经按照月份分好类，直接修改即可
	 */
	public static boolean InsertJingdianPhoto(JingdianNum jingdian){
		String sql = "update jingdiannum1 set photo3=?,photo6=?,photo9=?,photo12=? where id=?";
		int a = 0;
		a = JDBCUtil1.executeUpdate(sql,jingdian.getPhoto3(),jingdian.getPhoto6(),jingdian.getPhoto9(),jingdian.getPhoto12(),jingdian.getId());
		boolean f = false;
		if(a>0)
			f= true;
		return f;
	}

	/**
	 * 根据城市和景点，查询该景点对应的游记id
	 */
	public static JingdianTravelId travelId(String city1,String jingdina) throws Exception {
		String sql = "select * from jingdiantravelid where city1=? and jingdian=?";
		List<JingdianTravelId> travelIds = JDBCUtil1.executeQuery(sql, new RowMap<JingdianTravelId>() {
			public JingdianTravelId rowMapping(ResultSet rs) throws SQLException {
				JingdianTravelId travelId = new JingdianTravelId(rs.getInt("id"),rs.getString("jingdian"),
						rs.getString("city1"),rs.getString("travelids"));
				return travelId;
			}
		},city1,jingdina);
		return travelIds.get(0);
	}

}
