package edu.kh.project.member.model.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dao.MemberDAO;
import edu.kh.project.member.model.dto.Member;

@Service
public class MemberServiceImpl implements MemberService{

	private Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	
	@Autowired // 자동 연결
	private MemberDAO dao; // DI

	@Autowired // bean으로 등록된 객체 중 타입이 일치하는 객체를 DI
	private BCryptPasswordEncoder bcrypt;


	/** 로그인 서비스
	 *
	 */
	@Override
	public Member login(Member inputMember) {

		Member loginMember = dao.login(inputMember);
		
		if(loginMember != null) {
			
			if (bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
				
				loginMember.setMemberPw(null);
				
			} else {

				loginMember = null;
			
			}
			
		}
		
		return loginMember;
	}

	
	/** 회원가입 서비스
	 *
	 */
	@Override
	@Transactional
	public int signUp(Member inputMember) {
		
		String pw = bcrypt.encode(inputMember.getMemberPw());
		
		inputMember.setMemberPw(pw);
		
		return dao.signUp(inputMember);
	}
	
	
}
