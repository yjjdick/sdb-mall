# sdb mall

**项目说明** 
- sdb是一个轻量级的在renren-fast基础上利用jfinal架构二次开发的一个极速二次开发直播，拼团商城框架，前后端分离的Java快速开发平台，C端采用微信小程序，能快速开发项目并交付【接私活利器】
- 支持MySQL、Oracle、SQL Server、PostgreSQL等主流数据库
- 重点说明：此项目并不是提供下载运行后直接就能上生产的情况，在我多年的经验里告诉我，所有的项目即使是商业模式非常相似但是在业务需求细节上也是有各种各种不同的变化，特别是在C端和campaign这块，举几个例子，在C端方面，你可能和别人使用完全色调，页面布局一模一样的页面吗？在后端同样一个优惠券的功能你能保证你不做一些定制化的实现吗？所以sdb mall这个架构是用来让你可以快速开发成自己独有的，个性化的商城项目，并非是提供大而全的功能，当然我也会在其中添加一些比较通用的功能，比如商城的完整流程和之后会开发一些营销模块等
- 后端开源
<br>

[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-姚嘉钧-orange.svg)](https://github.com/yjjdick/sdb-mall) 
[![](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/yjjdick/sdb-mall) 
[![](https://img.shields.io/badge/QQ-406123228-brightgreen.svg)]()

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
- Github仓库：https://github.com/yjjdick/sdb-mall  
- Gitee仓库：https://gitee.com/yjjdick/sdb-mall
- 官方QQ交流群：346743162<a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=001dc5c4b5bf9abe5b187aa0d058ac9a19f0c98295647f9fa7c1a6b496b1b59f"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="森多邦商城交流群" title="森多邦商城交流群"></a>
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
- 微信退款
- 微信租户接口
- 客服系统
- 物流系统（快递100）
- 邮件接口
- 拼团模块
- 各种campaign（正在开发）
<br>

 **介绍和使用视频**
- 视频地址：
- idea下代码启动：https://v.qq.com/x/page/y0795k72ak8.html
- backend安装启动：https://v.qq.com/x/page/o0795qs3drx.html
- 小程序wepy框架介绍：https://v.qq.com/x/page/t0795vugxc2.html
- eclipse下代码启动：https://v.qq.com/x/page/a0795t266c8.html
- 小程序业务介绍：https://v.qq.com/x/page/f07958veoua.html
- 管理端业务流程介绍：https://v.qq.com/x/page/a079561268d.html
- 二次开发简单介绍：http://v.qq.com/x/page/i0795wutn2o.html
- 公众号森多邦工作室提供互联网软件开发，美术设计等服务![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B7%A5%E4%BD%9C%E5%AE%A4%E5%85%AC%E4%BC%97%E5%8F%B7.jpg?x-oss-process=image/resize,p_30 "工作室公众号")

<br>

 **介绍和使用视频**
 - 后端演示地址：https://qa.senduobang.com/backend/
 - 小程序演示地址：![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F%E5%95%86%E5%9F%8E%E4%BA%8C%E7%BB%B4%E7%A0%81.jpg?x-oss-process=image/resize,p_50 "工作室公众号")

 <br>

**后端管理系统：**
<br> 
- 首页直播
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/ezgif-1-1cb294e07bec.gif "物流详情")
<br> 
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

**小程序商城:**
<br> 
<br> 
- 首页
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E9%A6%96%E9%A1%B5.PNG?x-oss-process=image/resize,p_30&t1 "首页")
<br> 
<br> 
- 商品详情
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E5%95%86%E5%93%81%E8%AF%A6%E6%83%85.PNG?x-oss-process=image/resize,p_30&t=1 "商品详情")
<br> 
<br> 
- 商品详情2
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E5%95%86%E5%93%81%E8%AF%A6%E6%83%852.PNG?x-oss-process=image/resize,p_30&t=1 "商品详情")
<br> 
<br> 
- 客服
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E5%AE%A2%E6%9C%8D.PNG?x-oss-process=image/resize,p_30 "客服")
<br> 
<br> 
- 购物车
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E8%B4%AD%E7%89%A9%E8%BD%A6.PNG?x-oss-process=image/resize,p_30 "购物车")
<br> 
<br> 
- checkout
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-checkout.PNG?x-oss-process=image/resize,p_30 "checkout")
<br> 
<br> 
- 微信支付
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98.PNG?x-oss-process=image/resize,p_30 "微信支付")
<br> 
<br> 
- 订单详情
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%85.PNG?x-oss-process=image/resize,p_30 "订单详情")
<br> 
<br> 
- 我的中心
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E6%88%91%E7%9A%84%E4%B8%AD%E5%BF%83.PNG?x-oss-process=image/resize,p_30&t=1 "我的中心")
<br> 
<br> 
- 订单列表
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E8%AE%A2%E5%8D%95%E5%88%97%E8%A1%A8.PNG?x-oss-process=image/resize,p_30 "订单列表")
<br> 
<br> 
- 订单详情2
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%852.PNG?x-oss-process=image/resize,p_30 "订单详情2")
<br> 
<br> 
- 物流详情
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E7%89%A9%E6%B5%81%E8%AF%A6%E6%83%85.PNG?x-oss-process=image/resize,p_30 "物流详情")
<br> 
<br> 
- 拼团1
<br> 

![输入图片说明](
https://senduobang.oss-cn-shanghai.aliyuncs.com/%E5%B0%8F%E7%A8%8B%E5%BA%8F-%E6%8B%BC%E5%9B%A2.PNG?x-oss-process=image/resize,p_30 "物流详情")
<br> 
<br> 
- 拼团2
<br> 
