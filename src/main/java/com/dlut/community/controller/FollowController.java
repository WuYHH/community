package com.dlut.community.controller;

import com.dlut.community.service.FollowService;
import com.dlut.community.util.CommunityUtil;
import com.dlut.community.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wuyuhan
 * @date 2023/5/21 14:33
 */
@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private CurrentUserUtil currentUser;

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


}
