package com.cafe24.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cafe24.mysite.service.BoardService;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;

	// 게시글 리스트 조회 / 검색
	@RequestMapping(value = {"/list", ""})
	public String list(
			Model model,
			@RequestParam(value = "p", defaultValue = "1") int currPage,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			HttpSession session) {
		
		model.addAttribute("map", boardService.getListAndPager(currPage, keyword));
		
		return "board/list";
	}
	
	
	// 게시글 상세 조회
	@RequestMapping("/view/{no}")
	public String view(
			@PathVariable("no") Long no,
			HttpSession session,
			Model model) {
		BoardVo boardVo = boardService.view(no, "view");
		model.addAttribute("vo", boardVo);
		
		return "board/view";
	}
	
	
	// 글 작성 (일반 게시글)
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(
			@RequestParam(value = "groupno", defaultValue = "-1") int groupNo,
			@RequestParam(value = "orderno", defaultValue = "-1") int orderNo,
			@RequestParam(value = "depth", defaultValue = "-1") int depth,
			HttpSession session,
			Model model) {
		
		// 접근제어 - 로그인을 한 사용자만 게시글을 남길 수 있다.
		// list에서 거르긴 한다.
		if(session == null || session.getAttribute("authUser") == null) {
			return "redirect:/board";
		}
		
		model.addAttribute("groupNo", groupNo);
		model.addAttribute("orderNo", orderNo);
		model.addAttribute("depth", depth);
		
		return "board/write";
	}
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(
			@ModelAttribute BoardVo boardVo,
			HttpSession session) {
		boardVo.setUserNo(((UserVo) session.getAttribute("authUser")).getNo());
		boardService.writeBoard(boardVo);
		return "redirect:/board/list";
	}
	

	// 게시글 수정
	@RequestMapping(value = "/update/{no}", method = RequestMethod.GET)
	public String update(
			HttpSession session,
			Model model,
			@PathVariable("no") Long no) {
		// 접근제어 - 로그인을 한 사용자만 게시글을 남길 수 있다.
		// view에서 거르긴 한다.
		if(session == null) {
			return "redirect:/board/list";
		}
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board/list";
		}
		
		BoardVo boardVo = boardService.view(no, "update");
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
		if(session == null) {
			return "redirect:/board/list";
		}
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board/list";
		}
		// 글의 주인이 아니면 수정 권한이 없다.
		// view에서 처리하긴 한다.
		BoardVo thisBoardVo = boardService.view(no, "");
		if(authUser.getNo() != thisBoardVo.getUserNo()) {
			return "redirect:/board/list";
		}
		
		boardVo.setNo(no);
		boardService.updateBoard(boardVo);
		
		return "redirect:/board/list";
	}
	
	@RequestMapping(value = "/delete/{no}")
	public String delete(
			HttpSession session,
			@PathVariable("no") Long no) {
		boardService.deleteBoard(no);
		
		return "redirect:/board/list";
	}
}
