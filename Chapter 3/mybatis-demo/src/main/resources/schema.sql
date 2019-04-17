/**
  使用schema.sql来创建表结构
  price 是bigint类型
  create_time中有下划线，需要转换为驼峰式
  update_time中有下划线，需要转换为驼峰式
  建表时，字段名称建议用"_"分隔多个单词，比如:create_time、update_time，
  这样生成的entity，属性名称就会变成漂亮的驼峰命名，即：createTime、updateTime
 */

create table t_coffee (
    id bigint not null auto_increment,
    name varchar(255),
    price bigint not null,
    create_time timestamp,
    update_time timestamp,
    primary key (id)
);