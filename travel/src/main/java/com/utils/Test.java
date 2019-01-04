package com.utils;


import com.Bean.TravelBean1;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Test implements ResultSetHandler<TravelBean1>{

    public static void main(String[] args) throws Exception {

        //System.out.println(APITest());

        /*
        String s = "djjskksdk456ddd";
        System.out.println(s.replaceAll("[0-9]+",""));
        */


        /*
        String path1 = "D:/data/jingdianAll/beijing/";
        File f = new File(path1);
        if(!f.exists()){
            f.createNewFile();
        }else {
            f.mkdir();
        }
        */
        /*
        String path="D:/data/jingdianAll";
        File f1 = new File(path + "/3.txt");
        String title1 = "123";
        OutputStreamWriter write;
        write = new OutputStreamWriter(new FileOutputStream(f1,true), "UTF-8");
        BufferedWriter bw = new BufferedWriter(write);
        if (!f1.exists()) {
            //如果文件不存在,就创建文件,并且进行追加
            f1.createNewFile();
            System.out.println("创建文件，添加");
            bw.write(title1);
        }else{
            //如果文件存在，直接追加
            System.out.println("追加");
            bw.write("\n"+title1);
        }
        bw.flush();
        bw.close();
        */
        //readfile("D:/data/travelsAll");

        //System.out.println("结果："+TravelList().get(0).getId()+" city:"+TravelList().get(0).getCity()+" "+TravelList().get(0).getTitle()+"  "+TravelList().get(0).getDay()+" "+TravelList().get(0).getMonth()+" "+TravelList().get(0).getContent());
        //int id = 576;
        //System.out.println("结果：" +new Test().findById((long) id).toString());


        String json = search("商城","河南省");
        System.out.println("json: "+json.toString());
        JSONObject obj = JSON.parseObject(json);
        //JSONObject obj2 = obj.getJSONObject("student");//通过key获得的是内存的数据{},所以是JSONObject 类型的数据
        JSONArray array = obj.getJSONArray("results");
        JSONArray array3 = JSON.parseObject(json).getJSONArray("results");
        if(array3.size() > 0 ){
            JSONObject obj3 = array3.getJSONObject(0);
            String area = obj3.getString("area");//通过key值取value
        }

        if(array.size() > 0){
            String province = array.getJSONObject(0).getString("province");
            System.out.println("省："+province);
        }

        /*
        String city = array.getJSONObject(0).getString("city");
        String area = array.getJSONObject(0).getString("area");
        String name = array.getJSONObject(0).getString("name");
        String lng = array.getJSONObject(0).getJSONObject("location").getString("lng");
        String lat = array.getJSONObject(0).getJSONObject("location").getString("lat");
        System.out.println(name+" "+area+" "+province+" "+city+" "+lng+" "+lat);
        */
        //System.out.println(TravelMonthCityList(1,"hangzhou"));
        //将所有的景点放到一个文件中，构建景点词库
        //jingdianFile();
    }

    public static List<TravelBean1> TravelList() throws Exception {
        String sql = "select * from travel";
        List<TravelBean1> list = JDBCUtil.executeQuery(sql, new RowMap<TravelBean1>(){
            public TravelBean1 rowMapping(ResultSet rs) {
                TravelBean1 u = null;
                try {
                    u = new TravelBean1(rs.getInt("id"),rs.getString("title"),rs.getString("content"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return u;
            }
        }, null);
        return list;
    }

    /**
     * 测试松林的查询方法，
     * @param id
     * @return
     */
    //id=576
    public TravelBean1 findById(Long id){
        try {
            List<TravelBean1> list = JDBCUtil.query("select id,title,content,city,month,day from travel where id=?", this, id);
            if(list.isEmpty()){
                return null;
            }else{
                return list.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
   @Override
   public TravelBean1 handler(ResultSet rs, int row) throws SQLException {
       String content = rs.getString("content");
       String title = rs.getString("title");
       String city = rs.getString("city");
       int month = rs.getInt("month");
       int day = rs.getInt("day");
       TravelBean1 c = new TravelBean1(title,content,city,month,day);
       return c;
   }
    //获取目录中的所有文件
    public static List<String> readfile(String filepath) throws Exception {
        File file = new File(filepath);
        List<String> files = new ArrayList<String>();
        if (file.isDirectory()) {
            System.out.println("文件夹");
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "/" + filelist[i]);
                if (!readfile.isDirectory()) {
                    //是文件
                    System.out.println("path=" + readfile.getPath());

                    String path = readfile.getAbsolutePath();
                    System.out.println("absolutepath="+ path);
                    files.add(path);
                } else{
                    //是文件夹
                    String[] filelist2 = readfile.list();
                    for (int j = 0; j < filelist2.length; j++) {
                        File readfile2 = new File(filepath + "/" + filelist[i]+"/"+filelist2[j]);
                        if (!readfile2.isDirectory()) {
                            //是文件
                            System.out.println("path=" + readfile.getPath());
                            String path = readfile.getAbsolutePath();
                            System.out.println("absolutepath="+ path);
                            files.add(path);
                        }
                    }

                }
            }
        }
        return files;
    }

    /**
     * 获取经纬度
     */
    public static List<String> APITest() throws IOException {
        String url = "http://api.map.baidu.com/place/v2/detail?uid=5a8fb739999a70a54207c130&output=json&scope=2&ak=oBMRTa88lr0op8aVAj2KtkfvfvSd6udu";
        //String url = "http://gc.ditu.aliyun.com/geocoding?a=石家庄";
        URL thisurl = new URL(url); // 把字符串转换为URL请求地址
        HttpURLConnection connection = (HttpURLConnection) thisurl
                .openConnection();// 打开连接
        connection.connect();// 连接会话
        // 获取输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "UTF-8"));
        String line;
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {// 循环读取流

            System.out.println("line:"+line);
           /* String[]  word = line.split(",");
            String jing = word[0].split(":")[1];
            String wei = word[5].split(":")[1].split("}")[0];
            list.add(jing);
            list.add(wei);
            System.out.println("jing:"+jing+"  wei:"+wei);*/
        }
        br.close();// 关闭流
        connection.disconnect();// 断开连接
        return list;
    }

    public static String connectURL(String dest_url, String commString) {
        String rec_string = "";
        URL url = null;
        HttpURLConnection urlconn = null;
        OutputStream out = null;
        BufferedReader rd = null;
        try {
            url = new URL(dest_url);
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setReadTimeout(1000 * 30);
            urlconn.setRequestMethod("POST");
            urlconn.setDoInput(true);
            urlconn.setDoOutput(true);
            out = urlconn.getOutputStream();
            out.write(commString.getBytes("UTF-8"));
            out.flush();
            out.close();
            rd = new BufferedReader(new InputStreamReader(urlconn.getInputStream(),"UTF-8"));
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = rd.read()) > -1)
                sb.append((char) ch);
            rec_string = sb.toString();
        } catch (Exception e) {

            return "";
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (urlconn != null) {
                    urlconn.disconnect();
                }
                if (rd != null) {
                    rd.close();
                }
            } catch (Exception e) {

            }
        }
        return rec_string;
    }



    public static String search(String jingdian,String city){
        String serverUrl = "http://api.map.baidu.com/place/v2/search?query="+jingdian+"&region="+city+"&output=json&ak=oBMRTa88lr0op8aVAj2KtkfvfvSd6udu";
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn;
            conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while((line = in.readLine()) != null){
                result.append(line);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    /**
     * 根据城市，查询游记
     */
    public static List<TravelBean1> TravelMonthCityList(int month,String city) throws Exception {
    //month=? and
        String sql = "select * from travel where city=?";
        List<TravelBean1> list = JDBCUtil.executeQuery(sql, new RowMap<TravelBean1>(){
            public TravelBean1 rowMapping(ResultSet rs) {
                TravelBean1 u = null;
                try {
                    u = new TravelBean1(rs.getInt("id"),rs.getString("title"),rs.getString("content"),rs.getString("city"),rs.getInt("month"),rs.getInt("day"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return u;
            }
        }, city);
        return list;
    }


/////////////////////////////////////////////////////////////
    //在JingdianTest.java中重新实现
    /**
     * 读取所有的景点，将他们放在一个文件中，然后把它作为词库
     * @throws IOException
     */
    @org.junit.Test
    public void jingdianFile() throws IOException {
        String savePath = "D:/data/jingdianAll4.txt";
        String path = "D:/data/jingdianAll_G";
        List<File> Filelist = new ArrayList<File>();
        getXmlFiles(path, Filelist);
        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(new File(savePath), true), "UTF-8");
        BufferedWriter bw = new BufferedWriter(write);
        for (File f : Filelist) {
            System.out.println(f.getPath());
            List<String> list = new ArrayList<String>();
            //读取文件
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }
            //将文件内容放到一个文件中
            for (String l : list) {
                bw.write(l + "\n");
            }
        }
        bw.flush();
        bw.close();
    }

    public static void getXmlFiles(String path, List<File> fileList) {
        File file = new File(path);
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileIndex : files) {
                if (!fileIndex.exists()) {
                    throw new NullPointerException("Cannot find " + fileIndex);
                } else if (fileIndex.isFile()) {
                    fileList.add(fileIndex);
                } else {
                    if (fileIndex.isDirectory()) {
                        getXmlFiles(fileIndex.getAbsolutePath(), fileList);
                    }
                }
            }
        }
    }
///////////////////////////////////////////////////////////////////////////

    @org.junit.Test
    public void test(){
        String line = " 重庆丽晶酒店";
        //分词对象
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        //分词
        List<Term> termList = segment.seg(line);
        System.out.println(termList);
    }
}
