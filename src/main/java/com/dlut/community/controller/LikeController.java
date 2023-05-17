package com.dlut.community.controller;

import com.dlut.community.entity.User;
import com.dlut.community.service.LikeService;
import com.dlut.community.util.CommunityUtil;
import com.dlut.community.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String operateLogic(int entityType, int entityId) {
        User currentUser = currentUserUtil.getUser();
        likeService.like(entityType, entityId, currentUser.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("likeStatus", likeService.getLikeStatus(entityType, entityId, currentUser.getId()));
        map.put("likeCount", likeService.getLikeCount(entityType, entityId));
        return CommunityUtil.getJsonString(0, "获取点赞相关信息成功！", map);
    }
}
