drop database if exists mymall;
drop user if exists 'mymall'@'localhost';
-- 支持emoji：需要mysql数据库参数： character_set_server=utf8mb4
create database mymall default character set utf8mb4 collate utf8mb4_unicode_ci;
use mymall;
create user 'mymall'@'localhost' identified by 'mymall123456';
grant all privileges on mymall.* to 'mymall'@'localhost';
flush privileges;