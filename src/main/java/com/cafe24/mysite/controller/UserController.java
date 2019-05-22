package com.cafe24.mysite.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String join(		// validation check
			@ModelAttribute @Valid UserVo userVo,
			BindingResult result,
			Model model) {
		
		if(result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for(ObjectError error: list) {
				System.out.println(error);
			}
			model.addAllAttributes(result.getModel());
			
			return "user/join";
		}
		
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
		List<Integer> viewList = new ArrayList<>();	// 들렀던 페이지 목록
		
		if(authUser == null) {
			model.addAttribute("result", "fail");
			return "user/login";
		}
		// session 처리
		session.setAttribute("authUser", authUser);
		session.setAttribute("viewList", viewList);
		
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
		
		session.setAttribute("authUser", userService.update(userVo));
		model.addAttribute("result", "success");
		
		return "user/update";
	}
}
