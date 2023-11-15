package edu.kh.project.member.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.member.model.dto.Member;

@Repository
public class MemberDAO {

	@Autowired
	private MemberMapper memberMapper;
	
	public Member login(Member inputMember) {
		
		return memberMapper.login(inputMember);
	}
	
	public int signUp(Member inputMember) {
		
		return memberMapper.signUp(inputMember);
		
	}
	
}
