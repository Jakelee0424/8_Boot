package edu.kh.project.common.board.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.board.model.dto.Board;



public interface BoardService2 {

	/** 글 쓰기
	 * @author user1
	 *
	 */
	int insertBoard(Board board, List<MultipartFile> images) throws IllegalStateException, IOException;

	/** 글 수정
	 * @param board
	 * @param images
	 * @param webPath
	 * @param filePath
	 * @param deleteList
	 * @return
	 */
	int boardUpdate(Board board, List<MultipartFile> images, String webPath, String filePath, String deleteList) throws IllegalStateException, IOException;

	/** 게시글 삭제
	 * @param map
	 * @return
	 */
	int boardDelete(Map<String, Object> map);

}
