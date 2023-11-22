package edu.kh.project.common.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.board.model.exception.ImageDeleteException;
import edu.kh.project.common.board.model.dao.BoardMapper2;
import edu.kh.project.common.board.model.dto.Board;
import edu.kh.project.common.board.model.dto.BoardImage;
import edu.kh.project.utility.Util;


@Service
@PropertySource("classpath:/config.properties")
public class BoardServiceImpl2 implements BoardService2{

	@Value("${my.board.webpath}")
	private String webPath;
	
	@Value("${my.board.location}")
	private String filePath;
	
	@Autowired
	private BoardMapper2 mapper;

	/** 글쓰기 서비스
	 * @throws IOException 
	 * @throws IllegalStateException 
	 *
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertBoard(Board board, List<MultipartFile> images) throws IllegalStateException, IOException {

		
		
		// 1) board테이블에 insert(제목, 내용, 작성자, 게시판 코드)
		int result = mapper.boardInsert(board);
		int boardNo = 0;
		
		if (result == 0 ) return 0;  
		boardNo = board.getBoardNo();
		
		// 2) 게시글 삽입 성공 시
		// 업로드된 이미지가 있다면 BOARD_IMG 테이블에 삽입하는 dao 호출
		if(boardNo > 0) {

			//실제 업로드된 정보를 기록할 List 생성
			List<BoardImage> uploadList = new ArrayList<BoardImage>();

			for(int i = 0 ; i < images.size() ; i++ ) {

				if(images.get(i).getSize() > 0 ) {

					BoardImage img = new BoardImage();
					img.setImagePath(webPath); 
					img.setBoardNo(boardNo);
					img.setImageOrder(i);

					String fileName = images.get(i).getOriginalFilename();
					img.setImageOriginal(fileName);
					img.setImageReName(Util.fileRename(fileName) );


					uploadList.add(img);
				}

			}

			if(!uploadList.isEmpty()) {

				result = mapper.insertImages(uploadList);
				//result = 삽입된 행의 개수

				// 전체 insert 성공 여부 확인
				if(uploadList.size() == result) {

					for(int i = 0 ; i < uploadList.size() ; i++) {

						int index = uploadList.get(i).getImageOrder();
						String rename = uploadList.get(i).getImageReName();

						images.get(index).transferTo( new File(filePath + rename));

					}


				} else {

					// 예외 강제 발생 -> @transactional로 롤백

					throw new edu.kh.project.common.board.model.exception.FileUploadException();

				}



			}


		}

		return boardNo;
	}

	/** 글 수정 서비스
	 * @throws IOException 
	 * @throws IllegalStateException 
	 *
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int boardUpdate(Board board, List<MultipartFile> images, 
			String deleteList) throws IllegalStateException, IOException {

		// 2. 글 업데이트
		int rowCount = mapper.boardUpdate(board);

		// 3. 이미지 업데이트
		if (rowCount > 0) {

			// 3-1. 이미지 삭제
			if(!deleteList.equals("")) {
				Map<String, Object> deleteMap = new HashMap<String, Object>();
				deleteMap.put("boardNo", board.getBoardNo());
				deleteMap.put("deleteList", deleteList);

				rowCount = mapper.imageDelete(deleteMap);

				if(rowCount == 0 ) {

					throw new ImageDeleteException();

				}

			}

		} 
		// 4. 새로 업로드된 이미지 분류 작업

		// images : 실제 파일이 담긴 List
		//         -> input type="file" 개수만큼 요소가 존재
		//         -> 제출된 파일이 없어도 MultipartFile 객체가 존재

		List<BoardImage> uploadList = new ArrayList<>();

		for(int i=0 ; i<images.size(); i++) {

			if(images.get(i).getSize() > 0) { // 업로드된 파일이 있을 경우

				// BoardImage 객체를 만들어 값 세팅 후 
				// uploadList에 추가
				BoardImage img = new BoardImage();

				// img에 파일 정보를 담아서 uploadList에 추가
				img.setImagePath(webPath); // 웹 접근 경로
				img.setBoardNo(board.getBoardNo()); // 게시글 번호
				img.setImageOrder(i); // 이미지 순서

				// 파일 원본명
				String fileName = images.get(i).getOriginalFilename();

				img.setImageOriginal(fileName); // 원본명
				img.setImageReName( Util.fileRename(fileName) ); // 변경명    

				uploadList.add(img);

				// 오라클은 다중 UPDATE를 지원하지 않기 때문에
				// 하나씩 UPDATE 수행

				rowCount = mapper.imageUpdate(img);

				if(rowCount == 0) {
					// 수정 실패 == DB에 이미지가 없었다 
					// -> 이미지를 삽입
					rowCount = mapper.imageInsert(img);
				}
			}
		}


		// 5. uploadList에 있는 이미지들만 서버에 저장(transferTo())
		if(!uploadList.isEmpty()) {
			for(int i=0 ; i< uploadList.size(); i++) {

				int index = uploadList.get(i).getImageOrder();

				// 파일로 변환
				String rename = uploadList.get(i).getImageReName();

				images.get(index).transferTo( new File(filePath + rename)  );                    
			}
		}



		return rowCount;
	}

	/** 게시글 삭제 서비스
	 *
	 */
	@Override
	public int boardDelete(Map<String, Object> map) {

		return mapper.boardDelete(map);
	}

}
