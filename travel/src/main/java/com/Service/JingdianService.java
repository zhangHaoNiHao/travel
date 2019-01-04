package com.Service;

import com.Bean.JingdianNum;
import com.Bean.TravelBean;
import com.Bean.UserBean;
import com.Dao.JingdianDao;
import com.Dao.UserDaoImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JingdianService {

	@Autowired
	private JingdianDao jingdianDao;

	public List<JingdianNum> jingdianNumList() throws Exception{
		return jingdianDao.jingdianNumList();
	}


}
