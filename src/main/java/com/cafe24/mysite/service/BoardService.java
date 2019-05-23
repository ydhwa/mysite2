package com.cafe24.mysite.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cafe24.mysite.repository.BoardDao;
import com.cafe24.mysite.vo.BoardVo;
import com.cafe24.mysite.vo.FileVo;

@Service
public class BoardService {

	@Autowired
	private BoardDao boardDao;

	// pager
	private final int COUNT_PER_PAGE = 10;	// 페이지 당 보일 게시글 수
	private final int COUNT_PER_PAGER = 5;	// 페이지 당 보일 페이저 수 < N M O P Q >
	
	// 키워드와 현재 페이지에 따른 전체 게시글 조회
	public Map<String, Object> getListAndPager(int currPage, String keyword) {
		Map<String, Object> map = new HashMap<>();
		
		setPager(currPage, keyword, map);
		map.put("boardList", getList(currPage, keyword));
		
		return map;
	}
	private List<BoardVo> getList(int currPage, String keyword) {
		Map<String, Object> map = new HashMap<>();
		// Dao에서 getList를 호출하기 위한 값 세팅
		map.put("keyword", keyword);
		map.put("startIndex", (currPage - 1) * COUNT_PER_PAGE);
		map.put("countPerPage", COUNT_PER_PAGE);
		
		return boardDao.getList(map);
	}
	private void setPager(int currPage, String keyword, Map map) {
		int totalCount = boardDao.getListSize(keyword);
		int totalPageCount = (int) Math.ceil((double)totalCount / COUNT_PER_PAGE);
		int firstOfPager = ((int) ((currPage - 1) / COUNT_PER_PAGER)) * COUNT_PER_PAGER + 1;
		int prevPage = firstOfPager - COUNT_PER_PAGER < 1 ? -1 : firstOfPager - COUNT_PER_PAGER;
		int nextPage = firstOfPager + COUNT_PER_PAGER > totalPageCount ? -1 : firstOfPager + COUNT_PER_PAGER;
		// controller에게 반환할 값 세팅
		map.put("countPerPager", COUNT_PER_PAGER);
		map.put("totalPageCount", totalPageCount);
		
		map.put("startPage", firstOfPager);
		map.put("prevPage", prevPage == -1 ? prevPage : 1);
		map.put("nextPage", nextPage == -1 ? nextPage : totalPageCount);
		map.put("currPage", currPage);
		
		map.put("keyword", keyword);
	}
	
	// 게시글 상세조회 / 수정
	public BoardVo view(Long no, String mode, List<Long> viewList) {
		Map<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("mode", mode);
		
		// 수정 시에도 조회수가 업데이트 되면 곤란하다.
		if("view".equals(mode)) {
			// 비회원은 들어가는 족족 조회수가 올라간다.
			if(viewList == null) {
				boardDao.updateHit(no);
			}
			// 회원이 해당 세션이 처음 조회해보는 페이지면 조회수를 1 높인다.
			else if(!viewList.contains(no)) {
				viewList.add(no);
				boardDao.updateHit(no);
			}
		}

		return boardDao.get(map);
	}
	
	
	// 삽입
	public Boolean writeBoard(BoardVo boardVo) {
		Map<String, Object> map = new HashMap<>();
		map.put("groupNo", boardVo.getGroupNo());
		map.put("orderNo", boardVo.getOrderNo());
		
		// order 순서 재정렬 뒤에 삽입시켜줘야 한다!
		boardDao.updateOrderNo(map);
		return boardDao.insert(boardVo);
	}
	
	
	// 수정
	public Boolean updateBoard(BoardVo boardVo) {
		return boardDao.update(boardVo);
	}
	
	
	// 삭제
	public Boolean deleteBoard(Long no) {
		return boardDao.delete(no);
	}
	
	// 파일 업로드
	private static final String SAVE_PATH = "/mysite-uploads";
	private static final String URL = "/images";
	public FileVo fileUpload(MultipartFile multipartFile) {
		FileVo vo = null;
		try {
			if(multipartFile.isEmpty()) {
				return vo;
			}
			
			String originalName = multipartFile.getOriginalFilename();
			String extName = originalName.substring(originalName.lastIndexOf("." + 1));
			String saveName = generateSaveFileName(extName);
			long fileSize = multipartFile.getSize();
			
			byte[] fileData = multipartFile.getBytes();
			String path = SAVE_PATH + "/" + saveName;
			OutputStream os = new FileOutputStream(path);
			os.write(fileData);
			os.close();
			
			String url = URL + "/" + saveName;
			
			vo = new FileVo();
			vo.setOriginalName(originalName);
			vo.setExtName(extName);
			vo.setPath(path);
			vo.setSaveName(saveName);
			
		} catch(IOException e) {
			throw new RuntimeException("Fileupload ERROR: " + e);
		}
		return vo;
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
