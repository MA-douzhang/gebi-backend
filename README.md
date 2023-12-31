# 前言
***
开源地址

[微服务框架前端代码](https://github.com/MA-douzhang/gebi-frontend/tree/dev-cloud)

[微服务框架后端代码](https://github.com/MA-douzhang/gebi-backend/tree/dev-cloud)

[SpringBoot框架前端代码](https://github.com/MA-douzhang/gebi-frontend)

[SpringBoot框架后端代码](https://github.com/MA-douzhang/gebi-backend)

***
GeBI项目的后端源码，主要是用于总结学习中间件的知识和利用AI实现更便捷的功能。

后端功能主要分为，三个服务模块，用户服务，图表服务，文本服务。

## 技术栈
***
框架：Spring Boot 

数据库：MySQL

中间件：Redis + RabbitMq

插件：Mybatis(Plus)，Swagger（接口文档），鱼聪明

# 服务器部署
1. 使用服务器配置为2核4G
2. 宝塔面板
3. 详细步骤和思路笔记在项目doc目录下

## 说明
***
>如果对您有帮助，您可以右上角点一个“start”支持一下，👍
>
> 如有问题请直接在 Issues 中提，或者您发现问题并有非常好的解决方案，欢迎 PR 👍

# 效果展示
***
[查看在线展示](http://60.204.157.128/)

## 功能
***
+ [x] 登录注册功能
+ [x] 消息队列保证服务的健壮性
+ [x] 支付模块
+ [x] AI将文本格式转换
+ [x] AI将表格转为可视化

~~给开发者打钱的支付功能~~


# 总结
***
1. 将Spring Boot项目重构成Spring Cloud项目，利用微服务将多个模块解耦合，保证服务的稳定性，
当有一个服务出错时也不会影响其它服务进行。
2. 消息队列使用死信队列，保证当AI服务偶尔出错时，能够重新放回队列继续服务，提高服务稳定性，当超过1min还未成功就放入死信队列。
3. 将多个服务解耦合后，在后续项目扩展中可以更加便捷得增加模块且不影响原来的项目框架。
