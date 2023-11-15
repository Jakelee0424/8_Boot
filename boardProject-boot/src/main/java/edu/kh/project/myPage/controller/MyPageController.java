package edu.kh.project.myPage.controller;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import edu.kh.project.myPage.service.myPageService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/myPage")
@SessionAttributes({"loginMember"})
public class MyPageController {

	@Autowired
	myPageService service;

	/** 마이페이지 이동
	 * @return
	 */
	@GetMapping("/info")
	public String info() {

		return "/myPage/myPage-info";
	}

	/** 프로필 페이지 이동
	 * @return
	 */
	@GetMapping("/profile")
	public String profile() {

		return "/myPage/myPage-profile";
	}

	/** 비밀번호 변경 페이지
	 * @return
	 */
	@GetMapping("/changePw")
	public String changePw() {

		return "/myPage/myPage-changePw";
	}

	/** 탈퇴 페이지 이동
	 * @return
	 */
	@GetMapping("/secession")
	public String secession() {

		return "/myPage/myPage-secession";
	}



	/* MultipartFile : input type = "file"로 제출된 파일을 저장한 객체
	 * 
	 * [제공 메서드]
	 * - transferTo() : 파일을 지정된 경로에 저장(메모리 -> HDD / SDD)
	 * - getOriginalFileName() : 파일 원본명
	 * - getSize() : 파일 크기
	 * 
	 * */

	/** 프로필 사진 변경
	 * @return
	 */
	@PostMapping("/profile")
	public String updateProfile(@RequestParam("profileImage") MultipartFile profileImage, //업로드 파일
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra
			) throws Exception {

		// 1. 웹 접근 경로


		// 2. 프로필 이미지 수정 서비스 호출


		int result = service.updateProfile(profileImage, loginMember);

		String msg = null;

		if(result > 0) msg = "프로필 이미지가 변경되었습니다.";
		else msg = "프로필 변경 실패";

		ra.addFlashAttribute("msg", msg);

		return "redirect:profile";
	}

	/** 회원정보 수정
	 * @param loginMember
	 * @param updateMember
	 * @param memberAddress
	 * @param ra
	 * @return
	 */
	@PostMapping("/info")
	public String updateInfo(@SessionAttribute("loginMember") Member loginMember, // 세션에서 얻어온 "loginMember"에 해당하는 객체를 Member logingMember로 저장
			Member updateMember, // 수정할 닉네임, 전화번호가 담긴 객체
			String[] memberAddress, // name="memberAddress"인 input 3개의 값
			RedirectAttributes ra) { // 리다이렉트 시 값 전달용 객체



		if(updateMember.getMemberAddress().equals(",,")) {

			updateMember.setMemberAddress(null);

		} else {

			String addr = String.join("^^^", memberAddress);		
			updateMember.setMemberAddress(addr);

		}


		updateMember.setMemberNo(loginMember.getMemberNo());

		int result = service.updateInfo(updateMember);

		// 주소 합치기 -> updateMember에 세팅 -> 로그인한 회원의 번호를 updateMember에 세팅 -> Db 수정 -> 처리

		String msg = null;

		if(result > 0 ) {

			msg = "수정이 완료되었습니다";
			loginMember.setMemberNickname(updateMember.getMemberNickname());
			loginMember.setMemberTel(updateMember.getMemberTel());
			loginMember.setMemberAddress(updateMember.getMemberAddress());

		}else {

			msg = "회원 정보 수정이 실패했습니다.";

		}

		ra.addFlashAttribute("msg", msg);


		return "redirect:info";
	}

	/** 비밀번호 변경
	 * @return
	 */
	@PostMapping("/changePw")
	public String updatePw(@RequestParam(value = "currentPw", required = false) String currentPw, 
			@RequestParam(value = "newPw", required = false) String newPw, 
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra) {
		
		Map<String, String> map = new HashMap<String, String>();

		map.put("currentPw", currentPw);
		map.put("newPw", newPw);
		map.put("memberEmail", loginMember.getMemberEmail());

		int result = service.updatePw(map);

		String msg = null;

		if(result > 0 ) {

			msg = "비밀번호 수정이 완료되었습니다";

		}else {

			msg = "현재 암호가 일치하지 않습니다.";

		}

		ra.addFlashAttribute("msg", msg);

		return "redirect:changePw";
	}

	/** 회원 탈퇴
	 * @return
	 */
	@PostMapping("/secession")
	public String delete(String memberPw,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra,
			SessionStatus status
			) {

		Map<String, String> map = new HashMap<String, String>();

		map.put("memberPw", memberPw);
		map.put("memberEmail", loginMember.getMemberEmail());

		int result = service.delete(map);
		String msg = null;

		if (result > 0) {

			msg = "회원 탈퇴가 완료되었습니다.";
			status.setComplete();
			
		}else {

			msg = "암호가 일치하지 않습니다.";

		}

		ra.addFlashAttribute("msg", msg);

		return "redirect:/";
	}


}
