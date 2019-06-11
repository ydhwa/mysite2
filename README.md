# Servlet ~ Spring 실습

## 강의 시간에 구현한 내용
- 회원가입 및 로그인/로그아웃 부분 Spring 이용하여 변경

**Controller <-> Service <-> Repository**

## 190516
- mysite1에서 구현한 내용 전부를 MVC를 적용하여 변경
- 개인정보 수정
   - 개인정보 수정 시 바뀐 이름이 곧장 반영됨
- 게시판(board)
   - 목록 조회
   - 글 남기기(로그인을 한 사람만 글을 남길 수 있음)
   - 게시글 조회
   - 게시글 수정(해당 게시글의 작성자만 글을 수정할 수 있음)
   - 접근 제어 리다이렉트 상세 설정(게시글 쓰기, 수정하기 할 때 권한이 없는 경우 게시글 목록으로 리다이렉트됨)

## 190517
- MyBatis 적용
- 개인 정보 수정 폼 수정
   - 이름이나 패스워드를 공란으로 두었을 때, 기존 내용과 바뀌지 않도록 수정함(수정하지 않는다는 의사로 간주)

## 190520
- Email 중복검사 설정
   - Email 중복검사를 필수록 하도록 만들지는 않음

## 190521
- 게시판 수정(계층형 게시판으로 구현)
   - 페이저(pager): **페이지 당 10개의 게시글을** 보이도록 함
   - 로그인, 작성자 여부에 따른 접근 제어
   - 검색 기능: 페이지를 넘겨도 검색 기능 유지
   - 게시글 삭제: 게시글 비활성화 방식으로 구현
   - 답글 기능
- LOGGER
- 게시글 조회 시 조회수 update
   - 비회원: 그냥 들어가는 족족 조회수 반영
   - 회원: 들어갔던 페이지에 또 들어가면 조회수 올라가지 않음
   
## 190522
- 게시판
   - 수정 시 조회수가 2씩 올라갔던 문제 수정
   - 네이버 SmartEditor2 사용하여 게시글에 손쉽게 스타일 지정할 수 있도록 함


## 190523
- 게시판 파일 업로드 기능 구현
   - **파일 데이터베이스의 board_no는 원래 게시판 no의 외래키인데, 파일 업로드 로직 문제로 외래키로 지정하지 않음**
   - *게시글 삭제 시 DB에서는 파일이 삭제되지만, 실제 저장소에서는 삭제되지 않음*
   - *예외사항 처리 미흡*: 작성 중에 다른 사람이 먼저 게시글을 올릴 경우, 이미지의 boardNo가 잘못 지정됨
- 메인 페이지 DB에 지정되어 있는 것으로 보이도록 함
   - 원하는 것으로 지정하려면 site.xml의 쿼리문(where no = ###) 변경해야 함
   
## 190611
- development는 PostgreSQL을 사용하도록 마이그레이션 함


## 구현해야 할 것
- 관리자 페이지
- 게시글 삭제 시 파일도 삭제되는 기능