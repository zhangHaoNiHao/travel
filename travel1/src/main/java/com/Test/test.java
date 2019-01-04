package com.Test;

import com.Bean.TravelBean;
import com.Bean.TravelBean1;
import com.baidu.aip.nlp.AipNlp;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.utils.JDBCUtil;
import com.utils.RowMap;
import com.utils.qingganUtils;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class test {

    //设置APPID/AK/SK
    public static final String APP_ID = "14936500";
    public static final String API_KEY = "R0GNOGK4OqQf5YTQe36F2x3W";
    public static final String SECRET_KEY = "yinTpXjTYNMuHaLBXXgmRQISbFXUe6Y8";

    private static AipNlp instance = null;
    public static synchronized AipNlp getInstance() {
        if (instance == null) {
            instance = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

            instance.setConnectionTimeoutInMillis(2000);
            instance.setSocketTimeoutInMillis(60000);
        }
        return instance;
    }

    public static double xiangsiduUtil(String first,String line){
        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("model", "CNN");
        JSONObject res = getInstance().simnet(first,line,options);
        System.out.println(res.toString(2));
        Double score =  Double.valueOf(res.get("score").toString());
        return score;
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(xiangsiduUtil("今天天气真好","今天天气晴朗"));
        /*
        String s = "攻城狮你好啊";
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        List<Term> termList = segment.seg(s);
        System.out.println(termList);
        */
        /*
        String s = "将来今天天气真好 \n https://youimg1.c-ctrip.com/target/1008050000000xesbBF5B_R_1024_10000_Q90.jpg?proc=autoorient  http://you.ctrip.com/sight/zhujiajiao176/7088.html";
        System.out.println(s.replaceAll("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+",""));
        System.out.println(s);
        */


        List<TravelBean1> travels = TravelList();
        for(TravelBean1 travel : travels){
            double score = articleQinggan(travel.getContent());
            System.out.println("分数： "+score);
            addScore(score,travel.getId());
        }


    }


////////////////////////////////////////////////////////////////////////////////////
    /**
     * 修改每个游记的情感值
     * @param score
     */
    public static Boolean addScore(double score, int id) {
        Boolean flag = false;
        String sql = "update travel set score=? where id=?";
        int a = 0;
        String score1 = ""+score;
        a = JDBCUtil.executeUpdate(sql, score1,id);
        if(a > 0){
            flag = true;
        }
        return flag;
    }

    public static List<TravelBean1> TravelList() throws Exception {
        String sql = "select * from travel";
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
        }, null);
        return list;
    }

    public static double articleQinggan(String content){

        //将所有的链接删除
        String content1 = content.replaceAll("http(s)?://[a-zA-Z0-9/#&\\.\\?=_-]+", "");
        System.out.println("content："+content1);
        String[] lines = content1.split("\n");
        double score = 0.0;
        //参与情感分析的句子个数，将来算平均数
        int a = 0;
        for(int i=0;i<lines.length;i++){
            //System.out.println("lines[i]:"+lines[i]);
            if(lines[i] != null && (!(lines[i].equals("")))){
                if(lines[i].length()>1024){
                    //句子太长，需要切分
                    String[] line2 = lines[i].split("。");
                    for(int j=0;j<line2.length;j++){
                        if(line2[j] != null && (!line2[j].equals(""))){
                            System.out.println(line2[j]);
                            double positive = qingganUtils.qingganUtil(line2[j]);
                            if(positive == 0){
                                System.out.println("该句话不可置信");
                            }else{
                                score += positive;
                                a++;
                            }
                        }else{
                            System.out.println("line2[j]:为空"+line2[j]);
                        }
                    }
                }else{
                    double positive = qingganUtils.qingganUtil(lines[i]);
                    if(positive == 0){
                        System.out.println("该句话不可置信");
                    }else{
                        score += positive;
                        a++;
                    }
                }
            }else{
                System.out.println("lines[i]:为空"+lines[i]);
            }
        }

        double pingjun = score / a;

        System.out.println("平均分："+pingjun);

        return pingjun;
    }





////////////////////////////////////////////////////////////////////////////////////
}
