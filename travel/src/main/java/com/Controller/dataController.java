package com.Controller;

import com.Bean.JingdianNum;
import com.Bean.TravelBean;
import com.Bean.TravelBean1;
import com.Bean.UserBean;
import com.Dao.JingdianDao;
import com.Dao.TravelDao;
import com.Dao.UserDaoImp;
import com.Service.JingdianService;
import com.Service.UserService;

import com.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Controller
public class dataController {


    @Autowired //自动注入
    private UserService userService;
    private JingdianService jigndianService;




    @RequestMapping(value="/general")
    public String mgeneral() throws Exception {
        System.out.println("/general");
        return "travel/general";
    }


    @RequestMapping(value="/baidu")
    public String baidu() throws Exception {
        System.out.println("/baidu");
        return "map/baidu";
    }

    @RequestMapping(value="/indexMap")
    public String indexMap() throws Exception {
        System.out.println("/baidu");
        return "Test/indexMap";
    }



    @RequestMapping(value="/login1")
    @ResponseBody//返回json格式数据
    public int login1(HttpServletRequest req, String username,String password) throws Exception {
        System.out.println("login1 username:"+username+" password："+password);
        //List<UserBean> userlist = userService.search(username);
        UserBean userbean = (UserBean) req.getSession().getAttribute("userbean");
        int a = 0;
        System.out.println("userlist: "+userbean);
        if(userbean.getPassword().equals(password)){
            System.out.println("login1判断成功 "+userbean);
            req.getSession().setAttribute("user",userbean);
            a = 1;
        }
        else  {
            System.out.println("login1失败 "+userbean);
            a = 0;
        }
        System.out.println(a);
        return a;
    }

    /**
     * 查询数据库中的游记，列表显示
     * @param model
     */
    @RequestMapping(value="/list")
    //HttpServletRequest req
    public String list(Model model) throws Exception {
        System.out.println("/list");
        List<TravelBean> list =  userService.TravelList();
        model.addAttribute("travelbeans",list);
        return "travel/list";
    }

    String content = "";
    @RequestMapping(value="/detail")
    public String detail(Model model,Integer id,String city) throws Exception {
        System.out.println("/detail?id="+ id);
        TravelDao dao = new TravelDao();
        TravelBean1 travel =  dao.searchTravel(id);//userService.searchTravel(id).get(0);
        content = travel.getContent();
        List<String> list = new ArrayList<>();
        for (String l:content.split("\n")) {
            list.add(l);
        }
        //System.err.println(list);
        model.addAttribute("travel",list);
        model.addAttribute("city",city);
        return "travel/detail1";
    }

    /**
     * 查询数据库中的所有游记信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/travelList")
    @ResponseBody//返回json格式数据
    public List<TravelBean> travelList() throws Exception {
        System.out.println("/travelList");
        List<TravelBean> travelList = userService.searchTravel(null);
        return travelList;
    }

    /**
     * 返回游记的具体内容
     * 可以直接读取文件
     * 也可以从数据库中读取
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/listTxt")
    @ResponseBody//返回json格式数据
    public List<String> listTxt() throws IOException {
        System.out.println("/listTxt");
        //直接读取文件
        List<String> list = new UserDaoImp().listTxt();
        for (String l : list) {
            System.out.println("control :"+l);
        }
        return list;
    }


    @RequestMapping(value="/getUsername")
    @ResponseBody//返回json格式数据
    public UserBean getUsername(HttpServletRequest req) throws Exception {
        System.out.println("/getUsername");
        UserBean userbean = (UserBean) req.getSession().getAttribute("userbean");
        return userbean;
    }

    @RequestMapping(value="/getAddress")
    @ResponseBody//返回json格式数据
    public List<String> getAddress(String address) throws Exception {
        System.out.println("address:"+address);
        String url = "http://gc.ditu.aliyun.com/geocoding?a="+address;
        System.out.println("url:"+url);
        URL thisurl = new URL(url); // 把字符串转换为URL请求地址
        HttpURLConnection connection = (HttpURLConnection) thisurl.openConnection();// 打开连接
        connection.connect();// 连接会话
        // 获取输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "UTF-8"));
        String line;
        List<String> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[]  word = line.split(",");
            String jing = word[0].split(":")[1];
            String wei = word[5].split(":")[1].split("}")[0];
            list.add(jing);
            list.add(wei);
            System.out.println("经："+jing+" wei:"+wei);
        }
        br.close();// 关闭流
        connection.disconnect();// 断开连接
        return list;
    }

    @RequestMapping(value="/jingdianNum")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> jingdianNum() throws Exception {
        JingdianDao dao = new JingdianDao();
        System.out.println("查询所有的 景点数量");
        List<JingdianNum> list = dao.jingdianNumList();
        System.out.println("查询所有的 景点数量"+list.toString());
        return list;
    }

    /**
     * 查询各种类型的游记  按景点，购物，美食，住宿
     * @param model
     */
    @RequestMapping(value="/search")
    //HttpServletRequest req
    public String search(Model model) throws Exception {
        System.out.println("/list");
        //List<TravelBean> list =  userService.TravelList();
        //model.addAttribute("travelbeans",list);
        return "travelcity";
    }

    /**
     * 找到每篇文章中出现的景点
     * 找出景点的经纬度
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/articleJing")
    @ResponseBody//返回json格式数据
    public List<JingdianNum> articleJing(Model model,String city) throws Exception {
        System.out.println("/articleJing");
        //获取文章，清洗数据，找到景点
        content.replaceAll("http(s)?://[a-zA-Z0-9/\\\\.#=_-]+","")
        .replaceAll("\\n","");
        System.out.println("内容2："+content+" city:"+city);
        //找到一个集合，获取对应的经纬度
        JingdianDao dao = new JingdianDao();
        List<JingdianNum> jingdianNums = new ArrayList<>();
        List<String> jingdians = FileUtil.JingdianList(content,city);
        System.out.println("景点："+jingdians.toString());
        for(String j : jingdians){
            System.out.println("articleJing:"+j);
            JingdianNum jingdianNum = dao.JingdianDetail(j);
            if(jingdianNum != null)
                jingdianNums.add(jingdianNum);
        }
        System.out.println("jing:"+jingdianNums.toString());
        return jingdianNums;
    }

}

