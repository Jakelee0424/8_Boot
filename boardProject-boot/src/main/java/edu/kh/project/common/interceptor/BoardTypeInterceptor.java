package edu.kh.project.common.interceptor;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.project.common.board.model.service.BoardService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 인터셉터 : 요청/응답을 가로채는 객체
// Client <-> Dispatcher Servlet <-> (Interceptor) <-> Controller

// Filter : 전반적으로 쓰일 보안/인증/인가 관련 작업, 문자열 인코딩
// Interceptor :  세부적으로 쓰일 보안/인증, 요청에 대한 데이터 가공

public class BoardTypeInterceptor implements HandlerInterceptor {

	@Autowired
	private BoardService service;
	
	/** dispatcher servlet -> controller 사이
	 *
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// application scope 내장 객체 얻어오기
		ServletContext application = request.getServletContext();
		
		
		// application scope에 BOARD_TYPE이 조회되어 세팅되지 않았다면 
		// == 서버 시작 후 누구도 요청을 한 적이 없을 경우
		if(application.getAttribute("boardTypeList") == null) { 
			
			// 조회 서비스 호출
			List< Map<String, Object>> boardTypeList = service.selectBoardTypeList();
			
			// application scope에 세팅
			application.setAttribute("boardTypeList", boardTypeList);
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	/** controller -> dispatcher servlet 사이
	 *
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/** view resolver -> dispatcher servlet 사이
	 *
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

	
	
}
