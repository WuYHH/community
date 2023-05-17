package com.dlut.community.controller;

import com.dlut.community.entity.Comment;
import com.dlut.community.service.CommentService;
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
public class CommentController {

    @Autowired
    private CommentService commentService;


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
        // 通过路径得到
        return "redirect:/discuss/detail/" + postId;
    }
}
