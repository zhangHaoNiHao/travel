package com.JingdianUtil;

import com.Bean.JingdianNum;
import com.Bean.JingdianTravelId;
import com.Bean.ProvinceBean;
import com.Bean.TravelBean1;
import com.Dao.JingdianDao;
import com.Dao.TravelDao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.utils.JDBCUtil1;
import com.utils.RowMap;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.Dao.JingdianDao.AllJingdianNum;

public class JingdianTest {

    /*
    // 现将所有的景点转换成百度地图的景点，然后再对游记分词，找到地点，转成百度地图，再进行对比
    public static void main(String[] args) throws Exception {
        //读取所有的景点
        //获得目录
        String dir = "D:/data/jingdianAll_G";
        File f = new File(dir);
        File[] fileList = f.listFiles();
        for(File file : fileList ){
            String city1 = file.getName().split("\\.")[0];
            System.out.println(city1+"");

            List<String> jingdians = new ArrayList<>();
            BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            String jing = reader.readLine();
            while(jing != null){
                jingdians.add(jing);
                jing = reader.readLine();
            }
            for(String j : jingdians){
                String result = search(j,city1);
                JSONObject json = JSON.parseObject(result);
                JSONArray results = json.getJSONArray("results");
                System.out.println("city1:"+city1 +" 景点："+j+" results:"+results);
                String name = "";
                String province = "";
                String city = "";
                String area = "";
                String lng = "";
                String lat = "";

                if(null != results && results.size() > 0){
                    JSONObject item = results.getJSONObject(0);
                    System.out.println("item:"+item);
                    name = item.get("name").toString();
                    province = item.getString("province");
                    if(province == null){

                    }else{
                        city = item.getString("city");
                        area = item.getString("area");
                        if(item.getJSONObject("location") != null){
                            lng = item.getJSONObject("location").getString("lng");
                            lat = item.getJSONObject("location").getString("lat");
                        }
                        //加入数据库
                    }

                    System.out.println("name: "+name+" province:"+province+" city:"+city+" area:"+area+" lng:"+lng+" lat:"+lat);

                    //加入数据库  //景点,name,省，市，区，经纬度，数量，好感度
                    JingdianNum jingdianNum = new JingdianNum(j,name,province,city,city1,area,lng,lat);
                    System.out.println("jingdianNum对象："+jingdianNum.toString());
                    JingdianDao dao = new JingdianDao();
                    Boolean flag = dao.addjingdianNum1(jingdianNum);
                    if(flag){
                        System.out.println("添加成功");
                    }else{
                        System.out.println("添加失败");
                    }
                }
            }
        }
        //百度地图查询景点，并将信息放入数据库
        //读取数据库游记
    }
    */

