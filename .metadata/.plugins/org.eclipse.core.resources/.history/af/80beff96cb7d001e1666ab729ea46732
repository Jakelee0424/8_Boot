<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
	<html>

	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		
		<link rel="stylesheet" href="/resources/css/main-style.css" type="text/css">
		<script src="https://kit.fontawesome.com/b2efae0e57.js" crossorigin="anonymous"></script>
	</head>

	<body>
		<main>
			
			<%-- header.jsp 추가 --%>

			<%-- 

				<jsp:include page="jsp파일경로" />

				- jsp 파일 경로는 webapp 폴더 기준 작성
				- jsp 액션 태그(jsp에 기본 내장)
				- 다른 jsp 파일의 코드를 현재 위치에 추가

			--%>

			<jsp:include page ="/WEB-INF/views/common/header.jsp"/>


			<section class="content">
				
				<section class="content-1">

					<h3>로그인된 회원 정보</h3>
						이메일: ${sessionScope.loginMember.memberEmail} <br>
						닉네임: ${sessionScope.loginMember.memberNickname} <br>
						전화번호: ${sessionScope.loginMember.memberTel} <br>
						<br>
					<h3>닉네임이 일치하는 회원의 전화번호 조회</h3>

					<input type="text" id="inputNickname">
					<button id="btn1">조회</button>
					<h4 id="result1"></h4>

					<hr>

					<h3>이메일을 입력받아 일치하는 회원의 정보를 조회</h3>
					<input id="inputEmail">
					<button id="btn2">조회</button>
					<ul id="result2">
						

					</ul>

				</section>

				<section class="content-2">

					<c:choose>

						<%-- 로그인 안되었을때 --%>
						<%-- EL empty : 비어있거나 null --%>
						<c:when test="${empty sessionScope.loginMember}">


							<form action="/member/login" name="login-form" id="loginFrm" method="post">

								<fieldset class="id-pw-area">
		
									<section>
										<input type="text" name="memberEmail" placeholder="이메일"
											autocomplete="off"
											value="${cookie.saveId.value}"
											>										
										<input type="password" name="memberPw" placeholder="비밀번호">
									</section>
		
									<section>
										<button>로그인</button>
									</section>
		
								</fieldset>
		
								<label for="saveId">
									<c:if test="${not empty cookie.saveId.value}">
										<%-- 쿠키에 저장된 이메이이 있으면 변수 선언 
											-> page scope
										--%>
										
										<c:set var="save" value="checked"/>
										
									</c:if>
								
									<input type="checkbox" name="saveId" ${save}>아이디 저장
								</label>
		
								<section class="signup-find-area">
									<a href="/member/signUp">회원가입</a>
									<span>|</span>
									<a href="#">ID/PW 찾기</a>
								</section>
		
							</form>
	
						</c:when>


						<%-- 로그인 되었을 때 --%>
						<c:otherwise>
							
							<article class="login-area">
							
								<a href="/myPage/profile">
							
									<c:if test="${empty loginMember.profileImage}">

									<img src="/resources/images/user.png" id="memberProfile">

								</c:if>

								<c:if test="${not empty loginMember.profileImage}">

									<img src="${loginMember.profileImage}" id="memberProfile">

								</c:if>
							
								</a>

								<div class="my-info">
									<div>
										<a href="/myPage/info" id="nickname">${sessionScope.loginMember.memberNickname}</a>
										<a href="/member/logout" id="logoutBtn">로그아웃</a>
									</div>

									<p></p>
								</div>
								
							</article>


						</c:otherwise>

					</c:choose>


				</section>

			</section>
			
		</main>

		<jsp:include page ="/WEB-INF/views/common/footer.jsp"/>
	
		<%-- SockJS 추가 --%>
    	<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
		
		<script src="/resources/js/main.js"></script>


	</body>

	</html>