package com.dlut.community.service;

import com.dlut.community.dao.LoginTicketMapper;
import com.dlut.community.dao.UserMapper;
import com.dlut.community.entity.LoginTicket;
import com.dlut.community.entity.User;
import com.dlut.community.util.CommunityContant;
import com.dlut.community.util.CommunityUtil;
import com.dlut.community.util.MailClient;
import com.dlut.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author wuyuhan
 * @date 2023/4/24 16:08
 */
@Service
public class UserService implements CommunityContant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 使用redis优化
     * @param id
     * @return
     */
    public User findUserById(int id) {
        // 1.先找缓存
        User cacheUser = getCache(id);
        if (cacheUser == null) {
            // 2. 初始化缓存
            cacheUser = initCache(id);
        }
        return cacheUser;
//        return userMapper.selectById(id);
    }

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    /**
     * @param user
     * @return 注册可能会有多种结果，所以返回值为Map<></>
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        //
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }
        // 账号已经存在
        if (userMapper.selectByName(user.getUsername()) != null) {
            map.put("usernameMsg", "该账号已经存在");
            return map;
        }
        // 邮箱已经存在
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            map.put("emailMsg", "该邮箱已经存在");
            return map;
        }

        // 开始注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setCreateTime(new Date());
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        // 设置头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
//        mailClient.sendMail(user.getEmail(), "激活账号", content);
        // 注册成功则返回空的map
        return map;
    }

    // 激活

    public int activation(int userId, String activationCode) {
        User user = userMapper.selectById(userId);
        // 重复激活
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(activationCode)) {
            userMapper.updateStatus(userId, 1);
            // user状态变更
            updateCache(userId);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> res = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            res.put("usernameMsg", "账户名不能为空");
            return res;
        }

        if (StringUtils.isBlank(password)) {
            res.put("passwordMsg", "密码不能为空");
            return res;
        }

        User user = userMapper.selectByName(username);
        if (user == null) {
            res.put("usernameMsg", "该账户不存在！");
            return res;
        }

        if (user.getStatus() == 0) {
            res.put("usernameMsg", "该账户未激活！");
            return res;
        }

        if (user.getPassword().equals(CommunityUtil.md5(password + user.getSalt()))) {
            res.put("passwordMsg", "用户密码错误！");
            return res;
        }

        // 登录成功，发放凭证
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(user.getId());
        ticket.setStatus(0);
        ticket.setTicket(CommunityUtil.generateUUID());
        ticket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

//        loginTicketMapper.insertTicket(ticket);
        // 使用redis存储
        String ticketRedisKey = RedisKeyUtil.getTicket(ticket.getTicket());
        redisTemplate.opsForValue().set(ticketRedisKey, ticket);

        // 发放给客户端
        res.put("ticket", ticket.getTicket());

        return res;
    }

    public void logout(String ticket) {

//        loginTicketMapper.updateStatus(ticket, 1);
        String redisTicketKey = RedisKeyUtil.getTicket(ticket);
        LoginTicket loginTicket = (LoginTicket)redisTemplate.opsForValue().get(redisTicketKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisTicketKey, loginTicket);
    }

    public LoginTicket findLoginTicket(String ticket) {
        String redisTicketKey = RedisKeyUtil.getTicket(ticket);
        LoginTicket loginTicket = (LoginTicket)redisTemplate.opsForValue().get(redisTicketKey);
        return loginTicket;
    }

    public int updateHeader(int userId, String headerUrl) {
        updateCache(userId);
        return userMapper.updateHeader(userId, headerUrl);
    }

    /*
     使用Redis优化查询登录信息逻辑
    * */
    // 1.先从缓存中取值
    public User getCache(int userId) {
        String userKey = RedisKeyUtil.getUserKey(userId);
        User user = (User) redisTemplate.opsForValue().get(userKey);
        return user;
    }
    // 2.取不到时初始化缓存值

    public User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String userKey = RedisKeyUtil.getUserKey(userId);
        // 存放User对象
        redisTemplate.opsForValue().set(userKey, user, 36000, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时更新缓存值
    public void updateCache(int userId) {
        String userKey = RedisKeyUtil.getUserKey(userId);
        // 删除缓存
        redisTemplate.delete(userKey);
    }
}
