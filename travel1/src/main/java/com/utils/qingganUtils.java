package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.baidu.aip.nlp.AipNlp;

public class qingganUtils {
    public static final String APP_ID = "14936500";
    public static final String API_KEY = "R0GNOGK4OqQf5YTQe36F2x3W";
    public static final String SECRET_KEY = "yinTpXjTYNMuHaLBXXgmRQISbFXUe6Y8";

    /**
     * 单例模式
     */
    private static AipNlp instance = null;
	public static synchronized AipNlp getInstance() {
		if (instance == null) {
			instance = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
			
			instance.setConnectionTimeoutInMillis(2000);
			instance.setSocketTimeoutInMillis(60000);
		} 
		return instance;
	}

	public static double qingganUtil(String content){
		// 传入可选参数调用接口
		HashMap<String, Object> options = new HashMap<String, Object>();
		// 情感倾向分析
		JSONObject res = getInstance().sentimentClassify(content, options);
		System.out.println("查询结果："+res.toString(2));
		JSONObject res1 = null;
		double positive = 0.0;
		double confidence = 0.0;
		if(res.has("items"))
		{
			res1 = res.getJSONArray("items").getJSONObject(0);
			positive =  res1.getDouble("positive_prob");
			confidence = res1.getDouble("confidence");
		}
		//System.out.println(positive+":"+res.toString(2));
		System.out.println("positive："+positive);
		if(confidence > 0.45){
			return positive;
		}else{
			return 0;
		}
	}


	/**
	 * 两个句子的相似度
	 * @param first
	 * @param line
	 * @return
	 */
	public static double xiangsiduUtil(String first,String line){

	    HashMap<String, Object> options = new HashMap<String, Object>();
	    options.put("model", "CNN");
	    JSONObject res = getInstance().simnet(first,line,options);
	    System.out.println(res.toString(2));
	    Double score =  Double.valueOf(res.get("score").toString());
	    return score;
	}

