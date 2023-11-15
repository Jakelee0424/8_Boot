package edu.kh.project.myPage.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MyPageMapper {

	int update(Member updateMember);

	String selectPw(Map<String, String> map);

	int updatePw(Map<String, String> map);

	int delete(Map<String, String> map);

	int updateProfileImage(Member loginMember);

}
