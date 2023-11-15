package edu.kh.project.myPage.service;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.dao.MyPageMapper;
//import edu.kh.project.myPage.dao.myPageDAO;
import edu.kh.project.utility.Util;

@Service
@PropertySource("classpath:/config.properties")
public class myPageServiceImpl implements myPageService {
	
	@Value("${my.member.webpath}") // 
	private String webPath;
	
	@Value("${my.member.location}")
	private String filePath;

	@Autowired
	private MyPageMapper myPageMapper;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	/** 정보 수정 서비스
	 *
	 */
	@Override
	@Transactional
	public int updateInfo(Member updateMember) {


		return myPageMapper.update(updateMember);
	}

	/** 비밀번호 변경 서비스
	 *
	 */
	@Override
	@Transactional
	public int updatePw(Map<String, String> map) {

		int result = 0;

		String memberPw = myPageMapper.selectPw(map);

		if(bcrypt.matches(map.get("currentPw"), memberPw)) {

			String newPw = bcrypt.encode(map.get("newPw"));
			map.put("newPw", newPw);

			result = myPageMapper.updatePw(map);

		} else {

			result = 0;


		}

		return result;
	}

	/** 회원 탈퇴 서비스
	 *
	 */
	@Override
	@Transactional
	public int delete(Map<String, String> map) {

		int result = 0;

		if(bcrypt.matches(map.get("memberPw"), myPageMapper.selectPw(map))) {

			result = myPageMapper.delete(map);

		} else {

			result = 0;

		}

		return result;
	}

	/** 프로필 사진 변경 서비스
	 *
	 */
	@Override
	@Transactional
	public int updateProfile(MultipartFile profileImage, Member loginMember) throws Exception{
		
		// 프로필 이미지 변경 실패 대비
		String temp = loginMember.getProfileImage();		
		String rename = null; // 변경할 이름 저장 변수		
		
		
		if(profileImage.getSize() > 0) { // 업로드 이미지가 있을 경우
			
			// 1) 업로드된 이미지의 이름 변경
			rename = Util.fileRename(profileImage.getOriginalFilename());
			
			// 2) 바뀐 이름 loginMember에 세팅
			loginMember.setProfileImage(webPath + rename);

			
		} else { // 업로드된 이미지가 없는 경우(x버튼)
			
			loginMember.setProfileImage(null);
			
		}
		
		// 3) 프로필 이미지 수정 DAO 호출
		int result = myPageMapper.updateProfileImage(loginMember);
		
		
		if(result > 0) { // 업데이트 성공 시
			
			// 업로드된 새 이미지가 있을 경우
			if(rename != null) {
				
				//메모리에 임시 저장되어있는 파일을 서버에 진짜로 저장
				profileImage.transferTo( new File(filePath + rename));
				
			}
			
		} else { // 업데이트 실패 시
			
			// 이전 이미지로 프로필 다시 세팅
			loginMember.setProfileImage(temp);
			
		}
		
		return result;
	}

	
		
}



