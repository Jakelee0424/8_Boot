package edu.kh.project.common.filter;

import java.io.IOException;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// override로 메서드를 가져오면서 매개변수가 정해져 있기 때문에
		// 필터에서는 RedirectAttributes 를 사용할 수 없음

		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		HttpSession session = req.getSession();
		
		
		if(session.getAttribute("loginMember") == null) {
			
			resp.sendRedirect("/loginError");
			
			
		} else {
			
			chain.doFilter(request, response);
			// 다음 필터 또는 컨트롤러로 이동
			
		}
		
		
		
	}

	
}
