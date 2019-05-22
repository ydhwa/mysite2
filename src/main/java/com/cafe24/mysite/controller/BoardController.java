package com.cafe24.mysite.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
		
		BoardVo boardVo = boardService.view(no, "view", (List)session.getAttribute("viewList"));
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
		
		BoardVo boardVo = boardService.view(no, "update", null);
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
		BoardVo thisBoardVo = boardService.view(no, "", null);
		if(authUser.getNo() != thisBoardVo.getUserNo()) {
			return "redirect:/board/list";
		}
		
		boardVo.setNo(no);
		boardService.updateBoard(boardVo);
		
		return "redirect:/board/list";
	}
	
	// 게시글 삭제
	@RequestMapping(value = "/delete/{no}")
	public String delete(
			HttpSession session,
			@PathVariable("no") Long no) {
		boardService.deleteBoard(no);
		
		return "redirect:/board/list";
	}
	
	private static final String SAVE_PATH = "/mysite-uploads";
	private static final String URL = "/images";
	
	// 파일 업로드
	@RequestMapping(value = "/fileupload", method = RequestMethod.POST)
	@ResponseBody
	public String multiplePhotoUpload(
			@RequestHeader("file-name") String originalName,
			@RequestHeader("file-size") String fileSize,
			MultipartHttpServletRequest request) {
		
//		try {
			// 파일정보
			String extName = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
			String saveName = generateSaveFileName(extName);
			
			Iterator<String> itr = request.getFileNames();
			if(itr.hasNext()) {
			
				MultipartFile mpf = request.getFile(itr.next());
				System.out.println(mpf.getOriginalFilename());
			}
		
//			InputStream is = request.getInputStream();
//			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveName);
//			int numRead;
//			byte[] fileData = new byte[Integer.parseInt(fileSize)];
//			while((numRead = is.read(fileData, 0, fileData.length)) != -1) {
//				os.write(fileData);
//			}
//			if(is != null) {
//				is.close();
//			if(os != null) {
//				os.flush();
//				os.close();
//			}
//		File file = new File(filePath);
//		
//		// 디렉터리(서버)에 파일 저장
//		InputStream is = request.getInputStream();
//		byte[] fileData = new byte[Integer.parseInt(request.getHeader("file-size"))];
//		int numRead;
//			while((numRead = is.read(fileData, 0, fileData.length)) != -1) {
//				System.out.println('*');
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		return "";
	}
	private String generateSaveFileName(String extName) {
		String filename = "";
		Calendar calendar = Calendar.getInstance();

		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName);

		return filename;
	}
}
