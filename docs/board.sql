/* board */
desc board;

-- insert
select ifnull((select max(group_no) from board), 1);

insert
into board
values(null, '제목입니다', '내용입니다', 0, now(),
	select ifnull((select max(group_no) from board), 1),
    );

-- select
select b.no as no,
	u.name as username,
	title,
	date_format(b.reg_date, '%Y-%m-%d %H:%i:%s') as regdate,
	hit
from board b, user u
where b.user_no = u.no
order by group_no desc, order_no asc
limit 0, 5;
    
