drop table if exists lu_user;
create table if not exists lu_user
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    level       tinyint      not null default 0 comment '等级',
    username    varchar(20)  not null comment '用户名',
    integral    int                   default 0 comment '积分',
    balance     decimal(10, 2)        default 0 comment '余额',
    subscription_term_id    bigint   not null comment '订阅ID',
    subscription_start_time datetime not null comment '订阅开始时间',
    subscription_end_time   datetime comment '订阅结束时间',
    password    varchar(255) not null comment '密码',
    status      tinyint               default 1 comment '状态',
    deleted_key bigint       not null default -1 comment '删除标识，主键',
    email       varchar(255) comment '邮箱',
    api_key     varchar(64)  comment 'API 请求密钥',
    create_time datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime     not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key u_username (username),
    unique key u_email (email)
) ENGINE = InnoDB;

drop table if exists lu_user_role;
create table if not exists lu_user_role
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id   bigint not null comment '用户ID',
    role_id   bigint not null comment '角色ID',
    create_time datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key user_role (user_id, role_id)
) ENGINE = InnoDB;

drop table if exists lu_role;
create table if not exists lu_role
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    code        varchar(20) not null comment '角色编码',
    name        varchar(20) not null comment '角色名',
    description varchar(255) comment '角色描述',
    enabled     boolean     not null default true comment '是否启用',
    create_time datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime    not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key r_role_name (name)
) ENGINE = InnoDB;

drop table if exists lu_url;
create table if not exists lu_url
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_url       varchar(8)    not null comment '短链接',
    original_url    varchar(1024) not null comment '原始链接',
    user_id         bigint        not null comment '用户ID',
    status          tinyint                default 1 comment '状态',
    expiration_time datetime comment '过期时间',
    create_time     datetime      not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time     datetime      not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key url_short_url (short_url),
    key url_user_id (user_id)
) ENGINE = InnoDB;

drop table if exists lu_access_records;
create table if not exists lu_access_records
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_url   varchar(8)   not null comment '短链接',
    user_agent  varchar(255) not null comment '浏览器信息',
    ip          varchar(64)  not null comment 'IP地址',
    referer     varchar(255) comment '来源页面',
    access_time datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    key ar_short_url (short_url, access_time)
) ENGINE = InnoDB;

drop table if exists lu_permissions;
create table if not exists lu_permissions
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    code        varchar(20) not null comment '权限编码',
    name        varchar(20) not null comment '权限名称',
    description varchar(255) comment '权限描述',
    enabled     boolean     not null default true comment '是否启用',
    create_time datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime    not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key p_code (code)
) ENGINE = InnoDB;

drop table if exists lu_role_permissions;
create table if not exists lu_role_permissions
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id       bigint   not null comment '角色ID',
    permission_id bigint   not null comment '权限ID',
    create_time   datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time   datetime not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key role_permission (role_id, permission_id)
) ENGINE = InnoDB;

drop table if exists lu_subscriptions;
create table if not exists lu_subscriptions
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        varchar(20) not null comment '订阅名称',
    description varchar(255) comment '订阅描述',
    create_time datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime    not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key s_name (name)
) ENGINE = InnoDB;

drop table if exists lu_subscription_terms;
create table if not exists lu_subscription_terms
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    subscription_id bigint         not null comment '订阅ID',
    price           decimal(10, 2) not null comment '价格',
    period_unit     varchar(20)    not null comment '有效期单位',
    period_value    integer        not null comment '有效期时间',
    create_time     datetime       not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time     datetime       not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key subscription_term (subscription_id, period_unit, period_value)
) ENGINE = InnoDB;