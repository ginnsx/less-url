drop table if exists lu_user;
create table if not exists lu_user
(
    id                      BIGINT PRIMARY KEY,
    level                   tinyint      not null default 0 comment '等级',
    username                varchar(20)  not null comment '用户名',
    integral                int                   default 0 comment '积分',
    balance                 decimal(10, 2)        default 0 comment '余额',
    subscription_term_id    bigint comment '订阅ID',
    subscription_start_time datetime comment '订阅开始时间',
    subscription_end_time   datetime comment '订阅结束时间',
    password                varchar(255) not null comment '密码',
    status                  tinyint               default 1 comment '状态',
    deleted_key             bigint       not null default -1 comment '删除标识，主键',
    email                   varchar(255) comment '邮箱',
    api_key                 varchar(64) comment 'API 请求密钥',
    created_at              datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at              datetime     not null default CURRENT_TIMESTAMP comment '更新时间',
    version                 int                   default 0 comment '版本号',
    unique key u_username (username),
    unique key u_email (email)
) ENGINE = InnoDB;

drop table if exists lu_user_role;
create table if not exists lu_user_role
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    bigint   not null comment '用户ID',
    role_id    bigint   not null comment '角色ID',
    created_at datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at datetime not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key user_role (user_id, role_id)
) ENGINE = InnoDB;

drop table if exists lu_role;
create table if not exists lu_role
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    code        varchar(20) not null comment '角色编码',
    name        varchar(20) not null comment '角色名',
    description varchar(255) comment '角色描述',
    enabled     boolean     not null default true comment '是否启用',
    created_at  datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at  datetime    not null default CURRENT_TIMESTAMP comment '更新时间',
    version     int                  default 0 comment '版本号',
    unique key r_role_name (name)
) ENGINE = InnoDB;

drop table if exists lu_link;
create table if not exists lu_link
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_url        varchar(8)    not null comment '短链接',
    original_url     varchar(1024) not null comment '原始链接',
    owner_id         varchar(40)   not null comment '用户ID，带前缀 u_ 或 g_',
    status           tinyint                default 0 comment '状态',
    is_custom        boolean       not null default false comment '是否自定义短链接',
    expires_at       datetime comment '过期时间',
    visits           INT           NOT NULL DEFAULT 0 COMMENT '访问次数',
    last_access_time DATETIME COMMENT '最后访问时间',
    created_at       datetime      not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at       datetime      not null default CURRENT_TIMESTAMP comment '更新时间',
    version          int                    default 0 comment '版本号',
    unique key link_short_url (short_url),
    key link_owner_id (owner_id),
    key link_created_at (created_at)
) ENGINE = InnoDB;

drop table if exists lu_access_record;
create table if not exists lu_access_record
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_url   varchar(8)   not null comment '短链接',
    user_agent  varchar(255) not null comment '浏览器信息',
    ip          varchar(64)  not null comment 'IP地址',
    language varchar(80) comment '语言',
    referer     varchar(255) comment '来源页面',
    access_time datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    key idx_ar_short_url (short_url),
    key idx_ar_at (access_time)
) ENGINE = InnoDB;

drop table if exists lu_permission;
create table if not exists lu_permission
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    code        varchar(20) not null comment '权限编码',
    name        varchar(20) not null comment '权限名称',
    description varchar(255) comment '权限描述',
    enabled     boolean     not null default true comment '是否启用',
    created_at  datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at  datetime    not null default CURRENT_TIMESTAMP comment '更新时间',
    version     int                  default 0 comment '版本号',
    unique key p_code (code)
) ENGINE = InnoDB;

drop table if exists lu_role_permission;
create table if not exists lu_role_permission
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id       bigint   not null comment '角色ID',
    permission_id bigint   not null comment '权限ID',
    created_at    datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at    datetime not null default CURRENT_TIMESTAMP comment '更新时间',
    unique key role_permission (role_id, permission_id)
) ENGINE = InnoDB;

drop table if exists lu_subscription;
create table if not exists lu_subscription
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        varchar(20) not null comment '订阅名称',
    description varchar(255) comment '订阅描述',
    created_at  datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at  datetime    not null default CURRENT_TIMESTAMP comment '更新时间',
    version     int                  default 0 comment '版本号',
    unique key s_name (name)
) ENGINE = InnoDB;

