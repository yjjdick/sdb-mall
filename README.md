**项目说明** 
- sdb是一个轻量级的在renren-fast基础上利用jfinal架构二次开发的一个极速二次开发商城框架，前后端分离的Java快速开发平台，C端采用微信小程序，能快速开发项目并交付【接私活利器】
- 支持MySQL、Oracle、SQL Server、PostgreSQL等主流数据库
<br> 
<br>
**具有如下特点** 
- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过token进行数据交互，前端再也不用关注后端技术
- 灵活的权限控制，可控制到页面或按钮，满足绝大部分的权限需求
- 页面交互使用Vue2.x，极大的提高了开发效率
- 完善的代码生成机制，可在线生成entity、xml、dao、service、vue、sql代码，减少70%以上的开发任务
- 引入quartz定时任务，可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能
- 引入API模板，根据token作为登录令牌，极大的方便了APP接口开发
- 引入Hibernate Validator校验框架，轻松实现后端校验
- 引入云存储服务，已支持：七牛云、阿里云、腾讯云等
- 引入swagger文档支持，方便编写API接口文档
<br> 
**项目结构** 
```
sdb
├─db  项目SQL语句
│
├─common 公共模块
│  ├─aspect 系统日志
│  ├─exception 异常处理
│  ├─validator 后台校验
│  ├─entity 自定义实体对象
│  └─xss XSS过滤
│ 
├─config 配置信息
│ 
├─job 定时任务
│ 
├─controller 路由（代码生成器自动生成）
│ 
├─service 服务类（代码生成器自动生成）
│ 
├─dao 数据库模型高一级的抽象配合service（代码生成器自动生成）
│ 
├─model 数据库模型（代码生成器自动生成）
│ 
├─form 前端自定义请求类
│ 
├─sdbApplication 项目启动类
│  
├──resources 
│  ├─sql 复杂表级联sql模板
│  └─static 静态资源

```
<br> 
**如何交流、反馈、参与贡献？** 
- 开发文档：正在设计中
- Git仓库：https://github.com/yjjdick/sdb-mall  
- 官方QQ交流群：346743162
- 技术讨论、二次开发等咨询、问题和建议，请移步到交流群！
- 如需关注项目最新动态，请Watch、Star项目，同时也是对项目最好的支持
<br> 
**技术选型：** 
- 核心框架：Spring Boot 2.0
- 安全框架：Apache Shiro 1.4
- 视图框架：Jfinal Enjoy
- 持久层框架：Jfinal ORM
- 定时器：Quartz 2.3
- 数据库连接池：Druid 1.0
- 日志管理：logback
- 页面交互：Vue2.x 
<br> 
 **后端部署**
- 通过git下载源码
- 创建数据库sdb，数据库编码为UTF8mb4
- 执行db/init.sql文件，初始化数据
- 修改application-dev.yml，更新MySQL账号和密码
- Eclipse、IDEA运行sdbApplication.java，则可启动项目
- 项目后端地址：http://localhost:8080/sdb
- Swagger路径：http://localhost:8080/sdb/swagger/index.html
<br> 
 **前端部署**
 - 本项目是前后端分离的，还需要部署前端，才能运行起来
 - 前端下载地址：请到交流群询问
 - 前端页面可用项目自带的代码生成器自动生成减少90%以上开发工作量
 <br>
 **商城业务**
- 系统管理（一切后端需要的功能基本都能满足，鉴权机制，定时任务，系统日志，代码生成器等）
- 商品管理
- 订单管理
- 商品分类
- 规格管理
- 微信支付
- 客服系统
- 物流系统（快递100）
- 邮件接口
- 分销模块（接入中）
- 各种campaign（正在开发）
<br> 
 **介绍和使用视频**
- 介绍视频地址：正在制作中...
- 使用视频地址：正在制作中...
<br> 
**后端管理系统：**
<br> 
- 首页dash
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/admin-%E9%A6%96%E9%A1%B5.png "首页dash")
<br> 
<br> 
- 商品管理列表
<br> 
![输入图片说明](https://senduobang.oss-cn-shanghai.aliyuncs.com/admin-%E5%95%86%E5%93%81%E7%AE%A1%E7%90%86.png "商品管理列表")
<br> 
<br> 
- 多规格添加商品
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/admin-%E5%A4%9A%E8%A7%84%E6%A0%BC%E5%95%86%E5%93%81%E6%B7%BB%E5%8A%A0.png "多规格添加商品")
<br> 
<br> 
- 订单管理列表
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/admin-%E8%AE%A2%E5%8D%95%E7%AE%A1%1C%E7%90%86.png "订单管理列表")
<br> 
<br> 
- 订单详情
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/admin-%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%85.png "订单详情")
<br>
<br>
**小程序商城：**
<br> 
<br> 
- 首页
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E9%A6%96%E9%A1%B5.PNG "首页")
<br> 
<br> 
- 商品详情
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E5%95%86%E5%93%81%E8%AF%A6%E6%83%85.PNG "商品详情")
<br> 
<br> 
- 客服
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E5%AE%A2%E6%9C%8D.PNG "客服")
<br> 
<br> 
- 购物车
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E8%B4%AD%E7%89%A9%E8%BD%A6.PNG "购物车")
<br> 
<br> 
- checkout
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-checkout.PNG "checkout")
<br> 
<br> 
- 微信支付
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98.PNG "微信支付")
<br> 
<br> 
- 订单详情
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%85.PNG "订单详情")
<br> 
<br> 
- 我的中心
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E6%88%91%E7%9A%84%E4%B8%AD%E5%BF%83.PNG "我的中心")
<br> 
<br> 
- 订单列表
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E8%AE%A2%E5%8D%95%E5%88%97%E8%A1%A8.PNG "订单列表")
<br> 
<br> 
- 订单详情2
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%852.PNG "订单详情2")
<br> 
<br> 
- 物流详情
<br> 
![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E7%89%A9%E6%B5%81%E8%AF%A6%E6%83%85.PNG "物流详情")