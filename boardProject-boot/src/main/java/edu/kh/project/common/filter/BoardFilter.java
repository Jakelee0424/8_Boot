package edu.kh.project.common.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

public class BoardFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// override로 메서드를 가져오면서 매개변수가 정해져 있기 때문에
		// 필터에서는 RedirectAttributes 를 사용할 수 없음


		HttpServletRequest req = (HttpServletRequest) request;

		String[] arr = req.getRequestURI().split("/");

		try {

			String boardCode = arr[2];

			List<Map<String, Object>> boardTypeList
			= (List<Map<String, Object>>)(req.getServletContext().getAttribute("boardTypeList"));

			for(Map <String,Object> boardType : boardTypeList) {

				if((boardType.get("BOARD_CODE") + "").equals(boardCode)) {
					req.setAttribute("boardName", boardType.get("BOARD_NAME"));
				}
			}

		} catch(Exception e) {}

		chain.doFilter(request, response);
		
	}


}
