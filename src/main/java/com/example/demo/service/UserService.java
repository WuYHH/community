package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.util.ActivationStatus;
import com.example.demo.util.CommunityUtil;
import com.example.demo.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author wuyuhan
 * @date 2023/4/24 16:08
 */
@Service
public class UserService implements ActivationStatus {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;
    public User findById(int id) {
        return userMapper.selectById(id);
    }

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
        mailClient.sendMail(user.getEmail(), "激活账号", content);
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
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }
}
