package edu.kh.project.member.model.service;

import edu.kh.project.member.model.dto.Member;

public interface AjaxService {

	/** 닉네임으로 전화번호 조회
	 * @param nickname
	 * @return
	 */
	String selectMemberTel(String nickname);

	
	Member selectMember(String email);


	int duplicationCheck(String email);


	int duplicationCheckNickname(String nickname);

	
	
}
