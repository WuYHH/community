package com.example.demo;

import com.example.demo.dao.DiscussPostMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/4/22 22:43
 */
@SpringBootTest
@ContextConfiguration(classes = Demo3Application.class)
public class MybatisTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testSelect() {
        User user = userMapper.selectById(101);
        System.out.println(user);
        user = userMapper.selectByName("liubei");
        System.out.println(user);
        user = userMapper.selectByEmail("nowcoder1@sina.com");
        System.out.println(user);
    }

    @Test
    void testInsert() {
        User user = new User();
        user.setUsername("wuyuhan");
        user.setEmail("yuhann.w@outlook.com");
        user.setPassword("1234");
        user.setSalt("abc");
        user.setHeaderUrl("http://www.test.com");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println("affect rows: " + rows);
    }

    @Test
    void testUpdate() {
        int rows = userMapper.updateHeader(150, "http://150");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "123456");
        System.out.println(rows);

        rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);
    }

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    void testDiscussPost() {
        List<DiscussPost> discussPosts =
                discussPostMapper.selectDiscussPosts(101, 0, 10);
        for (DiscussPost discussPost: discussPosts) {
            System.out.println(discussPost);
        }
        int rows = discussPostMapper.selectDiscussPostRows(101);
        System.out.println(rows);
    }
}
