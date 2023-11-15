package edu.kh.project.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionController {

	public String exceptionHandler(Exception e) {
		
		e.printStackTrace();
	
		return "error/500";
		
	}
	
}
