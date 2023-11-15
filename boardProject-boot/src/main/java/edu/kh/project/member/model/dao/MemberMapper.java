package edu.kh.project.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MemberMapper {

	Member login(Member inputMember);
	// 메서드 이름 login -> 연결된 mapper.xml에서 id가 login인 sql  수행

	int signUp(Member inputMember);
	

}