    /**
     * 读取本地的游记目录，统计每个城市的景点数量
     * @throws Exception
     */
    @Test
    public void readfile1() throws Exception {
        String filepath =  "D:/data/travelsAll3";
        File file = new File(filepath);
        List<String> files = new ArrayList<String>();
        if (file.isDirectory()) {
            System.out.println("文件夹");
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "/" + filelist[i]);
                if (readfile.isDirectory()) {
                    //是文件夹
                    System.out.println("城市目录路径："+readfile);
                    System.out.println("城市："+readfile.getName());
                    TravelJingdianNum(readfile);
                }
            }
        }
    }
    /**
     * 每个城市的游记(根据每个城市的目录文件，找到城市名，通过城市名找到该城市的景点，然后再统计该城市的景点个数)
     * path:一个城市的游记目录，将每个景点、城市、数量
     */
    //在项目travel2中实现，JingdianTest.java
    public static void TravelJingdianNum(File path) throws IOException {
        //城市游记目录名
        String city1 = path.getName();
        System.out.println("城市："+city1);
        String jingdiandir = "D:/data/jingdianAll_G/"+city1+".txt";
        //读取景点内容
        List<String> jingdians = new ArrayList<String>();
        File jingdianFile = new File(jingdiandir);
        if(jingdianFile.exists()){//如果景点文件存在

            FileInputStream input1 = new FileInputStream(jingdianFile);
            BufferedReader reader1=new BufferedReader(new InputStreamReader(input1,"utf-8"));
            String jing  = reader1.readLine();
            while(jing != null){
                jingdians.add(jing);
                jing = reader1.readLine();
            }
            reader1.close();
            //存放地点名词的词频
            HashMap<String , Integer> map = new HashMap<String, Integer>();
            if(jingdians.size() > 0){

                //分词
                Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
                //读取游记目录中的所有游记
                File[] files = path.listFiles();
                for(File f : files){
                    BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf-8"));
                    String line = reader.readLine();
                    while(line != null){
                        if(!line.equals("")){
                            List<Term> termList = segment.seg(line);
                            //System.out.println("分词："+termList.toString());
                            for (int i = 0; i < termList.size(); i++) {
                                String word = termList.get(i).word.toString();
                                if(jingdians.contains(word)){
                                    //统计每个景点的频率
                                    if(map.get(word) == null){ //如果不存在
                                        map.put(word, 1);//加入
                                    }else{ //如果存在，直接加1
                                        int count = map.get(word);
                                        map.put(word,count+1);
                                    }
                                }
                            }
                        }
                        line = reader.readLine();
                    }
                    reader.close();
                }//读取完该城市目录下的所有游记
                OutputStreamWriter write = null ;
                write = new OutputStreamWriter(new FileOutputStream("D:/data/quanguojingdian.txt",true), "UTF-8");
                BufferedWriter bw = new BufferedWriter(write);
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    Integer value = entry.getValue();
                    String key = entry.getKey();
                    bw.write(city1+","+key+","+value+"\n");
                }
                bw.close();
                write.close();
            }
        }

    }
    /**已验证
     * 读取全国的景点文件，找到它们的详细信息，并存到数据库
     */
    @Test
    public void JingdianDetail() throws IOException {
        List<String> jingdians = new ArrayList<String>();
        FileInputStream input = new FileInputStream(new File("D:/data/quanguojingdian.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(input,"utf-8"));
        String line = reader.readLine();
        while(line != null){

            int num = Integer.parseInt(line.split(",")[2]);
            String city1 = line.split(",")[0];
            String jingdian = line.split(",")[1];

            String result = search(jingdian,city1);
            JSONObject json = JSON.parseObject(result);
            System.out.println("地点信息："+json.toString());
            JSONArray results = json.getJSONArray("results");

            String name = "";
            String province = "";
            String city = "";
            String area = "";
            String lng = "";
            String lat = "";
            if(null != results && results.size() > 0){
                JSONObject item = results.getJSONObject(0);
                name = item.get("name").toString();
                province = item.getString("province");
                city = item.getString("city");
                area = item.getString("area");
                if(item.getJSONObject("location") != null){
                    lng = item.getJSONObject("location").getString("lng");
                    lat = item.getJSONObject("location").getString("lat");
                }
                System.out.println("name: "+name);
                //加入数据库  //景点,name,省，市，区，经纬度，数量，好感度
                JingdianNum jingdianNum = new JingdianNum(jingdian,name,province,city,city1,area,num,lng,lat);

                JingdianDao dao = new JingdianDao();
                Boolean a = dao.addjingdianNum1(jingdianNum);
                if(a){
                    System.out.println("添加成功");
                }else{
                    System.out.println("添加失败");
                }
            }
            line = reader.readLine();
        }
        reader.close();
    }
    @Test
    public void Test(){
        System.out.println(search("玉龙山","重庆市"));
    }
    /**已验证
     * 利用百度地图的API查找景点的详细信息
     */
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
        //JSONObject json =JSONObject.fromObject();
        return result.toString();
    }
///////////////////////////////////////////////////////////////////////////////

    /**已验证
     * 读取所有的景点文件，放到一个文件中，作为词库
     * 注意删除所有的英文词
     */
    @Test  //将所有城市文件中的景点放到一个文件中，当做分词的词库
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

    /**已验证
     * 递归获取目录中的所有文件
     */
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


    /**已验证
     * 读取所有的美食，放到一个文件中，作为词库
     * 注意删除所有的英文词
     */
    @Test  //将所有城市文件中的景点放到一个文件中，当做分词的词库
    public void MeishiFile() throws IOException {
        String savePath = "D:/data/ciku/meishi.txt";
        String path = "D:/data/meishi";
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

    /**已验证
     * 读取所有的物品，放到一个文件中，作为词库
     * 注意删除所有的英文词
     */
    @Test  //将所有城市文件中的景点放到一个文件中，当做分词的词库
    public void WupinFile() throws IOException {
        String savePath = "D:/data/ciku/wupin.txt";
        String path = "D:/data/wupin";
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


    @Test
    public void Fenci(){
        //分词对象
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        String line = "今天天气真好，攻城狮体仁阁";
        List<Term> termList = segment.seg(line);
        System.out.println(termList.toString());
    }


    /**
     * 统计每个季节的景点数量
     * 1.查询所有的游记 //根据城市进行查询
     * 2.对游记进行分词
     * 3.统计每个景点的数量
     * 在travel2的JingdianTest中执行
     */
    public void SeasonJingdianNum() throws Exception {
        TravelDao dao = new TravelDao();
        //所有的市
        List<ProvinceBean> city1list = dao.allCity();
        for(ProvinceBean bean : city1list){
            //查询每个城市的游记
            String city1 = bean.getCity1();
            List<TravelBean1> travelBean1s = dao.cityTravels(city1);
            //获取该城市的所有景点
            // 1.读取该城市的景点文件
            String jingdiandir = "D:/data/jingdianAll_G/"+city1+".txt";
            List<String> jingdians = new ArrayList<String>();
            File jingdianFile = new File(jingdiandir);
            if(jingdianFile.exists()) {//如果景点文件存在
                FileInputStream input1 = new FileInputStream(jingdianFile);
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1, "utf-8"));
                String jing = reader1.readLine();
                while (jing != null) {
                    jingdians.add(jing);
                    jing = reader1.readLine();
                }
                reader1.close();
                //存放地点名词的词频
                HashMap<String , Integer> map = new HashMap<String, Integer>();
                if(jingdians.size() > 0){
                    // 2.对游记进行分词统计
                    Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
                    for(TravelBean1 travel : travelBean1s){
                        //int id = travel.getId();//在该景点出现过，该景点所在的城市的游记
                        String content = travel.getContent();
                        int month = travel.getMonth();

                        List<Term> termList = segment.seg(content.replaceAll(" ",""));
                        for (int i = 0; i < termList.size(); i++) {
                            String word = termList.get(i).word.toString();
                            if(jingdians.contains(word)){
                                //统计每个景点的频率
                                String key = word+","+month;
                                if(map.get(key) == null){ //如果不存在
                                    map.put(key, 1);//加入
                                }else{ //如果存在，直接加1
                                    int count = map.get(key);
                                    map.put(key,count+1);
                                }
                            }
                        }
                    }//每个城市的游记读取完
                    //每个城市写一次
                    OutputStreamWriter write = null ;
                    write = new OutputStreamWriter(new FileOutputStream("D:/data/quanguojingdian2.txt",true), "UTF-8");
                    BufferedWriter bw = new BufferedWriter(write);
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        Integer value = entry.getValue();
                        String key = entry.getKey();
                        bw.write(city1+","+key+","+value+"\n");
                    }
                    bw.close();
                    write.close();
                }
            }
        }
    }

    /**
     * 将上边统计的每个景点各个季节的数量存放到数据库
     */
    @Test
    public void saveSeasonJingdianNum() throws Exception {
        JingdianDao dao = new JingdianDao();
        File file = new File("D:/data/quanguojingdian2.txt");
        FileInputStream input1 = new FileInputStream(file);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1, "utf-8"));
        String line = reader1.readLine();
        int a = 1;
        while (line != null) {
            String city1 = line.split(",")[0];
            String jingdian = line.split(",")[1];
            int month = Integer.parseInt(line.split(",")[2]);
            int num = Integer.parseInt(line.split(",")[3]);
            boolean f = dao.updateNum(jingdian,num,month,city1);
            if(f){
                System.out.println("添加成功,第"+a+"行");
            }

            line = reader1.readLine();
            a++;
        }
        reader1.close();
    }


    /**
     * 将每个景点的链接存到数据库
     * 统计每个景点,在每个季度的数量
     */
    @Test
    public void photoTest() throws Exception {
        //1.从数据库获取所有的景点
        File file = new File("D:/data/photo.txt");
        FileInputStream input1 = new FileInputStream(file);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1, "utf-8"));
        String line = reader1.readLine();
        JingdianDao dao = new JingdianDao();

        int a = 1;
        while (line != null) {
            String[] text = line.split("\\[");
            String city1 = text[0].split(",")[0];
            String jingdian = text[0].split(",")[1];
            JingdianNum jingdianNum = dao.jingdianNumSearch(city1,jingdian);
            if(jingdianNum == null){
                jingdianNum = dao.jingdianNumSearch(jingdian);
            }
            if(jingdianNum != null && text.length ==2){
                String photo = text[1];
                System.out.println(jingdianNum.getId());
                boolean f = dao.updatePhoto(jingdianNum.getId(),photo);
                if(f){
                    System.out.println("添加成功");
                }
            }else{
                System.out.println("没有找到景点");
            }
            line = reader1.readLine();
        }
    }

    @Test
    public void test() throws Exception {
        /*
        String l = "sdfdsdfsd[kjjg";
        String[] s = l.split("\\[");
        System.out.println(s.length);
        */
        //System.out.println(search("平顶山","中国"));
        JingdianDao dao = new JingdianDao();
        JingdianNum jingdianNum = dao.jingdianNumSearch("xiamen","鼓浪屿");
        System.out.println(jingdianNum);
    }


    /**
     * 统计景点的图片
     * 1.根据城市查询游记
     * 2.获得城市和月份
     * 3.过滤出图片链接，判断下一句是否含有景点，如果有，则将保存到文件
     * 4.城市、景点、photo3,photo6,photo9,photo12
     */
    @Test
    public void photosMonth() throws Exception {
        //获得所有的城市拼音
        TravelDao dao = new TravelDao();
        JingdianDao jingdianDao = new JingdianDao();
        //分词
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        //所有的市
        List<ProvinceBean> city1list = dao.allCity();
        //System.out.println(dao.cityTravels(city1list.get(0).getCity1()).get(0).getContent().replaceAll("[\n\n]+","\n"));
        for(ProvinceBean bean : city1list) {
            //查询每个城市的游记
            String city1 = bean.getCity1();
            //读取景点
            String jingdiandir = "D:/data/jingdianAll_G/"+city1+".txt";
            List<String> jingdians = new ArrayList<String>();
            File jingdianFile = new File(jingdiandir);
            if(jingdianFile.exists()) {//如果景点文件存在
                FileInputStream input1 = new FileInputStream(jingdianFile);
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1, "utf-8"));
                String jing = reader1.readLine();
                while (jing != null) {
                    jingdians.add(jing);
                    jing = reader1.readLine();
                }
                reader1.close();

                if(jingdians.size()>0){
                    List<TravelBean1> travelBean1s = dao.cityTravels(city1);
                    for(TravelBean1 travel : travelBean1s){
                        String content = travel.getContent().replaceAll("[\n\n]+","\n");
                        int month = travel.getMonth();
                        String[] lines = content.split("\n");
                        String first = "";
                        String last = "";
                        for(int i=2;i<lines.length;i++){
                            first = lines[i-1];
                            System.out.println("first : "+lines[i-1]);
                            System.out.println("lines"+i+" : "+lines[i]);
                            if(i<= lines.length-2){
                                last = lines[i+1];
                                System.out.println("last : "+lines[i+1]);
                            }

                            if(lines[i].matches("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.jpg[a-zA-Z0-9\\?=]{0,30}")){//如果是图片链接
                                //如果这句话是图片，先判断下一句是否是短语，如果是就判断，如果不是，判断上一句中的景点，但是
                                if(last.length()<20){
                                    List<Term> termList = segment.seg(last);
                                    for (int j=0;j<termList.size();j++) {
                                        String word = termList.get(j).word.toString();
                                        if(jingdians.contains(word)){
                                            //城市、景点、photo3,photo6,photo9,photo12
                                            //根据城市和景点查找景点
                                            JingdianNum jingdian = jingdianDao.jingdianNumSearch(city1,word);
                                            if(jingdian != null){
                                                if(month>=3 && month<=5){
                                                    String photo3 = jingdian.getPhoto3();
                                                    photo3 += lines[i]+",";
                                                    jingdian.setPhoto3(photo3);
                                                }else if(month>=6 && month<=8){
                                                    String photo6 = jingdian.getPhoto6();
                                                    photo6 += lines[i]+",";
                                                    jingdian.setPhoto6(photo6);
                                                }else if(month>=9 && month<=11){
                                                    String photo9 = jingdian.getPhoto9();
                                                    photo9 += lines[i]+",";
                                                    jingdian.setPhoto9(photo9);
                                                }else if(month==12 || month<=2){
                                                    String photo12 = jingdian.getPhoto12();
                                                    photo12 += lines[i]+",";
                                                    jingdian.setPhoto12(photo12);
                                                }
                                                //将该景点的内容插入数据库，或者写入文件
                                                //city1,jingdian,photo3,
                                                boolean f = jingdianDao.InsertJingdianPhoto(jingdian);
                                                if(f){
                                                    System.out.println("添加成功");
                                                }
                                            }
                                        }
                                    }
                                }
                                else {
                                    //判断上一句话中主要的景点
                                    List<Term> termList = segment.seg(first);
                                    HashMap<String,Integer> jing1 = new HashMap<String,Integer>();
                                    for (int j=termList.size()-1;j>=0;j--) {
                                        String word = termList.get(j).word.toString();
                                        if(jingdians.contains(word)){
                                            if(jing1.get(word) == null){
                                                jing1.put(word,1);
                                            }else{
                                                jing1.put(word,jing1.get(word)+1);
                                            }
                                        }
                                    }
                                    String key1 = More(jing1);
                                    System.out.println("关键景点："+key1);
                                    JingdianNum jingdian = jingdianDao.jingdianNumSearch(city1,key1);
                                    if(jingdian != null){
                                        if(month>=3 && month<=5){
                                            String photo3 = jingdian.getPhoto3();
                                            photo3 += lines[i]+",";
                                            jingdian.setPhoto3(photo3);
                                        }else if(month>=6 && month<=8){
                                            String photo6 = jingdian.getPhoto6();
                                            photo6 += lines[i]+",";
                                            jingdian.setPhoto6(photo6);
                                        }else if(month>=9 && month<=11){
                                            String photo9 = jingdian.getPhoto9();
                                            photo9 += lines[i]+",";
                                            jingdian.setPhoto9(photo9);
                                        }else if(month==12 || month<=2){
                                            String photo12 = jingdian.getPhoto12();
                                            photo12 += lines[i]+",";
                                            jingdian.setPhoto12(photo12);
                                        }
                                        //将该景点的内容插入数据库，或者写入文件
                                        //city1,jingdian,photo3,
                                        boolean f = jingdianDao.InsertJingdianPhoto(jingdian);
                                        if(f){
                                            System.out.println("添加成功");
                                        }
                                    }

                                }

                            }

                        }


                    }
                }
            }
        }
    }


    @Test
    public void test1() throws Exception {
        /*String url = "https://dimg08.c-ctrip.com/images/100u0z000000n3nblAA9D_R_1024_10000_Q90.jpg?sddss=sdsd";
        if(url.contains("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.jpg")){//http(s)?://[a-zA-Z0-9/#\.\?=_-]+\.jpg  //http(s)?://[a-zA-Z0-9/#\.\?=_-]+\.jpg
            System.out.println("匹配");
        }else if(url.matches("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.jpg[a-zA-Z0-9\\?=]{0,30}")){
            System.out.println("mathc");
        }*/

        /*
        String str = "sss|xxx|xxx||cccc|vvvv||||bbbb";
        String str2 = str.replaceAll("[||]+","|");
        System.out.println(str2);
        */
        JingdianDao dao = new JingdianDao();
        JingdianNum jingdian = dao.GetJingdainNumById(3);

        System.out.println(jingdian.getPhoto3().replaceAll("null",""));
    }

    /**
     * 找到hashmap中最多的景点
     */
    public String More(HashMap<String,Integer> map){
        String key1 = "";
        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Integer value = entry.getValue();
            String key = entry.getKey();
            if(entry.getValue() > i){
                i = value;
                key1 = key;
            }
        }
        return key1;
    }

    /**
     * 查询所有的景点，将景点链接中的null去掉，再修改
     */
    @Test
    public void updatephotoMonthnull() throws Exception {
        JingdianDao dao = new JingdianDao();
        List<JingdianNum> jingdians = AllJingdianNum();
        for(JingdianNum jingdian : jingdians){

            String photo3 = jingdian.getPhoto3();
            if(photo3 != null){
                photo3 = photo3.replaceAll("null","");
                jingdian.setPhoto3(photo3);
            }
            String photo6 = jingdian.getPhoto6();
            if(photo6 != null){
                photo6 = photo6.replaceAll("null","");
                jingdian.setPhoto6(photo6);
            }
            String photo9 = jingdian.getPhoto9();
            if(photo9 != null){
                photo9 = photo9.replaceAll("null","");
                jingdian.setPhoto9(photo9);
            }
            String photo12 = jingdian.getPhoto12();
            if(photo12 != null){
                photo12 = photo12.replaceAll("null","");
                jingdian.setPhoto12(photo12);
            }

            boolean f = dao.InsertJingdianPhoto(jingdian);
            if(f){
                System.out.println("修改成功");
            }

        }

    }

    @Test
    public void test2() throws Exception {
        /*
        List<String> str = new ArrayList<>();
        str.add("zhang");
        str.add("hao");
        str.add("ni");
        str.add("hao");
        for(int i=0;i<5;i++){
            File wupinnumfile = new File("D:/data/travelnum/test.txt");
            OutputStreamWriter write4 = new OutputStreamWriter(new FileOutputStream(wupinnumfile,true), "UTF-8");
            BufferedWriter bw4 = new BufferedWriter(write4);
            String content = "";
            for(String l : str){
                content += l+"%";
            }
            bw4.write(content+"\n");
            bw4.flush();
        }*/
        String t = "beijing,1107,";
        for(String l : t.split(","))
            System.out.println("1::"+l);


    }
    @Test
    public void TongjiNumMeishi() throws Exception {
        //城市、游记id、美食。。。、
        //通过美食，找到关于该美食的所有游记和内容
        File meishi_idFile = new File("D:/data/travelnum/meishi_id.txt");
        HashMap<String,String> meishiTravlId = new HashMap<>();
        FileInputStream input2 = new FileInputStream(meishi_idFile);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2, "utf-8"));
        String line = reader2.readLine();
        while (line != null) {
            String[] linetext = line.split(",");
            String city1 = linetext[0];
            String Travelid = linetext[1];
            if(linetext.length==3){
                String jingline = linetext[2];
                String[] meishis = jingline.split("\\|");
                for(int x=0;x<meishis.length;x++){
                    System.out.println(meishis[x]);
                    if(meishiTravlId.get(city1+","+meishis[x]) == null){
                        meishiTravlId.put(city1+","+meishis[x],Travelid);
                    }else{
                        meishiTravlId.put(city1+","+meishis[x],meishiTravlId.get(city1+","+meishis[x])+","+Travelid);
                    }
                }

            }
            line = reader2.readLine();
        }
        reader2.close();

        //city1,meishi,travelId //插入数据库
        for (Map.Entry<String, String> entry : meishiTravlId.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();

            String[] keys = key.split(",");
            if(keys.length == 2){
                String city1 = keys[0];
                String meishi = keys[1];
                boolean f = insertMeishi(meishi,city1,value);
                if(f){
                    System.out.println("添加成功");
                }
            }
        }

    }

    public static boolean insertMeishi(String meishi,String city1,String travelids){
        String sql = "insert into meishitravelid(meishi,city1,travelids) values(?,?,?)";
        int a = JDBCUtil1.executeUpdate(sql, meishi, city1, travelids);
        boolean f = false;
        if(a>0)
            f = true;
        return f;
    }
    @Test
    public void TongjiNumWupin() throws Exception {
        //城市、游记id、物品。。。
        File wupin_idFile = new File("D:/data/travelnum/wupin_id.txt");
        HashMap<String,String> wupinTravlId = new HashMap<>();
        FileInputStream input2 = new FileInputStream(wupin_idFile);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2, "utf-8"));
        String line = reader2.readLine();
        while (line != null) {
            String[] linetext = line.split(",");
            String city1 = linetext[0];
            String Travelid = linetext[1];
            if(linetext.length==3){
                String jingline = linetext[2];
                System.out.println("ll:::"+jingline);
                String[] wupins = jingline.split("\\|");
                System.out.println("dd:::"+wupins[0]);
                for(int x=0;x<wupins.length;x++){
                    if(wupinTravlId.get(city1+","+wupins[x]) == null){
                        wupinTravlId.put(city1+","+wupins[x],Travelid);
                    }else{
                        wupinTravlId.put(city1+","+wupins[x],wupinTravlId.get(city1+","+wupins[x])+","+Travelid);
                    }
                }
            }
            line = reader2.readLine();
        }
        reader2.close();

        //city1,wupin,travelId //插入数据库
        for (Map.Entry<String, String> entry : wupinTravlId.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();

            String[] keys = key.split(",");
            if(keys.length == 2){
                String city1 = keys[0];
                String wupin = keys[1];
                boolean f = insertWupin(wupin,city1,value);
                if(f){
                    System.out.println("添加成功");
                }
            }
        }

    }
    public static boolean insertWupin(String wupin,String city1,String travelids){
        String sql = "insert into wupintravelid(wupin,city1,travelids) values(?,?,?)";
        int a = JDBCUtil1.executeUpdate(sql, wupin, city1, travelids);
        boolean f = false;
        if(a>0)
            f = true;
        return f;
    }

    /**
     * 统计每篇游记的景点,可以根据景点查询出关于该景点的所有的游记id
     */
    @Test
    public void TongjiNumJingdian() throws IOException {
        //城市、游记id、景点。。。
        //景点、城市，关于该景点的所有游记id
        //通过景点，找到关于该景点的所有游记
        HashMap<String,String> jingdianTravelId = new HashMap<String,String>();
        //每篇游记里的景点
        File jingdian_idFile = new File("D:/data/travelnum/jingdian_id.txt");
        FileInputStream input1 = new FileInputStream(jingdian_idFile);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1, "utf-8"));
        String line = reader1.readLine();
        while (line != null) {
            String[] linetext = line.split(",");
            String city1 = linetext[0];
            String Travelid = linetext[1];
            if(linetext.length==3){
                String jingline = linetext[2];
                String[] jingdians = jingline.split("\\|");
                for(int x=0;x<jingdians.length;x++){
                    if(jingdianTravelId.get(city1+","+jingdians[x]) == null){
                        jingdianTravelId.put(city1+","+jingdians[x],Travelid);
                    }else{
                        jingdianTravelId.put(city1+","+jingdians[x],jingdianTravelId.get(city1+","+jingdians[x])+","+Travelid);
                    }
                }
            }
            line = reader1.readLine();
        }
        reader1.close();

        for (Map.Entry<String, String> entry : jingdianTravelId.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();

            String[] keys = key.split(",");
            if(keys.length == 2){
                String city1 = keys[0];
                String jingdian = keys[1];
                boolean f = insertJingdian(jingdian,city1,value);
                if(f){
                    System.out.println("添加成功");
                }
            }
        }



        //表： 城市，游记id，物品





        //每个城市(省)热门商品、热门景点、热门美食
        //相对应的每个商品、景点、美食的内容，并且进行打分，给出满意度
        //找出最热门的商品、景点、美食、构建路线图

        //通过美食，找到美食对应的游记，美食的内容
        //通过城市、景点，找到对应的游记和内容
        //通过物品，找到对应的游记和内容

    }
    //数据库保存
    public static boolean insertJingdian(String jingdian,String city1,String travelids){
        String sql = "insert into jingdiantravelid(jingdian,city1,travelids) values(?,?,?)";
        int a = JDBCUtil1.executeUpdate(sql, jingdian, city1, travelids);
        boolean f = false;
        if(a>0)
            f = true;
        return f;
    }

    @Test
    public void test11() throws Exception {
        TongjiNumWupin();
        TongjiNumMeishi();
    }

    /**
     * 将上边统计的景点对应的游记id进行修改
     * 删除城市与景点不对应的记录
     * 比如  beijing 周口店   tianjin 周口店
     */
    @Test
    public void updateJingdianTravelId() throws Exception {
        JingdianDao dao = new JingdianDao();
        //所有的JingdianTravelids
        List<JingdianTravelId> travelIds = AllJingdianTravelids();
        for(JingdianTravelId travelId : travelIds){
            String jingdian = travelId.getJingdian();
            String city1 = travelId.getCity1();
            int id = travelId.getId();
            JingdianNum jingdianNum = dao.jingdianNumSearch(city1, jingdian);
            if(jingdianNum == null){
                //删除
                deleteJingdianTravelid(id);
            }
        }
    }

    /**
     * 获得所有，判断他的景点城市是否相同，不相同就删除
     */
    public static List<JingdianTravelId> AllJingdianTravelids() throws Exception {
        String sql = "select * from jingdianTravelid";
        List<JingdianTravelId> travelIds = JDBCUtil1.executeQuery(sql, new RowMap<JingdianTravelId>() {
            public JingdianTravelId rowMapping(ResultSet rs) throws SQLException {
                JingdianTravelId travelId = new JingdianTravelId(rs.getInt("id"),rs.getString("jingdian"),rs.getString("city1"));
                return travelId;
            }
        });
        return travelIds;
    }

    /**
     * 根据id删除不合适的JingdianTravelid
     */
    public static boolean deleteJingdianTravelid(int id){
        String sql = "delete from jingdianTravelid where id=?";
        int a = JDBCUtil1.executeUpdate(sql,id);
        boolean f = false;
        if(a>0){
            f = true;
        }
        return f;
    }

    //获得景点和城市的集合
    public static HashMap<String,String> jingdianCity() throws SQLException {
        String sql = "select * from jingdiannum1";
        Connection conn = null;
        conn = JDBCUtil1.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        HashMap<String,String> jingdians = new HashMap<>();
        rs = pstmt.executeQuery();
        while (rs.next()){
            String jingdian = rs.getString("jingdian");
            String city1 = rs.getString("city1");
            jingdians.put(jingdian,city1);
        }
        JDBCUtil1.Close(conn,pstmt,rs);
        return jingdians;
    }

    @Test
    public void test222() throws IOException {
        TongjiMeishiNum();
        TongjiWupinNum();
    }
    /**
     * 统计每个城市美食的数量，并存到数据库
     */
    @Test
    public void TongjiMeishiNum() throws IOException {
        //城市、美食、数量
        File meishi_numFile = new File("D:/data/travelnum/meishi_num.txt");
        FileInputStream input1 = new FileInputStream(meishi_numFile);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1, "utf-8"));
        String line = reader1.readLine();
        while (line != null) {
            String[] text = line.split(",");
            String city1 = text[0];
            String meishi = text[1];
            int num = Integer.parseInt(text[2]);
            boolean f = saveMeishiNum(meishi,city1,num);
            if(f){
                System.out.println("保存成功");
            }
            line = reader1.readLine();
        }
    }
    //保存美食数量
    public static boolean saveMeishiNum(String meishi,String city1,int num){
        String sql = "insert into meishinum(meishi,city1,num) values(?,?,?)";
        int a = JDBCUtil1.executeUpdate(sql,meishi,city1,num);
        boolean f = false;
        if(a > 0){
            f = true;
        }
        return f;
    }


    /**
     * 统计每个城市物品的数量，并存到数据库
     */
    @Test
    public void TongjiWupinNum() throws IOException {
        //城市、物品、数量
        File wupin_numFile = new File("D:/data/travelnum/wupin_num.txt");
        FileInputStream input1 = new FileInputStream(wupin_numFile);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1, "utf-8"));
        String line = reader1.readLine();
        while (line != null) {
            String[] text = line.split(",");
            String city1 = text[0];
            String wupin = text[1];
            int num = Integer.parseInt(text[2]);
            boolean f = saveWupinNum(wupin,city1,num);
            if(f){
                System.out.println("保存成功");
            }
            line = reader1.readLine();
        }
    }
    //保存物品数量
    public static boolean saveWupinNum(String wupin,String city1,int num){
        String sql = "insert into wupinnum(wupin,city1,num) values(?,?,?)";
        int a = JDBCUtil1.executeUpdate(sql,wupin,city1,num);
        boolean f = false;
        if(a > 0){
            f = true;
        }
        return f;
    }

    @Test
    public void test333(){
        List<Integer> aaa = new ArrayList<>();
        aaa.add(0);aaa.add(1);aaa.add(2);aaa.add(3);aaa.add(4);aaa.add(5);aaa.add(6);aaa.add(7);aaa.add(8);aaa.add(9);
        aaa.add(10);aaa.add(11);aaa.add(12);aaa.add(13);aaa.add(14);aaa.add(15);aaa.add(16);aaa.add(17);aaa.add(18);
        aaa.add(19);aaa.add(20);aaa.add(21);aaa.add(22);aaa.add(23);aaa.add(24);aaa.add(25);aaa.add(26);aaa.add(27);aaa.add(28);aaa.add(29);

        List<Integer> b = new ArrayList<>();
        int s = 3;

        int r = 1;
        b = aaa.subList((r-1)*10,r*10);
        System.out.println(b);

        r++;
        System.out.println(aaa.subList((r-1)*10,r*10));

        r++;
        if(r == 3){
            System.out.println(aaa.subList((r-1)*10,aaa.size()));
        }

    }


    /**
     * 修改每个城市的经纬度
     * @throws Exception
     */
    @Test
    public void test3333() throws Exception {
        TravelDao dao = new TravelDao();
        List<ProvinceBean> provinceBeans = dao.allCityDetail();
        for(ProvinceBean bean : provinceBeans){
            String result = search(bean.getCity(), bean.getProvince());
            JSONObject json = JSON.parseObject(result);
            System.out.println("地点信息："+json.toString());
            JSONArray results = json.getJSONArray("results");

            if(null != results && results.size() > 0){
                JSONObject item = results.getJSONObject(0);
                if(item.getJSONObject("location") != null){
                    String lng = item.getJSONObject("location").getString("lng");
                    String lat = item.getJSONObject("location").getString("lat");
                    bean.setLng(lng);bean.setLat(lat);
                    boolean f = dao.UpdateProvince(bean);
                    if(f)
                        System.out.println("修改成功");
                    else
                        System.out.println("修改失败");
                }

            }
        }
    }


}
