package edu.kh.project.common.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.project.common.board.model.dto.Comment;
import edu.kh.project.common.board.model.service.CommentService;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.utility.Util;

@RestController // @Controller + @ResponseBody (모든 응답은 비동기)
@SessionAttributes({"loginMember"})
public class CommentController {

	@Autowired
	private CommentService service;
	
	/** 댓글 조회
	 * @param boardNo
	 * @return
	 */
	@GetMapping(value="comment")
	public List<Comment> select(int boardNo) {
		
		return service.select(boardNo); 		
	}
	
	@PostMapping(value="comment")
	public int insert(@RequestBody Comment comment) {
		
		return service.insertComment(comment); 		
	}
	
	@GetMapping(value="insert")
	public int insert(String comment, int boardNo, @SessionAttribute("loginMember")  Member loginMember) {
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("memberNo", loginMember.getMemberNo());
		map.put("comment", Util.XSSHandling(comment));
		map.put("boardNo", boardNo);
		
		int result = service.insert(map);
		
		return result;
	}
	
	@GetMapping(value="delete")
	public int delete(int commentNo) {
		
		int result = service.delete(commentNo);
		
		return result;
	}
	
	@GetMapping(value="update")
	public int update(String commentContent, int commentNo) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("commentNo", commentNo);
		map.put("commentContent", commentContent);
		
		int result = service.update(map);
		
		return result;
	}
	
	
	
	
}
