package edu.kh.project.common.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.common.board.model.dto.Board;
import edu.kh.project.common.board.model.dto.BoardImage;


//@Repository
@Mapper
public interface BoardMapper2 {


	/** 글쓰기 DAO
	 * @param board
	 * @return
	 */
	public int boardInsert(Board board);

	/** 사진 업로드 DAO
	 * @param uploadList
	 * @return 
	 */
	public int insertImages(List<BoardImage> uploadList);

	/** 글 수정(제목, 내용) DAO
	 * @param board
	 * @return
	 */
	public int boardUpdate(Board board);

	/** 사진 삭제 DAO
	 * @param deleteMap
	 * @return
	 */
	public int imageDelete(Map<String, Object> deleteMap);

	/** 사진 업데이트 DAO
	 * @param img
	 * @return
	 */
	public int imageUpdate(BoardImage img);

	/** 사진 삽입 DAO
	 * @param img
	 * @return
	 */
	public int imageInsert(BoardImage img);

	/** 개시글 삭제 DAO
	 * @param map
	 * @return
	 */
	public int boardDelete(Map<String, Object> map);
}
