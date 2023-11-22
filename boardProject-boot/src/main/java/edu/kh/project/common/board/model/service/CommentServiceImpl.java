package edu.kh.project.common.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.common.board.model.dao.BoardMapper;
import edu.kh.project.common.board.model.dao.CommentMapper;
import edu.kh.project.common.board.model.dto.Comment;
import edu.kh.project.utility.Util;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private BoardMapper mapper;
	
	@Override
	public List<Comment> select(int boardNo) {

		return mapper.selectCommentList(boardNo);
	}

	@Override
	public int delete(int commentNo) {

		return mapper.deleteComment(commentNo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insert(Map<String, Object> map) {

		return mapper.insertComment(map);
	}

	@Override
	public int update(Map<String, Object> map) {

		return mapper.updateComment(map);
	}

	@Override
	public int insertComment(Comment comment) {

		return mapper.insert(comment);
	}


}
