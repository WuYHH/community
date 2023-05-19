package com.dlut.community.controller;

import com.dlut.community.entity.User;
import com.dlut.community.service.LikeService;
import com.dlut.community.service.UserService;
import com.dlut.community.util.CommunityUtil;
import com.dlut.community.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/5/17 16:31
 */
@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String operateLogic(int entityType, int entityId, int entityUserId) {
        User currentUser = currentUserUtil.getUser();
        // 点赞
        likeService.like(entityType, entityId, currentUser.getId(),entityUserId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeStatus", likeService.getLikeStatus(entityType, entityId, currentUser.getId()));
        map.put("likeCount", likeService.getLikeCount(entityType, entityId));
        return CommunityUtil.getJsonString(0, "获取点赞相关信息成功！", map);
    }

    /**
     * 进入个人主页
     * @return
     */
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfile(Model model, @PathVariable("userId") int userId) {
        User user = userService.findUserById(userId);
        model.addAttribute("userInfo", user);
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);
        return "/site/profile";
    }
}
