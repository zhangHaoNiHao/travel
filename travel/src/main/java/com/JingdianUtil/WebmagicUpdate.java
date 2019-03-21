package com.JingdianUtil;

import com.Bean.*;
import com.Dao.*;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.utils.qingganUtils;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 爬虫更新
 */
public class WebmagicUpdate {

    /**
     * 统计景点的图片
     * 1.根据城市查询游记
     * 2.获得城市和月份
     * 3.过滤出图片链接，判断下一句是否含有景点，如果有，则将保存到文件
     * 4.城市、景点、photo3,photo6,photo9,photo12
     */


    public void photosMonthUpdate(String city1,TravelBean1 travel) throws Exception {
        //获得所有的城市拼音
        TravelDao dao = new TravelDao();
        JingdianDao jingdianDao = new JingdianDao();
        //分词
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        //读取景点
        String jingdiandir = "D:/data/jingdianAll_G/"+city1+".txt";
        List<String> jingdians = new ArrayList<String>();
        File jingdianFile = new File(jingdiandir);
        if(jingdianFile.exists()) {  //如果景点文件存在
            FileInputStream input1 = new FileInputStream(jingdianFile);
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1, "utf-8"));
            String jing = reader1.readLine();
            while (jing != null) {
                jingdians.add(jing);
                jing = reader1.readLine();
            }
            reader1.close();