drop table if exists lu_subscription_term;
create table if not exists lu_subscription_term
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    subscription_id bigint         not null comment '订阅ID',
    price           decimal(10, 2) not null comment '价格',
    period_unit     varchar(20)    not null comment '有效期单位',
    period_value    integer        not null comment '有效期时间',
    created_at      datetime       not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at      datetime       not null default CURRENT_TIMESTAMP comment '更新时间',
    version         int                     default 0 comment '版本号',
    unique key subscription_term (subscription_id, period_unit, period_value)
) ENGINE = InnoDB;

drop table if exists lu_email_template;
create table if not exists lu_email_template
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       varchar(50) not null comment '模板名称',
    subject    varchar(255) comment '邮件主题',
    content    TEXT comment '邮件内容',
    created_at datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at datetime    not null default CURRENT_TIMESTAMP comment '更新时间',
    version    int                  default 0 comment '版本号',
    unique key et_name (name)
) engine = InnoDB;

drop table if exists lu_task;
CREATE TABLE IF NOT EXISTS lu_task
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_name        varchar(50) not null comment '任务名称',
    last_executed_at DATETIME comment '最近一次运行时间',
    created_at       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    updated_at       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP comment '更新时间',
    unique key tel_name (task_name)
) ENGINE = InnoDB comment '任务执行记录表';

-- 事实表：访问记录
DROP TABLE IF EXISTS lu_visit_stats;
CREATE TABLE IF NOT EXISTS lu_visit_stats
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_url   VARCHAR(8) NOT NULL COMMENT '短链接',
    visit_time  TIMESTAMP  NOT NULL COMMENT '访问时间',
    geo_id      BIGINT     NOT NULL COMMENT '地理位置ID',
    device_id   BIGINT     NOT NULL COMMENT '设备ID',
    platform_id BIGINT     NOT NULL COMMENT '平台ID',
    locale_id   BIGINT     NOT NULL COMMENT '语言ID',
    referer_id  BIGINT     NOT NULL COMMENT '来源ID',
    visits      INT        NOT NULL DEFAULT 1 COMMENT '访问次数',
    visitors    INT        NOT NULL DEFAULT 1 COMMENT '访客数',
    created_at  DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_visit (short_url, visit_time, geo_id, device_id, platform_id, locale_id, referer_id)
) ENGINE = InnoDB COMMENT '访问记录事实表';

-- 维度表：地理位置
DROP TABLE IF EXISTS lu_dim_geo;
CREATE TABLE IF NOT EXISTS lu_dim_geo
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    country   VARCHAR(50) NOT NULL COMMENT '国家',
    region    VARCHAR(50) COMMENT '区域',
    city      VARCHAR(50) COMMENT '城市',
    continent VARCHAR(50) COMMENT '洲',
    UNIQUE KEY uk_country_region_city (country, region, city)
) ENGINE = InnoDB COMMENT '地理位置维度表';

-- 维度表：设备
DROP TABLE IF EXISTS lu_dim_device;
CREATE TABLE IF NOT EXISTS lu_dim_device
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    device      VARCHAR(100) NOT NULL COMMENT '设备',
    brand       VARCHAR(50)  NOT NULL COMMENT '品牌',
    device_type VARCHAR(20)  NOT NULL COMMENT '设备类型',
    UNIQUE KEY uk_os_browser_device_language (device, device_type)
) ENGINE = InnoDB COMMENT '设备维度表';

-- 维度表：平台
DROP TABLE IF EXISTS lu_dim_platform;
CREATE TABLE IF NOT EXISTS lu_dim_platform
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    os      VARCHAR(50) NOT NULL COMMENT '操作系统',
    browser VARCHAR(50) NOT NULL COMMENT '浏览器',
    UNIQUE KEY uk_os_browser (os, browser)
) ENGINE = InnoDB COMMENT '平台维度表';

-- 维度表：语言时区
DROP TABLE IF EXISTS lu_dim_locale;
CREATE TABLE IF NOT EXISTS lu_dim_locale
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    timezone VARCHAR(80) NOT NULL COMMENT '时区',
    language VARCHAR(80) NOT NULL COMMENT '语言',
    UNIQUE KEY uk_os_browser (timezone, language)
) ENGINE = InnoDB COMMENT '语言时区维度表';

-- 维度表：来源
DROP TABLE IF EXISTS lu_dim_referer;
CREATE TABLE IF NOT EXISTS lu_dim_referer
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    referer_type varchar(50)  NOT NULL COMMENT '来源类型',
    referer      VARCHAR(255) NOT NULL COMMENT '来源',
    UNIQUE KEY uk_referer (referer_type, referer)
) ENGINE = InnoDB COMMENT '来源维度表';
