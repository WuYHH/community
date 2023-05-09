package com.example.demo.controller;

import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.Page;
import com.example.demo.service.DiscussPostService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/4/24 16:02
 */
@Controller
@RequestMapping()
public class HomePageController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path="/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        System.out.println(page);
        // SpringMVC自动实例化page, 且自动将page放入model中
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> lists = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        // 存放文章和用户信息
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (lists != null) {
            for (DiscussPost discussPost : lists) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                map.put("user", userService.findUserById(discussPost.getUserId()));
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPost", discussPosts);
        return "/index";
    }

}
