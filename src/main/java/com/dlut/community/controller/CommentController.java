package com.dlut.community.controller;

import com.dlut.community.entity.Comment;
import com.dlut.community.entity.DiscussPost;
import com.dlut.community.entity.Event;
import com.dlut.community.event.EventProducer;
import com.dlut.community.service.CommentService;
import com.dlut.community.service.DiscussPostService;
import com.dlut.community.util.CommunityContant;
import com.dlut.community.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author wuyuhan
 * @date 2023/5/11 22:47
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityContant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private DiscussPostService discussPostService;


    /**
     * @param postId 为了重定向到详情页面
     * @param comment 从页面传入的参数(由name属性指定)，封装为Comment类
     * @return
     */
    @RequestMapping(path = "/add/{postId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("postId") int postId, Comment comment) {
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.insertComment(comment);
        int userId = comment.getUserId();

        // 触发评论消息
        Event event = new Event();
        event.setTopic(KAFKA_TOPIC_POST);
        event.setUserId(currentUserUtil.getUser().getId());
        event.setEntityType(comment.getEntityType());
        event.setEntityId(comment.getEntityId());
//                .setEntityUserId(comment.getTargetId()):
        event.setData("postId", postId);// 方便定位到评论的帖子
        // 如果评论的是帖子
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            // 获取到用户评论的那个帖子
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
            // 如果回复的是评论
        } else if (comment.getEntityType() == ENTITY_TYPE_REPALY) {
            // 获取到用户评论的那个回复
            Comment target = commentService.selectCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }

        eventProducer.sendMessage(event);
        // 通过路径得到
        return "redirect:/discuss/detail/" + postId;
    }
}
