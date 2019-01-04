package com.JingdianUtil;

import java.io.File;

/**
 * 重新爬取景点(按照一定条件，爬取景点频率较高的) 保存到本地目录中
 * 再根据景点查找百度地图中的的信息，存放到数据库
 * 查询每个城市中的所有的游记，分词，将ns的地点在百度地图中查询
 * 如果该地点在之前爬取的景点(百度地图的标准地点)中，则进行统计
 */
public class JingdianUtil {


    public static void main(String[] args) {
        //读取所有的景点
        //获得目录
        String dir = "D:/data/jingdianAll_G";
        File f = new File(dir);
        File[] fileList = f.listFiles();
        for(File file : fileList ){
            String city1 = file.getName();
            System.out.println(city1+"");
        }
        //百度地图查询景点，并将信息放入数据库


        //读取数据库游记
    }


}
