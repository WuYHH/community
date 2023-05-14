package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.Page;
import com.example.demo.entity.User;
import com.example.demo.service.CommentService;
import com.example.demo.service.DiscussPostService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommunityContant;
import com.example.demo.util.CommunityUtil;
import com.example.demo.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author wuyuhan
 * @date 2023/5/9 14:33
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityContant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    // 返回json字符串时加上这个注解
    @ResponseBody
    public String addPost(String title, String content) {
        // 获取当前登录用户信息
        User user = currentUserUtil.getUser();
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

    /**
     * Page会自动存入Model中，可以在页面中直接调用page
     * @param postId
     * @param model
     * @param page
     * @return
     */
    @RequestMapping(path = "/detail/{postId}", method = RequestMethod.GET)
    public String findPost(@PathVariable("postId") int postId, Model model, Page page) {
        // 显示帖子的信息
        DiscussPost discussPost = discussPostService.selectDiscussPost(postId);
        model.addAttribute("post", discussPost);
        // 显示用户的信息
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);

        // 设置底部分页信息
        page.setLimit(5); // 分页信息
        page.setPath("/discuss/detail/" + postId); // 每一页的链接
        page.setRows(discussPost.getCommentCount()); // 总记录数

        // 获取结果,获取实体类型为文章的id，即评论给文章
        List<Comment> commentList = commentService.selectComment(ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment: commentList) {
                // 封装当前评论
                Map<String, Object> commentVo = new HashMap<>();
                commentVo.put("comment", comment);
                // 获取当前评论的用户
                user = userService.findUserById(comment.getUserId());
                commentVo.put("user", user);
                // 获取当前评论的回复,两种回复：直接回复和回复某个人
                List<Comment> replayList = commentService.selectComment(ENTITY_TYPE_REPALY, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replayVoList = new ArrayList<>();
                if (replayList != null) {
                    for (Comment replay: replayList) {
                        Map<String, Object> replayVo = new HashMap<>();
                        // 回复
                        replayVo.put("replay", replay);
                        // 回复的作者
                        replayVo.put("user", userService.findUserById(replay.getUserId()));
                        // 回复的目标, 是回复人还是？
                        User target =
                                replay.getTargetId() == 0 ? null : userService.findUserById(replay.getTargetId());
                        replayVo.put("target", target);
                        replayVoList.add(replayVo);
                    }
                }
                commentVo.put("replays", replayVoList);
                // 点赞数量

                // 回复数量
                int replayCount = commentService.selectCountByEntity(ENTITY_TYPE_REPALY, comment.getId());
                commentVo.put("replayCount", replayCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }

}