            if(jingdians.size()>0){
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
     * 读取游记中的景点、美食、特产的内容
     */
    public static void Content(Segment segment,TravelBean1 travel,List<String> jingdians,List<String> wupins,List<String> meishis) throws IOException {
        //该城市的每一篇游记
        String content = travel.getContent().replaceAll("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+","").replaceAll("[\n\n]+","\n");//.replaceAll("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+","");
        int month = travel.getMonth();
        int id = travel.getId();
        String city1 = travel.getCity();
        //独立句子
        List<String> duli = new ArrayList<>();
        //记录  景点，景点描述
        HashMap<String,List<String>> map = new HashMap<String,List<String>>();
        //记录美食句子
        HashMap<String,String> meishiLines = new HashMap<String,String>();
        //记录美食句子
        HashMap<String,String> wupinLines = new HashMap<String,String>();

        //记录每个景点出现的最新的行数，景点
        HashMap<String,Integer> jingdianNum = new HashMap<String,Integer>();
        String[] lines = content.split("\n");
        String first = lines[1];
        for(int i=1;i<lines.length;i++){

            System.out.println("line:"+lines[i]);
            //统计每行的地点出现频度
            HashMap<String , Integer> linemap = new HashMap<String, Integer>();
            //分词
            List<Term> termList = segment.seg(lines[i]);

            for (int j=0;j<termList.size();j++){
                String word = termList.get(j).word.toString();//分词
                if(jingdians.contains(word)){//提取该游记中所有的景点
                    //统计每行出现的地点频度
                    if(null == linemap.get(word) ){ //如果不存在
                        linemap.put(word, 1);//加入
                    }else{ //如果存在，直接加1
                        int count = linemap.get(word);
                        linemap.put(word,count+1);
                    }
                }
                if(wupins.contains(word)){
                    if(null == wupinLines.get(word)){
                        wupinLines.put(word,lines[i]);
                    }else{
                        wupinLines.put(word,wupinLines.get(word)+"||"+lines[i]);
                    }

                }
                if(meishis.contains(word)){
                    if(null == meishiLines.get(word)){
                        meishiLines.put(word,lines[i]);
                    }else{
                        meishiLines.put(word,wupinLines.get(word)+"||"+lines[i]);
                    }

                }
            }
            //每行出现最多的景点
            String key1 ="";
            //找到每行出现最多的地点,如果每行出现的景点超过4个，将改行忽略
            if(linemap.size() >= 6){
                System.out.println("地点超过6，独立");
                duli.add(lines[i]);
            }
            else { //景点为空或少于三个
                Integer c = 0;
                //找到每行出现频率最大的景点
                for (Map.Entry<String, Integer> entry : linemap.entrySet()) {
                    Integer value = entry.getValue();
                    String key = entry.getKey();
                    if (entry.getValue() > c) {
                        c = value;
                        key1 = key;
                    }
                }
                System.out.println("  景点key1：" + key1 + " line:" + lines[i]);

                //如果该行没有出现地点，判断该句子的上下依存关系
                if ("".equals(key1)) {
                    System.out.println("景点词为空");

                    //景点为空，判断独立还是依存
                    System.out.println("first：" + first + "  line:" + lines[i]);
                    //解析相似度
                    Double score = qingganUtils.xiangsiduUtil(first, lines[i]);
                    if (score > 0.35) {
                        System.out.println("向上依存");
                        //放到最新的集合
                        //判断first字符串在哪个
                        //如果找到map中找到first，flag为true
                        Boolean flag = false;
                        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                            List<String> value = entry.getValue();
                            String key = entry.getKey();
                            if (value.contains(first)) {
                                System.out.println(key + " 景点中有上一句" + first + " ;" + lines[i] + "，所以添加到该集合中");
                                value.add(lines[i]);
                                map.put(key, value);
                                flag = true;
                                break;
                            }
                        }
                        //如果上一句话不在景点map中，在独立句子中，就将该句子放到独立集合中
                        if (flag == false && duli.contains(first)) {
                            System.out.println("first为独立，也把该依存句子放到独立中");
                            duli.add(lines[i]);
                        }
                    } else {//景点为空，且跟上一句话相似度小于0.45
                        System.out.println("该句子独立");
                        duli.add(lines[i]);
                    }

                } else {//如果景点不为空，肯定是杭州的景点

                    //如果景点已经存在，则放到已存在的里边，但是上一句话要在这里边
                    //如果上一句在该集合，并且该句话中存在上句话的景点，就把该句话添加进去

                    if (map.get(key1) != null) {//该景点已存在
                        // 记录每个景点出现的行数，再遇到景点，判断行数
                        System.out.println("景点已存在，");

                        //如果景点已存在，就将现在的行数与上次的行数进行比较,如果小于某个值，就循环覆盖
                        Integer num = 0;
                        //找到已存在的景点出现的行数
                        num = jingdianNum.get(key1); //千湖岛 第36行 ，
                        //如果上次出现的行数，与本次出现的行数相差小于5
                        if ((i - num) < 10) { //找到上一个景点的句子，将后边的删除
                            System.out.println("两个景点相差小于5行");
                            //两个景点 中间的内容
                            List<String> newlines = new ArrayList<String>();

                            //读取游记，获取第36行
                            String latest = lines[num];
                            for (int x = num - 1; x <= i; x++) {
                                newlines.add(lines[x]);
                            }

                            System.out.println("上一个含有该景点的行是" + latest);
                            System.out.println("两个景点之间的内容是：" + newlines.toString());

                            //将map该景点的集合中，在该行后边的都删除
                            //景点信息集合
                            List<String> value = map.get(key1);
                            int shan = -1;
                            int size = value.size();
                            for (int x = 0; x < size; x++) {
                                if (shan == -1) {
                                    if (value.get(x).equals(latest)) {
                                        shan = x;
                                    }
                                }
                                if (shan != -1) {
                                    value.remove(shan);
                                }
                            }
                            System.out.println("删除中间内容之后：" + value);
                            //合并景点集合
                            value.addAll(newlines);
                            System.out.println("开始内容与中间内容合并：" + value.toString());
                            map.put(key1, value);

                            //更新该景点的行数
                            jingdianNum.put(key1, i);
                        } else if ((!"".equals(first)) && map.get(key1).contains(first)) {
                            //景点已存在，且上一句话在就在该景点
                            System.out.println("景点已存在，该句子含有景点，且上first在上一个集合，依存景点集合");
                            List<String> vall = map.get(key1);
                            vall.add(lines[i]);
                            map.put(key1, vall);
                        } else { //景点已存在，但是不连续，则判为独立
                            //例如一段都 是再写千岛湖，后一句写西湖，再一句又提到千岛湖
                            List<String> vall = map.get(key1);
                            vall.add(lines[i]);
                            map.put(key1, vall);
                            System.out.println("景点存在，但上一句话不在该集合中，判为独立");
                            duli.add(lines[i]);
                        }
                    } else { //景点还不存在
                        System.out.println("景点不存在，添加map");
                        List<String> value = new ArrayList<String>();
                        value.add(lines[i]);
                        map.put(key1, value);
                        //记录每个景点的最新行数
                        jingdianNum.put(key1, i);
                    }
                }
            }
            first = lines[i];
        }

        JingdianContentDao dao = new JingdianContentDao();
        MeishiContentDao meishiContentDao = new MeishiContentDao();
        WupinContentDao wupinContentDao = new WupinContentDao();

        //将该篇游记的内容写到文件，可追加
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            List<String> value = entry.getValue();
            String key = entry.getKey();
            JingdianContentBean contentt = new JingdianContentBean(key,city1,value.toString(),id,month);
            dao.addJingdianContent(contentt);
        }


