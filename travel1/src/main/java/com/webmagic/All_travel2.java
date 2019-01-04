package com.webmagic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;



import com.Bean.TravelBean;
import com.Bean.TravelBean1;
import com.Dao.JingdianDao;
import com.utils.FileUtil;
import com.utils.qingganUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 爬取全国的游记，具体的地区还是按照爬取景点的地区
 * 
 * @author Mr_Zhang
 *
 */
public class All_travel2 implements PageProcessor{
	static int a = 0; //2000
	
	JingdianDao dao = new JingdianDao();
	
	String path="D:/data/travelsAll";
	// http://you.ctrip.com/travels/chongqing158/t3-p2.html
	// http://you.ctrip.com/travels/chongqing158/t3.html
	// public static final String URL_LIST = "http://you\\.ctrip\\.com/travels/hangzhou14/t3-p\\d-d1-c4-g17+\\.html";
	public static final String URL_LIST1 = "http://you\\.ctrip\\.com/travels/\\w+\\.html";
	public static final String URL_LIST1_1 = "http://you\\.ctrip\\.com/travels/\\w+/t3-p\\d+\\.html";
	// http://you.ctrip.com/travels/xianggelila106/1873736.html
	// http://you.ctrip.com/travels/hangzhou14/3630781.html                 hangzhou
	//public static final String URL_POST = "http://you\\.ctrip\\.com/travels/\\w+/[0-9]{5,9}\\.html";
	public static final String URL_POST = "http://you\\.ctrip\\.com/travels/hangzhou14/[0-9]{5,9}\\.html";
    // http://you.ctrip.com/travels/china110000.html
    public static final String URL_LIST = "http://you\\.ctrip\\.com/countrysightlist/China110000/p\\d+\\.html";
    
    private Site site = Site
            .me()
            .setDomain("you.ctrip.com")
            .setSleepTime(2000)
            .setTimeOut(20000)
            .setUserAgent(
            		"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
    public void process(Page page) {
    	synchronized (this) {  //加锁
    	//System.out.println( "链接："+page.getUrl().toString());
        if (page.getUrl().regex(URL_POST).match()) {
				System.out.println("详情页");
				// 获取游记的省和市

			/**
				String url = page.getUrl().toString();
				String city = url.split("/")[4].replaceAll("[0-9]+", "");
				a++;
				String title1 = page.getHtml().xpath("//*[@class='ctd_head_con cf']/h1/text()").toString();
				if (title1 == null || title1 .equals("")) {
					title1 = page.getHtml().xpath("//*[@class='ctd_head_left']/h2/text()").toString();
				}
				System.out.println(title1);
				String content = "";
				content = page.getHtml().xpath("//*[@class='ctd_main_body']/tidyText()")
					.toString()
					.replace("<", "")
					.replace(">", "")
					//删除空格
					.replace(" ", "")
					// 将所有带有.html的链接删除，并且因为这些链接造成转折的句子恢复
					.replaceAll("\nhttp(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.html[a-zA-Z0-9/#\\.\\?=_-]{0,100}\n", "")
					.replaceAll("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.html[a-zA-Z0-9/#\\.\\?=_-]{0,100}\n", "")
					.replaceAll("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+\\.html[a-zA-Z0-9/#\\.\\?=_-]{0,100}", "");
				String title = "" + a;
				page.putField("title1 数量" + a, title1);
				//判断文件个数 path+"/"+city
				int filenum = 0;
				try {
					List<String> filelist = FileUtil.readfile(path+"/"+city);
					filenum = filelist.size();
				} catch (Exception e) {
					e.printStackTrace();
				}
				///////////////////////////
				//如果每个城市的游记数小于某个值，就不再爬取
				File f1 = new File(path + "/" +city+"/"+ title + ".txt");
				if(filenum < 1000) {
					FileUtil.travelsave(f1,content,title1,city);
				}
				*/
	        }
        	else if(page.getUrl().regex(URL_LIST1).match() || page.getUrl().regex(URL_LIST1_1).match()){
	        	System.out.println("列表页"); 
	        	
	        	//列表页中的喜欢值
	        	List<String> content = page.getHtml().xpath("//*[@class='journalslist cf']/a/div/ul/li[2]/i/text()").all();//.toString();
	        	System.out.println("content喜欢值："+content);

	        	List<String> xiangqingye = page.getHtml().xpath("//*[@class='journalslist cf']/a/@href").all();
	        	
	        	//将符合喜欢值的游记爬取下来
	        	List<String> links = new ArrayList();
	        	for(int i=0;i<content.size();i++){
	        		if(Integer.parseInt(content.get(i)) > 500){
	        			links.add(xiangqingye.get(i));
	        			System.out.println("喜欢值："+content.get(i)+"  游记链接"+xiangqingye.get(i));
	        		}
	        	}
	        	page.addTargetRequests(links);
	        	
	        	for(String r : links){
	        		System.out.println("加载的链接："+r.toString());
	        	}
	        }
	        else if(page.getUrl().regex(URL_LIST).match()){
	        	System.out.println("首页");
	        	
	        	
	        	//通过xPath来定位页面列表中文章的url
	        	//找到列表页中的详情页地址，并加入待爬取队列
	        	List<String> list = page.getHtml().xpath("//*[@class='list_wide_mod1']/div/div[1]/a/@href").all();
	        	List<String> list3 = new ArrayList<String>();
	        	for(String l : list){
	        		String d = l.replace("place", "travels");
	        		list3.add(d);
	        	}
	        	System.out.println(list3);
	        	page.addTargetRequests(list3);
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
    	Spider.create(new All_travel2()).addUrl("http://you.ctrip.com/countrysightlist/China110000/p1.html")
        .thread(8)
        .run();
        System.out.println("一共爬取了"+a+"条游记");
    }

}
