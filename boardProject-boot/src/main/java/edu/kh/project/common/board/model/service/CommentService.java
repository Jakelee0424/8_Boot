package edu.kh.project.common.board.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.common.board.model.dto.Comment;


public interface CommentService {

	List<Comment> select(int boardNo);

	int delete(int commentNo);

	int insert(Map<String, Object> map);

	int update(Map<String, Object> map);

	int insertComment(Comment comment);

}
