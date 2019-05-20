package com.cafe24.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.BoardDao;
import com.cafe24.mysite.vo.BoardVo;

@Service
public class BoardService {

	@Autowired
	private BoardDao boardDao;

	// pager
	private final int COUNT_PER_PAGE = 3;
	private final int COUNT_PER_PAGER = 5;

	public List<BoardVo> getBoards(int currPage, String keyword) {
		//
		return boardDao.getList();
	}

	public BoardVo getBoard(Long no) {
		return boardDao.get(no);
	}

	public Boolean addBoard(BoardVo boardVo) {
		return boardDao.insert(boardVo);
	}

	public Boolean updateBoard(BoardVo boardVo) {
		return boardDao.update(boardVo);
	}


}
