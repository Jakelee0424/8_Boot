package edu.kh.project.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.member.model.dto.Member;

@Repository // db연결 클래스임을 명시 + bean 등록
public class AjaxDAO {
	
	@Autowired // bean 중에서 타입이 같은 객체를 DI
	private AjaxMapper ajaxMapper;

	/** 닉네임으로 전화번호 조회
	 * @param nickname
	 * @return
	 */
	public String selectMemberTel(String nickname) {
		
		return ajaxMapper.selectMemberTel(nickname);
	}


	/** 이메일로 회원정보 조회
	 * @param email
	 * @return
	 */
	public Member selectMember(String email) {

		return ajaxMapper.selectMember(email);

	}
	
	public int duplicationCheck(String email) {
		
		return ajaxMapper.duplicationCheck(email);
	}


	public int duplicationCheckNickname(String nickname) {
		
		
		return ajaxMapper.duplicationCheckNickname(nickname);
	}

	
	

}
