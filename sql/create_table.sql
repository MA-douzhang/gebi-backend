# 建表脚本

-- 创建库
create database if not exists ge_bi;

-- 切换库
use ge_bi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 图表
create table if not exists chart
(
    id          bigint auto_increment comment 'id' primary key,
    `name`      varchar(128)                       null comment '图表名称',
    goal        text                               null comment '分析目标',
    chartData   text                               null comment '图表数据',
    chatType    varchar(128)                       null comment '图表类型',
    genChat     text                               null comment '生成的图表数据',
    genResult   text                               null comment '生成的分析结论',
    userId      bigint                             null comment '创建用户Id',
    `status`    varchar(128)                       not null default 'wait' comment 'wait,running,succeed,failed',
    execMessage text                               null comment '执行信息',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;

-- 积分表
create table if not exists credit
(
    id          bigint auto_increment comment 'id' primary key,
    userId      bigint                             null comment '创建用户Id',
    creditTotal bigint                             null default 0 comment '总积分',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
) comment '积分表' collate = utf8mb4_unicode_ci;

-- 充值订单表
create table if not exists orders
(
    id            bigint auto_increment comment 'id' primary key comment '订单id',
    alipayTradeNo varchar(128)                       null comment '支付宝交易凭证id',
    `userId`      bigint                             NOT NULL COMMENT '用户id',
    subject       varchar(128)                       not null comment '交易名称',
    totalAmount   double                             not null comment '交易金额',
    tradeStatus   varchar(128)                       not null default 'unpaid ' comment 'unpaid,paying,succeed,failed',
    buyerId       varchar(64)                        null comment '支付宝买家id',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除'
) comment '充值订单表' collate = utf8mb4_unicode_ci;

-- 文本任务表
create table if not exists text_task
(
    id             bigint auto_increment comment '任务id' primary key,
    `name`         varchar(128)                       null comment '笔记名称',
    textType       varchar(128)                       null comment '文本类型',
    genTextContent text                               null comment '生成的文本内容',
    userId         bigint                             null comment '创建用户Id',
    `status`       varchar(128)                       not null default 'wait' comment 'wait,running,succeed,failed',
    execMessage    text                               null comment '执行信息',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除'
) comment '文本任务表' collate = utf8mb4_unicode_ci;

-- 文本记录表
create table if not exists text_record
(
    id             bigint auto_increment comment 'id' primary key,
    textTaskId     bigint comment '文本任务id',
    textContent    text                               null comment '文本内容',
    genTextContent text                               null comment '生成的文本内容',
    `status`       varchar(128)                       not null default 'wait' comment 'wait,running,succeed,failed',
    execMessage    text                               null comment '执行信息',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除'
) comment '文本记录表' collate = utf8mb4_unicode_ci;
