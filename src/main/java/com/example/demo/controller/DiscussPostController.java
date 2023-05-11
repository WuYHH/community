package com.example.demo.controller;

import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.User;
import com.example.demo.service.DiscussPostService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommunityUtil;
import com.example.demo.util.HostHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author wuyuhan
 * @date 2023/5/9 14:33
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolderUtil hostHolderUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    // 返回json字符串时加上这个注解
    @ResponseBody
    public String addPost(String title, String content) {
        // 获取当前登录用户信息
        User user = hostHolderUtil.getUser();
        if (user == null) {
            return CommunityUtil.getJsonString(403, "你还没有登录！");
        }
        DiscussPost post = new DiscussPost();
        post.setTitle(title);
        post.setContent(content);
        post.setUserId(user.getId());
        post.setCreateTime(new Date());
        // 其他有默认值
        discussPostService.insertDiscussPost(post);
        return CommunityUtil.getJsonString(0, "发布成功！");
    }

    @RequestMapping(path = "/detail/{postId}", method = RequestMethod.GET)
    public String findPost(@PathVariable("postId") int postId, Model model) {
        // 显示帖子的信息
        DiscussPost discussPost = discussPostService.selectDiscussPost(postId);
        model.addAttribute("post", discussPost);
        // 显示用户的信息
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);
        return "/site/discuss-detail";
    }
}
