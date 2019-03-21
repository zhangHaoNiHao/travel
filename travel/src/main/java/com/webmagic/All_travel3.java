package com.webmagic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.Bean.TravelBean1;
import com.Dao.JingdianDao;
import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 爬取全国的游记，具体的地区还是按照爬取景点的地区
 * 
 * @author Mr_Zhang
 *
 */
public class All_travel3 implements PageProcessor{
	static int a = 0; //2000

	static int nowYear = 2018;
	static int nowMonth = 1;

	JingdianDao dao = new JingdianDao();
	
	String path="D:/data/travelsAll3";
	public static final String URL_LIST1 = "http://you\\.ctrip\\.com/travels/\\w+\\.html";
	public static final String URL_LIST1_1 = "http://you\\.ctrip\\.com/travels/\\w+/t3-p\\d+\\.html";

	public static final String URL_POST = "http://you\\.ctrip\\.com/travels/\\w+/[0-9]{5,9}\\.html";
    public static final String URL_LIST = "http://you\\.ctrip\\.com/travels/China110000/t2-p\\d+\\.html";
    
    //存放游记
    List<TravelBean1> Travellist = new ArrayList<TravelBean1>();

    private Site site = Site
            .me()
            .setDomain("you.ctrip.com")
            .setSleepTime(2000)
            .setTimeOut(100000)
            .setUserAgent(
            		"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");

