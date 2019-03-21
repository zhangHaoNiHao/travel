package com.Service;

import com.Bean.UserBean;
import com.Dao.UserDaoImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {

	@Autowired
	private UserDaoImp userDao;

	public List<UserBean> search(String username) throws Exception{
		return userDao.search(username);
	}

}
