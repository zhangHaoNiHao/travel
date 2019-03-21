package com.Controller;

import com.Bean.UserBean;
import com.Dao.UserDao;
import com.Dao.UserDaoImp;
import com.Service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        return "index/indexMap01";  //return "index/indexMap01";
    }

    @RequestMapping(value="/login1")
    @ResponseBody//返回json格式数据
    public int login1(HttpServletRequest req, String username,String password) throws Exception {
        System.out.println("login1 username:"+username+" password："+password);
        //List<UserBean> userlist = userService.search(username);
        UserBean userbean = (UserBean) req.getSession().getAttribute("user");
        int a = 0;
        System.out.println("userlist: "+userbean);
        if(userbean != null && userbean.getPassword().equals(password)){
            System.out.println("login1判断成功 "+userbean);
            req.getSession().setAttribute("user",userbean);
            req.getSession().setAttribute("juese",userbean.getJuese());
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
            req.getSession().setAttribute("user",list.get(0));
            req.getSession().setAttribute("juese",list.get(0).getJuese());
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
        System.out.println(userlist.get(0).getUsername());
        model.addAttribute("userBeans",userlist);
        return "user/userlist";
    }

    /**
     * 显示用户信息
     */
    @RequestMapping(value="/UserInfo")
    public String UserInfo() throws Exception {
        System.out.println("/UserInfo 用户信息");

        return "user/userinfo2";
    }

    /**
     * 权限管理
     */
    @RequestMapping(value="/updateRight")
    public String RightM(int id,Model model) throws Exception {
        System.out.println("/权限管理");
        System.out.println(id);
        //根据id查询该用户的信息
        UserDaoImp dao = new UserDaoImp();
        UserBean userBean = dao.getUserInfoById(id);
        model.addAttribute("userBean",userBean);
        model.addAttribute("id",id);
        return "user/userinfo3";
    }

    @RequestMapping(value="/UpdateUser")
    @ResponseBody
    public int Updateuser(String username,String email,int id,HttpServletRequest req) throws Exception {
        System.out.println("/UpdateUser");
        UserDaoImp dao = new UserDaoImp();
        int a = 0;
        a = dao.Updateuser(username,email,id);
        UserBean user = dao.getUserInfoById(id);
        req.getSession().setAttribute("user",user);
        req.getSession().setAttribute("juese",user.getJuese());
        return a;
    }

    @RequestMapping(value="/UpdateUser1")
    @ResponseBody
    public int Updateuser1(String username,String email,int id,String role,HttpServletRequest req) throws Exception {
        System.out.println("/UpdateUser1");
        UserDaoImp dao = new UserDaoImp();
        int a = 0;
        System.out.println("角色："+role);
        a = dao.Updateuser1(username,email,id,role);
        UserBean user = dao.getUserInfoById(id);
        req.getSession().setAttribute("user",user);
        req.getSession().setAttribute("juese",user.getJuese());
        return a;
    }
    //deleteuser
    @RequestMapping(value="/deleteuser")
    @ResponseBody
    public int deleteuser(int id,Model model) throws Exception {
        System.out.println("/deleteuser");
        UserDaoImp dao = new UserDaoImp();
        int a = 0;
        a = dao.deleteuser(id);
        return a;
    }

    @RequestMapping(value="/loginMobile")
    @ResponseBody
    public int loginMobile(String username,String password,HttpServletRequest req) throws Exception {
        System.out.println("/loginMobile");
        UserDaoImp dao = new UserDaoImp();
        UserBean user = dao.search(username).get(0);
        if(user != null && password.equals(user.getPassword())){
            req.getSession().setAttribute("userM",user);
            return 1;
        }else{
            return 0;
        }

    }

    @RequestMapping(value="/SearchUser")
    @ResponseBody
    public List<String> SearchUser(HttpServletRequest req) throws Exception {
        System.out.println("/SearchUser");
       /* UserDaoImp dao = new UserDaoImp();
        List<UserBean> user = dao.search(username);*/
        UserBean user = (UserBean) req.getSession().getAttribute("userM");
        if(user != null){
            String username = user.getUsername();
            String email = user.getEmail();
            String role = user.getJuese();
            System.out.println(email+"dd"+role);
            List<String> list = new ArrayList<>();
            list.add(email+","+role+","+username);
            return list;
        }else{
            return null;
        }
    }

    /**
     * 修改密码
     * @param req
     * @throws Exception
     */
    //UpdatePwd
    @RequestMapping(value="/UpdatePwd")
    @ResponseBody
    public String UpdatePwd(String currpwd,String newpwd,String username) throws Exception {
        System.out.println("/UpdatePwd");
        UserDaoImp userDaoImp = new UserDaoImp();
        //根据用户名获取用户
        UserBean user = userDaoImp.search(username).get(0);
        System.out.println(user.toString());

        String password = user.getPassword();
        Integer id = user.getId();
        System.out.println("id "+id);
        if(password.equals(currpwd)){
            //密码正确
            System.out.println("密码正确");
            Integer a = userDaoImp.UpdatePassword(id,newpwd);
            System.out.println("aaa  "+a);
            if(a>0){ //修改成功
                return "1";
            }else{ //修改失败
                return "0";
            }
        }else{//密码错误
            System.out.println("密码错误");
            return "2";
        }
    }


}