    public void process(Page page) {
    	synchronized (this) {  //加锁
        if (page.getUrl().regex(URL_POST).match()) {
				System.out.println("详情页");
				// 获取游记的省和市
				String url = page.getUrl().toString();
				String city = url.split("/")[4].replaceAll("[0-9]+", "");
				
				a++;
				String title1 = page.getHtml().xpath("//*[@class='ctd_head_con cf']/h1/text()").toString();
				if (title1 == null || title1 .equals("")) {
					title1 = page.getHtml().xpath("//*[@class='ctd_head_left']/h2/text()").toString();
				}
				//2018.12.15
				String month1 = page.getHtml().xpath("//*[@class='ctd_head_con cf']/div[1]/text()").toString();
				if(month1 == null || month1.equals("")){
					//更新时间：2018.11.06                    
					month1 = page.getHtml().xpath("//*[@class='ctd_head_left']/p/text()").toString();
				}
				System.out.println("month1："+month1);
				int month = 0;
				if(month1 != null && !month1.trim().equals(""))
					month = Integer.parseInt(month1.split("\\.")[1]);

				System.out.println(title1);
				String content = "";
				content = page.getHtml().xpath("//*[@class='ctd_main_body']/tidyText()")
						.toString()
						.replace("<", "")
						.replace(">", "")
						//删除空格
						.replace(" ", "")
						// 将所有带有.html的链接删除，并且因为这些链接造成转折的句子恢复
						.replaceAll("\nhttp(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.html[a-zA-Z0-9/&#\\.\\?=_-]{0,100}\n", "")
						.replaceAll("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.html[a-zA-Z0-9/#&\\.\\?=_-]{0,100}\n", "")
						.replaceAll("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.html[a-zA-Z0-9/#&\\.\\?=_-]{0,100}", "");
				
				String title = "" + a;
				page.putField("title1 数量" + a, title1);


				File f = new File(path+"/"+city);
				if(!f.exists())
					f.mkdirs();
				File f1 = new File(path + "/" +city+"/"+ title + ".txt");
				//fileClean.travelsave(f1,content,title1,city,Travellist);
				
				if (!f1.exists()) {
		            int day = 0;
		            String line = content.split("\n")[0];
		            System.out.println("line:"+line);
		            if(line.contains("天数")){
		            	line = line.substring(line.indexOf("天数")+1,line.length());
		            	String daytext = line.split("：")[1];
		            	day = Integer.parseInt(daytext.substring(0, daytext.indexOf("天")));
		                System.out.println(day+"天");
		            }
		            if(line.contains("月") && month == 0){
		                String monthtext = line.substring(line.indexOf("时间"), line.indexOf("月"));
		                month = Integer.parseInt(monthtext.split("：")[1]);
		                System.out.println(month+"月");
		            }

		            int count = 0;
		            if(content.contains("发表于")){
		                count = content.indexOf("发表于");
		                if(month == 0){
		                	String text = content.substring(content.indexOf("发表于"),content.indexOf("发表于")+12);
		                	System.out.println(text);
		                	month = Integer.parseInt(text.split("-")[1]);
		                }
		                
		                
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
		            content = content.substring(count, content.length()).replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");

		            //文章满意度分析,放在最后，爬取速度太慢
		            //double fen = qingganUtils.articleQinggan(content);
		            content = title1+"\n"+content;
		            
		            
		            System.out.println("月份： "+month);
		            
		            TravelBean1 bean = new TravelBean1(title1,content,city,month,day);
		            Travellist.add(bean);
		            
		            if(Travellist.size() > 100){
		            	for(TravelBean1 bea : Travellist){
		            		if(dao.addTravel(bea)){
		                        System.out.println("添加成功");
		                    }else{
		                        System.out.println("添加失败");
		                        System.out.println(bea.toString());
		                    }
		            	}
		            	Travellist.clear();
		            }
		            
		            try {f1.createNewFile();
		                System.out.println("文件不存在，创建文件");

		                //写内容
		                try {
		                    OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f1), "UTF-8");
		                    BufferedWriter bw = new BufferedWriter(write);
		                    bw.write(month + "\n");
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
	        else if(page.getUrl().regex(URL_LIST).match()){
	        	System.out.println("首页");
	        	
	        	//找到列表页中的详情页地址，并加入待爬取队列
	        	List<String> list = page.getHtml().xpath("//*[@class='journalslist cf']/a/@href").all();

	        	//列表页中的日期
	        	List<String> time = page.getHtml().xpath("//*[@class='journalslist cf']/a/div/dl/dd[1]/text()").all();//.toString();

	        	System.out.println("日期：："+time);
	        	
	        	//将符合要求的游记爬取下来
	        	List<String> links = new ArrayList();
	        	for(int i=0;i<time.size();i++){
	        		String text = time.get(i).split("发表于")[1].split("-")[0];
	        		int month = 1;
	        		month = Integer.parseInt(time.get(i).split("-")[1]);
					System.out.println("月份: "+month);
	        		int year = 0;
	        		year = Integer.parseInt(text.substring(text.length()-4, text.length()));

	        		if(year == nowYear && month == nowMonth){
	        			links.add(list.get(i));
	        			System.out.println("年份："+year+"  游记链接"+list.get(i));
	        		}
	        	}
	        	page.addTargetRequests(links);
	        	
	        	for(String r : links){
	        		System.out.println("加载的链接："+r.toString());
	        	}
	        	List<String> liebiaourl = new ArrayList();
	        	// 52065
	        	for(int x=2; x<52065; x++){
	        		String url = "http://you.ctrip.com/travels/china110000/t2-p"+x+".html";
	        		liebiaourl.add(url);
	        	}
	        	page.addTargetRequests(liebiaourl);
	        	
	        }
    	}
    }
    
    public Site getSite() {
        return site;
    }

    //启动爬虫
	//@Test
    public static void Webmagic(int year , int month){
    	nowYear = year;
    	nowMonth = month;

		System.out.println("年份："+nowYear);
		Spider.create(new All_travel3()).addUrl("http://you.ctrip.com/travels/china110000/t2-p1.html")
				.thread(8)
				.run();
	}

    public static void main(String[] args) {
    	Spider.create(new All_travel3()).addUrl("http://you.ctrip.com/travels/china110000/t2-p1.html")
        .thread(8)
        .run();
    	//将每个城市插入游记个数

    	//newTravelTitle
        System.out.println("一共爬取了"+a+"条游记");
    }
    
    
    

}
