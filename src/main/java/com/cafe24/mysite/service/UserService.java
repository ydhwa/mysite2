package com.cafe24.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.UserDao;
import com.cafe24.mysite.vo.UserVo;

// service는 root application context에 설정한다.
@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;

	public Boolean join(UserVo userVo) {
		// 원래 insert는 count를 return해줘야 함
		return userDao.insert(userVo);
	}

	public UserVo getUser(UserVo userVo) {
		return userDao.get(userVo.getEmail(), userVo.getPassword());
	}
	
	public UserVo getUser(Long userNo) {
		return userDao.get(userNo);
	}

	public Boolean update(UserVo userVo) {
		return userDao.update(userVo);
	}
	
}
