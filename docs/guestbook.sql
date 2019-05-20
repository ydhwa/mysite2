use webdb;
insert
into guestbook
values(null, '둘리', '1234', '호이~', now());
insert
into guestbook
values(null, '마이콜', '4567', '맛좋은라면', now());

show tables;

select no, name, contents, date_format(reg_date, '%Y-%m-%d %H:%i:%s')
from guestbook
order by reg_date desc;

select * from guestbook;

desc user;

select no, name from user where email='email@email.com' and password='1234' order by no asc;

update user
set name = '양승진', email = 'email@email.com', password = '1234', gender = 'male'
where no = 1;

select * from user;
delete from user where no = 3;

select name, email, password, gender from user where no = 1;

select exists(
		select *
		from user
        where email = 'email@email1.com');

select name from user where no = 1;

-- 게시판
desc board;

insert
into board
values(null, '하이', '안녕', now(), 0, 1);

select u.name, title, contents, date_format(b.reg_date, '%Y-%m-%d %H:%i:%s'), views
from board b, user u
where b.user_no = u.no
and b.no = 1;

update board
set title = '제목입니다~', contents = '내용 내용입니다!'
where no = 1;

select u.name, title, date_format(b.reg_date, '%Y-%m-%d %H:%i:%s'), views
from board b, user u
where b.user_no = u.no
order by reg_date desc;