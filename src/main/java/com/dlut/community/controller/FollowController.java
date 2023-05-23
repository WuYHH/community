package com.dlut.community.controller;

import com.dlut.community.entity.Page;
import com.dlut.community.entity.User;
import com.dlut.community.service.FollowService;
import com.dlut.community.service.UserService;
import com.dlut.community.util.CommunityContant;
import com.dlut.community.util.CommunityUtil;
import com.dlut.community.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/5/21 14:33
 */
@Controller
public class FollowController implements CommunityContant {

    @Autowired
    private FollowService followService;

    @Autowired
    private CurrentUserUtil currentUser;

    @Autowired
    private UserService userService;

    /**
     * 通过异步请求改变关注状态
     * @param entityType
     * @param entityId
     * @return
     */
    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        int userId = currentUser.getUser().getId();
        followService.follow(userId, entityType, entityId);
        return CommunityUtil.getJsonString(0, "关注成功！");
    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        int userId = currentUser.getUser().getId();
        followService.unfollow(userId, entityType, entityId);
        return CommunityUtil.getJsonString(0, "已取消成功！");
    }

    @RequestMapping(path = "/followee/{userId}", method = RequestMethod.GET)
    public String getFollowerList(@PathVariable("userId") int userId, Model model, Page page) {
        // 配置分页信息
        page.setLimit(5);
        page.setPath("/followee/" + userId);
        page.setRows((int) followService.getFolloweeCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> followeeList = followService.getFolloweeList(userId, page.getOffset(), page.getLimit());
        model.addAttribute("targetUsers", followeeList);
        User selectUser = userService.findUserById(userId);
        if (selectUser != null) {
            model.addAttribute("selectUser", selectUser);
        }
        return "/site/followee";
    }
}
