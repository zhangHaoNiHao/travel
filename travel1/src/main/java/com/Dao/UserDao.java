package com.Dao;


import com.Bean.TravelBean;
import com.Bean.UserBean;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserDao {

    public Boolean checkIsExit(String username) throws Exception;
    public List<UserBean> search(String username) throws Exception;
    public List<UserBean> Userlist() throws Exception;
    public Boolean delete(String username) throws Exception;
    public List<TravelBean> TravelList() throws Exception;
    public List<TravelBean> searchTravel(Integer id) throws Exception;
}
