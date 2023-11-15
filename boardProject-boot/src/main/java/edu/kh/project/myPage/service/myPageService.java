package edu.kh.project.myPage.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;

public interface myPageService {
	
	int updateInfo(Member updateMember);

	int updatePw(Map<String, String> map);

	int delete(Map<String, String> map);

	int updateProfile(MultipartFile profileImage, Member loginMember) throws Exception;

}