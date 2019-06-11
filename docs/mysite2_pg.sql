select * from member;
select * from guestbook;
select * from board;
select * from site;
select * from file;


-- member
insert 
into member(no, name, email, password, gender, join_date) 
values(nextval('seq_member'), '이름', 'email@email.com', '1234', 'famale', now());

select currval('seq_member');

select no, name 
from member
where email = 'email@email.com' 
	and password = '1234';

select no, name, email, gender 
from member 
where email = 'email@email.com';

select name, email, gender 
from member 
where no = 1;

select name 
from member 
where no = 1;

update member
set email = 'email@email.com',
	name = '이름입니다',
	password = '1234', 
	gender = 'male'
where no = 1;


-- guestbook
select no, name, contents, to_char(reg_date, 'yyyy-mm-dd hh24:mi:ss')
from guestbook
order by reg_date desc;

insert 
into guestbook 
values(nextval('seq_guestbook'), '방명록이름', '1234', '내용입니다~', now());

delete 
from guestbook 
where no = 1 
	and password = '1234';


-- board
select board.no as no, 
	member.name as username, 
	title, 
	to_char(board.reg_date, 'yyyy-mm-dd hh24:mi:ss') as regdate, 
	hit, 
	status, 
	depth, 
	member.no as userno 
from board, member 
where title like concat('%', '안녕', '%') 
	and board.user_no = member.no 
order by group_no desc, order_no asc 
limit 5
offset 0;

select board.no as no,
	group_no as groupno,
	order_no as orderno,
	depth,
	title,
	board.user_no as userno,
	to_char(board.reg_date, 'yyyy-mm-dd hh24:mi:ss') as regdate, 
	hit,
	member.name as username,
	contents
from board, member
where board.user_no = member.no
	and board.no = 1;

select count(*) 
from board 
where title like concat('%', '안녕', '%');

insert 
into board 
values(nextval('seq_board'), '안녕', '내용이야!', 0, now(), 
	(select coalesce((select max(b.group_no) + 1 from board b), 1)), 
    1, 
    0, 
    1, 'ACTIVE');
   
update board 
set order_no = order_no + 1 
where group_no = 1 
and order_no >= 1 + 1;

update board 
set title = '제목 바꿉니다', contents = '내용도 바꿉니다' 
where no = 1;

update board
set hit = hit + 1
where no = 1;

update board 
set status = 'ACTIVE' 
where no = 1;


-- site
select title, welcome_message as welcomeMessage, profile_url as profileUrl, description
from site
limit 1;

insert into site
values(nextval('seq_site'), '동화의 홈페이지', '반갑습니다~', 'logo.png', '동화의 웹프로그래밍 실습 사이트입니다!');


-- file
-- 쿼리 상의 오류가 많기 때문에 손대지 않음