package com.cafe24.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cafe24.mysite.exception.UserDaoException;
import com.cafe24.mysite.service.UserService;
import com.cafe24.mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	// 회원가입
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join() {
		return "user/join";
	}
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(@ModelAttribute UserVo userVo) {
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
	@RequestMapping("/joinsuccess")
	public String joinSuccess() {
		return "user/joinsuccess";
	}
	
	// 로그인
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(
			@RequestParam(value = "email", required = true, defaultValue = "") String email,
			@RequestParam(value = "password", required = true, defaultValue = "") String password,
			HttpSession session,
			Model model) {
		
		UserVo authUser = userService.getUser(new UserVo(email, password));
		
		if(authUser == null) {
			model.addAttribute("result", "fail");
			return "user/login";
		}
		// session 처리
		session.setAttribute("authUser", authUser);
		
		return "redirect:/";
	}
	
	// 로그아웃
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("authUser");
		session.invalidate();
		
		return "redirect:/";
	}
	
	// 개인정보 수정
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(HttpSession session, Model model) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		model.addAttribute("userVo", userService.getUser(authUser.getNo()));
		
		return "user/update";
	}
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(
			@ModelAttribute UserVo userVo,
			HttpSession session,
			Model model) {
		
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		
		if(authUser == null) {	// 로그인 정보가 없는데 수정하려고 할 때
			return "redirect:/";
		}
		
		userVo.setNo(authUser.getNo());
		userService.update(userVo);
		
		// 수정 후 바로 반영을 위해 수정된 userVo를 세션의 속성 테이블에 등록함
		session.setAttribute("authUser", userVo);
		
		return "redirect:/user/updatesuccess";
	}
	@RequestMapping("/updatesuccess")
	public String updateSuccess() {
		return "user/updatesuccess";
	}
	
//	@ExceptionHandler( Exception.class )
//	public String handleUserDaoException() {
//		System.out.println("예외 발생!!!!!!!!!!!!!!!!");
//		return "error/exception";
//	}

}
