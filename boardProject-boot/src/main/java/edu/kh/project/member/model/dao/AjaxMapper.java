package edu.kh.project.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface AjaxMapper {

	String selectMemberTel(String nickname);

	Member selectMember(String email);

	int duplicationCheck(String email);

	int duplicationCheckNickname(String nickname);

}
