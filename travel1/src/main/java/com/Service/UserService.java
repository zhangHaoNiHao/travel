package com.Service;

import com.Bean.TravelBean;
import com.Bean.UserBean;
import com.Dao.UserDao;
import com.Dao.UserDaoImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {

	@Autowired
	private UserDaoImp userDao;

	public boolean checkIsExit(String username) throws Exception {
		return userDao.checkIsExit(username);
	}
	
	public List<UserBean> search(String username) throws Exception{
		return userDao.search(username);
	}
	public List<TravelBean> searchTravel(Integer id) throws Exception{
		return userDao.searchTravel(id);
	}
	public List<TravelBean> TravelList() throws Exception{
		return userDao.TravelList();
	}

}
