drop table if exists lu_user;
create table if not exists lu_user
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    level       tinyint      not null comment '等级',
    username    varchar(20)  not null,
    integral    int                   default 0 comment '积分',
    balance     decimal(10, 2)        default 0 comment '余额',
    password    varchar(255) not null comment '密码',
    status      tinyint               default 1 comment '状态',
    deleted_key bigint       not null default -1 comment '删除标识，主键',
    email       varchar(255) comment '邮箱',
    api_key     varchar(64)  not null comment 'API 请求密钥',
    create_time datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime     not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key u_username (username),
    unique key u_email (email)
) ENGINE = InnoDB;

drop table if exists lu_url;
create table if not exists lu_url
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_url    varchar(64)   not null comment '短链接',
    original_url varchar(1024) not null comment '原始链接',
    user_id      bigint        not null comment '用户ID',
    status       tinyint                default 1 comment '状态',
    create_time  datetime      not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time  datetime      not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key short_url (short_url),
    key url_user_id (user_id)
) ENGINE = InnoDB;

drop table if exists lu_analysis;
create table if not exists lu_analysis
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     bigint      not null comment '用户ID',
    short_url   varchar(64) not null comment '短链接',
    frequency   bigint               default 0 comment '访问次数',
    create_time datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime    not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key aly_short_url (short_url)
) ENGINE = InnoDB;