        //将该篇游记的美食内容写到文件，可追加
        for (Map.Entry<String, String> entry : meishiLines.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            MeishiContentBean contentt = new MeishiContentBean(key,city1,value.toString(),id,month);
            meishiContentDao.addJingdianContent(contentt);
        }

        //将该篇游记的物品内容写到文件，可追加
        for (Map.Entry<String, String> entry : wupinLines.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            WupinContentBean contentt = new WupinContentBean(key,city1,value.toString(),id,month);
            wupinContentDao.addWupinContent(contentt);
            //统计每个景点的游记

        }
    }


    //抽取景点、美食、特产的图片
    public void photosMonthUpdate1(String city1,TravelBean1 travel,List<String> jingdians,List<String> wupins,List<String> meishis) throws Exception {
        //获得所有的城市拼音
        TravelDao dao = new TravelDao();
        JingdianDao jingdianDao = new JingdianDao();
        MeishiDao meishiDao = new MeishiDao();
        WupinDao wupinDao = new WupinDao();
        //分词
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        //读取景点
        if(jingdians.size()>0){
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
                                if(meishis.contains(word)){
                                    //城市、食物、photos
                                    //根据城市和景点查找景点
                                    MeishiBean meishiBean = meishiDao.getMeishi(city1,word);
                                    if(meishiBean != null){
                                        String photos = meishiBean.getPhotos();
                                        photos += lines[i]+",";
                                        meishiBean.setPhotos(photos);

                                        //将该景点的内容插入数据库，或者写入文件
                                        //city1,jingdian,photo3,
                                        boolean f = meishiDao.UpdateMeishiPhotos(meishiBean);
                                        if(f){
                                            System.out.println("添加成功");
                                        }
                                    }
                                }
                                if(wupins.contains(word)){
                                    //城市、食物、photos
                                    //根据城市和景点查找景点
                                    WupinBean wupinBean = wupinDao.getWupin(city1,word);
                                    if(wupinBean != null){
                                        String photos = wupinBean.getPhotos();
                                        photos += lines[i]+",";
                                        wupinBean.setPhotos(photos);

                                        //将该景点的内容插入数据库，或者写入文件
                                        //city1,jingdian,photo3,
                                        boolean f = wupinDao.UpdateWupinPhotos(wupinBean);
                                        if(f){
                                            System.out.println("添加成功");
                                        }
                                    }

                                }
                            }
                        }
                    }
                    else if(!first.matches("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.jpg[a-zA-Z0-9\\?=]{0,30}")){
                        //判断上一句话中主要的景点
                        List<Term> termList = segment.seg(first);
                        HashMap<String,Integer> mei = new HashMap<String,Integer>();
                        HashMap<String,Integer> wu = new HashMap<String,Integer>();
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
                            if(meishis.contains(word)){
                                if(mei.get(word) == null){
                                    mei.put(word,1);
                                }else{
                                    mei.put(word,mei.get(word)+1);
                                }
                            }
                            if(wupins.contains(word)){
                                if(wu.get(word) == null){
                                    wu.put(word,1);
                                }else{
                                    wu.put(word,wu.get(word)+1);
                                }
                            }
                        }
                        String key1 = More(jing1);
                        String key3 = More(mei);
                        String key2 = More(wu);
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
                        MeishiBean meishiBean = meishiDao.getMeishi(city1,key3);
                        if(meishiBean != null){
                            String photos = meishiBean.getPhotos();
                            photos += lines[i]+",";
                            meishiBean.setPhotos(photos);
                            //将该景点的内容插入数据库，或者写入文件
                            //city1,jingdian,photo3,
                            boolean f = meishiDao.UpdateMeishiPhotos(meishiBean);
                            if(f){
                                System.out.println("添加成功");
                            }
                        }
                        WupinBean wupinBean = wupinDao.getWupin(city1,key2);
                        if(wupinBean != null){
                            String photos = wupinBean.getPhotos();
                            photos += lines[i]+",";
                            wupinBean.setPhotos(photos);

                            //将该景点的内容插入数据库，或者写入文件
                            //city1,jingdian,photo3,
                            boolean f = wupinDao.UpdateWupinPhotos(wupinBean);
                            if(f){
                                System.out.println("添加成功");
                            }
                        }
                    }
                }
            }
        }
    }

    //统计景点的数量
    public void SeasonJingdianNum(Segment segment,List<String> cityJingdians,TravelBean1 travel) throws Exception {
        //存放地点名词的词频
        HashMap<String , Integer> map = new HashMap<String, Integer>();
        JingdianDao jingdianDao = new JingdianDao();
        if(cityJingdians.size()>0){

            String content = travel.getContent().replaceAll("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+","");
            int month = travel.getMonth();
            String city1 = travel.getCity();
            List<Term> termList = segment.seg(content.replaceAll(" ",""));
            for (int i = 0; i < termList.size(); i++) {
                String word = termList.get(i).word.toString();
                if(cityJingdians.contains(word)){
                    //统计每个景点的频率
                    if(map.get(word) == null){ //如果不存在
                        map.put(word, 1);//加入
                    }else{ //如果存在，直接加1
                        int count = map.get(word);
                        map.put(word,count+1);
                    }
                }
            }
            //保存 根据城市和景点找到该景点，然后修改各个季节的数量
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                Integer value = entry.getValue();
                String key = entry.getKey();
                jingdianDao.updateNumWebMagic(key,value,month,city1);
            }

        }
    }

    //修改物品的评分
    public void UpdateWupinScore() throws Exception {
        //查询物品信息
        WupinContentDao dao = new WupinContentDao();
        List<WupinContentBean> wupin = dao.getWupin();
        //打分
        for (WupinContentBean content : wupin){
            String str = content.getContent();
            double v = qingganUtils.qingganUtil(str);
            String score = ""+v;
            //修改
            dao.updateWupinScore(content.getId(),score);
        }
    }

    //修改美食的评分
    public void UpdateMeishiScore() throws Exception {
        //查询物品信息
        MeishiContentDao dao = new MeishiContentDao();
        List<MeishiContentBean> meishis = dao.getMeishi();
        //打分
        for (MeishiContentBean content : meishis){
            String str = content.getContent();
            double v = qingganUtils.qingganUtil(str);
            String score = ""+v;
            //修改
            dao.updateMeishiScore(content.getId(),score);
        }
    }

    //修改景点的评分
    public void UpdateJingdianScore() throws Exception {
        //查询物品信息
        JingdianContentDao dao = new JingdianContentDao();
        List<JingdianContentBean> jingdains = dao.getJingdian();
        //打分
        for (JingdianContentBean content : jingdains){
            String str = content.getContent();
            double v = qingganUtils.qingganUtil(str);
            String score = ""+v;
            //修改
            dao.updateJingdianScore(content.getId(),score);
        }
    }

    /**
     * 找到每个城市的游记
     * 找到每个城市中提到的景点，物品，美食，提取出来，记录个数，和游记的id
     */
    @Test
