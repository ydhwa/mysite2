show tables;
select * from user;
alter table user add column role enum('ROLE_ADMIN', 'ROLE_USER') default 'ROLE_USER';
alter table user change column role role enum('ROLE_ADMIN', 'ROLE_USER') default 'ROLE_USER';
insert into user values(null, '운영자', 'admin@mysite.com', '1234', 'female', now(), 'ROLE_ADMIN');

desc user;
alter table user change column password password varchar(128) not null;

update user set role='ROLE_USER' where no > 3;