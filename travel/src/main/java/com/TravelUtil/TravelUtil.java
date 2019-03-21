package com.TravelUtil;

import com.Bean.TravelBean1;
import com.Dao.TravelDao;
import com.utils.qingganUtils;

import java.util.List;

public class TravelUtil {

    /**
     * 游记情感分析（打分）
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        QingganScore();
    }
    //修改游记评分
    public static void QingganScore() throws Exception{
        TravelDao traveldao = new TravelDao();
        List<TravelBean1> travels = traveldao.TravelList();
        for(TravelBean1 travel : travels){
            //分页查询
            // Thread.sleep(1000);
            double score = articleQinggan(travel.getContent());
            System.out.println("文章的满意度是："+score);
            if(score != 0.0)
                traveldao.addScore(score,travel.getId());

        }
    }
    //情感分析
    public static double articleQinggan(String content) throws Exception{
        //将所有的链接删除
        String content1 = content.replaceAll("http(s)?://[a-zA-Z0-9/#&\\.\\?=_-]+", "");
        String[] lines = content1.split("\n");
        double score = 0.0;
        //参与情感分析的句子个数，将来算平均数
        int a = 0;
        for(int i=0;i<lines.length;i++){

            //System.out.println("lines[i]:"+lines[i]);
            if(lines[i] != null && (!(lines[i].equals("")))){
                // Thread.sleep(100);
                //lines[i]=new String(lines[i].getBytes(),"GBK");
                if(lines[i].length()>1000){
                    //句子太长，需要切分
                    System.out.println("句子长度大于1000");
                    String[] line2 = lines[i].split("。");
                    if(line2.length < 1){
                        line2 = lines[i].split("；");
                    }
                    for(int j=0;j<line2.length;j++){
                        if(line2[j] != null && (!line2[j].equals(""))){
                            //System.out.println("line2[j]:"+line2[j]);
                            double positive = qingganUtils.qingganUtil(line2[j]);
                            if(positive == 0){
                                System.out.println("该句话不可置信");
                            }else{
                                score += positive;
                                a++;
                            }
                        }else{
                           // System.out.println("line2[j]:为空"+line2[j]);
                        }
                    }
                }else{
                    //System.out.println("line[i]: "+lines[i]);
                    System.out.println("句子长度小于1000");
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
}
