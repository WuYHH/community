# community

### 第一章
### 第二章
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

### 第三章
1. **过滤敏感词**
- 逻辑：
利用**Trie字典树**，在编译期构造方法调用后**初始化**敏感词文件，构建字典树后调用相应的方法进行敏感词过滤，在待
**过滤文本上选择双指针**+**Trie树单指针**进行过滤。
- 技术
  - @PostContrustor注解标记初始化方法
  - Trie树数据结构：Map<Character, Trie>
  - 双指针
  - 特殊符号处理
2. **发布帖子**
- 逻辑
用户登录状态下，显示发布按钮，点击发布后，填入标题和内容后，异步发送,返回响应结果；
- 技术
  - AJAX $.post请求(请求路径，请求内容，响应函数)
  - HostHolderUtil获取用户登录状态
  - HtmlUtil.
  - 敏感词过滤
3. **帖子详情**
- 逻辑
点击标题，跳转到详情页面
- 技术
  - 利用map数据前端获取帖子id
  - @PathVariable接收id
  - model封装post数据和user数据
4. **事务管理**
5. **显示评论**
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
  - Thymeleaf的应用
6. **添加评论**
- 技术:
  - 事务使用的场景
  - 事务的隔离级别
  - Spring事务注解
  - 前端属性name通过表单发送时，可以组合为一个Comment类，controller可以定义Comment类为形参。
  - 隐藏标签``<input type="inhidden">``可以用来发送属性。
7. **私信列表**
- 逻辑：
  - conversation_id由**from_id**和**to_id**组成，是表的**冗余字段**，构成时默认111_222,即小号id在前，大号id在后
  - 一次会话(conversation)包含多次私信(message)，显示的时候，只显示最近的一条消息（判断最近？max(id)或者createTime）
  - 当前用户既是from_id，又是to_id
  - Message既是会话，又是消息
  - 系统私信：from_id = 1; 用户私信：from_id != 1
  - 显示某个会话的私信详情时，从from_id中提取用户作为当前私信的发出者
- 技术
  - 获取只显示最新消息的会话列表的SQL语句（嵌套查询）
  - 查询详情时，需要在url连接带上必要的id信息，例如/letter/list/{conversationId}
  - thymeleaf字符串的拼接|xxx|
8. **发送私信**
- 逻辑
  - 将未读状态修改为已读状态的实现
9. **统一处理异常**
- 逻辑：
  - 异常将会从dao层传递到controller层
  - 在controller统一处理**所有的异常**，使用页面展示**客户端异常**信息和**服务端异常**信息。
- 技术：
  - 静态文件放在/template/error下，必须是该文件名
  - 状态码文件必须为404, 501的html格式
  - 人为处理异常的话，需要手动重定向到500页面；否则，Spring自动处理；
  - @ControllerAdvice(annotations = Controller.class)注解标记异常处理类，@ExceptionHandler标记异常处理方法
  - 服务器异常分类：
    - AJAX异步请求异常：
      - 从Request的请求头判断
      - 使用Response的输出流，构造JSON字符串[包括**状态码**和**状态信息**]到页面
    - 同步请求异常
10. **统一记录日志**
- 逻辑:
  - joinpoint：可以织入的地方
  - pointcut：确定的要织入的地方（joincut的子集）
  - 注意织入的时机
- 技术：
  - JointPoint可以确定织入的方法名等一系列信息
### 第四章
1. Redis入门
- 应用：缓存、排行榜、计数器、社交网络、消息队列
2. Spring整合Redis
- 技术:
  - 编程式事务的编写
  - 批量访问Redis
  - key的命名->>[a:b]
3. 点赞
- 逻辑：
  - 异步的方式进行请求：
    - 请求参数（ajax）：entityType、entityId【也可以直接从详情页面获取】
    - 非常重要的**两个变量**：
      - 赞的数量，存放在不同的entity里
      - 赞的状态（当前用户对当前实体点赞），存放在不同的entity里
        - 已赞
        - 未赞
    - 刷新**点赞状态**和**点赞数量**
      - 局部刷新：用户点击后，AJAX请求走/like, 更新用户点击后的状态
      - 全局刷新：刷新页面后，走/discuss/detail/{postId}，Model对象更新赞的状态和数量
      - 如果仅有局部刷新，用户刷新后会再次失去所有值
    - 用set存放实体的点赞，因为set不重复，所以只会有唯一的userId。
    - 实体点赞的key的设计：`like:entity: entityType:entityId  --> set(userId),`
