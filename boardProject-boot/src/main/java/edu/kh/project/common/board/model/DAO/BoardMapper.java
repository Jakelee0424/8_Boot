package edu.kh.project.common.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.kh.project.common.board.model.dto.Board;
import edu.kh.project.common.board.model.dto.Pagination;

@Mapper
public interface BoardMapper {

	List<Map<String, Object>> selectBoardTypeList();

	int getListCount(int boardCode);

	List<Board> selectBoardList(int boardCode, RowBounds rowBounds);

	Board selectBoard(Map<String, Object> map);

	int selectLike(Map<String, Object> map);

	int updateReadCount(int boardNo);

	int insertBoardLike(Map<String, Integer> paramMap);

	int deleteBoardLike(Map<String, Integer> paramMap);

	int selectBoardLike(Map<String, Integer> paramMap);

	int getSearchListCount(Map<String, Object> paramMap);

	List<Board> selectSearchBoardList(Map<String, Object> paramMap, RowBounds rowBounds);

	List<String> selectImageList();

}
