package com.cafe24.mysite.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// 이제 mysite2의 Exception은 전부 여기로 들어옴
	@ExceptionHandler( Exception.class )	// Exception이 발생하면 여길 불러줘!
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {	// Exception의 내용을 읽어봐야 해!
		// 1. 로깅
		e.getMessage();
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));	// 보조 스트림 이용하여 연결
//		LOGGER.error(errors.toString());
		System.out.println(errors.toString());
		
		// 2. 안내 페이지 가기 + 정상 종료(response)
		request.setAttribute("uri", request.getRequestURI());
		request.setAttribute("exception", errors.toString());
		request.getRequestDispatcher("/WEB-INF/views/error/exception.jsp").forward(request, response);
	}
}
