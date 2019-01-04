package com.Controller;

import com.Bean.UserBean;
import com.Dao.UserDaoImp;
import com.Service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class userController {

    @Autowired
    private UserService userService;

    private UserDaoImp userdao = new UserDaoImp();


    @RequestMapping(value="/login")
    public String login() {
        System.out.println("/login");
        return "login/login";
    }

    @RequestMapping(value="/index1")
    public String index1() {
        System.out.println("/index1");
        return "index/index1";
    }

    @RequestMapping(value="/index")
    public String index() {
        System.out.println("/index");
        return "index/indexMap";
    }


    /**
     * 判断用户是否存在
     */
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


    /**
     * 显示所有的用户
     */
    @RequestMapping(value="/Userlist")
    public String Userlist(Model model) throws Exception {
        System.out.println("/所有用户");
        List<UserBean> userlist = userdao.Userlist();
        model.addAttribute("UserBeans",userlist);
        return "user/userlist";
    }

    /**
     * 显示用户信息
     */
    @RequestMapping(value="/UserInfo")
    public String UserInfo(int userid,Model model) throws Exception {
        System.out.println("/用户信息");
        UserBean userBean = userdao.getUserInfoById(userid);
        model.addAttribute("UserBean",userBean);
        return "Test/userinfo1";
    }

    /**
     * 权限管理
     */
    @RequestMapping(value="/RightM")
    public String RightM() {
        System.out.println("/权限管理");
        return "Test/EcharsMap4_3Point2";
    }



}
