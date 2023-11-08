<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="/resources/css/main-style.css" type="text/css">
	


	<header>

		<section>
			<a href="/"> <img src="/resources/images/logo.jpg" id="homeLogo">
			</a>
		</section>

		<section>
			<section class="search-area">

					<form action="/search" name="search-form" method="get">
						

						<fieldset>
							<input type="search" id="query" name="query" autocomplete="off"
							placeholder="검색어를 입력하세요">
							
							<button id="searchBtn" class="fa-solid fa-magnifying-glass"></button>
						</fieldset>
						
						
					</form>
					
					
					
					
				</section>
		</section>

		<section></section>

	</header>


	<nav>
		<ul>
		
		<%-- 인터셉터를 이용해 조회된 boardTypeList를 화면에 출력 (from application scope) --%>
			<c:forEach var="boardType" items="${boardTypeList}">
			<li>
				<a href="/board/${boardType.BOARD_CODE}">${boardType.BOARD_NAME}</a>
			</li>
			</c:forEach>		
		
		
			<%-- 
			<li><a href="#">${}</a></li>
			<li><a href="/board/boardList"></a></li>
			<li><a href="#"></a></li>
			<li><a href="#"></a></li>
			<li><a href="#"></a></li>
			--%>
			<%-- 로그인 했을 때 채팅 보여짐 --%>
				   <c:if test="${not empty loginMember}" >
					   <li><a href="/chatting">채팅</a></li>
				   </c:if>
		</ul>
	</nav>





