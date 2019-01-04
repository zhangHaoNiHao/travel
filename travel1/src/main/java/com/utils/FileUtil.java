package com.utils;

import com.Bean.TravelBean;
import com.Bean.TravelBean1;
import com.Dao.JingdianDao;
import com.Dao.TravelDao;
import com.Dao.UserDaoImp;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.junit.Test;

import java.io.*;
import java.util.*;


public class FileUtil {

    /**
     * 找出每篇游记，作者去过的地点
     * 1.找到文章中所有的杭州景点
     * 2.再判断每个景点的频度，按照list的先后顺序
     * @param
     * @return
     * @throws IOException
     */
    private static class ValueComparator implements Comparator<Map.Entry<String,Integer>>
    {
        public int compare(Map.Entry<String,Integer> m,Map.Entry<String,Integer> n)
        {
            return n.getValue()-m.getValue();
        }
    }

     /*public static List<String> JingdianList(String content,String city) throws Exception {
        return null;
     }*/
    @Test
    public static List<String> JingdianList(String content,String city) throws Exception {
        /*TravelDao dao = new TravelDao();
        TravelBean1 bean = dao.searchTravel(7);
        String content = bean.getContent();*/

        content.replaceAll("http(s)?://[a-zA-Z0-9/\\\\.#=_-]+","")
                .replaceAll("\\n","");
        //统计每行的地点出现频度
        HashMap<String , Integer> map = new HashMap<String, Integer>();
        //存放景点
        List<String> list = new ArrayList<>();
        //存放景点,大小值排序
        List<String> list2 = new ArrayList<>();

        //分词对象
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        //将杭州所有景点读到list集合中
        List<String> jingdians = new ArrayList<String>();
        FileInputStream input2 = new FileInputStream(new File("D:/data/jingdianAll_G/"+city+".txt"));
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2,"utf-8"));
        String jing2 = reader2.readLine();
        while(jing2 != null){
            jingdians.add(jing2);
            jing2 = reader2.readLine();
        }
        reader2.close();
        //分词
        List<Term> termList = segment.seg(content);
        //System.out.println(termList);
        for (int i = 0; i < termList.size(); i++){
            String word = termList.get(i).word.toString();//词
            if(word.equals("重庆丽晶酒店") && jingdians.contains("重庆丽晶酒店")){
                System.out.println("存在 重庆丽晶酒店");
            }
            //统计地点出现频率
            if(jingdians.contains(word)) { //如果该地点在杭州

                if (null == map.get(word)) { //如果不存在
                    System.out.println("null时加入：" + word);
                    map.put(word, 1);//加入
                } else { //如果存在，直接加1
                    int count = map.get(word);
                    map.put(word, count + 1);
                }
                if (!list.contains(word)) {
                    list.add(word);
                }
            }
        }

        //list集合找好
        //需要找到数量最大的几个景点，并将它们从list集合中找到，或者转到其他集合中

        //map排序
        List<Map.Entry<String,Integer>> list_data=new ArrayList<>();
        list_data.addAll(map.entrySet());
        FileUtil.ValueComparator vc=new ValueComparator();
        Collections.sort(list_data,vc);

        List<String> list3 = new ArrayList<>();
        int a = 0;
        for(Iterator<Map.Entry<String,Integer>> it=list_data.iterator();it.hasNext();)
        {
            list3.add(it.next().getKey());
        }
        //前四个保留，将list中按原顺序中删除
        List<String> list4 = new ArrayList<>();

        for(int i=0;i<list3.size()*0.8;i++){
            list4.add(list3.get(i));
        }
        List<String> list5 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list4.contains(list.get(i))){
                list5.add(list.get(i));
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Integer value = entry.getValue();
            String key = entry.getKey();
            System.out.println(key+": "+value.toString());
        }
        for(String j : list){
            System.out.println("list："+j);
        }
        for(String j : list5){
            System.out.println("list5："+j);
        }
        return list5;
    }

    public static void main(String[] args) throws Exception {

        UserDaoImp dao = new UserDaoImp();
        TravelBean bean =  dao.searchTravel(6).get(0);
        String content = bean.getTravel();
        content.replaceAll("http(s)?://[a-zA-Z0-9/\\\\.#=_-]+","")
                .replaceAll("\\n","");

        System.out.println("内容："+content);
        //统计每行的地点出现频度
        HashMap<String , Integer> map = new HashMap<String, Integer>();

        //存放景点
        List<String> list = new ArrayList<>();
        //存放景点,大小值排序
        List<String> list2 = new ArrayList<>();

        //分词对象
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        //将杭州所有景点读到list集合中
        List<String> jingdians = new ArrayList<String>();
        FileInputStream input2 = new FileInputStream(new File("D:/data/hangzhou_jingdian.txt"));
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2,"utf-8"));
        String jing2 = reader2.readLine();
        while(jing2 != null){
            jingdians.add(jing2);
            jing2 = reader2.readLine();
        }
        reader2.close();
        //分词
        List<Term> termList = segment.seg(content);
        for (int i = 0; i < termList.size(); i++){
            String word = termList.get(i).word.toString();//词

            //统计地点出现频率
            if(jingdians.contains(word)) { //如果该地点在杭州
                if (null == map.get(word)) { //如果不存在
                    System.out.println("null时加入：" + word);
                    map.put(word, 1);//加入
                } else { //如果存在，直接加1
                    int count = map.get(word);
                    map.put(word, count + 1);
                }

                if (!list.contains(word)) {
                    list.add(word);
                }
            }

        }
        for(String j : list){
            System.out.println("list："+j);
        }
        //list集合找好
        //需要找到数量最大的几个景点，并将它们从list集合中找到，或者转到其他集合中

        //map排序
        List<Map.Entry<String,Integer>> list_data=new ArrayList<>();
        list_data.addAll(map.entrySet());
        FileUtil.ValueComparator vc=new ValueComparator();
        Collections.sort(list_data,vc);

        List<String> list3 = new ArrayList<>();
        int a = 0;
        for(Iterator<Map.Entry<String,Integer>> it=list_data.iterator();it.hasNext();)
        {
            //System.out.println("map排序："+it.next());
            HashMap<String,Integer> map1 = new HashMap<>();
            //map1 = it.next();
            list3.add(it.next().getKey());
        }
        for(String j : list3){
            System.out.println("list3："+j);
        }
        //前四个保留，将list中按原顺序中删除
        List<String> list4 = new ArrayList<>();

        for(int i=0;i<list3.size()*0.8;i++){
            list4.add(list3.get(i));
        }
        for(String j : list4){
            System.out.println("list4："+j);
        }
        List<String> list5 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list4.contains(list.get(i))){
                list5.add(list.get(i));
            }
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Integer value = entry.getValue();
            String key = entry.getKey();
            System.out.println(key+": "+value.toString());
        }

        for(String j : list5){
            System.out.println("list5："+j);
        }


        /*
        List<Integer> intg = new ArrayList<>();
        //遍历HashMap
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Integer value = entry.getValue();
            String key = entry.getKey();
            intg.add(value);
            System.out.println("map::"+key+": "+value.toString());
        }
        Collections.sort(intg);
        System.out.println("intg:"+intg);
        System.out.println("list。size:"+list.size()*0.8);
        //按照list出现的前后顺序，和频率
        int a = 0;
        for(int i=list.size()-1;i>0;i--){

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                Integer value = entry.getValue();
                String key = entry.getKey();
                if(intg.get(i) == value && a<list.size()*0.8 ){
                    list2.add(key);
                    a++;
                }
            }
        }


            System.out.println("list21："+list2);

         */


    }

    //获取目录中所有的文件地址
    public static List<String> readfile(String filepath) throws Exception {
        File file = new File(filepath);
        if(!file.exists()){
            file.mkdir();
        }
        List<String> files = new ArrayList<String>();
        if (file.isDirectory()) {
            System.out.println("文件夹");
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "\\" + filelist[i]);
                if (!readfile.isDirectory()) {
                    String path = readfile.getName();
                    files.add(path);
                }
            }
        }
        return files;
    }

    /**
     * 将游记进行保存（文件和数据库）
     */
    public static void travelsave(File f1,String content,String title1,String city){
        if (!f1.exists()) {
            int month = 0;
            int day = 0;
            String line = content.split("\\n")[0];
            System.out.println("line:"+line);
            if(line.contains("天")){
                String daytext = line.split("：")[1];
                day = Integer.parseInt(daytext.substring(0, daytext.indexOf("天")));
                System.out.println(day+"天");
            }else if(line.contains("月")){
                String monthtext = line.split("：")[2];
                month = Integer.parseInt(monthtext.substring(0, monthtext.indexOf("月")));
                System.out.println(month+"月");
            }

            int count = 0;
            if(content.contains("发表于")){
                count = content.indexOf("发表于");
            }
            if(content.contains("展开更多酒店") ){
                int x = content.indexOf("展开更多酒店")+6;
                if(count < x){
                    count = x;
                }
            }
            if(content.contains("收起") && content.contains("显示全部")){
                int x = content.indexOf("收起")+2;
                if(count < x){
                    count = x;
                }
            }

            System.out.println("count:"+count);
            content = content.substring(count, content.length());
            System.out.println("content： "+content);

            //文章满意度分析
            double fen = qingganUtils.articleQinggan(content);
            content = title1+"\n"+content;

            /*
            JingdianDao dao = new JingdianDao();
            TravelBean1 bean = new TravelBean1(title1,content,city,fen,month,day);
            if(dao.addTravel(bean)){
                System.out.println("添加成功");
            }else{
                System.out.println("添加失败");
            }
            */
            try {
                System.out.println("文件不存在，创建文件");
                f1.createNewFile();
                //写内容
                try {
                    OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f1), "UTF-8");
                    BufferedWriter bw = new BufferedWriter(write);
                    bw.write(title1 + "\n");
                    bw.write(content);
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