4. 我收到的赞
- 逻辑：
  - 用户点赞的同时，更新被点赞用户的点赞数量，采用redis的事务来解决
  - 点赞或者取消赞同时需要：currentUser、entityType、entityId和targetId,其中entityType和entityId捆在一起
  - 点赞实体：
    - 帖子点赞
    - 评论点赞
  - **key_value的构造**，参考"关注、取消关注"中的key的设计。
  - 在页面构造url请求时，如果想带上当前页面的信息，如userId,形式必须使用``|XX|``拼接，形式如下：
    - ``th:href="@{|/profile/${user.id}|}"``, 然后controller使用@PathVariable接收参数。
5. 关注、取消关注
- 逻辑：
用户关注某个人的同时，用户的关注列表更新，被关注列表的粉丝更新，必须同时进行。
  - 关注：
    - Follower(粉丝)：构造Redis ID，其中redis采用zset数据结构，分数score为日期信息
      某个用户关注的实体：`followee:userId:entityType --> zset(entityId, nowDate)`
    - Followee(目标)：构造ID，同样采用zset
      某个实体类型拥有的粉丝(人)：`follower:entityType:entityId --> zset(userId, nowDate)`
- 技术：
  - 当前登录用户和访问用户界面详情时同一个人的话，采用th:if语句判断，这里获取登录用户时，采用的是LoginTicketInterceptor中的数据，而**不是自己**在LikeController里model加入的
    - LoginInterceptor拦截所有controller执行后的请求，加入当前用户，所有页面都可以共享登录用户
    - ThreadLocal获取当前用户
6. 关注列表、粉丝列表
- 逻辑：查询列表时需要带上当前用户的id，针对列表的每一项，分析Map中需要携带的数据，构造List<Map>存入Model中
- 技术：
  - 获取zset中的**元素**和对应的**分数**，这里分数为**关注时间**，关注时间是从redis中取出来的
  - 获取关注状态，复用之前的逻辑，判定当前登录用户a查询的用户b的所有的关注人c，如果a关注了c那么显示已关注，否则未关注。
  - 前端页面同样采用ajax更新已关注还是未关注
7. 优化登录模块
- 逻辑：
  - 采用redis优化验证码逻辑：
    - RedisKeyUtil构建key前缀的工具
    - 用户点击登录按钮，发送"/login"的GET请求，跳转到"/site/login/"页面
    - 显示登录页面，验证码自动发送url请求"/kaptcha"，到LoginController中
    - 获取验证码文本text，随机生成UUID，构建cookie，通过response为浏览器发送凭证id，并利用UUID构建redisKey，将验证码text存入redis，设置过期时间，然后response通过输出流写入到浏览器端，形成验证码图片。
    - 用户填写完表单，点击提交按钮时，发送发送"/login"的POST请求，到LoginController中，通过@CookieValue获取刚才发送给浏览器的cookie，验证cookie，如果该cookie存在，构建redisKey，从redis中取出验证码文本值。
  - redis存储登录凭证，不再使用login_ticket
    - 不再使用LoginTicketMapper
    - 不对Redis设置过期时间，让redis存储整个LoginTicket对象<key, LoginTicket>
  - redis优化查询用户登录信息
    - 三种逻辑
      - 优先从缓存中取值。
      - 取不到值时从数据库中取，然后存入redis中，初始化缓存数据。
      - 更新时删除缓存（而非更改缓存），如用户状态变更、用户更换头像等。
### 第五章
1. 阻塞队列
2. kafka入门
- 特点：
   3. 高吞吐量
   4. 消息持久化，持久化到硬盘，顺序读取性能>内存随机读取的性能
   5. 异步处理，不会阻塞当前线程
- 概念：
   5. broker：服务器
   6. zookeeper：管理集群（单独安装或者集成）
   7. Topic：主题
   8. Partition：Topic的分区
   9. offset：消息在partition的位置
   10. Leader Replica：副本
   11. 
