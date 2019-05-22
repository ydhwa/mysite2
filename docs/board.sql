/* board */
desc board;
select * from board;
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