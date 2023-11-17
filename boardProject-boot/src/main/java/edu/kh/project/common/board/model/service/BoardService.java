package edu.kh.project.common.board.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.common.board.model.dto.Board;

public interface BoardService {

	List<Map<String, Object>> selectBoardTypeList();

	/** 게시글 목록 조회
	 * @param boardCode
	 * @param cp
	 * @return
	 */
	Map<String, Object> selectBoardList(int boardCode, int cp);

	/** 게시글 상세 조회
	 * @param map
	 * @return
	 */
	Board selectBoard(Map<String, Object> map);

	/** 좋아요 여부 조회
	 * @param map
	 * @return
	 */
	int selectLike(Map<String, Object> map);

	/** 조회수 조회
	 * @param boardNo
	 * @return
	 */
	int updateReadCount(int boardNo);

	/** 좋아요 처리
	 * @param paramMap
	 * @return
	 */
	int like(Map<String, Integer> paramMap);

	/** 게시글 검색
	 * @param paramMap
	 * @param cp
	 * @return
	 */
	Map<String, Object> selectBoardList(Map<String, Object> paramMap, int cp);

	/** 이미지 목록 조회
	 * @return
	 */
	List<String> selectImageList();
	
}