3. Spring整合kafka
- 技术：自动监听：消费者中配置`@KafkaListener(topics = {"test"}, test为主题名称)`
4. 发送系统通知
- 逻辑:
  - 面向**事件**编程，封装主题、数据、冗余数据，整个事件将以**JSON格式**传输。
    - topic
    - userId：哪个用户id发起的消息
    - entityType
    - entityId
    - entityUserId：实体entity所属的用户id
    - data(冗余数据)
  - 消费者获取到事件后，需要将事件Event-->消息Message
    - 构建消息content，增加事件的额外信息
  - 构建评论消息时，需要增加额外数据`postId`,方便跳转到帖子详情页
  - 三种事件处理
    - 点赞事件：
      - 需要跳转帖子详情页面，需要`postId`信息，需要从前端页面ajax请求传入该信息（方法增加int postId形参）
      - 其他信息与异步方法携带的参数一致
      - 有附加数据data，内容为`postId`
    - 关注事件：
      - entityUserId 与 entityId一致，因为目前只有关注人的逻辑
      - 无附加数据 data，因为不需要跳转
    - 评论事件
      - 需要跳转帖子详情页面，需要`postId`信息，该信息通过区分**评论种类**加入到event中，有**两种评论**：
        - 评论回复：CommentService获取回复Comment，通过该类获取被回复的用户Id（发布该评论的用户），注入entityUserId
        - 评论帖子：DiscussPostService获取帖子DiscussPost，通过该类获取被回复的用户Id(发布该帖子的用户)，注入entityUserId
      - 有附加数据data，内容为`postId`
  - 三种数据区分：
    - 事件Event
    - 消息Message
      - xxx 数据库中的字段
      - 内容Content，需要拼成JSON字符串，为了构造消息：用户 xxxx 关注/点赞/评论 了你/你的帖子 
        - userId
        - entityType
        - entityId
        - data（如果能从Event中获取）
  - 整体数据流程：
    - 基于事件驱动，构建生产者和消费者，在每一个业务逻辑，点赞/关注/评论逻辑中调用生产者，正确的封装Event事件（是否封装data用于跳转、评论中的entityUserId是postid
    还是commentId）
    - 消费者的逻辑为根据获得的Event事件的Topic主题构建Message信息，通过Service插入到数据表中。
  - 在消费者中直接调用Service，会导致ServiceLogAspect中的request请求为空，因为没有走Controller层导致attributes为空。
    
5. 显示系统通知

### 第六章
1. Elasticsearch
2. Spring整合elasticsearch
3. 开发社区搜索功能

### 第七章
1. Spring security
2. 权限控制
3. 置顶、加精、删除
4. 网站数据统计
5. 任务执行和调度
6. 热帖排行
7. 生成长图
8. 文件上传到云服务器
9. 网站性能优化

### 第八章
1. 单元测试
2. 项目监控
3. 项目部署
4. 项目总结

#### idea调试技巧
1. 使用debug模式启动(每一次)
2. 打断点，或者选择某些断点执行，侧边栏：view breakpoints
3. 如果想让前端页面在**执行断点**后继续向下执行，需要点击 resume applcation, 此时不必重新启动

#### Thymeleaf使用汇总
1. 使用循环：th:each
````html
<ul>
  // 必须写在<li>标签上
  <li th:each="student:${students}">
  </li>
</ul>

````
2. 任意标签，任意属性，拼接变量：th:X="|mmmm|"
````html
<input  th:placeholder="|回复${vo.user.username}|">
````

3. 工具类：date
````html
<p th:text="${#dates.format(commentVo.comment.createTime, "yyyy-MM-dd HH:mm:ss")}">
````
4. 判断语句，如果值为true，**显示标签**；
````html
<div th:if="${user.isAdmin}">
    <p>Welcome, admin!</p>
</div>
如果user对象的isAdmin属性为true，则显示欢迎消息。
````
5. 在``th:each``中的Stat对象，如果不显式的定义集合的第二个变量iterStat，则默认第一个参数加Stat后缀
````html
<tr th:each="prod,iterStat : ${prods}" th:class="${iterStat.odd}? 'odd'">
<tr th:each="prod : ${prods}" th:class="${prodStat.odd}? 'odd'">
````
th:each属性中，定义了如下状态变量：
- index 属性是当前 迭代器索引（iteration index），从0开始
- count 属性是当前 迭代器索引（iteration index），从1开始
- size 属性是迭代器元素的总数
- current 是当前 迭代变量（iter variable）
- even/odd 判断当前迭代器是否是 even 或 odd
- first 判断当前迭代器是否是第一个
- last 判断当前迭代器是否是最后

6. @{}和${}
   7. @{}：用于创建URL链接 
   8. @{#...}：用于引用模板中的片段
   9. ${...}：用于访问页面上下文中的变量或者Spring MVC Model中的属性
7. 使用`|XXX|`拼接字符串，可以拼接连接，可以拼接class
```html
<a th:href="@{|/letter/detail/${map.conversation.conversationId}|}"></a>
```