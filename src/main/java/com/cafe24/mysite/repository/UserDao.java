package com.cafe24.mysite.repository;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.exception.UserDaoException;
import com.cafe24.mysite.vo.UserVo;

@Repository
public class UserDao {
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private DataSource dataSource;
	
	public Boolean insert(UserVo vo) {
//		System.out.println(vo);
		int count = sqlSession.insert("user.insert", vo);
//		System.out.println(vo);
		return 1 == count;
	}
	
	public Boolean update(UserVo vo) {
		int count = sqlSession.update("user.update", vo);
		return 1 == count;
	}
	
	// logout, update
	public UserVo get(Long no) {
		return sqlSession.selectOne("user.getByNo", no);
	}
	
	public UserVo get(String email, String password) throws UserDaoException {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("password", password);
		UserVo userVo = sqlSession.selectOne("user.getByEmailAndPassword", map);
		return userVo;
	}
	
	public String getName(Long no) {
		return sqlSession.selectOne("user.getNameByNo", no);
	}
	
	// 이메일 중복성 검사(이메일로 로그인 하기 때문)
	public Boolean getEmailValidation(String email) {
		return sqlSession.selectOne("user.getEmailValidation", email);
	}

}
