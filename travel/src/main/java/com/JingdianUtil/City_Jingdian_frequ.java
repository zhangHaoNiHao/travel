package com.JingdianUtil;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.Bean.JingdianNum;
import com.Dao.JingdianDao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;


/**
 * 从每个城市爬取的游记中统计
 * 每个城市的景点出现频率，计算景点的热门程度
 * 顺便找出每篇游记的路线图
 * @author Mr_Zhang
 *
 */
public class City_Jingdian_frequ {

	/**
	 * 1.将城市景点文件放到自定义词库中
	 * 2.读取每个城市的景点文件
	 * 3.根据景点名，找到每个城市的游记
	 * 4.读取游记和景点，根据分词，找到每个景点，并统计频率
	 * 5.还是之前的问题：游记中找到的词都是根据景点库找的，有的景点描述是一个景点，但是写的不一样，需要注意
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String travelpath = "D:/data/travelsAll3";
		String jingdianpath = "D:/data/jingdianAll2.txt";


		//游记文件地址集合
		// List<String> travelfile = readfile(travelpath); //getXmlFiles
		List<File> travelfile = new ArrayList<File>();
		getXmlFiles(travelpath,travelfile);
		//travelfile是所有游记文件

		//分词
		Segment segment = HanLP.newSegment().enablePlaceRecognize(true);

		// 存放地名的集合
		List<String> nameList = new ArrayList<String>();
		//存放地点名词的词频
		HashMap<String , Integer> map = new HashMap<String, Integer>();

		//读取网站中所有杭州的景点，到List集合中
		List<String> jingdians = new ArrayList<String>();
		FileInputStream input1 = new FileInputStream(new File(jingdianpath));
		BufferedReader reader1=new BufferedReader(new InputStreamReader(input1,"utf-8"));
		String jing  = reader1.readLine();
		while(jing != null){
			jingdians.add(jing);
			jing = reader1.readLine();
		}
		reader1.close();

		//读取目录下的所有关于杭州的游记，统计其中的景点
		//travelfile是所有游记文件
		for(File file : travelfile){
			FileInputStream input = new FileInputStream(file);
			BufferedReader reader=new BufferedReader(new InputStreamReader(input,"utf-8"));
			String line = reader.readLine();
			while(line != null){
				List<Term> termList = segment.seg(line);
		        System.out.println("分词:  "+termList);
				for (int i = 0; i < termList.size(); i++){
					String word = termList.get(i).word.toString();
					String nature = termList.get(i).nature.toString();

					// 5000条游记中的地点中，在杭州的地名
					//判断分词出来的地点是否在地点文件中，如果不存在不添加
					if(jingdians.contains(word)){
						//如果存在，直接进行统计
						/**
						 //先将所有的17万个景点在百度地图中找到各自的标准名称，
						 // 然后将分词出来的地点词再进行百度地图查询
						 //  看看两者的景点词是否相同
						 */

						
						OutputStreamWriter writeall = null ;
						//游记中的所有杭州景点，不记录词频，重复的都包含
				        writeall = new OutputStreamWriter(new FileOutputStream("D:/data/quanguojingdian.txt",true), "UTF-8");
						BufferedWriter bw2 = new BufferedWriter(writeall);
						bw2.write(word+ "\n");
						bw2.flush();
						bw2.close();
						
						
						/*
						//如果存在的话，再进行词频统计
						// 统计词频
						if(map.get(word) == null){ //如果不存在
							map.put(word, 1);
						}else{ //如果存在，直接加1
							int count = map.get(word);
							map.put(word,count+1);
						}						
						if(!nameList.contains(word)){
							nameList.add(word);
						}
						*/
					}
				}
		        line = reader.readLine();
			}
		}
		
		//System.out.println("地点: "+nameList.toString());
		//System.out.println(map.toString());
		//按值进行排序
		//SortMap(map);
		//将排序结果存放到文件中


		//输出词频最高的地点

		/*
		OutputStreamWriter writeall = null ;
		//游记中的所有杭州景点，不记录词频，重复的都包含
        writeall = new OutputStreamWriter(new FileOutputStream("D:/data/hangzhou_jingdian2.txt",true), "UTF-8");
		BufferedWriter bw2 = new BufferedWriter(writeall);
		bw2.write(word+ "\n");
		bw2.flush();
		bw2.close();
		*/
				
    }

	//按value的大小进行排序
    public static void SortMap(Map<String,Integer> oldmap) throws IOException{  
          
        ArrayList<Entry<String,Integer>> list = new ArrayList<Entry<String,Integer>>(oldmap.entrySet());
          
        Collections.sort(list,new Comparator<Entry<String,Integer>>(){
              
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {  
                return o2.getValue() - o1.getValue();  //降序
            }
        });  
        OutputStreamWriter write = null ;
        write = new OutputStreamWriter(new FileOutputStream("D:/data/hangzhou_jingdian_count2.txt",true), "UTF-8");
		BufferedWriter bw = new BufferedWriter(write);
		
		
        for(int i = 0; i<list.size(); i++){  
        	String key = list.get(i).getKey();
        	Integer count = list.get(i).getValue();
            System.out.println(key+ ": " +count);  
            bw.write(key+","+count + "\n");
        }     
        bw.flush();
		bw.close();
    }  
    @Test
    public void test() throws Exception{
    	System.out.println(readfile("D:/data/travelsAll"));
    }
    
    /**
     *获取目录中所有的文件地址
	 * 游记地址：D:/data/travelsAll
	 * 景点地址：D:/data/jingdianAll2.txt
     * @return
     * @throws Exception
     */  
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
					String path = readfile.getPath();
					files.add(path);
			    } else{
					//是文件夹
					 String[] filelist2 = readfile.list();
					 for (int j = 0; j < filelist2.length; j++) {
						 File readfile2 = new File(filepath + "/" + filelist[i]+"/"+filelist2[j]);
						 if (!readfile2.isDirectory()) {
							String path = readfile2.getPath();
							files.add(path);
						}
					 }
				 
			    }
			}
		}
		return files;
    }
    /**
     * 游记的文件夹：D:/data/travelsAll ，返回所有的游记文件
     * @param path
     * @param fileList
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


////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 读取游记目录，根据城市，找到对应的景点库，统计个数
	 * @return
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
					TravelJingdianNum(readfile);
				}
			}
		}
	}

	/**
	 * 将所有的景点通过百度地图接口转化为官方名称
	 * 读取游记，进行分词，如果有地名，就进行查找，转化为官方词。如果有官方词，则进行统计
	 */
	public static void fileJingdian(File path){
		String jigndiandir = "D:/data/jingdianAll";
		String path1 = jigndiandir+"/"+path.getName();
		String city = path.getName();
		List<String> jingdians = new ArrayList<>();
		for(String j:jingdians){
			String result = search(j,city);
			//解析出该景点的信息
			//?存到数据库
		}
		//读取所有的游记
		//根据路径，找到文件名(城市)
		//读取内容，查询景点的原名
		//将原名放到文件中、数据库中
		//读取本城市的游记
		//统计所有的景点个数
	}

	/**
	 * 每个城市的游记(根据每个城市的目录文件，找到城市名，通过城市名找到该城市的景点，然后再统计该城市的景点个数)
	 * path:一个城市的游记目录
	 */
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
            if(jingdians.size() > 0){
                //存放地点名词的词频
                HashMap<String , Integer> map = new HashMap<String, Integer>();
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
                }

                OutputStreamWriter write = null ;
                write = new OutputStreamWriter(new FileOutputStream("D:/data/hangzhou_jingdian_count2.txt",true), "UTF-8");
                BufferedWriter bw = new BufferedWriter(write);

                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    Integer value = entry.getValue();
                    String key = entry.getKey();
                    bw.write(city1+","+key+","+value+"\n");


                }
                bw.close();
                write.close();
                /*
                // 该城市的游记读取完之后
                // 将该城市的景点信息存放到数据库
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    Integer value = entry.getValue();
                    String key = entry.getKey();
                    //杭州、西湖、1000
                    //杭州、千岛湖、2000


                    String result = search(key,city1);
                    //解析景点信息、存到数据库
                    JSONObject json = JSON.parseObject(result);
                    JSONArray results = json.getJSONArray("results");
                    String name = "";
                    String province = "";
                    String city = "";
                    String area = "";
                    String address = "";
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
                        JingdianNum jingdianNum = new JingdianNum(key,name,province,city,city1,area,value,lng,lat);
                        System.out.println("jingdianNum对象："+jingdianNum.toString());
                        JingdianDao dao = new JingdianDao();
                        Boolean f = dao.addjingdianNum1(jingdianNum);
                        if(f){
                            System.out.println("添加成功");
                        }else{
                            System.out.println("添加失败");
                        }
                    }
                }*/ //景点文件存在

            }



		}

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
		//JSONObject json =JSONObject.fromObject();
		return result.toString();
	}
	
}
