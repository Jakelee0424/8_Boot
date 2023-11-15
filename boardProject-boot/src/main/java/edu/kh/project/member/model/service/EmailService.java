package edu.kh.project.member.model.service;

public interface EmailService {

	int signUp(String email, String string);

	int checkAuthKey(String inputKey, String email);


}
