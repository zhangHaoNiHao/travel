package com.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {
        /*//这里写你要访问的url地址
        String url = "www.baidu.com";
        //参数
        String param1 ="liu";
        String param2 ="youer";

        HttpRequest hr = new HttpRequest();//这个类，请看我另外一篇
        hr.setURL(url);
        //访问方法为post
        hr.setMethod("POST");

            hr.addParam("xing",param1);
            hr.addParam("ming",param2);
            hr.addParam("sex","男");

        //返回值为String类型的Json字符串
        String response = hr.Send();
        //取值方法如下：
        JSONObject ret = JSONObject.fromObject(response);*/
        /*
                   如果是多层的Json，比如
                   {"server_response":{"status_code":201,"balance":"1974600.00",""coupons_balance":"0.00"}}
                  具体取值，如下：
                  JSONObject jsonObj = JSONObject.fromObject(response);
                     JSONObject jsonObj1 =jsonObj.getJSONObject("server_response");
                     Object balance =jsonObj1.get("balance");
                     Object coupons_balance =jsonObj1.get("coupons_balance");
          */
        System.out.println(APITest());
    }

    public static List<String> APITest() throws IOException {

        String url = "http://gc.ditu.aliyun.com/geocoding?a=石家庄";
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
            String[]  word = line.split(",");
            String jing = word[0].split(":")[1];
            String wei = word[5].split(":")[1].split("}")[0];
            list.add(jing);
            list.add(wei);
            System.out.println("jing:"+jing+"  wei:"+wei);
        }
        br.close();// 关闭流
        connection.disconnect();// 断开连接
        return list;
    }

}
