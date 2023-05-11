# community

#### 第一章
#### 第二章
1. 发送邮件
2. 注册功能
3. 会话管理 
4. 生成验证码
5. 登录、登出功能
6. 显示登录信息
- 逻辑：
浏览器请求服务器后，服务器会给浏览器一个cookie，包含ticket凭证信息，浏览器再次访问时，服务端会根据携带的ticket查询login_ticket，得到当前用户的所有信息，并将其存储在ThreadLocal中，供当前这一整个请求使用，利用拦截器实现。

- 技术：
  - interceptor拦截器
  - ThreadLocal用户信息
  - Cookie
  - ModelAndView
7. 账号设置
- 逻辑：
首先将图片上传到服务器（目前为本地），为上传的图片生成web访问路径后，更新数据库；首页自动展示头像时访问web路径，采用response的输出流和自定义的输入流读取服务器的图片，构建图片响应。
- 技术：
  - MultipartFile
  - thymeleaf
8. 检查登录状态
- 逻辑：
在每个**需要登录**的方法加上@LoginRequired注解，配合**拦截器**和**当前用户登录信息**判定用户是否可以访问被注解标记的方法。
- 技术：
  - @LoginRequired注解
  - Interceptor拦截器
  - HostHolderUtil

#### 第三章
1. 过滤敏感词
- 逻辑：
利用**Trie字典树**，在编译期构造方法调用后**初始化**敏感词文件，构建字典树后调用相应的方法进行敏感词过滤，在待
**过滤文本上选择双指针**+**Trie树单指针**进行过滤。
- 技术
  - @PostContrustor注解标记初始化方法
  - Trie树数据结构：Map<Character, Trie>
  - 双指针
  - 特殊符号处理
2. 发布帖子
- 逻辑
用户登录状态下，显示发布按钮，点击发布后，填入标题和内容后，异步发送,返回响应结果；
- 技术
  - AJAX $.post请求(请求路径，请求内容，响应函数)
  - HostHolderUtil获取用户登录状态
  - HtmlUtil.
  - 敏感词过滤
3. 帖子详情
- 逻辑
点击标题，跳转到详情页面
- 技术
  - 利用map数据前端获取帖子id
  - @PathVariable接收id
  - model封装post数据和user数据
4. 事务管理
5. 显示评论
- 逻辑：
  - 构建实体类Comment，page分页类
  - 创建mapper接口，xml文件编写
  - 创建service，
  - 创建controller，Model+Page，重定向
- 技术
  - 评论：给帖子的评论
    - post_id
  - 回复：给评论的评论；给某人的评论
    - comment_id
    - target_id（给某人）
    - entity_id（给评论）
6. 添加评论
- 技术:
  - 事务使用的场景
  - 事务的隔离级别
7. 私信列表
8. 发送私信
9. 统一处理异常
10. 统一记录日志

#### 第四章
1. Redis入门
2. Spring整合Redis
3. 点赞
4. 我收到的赞
5. 关注、取消关注
6. 关注列表、粉丝列表
7. 优化登录模块

#### 第五章
1. 阻塞队列
2. kafka入门
3. Spring整合kafka
4. 发送系统通知
5. 显示系统通知

#### 第六章
1. Elasticsearch
2. Spring整合elasticsearch
3. 开发社区搜索功能

#### 第七章
1. Spring security
2. 权限控制
3. 置顶、加精、删除
4. 网站数据统计
5. 任务执行和调度
6. 热帖排行
7. 生成长图
8. 文件上传到云服务器
9. 网站性能优化

#### 第八章
1. 单元测试
2. 项目监控
3. 项目部署
4. 项目总结