	/**
	 * 整篇文章的满意度
	 * @param content
	 * @return
	 */
	public static double articleQinggan(String content){
		String content1 = content.replaceAll("http(s)?://[a-zA-Z0-9/#\\.\\?=_-]+", "");
		System.out.println("content："+content1);
		String[] lines = content1.split("\n");
		double score = 0;
		int a = 0;
		for(int i=0;i<lines.length;i++){
			System.out.println("lines[i]:"+lines[i]);
			if(lines[i] != null && (!(lines[i].equals("")))){
				if(lines[i].length()>1024){
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
							System.out.println("line2[j]:空"+line2[j]);
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
				System.out.println("lines[i]:空"+lines[i]);
			}
		}
		double pingjun = score / a;
		
		System.out.println("满意度："+pingjun);
	
		return pingjun;
	}
	

    public static void main(String[] args) throws IOException, InterruptedException {
       
    	System.out.println(xiangsiduUtil("今天天气真好","天气好"));
        //sample1(client);
		System.out.println(articleQinggan("今天天气真好"));
    }
    
    public static void sample(AipNlp client) {
        String text = "千岛湖的水真的非常的绿，这几天雨水较多，黄金腰带基本看不见了，山间水汽也较重，所以照片效果一般。";

        HashMap<String, Object> options = new HashMap<String, Object>();
        JSONObject res = client.sentimentClassify(text, options);
        //System.out.println(res.toString(2));
        double positive =  res.getJSONArray("items").getJSONObject(0).getDouble("positive_prob");
        System.out.println(positive+":"+res.toString(2));

    }
    /**
     * 相似度
     * @param client
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void sample1(AipNlp client) throws IOException, InterruptedException {
         /*
    	String text1 = "到达千岛湖的酒店已经是下午三点，作为以旅游闻名的小镇，拥有各式各样的酒店，淡季里人不是很多，而且价格便宜。条件好一点的酒店就建在湖边，与湖水、群岛浑然成景。还没开始游湖，我就被吸引了。";
        String text2 = "踱在酒店的小径上，听着鸟儿悦耳的歌声，湖水拍打着岸边，远处传来游轮的汽笛声。";
        String text3 = "无边的泳池直接“伸进”湖中，于是有了再水中徜徉的冲动，或者，扒在岸边什么也不做，静静的看，静静的听……";
        String text4 = "和心爱的人一起坐在摇椅上欣赏湖景，一起回忆那曾经的美好，是不是最浪漫的事呢？";
        String text5 = "沿着小路边走边看，远处的魁星楼吸引了我，在中国，很多地方都建有魁星楼，都是保佑各路文人骚客才高八斗高中及第的。淳安县是人才辈出的地方，自然少不了魁星的保佑。每逢七月七这里应该很是热闹。七月七不光是七夕节，还是魁星诞哟。你知道吗，反正我知道，呵呵！";
        String text6 = "酒店的不远处便是千岛湖的游船码头，傍晚时分游船纷纷归航，看到游客脸上绽放的笑容，我不禁憧憬起明天的游湖旅程。";
        String text7 = "租了一辆自行车，摊主答应转天带我去买船票，原本150元的船票，只用120两银子。于是租下车，沿着湖边慢慢的骑着，一会儿左看看，一会儿右看看，一会儿飞驰，一会停下来，一切随心所欲。因为千岛湖的景色是随心而变的。";
        String text8 = "一径枝叶绿，满目桂花红。千岛湖有一条临湖的骑行路，分为红、绿、紫三种颜色，与公路隔开，人很少，湖水就在我旁边，骑着小车哼着小曲，悠闲自得知。";
        String text9 = "湖边岛屿交错，我总是不停地回头，景色也不停地变换，千岛湖的景总会给人带来不同感受，而不变的是不断发现的惊喜。";
        String text10 = "一座座“漂浮”在湖面上的小岛，从不同的角度望去，总有不同的精彩。这里的小岛原本都是一座座山尖，这里平均水深34米，最深处108米，相当于36层楼的高度。";
        String[] test = {text1,text2,text3,text4,text5,text6,text7,text8,text9,text10};

        String test11 = "据说这个小岛上的松树很是神奇，丰水期它会淹没，枯水期又会崭露头角，如此大起大落的环境，依然还长得这么挺拔郁葱，可叹其生命力了得！";
        String test12 = "您是不是很纳闷？为什么这里的岛都有一段黄土外露，寸草不生，这就是所谓的“青山绿水金腰带”。其实，这条腰带大有讲究，它是千岛湖的生命线。如果您仔细看不难发现，这些金腰带都一般高，其实这是千岛湖的泄洪线，汛期时，如果湖水没过这条腰带，那就变成祸水喽，新安江水库就要向外泄洪，确保百姓的生命安全。";
        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("model", "CNN");
        
        JSONObject res = client.simnet(test11, test12, options);
    	System.out.println(res.toString(2));
        
    	*/
		// 短文本相似度
        /*for(int i=0;i<test.length;i++ ){
        	JSONObject res = client.simnet(test[i], test[i+1], options);
        	System.out.println(res.get("score"));
            System.out.println(res.toString(2));
        }
        */
    	
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	options.put("model", "CNN");
    	FileInputStream input = new FileInputStream(new File("D:/data/2.txt"));
		BufferedReader reader=new BufferedReader(new InputStreamReader(input,"utf-8"));
		String line = reader.readLine();
		int a =1;
		String first = "";
		while(null != line){
			if(a > 1){
				if("".equals(first)){
					System.out.println("空");
					first = line;
				}	
				else{
					JSONObject res = client.simnet(first, line, options);
					System.out.println(res.toString(2));
					Double score =  (Double) res.get("score");
					//System.out.println(res.get("score"));
					
					/*
					if(score > 0.2){
						System.out.println("�й�");
					}else{
						System.out.println("�޹�");
					}
					*/
				}
					
			}
			first = line;
			line = reader.readLine();
			
			a++;
			Thread.sleep ( 1000 ) ;
		}
    }
}
