package edu.kh.project.myPage.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.member.model.dto.Member;

@Repository
public class myPageDAO {

	@Autowired
	private MyPageMapper myPageMapper;
	
	/** 정보 수정 DAO
	 * @param updateMember
	 * @return
	 */
	public int updateInfo(Member updateMember) {
	
		return myPageMapper.update(updateMember);
	}

	/** 비밀번호 조회 DAO
	 * @param string
	 * @return
	 */
	public String selectPw(Map<String, String> map) {

		return myPageMapper.selectPw(map);
	}
	
	/** 비밀번호 변경 DAO
	 * @param map
	 * @return
	 */
	public int updatePw(Map<String, String> map) {

		return myPageMapper.updatePw(map);
	}

	/** 회원 탈퇴 DAO
	 * @param map
	 * @return
	 */
	public int delete(Map<String, String> map) {

		return myPageMapper.delete(map);
	}

	/** 프로필 사진 수정 DAO
	 * @param loginMember
	 * @return
	 */
	public int updateProfileImage(Member loginMember) {

		return myPageMapper.updateProfileImage(loginMember);

	}


}


