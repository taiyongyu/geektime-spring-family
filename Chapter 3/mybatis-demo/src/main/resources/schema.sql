/**
  使用schema.sql来创建表结构
  price 是bigint类型
  create_time中有下划线，需要转换为驼峰式
  update_time中有下划线，需要转换为驼峰式
 */

create table t_coffee (
    id bigint not null auto_increment,
    name varchar(255),
    price bigint not null,
    create_time timestamp,
    update_time timestamp,
    primary key (id)
);