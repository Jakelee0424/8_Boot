package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller 
@RequestMapping("/member") 
@SessionAttributes({"loginMember"}) 
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	/** 로그인
	 * @param inputMember
	 * @param model
	 * @param referer
	 * @param ra
	 * @param saveId
	 * @param resp
	 * @return
	 */
	@PostMapping("/login")
	public String login(Member inputMember, Model model,
						@RequestHeader(value = "referer") String referer, 
						RedirectAttributes ra,
						@RequestParam(value = "saveId", required = false) String saveId,
						HttpServletResponse resp
			) {
		
		Member loginMember = service.login(inputMember);
		
		String path = "redirect:";
		
		if(loginMember != null) { 
			
			path += "/";
			
			model.addAttribute("loginMember", loginMember);
			
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());

			
			if(saveId != null) { 
				
				cookie.setMaxAge(60*60*24*30);
				
				
			}else { 
			
				cookie.setMaxAge(0);

			}
			
			cookie.setPath("/"); 
			
			resp.addCookie(cookie);
			
			
		} else { 

			path += referer;

			ra.addFlashAttribute("msg", "아이디 또는 비밀번호 불일치");
		}
		
		
		return path;
	}
	
	/** 로그아웃
	 * @param status
	 * @return
	 */
	@GetMapping("/logout")
	public String logout(SessionStatus status) {
		
		status.setComplete();
		
		return "redirect:/";
	}
	
	/** 회원가입 페이지 이동
	 * @return
	 */
	@GetMapping("/signUp")
	public String signUp() {
		
		return "member/signUp";
	}
	
	/** 회원가입
	 * @param inputMember
	 * @param memberAddress
	 * @param ra
	 * @return
	 */
	@PostMapping("/signUp")
	public String signUp(Member inputMember,
						 String[] memberAddress,
						 RedirectAttributes ra
			) {
		
		if(inputMember.getMemberAddress().equals(",,")) {
			inputMember.setMemberAddress(null);
		
		} else {
			
			String addr = String.join("^^^", memberAddress);		
			inputMember.setMemberAddress(addr);
			
		}
		
		int result = service.signUp(inputMember);
		
		String path = "redirect:";
		String msg = null;
		
		if(result > 0) {
			
			path += "/"; 
			msg = inputMember.getMemberNickname() + "님의 가입을 환영합니다!";
			
		}else {
			
			path += "signUp"; 
			
			msg = "회원가입 실패";
			
		}
		
		ra.addFlashAttribute("msg", msg);
		
		return path;
	}
	
	
	
}
