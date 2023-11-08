const insertBtn = document.getElementById("insertBtn");
const boardType = location.pathname.split("/")[2];

// 글쓰기 버튼 클릭 시
if(insertBtn != null){ // 로그인 여부에 따라 insertBtn이 있는가 없는가에 대한 예외처리
    
    insertBtn.addEventListener("click", () => {

        // location 객체 사용
        location.href  = `/board2/${boardType}/insert`;

    });
    

}