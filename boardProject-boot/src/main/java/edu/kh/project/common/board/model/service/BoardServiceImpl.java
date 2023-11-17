package edu.kh.project.common.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.common.board.model.dao.BoardMapper;
import edu.kh.project.common.board.model.dto.Board;
import edu.kh.project.common.board.model.dto.Pagination;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper mapper;

	@Override
	public List<Map<String, Object>> selectBoardTypeList() {

		return mapper.selectBoardTypeList();
	}
	
	/** 게시글 목록 조회 서비스
	 *
	 */
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {
		
		// 1. 특정 게시판의 삭제되지 않은 게시글 수 조회
		int listCount = mapper.getListCount(boardCode);
		
		// 2. 1번 조회결과 + cp를 이용하여 pagination 객체 생성
		Pagination pagination = new Pagination(listCount, cp);

		
		// 1) offset 계산
		int offset = (pagination.getCurrentPage() -1 ) * pagination.getLimit();
		
		// 2) RowBounds 객체 생성
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		
		// 3. 특정 게시판(boardCode)에서 원하는 페이지(Pagination.currentPage)에 해당하는 부분에 대한 게시글 목록과
		//	  게시굴 수(pagination.limit) 조회
		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
		//4. pagination과 boardlist를 map에 담아 반환
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
		
	}

	/** 게시글 상세 조회 서비스
	 *
	 */
	@Override
	public Board selectBoard(Map<String, Object> map) {

		return mapper.selectBoard(map);
	}

	/** 좋아요 클릭 여부 조회 서비스
	 *
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int selectLike(Map<String, Object> map) {

		return mapper.selectLike(map);
	}

	/** 조회 수 증가 서비스
	 *
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateReadCount(int boardNo) {

		return mapper.updateReadCount(boardNo);
	}
	
	/** 좋아요 처리 서비스
	 *
	 */
	@Override
	public int like(Map<String, Integer> paramMap) {

		int result = 0;
		int count = -1;
		
		if(paramMap.get("check") == 0) {
			
			result = mapper.insertBoardLike(paramMap);
			
		} else {
			
			result = mapper.deleteBoardLike(paramMap);
		
		}
		
		if(result > 0) {
			
			count =  mapper.selectBoardLike(paramMap);
			
		}
		
		return count;
	}

	/** 게시글 검색
	 *
	 */
	@Override
	public Map<String, Object> selectBoardList(Map<String, Object> paramMap, int cp) {
		
		// 1. 특정 게시판의 삭제되지 않은 게시글 + 검색 조건이 일치하는 게시글 수 조회
		int listCount = mapper.getSearchListCount(paramMap);
		
		// 2. 1번 조회결과 + cp를 이용하여 pagination 객체 생성
		Pagination pagination = new Pagination(listCount, cp);
		
		// 1) offset 계산
		int offset = (pagination.getCurrentPage() -1 ) * pagination.getLimit();
		
		// 2) RowBounds 객체 생성
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
				
		// 3. 특정 게시판(boardCode)에서 현재 페이지(Pagination.currentPage)에 해당하는 부분에 대한 게시글 목록과
		//	  게시글 수(pagination.limit) 조회 
		//    단, 검색조건이 일치
		List<Board> boardList = mapper.selectSearchBoardList(paramMap, rowBounds);
		
		//4. pagination과 boardlist를 map에 담아 반환
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

	
	/** 이미지 목록 조회
	 *
	 */
	@Override
	public List<String> selectImageList() {

		return mapper.selectImageList();
	}

}
