## 一、云尚办公系统

### 1、介绍

云上办公系统是一套自动办公系统，用户可以通过手机微信扫码关注公众号，在公众号上进行OA审批流程

系统主要包含：管理端和员工端

管理端包含：权限管理、审批管理、公众号菜单管理

员工端采用微信公众号操作，包含：办公审批、微信授权登录、消息推送等功能

项目服务器端架构：SpringBoot + MyBatisPlus + SpringSecurity + Redis + Activiti+ MySQL

前端架构：vue-admin-template + Node.js + Npm + Vue + ElementUI + Axios

### 2、核心技术

| 基础框架：SpringBoot                                         |
| ------------------------------------------------------------ |
| 数据缓存：Redis                                              |
| 数据库：MySQL                                                |
| 权限控制：SpringSecurity                                     |
| 工作流引擎：Activiti                                         |
| 前端技术：vue-admin-template + Node.js + Npm + Vue + ElementUI + Axios |
| 微信公众号：公众号菜单 + 微信授权登录 + 消息推送             |


### 3、开发环境说明

| 工具         | 版本                                   |
| ------------ |--------------------------------------|
| 后台         | SpringBoot 2.3.6 + MyBatisPlus 3.4.1 |
| 服务器       | Tomcat 8.5.73                        |
| 数据库       | MySQL 8.0.27                         |
| Build Tools  | Maven 3.8.5                          |
| 前端         | Vue + ElementUI + Node.js            |
| 开发工具     | IDEA 2022.3                          |
| 版本管理工具 | Git                                  |


### 4、项目总结

1.集成Swagger，方便进行接口API的统一测试

2.首次使用Activiti组件

3.首次使用内网穿透连接微信公众号

4.项目中使用JWT加密token，用作用户登录身份校验，用 SpringSecurity 来做权限控制，涉及多表查询，是项目的重点

5.
