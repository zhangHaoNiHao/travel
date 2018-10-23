package com.Controller;

import com.Bean.TravelBean;
import com.Bean.UserBean;
import com.Dao.UserDaoImp;
import com.Service.UserService;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Controller
public class dataController {


    @Autowired //自动注入
    private UserService userService;

    @RequestMapping(value="/index")
    public String index(HttpServletRequest req) throws Exception {
        System.out.println("/index");
        return "index";
    }
    @RequestMapping(value="/map")
    public String map(HttpServletRequest req) throws Exception {
        System.out.println("/map");
        return "map";
    }

    @RequestMapping(value="/map2")
    public String map2(HttpServletRequest req) throws Exception {
        System.out.println("/map2");
        return "map2";
    }

    @RequestMapping(value="/map3")
    public String map3(HttpServletRequest req) throws Exception {
        System.out.println("/map3");
        return "map3";
    }
    @RequestMapping(value="/map4")
    public String map4(HttpServletRequest req) throws Exception {
        System.out.println("/map4");
        return "map4";
    }

    @RequestMapping(value="/general")
    public String mgeneral(HttpServletRequest req) throws Exception {
        System.out.println("/general");
        return "general";
    }


    @RequestMapping(value="/baidu")
    public String baidu(HttpServletRequest req) throws Exception {
        System.out.println("/baidu");
        return "baidu";
    }

    @RequestMapping(value="/login")
    public String login() throws Exception {
        System.out.println("/login");
        return "login";
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
        return "list";
    }

    @RequestMapping(value="/detail")
    //HttpServletRequest req
    public String detail(Model model,Integer id) throws Exception {
        System.out.println("/detail?id="+ id);
        //获取游记title
        //根据游记title查询游记信息
        TravelBean bean =  userService.searchTravel(id).get(0);
        List<String> list = new ArrayList<>();
        for (String l:bean.getTravel().split("\n")) {
            list.add(l);
        }
        System.err.println(list);
        model.addAttribute("travel",list);
        return "detail1";
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
        /*
        for(TravelBean travelBean : travelList){
            String content = travelBean.getTravel();
            Integer id = travelBean.getId();
            System.out.println("id: "+id+"  content: "+content);
        }
        */
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

    @RequestMapping(value="/checkIsExit")
    @ResponseBody//返回json格式数据
    public int checkIsExit(String username,HttpServletRequest req) throws Exception {
        System.out.println("checkIsExit用户:"+ username);
        List<UserBean> list = userService.search(username);
        System.out.println(list.toString());
        int a = 0;
        if(null != list && list.size() > 0){
            //将数据存到session中
            a = 1;
            System.out.println("list不为空 ："+list.toString());
            req.getSession().setAttribute("userbean",list.get(0));
            System.out.println("checkIsExit用户"+list.toString());
        }
        else{
            a = 0;
        }
        return a;
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
}

