package edu.kh.project.common.board.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.common.board.model.dto.Board;
import edu.kh.project.common.board.model.dto.BoardImage;
import edu.kh.project.common.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/board")
@SessionAttributes({"loginMember"})
public class BoardController {

	@Autowired
	private BoardService service; 
	
	/*
	 * 	ex) 조회 주소 예시
	 * 
	 *  목록조회 : /board/1?cp=1 (current page)
	 *  상세조회 : /board/1/1500?cp=1 
	 * 
	 * 	컨트롤러 따로 생성
	 *  삽입 : /board2/1/insert
	 * 	수정 : /board2/1/1500/update
	 * 	삭제 : /board2/1/1500/delete
	 * 
	 * 
	 * */
	
	/*
	 * @PathVariable
	 * - url 경로에 있는 값을 매개변수로 이용할 수 있게 하는 어노테이션
	 *   + request scope에 자동 세팅
	 * 
	 * - url 값을 '/' 로 구분 : 자원의 구분 혹은 식별 
	 *    ex) github.com/jaekyung
	 *    ex) github.com/test
	 *    
	 * - url 값을 쿼리스트링으로 구분 : 검색, 정렬, 필터링
	 * 	  ex) search.naver.com?query=날씨
	 * 	  ex) search.naver.com?query=맛집
	 * 	  ex) /board2/insert?code=1
	 * 	  ex) /board2/insert?code=2
	 * 	  ex) /board2/list?order=recent
	 * 	  ex) /board2/list?order=most
	 *   
	 * 
	 * 
	 * ** @PathVariable 사용시 문제점과 해결법
	 *  
	 * 문제점 : 요청주소와 @PathVariable의 주소의 레벨이 같으면 
	 * 			구분없이 모두 매핑
	 * 
	 * 해결법 : @PathVariable 지정 시 정규 표현식 사용
	 * 			{키 : 정규표현식}
	 * 
	 * 
	 * 
	 * */
	
	/** 게시글 목록조회 
	 * @param boardCode
	 * @param cp
	 * @return
	 */
	@GetMapping("/{boardCode:[0-9]+}") //boardCode는 1자리 이상 숫자
	public String selectBoardList( @PathVariable("boardCode") int boardCode,
								   @RequestParam(value="cp", required=false , defaultValue="1") int cp,
								   Model model,
								   @RequestParam Map<String, Object> paramMap
			) {

		
		if(paramMap.get("key") == null) {
		
			// 게시글 목록 조회 서비스 호출 
			Map<String, Object> map = service.selectBoardList(boardCode, cp);
			
			// 조회 결과물 request scope에 세팅 후 forward
			model.addAttribute("map", map);
		
		} else {
			
			paramMap.put("boardCode", boardCode);
			
			Map<String, Object> map = service.selectBoardList(paramMap, cp);

			model.addAttribute("map", map);
		}
			
		return "board/boardList";
	}
	
	/** 게시글 상세 조회
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping("/{boardCode}/{boardNo}")
	public String boardDetail(
			@PathVariable("boardCode") int boardCode, 
			@PathVariable("boardNo") int boardNo, 
			Model model,
			RedirectAttributes ra,
			@SessionAttribute(value="loginMember", required=false) Member loginMember,
			HttpServletRequest req,
			HttpServletResponse resp
			) throws ParseException {
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		
		Board board = service.selectBoard(map);
		
		String path = null;
		
		if(board != null) {
			
			int result = 0;
			
			if(loginMember != null) {
			
				
				int memberNo = loginMember.getMemberNo();
				
				map.put("memberNo", memberNo);
				
				result = service.selectLike(map);
				
				 if(result > 0) model.addAttribute("likeCheck", "on");

			} 		
			model.addAttribute("result", result);
				
			
			
			// ------ 쿠키를 이용한 조회 수 증가 ------------------
			
			// 1) 비회원 또는 로그인한 회원의 글이 아닌 경우
			
			if(loginMember == null || loginMember.getMemberNo() != board.getMemberNo() ) {
				
			// 2) 쿠키 얻어오기

				Cookie c = null;
				
				Cookie[] cookies = req.getCookies();  // 현존하는 쿠키 조회
				
				if(cookies != null) { // 쿠키가 있으면 c에 저장
					
					for(Cookie cookie : cookies) { 
						if(cookie.getName().equals("readBoardNo")) {
							c = cookie;
							break;
						}
					}
					
				}
			
			// 3) 기존 쿠키가 없거나, 존재는 하나 게시글 번호가 저장되지 않은 경우(본적 없는 경우)	
				
				int res = 0;
				
				if(c == null) {
					
					c = new Cookie("readBoardNo", "|" + boardNo + "|");
					
					res = service.updateReadCount(boardNo);
					
				} else {
					
					// cookie.getValue : 쿠키에 저장된 모든 값 읽어옴 -> String으로 반환
					// indexOf("문자열") : 찾는 문자열이 몇번 인덱스에 존재하는지 반환, 없으면 -1 반환
					if(c.getValue().indexOf("|" + boardNo + "|") == -1 ) {
						
						c.setValue( c.getValue() + "|" + boardNo + "|");
						
						res = service.updateReadCount(boardNo);
						
					}
					
				}
				
				if(res > 0) {
					board.setReadCount(board.getReadCount() + 1);
					// 조회된 board 조회 수와 DB 조회 수 동기화
					
					// 적용 경로 설정
					c.setPath("/"); //  "/" 이하 경로 요청 시 쿠키 서버로 전달
					
					// 수명 지정
					Calendar cal = Calendar.getInstance(); // 싱글톤 패턴
					cal.add(cal.DATE, 1);
					
					// 날짜 표기법 변경 객체 (DB의 TO_CHAR()와 비슷)
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
					// java.util.Date
					Date a = new Date(); // 현재 시간
					
					Date temp = new Date(cal.getTimeInMillis()); // 내일 (24시간 후)
					// 2023-05-11 12:16:10
					
					Date b = sdf.parse(sdf.format(temp)); // 내일 0시 0분 0초
					
					
					// 내일 0시 0분 0초 - 현재 시간
					long diff = (b.getTime()  -  a.getTime()) / 1000; 
					// -> 내일 0시 0분 0초까지 남은 시간을 초단위로 반환
					
					c.setMaxAge((int)diff); // 수명 설정
					
					resp.addCookie(c); // 응답 객체를 이용해서
									   // 클라이언트에게 전달
				}
				
			}
			
			
			
			
			// ----------------------------------------------------
			
			
			
			
			
			path = "board/boardDetail";
			model.addAttribute("board", board);
			
			if(!board.getImageList().isEmpty()) {
				
				BoardImage thumbnail = null;
				
				if(board.getImageList().get(0).getImageOrder() == 0) {
					
					thumbnail = board.getImageList().get(0);
					
				}
					
				model.addAttribute("thumbnail", thumbnail);
			
				model.addAttribute("start", thumbnail != null ? 1 : 0);
				
			}
				
		} else {
			
			path = "redirect:/board/" + boardCode;
			
			ra.addFlashAttribute("msg", "조회된 게시글이 없습니다.");
		}
		
		
		
		return path;
	}

	/** 좋아요 처리
	 * @return
	 */
	@PostMapping("/like")
	@ResponseBody
	public int like(@RequestBody Map<String, Integer> paramMap) {
		
		return service.like(paramMap);
	}
	
}
