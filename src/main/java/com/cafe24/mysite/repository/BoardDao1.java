package com.cafe24.mysite.repository;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.BoardVo;

@Repository
public class BoardDao {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SqlSession sqlSession;

	public Boolean insert(BoardVo vo) {
		int count = sqlSession.insert("board1.insert", vo);
		return 1 == count;
	}

	public Boolean update(BoardVo vo) {
		int count = sqlSession.update("board1.update", vo);
		return 1 == count;
	}

	// updateform, view(update에서는 등록일과 조회수를 볼 필요 없지만 그냥 한번에 처리해버림)
	public BoardVo get(Long no) {
		return sqlSession.selectOne("board1.getByNo", no);
	}

	public List<BoardVo> getList() {
		return sqlSession.selectList("board1.getList");
	}


}
