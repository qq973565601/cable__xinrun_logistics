# cable_昕润物流

#### 介绍
基于Jeecg-boot开发的物流仓储系统，涵盖模块：用户管理、车辆管理、计划管理、仓库管理、库存管理、财务管理、统计报表、系统管理等模块组成

#### 软件架构
- jeecg-boot-master 后台项目
- cable.sql 后台管理系统数据库脚本
- logistics：微信小程序
- wxImg: 微信小程序图片

#### 所用技术
- 此系统基于 Jeecg-boot 为脚手架开发的PRD管理系统
- 后端技术：SpringBoot 2.1.3 + Shiro 1.4.0 + Redis + Mysql + MyBatis-Plus 3.1.2 + Jwt 3.7.0 + Swagger-ui
- 前端技术：Vue + Ant-design-vue + Webpack
- 其他技术：Druid(数据库连接池)、Logback(日志工具)、poi(Excel工具)、Quartz(定时任务)、lombok(简化代码)
- 项目构建：Maven、JDK1.8+

#### 启动教程
- 查看 jeecg-boot 官网的文档说明：根据步骤安装所需的环境依赖，然后启动项目即可。
- 启动项目分为前端和后端
- [JeecgBoot官方文档](http://jeecg-boot.mydoc.io/)

#### 项目截图
1. 登录界面
![登录截图](https://images.gitee.com/uploads/images/2020/0628/192351_69d1a279_5459645.jpeg "1.jpg")
2. 首页
![首页](https://images.gitee.com/uploads/images/2020/0628/192911_b2a0b33a_5459645.jpeg "2.jpg")
3. 系统设置 - 可以更改系统主体颜色设置等等
![系统设置](https://images.gitee.com/uploads/images/2020/0829/131611_5c8e13e6_5459645.jpeg "系统设置.jpg")
4. 员工管理模块 
![员工管理](https://images.gitee.com/uploads/images/2020/0829/131638_4fd807ec_5459645.jpeg "员工管理.jpg")
5. 角色授权 - 通过分配给用户不同的角色，可访问不同的菜单列表
![角色授权](https://images.gitee.com/uploads/images/2020/0829/131718_1e498bac_5459645.jpeg "角色授权.jpg")
6. 计划导出 - 通过 excelPoi 技术实现信息导出功能
![计划导出](https://images.gitee.com/uploads/images/2020/0829/131754_11c8c927_5459645.jpeg "计划导出.jpg")
7. 定时任务 - 定时指定某些特定的任务
![定时任务](https://images.gitee.com/uploads/images/2020/0829/131834_ebdb6126_5459645.jpeg "定时任务.jpg")
