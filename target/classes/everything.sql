-- 创建数据库 (h2以一个单独的文件存储数据，所以在代码中指定即可)
-- create database if not exists everything;

-- 创建数据库表
drop table if exists file_index;

create table if not exists file_index(
  name varchar(255) not null comment '文件名称',
  path varchar(1024) not null comment '文件路径',
  depth int not null comment '文件路径深度',
  file_type varchar(32) not null comment '文件类型'

);

-- 建立索引
-- create index file_name on file_index(name);
