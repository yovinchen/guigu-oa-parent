## 一、云尚办公系统

[总后端](https://github.com/yovinchen/guigu-oa-parent)

[管理端](https://github.com/yovinchen/guigu-oa-admin)

[员工端](https://github.com/yovinchen/guigu-oa-web)

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

###5、后端常见问题

#### 5.1、跨域问题

**访问协议**： http   https

**ip地址（域名）**： oa.atguigu.com    oa.baidu.com

**端口号**：8800  9528

**多种解决方式：**

（1）在controller类上面添加注解

（2）在前端进行配置



#### 5.2、mapper扫描问题

```java
//第一种方式 ：创建配置类，使用@MapperScan注解
@Configuration
@MapperScan(basePackages = {"com.atguigu.auth.mapper","com.atguigu.process.mapper","com.atguigu.wechat.mapper"})
public class MybatisPlusConfig {

}

//第二种方式：在mapper的接口上面添加注解 @Mapper
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    
}
```



#### 5.3、xml文件加载问题

**Maven默认情况下，在src - main -java目录下面，只会加载java类型文件，其他类型文件不会加载的**

**第一种解决方式：把xml文件放到resources目录下**

**第二种解决方式：在pom.xml和项目配置文件进行配置**



#### 5.4、流程定义部署zip文件

**zip文件规范（要求）**

**（1）zip文件名称和流程key保持一致**  

例如：<process id="qingjia" isExecutable="true"> 文件名称 qingjia.zip

**（2）在zip文件打包xml文件，xml文件命名 .bpmn20.xml**

例如：jiaban.bpmn20.xml



#### 5.5、内网穿透问题

**接口和页面在本地localhost，公众号不能直接访问本地路径的，需要使用内网穿透**

有两个用途：

**第一个：公众号页面通过内网穿透到本地页面  9090**

**第二个：公众号里面接口通过内网穿透到本地接口 8800**
