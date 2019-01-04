package com.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 携程旅游游记爬取
 */
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
/**
 * 携程旅游 游记爬取 爬取杭州的游记
 * 将游记中第二行的信息分离出来
 * @author Mr_Zhang
 *
 */
public class WebMagic_Hangzhou implements PageProcessor{
    static int a = 0; //2000

    String path="D:/data/hangzhou";

    // http://you.ctrip.com/travels/china110000/t3-p3.html
    public static final String URL_LIST = "http://you\\.ctrip\\.com/travels/hangzhou14/t3-p\\d+\\.html";
    // http://you.ctrip.com/travels/xianggelila106/1873736.html
    // http://you.ctrip.com/travels/hangzhou14/3630781.html
    public static final String URL_POST = "http://you\\.ctrip\\.com/travels/\\w+/[0-9]{5,9}\\.html";
    // http://you.ctrip.com/travels/china110000.html
    // http://you.ctrip.com/travels/hangzhou14.html
    public static final String URL_LIST1 = "http://you\\.ctrip\\.com/travels/hangzhou14.html";

    private Site site = Site
            .me()
            .setDomain("you.ctrip.com")
            .setSleepTime(2000)
            .setTimeOut(200000)
            //.addHeader("Accept-Encoding", "/").setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
    public void process(Page page) {
        synchronized (this) {  //加锁
            //System.out.println( "链接："+page.getUrl().toString());
            //判断是否是文章页的链接
            if (page.getUrl().regex(URL_POST).match()) {

                System.out.println("详情页");
                String url = page.getUrl().toString();
                a++;

                String title = "";

                String content = "";

                String title1 = page.getHtml().xpath("//*[@class='ctd_head_con cf']/h1/text()").toString();
                if (title1 == null) {
                    title1 = page.getHtml().xpath("//*[@class='ctd_head_left']/h2/text()").toString();
                }


                // a[1]/div/ul/li[1]/i/text()
                title = "" + a;
                page.putField("title1 爬取" + a, title1);
                //将文章存到本地文件中
                File f1 = new File(path + "/" + title + ".txt");
                if (!f1.exists()) {

                    content = page.getHtml().xpath("//*[@class='ctd_main_body']/tidyText()")
                            .toString()
                            .replace("<", "")
                            .replace(">", "");
                    //将所有的回车都改为空格
                    //.replace("\n", "\t");
                    //System.out.println(content.toString());

                    try {
                        System.out.println("文件不存在，创建文件");
                        f1.createNewFile();
                        //写内容
                        try {
                            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f1), "UTF-8");

                            //FileWriter fw = new FileWriter(f1,false);
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
            else if(page.getUrl().regex(URL_LIST).match() ){
                System.out.println("列表页");
                page.addTargetRequests(
                        page.getHtml().xpath("//*[@class='journalslist cf']/a/@href").all());
            }
            else if(page.getUrl().regex(URL_LIST1).match()){
                System.out.println("首页");

                //通过xPath来定位页面列表中文章的url
                //找到列表页中的详情页地址，并加入待爬取队列
                page.addTargetRequests( // //*[@id='photo-fall']/div/div[2]/a[2]/@href
                        page.getHtml().xpath("//*[@class='journalslist cf']/a/@href").all());

                // 定位翻页的url，并将其加入带爬取页面

                List<String> list = new ArrayList<String>();
                // 500
                for(int i=2;i<=500;i++){
                    list.add("http://you.ctrip.com/travels/hangzhou14/t3-p"+i+".html");
                    System.out.println(list.toString());
                }

                page.addTargetRequests(list);

                List<Request> list1 = page.getTargetRequests();
                for(Request r : list1){
                    System.out.println("队列中的请求："+r.toString());
                }
            }
        }
    }

    public Site getSite() {
        return site;
    }
    public static void main(String[] args) {
        Spider.create(new WebMagic_Hangzhou()).addUrl("http://you.ctrip.com/travels/hangzhou14.html")
                .thread(8)
                .run();
        System.out.println("一共爬取了"+a+"条数据");
    }

}
