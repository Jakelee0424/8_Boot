package edu.kh.project.common.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.common.board.model.dto.Board;
import edu.kh.project.common.board.model.service.BoardService;
import edu.kh.project.common.board.model.service.BoardService2;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;
import oracle.jdbc.proxy.annotation.Post;

@Controller
@RequestMapping("/board2")
@SessionAttributes({"loginMember"})
public class BoardController2 {

	@Autowired
	private BoardService2 service;

	@Autowired
	private BoardService boardService;

	
	/** 글쓰기 이동
	 * @param boardCode
	 * @return
	 */
	@GetMapping("/{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode) {
		
		return "board/boardWrite";
	}

	/** 글쓰기 
	 * @param boardCode
	 * @param board
	 * @param images
	 * @param loginMember
	 * @param ra
	 * @param session
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@PostMapping("/{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode,
							  @ModelAttribute Board board,
							  @RequestParam(value = "images", required = false) List<MultipartFile> images,
							  @SessionAttribute("loginMember") Member loginMember,
							  RedirectAttributes ra
			) throws IllegalStateException, IOException {

		// List<MultipartFile>
		// 업로드된 이미지가 없어도 요소는 존재(사이즈 0, 파일명(getOriginalFileName() = "") 
		
		// 1) 로그인한 회원 얻어와 board에 세팅
		board.setMemberNo(loginMember.getMemberNo());
		
		// 2) boardCode 세팅
		board.setBoardCode(boardCode);
		
		
		
		
		// 게시글 삽입 서비스 호출
		// 삽입된 게시글 번호 반환 -> 해당 게시글로 리 다이렉트 -> /board/{boardCode}/{boardNo}
		int boardNo = service.insertBoard(board, images);
		
		String msg = null;
		String path = "redirect:";
		
		if(boardNo > 0) {
			
			msg = "게시글이 등록되었습니다.";
			path += "/board/" + boardCode + "/" + boardNo;
			
		} else { 
			
			msg = "게시글 등록 실패";
			path += "insert";
			
		}
		
		ra.addFlashAttribute("msg", msg);
			
		
		return path;
	}

	/** 글 수정 이동
	 * @return
	 */
	@GetMapping("/{boardCode}/{boardNo}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode,
							  @PathVariable("boardNo") int boardNo,
							  Model model  
			) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		Board board = boardService.selectBoard(map);
		
		model.addAttribute("board", board);
		
		return "board/boardUpdate";
	}
	
	/** 글 수정
	 * @return
	 */
	@PostMapping("/{boardCode}/{boardNo}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode,
							  @PathVariable("boardNo") int boardNo,
							  @RequestParam(value="cp", required = false, defaultValue = "1") int cp,
							  @RequestParam(value="images", required = false) List<MultipartFile> images,
							  @RequestParam(value="deleteList", required = false) String deleteList,
							  Board board,
							  HttpSession session,
							  RedirectAttributes ra
							  ) throws IllegalStateException, IOException {
		
		board.setBoardCode(boardCode);
		board.setBoardNo(boardNo);
		
		// 서비스 호출
		int rowCount = service.boardUpdate(board, images, deleteList);
		
		String msg = null;
		String path = "redirect:";
		
		if(rowCount > 0 ) {
		
			msg = "수정이 완료되었습니다.";
			path += "/board/" + boardCode + "/" + boardNo + "?cp=" + cp; 
			
		} else {
			
			msg = "수정에 실패하였습니다.";
			path += "update";
			
		}
		
		ra.addFlashAttribute("msg", msg);
		
		return path;
	}
	
	// 게시글 삭제
	// btn 눌렀을 떄 location -> 처리
	
	@GetMapping("/{boardCode}/{boardNo}/delete")
	public String boardDelete(@PathVariable("boardCode") int boardCode,
			  				  @PathVariable("boardNo") int boardNo,
			  				  RedirectAttributes ra
			  ) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		int result = service.boardDelete(map);
		
		String path = "redirect:";
		String msg = null;
		
		
		if(result > 0) {
			
			msg = "삭제되었습니다.";
			path += "/board/" + boardCode; 
			
			
		} else {
			
			msg = "삭제 실패.";
			path += "/board/" + boardCode + "/" + boardNo; 
		}
		
		ra.addFlashAttribute("msg", msg);
		
		return path ;
		
	}
	
	
}



