package com.cafe24.mysite.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cafe24.mysite.service.BoardService;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	// 게시판 조회
	@RequestMapping({"/list", ""})
	public String list(Model model) {
		List<BoardVo> list = boardService.getBoards();
		model.addAttribute("list", list);
		
		return "board/list";
	}
	
	// 게시글 추가
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpSession session) {
		// 접근제어 - 로그인을 한 사용자만 게시글을 남길 수 있다.
		// 비로그인자는 아무리 눌러도 게시판으로 리다이렉트된다.
		if(session == null || session.getAttribute("authUser") == null) {
			return "redirect:/board/list";
		}
		
		return "board/add";
	}
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(
			@ModelAttribute BoardVo boardVo,
			HttpSession session) {
		// 접근제어 - 로그인을 한 사용자만 게시글을 남길 수 있다.
		// add form 접근 시 거르긴 한다.
		if(session == null || session.getAttribute("authUser") == null) {
			return "redirect:/board/list";
		}
		
		boardVo.setUserNo(((UserVo) session.getAttribute("authUser")).getNo());
		boardService.addBoard(boardVo);
		return "redirect:/board/list";
	}
	
	// 게시글 상세 조회
	@RequestMapping("/view/{no}")
	public String view(@PathVariable("no") Long no, Model model) {
		BoardVo boardVo = boardService.getBoard(no);
		model.addAttribute("vo", boardVo);
		
		return "board/view";
	}
	
	// 게시글 수정
	@RequestMapping(value = "/update/{no}", method = RequestMethod.GET)
	public String update(
			HttpSession session,
			Model model,
			@PathVariable("no") Long no) {
		// 접근제어 - 로그인을 한 사용자만 게시글을 남길 수 있다.
		// 비로그인자는 아무리 눌러도 게시판으로 리다이렉트된다.
		if(session == null) {
			return "redirect:/board/list";
		}
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board/list";
		}
		// 글의 주인이 아니면 수정 권한이 없다.
		BoardVo boardVo = boardService.getBoard(no);
		if(authUser.getNo() != boardVo.getUserNo()) {
			return "redirect:/board/list";
		}
		
		model.addAttribute("vo", boardVo);
		
		return "/board/update";
	}
	@RequestMapping(value = "/update/{no}", method = RequestMethod.POST)
	public String update(
			HttpSession session,
			@PathVariable("no") Long no,
			@ModelAttribute BoardVo boardVo) {
		// 접근제어 - 로그인을 한 사용자만 게시글을 남길 수 있다.
		// update form에서 처리하긴 한다.
		// 비로그인자는 아무리 눌러도 게시판으로 리다이렉트된다.
		if(session == null) {
			return "redirect:/board/list";
		}
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board/list";
		}
		// 글의 주인이 아니면 수정 권한이 없다.
		BoardVo thisBoardVo = boardService.getBoard(no);
		if(authUser.getNo() != thisBoardVo.getUserNo()) {
			return "redirect:/board/list";
		}
		
		boardVo.setNo(no);
		boardService.updateBoard(boardVo);
		
		return "redirect:/board/list";
	}
}
