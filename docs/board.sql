/* board */
desc board;
select * from user;
desc user;

-- insert
select ifnull((select max(group_no) + 1 from board), 1);

insert
into board
values(null, '제목입니다', '내용입니다', 0, now(),
	(select ifnull((select max(b.group_no) + 1 from board b), 1)),
    1,
    0,
    1,
    true);
    
insert
into board
values(null, '제목-답글제목', '제목-답글내용', 0, now(),
	1,
    1,
    1,
    2,
    true);
    
(select max(b.group_no) + 1 from board b);

-- select
select b.no as no,
	u.name as username,
	title,
	date_format(b.reg_date, '%Y-%m-%d %H:%i:%s') as regdate,
	hit,
    depth
from board b, user u
where b.user_no = u.no
	and title like '%%'
order by group_no desc, order_no asc
limit 0, 5;
    
select count(*) from board where title like '%hey%';

update board
set hit = hit + 1
where no = 1;

-- 파일 업로드
show tables;
desc file;


-- admin
desc site;
select * from site;
insert into site values(null, "양동화의 페이지", "양동화의 site에 방문하신 것을 환영합니다!", "/images/profile/20194232284527512aa75cf-ba3c-4717-a24d-b84cff14898d.jpg", "이 사이트는 웹프로그래밍 예제 사이트입니다.");