public void TravelId(Segment segment ,TravelBean1 travel,List<String> jingdians,List<String> wupins,List<String> meishis,HashMap<String,Integer> meishinum,HashMap<String,Integer> wupinnum) throws Exception {
//获得所有的城市拼音
TravelDao dao = new TravelDao();

/* //每个城市的食物的数量
HashMap<String,Integer> meishinum = new HashMap<String,Integer>();
//每个城市物品的数量
HashMap<String,Integer> wupinnum = new HashMap<String,Integer>();*/

        //记录该游记的景点
        List<String> jingdian1 = new ArrayList<>();
        //记录该游记的美食
        List<String> meishi1 = new ArrayList<>();
        //记录游记中的物品
        List<String> wupin1 = new ArrayList<>();
        //在景点中记录游记中的景点,、物品数量、美食数量 对应的id

        String content = travel.getContent().replaceAll("[\n\n]+","\n");
        String city1 = travel.getCity();
        int month = travel.getMonth();
        int id = travel.getId();
        String[] lines = content.split("\n");
        for(int i=1;i<lines.length;i++){
            if(!lines[i].matches("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+")){
                List<Term> termList = segment.seg(lines[i]);
                for(int j=0;j<termList.size();j++)
                {
                    String word = termList.get(j).word.toString();
                    if(jingdians.contains(word)){//提取该游记中所有的景点
                        if(!jingdian1.contains(word)){
                            jingdian1.add(word);
                        }
                    }
                    if(meishis.contains(word)){
                        if(!meishi1.contains(word)){
                            meishi1.add(word);
                        }
                        //统计美食的个数
                        if(meishinum.get(word) == null){
                            meishinum.put(word,1);
                        }else{
                            meishinum.put(word,meishinum.get(word)+1);
                        }
                    }
                    if(wupins.contains(word)){
                        if(!wupin1.contains(word)){
                            wupin1.add(word);
                        }
                        //统计物品的个数
                        if(wupinnum.get(word) == null){
                            wupinnum.put(word,1);
                        }else{
                            wupinnum.put(word,wupinnum.get(word)+1);
                        }
                    }
                    //物品 travelId city
                }
            }
        }


        //将找到的景点，根据城市，存到jingdianTravelid数据库,加上
        JingdianDao jingdianDao = new JingdianDao();
        for(String j : jingdian1){
            //找到景点
            JingdianTravelId travelId = jingdianDao.travelId(city1, j);
            String travelids = travelId.getTravelids();
            travelids += ","+travel.getId();
            travelId.setTravelids(travelids);
            jingdianDao.UpdatetravelId(travelId);
        }

        MeishiDao meishiDao = new MeishiDao();
        for(String j : meishi1){
            MeishiBean travelId = meishiDao.travelId(city1,j);
            String travelids = travelId.getTravelids();
            travelids += ","+travel.getId();
            travelId.setTravelids(travelids);
            meishiDao.UpdatetravelId(travelId);
        }

        WupinDao wupinDao = new WupinDao();
        for(String j : wupin1){
            WupinBean travelId = wupinDao.travelId(city1,j);
            String travelids = travelId.getTravelids();
            travelids += ","+travel.getId();
            travelId.setTravelids(travelids);
            wupinDao.UpdatetravelId(travelId);
        }

        /*System.out.println("美食："+meishi1.toString());
        System.out.println("景点："+jingdian1.toString());
        System.out.println("物品："+wupin1.toString());*/


    }//一篇游记完

}
